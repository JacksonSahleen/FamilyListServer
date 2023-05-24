package service;

import model.Model;
import model.Permissions;

import java.util.*;

/**
 * Class used to synchronize data between the database and the application.
 */
public class DataSynchronizer {

    /**
     * Synchronizes the data between the database and the application client.
     *
     * @param clientData The data from the application.
     * @param dbData The data from the database.
     * @param removals The IDs of the data to be removed.
     * @return The synchronized list of data.
     */
    public List<Model> syncData(List<Model> clientData, List<Model> dbData, List<String> removals) {
        List<Model> syncedData = new ArrayList<>();
        HashSet<String> dataIDs = new HashSet<>();

        // Create a HashMap of the client data and update the set of data IDs
        Map<String, Model> clientMap = new HashMap<>();
        for (Model model : clientData) {
            // Skip if the data is to be removed
            if (removals.contains(model.getId())) {
                continue;
            }

            // Add the data to the HashMap and update the set of data IDs
            clientMap.put(model.getId(), model);
            dataIDs.add(model.getId());
        }

        // Create a HashMap of the database data and update the set of data IDs
        Map<String, Model> dbMap = new HashMap<>();
        for (Model model : dbData) {
            // Skip if the data is to be removed
            if (removals.contains(model.getId())) {
                continue;
            }

            // Add the data to the HashMap and update the set of data IDs
            dbMap.put(model.getId(), model);
            dataIDs.add(model.getId());
        }

        // Add the latest version of each data element to the synced data
        for (String id : dataIDs) {
            // Try to get the model from both sets of data
            Model clientDatum = clientMap.get(id);
            Model dbDatum = dbMap.get(id);

            // Update the synced data as needed based on the data from each set
            if (clientDatum != null && dbDatum != null) {
                // Add the latest version to the synced data and use the client version if the timestamps are equal
                if (dbDatum.getLastUpdated().isAfter(clientDatum.getLastUpdated())) {
                    syncedData.add(dbDatum);
                } else {
                    syncedData.add(clientDatum);
                }
            } else if (clientDatum != null) {
                syncedData.add(clientDatum);
            } else if (dbDatum != null) {
                syncedData.add(dbDatum);
            }
        }

        return syncedData;
    }

    /**
     * Synchronizes the permissions between the database and the application client.
     *
     * @param clientPermissions The permissions from the application.
     * @param dbPermissions The permissions from the database.
     * @param removals The IDs of the permissions to be removed.
     * @param revocations The permissions to be revoked.
     * @return The synchronized list of permissions.
     */
    public List<Permissions> syncPermissions(List<Permissions> clientPermissions, List<Permissions> dbPermissions,
                                             List<String> removals, List<Permissions> revocations) {
        Set<Permissions> syncedPermissions = new HashSet<>();

        // Add the database permissions to the synced permissions if they are not to be revoked
        for (Permissions dbPermission : dbPermissions) {
            if (!revocations.contains(dbPermission) && !removals.contains(dbPermission.object()) && !removals.contains(dbPermission.holder())) {
                syncedPermissions.add(dbPermission);
            }
        }

        // Add the client permissions to the synced permissions if they are not to be revoked
        for (Permissions clientPermission : clientPermissions) {
            if (!revocations.contains(clientPermission) && !removals.contains(clientPermission.object()) && !removals.contains(clientPermission.holder())) {
                syncedPermissions.add(clientPermission);
            }
        }

        return syncedPermissions.stream().toList();
    }
}
