package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class that contains information about a list of items.
 */
public class ItemList {

    /**
     * Unique identifier for the list
     */
    private String id;

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
     * Creates a new ItemList object
     *
     * @param id Unique identifier for the list
     * @param name Name of the list
     * @param owner The unique id of the user who owns this ItemList
     * @param items List of items in the ItemList
     * @param categories List of categories in the ItemList
     */
    public ItemList(String id, String name, String owner, List<Item> items, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.items = items;
        this.categories = categories;
    }

    /**
     * Creates a new ItemList object without any items or categories
     *
     * @param id Unique identifier for the list
     * @param name Name of the list
     * @param owner The unique id of the user who owns this ItemList
     */
    public ItemList(String id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.items = new ArrayList<>();
        this.categories = new ArrayList<>();
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
