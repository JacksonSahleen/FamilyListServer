package request;

import model.Collection;

import java.util.List;

/**
 * Stores the data for a collection service request
 *
 * @param authtoken The authtoken of the user making the request
 * @param data The client's collection data to sync with the server
 */
public record CollectionRequest(String authtoken, List<Collection> data) {}
