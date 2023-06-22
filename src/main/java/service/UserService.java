package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.UserDAO;
import model.Authtoken;
import model.User;
import request.UserRequest;
import result.UserResult;

/**
 * The service class responsible for handling User requests.
 */
public class UserService {

    /**
     * The Database object used to access the database.
     */
    private final Database db;

    /**
     * Constructor for UserService
     */
    public UserService() {
        db = new Database();
    }

    /**
     * Syncs the local User data with the server and returns the updated data.
     *
     * @param request The service request with the local data to be synced with the server.
     * @return The updated User data from the server after being synced.
     */
    public UserResult sync(UserRequest request) {
        // Check that the request is valid
        if (!checkRequest(request)) {
            return new UserResult("ERROR: Invalid request");
        }

        try {
            UserDAO uDao = new UserDAO(db.getConnection());
            AuthtokenDAO aDao = new AuthtokenDAO(db.getConnection());

            // Identify the user making the request if possible
            Authtoken userAuthtoken = aDao.find(request.getAuthtoken());
            if (userAuthtoken == null) {
                db.closeConnection(false);
                return new UserResult("ERROR: Invalid authtoken.");
            }
            String requester = userAuthtoken.getUserID();

            // Get the user data from both the request and the database
            User requestUser = request.getUser();
            User dbUser = uDao.find(requester);

            // Check that the user whose data is being synced is the same as the user making the request
            if (!requestUser.getUsername().equals(requester)) {
                db.closeConnection(false);
                return new UserResult("ERROR: User " + requester + " cannot sync data for user "
                        + requestUser.getUsername() + ".");
            }

            // Select the most recent copy of the user
            User syncedUser;
            if (requestUser.getLastUpdated().isAfter(dbUser.getLastUpdated())) {
                syncedUser = requestUser;
            } else {
                syncedUser = dbUser;
            }

            // Update the database with the synced data
            uDao.update(syncedUser);

            // Return the final data in the result for the client
            String message = "Successfully synced user data for " + syncedUser.getUsername() + ".";
            db.closeConnection(true);
            return new UserResult(request.getAuthtoken(), syncedUser, message);

        } catch (Exception e) {
            // Return the error message in the result for the client if a server error occurs
            e.printStackTrace();
            db.closeConnection(false);
            return new UserResult("ERROR: Internal Server Error (" + e.getMessage() + ").");
        }
    }

    /**
     * Checks if the request is valid.
     *
     * @param request The request to check
     * @return True if the request is valid, false otherwise
     */
    private boolean checkRequest(UserRequest request) {
        // Check if request is null or any of the fields are null
        return request != null && request.getUser() != null;
    }
}
