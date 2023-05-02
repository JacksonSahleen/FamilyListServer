package model;

/**
 * Model class that contains information about a recipe
 */
public class Recipe {

    /**
     * Unique identifier for this recipe
     */
    private String id;

    /**
     * Name of this recipe
     */
    private String name;

    /**
     * The unique id of the user who owns this recipe
     */
    private String owner;

    /**
     * Short description of this recipe
     */
    private String description;

    /**
     * List of ingredients for this recipe
     */
    private ItemList ingredients;

    /**
     * List of steps for this recipe
     */
    private ItemList steps;

    /**
     * Creates a new recipe object
     *
     * @param id Unique identifier for this recipe
     * @param name Name of this recipe
     * @param owner The unique id of the user who owns this recipe
     * @param description Short description of this recipe
     * @param ingredients List of ingredients for this recipe
     * @param steps List of steps for this recipe
     */
    public Recipe(String id, String name, String owner, String description, ItemList ingredients, ItemList steps) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    /**
     * Creates a new recipe object with no ingredients or steps
     *
     * @param id Unique identifier for this recipe
     * @param name Name of this recipe
     * @param owner The unique id of the user who owns this recipe
     * @param description Short description of this recipe
     */
    public Recipe(String id, String name, String owner, String description) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.ingredients = null;
        this.steps = null;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemList getIngredients() {
        return ingredients;
    }

    public void setIngredients(ItemList ingredients) {
        this.ingredients = ingredients;
    }

    public ItemList getSteps() {
        return steps;
    }

    public void setSteps(ItemList steps) {
        this.steps = steps;
    }
}
