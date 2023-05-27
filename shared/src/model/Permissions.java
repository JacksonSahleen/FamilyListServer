package model;

import java.util.Objects;

/**
 * Model class that stores the permissions relationship data for a database object
 */
public class Permissions {

    /**
     * The object that the permissions are for
     */
    private String object;

    /**
     * The holder of the permissions
     */
    private String holder;

    /**
     * Constructor for the Permissions class
     *
     * @param object The object that the permissions are for
     * @param holder The holder of the permissions
     */
    public Permissions(String object, String holder) {
        this.object = object;
        this.holder = holder;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    /**
     * Performs value equality on this Permissions object and another object
     *
     * @param o The other (Permissions) object to compare to
     * @return True if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permissions p = (Permissions) o;

        return Objects.equals(holder, p.holder) &&
                Objects.equals(object, p.object);
    }

    /**
     * Returns a string representation of this Permissions object
     *
     * @return A string representation of this Permissions object
     */
    @Override
    public String toString() {
        return "Permissions{" +
                "object='" + object + '\'' +
                ", holder='" + holder + '\'' +
                '}';
    }

    /**
     * Returns a hash code for this Permissions object
     *
     * @return A hash code for this Permissions object
     */
    @Override
    public int hashCode() {
        return Objects.hash(object, holder);
    }
}
