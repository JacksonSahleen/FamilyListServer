package request;

/**
 * Stores the request data for the Login service
 */
public class LoginRequest {

    private String username;

    private String password;

    /**
     * Creates a LoginRequest object with the given data
     *
     * @param username The username for the request
     * @param password The password for the request
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
