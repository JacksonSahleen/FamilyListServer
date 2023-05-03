package model;

import java.util.Objects;

/**
 * Model class that contains information about a category
 */
public class Category {

    /**
     * Unique identifier for the category
     */
    private String id;

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
     * Creates a new category object
     *
     * @param id Unique identifier for the category
     * @param name Name of the category
     * @param owner The unique id of the user that owns this category
     * @param parentList The id of the list that this category belongs to
     */
    public Category(String id, String name, String owner, String parentList) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.parentList = parentList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getParentList() {
        return parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
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
}
