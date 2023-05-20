package service;

import dao.*;
import model.*;
import request.LoadRequest;
import result.LoadResult;

import java.sql.Connection;
import java.util.List;

/**
 * The service class responsible for handling Load requests.
 */
public class LoadService {

    /**
     * The Database object used to access the database.
     */
    private static Database db;

    /**
     * Constructor for LoadService
     */
    public LoadService() {
        db = new Database();
    }

    /**
     * Loads data into the database.
     *
     * @param request The request object containing the data to be loaded.
     * @return The result object containing the success or failure of the operation.
     */
    public LoadResult load(LoadRequest request) {
        // Check that the request is valid
        if (!checkRequest(request)) {
            return new LoadResult("ERROR: Invalid request.", false);
        }

        // Load the data into the database
        try {
            Connection conn = db.getConnection();
            UserDAO uDao = new UserDAO(conn);
            AuthtokenDAO aDao = new AuthtokenDAO(conn);
            CategoryDAO catDao = new CategoryDAO(conn);
            CollectionDAO colDao = new CollectionDAO(conn);
            ItemDAO iDao = new ItemDAO(conn);
            ItemListDAO lDao = new ItemListDAO(conn);
            RecipeDAO rDao = new RecipeDAO(conn);

            // Clear the database if requested
            if (request.clearDatabase()) {
                uDao.clear();
                aDao.clear();
                catDao.clear();
                colDao.clear();
                iDao.clear();
                lDao.clear();
                rDao.clear();
            }

            String message = "Successfully added ";

            // Insert any provided Users
            if (request.users() != null) {
                for (User user : request.users()) {
                    uDao.insert(user);
                }
                message += request.users().size() + " users, ";
            }

            // Insert any provided Lists
            if (request.lists() != null) {
                for (ItemList list : request.lists()) {
                    lDao.insert(list);
                }
                message += request.lists().size() + " lists, ";
            }

            // Insert Any provided Collections
            if (request.collections() != null) {
                for (Collection collection : request.collections()) {
                    colDao.insert(collection);
                }
                message += request.collections().size() + " collections, ";
            }

            // Insert any provided Recipes
            if (request.recipes() != null) {
                for (Recipe recipe : request.recipes()) {
                    rDao.insert(recipe);
                }
                message += request.recipes().size() + " recipes, ";
            }

            // Insert any provided Categories
            if (request.categories() != null) {
                for (Category category : request.categories()) {
                    catDao.insert(category);
                }
                message += request.categories().size() + " categories, ";
            }

            // Insert any provided Items
            if (request.items() != null) {
                for (Item item : request.items()) {
                    iDao.insert(item);
                }
                message += request.items().size() + " items, ";
            }

            // Parse and insert any provided list permissions
            for (List<String> permissionPair : request.listPermissions()) {
                String listID = permissionPair.get(0);
                String username = permissionPair.get(1);

                // Give the user permission to the list if both exist
                ItemList list = lDao.find(listID);
                if (list != null && uDao.find(username) != null) {
                    lDao.share(list, username);
                }
            }

            // Parse and insert any provided recipe permissions
            for (List<String> permissionPair : request.recipePermissions()) {
                String recipeID = permissionPair.get(0);
                String username = permissionPair.get(1);

                // Give the user permission to the recipe if both exist
                Recipe recipe = rDao.find(recipeID);
                if (recipe != null && uDao.find(username) != null) {
                    rDao.share(recipe, username);
                }
            }

            // Parse and insert any provided collection recipes
            for (List<String> collectionPair : request.collectionRecipes()) {
                String collectionID = collectionPair.get(0);
                String recipeID = collectionPair.get(1);

                // Add the recipe to the collection if both exist
                if (colDao.find(collectionID) != null && rDao.find(recipeID) != null) {
                    colDao.addCollectionRecipe(collectionID, recipeID);
                }
            }

            // Commit the changes to the database and close the connection
            db.closeConnection(true);

            return new LoadResult(message, true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new LoadResult("ERROR: Failed to load data into the database.", false);
        }
    }

    /**
     * Checks that the request is valid.
     *
     * @param request The request object to be checked.
     * @return True if the request is valid, false otherwise.
     */
    private boolean checkRequest(LoadRequest request) {
        return request != null && (request.users() != null || request.lists() != null ||
                request.collections() != null || request.recipes() != null ||
                request.categories() != null || request.items() != null ||
                request.listPermissions() != null || request.recipePermissions() != null ||
                request.collectionRecipes() != null);
    }
}
