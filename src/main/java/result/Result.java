package result;

/**
 * Base class for all service result objects
 */
public abstract class Result {

    /**
     * The message to be returned to the user
     */
    protected String message;

    /**
     * The success of the service operation
     */
    protected boolean success;

    /**
     * Abstract constructor for the Result class
     */
    public Result() {
        this.message = null;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
