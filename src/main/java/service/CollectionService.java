package service;

import dao.*;
import model.*;
import request.CollectionRequest;
import result.CollectionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * The service class responsible for handling Collection requests.
 */
public class CollectionService {

    /**
     * The Database object used to access the database.
     */
    private final Database db;

    /**
     * The DataSynchronizer object used to synchronize data between the database and the application.
     */
    private final DataSynchronizer synchronizer;

    /**
     * Constructor for CollectionService
     */
    public CollectionService() {
        db = new Database();
        synchronizer = new DataSynchronizer();
    }

    /**
     * Syncs the local Collection data with the server and returns the updated data.
     *
     * @param request The service request with the local data to be synced with the server.
     * @param authtoken The authtoken of the user making the request.
     * @return The updated Collection data from the server after being synced.
     */
    public CollectionResult sync(CollectionRequest request, String authtoken) {
        // Check that the request is valid
        if (checkInvalidRequest(request) || authtoken == null) {
            return new CollectionResult("ERROR: Invalid request.", false);
        }

        try {
            // Identify the user making the request
            AuthtokenDAO aDao = new AuthtokenDAO(db.getConnection());
            String username = aDao.find(authtoken).getUserID();

            // Get the data and permissions from the request
            List<Model> clientData = List.copyOf(request.getData());
            List<Permissions> clientAssociations = List.copyOf(request.getAssociations());
            List<String> removals = List.copyOf(request.getRemovals());
            List<Permissions> revocations = List.copyOf(request.getRevocations());

            // Get the data from the database
            List<Model> dbData = new ArrayList<>();
            CollectionDAO cDao = new CollectionDAO(db.getConnection());
            List<String> userCollections = cDao.findUserCollections(username);
            if (userCollections != null) {
                for (String collection : userCollections) {
                    dbData.add(cDao.find(collection));
                }
            }

            // Get the associations from the database
            List<Permissions> dbAssociations = new ArrayList<>();
            for (Model dbCollection : dbData) {
                List<String> collectionRecipes = cDao.findCollectionRecipes(dbCollection.getId());
                if (collectionRecipes != null) {
                    for (String recipe : collectionRecipes) {
                        dbAssociations.add(new Permissions(recipe, dbCollection.getId()));
                    }
                }
            }

            // Synchronize the sets of data and associations
            List<Model> syncData = synchronizer.syncData(clientData, dbData, removals);
            List<Permissions> syncAssociations = synchronizer.syncPermissions(clientAssociations, dbAssociations,
                                                                              removals, revocations);
            List<Collection> collections = new ArrayList<>();

            // Update the database with the synced data
            for (Model syncModel : syncData) {
                Collection sc = (Collection) syncModel;
                collections.add(sc);

                if (!dbData.contains(syncModel) && !removals.contains(sc.getId())) {
                    cDao.insert(sc);
                } else {
                    cDao.update(sc);
                }
            }
            for (String removal : removals) {
                cDao.remove(cDao.find(removal));
            }

            // Update the database with the synced associations
            for (Permissions syncPermissions : syncAssociations) {
                if (!dbAssociations.contains(syncPermissions)) {
                    cDao.addCollectionRecipe(syncPermissions.getHolder(), syncPermissions.getObject());
                }
            }
            for (Permissions revocation : revocations) {
                cDao.removeCollectionRecipe(revocation.getHolder(), revocation.getObject());
            }

            // Close the database connection and commit changes
            db.closeConnection(true);

            // Return the final collections data in the result for the client
            return new CollectionResult(collections, syncAssociations);

        } catch (Exception e) {
            // Return the error message in the result for the client if a server error occurs
            e.printStackTrace();
            db.closeConnection(false);
            return new CollectionResult("ERROR: Internal Server Error (" + e.getMessage() + ").", false);
        }
    }

    /**
     * Checks that the given request is valid.
     *
     * @param request The request object to be checked.
     * @return True if the request is valid, false otherwise.
     */
    private boolean checkInvalidRequest(CollectionRequest request) {
        return request == null || request.getData() == null || request.getAssociations() == null ||
                request.getRemovals() == null || request.getRevocations() == null;
    }
}
