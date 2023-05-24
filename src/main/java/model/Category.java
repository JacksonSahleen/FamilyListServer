package model;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Model class that contains information about a category
 */
public class Category extends Model {

    /**
     * Name of the category
     */
    private String name;

    /**
     * The unique id of the user that owns this category
     */
    private String owner;

    /**
     * The id of the list that this category belongs to
     */
    private String parentList;

    /**
     * Creates a Category object with the provided information
     *
     * @param id Unique identifier for the category
     * @param name Name of the category
     * @param owner The unique id of the user that owns this category
     * @param parentList The id of the list that this category belongs to
     * @param lastUpdated The date and time the category was last updated
     */
    public Category(String id, String name, String owner, String parentList, ZonedDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.parentList = parentList;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Creates a new Category object (lastUpdated is set to now)
     *
     * @param id Unique identifier for the category
     * @param name Name of the category
     * @param owner The unique id of the user that owns this category
     * @param parentList The id of the list that this category belongs to
     */
    public Category(String id, String name, String owner, String parentList) {
        this(id, name, owner, parentList, ZonedDateTime.now());
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

    public String getParentList() {
        return parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
        setLastUpdated();
    }

    /**
     * Performs value equality on this Category object and another object
     *
     * @param o The other (Category) object to compare to
     * @return True if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name) &&
                Objects.equals(owner, category.owner) &&
                Objects.equals(parentList, category.parentList);
    }

    /**
     * Returns a string representation of this Category object
     *
     * @return A string representation of this Category object
     */
    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", parentList='" + parentList + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
