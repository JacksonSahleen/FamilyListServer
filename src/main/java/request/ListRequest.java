package request;

import model.ItemList;
import model.Permissions;

import java.util.List;

/**
 * Stores the data for a list service request
 *
 * @param data The client's list data to sync with the server
 * @param permissions The client's list permissions to sync with the server
 * @param removals The client's removals of specific lists to sync with the server
 * @param revocations The client's revocations of user permissions to specific lists to sync with the server
 */
public record ListRequest(List<ItemList> data, List<Permissions> permissions,
                          List<String> removals, List<Permissions> revocations) {}
