package dao;

import model.Collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /*-----------------------------------------------------
                Collection Membership Methods
    -----------------------------------------------------*/

    /**
     * Adds a recipe to a collection
     *
     * @param collectionID The ID of the collection to add the recipe to
     * @param recipeID The ID of the recipe to add to the collection
     * @throws DataAccessException If an error occurs while adding the recipe to the collection
     */
    public void addCollectionRecipe(String collectionID, String recipeID) throws DataAccessException {
        // Check that neither the collection nor the recipe are null
        if (collectionID == null || recipeID == null) {
            throw new DataAccessException("Error: Cannot add null collection or recipe");
        }

        // Check that the collection and recipe exist
        if (find(collectionID) == null || new RecipeDAO(conn).find(recipeID) == null) {
            throw new DataAccessException("Error: Cannot add recipe to nonexistent collection");
        }

        // Insert the collection-recipe pair into the database
        String sql = "INSERT INTO CollectionRecipes (collection, recipe) VALUES (?, ?);";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collectionID);
            stmt.setString(2, recipeID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while adding a recipe to a collection");
        }
    }

    /**
     * Removes a recipe from a collection
     *
     * @param collectionID The ID of the collection to remove the recipe from
     * @param recipeID The ID of the recipe to remove from the collection
     * @throws DataAccessException If an error occurs while removing the recipe from the collection
     */
    public void removeCollectionRecipe(String collectionID, String recipeID) throws DataAccessException {
        // Check that neither the collection nor the recipe are null
        if (collectionID == null || recipeID == null) {
            throw new DataAccessException("Error: Cannot remove null collection or recipe");
        }

        // Remove the collection-recipe pair from the database
        String sql = "DELETE FROM CollectionRecipes WHERE collection = ? AND recipe = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collectionID);
            stmt.setString(2, recipeID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while removing a recipe from a collection");
        }
    }

    /**
     * Finds the all recipes in the given collection
     *
     * @param id The ID of the collection to find recipes for
     * @return A list of all recipes in the given collection, or null if no recipes were found
     * @throws DataAccessException If an error occurs while finding recipes in the collection
     */
    public List<String> findCollectionRecipes(String id) throws DataAccessException {
        ArrayList<String> recipes = new ArrayList<>();
        ResultSet rs;
        String sql = "SELECT recipe FROM CollectionRecipes WHERE collection = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            // Add any found recipes to the list
            while (rs.next()) {
                recipes.add(rs.getString("recipe"));
            }

            // Return null of no recipes were found and the list otherwise
            if (recipes.size() == 0) {
                return null;
            } else {
                return recipes;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding recipes in the database");
        }
    }

    /*-----------------------------------------------------
                User Specific Database Methods
    -----------------------------------------------------*/

    /**
     * Finds the IDs of all Items that the given user has access to
     *
     * @param username The username of the user to find items for
     * @return A list of the IDs of all Items that the user has access to
     * @throws DataAccessException If an error occurs while finding items the user has access to
     */
    public List<String> findUserCollections(String username) throws DataAccessException {
        ArrayList<String> collections = new ArrayList<>();
        ResultSet rs;
        String sql = "SELECT id FROM Collection WHERE owner = ?;";

        // Execute the SQL statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            // Add any found collections to the list
            while (rs.next()) {
                collections.add(rs.getString("id"));
            }

            // Return null of no collections were found and the list otherwise
            if (collections.size() == 0) {
                return null;
            } else {
                return collections;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding Collections for the user");
        }
    }
}
