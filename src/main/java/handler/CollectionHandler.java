package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.CollectionRequest;
import result.CollectionResult;
import service.CollectionService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * WebAPI handler for the /collections API
 */
public class CollectionHandler extends Handler implements HttpHandler {

    /**
     * Handles HTTP requests containing the "/collections" URL path.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            CollectionResult result = null;

            // Only allow PUT requests for this operation.
            if (exchange.getRequestMethod().equalsIgnoreCase("put")) {
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();

                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    // Extract the auth token from the "Authorization" header
                    String authtoken = reqHeaders.getFirst("Authorization");

                    // Verify the authtoken provided by the user
                    UserAuthenticator authenticator = new UserAuthenticator();
                    if (authenticator.authenticate(authtoken)) {

                        // Get the request body's data from the HTTP exchange
                        InputStream reqBody = exchange.getRequestBody();
                        String reqData = readString(reqBody);

                        // Convert JSON string into a request object
                        CollectionRequest request = GSON.fromJson(reqData, CollectionRequest.class);

                        if (request != null) {
                            // Run the request through the service
                            CollectionService service = new CollectionService();
                            result = service.sync(request);

                            // Report a successful request
                            if (result.isSuccess()) {
                                // Log the successful request
                                System.out.println("CollectionHandler: Successfully retrieved person data.");

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
                        }
                    }
                }
            }

            // Report a failed request
            if (!success) {
                // Add a default error message if one was not provided
                if (result == null) {
                    result = new CollectionResult("ERROR: Invalid request.", false);
                }
                System.out.println("CollectionHandler: (Bad Request) " + result.getMessage());

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
            System.out.println("CollectionHandler: Failed to retrieve person data (Internal Server Error).");
            e.printStackTrace();
        }
    }
}
