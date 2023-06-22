package dao;

import model.Authtoken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Data Access Object (DAO) to access Authtoken data in the database
 */
public class AuthtokenDAO extends DAO {

    /**
     * Creates a new AuthtokenDAO object
     *
     * @param conn The database connection this DAO uses to access the Authtoken table
     */
    public AuthtokenDAO(Connection conn) {
        this.conn = conn;
    }

    /*-----------------------------------------------------
                Basic Database Interaction Methods
    -----------------------------------------------------*/

    /**
     * Inserts a new Authtoken into the Authtoken table in the database
     *
     * @param token The Authtoken to insert into the database
     * @throws DataAccessException If an error occurs while inserting into the Authtoken table
     */
    public void insert(Authtoken token) throws DataAccessException {
        String sql = "INSERT INTO Authtoken (token, user) " + "VALUES (?, ?);";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token.getToken());
            stmt.setString(2, token.getUserID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the Authtoken table");
        }
    }

    /**
     * Finds an Authtoken in the database with the given token
     *
     * @param token The token of the Authtoken to find in the database
     * @return The Authtoken with the given token, or null if no such Authtoken exists
     * @throws DataAccessException If an error occurs while finding the Authtoken
     */
    public Authtoken find(String token) throws DataAccessException {
        Authtoken authtoken;
        ResultSet rs;
        String sql = "SELECT * FROM Authtoken WHERE token = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();

            // If a result was found, return it
            if (rs.next()) {
                authtoken = new Authtoken(rs.getString("token"),
                                          rs.getString("user"));
                return authtoken;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding Authtoken");
        }
    }

    /**
     * Updates an Authtoken in the database
     *
     * @param token The Authtoken to update in the database
     * @throws DataAccessException If an error occurs while updating the Authtoken
     */
    public void update(Authtoken token) throws DataAccessException {
        String sql = "UPDATE Authtoken SET user = ? WHERE token = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token.getUserID());
            stmt.setString(2, token.getToken());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating Authtoken in the database");
        }
    }

    /**
     * Removes the given Authtoken from the database
     *
     * @param authtoken The Authtoken to remove from the database
     * @throws DataAccessException If an error occurs while removing from the Authtoken table
     */
    public void remove(Authtoken authtoken) throws DataAccessException {
        String sql = "DELETE FROM Authtoken WHERE token = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authtoken.getToken());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while removing from the Authtoken table");
        }
    }

    /**
     * Clears all data from the Authtoken table in the database
     *
     * @throws DataAccessException If an error occurs while clearing the Authtoken table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Authtoken;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the Authtoken table");
        }
    }

    /*-----------------------------------------------------
                    Authtoken Management Methods
    -----------------------------------------------------*/

    /**
     * Finds all Authtokens in the database for the given user
     *
     * @param id The unique ID of the user to find Authtokens for
     * @return A list of all Authtokens for the given user
     * @throws DataAccessException If an error occurs while finding the Authtokens
     */
    public List<Authtoken> findUserAuthtokens(String id) throws DataAccessException {
        Authtoken authtoken;
        ArrayList<Authtoken> userTokens = new ArrayList<>();
        ResultSet rs;
        String sql = "SELECT * FROM Authtoken WHERE user = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // If a result was found, add it to the list
            while (rs.next()) {
                authtoken = new Authtoken(rs.getString("token"),
                        rs.getString("user"));
                userTokens.add(authtoken);
            }

            // If any results were found, return the list
            return userTokens;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding Authtoken");
        }
    }
}
