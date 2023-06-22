package service;

import dao.AuthtokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.Authtoken;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.UserResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * LoginService object to run tests on
     */
    private static LoginService loginService;

    /*
     * User object to use for testing
     */
    private static final User exUser = new User("username", "password",
            "firstName", "lastName");

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        loginService = new LoginService();

        Connection conn = db.openConnection();
        UserDAO uDao = new UserDAO(conn);
        uDao.clear();
        uDao.insert(exUser);

        db.closeConnection(true);
    }

    @Test
    public void successfulLogin() {
        // Create a request
        LoginRequest request = new LoginRequest("username", "password");

        // Run the login service
        UserResult result = loginService.login(request);

        // Verify the correct results
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(exUser, result.getUser());
        assertNotNull(result.getAuthtoken());

        // Check that an entry was added to the Authtoken table
        try {
            Connection conn = db.openConnection();
            AuthtokenDAO aDao = new AuthtokenDAO(conn);

            Authtoken token = aDao.find(result.getAuthtoken());
            assertNotNull(token);
            assertEquals(result.getUser().getUsername(), token.getUserID());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            fail();
        }
    }

    @Test
    public void multipleUserLogins() {
        // Create a request
        LoginRequest request = new LoginRequest("username", "password");

        // Run the login service
        UserResult result = loginService.login(request);

        // Verify the correct results
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(exUser, result.getUser());
        assertNotNull(result.getAuthtoken());

        // Check that an entry was added to the Authtoken table
        try {
            Connection conn = db.openConnection();
            AuthtokenDAO aDao = new AuthtokenDAO(conn);

            Authtoken token = aDao.find(result.getAuthtoken());
            assertNotNull(token);
            assertEquals(result.getUser().getUsername(), token.getUserID());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            fail();
        }

        // Run the login service again for the same user
        UserResult result2 = loginService.login(request);

        // Verify the correct results
        assertNotNull(result2);
        assertTrue(result2.isSuccess());
        assertEquals(exUser, result2.getUser());
        assertNotNull(result2.getAuthtoken());

        // Check that both login results are for the same user but with different AuthTokens
        assertNotEquals(result.getAuthtoken(), result2.getAuthtoken());
        assertEquals(result.getUser(), result2.getUser());

        // Check that both entries are in the database
        try {
            Connection conn = db.openConnection();
            AuthtokenDAO aDao = new AuthtokenDAO(conn);

            Authtoken token = aDao.find(result.getAuthtoken());
            assertNotNull(token);
            assertEquals(result.getUser().getUsername(), token.getUserID());

            Authtoken token2 = aDao.find(result2.getAuthtoken());
            assertNotNull(token2);
            assertEquals(result2.getUser().getUsername(), token2.getUserID());

            assertNotEquals(result.getAuthtoken(), result2.getAuthtoken());
            assertEquals(result.getUser(), result2.getUser());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            fail();
        }
    }

    @Test
    public void invalidRequest() {
        // Create a request
        LoginRequest request = new LoginRequest(null, "password");

        // Run the login service
        UserResult result = loginService.login(request);

        // Verify the correct results
        assertFalse(result.isSuccess());
    }

    @Test
    public void invalidCredentials() {
        // Create a request
        LoginRequest request = new LoginRequest("invalidUsername", "invalidPassword");

        // Run the login service
        UserResult result = loginService.login(request);

        // Verify the correct results
        assertFalse(result.isSuccess());
    }

}
