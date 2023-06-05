package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.RecipeDAO;
import model.Model;
import model.Permissions;
import model.Recipe;
import request.RecipeRequest;
import result.RecipeResult;

import java.util.ArrayList;
import java.util.List;

/**
 * The service class responsible for handling Recipe requests.
 */
public class RecipeService {

    /**
     * The Database object used to access the database.
     */
    private final Database db;

    /**
     * The DataSynchronizer object used to synchronize data between the database and the application.
     */
    private final DataSynchronizer synchronizer;

    /**
     * Constructor for RecipeService
     */
    public RecipeService() {
        db = new Database();
        synchronizer = new DataSynchronizer();
    }

    /**
     * Syncs the local Recipe data with the server and returns the updated data.
     *
     * @param request The service request with the local data to be synced with the server.
     * @return The updated Recipe data from the server after being synced.
     */
    public RecipeResult sync(RecipeRequest request) {
        // Check that the request is valid
        if (checkInvalidRequest(request)) {
            return new RecipeResult("ERROR: Invalid request.", false);
        }

        try {
            // Identify the user making the request
            AuthtokenDAO aDao = new AuthtokenDAO(db.getConnection());
            String username = aDao.find(request.getAuthtoken()).getUserID();

            // Get the data and permissions from the request
            List<Model> clientData = List.copyOf(request.getData());
            List<Permissions> clientPermissions = List.copyOf(request.getPermissions());
            List<String> removals = List.copyOf(request.getRemovals());
            List<Permissions> revocations = List.copyOf(request.getRevocations());

            // Get the data from the database
            List<Model> dbData = new ArrayList<>();
            RecipeDAO rDao = new RecipeDAO(db.getConnection());
            List<String> userRecipes = rDao.findUserRecipes(username);
            if (userRecipes != null) {
                for (String recipeID : userRecipes) {
                    dbData.add(rDao.find(recipeID));
                }
            }

            // Get the permissions from the database
            List<Permissions> dbPermissions = new ArrayList<>();
            for (Model dbRecipe : dbData) {
                List<String> recipeUsers = rDao.findUsersWithAccess(dbRecipe.getId());
                if (recipeUsers != null) {
                    for (String recipeUser : recipeUsers) {
                        dbPermissions.add(new Permissions(dbRecipe.getId(), recipeUser));
                    }
                }
            }

            // Synchronize the sets of data and permissions
            List<Model> syncData = synchronizer.syncData(clientData, dbData, removals);
            List<Permissions> syncPermissions = synchronizer.syncPermissions(clientPermissions, dbPermissions,
                    removals, revocations);
            List<Recipe> recipes = new ArrayList<>();

            // Update the database with the synced data
            for (Model syncModel : syncData) {
                Recipe sr = (Recipe) syncModel;
                recipes.add(sr);

                if (!dbData.contains(syncModel) && !removals.contains(sr.getId())) {
                    rDao.insert(sr);
                } else {
                    rDao.update(sr);
                }
            }
            for (String removal : removals) {
                rDao.remove(rDao.find(removal), username);
            }

            // Get a synchronized list of all the IDs of the Recipes the user owns
            List<String> userRecipeIDs = new ArrayList<>();
            for (Recipe r : recipes) {
                if (r.getOwner().equals(username)) {
                    userRecipeIDs.add(r.getId());
                }
            }

            // Update the database with the synced permissions if the user owns the recipe
            for (Permissions syncPermission : syncPermissions) {
                if (!dbPermissions.contains(syncPermission) && userRecipeIDs.contains(syncPermission.getObject())) {
                    rDao.share(syncPermission.getObject(), syncPermission.getHolder());
                }
            }
            for (Permissions revocation : revocations) {
                if (userRecipeIDs.contains(revocation.getObject())) {
                    rDao.unshare(revocation.getObject(), revocation.getHolder());
                }
            }

            // Close the database connection and commit changes
            db.closeConnection(true);

            // Return the final recipes data in the result for the client
            return new RecipeResult(recipes, syncPermissions);

        } catch (Exception e) {
            // Return the error message in the result for the client if a server error occurs
            e.printStackTrace();
            db.closeConnection(false);
            return new RecipeResult("ERROR: Internal Server Error (" + e.getMessage() + ").", false);
        }
    }

    /**
     * Checks that the given request is valid.
     *
     * @param request The request object to be checked.
     * @return True if the request is valid, false otherwise.
     */
    private boolean checkInvalidRequest(RecipeRequest request) {
        return request == null || request.getAuthtoken() == null || request.getData() == null ||
                request.getPermissions() == null || request.getRemovals() == null || request.getRevocations() == null;
    }
}
