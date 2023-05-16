package request;

import model.Recipe;

import java.util.List;

/**
 * Stores the data for a recipe service request
 *
 * @param authtoken The authtoken of the user making the request
 * @param data The client's recipe data to sync with the server
 */
public record RecipeRequest(String authtoken, List<Recipe> data) {}
