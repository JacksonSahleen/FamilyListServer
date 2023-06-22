package dao;

import model.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A Data Access Object (DAO) to access Recipe data in the database
 */
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
        String sql = "INSERT INTO Recipe (id, name, owner, description, ingredients, steps, lastUpdated) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipe.getId());
            stmt.setString(2, recipe.getName());
            stmt.setString(3, recipe.getOwner());
            stmt.setString(4, recipe.getDescription());
            stmt.setString(7, recipe.getLastUpdated().format(dtFormatter));

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
            throw new DataAccessException("Error encountered while inserting Recipe into the database");
        }

        // Add a row to the RecipePermissions table for the owner of the Recipe
        sql = "INSERT INTO RecipePermissions (recipe, user) VALUES (?, ?);";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipe.getId());
            stmt.setString(2, recipe.getOwner());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting RecipePermission into the database");
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
                                    rs.getString("description"),
                                    null,
                                    null,
                                    ZonedDateTime.parse(rs.getString("lastUpdated")));
                ingredientsID = rs.getString("ingredients");
                stepsID = rs.getString("steps");
            } else {
                recipe = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding the Recipe in the database");
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
        String sql = "UPDATE Recipe SET name = ?, owner = ?, description = ?, ingredients = ?, steps = ?, " +
                "lastUpdated = ? WHERE id = ?;";

        // Execute the SQL statement
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getOwner());
            stmt.setString(3, recipe.getDescription());
            stmt.setString(6, recipe.getLastUpdated().format(dtFormatter));
            stmt.setString(7, recipe.getId());

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
            throw new DataAccessException("Error encountered while updating Recipe in the database");
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
            throw new DataAccessException("Error encountered while deleting RecipePermission from the database");
        }

        // Remove the recipe's data if no other users have access to it
        if (findUsersWithAccess(recipe.getId()).isEmpty()) {
            sql = "DELETE FROM Recipe WHERE id = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, recipe.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered while deleting Recipe from the database");
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
            throw new DataAccessException("Error encountered while clearing Recipes from the database");
        }

        // Clear all RecipePermissions from the database
        sql = "DELETE FROM RecipePermissions;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing RecipePermissions from the database");
        }
    }

    /*-----------------------------------------------------
                        Permissions Methods
    -----------------------------------------------------*/

    /**
     * Shares a Recipe with another user
     *
     * @param recipeID The ID of the Recipe to share
     * @param username The username of the user to share the Recipe with
     * @throws DataAccessException If an error occurs while sharing the Recipe
     */
    public void share(String recipeID, String username) throws DataAccessException {
        // Find all users with access to the Recipe
        List<String> users = findUsersWithAccess(recipeID);

        // Add the user to the permissions if they don't already have access
        if (!users.contains(username)) {
            String sql = "INSERT INTO RecipePermissions (recipe, user) VALUES (?, ?);";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, recipeID);
                stmt.setString(2, username);

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered while inserting RecipePermissions into the database");
            }
        }
    }

    /**
     * Unshares a Recipe with another user
     *
     * @param recipeID The ID of the Recipe to unshare
     * @param username The username of the user to unshare the Recipe with
     */
    public void unshare(String recipeID, String username) throws DataAccessException {
        //Find all the users with access to the Recipe
        List<String> users = findUsersWithAccess(recipeID);

        //Remove the user from the permissions if they have access
        if (users.contains(username)) {
            String sql = "DELETE FROM RecipePermissions WHERE recipe = ? AND user = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, recipeID);
                stmt.setString(2, username);

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error encountered while deleting RecipePermissions from the database");
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

    /*-----------------------------------------------------
                User Specific Database Methods
    -----------------------------------------------------*/

    /**
     * Finds the IDs of all Recipes that the given user has access to
     *
     * @param username The username of the user to find recipes for
     * @return A list of the IDs of all Recipes that the user has access to
     * @throws DataAccessException If an error occurs while finding recipes the user has access to
     */
    public List<String> findUserRecipes(String username) throws DataAccessException {
        ArrayList<String> recipes = new ArrayList<>();
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
