package request;

/**
 * Stores the data for a register service request
 */
public class RegisterRequest {

    /**
     * The username of the new user to register
     */
    private String username;

    /**
     * The password of the new user to register
     */
    private String password;

    /**
     * The first name of the new user to register
     */
    private String firstName;

    /**
     * The last name of the new user to register
     */
    private String lastName;

    /**
     * Creates a new RegisterRequest object
     *
     * @param username the username of the new user to register
     * @param password the password of the new user to register
     * @param firstName the first name of the new user to register
     * @param lastName the last name of the new user to register
     */
    public RegisterRequest(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
