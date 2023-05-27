package model;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Model class that contains information about a recipe
 */
public class Recipe extends Model {

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
     * Creates a recipe object with the provided information
     *
     * @param id Unique identifier for this recipe
     * @param name Name of this recipe
     * @param owner The unique id of the user who owns this recipe
     * @param description Short description of this recipe
     * @param ingredients List of ingredients for this recipe
     * @param steps List of steps for this recipe
     * @param lastUpdated The date and time the recipe was last updated
     */
    public Recipe(String id, String name, String owner, String description, ItemList ingredients,
                  ItemList steps, ZonedDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Creates a new recipe object (no ingredients or steps and lastUpdated is set to now)
     *
     * @param id Unique identifier for this recipe
     * @param name Name of this recipe
     * @param owner The unique id of the user who owns this recipe
     * @param description Short description of this recipe
     */
    public Recipe(String id, String name, String owner, String description) {
        this(id, name, owner, description, null, null, ZonedDateTime.now());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        setLastUpdated();
    }

    public ItemList getIngredients() {
        return ingredients;
    }

    public void setIngredients(ItemList ingredients) {
        this.ingredients = ingredients;
        setLastUpdated();
    }

    public ItemList getSteps() {
        return steps;
    }

    public void setSteps(ItemList steps) {
        this.steps = steps;
        setLastUpdated();
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
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    /**
     * Returns a hash code for this Recipe object
     *
     * @return A hash code for this Recipe object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, description, ingredients, steps);
    }
}
