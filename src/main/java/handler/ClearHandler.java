package handler;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;
import result.ClearResult;
import service.ClearService;

/**
 * WebAPI handler for the /clear API
 */
public class ClearHandler extends Handler implements HttpHandler {

    /**
     * Handles HTTP requests containing the "/clear" URL path.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            ClearResult result = null;

            // Only allow POST requests for this operation.
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();

                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    // Extract the auth token from the "Authorization" header
                    String authtoken = reqHeaders.getFirst("Authorization");

                    // Run the request through the corresponding service if the admin authtoken is provided
                    UserAuthenticator authenticator = new UserAuthenticator();
                    if (authenticator.authenticateAdmin(authtoken)) {
                        ClearService service = new ClearService();
                        result = service.clearDatabase();

                        // Report a successful request
                        if (result.isSuccess()) {
                            // Log the successful request
                            System.out.println("ClearHandler: Successfully cleared the database.");

                            // Return an "ok" status code to the client
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                            // Convert the result to a JSON string
                            String respData = GSON.toJson(result);

                            // Write the JSON string to the response body
                            OutputStream respBody = exchange.getResponseBody();
                            writeString(respData, respBody);

                            // Close the output stream
                            respBody.close();

                            success = true;
                        }
                    } else {
                        // Report an unsuccessful request from the user not being an admin
                        result = new ClearResult("ERROR: Clear attempted by a non-admin user", false);
                    }
                }
            }

            // Handle results of unsuccessful requests
            if (!success) {
                // Add a default error message if one was not provided
                if (result == null) {
                    result = new ClearResult("ERROR: Invalid request.", false);
                }
                System.out.println("ClearHandler: (Bad Request) " + result.getMessage());

                // Return a "bad request" status code to the client
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                // Convert the result to a JSON string
                String respData = GSON.toJson(result);

                // Write the JSON string to the response body
                OutputStream respBody = exchange.getResponseBody();
                writeString(respData, respBody);

                // Close the output stream
                respBody.close();
            }
        }
        catch (IOException e) {
            // Return an "internal server error" status code to the client
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();

            // Display/log the stack trace
            System.out.println("ClearHandler: Failed to clear the database (Internal Server Error).");
            e.printStackTrace();
        }
    }
}