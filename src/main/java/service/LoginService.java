package service;

import dao.AuthtokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.Authtoken;
import model.User;
import request.LoginRequest;
import result.LoginResult;

import java.sql.Connection;
import java.util.UUID;

/**
 * The service class responsible for handling user Login requests.
 */
public class LoginService {

    /**
     * The Database object used to access the database.
     */
    private final Database db;

    /**
     * Constructor for LoginService
     */
    public LoginService() {
        db = new Database();
    }

    /**
     * Logs in a user.
     *
     * @param request The request object containing the user's username and password.
     * @return The result object containing a corresponding Authtoken and the success or failure of the operation.
     */
    public LoginResult login(LoginRequest request) {
        // Check that a valid request was given
        if (!checkRequest(request)) {
            return new LoginResult("ERROR: Invalid request.");
        }

        // Attempt to log the user into the system
        String authtoken = verifyLogin(request.username(), request.password());

        if (authtoken == null) {
            return new LoginResult("ERROR: Invalid login credentials provided.");
        } else {
            // Get the User object for the newly-logged in user
            try {
                Connection conn = db.getConnection();

                UserDAO uDao = new UserDAO(conn);
                User user = uDao.find(request.username());

                db.closeConnection(true);
                return new LoginResult(authtoken, user.getUsername());

            } catch (DataAccessException e) {
                e.printStackTrace();
                return new LoginResult("ERROR: Internal Server Error (" + e.getMessage() + ").");
            }
        }
    }

    /**
     * Checks if the given username and password match a registered user
     *
     * @param username The username of the user
     * @param password The password of the user
     * @return generated authtoken if the username and password match a registered user, null otherwise
     */
    public String verifyLogin(String username, String password) {
        // Check that a username and password were given
        if (username == null || password == null) {
            return null;
        } else {
            // Search for User in database and create User object
            try {
                Connection conn = db.openConnection();

                UserDAO uDao = new UserDAO(conn);
                User foundUser = uDao.find(username);

                db.closeConnection(true);

                // If user was not found, return null
                if (foundUser == null) {
                    return null;
                } else {
                    // Check password against password from the database
                    if (!password.equals(foundUser.getPassword())) {
                        return null;
                    } else {
                        // Generate authtoken
                        String authtoken = UUID.randomUUID().toString();

                        // Add authtoken to database
                        conn = db.openConnection();

                        AuthtokenDAO aDao = new AuthtokenDAO(conn);
                        aDao.insert(new Authtoken(authtoken, username));

                        db.closeConnection(true);

                        // Return authtoken
                        return authtoken;
                    }
                }

            } catch (DataAccessException e) {
                e.printStackTrace();
                db.closeConnection(false);
                return null;
            }
        }
    }

    /**
     * Checks if the provided request if valid
     *
     * @param request The request object to check
     * @return True if the request is valid and false if not
     */
    private boolean checkRequest(LoginRequest request) {
        return request != null && request.username() != null && request.password() != null;
    }
}
