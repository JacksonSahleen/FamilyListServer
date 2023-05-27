package request;

import model.ItemList;
import model.Permissions;

import java.util.List;

/**
 * Stores the data for a list service request
 */
public class ListRequest {

    /**
     * The List data for the request
     */
    private List<ItemList> data;

    /**
     * The permissions data for the request
     */
    private List<Permissions> permissions;

    /**
     * The List removal data for the request
     */
    private List<String> removals;

    /**
     * The permission revocation data for the request
     */
    private List<Permissions> revocations;

    /**
     * Creates a ListRequest object with the given data
     *
     * @param data The List data for the request
     * @param permissions The permissions data for the request
     * @param removals The removal data for the request
     * @param revocations The revocation data for the request
     */
    public ListRequest(List<ItemList> data, List<Permissions> permissions,
                       List<String> removals, List<Permissions> revocations) {
        this.data = data;
        this.permissions = permissions;
        this.removals = removals;
        this.revocations = revocations;
    }

    public List<ItemList> getData() {
        return data;
    }

    public void setData(List<ItemList> data) {
        this.data = data;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRemovals() {
        return removals;
    }

    public void setRemovals(List<String> removals) {
        this.removals = removals;
    }

    public List<Permissions> getRevocations() {
        return revocations;
    }

    public void setRevocations(List<Permissions> revocations) {
        this.revocations = revocations;
    }
}
