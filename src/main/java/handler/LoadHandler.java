package handler;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import request.LoadRequest;
import result.LoadResult;
import service.LoadService;

/**
 * WebAPI handler for the /load API
 */
public class LoadHandler extends Handler implements HttpHandler {

    /**
     * Handles HTTP requests containing the "/load" URL path.
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
            LoadResult result = null;

            // Only allow POST requests for this operation.
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                // Get the request body
                InputStream reqBody = exchange.getRequestBody();

                // Read JSON string from the input stream
                String reqData = readString(reqBody);

                // Convert JSON string to a Request object
                LoadRequest request = gson.fromJson(reqData, LoadRequest.class);

                // Run the request through the corresponding service
                LoadService service = new LoadService();
                result = service.load(request);

                // Report a successful request
                if (result.isSuccess()) {
                    // Log the successful request
                    System.out.println("LoadHandler: " + result.getMessage());

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
                    System.out.println("LoadHandler: (Bad Request) " + result.getMessage());

                    // Convert the result to a JSON string
                    String respData = gson.toJson(result);

                    // Write the JSON string to the response body
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);

                    // Close the output stream
                    respBody.close();
                } else {
                    System.out.println("LoadHandler: (Bad Request) Failed to load data into the database.");
                    exchange.getResponseBody().close();
                }
            }
        }
        catch (IOException e) {
            // Return an "internal server error" status code to the client
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();

            // Display/log the stack trace
            System.out.println("LoadHandler: Failed to log in user (Internal Server Error).");
            e.printStackTrace();
        }
    }
}