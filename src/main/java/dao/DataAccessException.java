package dao;

/**
 * Exception thrown when an error occurs while accessing the SQL database
 */
public class DataAccessException extends Exception {

    /**
     * Creates a new DataAccessException object
     *
     * @param message The error message to display
     */
    DataAccessException(String message) {
        super(message);
    }
}
