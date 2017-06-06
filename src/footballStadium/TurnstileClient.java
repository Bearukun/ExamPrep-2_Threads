package footballStadium;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A client which connects to a server.
 */
public class TurnstileClient {

    private final String host;
    private final int port, turnstileNumber;
    private Socket clientSocket;

    public TurnstileClient(int turnstileNumber, String host, int port) {
        this.turnstileNumber = turnstileNumber;
        this.host = host;
        this.port = port;

    }

    public void open() throws IOException {

        clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(host, port));
        System.out.println("Client connected to server on port " + port);

    }

    /**
     * Sends a message to the server by opening a socket, writing to the input
     * and reading from the output.
     *
     * @param message The message to send
     * @throws IOException
     */
    public void sendMessage(int people) throws IOException {
        // Write to the server
        OutputStream output = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(output);

        //Tell server that we are a TurnstileClient!
        writer.println("turnstile-" + turnstileNumber);

        for (int i = 0; i < people; i++) {

            writer.println(1);

        }

        //Tell server that we are done
        writer.println("done");

        writer.flush();

    }

    /**
     * Reads a message from the server, if connected.
     *
     * @return A message from the server.
     * @throws IOException
     */
    @SuppressWarnings("Duplicates")
    public String readMessage() throws IOException {

        // Read from the server
        InputStream input = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String fromServer;

        while ((fromServer = reader.readLine()) == null) {

            fromServer += reader.readLine();

        }

        return fromServer;
    }

    public static void main(String[] args) throws IOException {

        new Thread(() -> {

            try {
                TurnstileClient client = new TurnstileClient(1, "localhost", 8080);

                client.open();

                client.sendMessage(100);

                System.out.println("Beep boop I'm done ^__^");
            } catch (IOException ex) {
                Logger.getLogger(TurnstileClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }).start();

        new Thread(() -> {

            try {
                TurnstileClient client = new TurnstileClient(2, "localhost", 8080);

                client.open();

                client.sendMessage(100);

                System.out.println("Beep boop I'm done ^__^");
            } catch (IOException ex) {
                Logger.getLogger(TurnstileClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }).start();

    }

}
