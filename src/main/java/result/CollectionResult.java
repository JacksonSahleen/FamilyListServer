package result;

import model.Collection;
import model.Permissions;

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
     * The synchronized recipe associations from the server database
     */
    private List<Permissions> syncAssociations;

    /**
     * Constructor for a CollectionResult class with data included
     *
     * @param syncData The synchronized data from the server database
     * @param syncAssociations The synchronized recipe associations from the server database
     */
    public CollectionResult(List<Collection> syncData, List<Permissions> syncAssociations) {
        this.syncData = syncData;
        this.syncAssociations = syncAssociations;
        this.message = "Successfully synchronized user collections";
        this.success = true;
    }

    /**
     * Constructor for a CollectionResult class with just a message
     *
     * @param message The error message for the failed CollectionResult
     * @param success The success status of the CollectionResult
     */
    public CollectionResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public List<Collection> getSyncData() {
        return syncData;
    }

    public List<Permissions> getSyncAssociations() {
        return syncAssociations;
    }
}
