package result;

import model.Recipe;

import java.util.List;

/**
 * Stores the result information from the Recipe service
 */
public class RecipeResult extends Result {

    /**
     * The synchronized data from the server database
     */
    private List<Recipe> syncData;

    /**
     * Constructor for a successful RecipeResult class
     *
     * @param syncData The synchronized data from the server database
     */
    public RecipeResult(List<Recipe> syncData) {
        this.syncData = syncData;
        this.message = "Successfully retrieved recipes";
        this.success = true;
    }

    /**
     * Constructor for a failed RecipeResult class
     *
     * @param message The error message for the failed RecipeResult
     */
    public RecipeResult(String message) {
        this.message = message;
        this.success = false;
    }

    public List<Recipe> getSyncData() {
        return syncData;
    }
}
