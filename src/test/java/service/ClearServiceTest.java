package service;

import dao.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.ClearResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * ClearService object to use for testing
     */
    private static ClearService clearService;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        clearService = new ClearService();

        // Prepare the database for testing
        try(Connection conn = db.openConnection()) {
            // Initialize DAOs
            ItemDAO iDao = new ItemDAO(conn);
            CategoryDAO cDao = new CategoryDAO(conn);
            ItemListDAO lDao = new ItemListDAO(conn);
            RecipeDAO rDao = new RecipeDAO(conn);
            CollectionDAO coDao = new CollectionDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            AuthtokenDAO aDao = new AuthtokenDAO(conn);

            // Clear the database
            iDao.clear();
            cDao.clear();
            lDao.clear();
            rDao.clear();
            coDao.clear();
            uDao.clear();
            aDao.clear();

            // Create test data
            Item item = new Item("itemID", "itemName", "username", "listID");
            Category category = new Category("categoryID", "categoryName",
                    "username", "listID");
            ItemList list = new ItemList("listID", "listName", "username");
            Recipe recipe = new Recipe("recipeID", "recipeName", "username", "collectionID");
            Collection collection = new Collection("collectionID", "collectionName", "username");
            User user = new User("username", "password", "firstname", "lastname");
            Authtoken authtoken = new Authtoken("authtoken", "username");

            // Insert test data into the database
            iDao.insert(item);
            cDao.insert(category);
            lDao.insert(list);
            rDao.insert(recipe);
            coDao.insert(collection);
            uDao.insert(user);
            aDao.insert(authtoken);

            // Commit changes to the database
            db.closeConnection(true);

        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
        }
    }

    @AfterEach
    public void tearDown() {
        // Close the database connection and rollback any changes made
        db.closeConnection(false);
    }

    @Test
    public void clearDatabasePass() throws DataAccessException {
        // Verify that the database contains the test data before clearing
        Connection conn = db.openConnection();

        ItemDAO iDao = new ItemDAO(conn);
        CategoryDAO cDao = new CategoryDAO(conn);
        ItemListDAO lDao = new ItemListDAO(conn);
        RecipeDAO rDao = new RecipeDAO(conn);
        CollectionDAO coDao = new CollectionDAO(conn);
        UserDAO uDao = new UserDAO(conn);
        AuthtokenDAO aDao = new AuthtokenDAO(conn);

        assertNotNull(iDao.find("itemID"), "Test Item not added to database");
        assertNotNull(cDao.find("categoryID"), "Test Category not added to database");
        assertNotNull(lDao.find("listID"), "Test List not added to database");
        assertNotNull(rDao.find("recipeID"), "Test Recipe not added to database");
        assertNotNull(coDao.find("collectionID"), "Test Collection not added to database");
        assertNotNull(uDao.find("username"), "Test User not added to database");
        assertNotNull(aDao.find("authtoken"), "Test Authtoken not added to database");

        db.closeConnection(false);

        // Clear the database
        ClearResult result = clearService.clearDatabase();

        // Verify that a successful result was returned
        assertEquals("Clear succeeded.", result.getMessage(), "Incorrect message returned");
        assertTrue(result.isSuccess(), "Clear failed");

        // Verify that the database is empty after clearing
        conn = db.openConnection();

        iDao = new ItemDAO(conn);
        cDao = new CategoryDAO(conn);
        lDao = new ItemListDAO(conn);
        rDao = new RecipeDAO(conn);
        coDao = new CollectionDAO(conn);
        uDao = new UserDAO(conn);
        aDao = new AuthtokenDAO(conn);

        assertNull(iDao.find("itemID"), "Test Item not removed from database");
        assertNull(cDao.find("categoryID"), "Test Category not removed from database");
        assertNull(lDao.find("listID"), "Test List not removed from database");
        assertNull(rDao.find("recipeID"), "Test Recipe not removed from database");
        assertNull(coDao.find("collectionID"), "Test Collection not removed from database");
        assertNull(uDao.find("username"), "Test User not removed from database");
        assertNull(aDao.find("authtoken"), "Test Authtoken not removed from database");
    }
}
