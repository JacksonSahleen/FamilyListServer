package request;

import model.Collection;
import model.Permissions;

import java.util.List;

/**
 * Stores the data for a collection service request
 *
 * @param data The client's collection data to sync with the server
 * @param associations The client's associations of recipes in collections to sync with the server
 * @param removals The client's removals of specific collections to sync with the server
 * @param revocations The client's revocations of a recipe's association with a collection to sync with the server
 */
public record CollectionRequest(List<Collection> data, List<Permissions> associations,
                                List<String> removals, List<Permissions> revocations) {}
