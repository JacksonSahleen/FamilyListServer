package model;

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
     * Creates a new category object
     *
     * @param id Unique identifier for the category
     * @param name Name of the category
     * @param parentList Unique identifier for this category's parent list
     */
    public Category(String id, String name, String parentList) {
        this.id = id;
        this.name = name;
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
}
