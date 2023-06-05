package request;

import model.Permissions;
import model.Recipe;

import java.util.List;

/**
 * Stores the data for a recipe service request
 */
public class RecipeRequest {

    /**
     * The authtoken for the current session of the User making the request
     */
    private String authtoken;

    /**
     * The Recipe data for the request
     */
    private List<Recipe> data;

    /**
     * The permissions data for the request
     */
    private List<Permissions> permissions;

    /**
     * The Recipe removal data for the request
     */
    private List<String> removals;

    /**
     * The permission revocation data for the request
     */
    private List<Permissions> revocations;

    /**
     * Creates a ListRequest object with the given data
     *
     * @param authtoken The authtoken for the current session of the User making the request
     * @param data The List data for the request
     * @param permissions The permissions data for the request
     * @param removals The removal data for the request
     * @param revocations The revocation data for the request
     */
    public RecipeRequest(String authtoken, List<Recipe> data, List<Permissions> permissions,
                       List<String> removals, List<Permissions> revocations) {
        this.authtoken = authtoken;
        this.data = data;
        this.permissions = permissions;
        this.removals = removals;
        this.revocations = revocations;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public List<Recipe> getData() {
        return data;
    }

    public void setData(List<Recipe> data) {
        this.data = data;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRemovals() {
        return removals;
    }

    public void setRemovals(List<String> removals) {
        this.removals = removals;
    }

    public List<Permissions> getRevocations() {
        return revocations;
    }

    public void setRevocations(List<Permissions> revocations) {
        this.revocations = revocations;
    }
}
