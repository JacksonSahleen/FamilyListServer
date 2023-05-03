package dao;

import model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit tests for the ItemDAO class
 */
public class ItemDAOTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * Item object to use for testing
     */
    private static Item exItem;

    /*
     * DAO object to use for testing
     */
    private static ItemDAO iDao;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();

        // Create an example Item object
        exItem = new Item("ItemID", "name", "owner", null,
                "parentList", false, false);

        // Create a new ItemDAO
        Connection conn = db.getConnection();
        iDao = new ItemDAO(conn);

        // Wipe the database clean
        iDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Close the database connection and rollback any changes made
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Insert the example Item into the database
        iDao.insert(exItem);

        // Get the example Item back from the database
        Item compareTest = iDao.find(exItem.getId());

        // Verify that the Item we inserted and the Item we got back are the same
        assertNotNull(compareTest);
        assertEquals(exItem, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert the example Item into the database
        iDao.insert(exItem);

        // Try to insert the same Item again and verify that it throws an exception
        assertThrows(DataAccessException.class, () -> iDao.insert(exItem));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Insert the example Item into the database
        iDao.insert(exItem);

        // Verify that the Item we inserted and the Item we got back are the same
        Item compareTest = iDao.find(exItem.getId());
        assertNotNull(compareTest);
        assertEquals(exItem, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Try to get an Item that doesn't exist in the database
        Item compareTest = iDao.find(exItem.getId());

        // Verify that the Item we got back is null
        assertNull(compareTest);
    }

    @Test
    public void updatePass() throws DataAccessException {
        // Insert the example Item into the database
        iDao.insert(exItem);

        // Verify that the Item we inserted and the Item we got back are the same
        Item compareTest = iDao.find(exItem.getId());
        assertNotNull(compareTest);
        assertEquals(exItem, compareTest);

        // Update the example Item
        String newName = "newName";
        String newOwner = "newOwner";
        boolean newCompleted = true;
        exItem.setName(newName);
        exItem.setOwner(newOwner);
        exItem.setCompleted(newCompleted);
        iDao.update(exItem);

        // Verify that the Item we updated now has the updated values
        compareTest = iDao.find(exItem.getId());
        assertNotNull(compareTest);
        assertEquals(compareTest.getName(), newName);
        assertEquals(compareTest.getOwner(), newOwner);
        assertEquals(compareTest.isCompleted(), newCompleted);
    }

    @Test
    public void updateFail() throws DataAccessException {
        // Update an Item that doesn't exist in the database and verify that nothing happens
        Item fakeItem = new Item("fakeID", "fakeName", "fakeOwner", "fakeCategory",
                "fakeParentList", false, false);
        iDao.update(fakeItem);

        // Verify that the updated Item still does not exist
        Item compareTest = iDao.find(fakeItem.getId());
        assertNull(compareTest);
    }

    @Test
    public void removePass() throws DataAccessException {
        // Insert the example Item into the database
        iDao.insert(exItem);

        // Verify that the Item we inserted and the Item we got back are the same
        Item compareTest = iDao.find(exItem.getId());
        assertNotNull(compareTest);
        assertEquals(exItem, compareTest);

        // Insert a second Item into the database
        Item exItem2 = new Item("ItemID2", "name2", "owner2", "category2",
                "parentList2", false, false);
        iDao.insert(exItem2);

        // Verify that the second Item we inserted and the Item we got back are the same
        compareTest = iDao.find(exItem2.getId());
        assertNotNull(compareTest);
        assertEquals(exItem2, compareTest);

        // Remove the example Item from the database
        iDao.remove(exItem.getId());

        // Verify that the example Item has been removed from the database but the second Item still exists
        compareTest = iDao.find(exItem.getId());
        assertNull(compareTest);
        compareTest = iDao.find(exItem2.getId());
        assertNotNull(compareTest);
        assertEquals(exItem2, compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        // Remove an Item that doesn't exist in the database and verify that nothing happens
        iDao.remove(exItem.getId());

        // Verify that the Item still does not exist
        Item compareTest = iDao.find(exItem.getId());
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Insert the example Item into the database
        iDao.insert(exItem);

        // Clear the database and then try to get the example Item back
        iDao.clear();
        Item compareTest = iDao.find(exItem.getId());

        // Verify that the Item we got back is null
        assertNull(compareTest);
    }
}
