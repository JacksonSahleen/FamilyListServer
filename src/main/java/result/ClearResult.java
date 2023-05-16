package result;

/**
 * Stores the result information from the Clear service
 */
public class ClearResult extends Result {

    /**
     * Constructor for a ClearResult object
     *
     * @param message message to be displayed to the user
     * @param success boolean representing whether the request was successful or not
     */
    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
