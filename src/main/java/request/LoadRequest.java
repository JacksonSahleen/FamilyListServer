package request;

import model.Collection;
import model.ItemList;
import model.Recipe;

import java.util.List;

/**
 * Stores the data for a load service request
 *
 * @param lists The list data to load into the server
 * @param collections The collection data to load into the server
 * @param recipes The recipe data to load into the server
 * @param clearDatabase Whether to clear the database before loading or just add to it
 */
public record LoadRequest(List<ItemList> lists, List<Collection> collections, List<Recipe> recipes, boolean clearDatabase) {}