package model;

/**
 * Model class that contains information about an item
 */
public class Item {

    /**
     * Unique identifier for the item
     */
    private String id;

    /**
     * Name of the item
     */
    private String name;

    /**
     * Category of the item
     */
    private String category;

    public Item(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
