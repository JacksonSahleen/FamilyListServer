package result;

import model.ItemList;

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
     * Constructor for a successful ListResult class
     *
     * @param syncData The synchronized data from the server database
     */
    public ListResult(List<ItemList> syncData) {
        this.syncData = syncData;
        this.message = "Successfully retrieved lists";
        this.success = true;
    }

    /**
     * Constructor for a failed ListResult class
     *
     * @param message The error message for the failed ListResult
     */
    public ListResult(String message) {
        this.message = message;
        this.success = false;
    }

    public List<ItemList> getSyncData() {
        return syncData;
    }
}
