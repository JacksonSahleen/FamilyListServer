package model;

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
     * @param items List of items in the ItemList
     * @param categories List of categories in the ItemList
     */
    public ItemList(String id, String name, List<Item> items, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.categories = categories;
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
