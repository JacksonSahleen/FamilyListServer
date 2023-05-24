package model;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Collection extends Model {

    /**
     * Name of the collection
     */
    private String name;

    /**
     * The unique id of the user who owns this collection
     */
    private String owner;

    /**
     * Creates a Collection object with the provided information
     *
     * @param id Unique identifier for the collection
     * @param name Name of the collection
     * @param owner The unique id of the user who owns this collection
     * @param lastUpdated The date and time the collection was last updated
     */
    public Collection(String id, String name, String owner, ZonedDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Creates a new Collection object (lastUpdated is set to now)
     *
     * @param id Unique identifier for the collection
     * @param name Name of the collection
     * @param owner The unique id of the user who owns this collection
     */
    public Collection(String id, String name, String owner) {
        this(id, name, owner, ZonedDateTime.now());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setLastUpdated();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setLastUpdated();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        setLastUpdated();
    }

    /**
     * Performs value equality on this Collection object and the given object
     *
     * @param o The other (Collection) object to compare to
     * @return True if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection collection = (Collection) o;
        return Objects.equals(id, collection.id) &&
                Objects.equals(name, collection.name) &&
                Objects.equals(owner, collection.owner);
    }

    /**
     * Returns a string representation of this Collection object
     *
     * @return A string representation of this Collection object
     */
    @Override
    public String toString() {
        return "Collection{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
