package service;

import dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import result.LoadResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {

    /*
     * LoadService object to run the tests on
     */
    private static LoadService loadService;

    /*
     * The Database object used to access the database.
     */
    private static final Database db = new Database();

    @BeforeEach
    public void setUp() {
        loadService = new LoadService();

        try {
            Connection conn = db.getConnection();
            UserDAO uDao = new UserDAO(conn);
            ItemListDAO lDao = new ItemListDAO(conn);
            CollectionDAO cDao = new CollectionDAO(conn);
            RecipeDAO rDao = new RecipeDAO(conn);

            uDao.clear();
            lDao.clear();
            cDao.clear();
            rDao.clear();

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
        }
    }

    @Test
    public void loadDataPass() {
        // Load in data from json
        try {
            // Build the request from the data in the test JSON file
            LoadRequest request = loadService.getRequestFromFile("data/basicTestData.json");

            // Run the load service
            LoadResult result = loadService.load(request);

            // Check that the result is correct
            assertNotNull(result);
            assertTrue(result.isSuccess());
            System.out.println(result.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void invalidRequest() {
        // Create a request
        LoadRequest request = new LoadRequest(null,null, null, null, null,
                                              null, null, null,
                                              false);

        // Run the load service
        LoadResult result = loadService.load(request);

        // Check that the result is correct
        assertNotNull(result);
        assertEquals("ERROR: Invalid request.", result.getMessage());
        assertFalse(result.isSuccess());
    }

}
