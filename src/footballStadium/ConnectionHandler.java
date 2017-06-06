package footballStadium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bearu
 */
public class ConnectionHandler implements Runnable {

    private Socket connection;
    private TurnstileServer server;
    private AtomicInteger counter;

    private OutputStream output = null;
    private PrintWriter writer;
    private BufferedReader reader;

    public ConnectionHandler(Socket connection, TurnstileServer server, AtomicInteger counter) {
        this.connection = connection;
        this.server = server;
        this.counter = counter;
        System.out.println("New connection");
    }

    @Override
    public void run() {

        String type = "";

        try {

            output = connection.getOutputStream();

            InputStream input = connection.getInputStream();

            writer = new PrintWriter(connection.getOutputStream(), true);

            reader = new BufferedReader(new InputStreamReader(input));

            boolean active = true;

            String line = reader.readLine();

            if (line.contains("-")) {

                String[] splitString = line.split("-", 2);

                type = splitString[0];

                int id = Integer.parseInt(splitString[1]);

            } else {

                type = line;

            }

            if (type.equals("turnstile")) {

                while (active) {

                    String readLine = reader.readLine();

                    if (readLine.equals("1")) {

                        counter.addAndGet(1);

                    } else if (readLine.equals("done")) {

                        active = false;

                    }

                }

            } else if (type.equals("monitor")) {

                while (active) {

                    String readLine = reader.readLine();

                    if (readLine.equals("count")) {

                        writer.println("Hej");
                        writer.println(counter.get());
                        

                    } else if (readLine.equals("done")) {

                        active = false;

                    }

                }

            }

            switch (type) {

                default:

                    writer.println("Wrong command!");
                    break;

            }

        } catch (IOException ex) {

            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            try {

                System.out.println(type + " disconnected: \n Current counter:" + counter.get());

                output.close();
                writer.close();
                connection.close();

            } catch (IOException ex) {

                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

}
