package model;

/**
 * Model class that contains information about an item
 */
public class Item {

    /**
     * Unique identifier for this item
     */
    private String id;

    /**
     * Name of this item
     */
    private String name;

    /**
     * The unique id of the user that owns this item
     */
    private String owner;

    /**
     * Category of this item
     */
    private String category;

    /**
     * The id of the list that this item belongs to
     */
    private String parentList;

    /**
     * Whether this item is favorited or not
     */
    private boolean favorited;

    /**
     * Creates a new item with the given parameters
     *
     * @param id Unique identifier for this item
     * @param name Name of this item
     * @param owner The unique id of the user that owns this item
     * @param category Category of this item
     * @param parentList The id of the list that this item belongs to
     * @param favorited Whether this item is favorited or not
     */
    public Item(String id, String name, String owner, String category, String parentList, boolean favorited) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.category = category;
        this.parentList = parentList;
        this.favorited = favorited;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParentList() {
        return parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
