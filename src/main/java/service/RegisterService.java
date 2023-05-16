package service;

import dao.*;
import model.Authtoken;
import model.User;
import result.RegisterResult;
import request.RegisterRequest;

import java.sql.Connection;
import java.util.UUID;

/**
 * This class is responsible for registering a new user in the database.
 */
public class RegisterService {

    /**
     * Registers a new user in the database
     *
     * @param request The RegisterRequest object containing the information needed to register a new user
     * @return The RegisterResult object containing the result of the registration
     */
    public RegisterResult register(RegisterRequest request) {
        // Check if request is valid
        if (!checkRequest(request)) {
            return new RegisterResult("ERROR: Invalid request.");
        }

        Database db = new Database();

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            // Check if username is already taken
            if (uDao.find(request.username()) != null) {
                db.closeConnection(false);
                return new RegisterResult("ERROR: Username already taken.");
            } else {
                // Create new user
                User newUser = new User(request.username(), request.password(),
                        request.firstName(), request.lastName());

                // Insert new user into database
                uDao.insert(newUser);

                // Create new authtoken for the new user
                Authtoken newToken = new Authtoken(UUID.randomUUID().toString(), request.username());

                // Insert new authtoken into database
                AuthtokenDAO aDao = new AuthtokenDAO(conn);
                aDao.insert(newToken);

                // Close connection and commit changes to database
                db.closeConnection(true);

                // Return result
                return new RegisterResult(newToken.getToken(), newToken.getUserID());
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new RegisterResult("ERROR: Internal server error.");
        }
    }

    /**
     * Checks if the request is valid.
     *
     * @param request The request to check
     * @return True if the request is valid, false otherwise
     */
    private boolean checkRequest(RegisterRequest request) {
        // Check if request is null or any of the fields are null
        return request != null &&
                request.username() != null &&
                request.password() != null &&
                request.firstName() != null &&
                request.lastName() != null;
    }
}
