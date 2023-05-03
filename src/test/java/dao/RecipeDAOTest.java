package dao;

import model.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

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
        rDao.remove(exRecipe.getId());

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
        rDao.remove(exRecipe.getId());

        // Verify that the Recipe still does not exist
        Recipe compareTest = rDao.find(exRecipe.getId());
        assertNull(compareTest);
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
}
