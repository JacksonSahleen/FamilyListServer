package service;

import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RegisterServiceTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * RegisterService object to run tests on
     */
    private static RegisterService registerService;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        registerService = new RegisterService();

        Connection conn = db.getConnection();
        UserDAO uDao = new UserDAO(conn);
        uDao.clear();

        db.closeConnection(true);
    }

    @Test
    public void registerNewUser() {
        // Create a request
        RegisterRequest request = new RegisterRequest("username", "password",
                "firstName", "lastName");

        // Run the register service
        RegisterResult result = registerService.register(request);

        // Check that the correct result is given
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("username", result.getUsername());
        assertNotNull(result.getAuthtoken());
    }

    @Test
    public void invalidRequest() {
        // Create a request
        RegisterRequest request = new RegisterRequest("username", null,
                null, "lastName");

        // Run the register service
        RegisterResult result = registerService.register(request);

        // Check that the correct result is given
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("ERROR: Invalid request.", result.getMessage());
    }

    @Test void usernameAlreadyTaken() {
        // Create a request
        RegisterRequest request = new RegisterRequest("username", "password",
                "firstName", "lastName");

        // Add the user to the database already
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(new User("username", "password", "firstName", "lastName"));
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
        }

        // Run the register service
        RegisterResult result = registerService.register(request);

        // Check that the correct result is given
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("ERROR: Username already taken.", result.getMessage());
    }
}
