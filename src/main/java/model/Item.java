package model;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Model class that contains information about an item
 */
public class Item extends Model {

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
     * Whether this item is completed or not
     */
    private boolean completed;

    /**
     * Creates an Item object with the provided information
     *
     * @param id Unique identifier for this item
     * @param name Name of this item
     * @param owner The unique id of the user that owns this item
     * @param category Category of this item
     * @param parentList The id of the list that this item belongs to
     * @param favorited Whether this item is favorited or not
     * @param completed Whether this item is completed or not
     * @param lastUpdated The date and time the item was last updated
     */
    public Item(String id, String name, String owner, String category, String parentList,
                boolean favorited, boolean completed, ZonedDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.category = category;
        this.parentList = parentList;
        this.favorited = favorited;
        this.completed = completed;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Creates a new Item object (no category, flags are set to false, and lastUpdated is set to now)
     *
     * @param id Unique identifier for this item
     * @param name Name of this item
     * @param owner The unique id of the user that owns this item
     * @param parentList The id of the list that this item belongs to
     */
    public Item(String id, String name, String owner, String parentList) {
        this(id, name, owner, null, parentList, false, false, ZonedDateTime.now());
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        setLastUpdated();
    }

    public String getParentList() {
        return parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
        setLastUpdated();
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
        setLastUpdated();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        setLastUpdated();
    }

    /**
     * Performs value equality on this Item object and another object
     *
     * @param o The other (Item) object to compare to
     * @return True if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(name, item.name) &&
                Objects.equals(owner, item.owner) &&
                Objects.equals(category, item.category) &&
                Objects.equals(parentList, item.parentList) &&
                favorited == item.favorited &&
                completed == item.completed;
    }

    /**
     * Returns a string representation of this Item object
     *
     * @return A string representation of this Item object
     */
    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", category='" + category + '\'' +
                ", parentList='" + parentList + '\'' +
                ", favorited=" + favorited +
                ", completed=" + completed +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
