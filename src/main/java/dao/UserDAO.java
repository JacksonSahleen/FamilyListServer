package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A Data Access Object (DAO) to access User data in the database
 */
public class UserDAO extends DAO {

    /**
     * Creates a new UserDAO object
     *
     * @param conn The database connection this DAO uses to access the User table
     */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /*-----------------------------------------------------
                Basic Database Interaction Methods
    -----------------------------------------------------*/

    /**
     * Inserts a new User into the User table in the database
     *
     * @param user The User to insert into the database
     * @throws DataAccessException If an error occurs while inserting into the database
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO User (username, password, firstName, lastName) " + "VALUES (?, ?, ?, ?);";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Finds a User in the database with the given username
     *
     * @param username The username of the User to find in the database
     * @return The User with the given username, or null if no such User exists
     * @throws DataAccessException If an error occurs while finding the User
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE username = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            // If a result was found, return it
            if(rs.next()) {
                user = new User(rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("firstName"),
                                rs.getString("lastName"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        }
    }

    /**
     * Updates the password of the given User in the database
     *
     * @param user The User to update in the database
     * @throws DataAccessException If an error occurs while updating the User
     */
    public void update(User user) throws DataAccessException {
        String sql = "UPDATE User SET password = ?, firstName = ?, lastName = ? WHERE username = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating user");
        }
    }

    /**
     * Removes the User with the given ID from the database
     *
     * @param user The User to remove from the database
     * @throws DataAccessException If an error occurs while removing the User from the database
     */
    public void remove(User user) throws DataAccessException {
        String sql = "DELETE FROM User WHERE username = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the User table");
        }
    }

    /**
     * Deletes all Users from the database
     *
     * @throws DataAccessException If an error occurs while deleting from the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM User;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the User table");
        }
    }

    /*-----------------------------------------------------
                User Content Gathering Methods
    -----------------------------------------------------*/

    /**
     * Finds the IDs of all ItemLists that the given user has access to
     *
     * @param username The username of the user to find lists for
     * @return A list of the IDs of all ItemLists that the user has access to
     * @throws DataAccessException If an error occurs while finding lists the user has access to
     */
    public List<String> findUserLists(String username) throws DataAccessException {
        ArrayList<String> lists = new ArrayList<String>();
        ResultSet rs;
        String sql = "SELECT * FROM ListPermissions WHERE user = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            // Add any ItemLists found to the list of ItemLists
            while(rs.next()) {
                lists.add(rs.getString("list"));
            }

            // Return null if no lists were found and the list of lists otherwise
            if (lists.size() == 0) {
                return null;
            } else {
                return lists;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding lists the user has access to");
        }
    }

    /**
     * Finds the IDs of all Recipes that the given user has access to
     *
     * @param username The username of the user to find recipes for
     * @return A list of the IDs of all Recipes that the user has access to
     * @throws DataAccessException If an error occurs while finding recipes the user has access to
     */
    public List<String> findUserRecipes(String username) throws DataAccessException {
        ArrayList<String> recipes = new ArrayList<String>();
        ResultSet rs;
        String sql = "SELECT * FROM RecipePermissions WHERE user = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            // Add any recipes found to the list of recipes
            while(rs.next()) {
                recipes.add(rs.getString("recipe"));
            }

            // Return null if no recipes were found and the list of recipes otherwise
            if (recipes.size() == 0) {
                return null;
            } else {
                return recipes;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding recipes the user has access to");
        }
    }
}
