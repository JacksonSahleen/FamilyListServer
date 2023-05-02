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
     * Short description of this recipe
     */
    private String description;

    /**
     * Boolean flag indicating whether this recipe is public or not
     */
    private boolean isPublic;

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
     * @param description Short description of this recipe
     * @param isPublic Boolean flag indicating whether this recipe is public or not
     * @param ingredients List of ingredients for this recipe
     * @param steps List of steps for this recipe
     */
    public Recipe(String id, String name, String description, boolean isPublic, ItemList ingredients, ItemList steps) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.ingredients = ingredients;
        this.steps = steps;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
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
