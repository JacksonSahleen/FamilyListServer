package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * WebAPI handler for the /recipes API
 */
public class RecipeHandler extends Handler implements HttpHandler {


    /**
     * Handles HTTP requests containing the "/recipe" URL path.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // TODO: Implement RecipeHandler
    }
}
