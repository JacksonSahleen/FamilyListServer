import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

/**
 * The main class for the Family List Server program.
 */
public class Server {

    /**
     * The maximum number of waiting incoming connections to queue.
     */
    private static final int MAX_WAITING_CONNECTIONS = 12;

    /**
     * Initializes and runs the server.
     *
     * @param portNumber The port number on which the server should accept incoming client connections.
     */
    private void run(String portNumber) {
        // Create the HttpServer object, and bind it to the specified port.
        System.out.println("Initializing HTTP Server");
        HttpServer server;
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        server.setExecutor(null);

        // Create and install the HTTP contexts for the server.
        System.out.println("Creating contexts");
        // TODO: Create and install the handlers here.

        // Start the server.
        System.out.println("Starting server");
        server.start();
        System.out.println("Server started");
    }

    /**
     * The main method for the Family List Server program.
     *
     * @param args The command-line arguments for the program. Should only contain the port number.
     */
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}

