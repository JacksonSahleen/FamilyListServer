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
}
