package dao;

import model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A Data Access Object (DAO) to access Item data in the database
 */
public class ItemDAO extends DAO {

    /**
     * Creates a new ItemDAO object
     *
     * @param conn The database connection this DAO uses to access the Item table
     */
    public ItemDAO(Connection conn) {
        this.conn = conn;
    }

    /*-----------------------------------------------------
                Basic Database Interaction Methods
    -----------------------------------------------------*/

    /**
     * Inserts a new Item into the Item table in the database
     *
     * @param item The Item to insert into the database
     * @throws DataAccessException If an error occurs while inserting into the database
     */
    public void insert(Item item) throws DataAccessException {
        String sql = "INSERT INTO Item (id, name, owner, itemCategory, parentList, favorited, completed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getId());
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getOwner());
            stmt.setString(5, item.getParentList());
            stmt.setBoolean(6, item.isFavorited());
            stmt.setBoolean(7, item.isCompleted());

            if (item.getCategory() == null) {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(4, item.getCategory());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Finds an Item in the database with the given id
     *
     * @param id The id of the Item to find in the database
     * @return The Item with the given id, or null if no such Item exists
     * @throws DataAccessException If an error occurs while finding the Item
     */
    public Item find(String id) throws DataAccessException {
        Item item;
        ResultSet rs;
        String sql = "SELECT * FROM Item WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // If an Item was found with the given id
            if (rs.next()) {
                item = new Item(rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("owner"),
                                rs.getString("itemCategory"),
                                rs.getString("parentList"),
                                rs.getBoolean("favorited"),
                                rs.getBoolean("completed"));
                return item;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding the Item");
        }
    }

    /**
     * Updates an Item in the database with the given id
     * @param item The Item to update in the database
     * @throws DataAccessException If an error occurs while updating the Item
     */
    public void update(Item item) throws DataAccessException {
        String sql = "UPDATE Item SET name = ?, owner = ?, itemCategory = ?, parentList = ?, " +
                "favorited = ?, completed = ? WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getOwner());
            stmt.setString(4, item.getParentList());
            stmt.setBoolean(5, item.isFavorited());
            stmt.setBoolean(6, item.isCompleted());
            stmt.setString(7, item.getId());

            if (item.getCategory() == null) {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(3, item.getCategory());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating the database");
        }
    }

    /**
     * Removes an Item from the database with the given id
     *
     * @param item The Item to remove from the database
     * @throws DataAccessException If an error occurs while removing the Item
     */
    public void remove(Item item) throws DataAccessException {
        String sql = "DELETE FROM Item WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Clears all Items from the database
     *
     * @throws DataAccessException If an error occurs while clearing the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Item;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }
}
