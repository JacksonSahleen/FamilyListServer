package result;

/**
 * Stores the result information from the Load service
 */
public class LoadResult extends Result {

    /**
     * Constructor for a LoadResult object
     *
     * @param message message to be displayed to the user
     * @param success boolean representing whether the request was successful or not
     */
    public LoadResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
