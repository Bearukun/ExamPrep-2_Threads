package footballStadium;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * A server which simply just echoes whatever it receives
 */
public class TurnstileServer {

    private final String host;
    private final int port;
    private AtomicInteger counter = new AtomicInteger(0);

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
            handleConnection(connection);
            // Now the connection has been handled and we've sent our reply
            // -- So now the connection can be closed so we can open more
            //    sockets in the future
            connection.close();
            
            System.out.println("Turnstile disconnected: " + counter);

        }

    }

    /**
     * Handles a connection from a client by simply echoing back the same thing the client sent.
     *
     * @param connection The Socket connection which is connected to the client.
     * @throws IOException If network or I/O or something goes wrong.
     */
    private void handleConnection(Socket connection) throws IOException {

        OutputStream output = connection.getOutputStream();
        InputStream input = connection.getInputStream();
        PrintWriter writer = new PrintWriter(output);
        
        boolean active = true;

        // Read whatever comes in
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        
        String[] splitString = line.split("-",2);
        
        String type = splitString[0];
        int id = Integer.parseInt(splitString[1]);
        
        if(type.equals("turnstile")){
            
            while(active){
                
                String readLine = reader.readLine();
                
                if(readLine.equals("1")){
                    
                    
                    counter.addAndGet(1);
                    
                    
                }else if(readLine.equals("done")){
                    
                    active = false;
                    
                }
                
                
            }
            
            
            
            
        }
              
        
        
        switch (type) {

            case "UPPER":

                break;

          
            default:

                writer.println("Wrong command!");
                break;

        }

    }

    public static void main(String[] args) throws IOException {

        TurnstileServer server = new TurnstileServer("localhost", 8080);

        server.startServer();

    }

}
