package dao;

import model.ItemList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit tests for the ItemListDAO class
 */
public class ItemListDAOTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * ItemList object to use for testing
     */
    private static ItemList exList;

    /*
     * DAO object to use for testing
     */
    private static ItemListDAO lDao;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();

        // Create an example ItemList object
        exList = new ItemList("listID", "name", "owner");

        // Create a new ItemListDAO
        Connection conn = db.getConnection();
        lDao = new ItemListDAO(conn);

        // Wipe the database clean
        lDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Close the database connection and rollback any changes made
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Insert the example ItemList into the database
        lDao.insert(exList);

        // Get the example ItemList back from the database
        ItemList compareTest = lDao.find(exList.getId());

        // Verify that the ItemList we inserted and the ItemList we got back are the same
        assertNotNull(compareTest);
        assertEquals(exList, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert the example ItemList into the database
        lDao.insert(exList);

        // Try to insert the same ItemList again and verify that it throws an exception
        assertThrows(DataAccessException.class, () -> lDao.insert(exList));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Insert the example ItemList into the database
        lDao.insert(exList);

        // Verify that the ItemList we inserted and the ItemList we got back are the same
        ItemList compareTest = lDao.find(exList.getId());
        assertNotNull(compareTest);
        assertEquals(exList, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Try to get a ItemList that doesn't exist in the database
        ItemList compareTest = lDao.find(exList.getId());

        // Verify that the ItemList we got back is null
        assertNull(compareTest);
    }

    @Test
    public void updatePass() throws DataAccessException {
        // Insert the example ItemList into the database
        lDao.insert(exList);

        // Verify that the ItemList we inserted and the ItemList we got back are the same
        ItemList compareTest = lDao.find(exList.getId());
        assertNotNull(compareTest);
        assertEquals(exList, compareTest);

        // Update the example ItemList
        String newName = "newName";
        String newOwner = "newOwner";
        exList.setName(newName);
        exList.setOwner(newOwner);
        lDao.update(exList);

        // Verify that the ItemList we updated now has the updated values
        compareTest = lDao.find(exList.getId());
        assertNotNull(compareTest);
        assertEquals(compareTest.getName(), newName);
        assertEquals(compareTest.getOwner(), newOwner);
    }

    @Test
    public void updateFail() throws DataAccessException {
        // Update a ItemList that doesn't exist in the database and verify that nothing happens
        ItemList fakeItemList = new ItemList("fakeID", "fakeName", "fakeOwner");
        lDao.update(fakeItemList);

        // Verify that the updated ItemList still does not exist
        ItemList compareTest = lDao.find(fakeItemList.getId());
        assertNull(compareTest);
    }

    @Test
    public void removePass() throws DataAccessException {
        // Insert the example ItemList into the database
        lDao.insert(exList);

        // Verify that the ItemList we inserted and the ItemList we got back are the same
        ItemList compareTest = lDao.find(exList.getId());
        assertNotNull(compareTest);
        assertEquals(exList, compareTest);

        // Insert a second ItemList into the database
        ItemList exList2 = new ItemList("ItemListID2", "name2", "owner2");
        lDao.insert(exList2);

        // Verify that the second ItemList we inserted and the ItemList we got back are the same
        compareTest = lDao.find(exList2.getId());
        assertNotNull(compareTest);
        assertEquals(exList2, compareTest);

        // Remove the example ItemList from the database
        lDao.remove(exList.getId());

        // Verify that the example ItemList has been removed from the database but the second ItemList still exists
        compareTest = lDao.find(exList.getId());
        assertNull(compareTest);
        compareTest = lDao.find(exList2.getId());
        assertNotNull(compareTest);
        assertEquals(exList2, compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        // Remove a ItemList that doesn't exist in the database and verify that nothing happens
        lDao.remove(exList.getId());

        // Verify that the ItemList still does not exist
        ItemList compareTest = lDao.find(exList.getId());
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Insert the example ItemList into the database
        lDao.insert(exList);

        // Clear the database and then try to get the example ItemList back
        lDao.clear();
        ItemList compareTest = lDao.find(exList.getId());

        // Verify that the ItemList we got back is null
        assertNull(compareTest);
    }
}
