package result;

import model.Permissions;
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
     * The synchronized permissions from the server database
     */
    private List<Permissions> syncPermissions;

    /**
     * Constructor for a successful RecipeResult class
     *
     * @param syncData The synchronized data from the server database
     * @param syncPermissions The synchronized permissions from the server database
     */
    public RecipeResult(List<Recipe> syncData, List<Permissions> syncPermissions) {
        this.syncData = syncData;
        this.syncPermissions = syncPermissions;
        this.message = "Successfully synchronized user recipes";
        this.success = true;
    }

    /**
     * Constructor for a failed RecipeResult class
     *
     * @param message The error message for the failed RecipeResult
     * @param success The success of the RecipeResult
     */
    public RecipeResult(String message,  boolean success) {
        this.message = message;
        this.success = success;
    }

    public List<Recipe> getSyncData() {
        return syncData;
    }

    public List<Permissions> getSyncPermissions() {
        return syncPermissions;
    }
}
