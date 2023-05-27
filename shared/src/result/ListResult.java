package result;

import model.ItemList;
import model.Permissions;

import java.util.List;

/**
 * Stores the result information from the List service
 */
public class ListResult extends Result {

    /**
     * The synchronized data from the server database
     */
    private List<ItemList> syncData;

    /**
     * The synchronized permissions from the server database
     */
    private List<Permissions> syncPermissions;

    /**
     * Constructor for a successful ListResult class
     *
     * @param syncData The synchronized data from the server database
     * @param syncPermissions The synchronized permissions from the server database
     */
    public ListResult(List<ItemList> syncData, List<Permissions> syncPermissions) {
        this.syncData = syncData;
        this.syncPermissions = syncPermissions;
        this.message = "Successfully synchronized user lists";
        this.success = true;
    }

    /**
     * Constructor for a failed ListResult class
     *
     * @param message The error message for the failed ListResult
     * @param success The success status of the ListResult
     */
    public ListResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public List<ItemList> getSyncData() {
        return syncData;
    }

    public List<Permissions> getSyncPermissions() {
        return syncPermissions;
    }
}
