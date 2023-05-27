package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dao.*;
import model.*;
import request.LoadRequest;
import result.LoadResult;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The service class responsible for handling Load requests.
 */
public class LoadService {

    /**
     * The Database object used to access the database.
     */
    private static Database db;

    /**
     * Gson object for handling JSON serialization and deserialization, includes a TypeAdapter for ZonedDateTime
     */
    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new TypeAdapter<ZonedDateTime>() {
                @Override
                public void write(JsonWriter out, ZonedDateTime value) throws IOException {
                    out.value(value.toString());
                }

                @Override
                public ZonedDateTime read(JsonReader in) throws IOException {
                    return ZonedDateTime.parse(in.nextString());
                }
            })
            .enableComplexMapKeySerialization()
            .create();

    /**
     * Constructor for LoadService
     */
    public LoadService() {
        db = new Database();
    }

    /**
     * Inner record to store the data for recipes from the provided JSON file
     *
     * @param id The id of the recipe
     * @param name The name of the recipe
     * @param owner The owner of the recipe
     * @param description The description of the recipe
     * @param ingredients Unique id of the ItemList containing the ingredients
     * @param steps Unique id of the ItemList containing the steps
     * @param lastUpdated The last time the recipe was updated
     */
    record RecipeData(String id, String name, String owner, String description, String ingredients,
                      String steps, String lastUpdated) {

        /*
         * Converts the RecipeData object to a Recipe object
         *
         * @param conn Connection to the database
         * @return Recipe object for the data in the RecipeData object
         */
        public Recipe toRecipe() {
            try {
                ItemListDAO lDao = new ItemListDAO(db.getConnection());

                // Convert the ingredients and steps to ItemLists
                ItemList ingredientsList = lDao.find(ingredients);
                ItemList stepsList = lDao.find(steps);

                db.closeConnection(true);
                return new Recipe(id, name, owner, description, ingredientsList,
                        stepsList, ZonedDateTime.parse(lastUpdated));
            } catch (DataAccessException e) {
                e.printStackTrace();
                db.closeConnection(false);
                return null;
            }
        }
    }

    /**
     * Inner record to store and access the provided testing data in JSON file
     *
     * @param authtokens Authtoken data to load into database
     * @param users User data to load into database
     * @param lists ItemList data to load into database
     * @param collections Collection data to load into database
     * @param recipes Recipe data to load into database
     * @param listPermissions List permissions data to load into database
     * @param recipePermissions Recipe permissions data to load into database
     * @param collectionRecipes Collection recipes data to load into database
     * @param clearDatabase Whether to clear the database before loading or just add to it
     */
    record LoadData(List<Authtoken> authtokens, List<User> users, List<ItemList> lists, List<Collection> collections,
                    List<RecipeData> recipes, List<List<String>> listPermissions, List<List<String>> recipePermissions,
                    List<List<String>> collectionRecipes, boolean clearDatabase) {

        /*
         * Converts the LoadData object to a LoadRequest object
         */
        public LoadRequest toRequest() {
            // Convert each RecipeData object to a Recipe object and store in a list
            List<Recipe> recipes = this.recipes.stream().map(RecipeData::toRecipe).toList();

            // Return the LoadRequest object with the changed data from the LoadData object
            return new LoadRequest(authtokens, users, lists, collections, recipes, listPermissions,
                                   recipePermissions, collectionRecipes, clearDatabase);
        }
    }

    /**
     * Loads data into the database.
     *
     * @param request The request object containing the data to be loaded.
     * @return The result object containing the success or failure of the operation.
     */
    public LoadResult load(LoadRequest request) {
        // Check that the request is valid
        if (!checkRequest(request)) {
            return new LoadResult("ERROR: Invalid request.", false);
        }

        // Load the data into the database
        try {
            Connection conn = db.getConnection();
            UserDAO uDao = new UserDAO(conn);
            AuthtokenDAO aDao = new AuthtokenDAO(conn);
            CategoryDAO catDao = new CategoryDAO(conn);
            CollectionDAO colDao = new CollectionDAO(conn);
            ItemDAO iDao = new ItemDAO(conn);
            ItemListDAO lDao = new ItemListDAO(conn);
            RecipeDAO rDao = new RecipeDAO(conn);

            // Clear the database if requested
            if (request.isClearDatabase()) {
                uDao.clear();
                aDao.clear();
                catDao.clear();
                colDao.clear();
                iDao.clear();
                lDao.clear();
                rDao.clear();
            }

            String message = "Successfully added ";
            List<Item> items = new ArrayList<>();
            List<Category> categories = new ArrayList<>();

            // Insert any provided Authtokens
            if (request.getAuthtokens() != null) {
                for (Authtoken authtoken : request.getAuthtokens()) {
                    aDao.insert(authtoken);
                }
                message += request.getAuthtokens().size() + " authtokens, ";
            }

            // Insert any provided Users
            if (request.getUsers() != null) {
                for (User user : request.getUsers()) {
                    uDao.insert(user);
                }
                message += request.getUsers().size() + " users, ";
            }

            // Insert any provided Lists
            if (request.getLists() != null) {
                for (ItemList list : request.getLists()) {
                    lDao.insert(list);

                    // Append the list's items to the items list
                    if (list.getItems() != null) {
                        items.addAll(list.getItems());
                    }

                    // Append the list's categories to the categories list
                    if (list.getCategories() != null) {
                        categories.addAll(list.getCategories());
                    }
                }
                message += request.getLists().size() + " lists, ";
            }

            // Insert Any provided Collections
            if (request.getCollections() != null) {
                for (Collection collection : request.getCollections()) {
                    colDao.insert(collection);
                }
                message += request.getCollections().size() + " collections, ";
            }

            // Insert any provided Recipes
            if (request.getRecipes() != null) {
                for (Recipe recipe : request.getRecipes()) {
                    rDao.insert(recipe);
                }
                message += request.getRecipes().size() + " recipes, ";
            }

            // Insert any provided Categories
            if (categories.size() > 0) {
                for (Category category : categories) {
                    catDao.insert(category);
                }
                message += categories.size() + " categories, ";
            }

            // Insert any provided Items
            if (items.size() > 0) {
                for (Item item : items) {
                    iDao.insert(item);
                }
                message += items.size() + " items, ";
            }

            // Parse and insert any provided list permissions
            for (List<String> permissionPair : request.getListPermissions()) {
                String listID = permissionPair.get(0);
                String username = permissionPair.get(1);

                // Give the user permission to the list if both exist
                ItemList list = lDao.find(listID);
                if (list != null && uDao.find(username) != null) {
                    lDao.share(list.getId(), username);
                }
            }

            // Parse and insert any provided recipe permissions
            for (List<String> permissionPair : request.getRecipePermissions()) {
                String recipeID = permissionPair.get(0);
                String username = permissionPair.get(1);

                // Give the user permission to the recipe if both exist
                Recipe recipe = rDao.find(recipeID);
                if (recipe != null && uDao.find(username) != null) {
                    rDao.share(recipe.getId(), username);
                }
            }

            // Parse and insert any provided collection recipes
            for (List<String> collectionPair : request.getCollectionRecipes()) {
                String collectionID = collectionPair.get(0);
                String recipeID = collectionPair.get(1);

                // Add the recipe to the collection if both exist
                if (colDao.find(collectionID) != null && rDao.find(recipeID) != null) {
                    colDao.addCollectionRecipe(collectionID, recipeID);
                }
            }

            // Commit the changes to the database and close the connection
            db.closeConnection(true);

            return new LoadResult(message, true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new LoadResult("ERROR: Internal Server Error (" + e.getMessage() + ").", false);
        }
    }

    /**
     * Creates a LoadRequest object from the data in a JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return The LoadRequest object created from the JSON file, or null if the request could not be created.
     */
    public LoadRequest getRequestFromFile(String filePath) {
        // Create a file object and reader from the file path
        File dataFile = new File(filePath);
        try (FileReader reader = new FileReader(dataFile)) {
            // Create a Gson object and use it to parse the JSON file
            LoadData data = GSON.fromJson(reader, LoadData.class);

            // Convert the LoadData object to a LoadRequest object
            return data.toRequest();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Simple wrapper to load data from a file into the database.
     *
     * @param filePath The path to the JSON file.
     * @return The LoadResult object created from the request.
     */
    public LoadResult loadFromFile(String filePath) {
        // Create a LoadRequest object from the file
        LoadRequest request = getRequestFromFile(filePath);

        // Load the data from the request
        return load(request);
    }

    /**
     * Checks that the request is valid.
     *
     * @param request The request object to be checked.
     * @return True if the request is valid, false otherwise.
     */
    private boolean checkRequest(LoadRequest request) {
        return request != null && (request.getAuthtokens() != null || request.getUsers() != null
                || request.getLists() != null || request.getCollections() != null ||
                request.getRecipes() != null || request.getListPermissions() != null ||
                request.getRecipePermissions() != null || request.getCollectionRecipes() != null);
    }
}
