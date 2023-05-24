package handler;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

import com.sun.net.httpserver.*;

/**
 * WebAPI handler for retrieving a file or webpage from the server
 */
public class FileHandler extends Handler implements HttpHandler {

    /**
     * Handles HTTP requests containing the "/" URL path.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            // Only allow GET requests for this operation.
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                // Get the request URI
                String uri = exchange.getRequestURI().toString();

                // Convert the URI into a valid file path
                File file = convertURItoFile(uri);

                if (file != null) {
                    // Log the successful request
                    System.out.println("FileHandler: Successfully retrieved file " + file.getName() + ".");

                    // Return an "ok" status code to the client
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    // Log the failed request
                    System.out.println("FileHandler: ERROR: Unable to retrieved file for " + uri + ".");

                    // Return a "not found" status code to the client and set the file to the 404 page
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    file = new File("web" + File.separator + "HTML" + File.separator + "404.html");
                }

                // Copy the file to the response body
                OutputStream respBody = exchange.getResponseBody();
                Files.copy(file.toPath(), respBody);

                // Close the output stream
                respBody.close();

                success = true;
            }

            // Report a failed request
            if (!success) {
                // Return a "bad request" status code to the client
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();

                // Log the failed request
                System.out.println("FileHandler: Failed to retrieve file (Bad Request).");
            }
        }
        catch (IOException e) {
            // Return an "internal server error" status code to the client
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();

            // Display/log the stack trace
            System.out.println("FileHandler: Failed to retrieve file (Internal Server Error).");
            e.printStackTrace();
        }
    }

    /**
     * Converts a URI from the HTTP API into a valid file
     *
     * @param uri the URI to convert
     * @return the URI as a valid file if it exists, or null if it doesn't
     */
    private File convertURItoFile(String uri) {

        // Prepend "web" to the URI to convert it into a valid file path
        String path;
        if (uri.equals("/")) {
            path = "web" + File.separator + "index.html";
        } else {
            path = "web" + File.separator + uri;
        }

        // Create a new file from the path
        File file = new File(path);

        // Return the file if it exists, and the 404 page's file if it doesn't
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }
}
