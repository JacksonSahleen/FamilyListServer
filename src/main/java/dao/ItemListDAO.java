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

    /*-----------------------------------------------------
                Basic Database Interaction Methods
    -----------------------------------------------------*/

    /**
     * Inserts a new ItemList into the ItemList table in the database
     *
     * @param itemList The ItemList to insert into the database
     * @throws DataAccessException If an error occurs while inserting into the database
     */
    public void insert(ItemList itemList) throws DataAccessException {
        // Insert a row into the ItemList table
        String sql = "INSERT INTO ItemList (id, name, owner) VALUES (?, ?, ?);";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemList.getId());
            stmt.setString(2, itemList.getName());
            stmt.setString(3, itemList.getOwner());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }

        // Insert a row into the ListPermissions for the owner of the ItemList
        sql = "INSERT INTO ListPermissions (list, user) VALUES (?, ?);";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemList.getId());
            stmt.setString(2, itemList.getOwner());

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
     * @param list The ItemList to remove from the database
     * @param username The username of the user removing the object
     * @throws DataAccessException If an error occurs while removing the object
     */
    public void remove(ItemList list, String username) throws DataAccessException {
        // Remove user permissions from the ListPermissions table
        String sql = "DELETE FROM ListPermissions WHERE list = ? AND user = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, list.getId());
            stmt.setString(2, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }

        // Remove the list's data if no other users have access to it
        if (findUsersWithAccess(list.getId()).isEmpty()) {
            sql = "DELETE FROM ItemList WHERE id = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, list.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered while deleting from the database");
            }
        }
    }

    /**
     * Removes all ItemList objects from the database
     *
     * @throws DataAccessException If an error occurs while removing the objects
     */
    public void clear() throws DataAccessException {
        // Clear all lists from the ItemList table
        String sql = "DELETE FROM ItemList;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }

        // Clear all rows from the ListPermissions table
        sql = "DELETE FROM ListPermissions;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /*-----------------------------------------------------
                    Private Helper Functions
    -----------------------------------------------------*/

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

    /*-----------------------------------------------------
                        Permissions Methods
    -----------------------------------------------------*/

    /**
     * Shares an ItemList with another user
     *
     * @param itemList The ItemList to share
     * @param username The username of the user to share the ItemList with
     * @throws DataAccessException If an error occurs while sharing the ItemList
     */
    public void share(ItemList itemList, String username) throws DataAccessException {
        // Find all users with access to the ItemList
        List<String> users = findUsersWithAccess(itemList.getId());

        // Add the user to the permissions if they don't already have access
        if (!users.contains(username)) {
            String sql = "INSERT INTO ListPermissions (list, user) VALUES (?, ?);";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, itemList.getId());
                stmt.setString(2, username);

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered while inserting into the database");
            }
        }
    }

    /**
     * Finds all users with access to the given ItemList
     *
     * @param id The ID of the ItemList to find users with access to
     * @return A list of all users with access to the given ItemList
     * @throws DataAccessException If an error occurs while finding the users with access
     */
    public List<String> findUsersWithAccess(String id) throws DataAccessException {
        List<String> users = new ArrayList<>();
        ResultSet rs;
        String sql = "SELECT user FROM ListPermissions WHERE list = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // If a result was found, add it to the list
            while (rs.next()) {
                users.add(rs.getString("user"));
            }

            // Return the list of users with access to the ItemList
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding the ItemList");
        }
    }
}
