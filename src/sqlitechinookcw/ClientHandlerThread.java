/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlitechinookcw;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    String meesageToDelte = "";
    Parcel parcelRead;

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
     * @param genreList
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

            while ((parcelRead = (Parcel) objectInputStream.readObject()) != null) {

                System.out.println("Server: Read data from client: " + parcelRead);

                if (parcelRead.getIsTrack() == true) {

                    if (parcelRead.getTrack().getisViewTracks() == true) {
                        viewTracks(trackList);
                        objectOutputStream.writeObject(trackList);
                        System.out.println("main:");
                        for (Track t : trackList) {
                            System.out.print(t);
                            System.out.print(" | ");
                            System.out.println("");

                        }
                    } else if (parcelRead.getTrack().getisDelete() == true) {

                        if (parcelRead.getTrack().getisDeleteName() == true) {
                            deleteWhat = "Track Name"; // used to determine the value of the setString
                            selectSQL = "DELETE FROM tracks Where Name =?";
                            deleteTrack();
                        } else if (parcelRead.getTrack().getisDeleteName() == false) {
                            deleteWhat = "Track ID"; // used to determine the value of the setString
                            selectSQL = "DELETE FROM tracks Where TrackId =?";
                            deleteTrack();
                        }

                    } else if (parcelRead.getTrack().getIsEdit() == true) {
                        editTrack();
                    } else {
                        addTrack();
                    }

                } else if (parcelRead.getIsTrack() == false) {
                    if (parcelRead.getGenre().getIsViewGenre() == true) {
                        viewGenres(genreList);
                        objectOutputStream.writeObject(genreList);
                        System.out.println("main:");
                        for (Genre g : genreList) {
                            System.out.print(g);
                            System.out.print(" | ");
                            System.out.println("");

                        }
                    } else if (parcelRead.getGenre().getIsEdit() == true) {
                        editGenre();
                    } else if (parcelRead.getGenre().getIsDelete() == true) {

                        if (parcelRead.getGenre().getIsDeleteName() == true) {
                            deleteWhat = "Genre Name";
                            selectSQL = "DELETE FROM genres Where Name =?";
                            deleteGenre();
                        } else if (parcelRead.getGenre().getIsDeleteName() == false) {
                            deleteWhat = "Genre ID";
                            selectSQL = "DELETE FROM genres Where GenreId =?";
                            deleteGenre();
                        }
                    } else {
                        addGenre();
                    }

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

    public synchronized void viewTracks(ArrayList populateList) { // selectSQL = "INSERT INTO tracks(TrackId, Name, AlbumId, MediaTypeId, GenreId, Composer, Milliseconds, Bytes, UnitPrice) VALUES(?,?,?,?,?,?,?,?,?)";

        this.trackList = populateList;

        selectSQL = "SELECT * FROM tracks";

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {

            ResultSet resultSet = prep.executeQuery();

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

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void addTrack() {

        selectSQL = "INSERT INTO tracks(TrackId, Name, AlbumId, MediaTypeId, GenreId, Composer, Milliseconds, Bytes, UnitPrice) VALUES(?,?,?,?,?,?,?,?,?)";

        //String selectSQL = "SELECT * FROM tracks limit 10"; // lets just get the first 10 records for testing
        System.out.println("recieved2: " + selectSQL);
        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            prep.setString(1, Integer.toString(parcelRead.getTrack().getTrackId()));
            prep.setString(2, parcelRead.getTrack().getName());
            prep.setString(3, Integer.toString(parcelRead.getTrack().getAlbumId()));
            prep.setString(4, Integer.toString(parcelRead.getTrack().getMediaTypeId()));
            prep.setString(5, Integer.toString(parcelRead.getTrack().getGenreId()));
            prep.setString(6, parcelRead.getTrack().getComposer());
            prep.setString(7, Integer.toString(parcelRead.getTrack().getMilliseconds()));
            prep.setString(8, Integer.toString(parcelRead.getTrack().getBytes()));
            prep.setString(9, Double.toString(parcelRead.getTrack().getUnitPrice()));

            prep.executeUpdate();
            JOptionPane.showMessageDialog(null, "Inserted successfully to Data Base");
            System.out.print("Inserted succesfuly to Track Table");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void deleteTrack() {

        //selectSQL = "DELETE FROM tracks Where TrackId =?";
        //String selectSQL = "SELECT * FROM tracks limit 10"; // lets just get the first 10 records for testing
        System.out.println("recieved2: " + selectSQL);
        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            if ("Track ID".equals(deleteWhat)) {
                prep.setString(1, Integer.toString(parcelRead.getTrack().getTrackId()));
            } else if ("Track Name".equals(deleteWhat)) {
                prep.setString(1, (parcelRead.getTrack().getName()));;
            }
            prep.executeUpdate();
            System.out.print("Deleted succesfuly");
            JOptionPane.showMessageDialog(null, "Deleted succesfully");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void editTrack() {
        selectSQL = "UPDATE tracks SET TrackId='" + parcelRead.getTrack().getTrackId() + "' ,Name='" + parcelRead.getTrack().getName() + "', AlbumId='" + parcelRead.getTrack().getAlbumId() + "', MediaTypeId='" + parcelRead.getTrack().getMediaTypeId() + "', GenreId='" + parcelRead.getTrack().getGenreId() + "' ,Composer='" + parcelRead.getTrack().getComposer() + "',Milliseconds='" + parcelRead.getTrack().getMilliseconds() + "', Bytes='" + parcelRead.getTrack().getBytes() + "', UnitPrice='" + parcelRead.getTrack().getUnitPrice() + "' WHERE TrackId='" + parcelRead.getTrack().getTrackId() + "'";

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {

            prep.executeUpdate();
            System.out.print("Updated succesfuly");
            JOptionPane.showMessageDialog(null, "Updated  succesfully");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void viewGenres(ArrayList populateList) {

        this.genreList = populateList;

        selectSQL = "SELECT * FROM genres";

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {

            ResultSet resultSet = prep.executeQuery();

            // now rows
            while (resultSet.next()) {
                Genre genre = new Genre(
                        resultSet.getInt(1),
                        resultSet.getString(2));
                populateList.add(genre);

            }

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void addGenre() {

        selectSQL = "INSERT INTO genres(GenreId, Name) VALUES(?,?)";

        //String selectSQL = "SELECT * FROM tracks limit 10"; // lets just get the first 10 records for testing
        System.out.println("recieved2: " + selectSQL);
        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            prep.setString(1, Integer.toString(parcelRead.getGenre().getGenreId()));
            prep.setString(2, parcelRead.getGenre().getName());

            prep.executeUpdate();
            JOptionPane.showMessageDialog(null, "Inserted successfully to Data Base");
            System.out.print("Inserted succesfuly to Genre Table");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void editGenre() {
        selectSQL = "UPDATE genres SET GenreId='" + parcelRead.getGenre().getGenreId() + "' ,Name='" + parcelRead.getGenre().getName() + "'WHERE GenreId='" + parcelRead.getGenre().getGenreId() + "'";

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {

            prep.executeUpdate();
            System.out.print("Updated succesfuly");
            JOptionPane.showMessageDialog(null, "Updated  succesfully");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void deleteGenre() {

        System.out.println("recieved2: " + selectSQL);
        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            if ("Genre ID".equals(deleteWhat)) {
                prep.setString(1, Integer.toString(parcelRead.getGenre().getGenreId()));
            } else if ("Genre Name".equals(deleteWhat)) {
                prep.setString(1, (parcelRead.getGenre().getName()));;
            }
            prep.executeUpdate();
            System.out.print("Deleted succesfuly");
            JOptionPane.showMessageDialog(null, "Deleted succesfully");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
