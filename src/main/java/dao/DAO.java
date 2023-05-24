package dao;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;

/**
 * Base class for all DAO classes.
 */
public abstract class DAO {

    /**
     * The database connection this DAO uses to access the database
     */
    protected Connection conn;

    /**
     * The DateTimeFormatter used to convert between ZonedDateTime objects and Strings
     */
    protected final DateTimeFormatter dtFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    /**
     * Abstract constructor for the DAO class
     */
    public DAO(){
        conn = null;
    }

    /**
     * Clears the database of all data in the corresponding table(s)
     *
     * @throws DataAccessException If an error occurs while clearing the database
     */
    public abstract void clear() throws DataAccessException;

    public Connection getConnection() {
        return conn;
    }
}
