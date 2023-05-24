package dao;

import model.ItemList;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.ZonedDateTime;
import java.util.List;

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
        lDao.remove(exList, exList.getOwner());

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
        lDao.remove(exList, exList.getOwner());

        // Verify that the ItemList still does not exist
        ItemList compareTest = lDao.find(exList.getId());
        assertNull(compareTest);
    }

    @Test
    public void removeOtherUserAccess() throws DataAccessException {
        // Insert list into database
        lDao.insert(exList);

        // Share list with additional user
        lDao.share(exList.getId(), "user1");

        // Remove list from database for the original owner
        lDao.remove(exList, "owner");

        // Verify that the list still exists in the database
        ItemList compareTest = lDao.find(exList.getId());
        assertNotNull(compareTest);
        assertEquals(exList, compareTest);

        // Verify that the other user still has access to the list
        List<String> users = lDao.findUsersWithAccess(exList.getId());
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("user1", users.get(0));
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

    @Test
    public void sharePass() throws DataAccessException {
        // Insert list into database
        lDao.insert(exList);

        // Share list with additional users
        lDao.share(exList.getId(), "user1");
        lDao.share(exList.getId(), "user2");

        // Get list of users with access to list
        List<String> users = lDao.findUsersWithAccess(exList.getId());

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
        lDao.insert(exList);

        // Share list with additional users
        lDao.share(exList.getId(), "user1");
        lDao.share(exList.getId(), "user2");

        // Get list of users with access to list
        List<String> users = lDao.findUsersWithAccess(exList.getId());

        // Verify that the list of users contains the additional users
        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.contains("owner"));
        assertTrue(users.contains("user1"));
        assertTrue(users.contains("user2"));

        // Try to share list with user that already has access
        lDao.share(exList.getId(), "user1");

        // Get list of users with access to list
        users = lDao.findUsersWithAccess(exList.getId());

        // Verify that the list of users hasn't changed
        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.contains("owner"));
        assertTrue(users.contains("user1"));
        assertTrue(users.contains("user2"));
    }

    @Test
    public void unsharePass() throws DataAccessException {
        // Insert list into database
        lDao.insert(exList);

        // Share list with additional users
        lDao.share(exList.getId(), "user1");
        lDao.share(exList.getId(), "user2");

        // Get list of users with access to list
        List<String> users = lDao.findUsersWithAccess(exList.getId());

        // Verify that the list of users contains the additional users
        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.contains("owner"));
        assertTrue(users.contains("user1"));
        assertTrue(users.contains("user2"));

        // Unshare list with user
        lDao.unshare(exList.getId(), "user1");

        // Get list of users with access to list
        users = lDao.findUsersWithAccess(exList.getId());

        // Verify that the list of users doesn't contain the unshared user
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains("owner"));
        assertFalse(users.contains("user1"));
        assertTrue(users.contains("user2"));
    }

    @Test
    public void unshareFail() throws DataAccessException {
        // Insert list into database
        lDao.insert(exList);

        // Try to unshare the list with a user that doesn't have access
        lDao.unshare(exList.getId(), "user1");

        // Get list of users with access to list
        List<String> users = lDao.findUsersWithAccess(exList.getId());

        // Verify that the list of users hasn't changed
        assertNotNull(users);
        assertEquals(1, users.size());
        assertTrue(users.contains("owner"));
    }

    @Test
    public void findUserListsPass() throws DataAccessException {
        // Insert a user into the database
        UserDAO uDao = new UserDAO(db.getConnection());
        User exUser = new User("username", "password", "firstName",
                "lastName", ZonedDateTime.now());
        uDao.insert(exUser);

        // Insert multiple lists for the user into the database
        ItemList exList1 = new ItemList("listID1", "list1", exUser.getUsername());
        ItemList exList2 = new ItemList("listID2", "list2", exUser.getUsername());
        lDao.insert(exList1);
        lDao.insert(exList2);

        // Get the lists for the user from the database
        List<String> lists = lDao.findUserLists(exUser.getUsername());

        // Verify that the lists returned are the same as the ones inserted
        assertNotNull(lists);
        assertEquals(2, lists.size());
        assertTrue(lists.contains(exList1.getId()));
        assertTrue(lists.contains(exList2.getId()));
    }

    @Test
    public void findUserListsFail() throws DataAccessException {
        // Get the lists for a user that doesn't exist in the database
        User exUser = new User("username", "password", "firstName",
                "lastName", ZonedDateTime.now());
        List<String> lists = lDao.findUserLists(exUser.getUsername());

        // Verify that the lists returned are null
        assertNull(lists);
    }
}
