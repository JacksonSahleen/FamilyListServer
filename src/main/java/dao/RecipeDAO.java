package dao;

import model.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeDAO extends DAO {

    /**
     * Creates a new RecipeDAO object
     *
     * @param conn The database connection this DAO uses to access the Recipe
     */
    public RecipeDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new Recipe into the Recipe table in the database
     *
     * @param recipe The Recipe to insert into the database
     * @throws DataAccessException If an error occurs while inserting into the database
     */
    public void insert(Recipe recipe) throws DataAccessException {
        String sql = "INSERT INTO Recipe (id, name, owner, description, ingredients, steps) "
                + "VALUES (?, ?, ?, ?, ?, ?);";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipe.getId());
            stmt.setString(2, recipe.getName());
            stmt.setString(3, recipe.getOwner());
            stmt.setString(4, recipe.getDescription());
            stmt.setString(5, recipe.getIngredients().getId());
            stmt.setString(6, recipe.getSteps().getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Finds a Recipe in the database with the given ID
     *
     * @param id The ID of the Recipe to find in the database
     * @return The Recipe with the given ID, or null if no such Recipe exists
     * @throws DataAccessException If an error occurs while finding the Recipe
     */
    public Recipe find(String id) throws DataAccessException {
        Recipe recipe;
        ResultSet rs;
        String ingredientsID = null;
        String stepsID = null;
        String sql = "SELECT * FROM Recipe WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                recipe = new Recipe(rs.getString("id"),
                                    rs.getString("name"),
                                    rs.getString("owner"),
                                    rs.getString("description"));
                ingredientsID = rs.getString("ingredients");
                stepsID = rs.getString("steps");
            } else {
                recipe = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding the Recipe");
        }

        // Add ingredients and steps to the recipe
        if (recipe != null) {
            ItemListDAO lDAO = new ItemListDAO(conn);
            recipe.setIngredients(lDAO.find(ingredientsID));
            recipe.setSteps(lDAO.find(stepsID));
        }

        return recipe;
    }

    /**
     * Updates an existing Recipe in the Recipe table in the database
     *
     * @param recipe The Recipe to update in the database
     * @throws DataAccessException If an error occurs while updating the database
     */
    public void update(Recipe recipe) throws DataAccessException {
        String sql = "UPDATE Recipe SET name = ?, owner = ?, description = ?, ingredients = ?, steps = ? WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getOwner());
            stmt.setString(3, recipe.getDescription());
            stmt.setString(4, recipe.getIngredients().getId());
            stmt.setString(5, recipe.getSteps().getId());
            stmt.setString(6, recipe.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating the database");
        }
    }

    /**
     * Removes an existing Recipe from the Recipe table in the database
     *
     * @param id The ID of the object to remove from the database
     * @throws DataAccessException If an error occurs while removing from the database
     */
    public void remove(String id) throws DataAccessException {
        String sql = "DELETE FROM Recipe WHERE id = ?;";

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
     * Clears all data from the Recipe table in the database
     *
     * @throws DataAccessException If an error occurs while clearing the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Recipe;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }
}
