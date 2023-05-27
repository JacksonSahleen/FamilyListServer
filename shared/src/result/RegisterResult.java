package result;

/**
 * Stores the result information from the Register service
 */
public class RegisterResult extends Result {

    /**
     * The Authtoken for the current session of the user who was registered
     */
    private String authtoken;

    /**
     * The username of the user who was registered
     */
    private String username;

    /**
     * Constructor for a successful RegisterResult class
     *
     * @param authtoken The Authtoken for the current session of the user who was registered
     * @param username The username of the user who was registered
     */
    public RegisterResult(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
        this.message = "Successfully registered new user " + username;
        this.success = true;
    }

    /**
     * Constructor for an unsuccessful RegisterResult class
     *
     * @param message The message to be displayed to the user
     */
    public RegisterResult(String message) {
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
