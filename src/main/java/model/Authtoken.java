package model;

/**
 * Model class that contains information about an authtoken
 */
public class Authtoken {

    /**
     * The authtoken
     */
    private String authtoken;

    /**
     * The username of the user to which the authtoken belongs
     */
    private String username;

    /**
     * Creates a new Authtoken object
     *
     * @param authtoken The authtoken
     * @param username The username of the user to which the authtoken belongs
     */
    public Authtoken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
