package request;

import model.Collection;
import model.Permissions;

import java.util.List;

/**
 * Stores the data for a collection service request
 */
public class CollectionRequest {

    /**
     * The Collection data for the request
     */
    private List<Collection> data;

    /**
     * The association data for the request
     */
    private List<Permissions> associations;

    /**
     * The Collection removal data for the request
     */
    private List<String> removals;

    /**
     * The association revocation data for the request
     */
    private List<Permissions> revocations;

    /**
     * Creates a CollectionRequest object with the given data
     *
     * @param data The Collection data for the request
     * @param associations The association data for the request
     * @param removals The removal data for the request
     * @param revocations The revocation data for the request
     */
    public CollectionRequest(List<Collection> data, List<Permissions> associations,
                             List<String> removals, List<Permissions> revocations) {
        this.data = data;
        this.associations = associations;
        this.removals = removals;
        this.revocations = revocations;
    }

    public List<Collection> getData() {
        return data;
    }

    public void setData(List<Collection> data) {
        this.data = data;
    }

    public List<Permissions> getAssociations() {
        return associations;
    }

    public void setAssociations(List<Permissions> associations) {
        this.associations = associations;
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
