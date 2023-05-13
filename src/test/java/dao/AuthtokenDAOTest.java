package dao;

import model.Authtoken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AuthtokenDAOTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * Authtoken object to use for testing
     */
    private static Authtoken exAuthtoken;

    /*
     * DAO object to use for testing
     */
    private static AuthtokenDAO aDao;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();

        // Create an example Authtoken object
        exAuthtoken = new Authtoken("token", "userID");

        // Create a new AuthtokenDAO
        Connection conn = db.getConnection();
        aDao = new AuthtokenDAO(conn);

        // Wipe the database clean
        aDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Close the database connection and rollback any changes made
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Insert the example Authtoken into the database
        aDao.insert(exAuthtoken);

        // Get the example Authtoken back from the database
        Authtoken compareTest = aDao.find(exAuthtoken.getToken());

        // Verify that the Authtoken we inserted and the Authtoken we got back are the same
        assertNotNull(compareTest);
        assertEquals(exAuthtoken, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert the example Authtoken into the database
        aDao.insert(exAuthtoken);

        // Try to insert the same Authtoken again and verify that it throws an exception
        assertThrows(DataAccessException.class, () -> aDao.insert(exAuthtoken));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Insert the example Authtoken into the database
        aDao.insert(exAuthtoken);

        // Verify that the Authtoken we inserted and the Authtoken we got back are the same
        Authtoken compareTest = aDao.find(exAuthtoken.getToken());
        assertNotNull(compareTest);
        assertEquals(exAuthtoken, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Try to get an Authtoken that doesn't exist in the database
        Authtoken compareTest = aDao.find(exAuthtoken.getToken());

        // Verify that the Authtoken we got back is null
        assertNull(compareTest);
    }

    @Test
    public void findUserAuthtokensPass() throws DataAccessException {
        // Insert two tokens for the same user into the database
        Authtoken exAuthtoken2 = new Authtoken("token2", "userID");
        aDao.insert(exAuthtoken);
        aDao.insert(exAuthtoken2);

        // Verify that the Authtokens we inserted and the Authtokens we got back are the same
        List<Authtoken> userTokens = aDao.findUserAuthtokens(exAuthtoken.getUserID());
        assertNotNull(userTokens);
        assertEquals(2, userTokens.size());
        for (Authtoken token : userTokens) {
            assertTrue(token.equals(exAuthtoken) || token.equals(exAuthtoken2));
        }
    }

    @Test
    public void findUserAuthtokensFail() throws DataAccessException {
        // Try to get Authtokens for a user that doesn't exist in the database
        List<Authtoken> userTokens = aDao.findUserAuthtokens(exAuthtoken.getUserID());

        // Verify that the list of AuthTokens we got back is empty
        assertNotNull(userTokens);
        assertEquals(0, userTokens.size());
    }

    @Test
    public void removePass() throws DataAccessException {
        // Insert the example Authtoken into the database
        aDao.insert(exAuthtoken);

        // Verify that the Authtoken we inserted and the Authtoken we got back are the same
        Authtoken compareTest = aDao.find(exAuthtoken.getToken());
        assertNotNull(compareTest);
        assertEquals(exAuthtoken, compareTest);

        // Insert a second Authtoken into the database
        Authtoken exAuthtoken2 = new Authtoken("token2", "userID2");
        aDao.insert(exAuthtoken2);

        // Verify that the second Authtoken we inserted and the Authtoken we got back are the same
        compareTest = aDao.find(exAuthtoken2.getToken());
        assertNotNull(compareTest);
        assertEquals(exAuthtoken2, compareTest);

        // Remove the example Authtoken from the database
        aDao.remove(exAuthtoken);

        // Verify that the example Authtoken has been removed from the database but the second Authtoken still exists
        compareTest = aDao.find(exAuthtoken.getToken());
        assertNull(compareTest);
        compareTest = aDao.find(exAuthtoken2.getToken());
        assertNotNull(compareTest);
        assertEquals(exAuthtoken2, compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        // Remove an Authtoken that doesn't exist in the database and verify that nothing happens
        aDao.remove(exAuthtoken);

        // Verify that the Authtoken still does not exist
        Authtoken compareTest = aDao.find(exAuthtoken.getToken());
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Insert the example Authtoken into the database
        aDao.insert(exAuthtoken);

        // Clear the database and then try to get the example Authtoken back
        aDao.clear();
        Authtoken compareTest = aDao.find(exAuthtoken.getToken());

        // Verify that the Authtoken we got back is null
        assertNull(compareTest);
    }
}
