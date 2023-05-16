package result;

/**
 * Stores the result information from the Login service
 */
public class LoginResult extends Result {

    /**
     * The Authtoken for the user
     */
    private String authtoken;

    /**
     * The username of the user
     */
    private String username;

    /**
     * Constructor for a successful LoginResult class
     *
     * @param authtoken The Authtoken for the user
     * @param username The username of the user
     */
    public LoginResult(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
        this.message = "Successfully logged in " + username;
        this.success = true;
    }

    /**
     * Constructor for an unsuccessful LoginResult class
     *
     * @param message The message to be displayed to the user
     */
    public LoginResult(String message) {
        this.message = message;
        this.success = false;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }
}
