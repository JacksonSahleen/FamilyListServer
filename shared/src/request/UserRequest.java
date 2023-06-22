package request;

import model.User;

/**
 * Stores the data for a user service request
 */
public class UserRequest {

    /**
     * The authtoken for the current session of the User making the request
     */
    private String authtoken;

    /**
     * The User object storing the user data to be synced with the server.
     */
    private User user;

    /**
     * Creates a new UserRequest object
     *
     * @param user the User object storing the user data to be synced with the server.
     */
    public UserRequest(String authtoken, User user) {
        this.authtoken = authtoken;
        this.user = user;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
