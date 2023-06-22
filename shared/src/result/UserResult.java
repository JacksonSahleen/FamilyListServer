package result;

import model.User;

/**
 * Stores the result information from all User services (login, register, user data sync)
 */
public class UserResult extends Result {

    /**
     * The Authtoken for the current session of the user
     */
    private String authtoken;

    /**
     * The Model object of the User's information
     */
    private User user;

    /**
     * Constructor for a successful UserResult class
     *
     * @param authtoken The Authtoken for the current session of the user
     * @param user The Model object of the User's information
     * @param message The message to be displayed to the user
     */
    public UserResult(String authtoken, User user, String message) {
        this.authtoken = authtoken;
        this.user = user;
        this.message = message;
        this.success = true;
    }

    /**
     * Constructor for an unsuccessful UserResult class
     *
     * @param message The message to be displayed to the user
     */
    public UserResult(String message) {
        this.message = message;
        this.success = false;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public User getUser() {
        return user;
    }
}
