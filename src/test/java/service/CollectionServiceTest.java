package service;

import dao.CollectionDAO;
import dao.DataAccessException;
import dao.Database;
import model.Collection;
import model.Permissions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CollectionRequest;
import result.CollectionResult;
import result.LoadResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionServiceTest {

    /*
     * CollectionService object used for testing
     */
    private CollectionService collectionService;

    /*
     * Example data used for testing
     */
    private List<Collection> exData;

    /*
     * Example permissions used for testing
     */
    private List<Permissions> exAssociations;

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
        collectionService = new CollectionService();

        // Load the example data into the database
        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.loadFromFile("data/basicTestData.json");
        assertTrue(loadResult.isSuccess());

        // Get the example data and associations to use in testing
        try {
            CollectionDAO cDao = new CollectionDAO(db.getConnection());

            exData = new ArrayList<>();
            List<String> userCollections = cDao.findUserCollections(USERNAME);
            if (userCollections != null) {
                for (String collectionID : userCollections) {
                    exData.add(cDao.find(collectionID));
                }
            }

            exAssociations = new ArrayList<>();
            for (Collection collection : exData) {
                List<String> collectionRecipes = cDao.findCollectionRecipes(collection.getId());
                if (collectionRecipes != null) {
                    for (String recipeID : collectionRecipes) {
                        exAssociations.add(new Permissions(recipeID, collection.getId()));
                    }
                }
            }

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            fail();
        }

        // Remove collection2 from the example data
        exData.remove(1);

        // Add a new collection to the example data
        exData.add(new Collection("collectionID4", "italian", USERNAME));

        // Remove the association between recipe 1 and collection 2 from the example associations
        exAssociations.remove(1);

    }

    @Test
    public void syncPass() {
        // Create a request with the example data
        CollectionRequest request = new CollectionRequest(AUTHTOKEN, exData, exAssociations, new ArrayList<>(), new ArrayList<>());

        // Run the request through the service
        CollectionResult result = collectionService.sync(request);

        // Verify that the result returned is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSyncData());
        assertNotNull(result.getSyncAssociations());

        System.out.println("SyncData (" + result.getSyncData().size() + "): " + result.getSyncData());
        System.out.println("SyncAssociations (" + result.getSyncAssociations().size() + "): " + result.getSyncAssociations());

        assertEquals(3, result.getSyncData().size());
        assertEquals(2, result.getSyncAssociations().size());
    }

    @Test
    public void syncWithRemovals() {
        // Create a request with the example data that includes removals
        List<String> removals = new ArrayList<>();
        removals.add("collectionID2");
        CollectionRequest request = new CollectionRequest(AUTHTOKEN, exData, exAssociations, removals, new ArrayList<>());

        // Run the request through the service
        CollectionResult result = collectionService.sync(request);

        // Verify that the result returned is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSyncData());
        assertNotNull(result.getSyncAssociations());

        System.out.println("SyncData (" + result.getSyncData().size() + "): " + result.getSyncData());
        System.out.println("SyncAssociations (" + result.getSyncAssociations().size() + "): " + result.getSyncAssociations());

        assertEquals(2, result.getSyncData().size());
        assertEquals(0, result.getSyncAssociations().size());
    }

    @Test
    public void syncWithRevocations() {
        // Create a request with the example data that includes revocations
        List<Permissions> revocations = new ArrayList<>();
        revocations.add(new Permissions("recipeID3", "collectionID2"));
        CollectionRequest request = new CollectionRequest(AUTHTOKEN, exData, exAssociations, new ArrayList<>(), revocations);

        // Run the request through the service
        CollectionResult result = collectionService.sync(request);

        // Verify that the result returned is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSyncData());
        assertNotNull(result.getSyncAssociations());

        System.out.println("SyncData (" + result.getSyncData().size() + "): " + result.getSyncData());
        System.out.println("SyncAssociations (" + result.getSyncAssociations().size() + "): " + result.getSyncAssociations());

        assertEquals(3, result.getSyncData().size());
        assertEquals(1, result.getSyncAssociations().size());
    }

    @Test
    public void syncInvalidRequest() {
        // Create a request that would be invalid
        CollectionRequest request = new CollectionRequest(null, null, null, null, null);

        // Run the invalid request through the service
        CollectionResult result = collectionService.sync(request);

        // Verify that the result returned indicates an invalid request
        assertNotNull(result);
        assertTrue(result.getMessage().contains("Invalid request"));
        assertFalse(result.isSuccess());
    }
}
