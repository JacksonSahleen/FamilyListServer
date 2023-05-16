package result;

import model.Collection;

import java.util.List;

/**
 * Stores the result information from the Collection service
 */
public class CollectionResult extends Result {

    /**
     * The synchronized data from the server database
     */
    private List<Collection> syncData;

    /**
     * Constructor for a successful CollectionResult class
     *
     * @param syncData The synchronized data from the server database
     */
    public CollectionResult(List<Collection> syncData) {
        this.syncData = syncData;
        this.message = "Successfully retrieved collections";
        this.success = true;
    }

    /**
     * Constructor for a failed CollectionResult class
     *
     * @param message The error message for the failed CollectionResult
     */
    public CollectionResult(String message) {
        this.message = message;
        this.success = false;
    }

    public List<Collection> getSyncData() {
        return syncData;
    }
}
