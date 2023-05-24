package model;

/**
 * Record that stores the permissions relationship data for a database object
 *
 * @param object The object that the permissions are for
 * @param holder The holder of the permissions
 */
public record Permissions(String object, String holder) {}
