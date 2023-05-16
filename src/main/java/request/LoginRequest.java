package request;

/**
 * Record that stores the request data for the Login service
 *
 * @param username The user's unique username
 * @param password The user's password
 */
public record LoginRequest(String username, String password) {}
