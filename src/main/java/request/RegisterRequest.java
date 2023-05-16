package request;

/**
 * Record that stores the request data for the Register service
 *
 * @param username The username of the user to register
 * @param password The password of the user to register
 * @param firstName The first name of the user to register
 * @param lastName The last name of the user to register
 */
public record RegisterRequest(String username, String password, String firstName, String lastName) {}
