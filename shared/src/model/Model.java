package model;

import java.time.ZonedDateTime;

/**
 * Base version of a model object
 */
public abstract class Model {

    /**
     * Unique identifier for this model object
     */
    protected String id;

    /**
     * The date and time the model object was last updated
     */
    protected ZonedDateTime lastUpdated;

    /**
     * Abstract getter for the model ID field
     *
     * @return The model ID
     */
    public abstract String getId();

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Sets the date and time the model object was last updated to the current date and time
     */
    public void setLastUpdated() {
        this.lastUpdated = ZonedDateTime.now();
    }
}
