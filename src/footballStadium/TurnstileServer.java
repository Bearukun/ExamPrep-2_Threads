package footballStadium;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * A server which simply just echoes whatever it receives
 */
public class TurnstileServer {

    private final String host;
    private final int port;
    private AtomicInteger counter = new AtomicInteger(0);
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TurnstileServer(String host, int port) {

        this.host = host;
        this.port = port;

    }

    /**
     * Starts running the server.
     *
     * @throws IOException If network or I/O or something goes wrong.
     */
    public void startServer() throws IOException {

        // Create a new unbound socket
        ServerSocket socket = new ServerSocket();
        // Bind to a port number
        socket.bind(new InetSocketAddress(host, port));

        System.out.println("Server listening on port " + port);

        // Wait for a connection
        Socket connection;
        while ((connection = socket.accept()) != null) {

            // Handle the connection in the #handleConnection method below
            executorService.execute(new ConnectionHandler(connection, counter));

        }

    }

    public static void main(String[] args) throws IOException {

        TurnstileServer server = new TurnstileServer("localhost", 8080);

        server.startServer();

    }

}
