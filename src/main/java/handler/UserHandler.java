package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.UserRequest;
import result.UserResult;
import service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * WebAPI handler for the /user API
 */
public class UserHandler extends Handler implements HttpHandler {

    /**
     * Handles HTTP requests containing the "/user" URL path.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            UserResult result = null;

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

                        // Convert JSON string to a request object
                        UserRequest request = GSON.fromJson(reqData, UserRequest.class);

                        if (request != null) {
                            // Run the request through the corresponding service
                            UserService service = new UserService();
                            result = service.sync(request);

                            // Report a successful request
                            if (result.isSuccess()) {
                                // Log the successful request
                                System.out.println("UserHandler: Successfully synced user data for "
                                        + result.getUser().getUsername() + ".");

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
                // Return a "bad request" status code to the client
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                // Log the failed request
                if (result != null) {
                    System.out.println("UserHandler: (Bad Request) " + result.getMessage());

                    // Convert the result to a JSON string
                    String respData = GSON.toJson(result);

                    // Write the JSON string to the response body
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);

                    // Close the output stream
                    respBody.close();
                } else {
                    System.out.println("UserHandler: (Bad Request) Failed to sync user data.");
                    exchange.getResponseBody().close();
                }
            }
        }
        catch (IOException e) {
            // Return an "internal server error" status code to the client
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();

            // Display/log the stack trace
            System.out.println("UserHandler: Failed to sync user data (Internal Server Error).");
            e.printStackTrace();
        }
    }
}
