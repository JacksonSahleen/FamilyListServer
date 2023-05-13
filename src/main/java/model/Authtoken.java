package model;

import java.util.Objects;

/**
 * Model class that contains information about an authtoken
 */
public class Authtoken {

    /**
     * The authtoken
     */
    private String token;

    /**
     * The unique identifier of the user to which the authtoken belongs
     */
    private String userID;

    /**
     * Creates a new Authtoken object
     *
     * @param authtoken The authtoken
     * @param userID The unique identifier of the user to which the authtoken belongs
     */
    public Authtoken(String authtoken, String userID) {
        this.token = authtoken;
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String authtoken) {
        this.token = authtoken;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String username) {
        this.userID = username;
    }

    /**
     * Performs value equality on this Authtoken object and another object
     *
     * @param o The other (Authtoken) object to compare to
     * @return True if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authtoken auth = (Authtoken) o;
        return Objects.equals(token, auth.token) &&
                Objects.equals(userID, auth.userID);
    }

    /**
     * Returns a string representation of this Authtoken object
     *
     * @return A string representation of this Authtoken object
     */
    @Override
    public String toString() {
        return "Authtoken{" +
                "token='" + token + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
