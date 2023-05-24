package model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model class that contains information about a list of items.
 */
public class ItemList extends Model {

    /**
     * Name of the list
     */
    private String name;

    /**
     * The unique id of the user who owns this ItemList
     */
    private String owner;

    /**
     * List of items in the ItemList
     */
    private List<Item> items;

    /**
     * List of categories in the ItemList
     */
    private List<Category> categories;

    /**
     * Creates an ItemList object with the provided information
     *
     * @param id Unique identifier for the list
     * @param name Name of the list
     * @param owner The unique id of the user who owns this ItemList
     * @param items List of items in the ItemList
     * @param categories List of categories in the ItemList
     * @param lastUpdated The date and time the ItemList was last updated
     */
    public ItemList(String id, String name, String owner, List<Item> items,
                    List<Category> categories, ZonedDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.items = items;
        this.categories = categories;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Creates a new ItemList object (no items or categories and lastUpdated is set to now)
     *
     * @param id Unique identifier for the list
     * @param name Name of the list
     * @param owner The unique id of the user who owns this ItemList
     */
    public ItemList(String id, String name, String owner) {
        this(id, name, owner, new ArrayList<>(), new ArrayList<>(), ZonedDateTime.now());
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        setLastUpdated();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        setLastUpdated();
    }

    /**
     * Performs value equality on this ItemList object and another object
     *
     * @param o The other (ItemList) object to compare to
     * @return True if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemList itemList = (ItemList) o;
        return Objects.equals(id, itemList.id) &&
                Objects.equals(name, itemList.name) &&
                Objects.equals(owner, itemList.owner) &&
                Objects.equals(items, itemList.items) &&
                Objects.equals(categories, itemList.categories);
    }

    /**
     * Returns a string representation of this ItemList object
     *
     * @return String representation of this ItemList object
     */
    @Override
    public String toString() {
        return "ItemList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", items=" + items +
                ", categories=" + categories +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
