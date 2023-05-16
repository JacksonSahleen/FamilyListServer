package dao;

import model.Recipe;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit tests for the RecipeDAO class.
 */
public class RecipeDAOTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * Recipe object to use for testing
     */
    private static Recipe exRecipe;

    /*
     * DAO object to use for testing
     */
    private static RecipeDAO rDao;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();

        // Create an example Recipe object
        exRecipe = new Recipe("recipeID", "name", "owner", "description");

        // Create a new RecipeDAO
        Connection conn = db.getConnection();
        rDao = new RecipeDAO(conn);

        // Wipe the database clean
        rDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Close the database connection and rollback any changes made
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Insert the example Recipe into the database
        rDao.insert(exRecipe);

        // Get the example Recipe back from the database
        Recipe compareTest = rDao.find(exRecipe.getId());

        // Verify that the Recipe we inserted and the Recipe we got back are the same
        assertNotNull(compareTest);
        assertEquals(exRecipe, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert the example Recipe into the database
        rDao.insert(exRecipe);

        // Try to insert the same Recipe again and verify that it throws an exception
        assertThrows(DataAccessException.class, () -> rDao.insert(exRecipe));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Insert the example Recipe into the database
        rDao.insert(exRecipe);

        // Verify that the Recipe we inserted and the Recipe we got back are the same
        Recipe compareTest = rDao.find(exRecipe.getId());
        assertNotNull(compareTest);
        assertEquals(exRecipe, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Try to get a Recipe that doesn't exist in the database
        Recipe compareTest = rDao.find(exRecipe.getId());

        // Verify that the Recipe we got back is null
        assertNull(compareTest);
    }

    @Test
    public void updatePass() throws DataAccessException {
        // Insert the example Recipe into the database
        rDao.insert(exRecipe);

        // Verify that the Recipe we inserted and the Recipe we got back are the same
        Recipe compareTest = rDao.find(exRecipe.getId());
        assertNotNull(compareTest);
        assertEquals(exRecipe, compareTest);

        // Update the example Recipe values
        exRecipe.setName("newName");
        exRecipe.setOwner("newOwner");
        rDao.update(exRecipe);

        // Verify that the Recipe we updated now has the updated values
        compareTest = rDao.find(exRecipe.getId());
        assertNotNull(compareTest);
        assertEquals(compareTest.getName(), "newName");
        assertEquals(compareTest.getOwner(), "newOwner");
    }

    @Test
    public void updateFail() throws DataAccessException {
        // Update a Recipe that doesn't exist in the database and verify that nothing happens
        Recipe fakeRecipe = new Recipe("fakeID", "fakeName", "fakeOwner", "fakeDescription");
        rDao.update(fakeRecipe);

        // Verify that the updated Recipe still does not exist
        Recipe compareTest = rDao.find(fakeRecipe.getId());
        assertNull(compareTest);
    }

    @Test
    public void removePass() throws DataAccessException {
        // Insert the example Recipe into the database
        rDao.insert(exRecipe);

        // Verify that the Recipe we inserted and the Recipe we got back are the same
        Recipe compareTest = rDao.find(exRecipe.getId());
        assertNotNull(compareTest);
        assertEquals(exRecipe, compareTest);

        // Insert a second Recipe into the database
        Recipe exRecipe2 = new Recipe("recipeID2", "name2", "owner2", "description2");
        rDao.insert(exRecipe2);

        // Verify that the second Recipe we inserted and the Recipe we got back are the same
        compareTest = rDao.find(exRecipe2.getId());
        assertNotNull(compareTest);
        assertEquals(exRecipe2, compareTest);

        // Remove the example Recipe from the database
        rDao.remove(exRecipe, exRecipe.getOwner());

        // Verify that the example Recipe has been removed from the database but the second Recipe still exists
        compareTest = rDao.find(exRecipe.getId());
        assertNull(compareTest);
        compareTest = rDao.find(exRecipe2.getId());
        assertNotNull(compareTest);
        assertEquals(exRecipe2, compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        // Remove a Recipe that doesn't exist in the database and verify that nothing happens
        rDao.remove(exRecipe, exRecipe.getOwner());

        // Verify that the Recipe still does not exist
        Recipe compareTest = rDao.find(exRecipe.getId());
        assertNull(compareTest);
    }

    @Test
    public void removeOtherUserAccess() throws DataAccessException {
        // Insert list into database
        rDao.insert(exRecipe);

        // Share list with additional user
        rDao.share(exRecipe, "user1");

        // Remove list from database for the original owner
        rDao.remove(exRecipe, "owner");

        // Verify that the list still exists in the database
        Recipe compareTest = rDao.find(exRecipe.getId());
        assertNotNull(compareTest);
        assertEquals(exRecipe, compareTest);

        // Verify that the other user still has access to the list
        List<String> users = rDao.findUsersWithAccess(exRecipe.getId());
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("user1", users.get(0));
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Insert the example Recipe into the database
        rDao.insert(exRecipe);

        // Clear the database and then try to get the example Recipe back
        rDao.clear();
        Recipe compareTest = rDao.find(exRecipe.getId());

        // Verify that the Recipe we got back is null
        assertNull(compareTest);
    }

    @Test
    public void sharePass() throws DataAccessException {
        // Insert list into database
        rDao.insert(exRecipe);

        // Share list with additional users
        rDao.share(exRecipe, "user1");
        rDao.share(exRecipe, "user2");

        // Get list of users with access to list
        List<String> users = rDao.findUsersWithAccess(exRecipe.getId());

        // Verify that the list of users contains the additional user
        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.contains("owner"));
        assertTrue(users.contains("user1"));
        assertTrue(users.contains("user2"));

    }
    @Test
    public void shareFail() throws DataAccessException {
        // Insert list into database
        rDao.insert(exRecipe);

        // Share list with additional users
        rDao.share(exRecipe, "user1");
        rDao.share(exRecipe, "user2");

        // Get list of users with access to list
        List<String> users = rDao.findUsersWithAccess(exRecipe.getId());

        // Verify that the list of users contains the additional users
        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.contains("owner"));
        assertTrue(users.contains("user1"));
        assertTrue(users.contains("user2"));

        // Try to share list with user that already has access
        rDao.share(exRecipe, "user1");

        // Get list of users with access to list
        users = rDao.findUsersWithAccess(exRecipe.getId());

        // Verify that the list of users hasn't changed
        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.contains("owner"));
        assertTrue(users.contains("user1"));
        assertTrue(users.contains("user2"));

    }

    @Test
    public void findUserRecipesPass() throws DataAccessException {
        // Insert a user into the database
        UserDAO uDao = new UserDAO(db.getConnection());
        User exUser = new User("username", "password", "firstName", "lastName");
        uDao.insert(exUser);

        // Insert multiple recipes for the user into the database
        RecipeDAO rDao = new RecipeDAO(db.getConnection());
        Recipe exRecipe1 = new Recipe("recipeID1", "recipe1", exUser.getUsername(), "1st test recipe");
        Recipe exRecipe2 = new Recipe("recipeID2", "recipe2", exUser.getUsername(), "2nd test recipe");
        rDao.insert(exRecipe1);
        rDao.insert(exRecipe2);

        // Get the recipes for the user from the database
        List<String> recipes = rDao.findUserRecipes(exUser.getUsername());

        // Verify that the recipes returned are the same as the ones inserted
        assertNotNull(recipes);
        assertEquals(2, recipes.size());
        assertTrue(recipes.contains(exRecipe1.getId()));
        assertTrue(recipes.contains(exRecipe2.getId()));
    }

    @Test
    public void findUserRecipesFail() throws DataAccessException {
        // Get the recipes for a user that doesn't exist in the database
        User exUser = new User("username", "password", "firstName", "lastName");
        List<String> recipes = rDao.findUserRecipes(exUser.getUsername());

        // Verify that the recipes returned are null
        assertNull(recipes);
    }
}
