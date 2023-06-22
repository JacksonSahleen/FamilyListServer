package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.UserDAO;
import model.Authtoken;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.UserRequest;
import result.UserResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    /*
     * UserService object to run tests on
     */
    private static UserService userService;

    /*
     * User object to use for testing
     */
    private static final User exUser = new User("username", "password",
            "firstName", "lastName");

    /*
     * User object to use for testing
     */
    private static final User exUser2 = new User("username2", "password2",
            "firstName2", "lastName2");

    /*
     * Authtoken object to use for testing
     */
    private static final Authtoken exAuthtoken = new Authtoken("authtoken", "username");

    /*
     * Authtoken object to use for testing
     */
    private static final Authtoken exAuthtoken2 = new Authtoken("authtoken2", "username2");

    @BeforeEach
    public void setUp() throws Exception {
        Database db = new Database();
        userService = new UserService();

        // Open a connection to the database and clear the tables
        Connection conn = db.getConnection();
        UserDAO uDao = new UserDAO(conn);
        AuthtokenDAO aDao = new AuthtokenDAO(conn);
        uDao.clear();
        aDao.clear();

        // Insert example users and authtokens into the database
        uDao.insert(exUser);
        uDao.insert(exUser2);
        aDao.insert(exAuthtoken);
        aDao.insert(exAuthtoken2);

        // Close the connection
        db.closeConnection(true);
    }

    @Test
    public void syncPass() {
        // Create a request to update the user info with an unchanged username
        User updatedUser = new User("username", "newPassword",
                "newFirstName", "newLastName");
        UserRequest request = new UserRequest("authtoken", updatedUser);

        // Run the request through the service
        UserResult result = userService.sync(request);

        // Check that the result is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getMessage());

        assertNotNull(result.getAuthtoken());
        assertEquals(request.getAuthtoken(), result.getAuthtoken());

        assertNotNull(result.getUser());
        assertEquals(updatedUser.getUsername(), result.getUser().getUsername());
        assertEquals(updatedUser.getPassword(), result.getUser().getPassword());
        assertEquals(updatedUser.getFirstName(), result.getUser().getFirstName());
        assertEquals(updatedUser.getLastName(), result.getUser().getLastName());
    }

    @Test
    public void syncInvalidRequest() {
        // Create an invalid request
        UserRequest request = new UserRequest("authtoken", null);

        // Run the request through the service
        UserResult result = userService.sync(request);

        // Check that the result is correct
        assertNotNull(result);
        assertFalse(result.isSuccess());

        assertNotNull(result.getMessage());
        assertTrue(result.getMessage().toLowerCase().contains("invalid request"));
    }

    @Test
    public void syncUserNotFound() {
        // Create a request with a user that doesn't exist
        User updatedUser = new User("username3", "newPassword",
                "newFirstName", "newLastName");
        UserRequest request = new UserRequest("authtoken2", updatedUser);

        // Run the request through the service
        UserResult result = userService.sync(request);

        // Check that the result is correct
        assertNotNull(result);
        assertFalse(result.isSuccess());

        assertNotNull(result.getMessage());

        System.out.println(result.getMessage());

        assertTrue(result.getMessage().toLowerCase().contains("cannot sync data for"));
    }
}
