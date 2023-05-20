package service;

import dao.*;
import result.ClearResult;

import java.sql.Connection;

/**
 * The service class responsible for clearing the database when requested.
 */
public class ClearService {

    /**
     * Default constructor for ClearService
     */
    public ClearService() {}

    /**
     * Clears the database of all data.
     *
     * @return A ClearResult object containing a message and a boolean indicating success
     */
    public ClearResult clearDatabase() {
        // Create new database object for clearing tables
        Database db = new Database();

        try {
            // Create new open connection
            Connection conn = db.openConnection();

            // Clear Item table
            ItemDAO iDao = new ItemDAO(conn);
            iDao.clear();

            // Clear Category table
            CategoryDAO cDao = new CategoryDAO(conn);
            cDao.clear();

            // Clear ItemList table
            ItemListDAO lDao = new ItemListDAO(conn);
            lDao.clear();

            // Clear Recipe table
            RecipeDAO rDao = new RecipeDAO(conn);
            rDao.clear();

            // Clear Collection table
            CollectionDAO coDao = new CollectionDAO(conn);
            coDao.clear();

            // Clear User table
            UserDAO uDao = new UserDAO(conn);
            uDao.clear();

            // Clear Authtoken table
            AuthtokenDAO aDao = new AuthtokenDAO(conn);
            aDao.clear();

            // Close connection and commit changes to database
            db.closeConnection(true);
            return new ClearResult("Clear succeeded.", true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
            return new ClearResult("Error: Internal server error.", false);
        }
    }
}
