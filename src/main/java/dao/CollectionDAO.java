package dao;

import model.Collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CollectionDAO extends DAO {

    /**
     * Creates a new CollectionDAO object
     *
     * @param conn The database connection this DAO uses to access the Collection
     */
    public CollectionDAO(Connection conn) {
        this.conn = conn;
    }

    /*-----------------------------------------------------
                Basic Database Interaction Methods
    -----------------------------------------------------*/

    /**
     * Inserts a new Collection into the Collection table in the database
     *
     * @param collection The Collection to insert into the database
     * @throws DataAccessException If an error occurs while inserting into the database
     */
    public void insert(Collection collection) throws DataAccessException {
        String sql = "INSERT INTO Collection (id, name, owner) VALUES (?, ?, ?);";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collection.getId());
            stmt.setString(2, collection.getName());
            stmt.setString(3, collection.getOwner());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the Collection table");
        }
    }

    /**
     * Finds a Collection in the database with the given ID
     *
     * @param id The ID of the Collection to find in the database
     * @return The Collection with the given ID, or null if no such Collection exists
     * @throws DataAccessException If an error occurs while finding the Collection
     */
    public Collection find(String id) throws DataAccessException {
        Collection collection;
        ResultSet rs;
        String sql = "SELECT * FROM Collection WHERE id = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // Check if a Collection was found
            if (rs.next()) {
                collection = new Collection(rs.getString("id"),
                                            rs.getString("name"),
                                            rs.getString("owner"));
                return collection;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a Collection in the database");
        }
    }

    /**
     * Updates a Collection in the database
     *
     * @param collection The Collection to update in the database
     * @throws DataAccessException If an error occurs while updating the Collection
     */
    public void update(Collection collection) throws DataAccessException {
        String sql = "UPDATE Collection SET name = ?, owner = ? WHERE id = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collection.getName());
            stmt.setString(2, collection.getOwner());
            stmt.setString(3, collection.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating the Collection table");
        }
    }

    /**
     * Removes a Collection from the database
     *
     * @param collection The Collection to remove from the database
     * @throws DataAccessException If an error occurs while removing the Collection
     */
    public void remove(Collection collection) throws DataAccessException {
        String sql = "DELETE FROM Collection WHERE id = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collection.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while removing from the Collection table");
        }
    }

    /**
     * Clears all Collections from the database
     *
     * @throws DataAccessException If an error occurs while clearing the Collections
     */
    public void clear() throws DataAccessException {
        // Clear all Collections from the database
        String sql = "DELETE FROM Collection;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the Collection table");
        }

        // Clear all CollectionRecipes from the database
        sql = "DELETE FROM CollectionRecipes;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the CollectionRecipe table");
        }
    }
}
