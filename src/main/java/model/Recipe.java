package model;

import java.util.Objects;

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

    /**
     * Performs value equality on this Recipe object and another object
     *
     * @param o The other (Recipe) object to compare to
     * @return True if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) &&
                Objects.equals(name, recipe.name) &&
                Objects.equals(owner, recipe.owner) &&
                Objects.equals(description, recipe.description) &&
                Objects.equals(ingredients, recipe.ingredients) &&
                Objects.equals(steps, recipe.steps);
    }

    /**
     * Returns a string representation of this Recipe object
     *
     * @return A string representation of this Recipe object
     */
    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                '}';
    }
}
