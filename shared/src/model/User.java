package model;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Model class that contains information about a user
 */
public class User extends Model {

    /**
     * The user's username
     */
    private String username;

    /**
     * The user's password
     */
    private String password;

    /**
     * The user's first name
     */
    private String firstName;

    /**
     * The user's last name
     */
    private String lastName;

    /**
     * Creates a User object with the provided information
     *
     * @param username The user's username
     * @param password The user's password
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param lastUpdated The date and time the user was last updated
     */
    public User(String username, String password, String firstName, String lastName, ZonedDateTime lastUpdated) {
        this.id = username;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Creates a new User object (lastUpdated is set to now)
     *
     * @param username The user's username
     * @param password The user's password
     * @param firstName The user's first name
     * @param lastName The user's last name
     */
    public User(String username, String password, String firstName, String lastName) {
        this(username, password, firstName, lastName, ZonedDateTime.now());
    }

    public String getId() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        setLastUpdated();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        setLastUpdated();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        setLastUpdated();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        setLastUpdated();
    }

    /**
     * Performs value equality on this User object and another object
     *
     * @param o The other (User) object to compare to
     * @return True if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName);
    }

    /**
     * Returns a string representation of this User object
     *
     * @return A string representation of this User object
     */
    @Override
    public String toString() {
        return "User{" +
                "username=" + username +
                ", password=" + password +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    /**
     * Returns a hash code for this User object
     *
     * @return A hash code for this User object
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password, firstName, lastName);
    }
}
