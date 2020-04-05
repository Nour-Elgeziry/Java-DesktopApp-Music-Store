/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlitechinookcw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author nour
 */
public class ClientHandlerThread implements Runnable {

    String getWhat = "";
    String deleteWhat = "";
    String selectSQL = "";
    

    Track trackRead;

    private final Socket socket;
    ArrayList<Track> trackList = new ArrayList<>();
    ArrayList<Genre> genreList = new ArrayList<>();

    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    private static int connectionCount = 0;
    private final int connectionNumber;

    /**
     * Constructor just initialises the connection to client.
     *
     * @param socket the socket to establish the connection to client.
     * @param arrayList
     * @param trackList
     * @param hashMapNames a reference to the lookup table for getting email.
     * @throws IOException if an I/O error occurs when creating the input and
     * output streams, or if the socket is closed, or socket is not connected.
     */
    public ClientHandlerThread(Socket socket, ArrayList arrayList) throws IOException {
        this.socket = socket;
        this.trackList = arrayList;
        this.genreList = arrayList;

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        connectionCount++;
        connectionNumber = connectionCount;
        threadSays("Connection " + connectionNumber + " established.");
    }

    @Override
    public void run() {

        try {
            System.out.println("Server: Waiting for data from client...");
            while ((trackRead = (Track) objectInputStream.readObject()) != null) {

                System.out.println("Server: Read data from client: " + trackRead);
                if ("All Tracks".equals(trackRead.distinguish)) {
                    getWhat = "Tracks";
                    readAll(trackList);
                    objectOutputStream.writeObject(trackList);
                    System.out.println("main:");
                    for (Track t : trackList) {
                        System.out.print(t);
                        System.out.print(" | ");
                        System.out.println("");

                    }

                } else if ("All Genres".equals(trackRead.distinguish)) {
                    getWhat = "Genres";
                    readAll(genreList);
                    objectOutputStream.writeObject(genreList);
                    System.out.println("main:");
                    for (Genre g : genreList) {
                        System.out.print(g);
                        System.out.print(" | ");
                        System.out.println("");

                    }

                } else if (trackRead.name != null && trackRead.composer != null) {

                    insertTrack();

                } else if (trackRead.trackId != 0 && trackRead.deleteUnique == 0000) {
                    deleteWhat = "Track ID";
                    deleteTrack();
                } else if(trackRead.name != null && trackRead.deleteUnique == 0000) { 
                    deleteWhat = "Track Name";
                    deleteTrack();
                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientHandlerThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                threadSays("We have lost connection to client " + connectionNumber + ".");
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandlerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void threadSays(String say) {

        System.out.println("ClientHandlerThread" + connectionNumber + ": " + say);
    }

    public synchronized void readAll(ArrayList populateList) { // selectSQL = "INSERT INTO tracks(TrackId, Name, AlbumId, MediaTypeId, GenreId, Composer, Milliseconds, Bytes, UnitPrice) VALUES(?,?,?,?,?,?,?,?,?)";

        System.out.println("recieved: " + getWhat);

        switch (getWhat) {
            case "Tracks": {
                this.trackList = populateList;

                selectSQL = "SELECT * FROM tracks limit 10";
                break;
            }
            case "Genres": {
                this.genreList = populateList;
                selectSQL = "SELECT * FROM genres limit 10";
                break;
            } // lets just get the first 10 records for testing}
        }

        //String selectSQL = "SELECT * FROM tracks limit 10"; // lets just get the first 10 records for testing
        System.out.println("recieved2: " + selectSQL);
        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            prep.setString(1, trackRead.name);
            ResultSet resultSet = prep.executeQuery();
            switch (selectSQL) {
                case "SELECT * FROM tracks limit 10":
                    // now rows
                    while (resultSet.next()) {
                        Track track = new Track(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getInt(3),
                                resultSet.getInt(4),
                                resultSet.getInt(5),
                                resultSet.getString(6),
                                resultSet.getInt(7),
                                resultSet.getInt(8),
                                resultSet.getDouble(9));
                        populateList.add(track);

                    }
                    break;
                case "SELECT * FROM genres limit 10":

                    while (resultSet.next()) {
                        Genre genre = new Genre(
                                resultSet.getInt(1),
                                resultSet.getString(2));
                        populateList.add(genre);

                    }

                    break;
            }

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void insertTrack() {

        selectSQL = "INSERT INTO tracks(TrackId, Name, AlbumId, MediaTypeId, GenreId, Composer, Milliseconds, Bytes, UnitPrice) VALUES(?,?,?,?,?,?,?,?,?)";

        //String selectSQL = "SELECT * FROM tracks limit 10"; // lets just get the first 10 records for testing
        System.out.println("recieved2: " + selectSQL);
        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            prep.setString(1, Integer.toString(trackRead.trackId));
            prep.setString(2, trackRead.name);
            prep.setString(3, Integer.toString(trackRead.albumId));
            prep.setString(4, Integer.toString(trackRead.mediaTypeId));
            prep.setString(5, Integer.toString(trackRead.genreId));
            prep.setString(6, trackRead.composer);
            prep.setString(7, Integer.toString(trackRead.milliseconds));
            prep.setString(8, Integer.toString(trackRead.bytes));
            prep.setString(9, Double.toString(trackRead.unitPrice));

            prep.executeUpdate();
            JOptionPane.showMessageDialog(null, "Inserted successfully to Data Base");
            System.out.print("Inserted succesfuly");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void deleteTrack() {
        
        if ("Track ID".equals(deleteWhat)){
            selectSQL = "DELETE FROM tracks Where TrackId =?";
        }else if ("Track Name".equals(deleteWhat)){
             selectSQL = "DELETE FROM tracks Where Name =?";
        }

        

        //String selectSQL = "SELECT * FROM tracks limit 10"; // lets just get the first 10 records for testing
        System.out.println("recieved2: " + selectSQL);
        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
             if ("Track ID".equals(deleteWhat)){
            prep.setString(1, Integer.toString(trackRead.trackId));
        }else if ("Track Name".equals(deleteWhat)){
             prep.setString(1, (trackRead.name));;
        }
            prep.executeUpdate();
            System.out.print("Deleted succesfuly");
            JOptionPane.showMessageDialog(null, "Deleted succesfully");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
