/*
 *Written by Nourelrahman Elgeziry  SID: 7593776
 */
package sqlitechinookcw;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is our main class for the server-side program. This class starts a
 * threaded server via server sockets and then allows each thread to perform
 * their intended functionality in the ClientHandlerThread class.
 *
 * @author Chris Bass
 */
public class SQLiteChinookCw {

    private void connectToClients() {
        System.out.println("Server: Server starting.");

        try (ServerSocket serverSocket = new ServerSocket(2000)) {  // instance of the server socket with the coressponding socket number as the gui

            while (true) {
                System.out.println("Server: Waiting for connecting client...");

                try {
                    Socket socket = serverSocket.accept();

                    ClientHandlerThread clientHandlerThread = new ClientHandlerThread(socket);
                    Thread connectionThread = new Thread(clientHandlerThread);
                    connectionThread.start();
                } catch (IOException ex) {
                    System.out.println("Server: Could not start connection to a client.");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Server: Closed down");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SQLiteChinookCw app = new SQLiteChinookCw();
        app.connectToClients();

    }

    /**
     * This function simply reads the first 10 records / rows from the tracks
     * table in our SQLite database and then outputs them to the console for
     * testing, it also parses the result set and puts the records into a
     * data-structure. We have made it a synchronized method so that it prevents
     * any issue with thread interference or data races.
     *
     * @param populateList the data structure to populate with data from the
     * database
     */
}
