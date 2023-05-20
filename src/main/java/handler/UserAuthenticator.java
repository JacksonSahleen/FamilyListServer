package handler;

import dao.AuthtokenDAO;
import dao.DataAccessException;
import dao.Database;
import model.Authtoken;

import java.sql.Connection;

/**
 * This class is responsible for authenticating users as they make requests.
 */
public class UserAuthenticator {

    /**
     * Database object used to access the database
     */
    Database db = new Database();

    // The authtoken for the admin user
    protected final String ADMIN_AUTHTOKEN = "9513ADMIN_DEV9513";

    /**
     * Default constructor for UserAuthenticator
     */
    UserAuthenticator() {}

    /**
     * Checks if the given authtoken is valid
     *
     * @param authtoken The Authtoken of the requesting user
     * @return true if the authtoken is valid, false otherwise
     */
    public boolean authenticate(String authtoken) {
        // Initialize boolean to false
        boolean tokenFound = false;

        // Check that an authtoken was given
        if (authtoken != null) {
            // Check if authtoken is the admin authtoken
            if (authenticateAdmin(authtoken)) {
                tokenFound = true;
            } else {
                // Search for authtoken in database
                try {
                    // Search Authtoken table for the provided token
                    Connection conn = db.openConnection();
                    AuthtokenDAO aDAO = new AuthtokenDAO(conn);
                    Authtoken foundToken = aDAO.find(authtoken);
                    db.closeConnection(true);

                    // If authtoken was found set boolean to true
                    if (foundToken != null) {
                        tokenFound = true;
                    }

                } catch (DataAccessException e) {
                    e.printStackTrace();
                    db.closeConnection(false);
                }
            }
        }

        return tokenFound;
    }

    /**
     * Checks if the given authtoken is valid and belongs to the admin user
     *
     * @param authtoken The Authtoken of the requesting user
     * @return true if the authtoken is valid and belongs to the admin user, false otherwise
     */
    public boolean authenticateAdmin(String authtoken) {
        return authtoken.equals(ADMIN_AUTHTOKEN);
    }
}
