package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    /**
     * Inserts a new User into the User table in the database
     *
     * @param user The User to insert into the database
     * @throws DataAccessException If an error occurs while inserting into the database
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO User (id, username, password) " + "VALUES (?, ?, ?);";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Finds a User in the database with the given username
     *
     * @param id The unique id of the User to find in the database
     * @return The User with the given username, or null if no such User exists
     * @throws DataAccessException If an error occurs while finding the User
     */
    public User find(String id) throws DataAccessException {
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // If a result was found, return it
            if(rs.next()) {
                user = new User(rs.getString("id"),
                                rs.getString("username"),
                                rs.getString("password"));
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
        String sql = "UPDATE User SET username = ?, password = ? WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating user");
        }
    }

    /**
     * Removes the User with the given ID from the database
     *
     * @param id The ID of the User to remove from the database
     * @throws DataAccessException If an error occurs while removing the User from the database
     */
    public void remove(String id) throws DataAccessException {
        String sql = "DELETE FROM User WHERE id = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
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
}
