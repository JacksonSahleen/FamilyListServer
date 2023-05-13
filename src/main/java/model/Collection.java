package model;

import java.util.Objects;

public class Collection {

    /**
     * Unique identifier for the collection
     */
    private String id;

    /**
     * Name of the collection
     */
    private String name;

    /**
     * The unique id of the user who owns this collection
     */
    private String owner;

    /**
     * Creates a new Collection object
     *
     * @param id Unique identifier for the collection
     * @param name Name of the collection
     * @param owner The unique id of the user who owns this collection
     */
    public Collection(String id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
                '}';
    }
}
