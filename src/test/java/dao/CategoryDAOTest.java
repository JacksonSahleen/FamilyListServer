package dao;

import model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CategoryDAOTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * Category object to use for testing
     */
    private static Category exCategory;

    /*
     * DAO object to use for testing
     */
    private static CategoryDAO cDao;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();

        // Create an example Category object
        exCategory = new Category("CategoryID", "name", "owner", "parentList");

        // Create a new CategoryDAO
        Connection conn = db.getConnection();
        cDao = new CategoryDAO(conn);

        // Wipe the database clean
        cDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Close the database connection and rollback any changes made
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Insert the example Category into the database
        cDao.insert(exCategory);

        // Get the example Category back from the database
        Category compareTest = cDao.find(exCategory.getId());

        // Verify that the Category we inserted and the Category we got back are the same
        assertNotNull(compareTest);
        assertEquals(exCategory, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert the example Category into the database
        cDao.insert(exCategory);

        // Try to insert the same Category again and verify that it throws an exception
        assertThrows(DataAccessException.class, () -> cDao.insert(exCategory));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Insert the example Category into the database
        cDao.insert(exCategory);

        // Verify that the Category we inserted and the Category we got back are the same
        Category compareTest = cDao.find(exCategory.getId());
        assertNotNull(compareTest);
        assertEquals(exCategory, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Try to get a Category that doesn't exist in the database
        Category compareTest = cDao.find(exCategory.getId());

        // Verify that the Category we got back is null
        assertNull(compareTest);
    }

    @Test
    public void updatePass() throws DataAccessException {
        // Insert the example Category into the database
        cDao.insert(exCategory);

        // Verify that the Category we inserted and the Category we got back are the same
        Category compareTest = cDao.find(exCategory.getId());
        assertNotNull(compareTest);
        assertEquals(exCategory, compareTest);

        // Update the example Category
        String newName = "newName";
        String newOwner = "newOwner";
        exCategory.setName(newName);
        exCategory.setOwner(newOwner);
        cDao.update(exCategory);

        // Verify that the Category we updated now has the updated values
        compareTest = cDao.find(exCategory.getId());
        assertNotNull(compareTest);
        assertEquals(compareTest.getName(), newName);
        assertEquals(compareTest.getOwner(), newOwner);
    }

    @Test
    public void updateFail() throws DataAccessException {
        // Update a Category that doesn't exist in the database and verify that nothing happens
        Category fakeCategory = new Category("fakeID", "fakeName", "fakeOwner",
                "fakeParentList");
        cDao.update(fakeCategory);

        // Verify that the updated Category still does not exist
        Category compareTest = cDao.find(fakeCategory.getId());
        assertNull(compareTest);
    }

    @Test
    public void removePass() throws DataAccessException {
        // Insert the example Category into the database
        cDao.insert(exCategory);

        // Verify that the Category we inserted and the Category we got back are the same
        Category compareTest = cDao.find(exCategory.getId());
        assertNotNull(compareTest);
        assertEquals(exCategory, compareTest);

        // Insert a second Category into the database
        Category exCategory2 = new Category("CategoryID2", "name2", "owner2", "parentList2");
        cDao.insert(exCategory2);

        // Verify that the second Category we inserted and the Category we got back are the same
        compareTest = cDao.find(exCategory2.getId());
        assertNotNull(compareTest);
        assertEquals(exCategory2, compareTest);

        // Remove the example Category from the database
        cDao.remove(exCategory.getId());

        // Verify that the example Category has been removed from the database but the second Category still exists
        compareTest = cDao.find(exCategory.getId());
        assertNull(compareTest);
        compareTest = cDao.find(exCategory2.getId());
        assertNotNull(compareTest);
        assertEquals(exCategory2, compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        // Remove a Category that doesn't exist in the database and verify that nothing happens
        cDao.remove(exCategory.getId());

        // Verify that the Category still does not exist
        Category compareTest = cDao.find(exCategory.getId());
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Insert the example Category into the database
        cDao.insert(exCategory);

        // Clear the database and then try to get the example Category back
        cDao.clear();
        Category compareTest = cDao.find(exCategory.getId());

        // Verify that the Category we got back is null
        assertNull(compareTest);
    }
}
