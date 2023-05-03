package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Wrapper class for a connection to the SQL database
 */
public class Database {

    /**
     * The connection to the database
     */
    private Connection conn;

    /**
     * Creates a new Database object
     */
    public Database() {
        conn = null;
    }

    /**
     * Opens a new connection to the database
     *
     * @return The connection to the database
     * @throws DataAccessException Thrown if there is an error opening the connection to the database
     */
    public Connection openConnection() throws DataAccessException {
        try {
            // The Structure for this Connection is driver:language:path
            // The path assumes you start in the root of your project unless given a full file path
            final String CONNECTION_URL = "jdbc:sqlite:db/SLDatabase.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    /**
     * Gets the connection to the database if there is one and opens a new connection if there is not
     *
     * @return The connection to the database
     * @throws DataAccessException Thrown if there is an error opening a connection to the database if there wasn't one already
     */
    public Connection getConnection() throws DataAccessException {
        if (conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    /**
     * Closes the connection to the database
     *
     * @param commit Flag that indicates whether to commit the changes to the database or rollback any changes made
     */
    public void closeConnection(boolean commit) {
        try {
            if (commit) {
                // This will commit the changes to the database
                conn.commit();
            } else {
                // If we find out something went wrong, pass a false into closeConnection and this
                // will rollback any changes we made during this connection
                conn.rollback();
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            // If you get here there are probably issues with your code and/or a connection is being left open
            e.printStackTrace();
        }
    }
}

