package footballStadium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bearu
 */
public class ConnectionHandler implements Runnable {

    private Socket connection;
    private AtomicInteger counter;


    public ConnectionHandler(Socket connection, AtomicInteger counter) {
        this.connection = connection;
        this.counter = counter;
    }

    @Override
    public void run() {

        OutputStream output = null;
        PrintWriter writer;
        InputStream input;
        String type = "";


        try {

            output = connection.getOutputStream();

            input = connection.getInputStream();

            writer = new PrintWriter(output);

            boolean active = true;

           
            // Read whatever comes in
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

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

                        sendMessage(counter.get());

                    } else if (readLine.equals("done")) {

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

        } catch (IOException ex) {

            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            try {

                output.close();
                connection.close();
                System.out.println(type + " disconnected: \n Current counter:" + counter);

            } catch (IOException ex) {

                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }
    
    /**
     * Sends a message to the client, writing to the input
     *
     * @param message The message to send
     * @throws IOException
     */
    public void sendMessage(int people) throws IOException {
        
        // Write to the server
        OutputStream output = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(output);

        //Tell server that we are a TurnstileClient!
        writer.println(people);

        writer.flush();
        
    }

}
