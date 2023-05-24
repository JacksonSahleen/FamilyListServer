package request;

import model.Permissions;
import model.Recipe;

import java.util.List;

/**
 * Stores the data for a recipe service request
 *
 * @param data The client's recipe data to sync with the server
 * @param permissions The client's recipe permissions to sync with the server
 * @param removals The client's removals of specific recipes to sync with the server
 * @param revocations The client's revocations of user permissions to specific recipes to sync with the server
 */
public record RecipeRequest(List<Recipe> data, List<Permissions> permissions,
                            List<String> removals, List<Permissions> revocations) {}
