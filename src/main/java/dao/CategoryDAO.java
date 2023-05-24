package dao;

import model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * A Data Access Object (DAO) to access Category data in the database
 */
public class CategoryDAO extends DAO {

    /**
     * Creates a new CategoryDAO object
     *
     * @param conn The database connection this DAO uses to access the Category table
     */
    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    /*-----------------------------------------------------
                Basic Database Interaction Methods
    -----------------------------------------------------*/

    /**
     * Inserts a new Category into the Category table in the database
     *
     * @param category The Category to insert into the database
     * @throws DataAccessException If an error occurs while inserting into the database
     */
    public void insert(Category category) throws DataAccessException {
        String sql = "INSERT INTO Category (id, name, owner, parentList, lastUpdated) " + "VALUES (?, ?, ?, ?, ?);";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getId());
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getOwner());
            stmt.setString(4, category.getParentList());
            stmt.setString(5, category.getLastUpdated().format(dtFormatter));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting Category into the database");
        }
    }

    /**
     * Finds a Category in the database with the given ID
     *
     * @param id The ID of the Category to find in the database
     * @return The Category with the given ID, or null if no such Category exists
     * @throws DataAccessException If an error occurs while finding the Category
     */
    public Category find(String id) throws DataAccessException {
        Category category;
        ResultSet rs;
        String sql = "SELECT * FROM Category WHERE id = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // If a result was found, return the Category object
            if (rs.next()) {
                category = new Category(rs.getString("id"),
                                        rs.getString("name"),
                                        rs.getString("owner"),
                                        rs.getString("parentList"),
                                        ZonedDateTime.parse(rs.getString("lastUpdated")));
                return category;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding Category in the database");
        }
    }

    /**
     * Updates a Category in the database
     *
     * @param category The Category to update in the database
     * @throws DataAccessException If an error occurs while updating the database
     */
    public void update(Category category) throws DataAccessException {
        String sql = "UPDATE Category SET name = ?, owner = ?, parentList = ?, lastUpdated = ? WHERE id = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getOwner());
            stmt.setString(3, category.getParentList());
            stmt.setString(4, category.getLastUpdated().format(dtFormatter));
            stmt.setString(5, category.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating Category in the database");
        }
    }

    /**
     * Removes a Category from the Category table in the database
     *
     * @param category The Category to remove from the database
     * @throws DataAccessException If an error occurs while removing from the database
     */
    public void remove(Category category) throws DataAccessException {
        String sql = "DELETE FROM Category WHERE id = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while removing Category from the database");
        }
    }

    /**
     * Clears all data from the Category table in the database
     *
     * @throws DataAccessException If an error occurs while clearing the Category table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Category;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing Categories from the database");
        }
    }
}
