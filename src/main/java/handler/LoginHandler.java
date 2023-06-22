package handler;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;
import request.LoginRequest;
import result.UserResult;
import service.LoginService;

/**
 * WebAPI handler for the /user/login API
 */
public class LoginHandler extends Handler implements HttpHandler {

    /**
     * Handles HTTP requests containing the "/user/login" URL path.
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

            // Only allow POST requests for this operation.
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                // Get the request body
                InputStream reqBody = exchange.getRequestBody();

                // Read JSON string from the input stream
                String reqData = readString(reqBody);

                // Convert JSON string to a Request object
                LoginRequest request = GSON.fromJson(reqData, LoginRequest.class);

                // Run the request through the corresponding service
                LoginService service = new LoginService();
                result = service.login(request);

                // Report a successful request
                if (result.isSuccess()) {
                    // Log the successful request
                    System.out.println("LoginHandler: Successfully logged in as "
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

            // Report a failed request
            if (!success) {
                // Return a "bad request" status code to the client
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                // Log the failed request
                if (result != null) {
                    System.out.println("LoginHandler: (Bad Request) " + result.getMessage());

                    // Convert the result to a JSON string
                    String respData = GSON.toJson(result);

                    // Write the JSON string to the response body
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);

                    // Close the output stream
                    respBody.close();
                } else {
                    System.out.println("LoginHandler: (Bad Request) Failed to log in user.");
                    exchange.getResponseBody().close();
                }
            }
        }
        catch (IOException e) {
            // Return an "internal server error" status code to the client
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();

            // Display/log the stack trace
            System.out.println("LoginHandler: Failed to log in user (Internal Server Error).");
            e.printStackTrace();
        }
    }
}