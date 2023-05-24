package service;

import dao.DataAccessException;
import dao.Database;
import dao.RecipeDAO;
import model.Permissions;
import model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RecipeRequest;
import result.LoadResult;
import result.RecipeResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeServiceTest {

    /*
     * RecipeService object used for testing
     */
    private RecipeService recipeService;

    /*
     * Example data used for testing
     */
    private List<Recipe> exData;

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
        recipeService = new RecipeService();

        // Load the example data into the database
        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.loadFromFile("data/basicTestData.json");
        assertTrue(loadResult.isSuccess());

        // Get the example data and associations to use in testing
        try {
            RecipeDAO rDao = new RecipeDAO(db.getConnection());

            exData = new ArrayList<>();
            List<String> userRecipes = rDao.findUserRecipes(USERNAME);
            if (userRecipes != null) {
                for (String recipeID : userRecipes) {
                    exData.add(rDao.find(recipeID));
                }
            }

            exPermissions = new ArrayList<>();
            for (Recipe recipe : exData) {
                List<String> listUsers = rDao.findUsersWithAccess(recipe.getId());
                if (listUsers != null) {
                    for (String username : listUsers) {
                        exPermissions.add(new Permissions(recipe.getId(), username));
                    }
                }
            }

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            fail();
        }

        System.out.println("dbData (" + exData.size() + "): " + exData);    // DEBUG
        System.out.println("dbPermissions (" + exPermissions.size() + "): " + exPermissions);    // DEBUG

        // Remove recipe1 from the example data and permissions
        exData.remove(0);
        exPermissions.remove(0);

        // Add a new list to the example data
        exData.add(new Recipe("recipeID4", "newRecipe", USERNAME, "new test recipe"));
        exPermissions.add(new Permissions("listID4", USERNAME));

        // Remove user1 form recipe3's permissions
        exPermissions.remove(1);

        System.out.println("clientData (" + exData.size() + "): " + exData);    // DEBUG
        System.out.println("clientPermissions (" + exPermissions.size() + "): " + exPermissions);    // DEBUG
    }

    @Test
    public void syncPass() {
        // Create a request with the example data
        RecipeRequest request = new RecipeRequest(exData, exPermissions, new ArrayList<>(), new ArrayList<>());

        // Run the request through the service
        RecipeResult result = recipeService.sync(request, AUTHTOKEN);

        // Verify that the result returned is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSyncData());
        assertNotNull(result.getSyncPermissions());

        System.out.println("SyncPass Data (" + result.getSyncData().size() + "): " + result.getSyncData());
        System.out.println("SyncPass Permissions (" + result.getSyncPermissions().size() + "): " + result.getSyncPermissions());

        assertEquals(3, result.getSyncData().size());
        assertEquals(4, result.getSyncPermissions().size());
    }

    @Test
    public void syncWithRemovals() {
        // Create a request with the example data that includes a removal
        List<String> removals = new ArrayList<>();
        removals.add("recipeID1");
        RecipeRequest request = new RecipeRequest(exData, exPermissions, removals, new ArrayList<>());

        // Run the request through the service
        RecipeResult result = recipeService.sync(request, AUTHTOKEN);

        // Verify that the result returned is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSyncData());
        assertNotNull(result.getSyncPermissions());

        System.out.println("SyncWithRemovals Data (" + result.getSyncData().size() + "): " + result.getSyncData());
        System.out.println("SyncWithRemovals Permissions (" + result.getSyncPermissions().size() + "): " + result.getSyncPermissions());

        assertEquals(2, result.getSyncData().size());
        assertEquals(2, result.getSyncPermissions().size());
    }

    @Test
    public void syncWithRevocations() {
        // Create a request with the example data that includes revocations
        List<Permissions> revocations = new ArrayList<>();
        revocations.add(new Permissions("recipeID3", USERNAME));
        RecipeRequest request = new RecipeRequest(exData, exPermissions, new ArrayList<>(), revocations);

        // Run the request through the service
        RecipeResult result = recipeService.sync(request, AUTHTOKEN);

        // Verify that the result returned is correct
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSyncData());
        assertNotNull(result.getSyncPermissions());

        System.out.println("SyncWithRevocations Data (" + result.getSyncData().size() + "): " + result.getSyncData());
        System.out.println("SyncWithRevocations Permissions (" + result.getSyncPermissions().size() + "): " + result.getSyncPermissions());

        assertEquals(3, result.getSyncData().size());
        assertEquals(3, result.getSyncPermissions().size());
    }

    @Test
    public void syncInvalidRequest() {
        // Create a request that would be invalid
        RecipeRequest request = new RecipeRequest(null, null, null, null);

        // Run the invalid request through the service
        RecipeResult result = recipeService.sync(request, null);

        // Verify that the result returned indicates an invalid request
        assertNotNull(result);
        assertTrue(result.getMessage().contains("Invalid request"));
        assertFalse(result.isSuccess());
    }
}
