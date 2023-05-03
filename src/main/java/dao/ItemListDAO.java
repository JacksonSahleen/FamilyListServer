package dao;

import model.Category;
import model.Item;
import model.ItemList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Data Access Object (DAO) to access ItemList data in the database
 */
public class ItemListDAO extends DAO {

    /**
     * Creates a new ItemListDAO object
     *
     * @param conn The database connection this DAO uses to access the ItemList
     */
    public ItemListDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new ItemList into the ItemList table in the database
     *
     * @param itemList The ItemList to insert into the database
     * @throws DataAccessException If an error occurs while inserting into the database
     */
    public void insert(ItemList itemList) throws DataAccessException {
        String sql = "INSERT INTO ItemList (id, name, owner) " + "VALUES (?, ?, ?);";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemList.getId());
            stmt.setString(2, itemList.getName());
            stmt.setString(3, itemList.getOwner());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Finds an ItemList in the database with the given ID
     *
     * @param id The ID of the ItemList to find in the database
     * @return The ItemList with the given ID, or null if no such ItemList exists
     * @throws DataAccessException If an error occurs while finding the ItemList
     */
    public ItemList find(String id) throws DataAccessException {
        ItemList itemList;
        ResultSet rs;
        String sql = "SELECT * FROM ItemList WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // If the result set is not empty, create a new ItemList object
            if (rs.next()) {
                itemList = new ItemList(rs.getString("id"),
                                        rs.getString("name"),
                                        rs.getString("owner"));
            } else {
                itemList = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding the ItemList");
        }

        // Add items and categories to the ItemList object
        if (itemList != null) {
            itemList.setItems(findListItems(id));
            itemList.setCategories(findListCategories(id));
        }

        return itemList;
    }

    /**
     * Updates the given ItemList in the database
     *
     * @param itemList The ItemList to update in the database
     * @throws DataAccessException If an error occurs while updating the database
     */
    public void update(ItemList itemList) throws DataAccessException {
        String sql = "UPDATE ItemList SET name = ?, owner = ? WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemList.getName());
            stmt.setString(2, itemList.getOwner());
            stmt.setString(3, itemList.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating the database");
        }
    }

    /**
     * Removes the ItemList with the given ID from the database
     *
     * @param id The ID of the object to remove from the database
     * @throws DataAccessException If an error occurs while removing the object
     */
    public void remove(String id) throws DataAccessException {
        String sql = "DELETE FROM ItemList WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Removes all ItemList objects from the database
     *
     * @throws DataAccessException If an error occurs while removing the objects
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM ItemList;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Finds all Item objects in the database for the given ItemList
     *
     * @param id The ID of the ItemList to find Items for
     * @return A list of all Items in the database for the given ItemList
     * @throws DataAccessException If an error occurs while finding the Items
     */
    private List<Item> findListItems(String id) throws DataAccessException {
        Item item;
        ArrayList<Item> listItems = new ArrayList<>();
        ResultSet rs;
        String sql = "SELECT * FROM Item WHERE parentList = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // If a result was found, add it to the list
            while (rs.next()) {
                item = new Item(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("owner"),
                        rs.getString("category"),
                        rs.getString("parentList"),
                        rs.getBoolean("favorited"),
                        rs.getBoolean("completed"));
                listItems.add(item);
            }

            return listItems;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding the ItemList");
        }
    }

    /**
     * Finds all Category objects in the database for the given ItemList
     *
     * @param id The ID of the ItemList to find Categories for
     * @return A list of all Categories in the database for the given ItemList
     * @throws DataAccessException If an error occurs while finding the Categories
     */
    private List<Category> findListCategories(String id) throws DataAccessException {
        Category category;
        ArrayList<Category> listCategories = new ArrayList<>();
        ResultSet rs;
        String sql = "SELECT * FROM Category WHERE parentList = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // If a result was found, add it to the list
            while (rs.next()) {
                category = new Category(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("owner"),
                        rs.getString("parentList"));
                listCategories.add(category);
            }

            return listCategories;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding the ItemList");
        }
    }
}
