package dao;

import model.Collection;
import model.Recipe;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CollectionDAOTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * Collection object to use for testing
     */
    private static Collection exCollection;

    /*
     * DAO object to use for testing
     */
    private static CollectionDAO cDao;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();

        // Create an example Collection object
        exCollection = new Collection("CollectionID", "name", "owner");

        // Create a new CollectionDAO
        Connection conn = db.getConnection();
        cDao = new CollectionDAO(conn);

        // Wipe the database clean
        UserDAO uDao = new UserDAO(conn);
        uDao.clear();
        cDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Close the database connection and rollback any changes made
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Get the example Collection back from the database
        Collection compareTest = cDao.find(exCollection.getId());

        // Verify that the Collection we inserted and the Collection we got back are the same
        assertNotNull(compareTest);
        assertEquals(exCollection, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Try to insert the same Collection again and verify that it throws an exception
        assertThrows(DataAccessException.class, () -> cDao.insert(exCollection));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Verify that the Collection we inserted and the Collection we got back are the same
        Collection compareTest = cDao.find(exCollection.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Try to get a Collection that doesn't exist in the database
        Collection compareTest = cDao.find(exCollection.getId());

        // Verify that the Collection we got back is null
        assertNull(compareTest);
    }

    @Test
    public void updatePass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Verify that the Collection we inserted and the Collection we got back are the same
        Collection compareTest = cDao.find(exCollection.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection, compareTest);

        // Update the example Collection
        String newName = "newName";
        String newOwner = "newOwner";
        exCollection.setName(newName);
        exCollection.setOwner(newOwner);
        cDao.update(exCollection);

        // Verify that the Collection we updated now has the updated values
        compareTest = cDao.find(exCollection.getId());
        assertNotNull(compareTest);
        assertEquals(compareTest.getName(), newName);
        assertEquals(compareTest.getOwner(), newOwner);
    }

    @Test
    public void updateFail() throws DataAccessException {
        // Update a Collection that doesn't exist in the database and verify that nothing happens
        Collection fakeCollection = new Collection("fakeID", "fakeName", "fakeOwner");
        cDao.update(fakeCollection);

        // Verify that the updated Collection still does not exist
        Collection compareTest = cDao.find(fakeCollection.getId());
        assertNull(compareTest);
    }

    @Test
    public void removePass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Verify that the Collection we inserted and the Collection we got back are the same
        Collection compareTest = cDao.find(exCollection.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection, compareTest);

        // Insert a second Collection into the database
        Collection exCollection2 = new Collection("CollectionID2", "name2", "owner2");
        cDao.insert(exCollection2);

        // Verify that the second Collection we inserted and the Collection we got back are the same
        compareTest = cDao.find(exCollection2.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection2, compareTest);

        // Remove the example Collection from the database
        cDao.remove(exCollection);

        // Verify that the example Collection has been removed from the database but the second Collection still exists
        compareTest = cDao.find(exCollection.getId());
        assertNull(compareTest);
        compareTest = cDao.find(exCollection2.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection2, compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        // Remove a Collection that doesn't exist in the database and verify that nothing happens
        cDao.remove(exCollection);

        // Verify that the Collection still does not exist
        Collection compareTest = cDao.find(exCollection.getId());
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Clear the database and then try to get the example Collection back
        cDao.clear();
        Collection compareTest = cDao.find(exCollection.getId());

        // Verify that the Collection we got back is null
        assertNull(compareTest);
    }

    @Test
    public void addCollectionRecipePass() throws DataAccessException {
        // Insert a collection and multiple recipes into the database
        cDao.insert(exCollection);

        Recipe exRecipe1 = new Recipe("RecipeID1", "name1", "owner1", "first test recipe");
        Recipe exRecipe2 = new Recipe("RecipeID2", "name2", "owner2", "second test recipe");

        RecipeDAO rDao = new RecipeDAO(db.getConnection());
        rDao.insert(exRecipe1);
        rDao.insert(exRecipe2);

        // Add the recipes to the collection
        cDao.addCollectionRecipe(exCollection.getId(), exRecipe1.getId());
        cDao.addCollectionRecipe(exCollection.getId(), exRecipe2.getId());

        // Get the recipes for the collection from the database
        List<String> recipes = cDao.findCollectionRecipes(exCollection.getId());

        // Verify that the recipes returned are the same as the ones inserted
        assertNotNull(recipes);
        assertEquals(recipes.size(), 2);
        assertTrue(recipes.contains(exRecipe1.getId()));
        assertTrue(recipes.contains(exRecipe2.getId()));
    }

    @Test
    public void addCollectionRecipeFail() throws DataAccessException {
        // Insert a recipe into the database
        RecipeDAO rDao = new RecipeDAO(db.getConnection());
        Recipe exRecipe = new Recipe("RecipeID", "name", "owner", "test recipe");
        rDao.insert(exRecipe);

        // Verify that an exception was thrown when trying to add a recipe to a collection that doesn't exist
        assertThrows(DataAccessException.class, () -> cDao.addCollectionRecipe(exCollection.getId(), exRecipe.getId()));
    }

    @Test
    public void removeCollectionRecipePass() throws DataAccessException {
        // Insert a collection and a recipe into the database
        cDao.insert(exCollection);

        Recipe exRecipe = new Recipe("RecipeID", "name", "owner", "test recipe");

        RecipeDAO rDao = new RecipeDAO(db.getConnection());
        rDao.insert(exRecipe);

        // Add the recipe to the collection
        cDao.addCollectionRecipe(exCollection.getId(), exRecipe.getId());

        // Get the recipes for the collection from the database
        List<String> recipes = cDao.findCollectionRecipes(exCollection.getId());

        // Verify that the recipe returned is the same as the one inserted
        assertNotNull(recipes);
        assertEquals(recipes.size(), 1);
        assertTrue(recipes.contains(exRecipe.getId()));

        // Remove the recipe from the collection
        cDao.removeCollectionRecipe(exCollection.getId(), exRecipe.getId());

        // Get the recipes for the collection from the database
        recipes = cDao.findCollectionRecipes(exCollection.getId());

        // Verify that no recipes are returned
        assertNull(recipes);
    }

    @Test
    public void removeCollectionRecipeFail() throws DataAccessException {
        // Insert a collection and a recipe into the database
        cDao.insert(exCollection);

        Recipe exRecipe = new Recipe("RecipeID", "name", "owner", "test recipe");

        RecipeDAO rDao = new RecipeDAO(db.getConnection());
        rDao.insert(exRecipe);

        // Add the recipe to the collection
        cDao.addCollectionRecipe(exCollection.getId(), exRecipe.getId());

        // Get the recipes for the collection from the database
        List<String> recipes = cDao.findCollectionRecipes(exCollection.getId());

        // Verify that the recipe returned is the same as the one inserted
        assertNotNull(recipes);
        assertEquals(recipes.size(), 1);
        assertTrue(recipes.contains(exRecipe.getId()));

        // Try to remove a recipe that is not in the collection
        cDao.removeCollectionRecipe(exCollection.getId(), "fakeID");

        // Get the recipes for the collection from the database
        recipes = cDao.findCollectionRecipes(exCollection.getId());

        // Verify that the recipe returned is the same as the one inserted
        assertNotNull(recipes);
        assertEquals(recipes.size(), 1);
        assertTrue(recipes.contains(exRecipe.getId()));
    }

    @Test
    public void findUserCollectionsPass() throws DataAccessException {
        // Insert a user into the database
        UserDAO uDao = new UserDAO(db.getConnection());
        User exUser = new User("username", "password", "firstName",
                "lastName", ZonedDateTime.now());
        uDao.insert(exUser);

        // Insert multiple collections for the user into the database
        Collection exCollection1 = new Collection("collection1", "name1", exUser.getUsername());
        Collection exCollection2 = new Collection("collection2", "name2", exUser.getUsername());
        cDao.insert(exCollection1);
        cDao.insert(exCollection2);

        // Get the collections for the user from the database
        List<String> collections = cDao.findUserCollections(exUser.getUsername());

        // Verify that the collections returned are the same as the ones inserted
        assertNotNull(collections);
        assertEquals(2, collections.size());
        assertTrue(collections.contains(exCollection1.getId()));
        assertTrue(collections.contains(exCollection2.getId()));
    }

    @Test
    public void findUserCollectionsFail() throws DataAccessException {
        // Get the collections for a user that doesn't exist in the database
        User exUser = new User("username", "password", "firstName",
                "lastName", ZonedDateTime.now());
        List<String> lists = cDao.findUserCollections(exUser.getUsername());

        // Verify that the collections returned are null
        assertNull(lists);
    }
}
