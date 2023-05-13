package dao;

import model.Collection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CollectionDAOTest {

    /*
     * Database object to use for testing
     */
    private static Database db;

    /*
     * Collection object to use for testing
     */
    private static Collection exCollection;

    /*
     * DAO object to use for testing
     */
    private static CollectionDAO cDao;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();

        // Create an example Collection object
        exCollection = new Collection("CollectionID", "name", "owner");

        // Create a new CollectionDAO
        Connection conn = db.getConnection();
        cDao = new CollectionDAO(conn);

        // Wipe the database clean
        cDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Close the database connection and rollback any changes made
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Get the example Collection back from the database
        Collection compareTest = cDao.find(exCollection.getId());

        // Verify that the Collection we inserted and the Collection we got back are the same
        assertNotNull(compareTest);
        assertEquals(exCollection, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Try to insert the same Collection again and verify that it throws an exception
        assertThrows(DataAccessException.class, () -> cDao.insert(exCollection));
    }

    @Test
    public void findPass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Verify that the Collection we inserted and the Collection we got back are the same
        Collection compareTest = cDao.find(exCollection.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        // Try to get a Collection that doesn't exist in the database
        Collection compareTest = cDao.find(exCollection.getId());

        // Verify that the Collection we got back is null
        assertNull(compareTest);
    }

    @Test
    public void updatePass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Verify that the Collection we inserted and the Collection we got back are the same
        Collection compareTest = cDao.find(exCollection.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection, compareTest);

        // Update the example Collection
        String newName = "newName";
        String newOwner = "newOwner";
        exCollection.setName(newName);
        exCollection.setOwner(newOwner);
        cDao.update(exCollection);

        // Verify that the Collection we updated now has the updated values
        compareTest = cDao.find(exCollection.getId());
        assertNotNull(compareTest);
        assertEquals(compareTest.getName(), newName);
        assertEquals(compareTest.getOwner(), newOwner);
    }

    @Test
    public void updateFail() throws DataAccessException {
        // Update a Collection that doesn't exist in the database and verify that nothing happens
        Collection fakeCollection = new Collection("fakeID", "fakeName", "fakeOwner");
        cDao.update(fakeCollection);

        // Verify that the updated Collection still does not exist
        Collection compareTest = cDao.find(fakeCollection.getId());
        assertNull(compareTest);
    }

    @Test
    public void removePass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Verify that the Collection we inserted and the Collection we got back are the same
        Collection compareTest = cDao.find(exCollection.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection, compareTest);

        // Insert a second Collection into the database
        Collection exCollection2 = new Collection("CollectionID2", "name2", "owner2");
        cDao.insert(exCollection2);

        // Verify that the second Collection we inserted and the Collection we got back are the same
        compareTest = cDao.find(exCollection2.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection2, compareTest);

        // Remove the example Collection from the database
        cDao.remove(exCollection);

        // Verify that the example Collection has been removed from the database but the second Collection still exists
        compareTest = cDao.find(exCollection.getId());
        assertNull(compareTest);
        compareTest = cDao.find(exCollection2.getId());
        assertNotNull(compareTest);
        assertEquals(exCollection2, compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        // Remove a Collection that doesn't exist in the database and verify that nothing happens
        cDao.remove(exCollection);

        // Verify that the Collection still does not exist
        Collection compareTest = cDao.find(exCollection.getId());
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Insert the example Collection into the database
        cDao.insert(exCollection);

        // Clear the database and then try to get the example Collection back
        cDao.clear();
        Collection compareTest = cDao.find(exCollection.getId());

        // Verify that the Collection we got back is null
        assertNull(compareTest);
    }
}
