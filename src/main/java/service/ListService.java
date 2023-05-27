package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.ItemListDAO;
import model.ItemList;
import model.Model;
import model.Permissions;
import request.ListRequest;
import result.ListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * The service class responsible for handling List requests.
 */
public class ListService {

    /**
     * The Database object used to access the database.
     */
    private final Database db;

    /**
     * The DataSynchronizer object used to synchronize data between the database and the application.
     */
    private final DataSynchronizer synchronizer;

    /**
     * Constructor for ListService
     */
    public ListService() {
        db = new Database();
        synchronizer = new DataSynchronizer();
    }

    /**
     * Syncs the local ItemList data with the server and returns the updated data.
     *
     * @param request The service request with the local data to be synced with the server.
     * @param authtoken The authtoken of the user making the request.
     * @return The updated ItemList data from the server after being synced.
     */
    public ListResult sync(ListRequest request, String authtoken) {
        // Check that the request is valid
        if (checkInvalidRequest(request) || authtoken == null) {
            return new ListResult("ERROR: Invalid request.", false);
        }

        try {
            // Identify the user making the request
            AuthtokenDAO aDao = new AuthtokenDAO(db.getConnection());
            String username = aDao.find(authtoken).getUserID();

            // Get the data and permissions from the request
            List<Model> clientData = List.copyOf(request.getData());
            List<Permissions> clientPermissions = List.copyOf(request.getPermissions());
            List<String> removals = List.copyOf(request.getRemovals());
            List<Permissions> revocations = List.copyOf(request.getRevocations());

            // Get the data from the database
            List<Model> dbData = new ArrayList<>();
            ItemListDAO lDao = new ItemListDAO(db.getConnection());
            List<String> userLists = lDao.findUserLists(username);
            if (userLists != null) {
                for (String listID : userLists) {
                    dbData.add(lDao.find(listID));
                }
            }

            // Get the permissions from the database
            List<Permissions> dbPermissions = new ArrayList<>();
            for (Model dbList : dbData) {
                List<String> listUsers = lDao.findUsersWithAccess(dbList.getId());
                if (listUsers != null) {
                    for (String listUser : listUsers) {
                        dbPermissions.add(new Permissions(dbList.getId(), listUser));
                    }
                }
            }

            // Synchronize the sets of data and permissions
            List<Model> syncData = synchronizer.syncData(clientData, dbData, removals);
            List<Permissions> syncPermissions = synchronizer.syncPermissions(clientPermissions, dbPermissions,
                                                                             removals, revocations);
            List<ItemList> lists = new ArrayList<>();

            // Update the database with the synced data
            for (Model syncModel : syncData) {
                ItemList sl = (ItemList) syncModel;
                lists.add(sl);

                if (!dbData.contains(syncModel) && !removals.contains(sl.getId())) {
                    lDao.insert(sl);
                } else {
                    lDao.update(sl);
                }
            }
            for (String removal : removals) {
                lDao.remove(lDao.find(removal), username);
            }

            // Get a synchronized list of all the IDs of the ItemLists the user owns
            List<String> userListIDs = new ArrayList<>();
            for (ItemList l : lists) {
                if (l.getOwner().equals(username)) {
                    userListIDs.add(l.getId());
                }
            }

            // Update the database with the synced permissions if the user owns the list
            for (Permissions syncPermission : syncPermissions) {
                if (!dbPermissions.contains(syncPermission) && userListIDs.contains(syncPermission.getObject())) {
                    lDao.share(syncPermission.getObject(), syncPermission.getHolder());
                }
            }
            for (Permissions revocation : revocations) {
                if (userListIDs.contains(revocation.getObject())) {
                    lDao.unshare(revocation.getObject(), revocation.getHolder());
                }
            }

            // Close the database connection and commit changes
            db.closeConnection(true);

            // Return the final lists data in the result for the client
            return new ListResult(lists, syncPermissions);

        } catch (Exception e) {
            // Return the error message in the result for the client if a server error occurs
            e.printStackTrace();
            db.closeConnection(false);
            return new ListResult("ERROR: Internal Server Error (" + e.getMessage() + ").", false);
        }
    }

    /**
     * Checks that the given request is valid.
     *
     * @param request The request object to be checked.
     * @return True if the request is valid, false otherwise.
     */
    private boolean checkInvalidRequest(ListRequest request) {
        return request == null || request.getData() == null || request.getPermissions() == null ||
                request.getRemovals() == null || request.getRevocations() == null;
    }
}
