package service;

import model.Item;
import model.Model;
import model.Permissions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataSynchronizerTest {

    /*
     * DataSynchronizer object to use in the tests.
     */
    private static DataSynchronizer synchronizer;

    /*
     * List of data before changes.
     */
    private static List<Model> beforeData;

    /*
     * List of data after changes.
     */
    private static List<Model> afterData;

    /*
     * List of permissions before changes.
     */
    private static List<Permissions> beforePermissions;

    /*
     * List of permissions after changes.
     */
    private static List<Permissions> afterPermissions;

    @BeforeEach
    public void setUp() {
        // Initialize the Synchronizer and the lists of data
        synchronizer = new DataSynchronizer();
        beforeData = new ArrayList<>();
        afterData = new ArrayList<>();
        beforePermissions = new ArrayList<>();
        afterPermissions = new ArrayList<>();

        // Create items for the lists of data before and after the changes
        Item item1 = new Item("1", "Item 1", "user1", "list1");
        Item item2 = new Item("2", "Item 2", "user1", "list1");
        Item item3 = new Item("3", "Item 3", "user1", "list1");
        Item item1_revised = new Item("1", "Item 1", "user1", "list1");
        item1_revised.setCompleted(true);

        // Build the list of data before the changes
        beforeData.add(item1);
        beforeData.add(item2);

        // Build the list of data after the changes
        afterData.add(item1_revised);
        afterData.add(item2);
        afterData.add(item3);

        // Create permissions for the lists of permissions before and after the changes
        Permissions permissions1 = new Permissions("1", "user1");
        Permissions permissions2 = new Permissions("2", "user1");
        Permissions permissions3 = new Permissions("3", "user1");
        Permissions extraPermissions = new Permissions("1", "user2");

        // Build the list of permissions before the changes
        beforePermissions.add(permissions1);
        beforePermissions.add(permissions2);
        beforePermissions.add(permissions3);

        // Build the list of permissions after the changes
        afterPermissions.add(permissions1);
        afterPermissions.add(permissions2);
        afterPermissions.add(permissions3);
        afterPermissions.add(extraPermissions);
    }

    @Test
    public void syncDataPass() {
        // Run the two lists of data through the synchronizer
        List<Model> syncedData = synchronizer.syncData(afterData, beforeData, new ArrayList<>());

        // Verify that the synced data is correct
        assertNotNull(syncedData);
        assertEquals(afterData, syncedData);
        assertTrue(syncedData.contains(afterData.get(0)));
        assertFalse(syncedData.contains(beforeData.get(0)));
    }

    @Test
    public void syncDataWithRemovals() {
        // Create a list of removals that includes item 2
        List<String> removals = new ArrayList<>();
        removals.add("2");

        // Run the two lists of data through the synchronizer with the removals
        List<Model> syncedData = synchronizer.syncData(afterData, beforeData, removals);

        // Verify that the synced data is correct
        assertNotNull(syncedData);
        assertTrue(syncedData.contains(afterData.get(0)));
        assertFalse(syncedData.contains(afterData.get(1)));
        assertTrue(syncedData.contains(afterData.get(2)));

    }

    @Test
    public void emptyClientData() {
        // Run the synchronizer with the client data empty
        List<Model> syncedData = synchronizer.syncData(new ArrayList<>(), beforeData, new ArrayList<>());

        // Verify that the synced data is correct
        assertNotNull(syncedData);
        assertEquals(beforeData, syncedData);
    }

    @Test
    public void emptyDBData() {
        // Run the synchronizer with the database data empty
        List<Model> syncedData = synchronizer.syncData(afterData, new ArrayList<>(), new ArrayList<>());

        // Verify that the synced data is correct
        assertNotNull(syncedData);
        assertEquals(afterData, syncedData);
    }

    @Test
    public void emptyData() {
        // Run the synchronizer with both sets of data empty
        List<Model> syncedData = synchronizer.syncData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // Verify that the synced data is correct
        assertNotNull(syncedData);
        assertTrue(syncedData.isEmpty());
    }

    @Test
    public void identicalData() {
        // Run the synchronizer with both sets of data identical
        List<Model> syncedData = synchronizer.syncData(beforeData, beforeData, new ArrayList<>());

        // Verify that the synced data is correct
        assertNotNull(syncedData);
        assertEquals(beforeData, syncedData);
    }

    @Test
    public void syncPermissionsPass() {
        // Run the two lists of permissions through the synchronizer
        List<Permissions> syncedPermissions = synchronizer.syncPermissions(afterPermissions,
                beforePermissions, new ArrayList<>(), new ArrayList<>());

        // Verify that the synced permissions are correct
        assertNotNull(syncedPermissions);
        assertTrue(syncedPermissions.contains(afterPermissions.get(0)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(1)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(2)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(3)));
    }

    @Test
    public void syncPermissionsWithRevocations() {
        // Create a list of revocations that includes user1's permissions for item 2
        List<Permissions> revocations = new ArrayList<>();
        revocations.add(new Permissions("2", "user1"));

        // Run the two lists of permissions through the synchronizer with the revocations
        List<Permissions> syncedPermissions = synchronizer.syncPermissions(afterPermissions,
                beforePermissions, new ArrayList<>(), revocations);

        // Verify that the synced permissions are correct
        assertNotNull(syncedPermissions);
        assertTrue(syncedPermissions.contains(afterPermissions.get(0)));
        assertFalse(syncedPermissions.contains(afterPermissions.get(1)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(2)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(3)));
    }

    @Test
    public void syncPermissionsWithRemovals() {
        // Create a list of removals that includes user1's permissions for item 2
        List<String> removals = new ArrayList<>();
        removals.add("2");

        // Run the two lists of permissions through the synchronizer with the revocations
        List<Permissions> syncedPermissions = synchronizer.syncPermissions(afterPermissions,
                beforePermissions, removals, new ArrayList<>());

        // Verify that the synced permissions are correct
        assertNotNull(syncedPermissions);
        assertTrue(syncedPermissions.contains(afterPermissions.get(0)));
        assertFalse(syncedPermissions.contains(afterPermissions.get(1)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(2)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(3)));
    }

    @Test
    public void syncPermissionsWithRemovalsAndRevocations() {
        // Create a lists of removals and revocations that includes user1's permissions for item 2
        List<String> removals = new ArrayList<>();
        removals.add("2");
        List<Permissions> revocations = new ArrayList<>();
        revocations.add(new Permissions("3", "user1"));

        // Run the two lists of permissions through the synchronizer with the revocations
        List<Permissions> syncedPermissions = synchronizer.syncPermissions(afterPermissions,
                beforePermissions, removals, revocations);

        // Verify that the synced permissions are correct
        assertNotNull(syncedPermissions);
        assertTrue(syncedPermissions.contains(afterPermissions.get(0)));
        assertFalse(syncedPermissions.contains(afterPermissions.get(1)));
        assertFalse(syncedPermissions.contains(afterPermissions.get(2)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(3)));
    }

    @Test
    public void emptyClientPermissions() {
        // Run the synchronizer with the client permissions empty
        List<Permissions> syncedPermissions = synchronizer.syncPermissions(new ArrayList<>(),
                beforePermissions, new ArrayList<>(), new ArrayList<>());

        // Verify that the synced permissions are correct
        assertNotNull(syncedPermissions);
        assertTrue(syncedPermissions.contains(beforePermissions.get(0)));
        assertTrue(syncedPermissions.contains(beforePermissions.get(1)));
        assertTrue(syncedPermissions.contains(beforePermissions.get(2)));
    }

    @Test
    public void emptyDBPermissions() {
        // Run the synchronizer with the database permissions empty
        List<Permissions> syncedPermissions = synchronizer.syncPermissions(afterPermissions,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // Verify that the synced permissions are correct
        assertNotNull(syncedPermissions);
        assertTrue(syncedPermissions.contains(afterPermissions.get(0)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(1)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(2)));
        assertTrue(syncedPermissions.contains(afterPermissions.get(3)));
    }

    @Test
    public void emptyPermissions() {
        // Run the synchronizer with both sets of permissions empty
        List<Permissions> syncedPermissions = synchronizer.syncPermissions(new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // Verify that the synced permissions are correct
        assertNotNull(syncedPermissions);
        assertTrue(syncedPermissions.isEmpty());
    }

    @Test
    public void identicalPermissions() {
        // Run the synchronizer with both sets of permissions identical
        List<Permissions> syncedPermissions = synchronizer.syncPermissions(beforePermissions,
                beforePermissions, new ArrayList<>(), new ArrayList<>());

        // Verify that the synced permissions are correct
        assertNotNull(syncedPermissions);
        assertTrue(syncedPermissions.contains(beforePermissions.get(0)));
        assertTrue(syncedPermissions.contains(beforePermissions.get(1)));
        assertTrue(syncedPermissions.contains(beforePermissions.get(2)));
    }
}
