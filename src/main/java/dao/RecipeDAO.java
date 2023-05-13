package dao;

import model.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO extends DAO {

    /**
     * Creates a new RecipeDAO object
     *
     * @param conn The database connection this DAO uses to access the Recipe
     */
    public RecipeDAO(Connection conn) {
        this.conn = conn;
    }

    /*-----------------------------------------------------
                Basic Database Interaction Methods
    -----------------------------------------------------*/

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

            // Set the id of the list of ingredients if it exists
            if (recipe.getIngredients() != null) {
                stmt.setString(5, recipe.getIngredients().getId());
            } else {
                stmt.setString(5, null);
            }

            // Set the id of the list of steps if it exists
            if (recipe.getSteps() != null) {
                stmt.setString(6, recipe.getSteps().getId());
            } else {
                stmt.setString(6, null);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }

        // Add a row to the RecipePermissions table for the owner of the Recipe
        sql = "INSERT INTO RecipePermissions (recipe, user) VALUES (?, ?);";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipe.getId());
            stmt.setString(2, recipe.getOwner());

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
            stmt.setString(6, recipe.getId());

            if (recipe.getIngredients() != null) {
                stmt.setString(4, recipe.getIngredients().getId());
            } else {
                stmt.setString(4, null);
            }

            if (recipe.getSteps() != null) {
                stmt.setString(5, recipe.getSteps().getId());
            } else {
                stmt.setString(5, null);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating the database");
        }
    }

    /**
     * Removes an existing Recipe from the Recipe table in the database
     *
     * @param recipe The Recipe to remove from the database
     * @param username The username of the user removing the Recipe
     * @throws DataAccessException If an error occurs while removing from the database
     */
    public void remove(Recipe recipe, String username) throws DataAccessException {
        // Remove user permissions from the RecipePermissions table
        String sql = "DELETE FROM RecipePermissions WHERE recipe = ? AND user = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipe.getId());
            stmt.setString(2, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }

        // Remove the recipe's data if no other users have access to it
        if (findUsersWithAccess(recipe.getId()).isEmpty()) {
            sql = "DELETE FROM Recipe WHERE id = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, recipe.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered while deleting from the database");
            }
        }
    }

    /**
     * Clears all data from the Recipe table in the database
     *
     * @throws DataAccessException If an error occurs while clearing the database
     */
    public void clear() throws DataAccessException {
        // Clear all Recipes from the database
        String sql = "DELETE FROM Recipe;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }

        // Clear all RecipePermissions from the database
        sql = "DELETE FROM RecipePermissions;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /*-----------------------------------------------------
                        Permissions Methods
    -----------------------------------------------------*/

    /**
     * Shares a Recipe with another user
     *
     * @param recipe The Recipe to share
     * @param username The username of the user to share the Recipe with
     * @throws DataAccessException If an error occurs while sharing the Recipe
     */
    public void share(Recipe recipe, String username) throws DataAccessException {
        // Find all users with access to the Recipe
        List<String> users = findUsersWithAccess(recipe.getId());

        // Add the user to the permissions if they don't already have access
        if (!users.contains(username)) {
            String sql = "INSERT INTO RecipePermissions (recipe, user) VALUES (?, ?);";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, recipe.getId());
                stmt.setString(2, username);

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered while inserting into the database");
            }
        }
    }

    /**
     * Finds all users with access to a given Recipe
     *
     * @param id The ID of the Recipe to find users with access to
     * @return A list of usernames of users with access to the Recipe
     * @throws DataAccessException If an error occurs while finding the users with access
     */
    public List<String> findUsersWithAccess(String id) throws DataAccessException {
        List<String> users = new ArrayList<>();
        ResultSet rs;
        String sql = "SELECT user FROM RecipePermissions WHERE recipe = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // If a result was found, add it to the list
            while (rs.next()) {
                users.add(rs.getString("user"));
            }

            // Return the list of users with access to the Recipe
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding the Recipe");
        }
    }
}
