package dao;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit tests for the UserDAO class.
 */
public class UserDAOTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * User object to use for testing
     */
    private static User exUser;

    /*
     * DAO object to use for testing
     */
    private static UserDAO uDao;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();

        // Create an example User object
        exUser = new User("userID", "username", "password");

        // Create a new UserDAO
        Connection conn = db.getConnection();
        uDao = new UserDAO(conn);

        // Wipe the database clean
        uDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Close the database connection and rollback any changes made
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Insert the example User into the database
        uDao.insert(exUser);

        // Get the example User back from the database
        User compareTest = uDao.find(exUser.getId());

        // Verify that the User we inserted and the User we got back are the same
        assertNotNull(compareTest);
        assertEquals(exUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert the example User into the database
        uDao.insert(exUser);

        // Try to insert the same User again and verify that it throws an exception
        assertThrows(DataAccessException.class, () -> uDao.insert(exUser));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Insert the example User into the database
        uDao.insert(exUser);

        // Verify that the User we inserted and the User we got back are the same
        User compareTest = uDao.find(exUser.getId());
        assertNotNull(compareTest);
        assertEquals(exUser, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Try to get a User that doesn't exist in the database
        User compareTest = uDao.find(exUser.getId());

        // Verify that the User we got back is null
        assertNull(compareTest);
    }

    @Test
    public void updatePass() throws DataAccessException {
        // Insert the example User into the database
        uDao.insert(exUser);

        // Verify that the User we inserted and the User we got back are the same
        User compareTest = uDao.find(exUser.getId());
        assertNotNull(compareTest);
        assertEquals(exUser, compareTest);

        // Update the example User
        String newPassword = "newPassword";
        String newUsername = "newUsername";
        exUser.setPassword(newPassword);
        exUser.setUsername(newUsername);
        uDao.update(exUser);

        // Verify that the User we updated now has the updated values
        compareTest = uDao.find(exUser.getId());
        assertNotNull(compareTest);
        assertEquals(compareTest.getPassword(), newPassword);
        assertEquals(compareTest.getUsername(), newUsername);
    }

    @Test
    public void updateFail() throws DataAccessException {
        // Update a user that doesn't exist in the database and verify that nothing happens
        User fakeUser = new User("fakeID", "fakeUsername", "fakePassword");
        uDao.update(fakeUser);

        // Verify that the updated User still does not exist
        User compareTest = uDao.find(fakeUser.getId());
        assertNull(compareTest);
    }

    @Test
    public void removePass() throws DataAccessException {
        // Insert the example User into the database
        uDao.insert(exUser);

        // Verify that the User we inserted and the User we got back are the same
        User compareTest = uDao.find(exUser.getId());
        assertNotNull(compareTest);
        assertEquals(exUser, compareTest);

        // Insert a second User into the database
        User exUser2 = new User("userID2", "username2", "password2");
        uDao.insert(exUser2);

        // Verify that the second User we inserted and the User we got back are the same
        compareTest = uDao.find(exUser2.getId());
        assertNotNull(compareTest);
        assertEquals(exUser2, compareTest);

        // Remove the example User from the database
        uDao.remove(exUser.getId());

        // Verify that the example User has been removed from the database but the second User still exists
        compareTest = uDao.find(exUser.getId());
        assertNull(compareTest);
        compareTest = uDao.find(exUser2.getId());
        assertNotNull(compareTest);
        assertEquals(exUser2, compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        // Remove a User that doesn't exist in the database and verify that nothing happens
        uDao.remove(exUser.getId());

        // Verify that the User still does not exist
        User compareTest = uDao.find(exUser.getId());
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Insert the example User into the database
        uDao.insert(exUser);

        // Clear the database and then try to get the example User back
        uDao.clear();
        User compareTest = uDao.find(exUser.getId());

        // Verify that the User we got back is null
        assertNull(compareTest);
    }
}
