package model;

/**
 * Model class that contains information about a user
 */
public class User {

    /**
     * Unique ID for the user
     */
    private String id;

    /**
     * The user's username
     */
    private String username;

    /**
     * The user's password
     */
    private String password;

    /**
     * Creates a new User object
     *
     * @param id Unique ID for the user
     * @param username The user's username
     * @param password The user's password
     */
    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
