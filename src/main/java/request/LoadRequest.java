package request;

import model.*;

import java.util.List;

/**
 * Stores the data for a load service request
 *
 * @param users The user data to load into the server
 * @param lists The list data to load into the server
 * @param collections The collection data to load into the server
 * @param recipes The recipe data to load into the server
 * @param categories The category data to load into the server
 * @param items The item data to load into the server
 * @param listPermissions The list permissions to load into the server
 * @param recipePermissions The recipe permissions to load into the server
 * @param collectionRecipes The collection recipes to load into the server
 * @param clearDatabase Whether to clear the database before loading or just add to it
 */
public record LoadRequest(List<User> users, List<ItemList> lists, List<Collection> collections, List<Recipe> recipes,
                          List<Category> categories, List<Item> items, List<List<String>> listPermissions,
                          List<List<String>> recipePermissions, List<List<String>> collectionRecipes,
                          boolean clearDatabase) {}