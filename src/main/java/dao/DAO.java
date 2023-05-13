package dao;

import java.sql.Connection;

/**
 * Base class for all DAO classes.
 */
public abstract class DAO {

    /**
     * The database connection this DAO uses to access the database
     */
    protected Connection conn;

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
