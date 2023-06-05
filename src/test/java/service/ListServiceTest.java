package service;

import dao.DataAccessException;
import dao.Database;
import dao.ItemListDAO;
import model.ItemList;
import model.Permissions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ListRequest;
import result.ListResult;
import result.LoadResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListServiceTest {

    /*
     * ListService object used for testing
     */
    private ListService listService;

    /*
     * Example data used for testing
     */
    private List<ItemList> exData;

    /*
     * Example permissions used for testing
     */
    private List<Permissions> exPermissions;

    /*
     * Username of the user being used for testing
     */
    private static final String USERNAME = "user1";

    /*
     * Authtoken of the user being used for testing
     */
    private static final String AUTHTOKEN = "token1";

    @BeforeEach
    public void setUp() {
        Database db = new Database();
        listService = new ListService();

        // Load the example data into the database
        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.loadFromFile("data/basicTestData.json");
        assertTrue(loadResult.isSuccess());

        // Get the example data and associations to use in testing
        try {
            ItemListDAO lDao = new ItemListDAO(db.getConnection());

            exData = new ArrayList<>();
            List<String> userLists = lDao.findUserLists(USERNAME);
            if (userLists != null) {
                for (String listID : userLists) {
                    exData.add(lDao.find(listID));
                }
            }

            exPermissions = new ArrayList<>();
            for (ItemList list : exData) {
                List<String> listUsers = lDao.findUsersWithAccess(list.getId());
                if (listUsers != null) {
                    for (String username : listUsers) {
                        exPermissions.add(new Permissions(list.getId(), username));
                    }
                }
            }

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            fail();
        }

        // Remove list3 from the example data and permissions
        exData.remove(1);
        exPermissions.remove(1);

        // Add a new list to the example data
        exData.add(new ItemList("listID9", "new test list", USERNAME));
        exPermissions.add(new Permissions("listID9", USERNAME));

        // Remove user1 form list4's permissions
        exPermissions.remove(2);
    }

    @Test
    public void syncPass() {
        // Create a request with the example data
        ListRequest request = new ListRequest(AUTHTOKEN, exData, exPermissions, new ArrayList<>(), new ArrayList<>());

        // Run the request through the service
        ListResult result = listService.sync(request);

        // Verify that the result returned is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSyncData());
        assertNotNull(result.getSyncPermissions());

        System.out.println("SyncPass Data (" + result.getSyncData().size() + "): " + result.getSyncData());
        System.out.println("SyncPass Permissions (" + result.getSyncPermissions().size() + "): " + result.getSyncPermissions());

        assertEquals(7, result.getSyncData().size());
        assertEquals(8, result.getSyncPermissions().size());
    }

    @Test
    public void syncWithRemovals() {
        // Create a request with the example data that includes a removal
        List<String> removals = new ArrayList<>();
        removals.add("listID3");
        ListRequest request = new ListRequest(AUTHTOKEN, exData, exPermissions, removals, new ArrayList<>());

        // Run the request through the service
        ListResult result = listService.sync(request);

        // Verify that the result returned is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSyncData());
        assertNotNull(result.getSyncPermissions());

        System.out.println("SyncWithRemovals Data (" + result.getSyncData().size() + "): " + result.getSyncData());
        System.out.println("SyncWithRemovals Permissions (" + result.getSyncPermissions().size() + "): " + result.getSyncPermissions());

        assertEquals(6, result.getSyncData().size());
        assertEquals(7, result.getSyncPermissions().size());
    }

    @Test
    public void syncWithRevocations() {
        // Create a request with the example data that includes revocations
        List<Permissions> revocations = new ArrayList<>();
        revocations.add(new Permissions("listID4", "user1"));
        ListRequest request = new ListRequest(AUTHTOKEN, exData, exPermissions, new ArrayList<>(), revocations);

        // Run the request through the service
        ListResult result = listService.sync(request);

        // Verify that the result returned is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSyncData());
        assertNotNull(result.getSyncPermissions());

        System.out.println("SyncWithRevocations Data (" + result.getSyncData().size() + "): " + result.getSyncData());
        System.out.println("SyncWithRevocations Permissions (" + result.getSyncPermissions().size() + "): " + result.getSyncPermissions());

        assertEquals(7, result.getSyncData().size());
        assertEquals(7, result.getSyncPermissions().size());
    }

    @Test
    public void syncInvalidRequest() {
        // Create a request that would be invalid
        ListRequest request = new ListRequest(null, null, null, null, null);

        // Run the invalid request through the service
        ListResult result = listService.sync(request);

        // Verify that the result returned indicates an invalid request
        assertNotNull(result);
        assertTrue(result.getMessage().contains("Invalid request"));
        assertFalse(result.isSuccess());
    }
}
