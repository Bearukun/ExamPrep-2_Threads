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
public class ConnectionHandler implements Runnable{
    
    private Socket connection;
    private AtomicInteger counter;

    public ConnectionHandler(Socket connection, AtomicInteger counter) {
        
        this.connection = connection;
        this.counter = counter;
        
    }
    

    @Override
    public void run() {
        
        OutputStream output = null;
        try {
            output = connection.getOutputStream();
            InputStream input = connection.getInputStream();
            PrintWriter writer = new PrintWriter(output);
            boolean active = true;
            // Read whatever comes in
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            System.out.println(line);
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
                
            }   switch (type) {
                
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
                System.out.println("Turnstile disconnected: " + counter);
            } catch (IOException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
