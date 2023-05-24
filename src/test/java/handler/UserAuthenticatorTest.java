package handler;

import dao.AuthtokenDAO;
import dao.Database;
import dao.UserDAO;

import model.Authtoken;
import model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserAuthenticatorTest {

    /*
     * UserAuthenticator object to use for testing
     */
    private static UserAuthenticator uAuth;

    @BeforeEach
    public void setUp() throws Exception {
        Database db = new Database();
        Connection conn = db.openConnection();

        // Initialize UserAuthenticator
        uAuth = new UserAuthenticator();

        // User table setup
        UserDAO uDao = new UserDAO(conn);
        uDao.clear();
        User exUser = new User("username", "password", "firstname", "lastname");
        uDao.insert(exUser);

        // Authtoken table setup
        AuthtokenDAO aDao = new AuthtokenDAO(conn);
        aDao.clear();
        Authtoken exToken = new Authtoken("token", "username");
        aDao.insert(exToken);

        db.closeConnection(true);
    }

    @Test
    public void authenticatePass() {
        boolean tokenFound = uAuth.authenticate("token");
        assertTrue(tokenFound);
    }

    @Test
    public void authenticateNullToken() {
        boolean tokenFound = uAuth.authenticate(null);
        assertFalse(tokenFound);
    }

    @Test
    public void authenticateInvalidToken() {
        boolean tokenFound = uAuth.authenticate("invalidToken");
        assertFalse(tokenFound);
    }
}
