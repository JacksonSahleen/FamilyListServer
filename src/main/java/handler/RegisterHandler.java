package handler;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

/**
 * WebAPI handler for the /register API
 */
public class RegisterHandler extends Handler implements HttpHandler {

    /**
     * Handles HTTP requests containing the "/register" URL path.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Gson gson = new Gson();
        boolean success = false;

        try {
            RegisterResult result = null;

            // Only allow POST requests for this operation.
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                // Get the request body
                InputStream reqBody = exchange.getRequestBody();

                // Read JSON string from the input stream
                String reqData = readString(reqBody);

                // Convert JSON string to a Request object
                RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);

                // Run the request through the corresponding service
                RegisterService service = new RegisterService();
                result = service.register(request);

                // Report a successful request
                if (result.isSuccess()) {
                    // Log the successful request
                    System.out.println("Register Handler: Successfully registered the user " + result.getUsername() + ".");

                    // Return an "ok" status code to the client
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    // Convert the result to a JSON string
                    String respData = gson.toJson(result);

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
                    String respData = gson.toJson(result);

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
            System.out.println("RegisterHandler: Failed to register the user (Internal Server Error).");
            e.printStackTrace();
        }
    }
}