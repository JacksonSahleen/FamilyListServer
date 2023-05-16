package request;

import model.ItemList;

import java.util.List;

/**
 * Stores the data for a list service request
 *
 * @param authtoken The authtoken of the user making the request
 * @param data The client's list data to sync with the server
 */
public record ListRequest(String authtoken, List<ItemList> data) {}
