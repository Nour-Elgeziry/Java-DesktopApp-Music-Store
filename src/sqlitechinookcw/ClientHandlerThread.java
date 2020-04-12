/*
 *Written by Nourelrahman Elgeziry  SID: 7593776
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
 * This class handles all user requests. After receiving the socket, it carries
 * various checks in order to determine what function to call. After calling the
 * appropriate function the connection to database is established using the
 * ConnectionFactory class.
 *
 * @author nour
 */
public class ClientHandlerThread implements Runnable {

    private String deleteWhat = ""; // is set by delete track/genre functions to ddetermine using id or name to delete
    private String selectSQL = ""; // used to preset the sql statement when neededx to 
    int genreId;// used to store the genre id in the getTracksByGenre function.

    private Parcel parcelRead; // parcel instances to be able to recieve object input streams

    private final Socket socket; // declaring new instance of the socket

    ArrayList<Track> trackList = new ArrayList<>(); // arraylists used to popoulate when calling view tracks/genres function and send them through objectoutputstream to client
    ArrayList<Genre> genreList = new ArrayList<>();

    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    private static int connectionCount = 0;
    private final int connectionNumber;

    /**
     * Constructor just initialises the connection to client.
     *
     * @param socket the socket to establish the connection to client.
     * @throws IOException if an I/O error occurs when creating the input and
     * output streams, or if the socket is closed, or socket is not connected.
     */
    public ClientHandlerThread(Socket socket) throws IOException {
        this.socket = socket;

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        connectionCount++;
        connectionNumber = connectionCount;
        threadSays("Connection " + connectionNumber + " established.");
    }

    /*run function carries all checks to make sure hte correct functions are called. It checks if any object is recieved and assin=gn the value to the parcelRead instance of the parcel object. 
     Then checks for differnt information to identify the functionality intended. Catches IO and class not found exceptions.
     */
    @Override
    public void run() {

        try {
            System.out.println("Server: Waiting for data from client...");

            while ((parcelRead = (Parcel) objectInputStream.readObject()) != null) {     // assign recieved object to parcelRead

                System.out.println("Server: Read data from client: " + parcelRead);

                if (parcelRead.getIsTrack() == true) {              // checks if sent object is of type track or genre

                    if (parcelRead.getTrack().getIsSetUpperBound() == true) {       // ifsetting upper bound functionality intended

                        setUpperBound(); // call the function
                    } else if (parcelRead.getTrack().getisViewTracks() == true) {  // viewng tracks functionality intended
                        viewTracks(trackList);// call the function with the trackList arraylist parameter

                        objectOutputStream.writeObject(trackList); // write the object to the objectoutput stream
                        objectOutputStream.reset(); // resetting the the stream to avoid using refrencess insted of new objects
                        System.out.println("main:");  // printing the tracklist to the console.
                        for (Track t : trackList) {
                            System.out.print(t);
                            System.out.print(" | ");
                            System.out.println("");

                        }
                        trackList.clear(); // clearing traclist to avoid repetetion
                    } else if (parcelRead.getTrack().getIsSearch() == true) {  // get track by genre functionality intended
                        getTracksByGenre(trackList); //call the function with tracklist parameter
                        System.out.println("main:");
                        for (Track t : trackList) {
                            System.out.print(t);
                            System.out.print(" | ");
                            System.out.println("");

                        }
                        objectOutputStream.writeObject(trackList); // writing the object to the stream
                        objectOutputStream.reset(); // resetting to avoid using refrences

                        trackList.clear();// clearing to avoid repetetion

                    } else if (parcelRead.getTrack().getisDelete() == true) { // delete track functionality intended

                        if (parcelRead.getTrack().getisDeleteName() == true) { // delete by name ?
                            deleteWhat = "Track Name"; // used to determine the value of the setString 
                            selectSQL = "DELETE FROM tracks Where Name =?"; // pre setting the sqlite statement
                            deleteTrack();
                        } else if (parcelRead.getTrack().getisDeleteName() == false) { //delete  using track id
                            deleteWhat = "Track ID"; // used to determine the value of the setString
                            selectSQL = "DELETE FROM tracks Where TrackId =?";// pre setting the sqlite statement
                            deleteTrack();
                        }

                    } else if (parcelRead.getTrack().getIsEdit() == true) { // edit track functionality intended
                        editTrack();
                    } else if (parcelRead.getTrack().getIsRandom() == true) { // random track functonality intended
                        randomTrackGenerator(trackList);

                        System.out.println("main:");
                        for (Track t : trackList) {
                            System.out.print(t);
                            System.out.print(" | ");
                            System.out.println("");

                        }
                        objectOutputStream.writeObject(trackList);// writing the object to the stream
                        objectOutputStream.reset(); // resetting to avoid using refrences

                        trackList.clear(); // clearing to avoid repetetion

                    } else { // if non of the above , then add track functionality intended
                        addTrack();

                    }

                } else if (parcelRead.getIsTrack() == false) { // using genre object (dealing with genres)
                    if (parcelRead.getGenre().getIsViewGenre() == true) { // view genre intended
                        viewGenres(genreList);

                        objectOutputStream.writeObject(genreList);// writing the object to the stream
                        objectOutputStream.reset();// resetting to avoid using refrences

                    } else if (parcelRead.getGenre().getIsEdit() == true) { // edit genre functionality inteded
                        editGenre();
                    } else if (parcelRead.getGenre().getIsDelete() == true) { // delete track functionality intended

                        if (parcelRead.getGenre().getIsDeleteName() == true) { // delte genre by name
                            deleteWhat = "Genre Name"; // setting the string to be used in the function
                            selectSQL = "DELETE FROM genres Where Name =?"; // presetting the sqlite statement
                            deleteGenre();
                        } else if (parcelRead.getGenre().getIsDeleteName() == false) {  // delete using genre id
                            deleteWhat = "Genre ID";
                            selectSQL = "DELETE FROM genres Where GenreId =?";
                            deleteGenre();
                        }
                    } else { // if non of the above , then add genre functionality intended.
                        addGenre();

                    }

                } else {
                    System.out.println("eroor non choosen");
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

    /*
     view tracks function handels viewing tracks to the user. 
     it connects to the database and performs the sqlite staments and populates the arraylist 
     */
    public synchronized void viewTracks(ArrayList populateList) {

        selectSQL = "SELECT * FROM tracks"; // statemnt used to select all fields from tracks table

        try (Connection conn = ConnectionFactory.getConnection(); //  get connection to database using hte ConnectionFactory class,auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) { // returned connection from ConnectionFactory and prepare to excute.

            ResultSet resultSet = prep.executeQuery(); // returned connection from ConnectionFactory and prepare to excute.

            // now rows
            while (resultSet.next()) { //  get the values from the resultset and populate the list.
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

    /*
     add track function used to insert values to the databse , if successful,outputs a messafe to the user stating the insertion is successful.
     */
    public synchronized void addTrack() {

        selectSQL = "INSERT INTO tracks(TrackId, Name, AlbumId, MediaTypeId, GenreId, Composer, Milliseconds, Bytes, UnitPrice) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            prep.setString(1, Integer.toString(parcelRead.getTrack().getTrackId()));  // getting values from track objects, and  setting them to the sqlite statment to be executed
            prep.setString(2, parcelRead.getTrack().getName());
            prep.setString(3, Integer.toString(parcelRead.getTrack().getAlbumId()));
            prep.setString(4, Integer.toString(parcelRead.getTrack().getMediaTypeId()));
            prep.setString(5, Integer.toString(parcelRead.getTrack().getGenreId()));
            prep.setString(6, parcelRead.getTrack().getComposer());
            prep.setString(7, Integer.toString(parcelRead.getTrack().getMilliseconds()));
            prep.setString(8, Integer.toString(parcelRead.getTrack().getBytes()));
            prep.setString(9, Double.toString(parcelRead.getTrack().getUnitPrice()));

            prep.executeUpdate(); // executing the update
            JOptionPane.showMessageDialog(null, "Inserted successfully to Data Base"); // message to  be sent to user
            System.out.print("Inserted succesfuly to Track Table");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     delete track function is used to delet tracks. checks if delete going to occur using trsck name ir id by
     checking the pre set string before the function call in the run mehtod.
    
     After connecting to database, makes sure the track name or id is valid by checking the return value from the datbase, if 0 tehn id or name not avilable, else
     deletion is successful.
    
     */
    public synchronized void deleteTrack() {

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            if ("Track ID".equals(deleteWhat)) { // delete using track id
                prep.setString(1, Integer.toString(parcelRead.getTrack().getTrackId())); // set the value of the trackid 
            } else if ("Track Name".equals(deleteWhat)) { // delete using track name
                prep.setString(1, (parcelRead.getTrack().getName()));; // set the value of the track name 
            }
            int result = prep.executeUpdate(); //executing updtate and storing return value to the in

            if (result == 0) { // check returnnvalue to check if track is available
                System.out.print("Track Not Available");
                JOptionPane.showMessageDialog(null, "Track Not Available", "Track Not Available", JOptionPane.ERROR_MESSAGE);

            } else {
                System.out.print("Deleted succesfuly");
                JOptionPane.showMessageDialog(null, "Deleted succesfully");

            }

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*edit track usde to edit tracks by insreting the values directly o the sqlite statemnt and excuting it, check if return cvalue is 0 indicating the operation was not succesful
     else edit is confirmed to user by confirmation message
    
     */

    public synchronized void editTrack() {
        // sqlite statment with inserted values from track object getters
        selectSQL = "UPDATE tracks SET TrackId='" + parcelRead.getTrack().getTrackId() + "' ,Name='" + parcelRead.getTrack().getName() + "', AlbumId='" + parcelRead.getTrack().getAlbumId() + "', MediaTypeId='" + parcelRead.getTrack().getMediaTypeId() + "', GenreId='" + parcelRead.getTrack().getGenreId() + "' ,Composer='" + parcelRead.getTrack().getComposer() + "',Milliseconds='" + parcelRead.getTrack().getMilliseconds() + "', Bytes='" + parcelRead.getTrack().getBytes() + "', UnitPrice='" + parcelRead.getTrack().getUnitPrice() + "' WHERE TrackId='" + parcelRead.getTrack().getTrackId() + "'";

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {

            int result = prep.executeUpdate(); // execute update and store return value in int
            if (result == 0) {
                System.out.print("Track Not Available");
                JOptionPane.showMessageDialog(null, "Track Not Available", "Track Not Available", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.print("Updated succesfuly");
                JOptionPane.showMessageDialog(null, "Updated  succesfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     view genres function handels viewing genres to the user. 
     it connects to the database and performs the sqlite staments and populates the arraylist 
     */
    public synchronized void viewGenres(ArrayList populateList) {

        selectSQL = "SELECT * FROM genres";  // statemnt used to select all fields from genres table

        try (Connection conn = ConnectionFactory.getConnection(); //get connection to database using hte ConnectionFactory class,auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) { // returned connection from ConnectionFactory and prepare to excute.

            ResultSet resultSet = prep.executeQuery();// returned connection from ConnectionFactory and prepare to excute.

            // now rows
            while (resultSet.next()) { //  get the values from the resultset and populate the array list.
                Genre genre = new Genre(
                        resultSet.getInt(1),
                        resultSet.getString(2));
                populateList.add(genre);

            }

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     add genre function used to insert values to the databse , if successful,outputs a message to the user stating the insertion is successful.
     */
    public synchronized void addGenre() {

        selectSQL = "INSERT INTO genres(GenreId, Name) VALUES(?,?)"; // sqlite statment to be executed

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            prep.setString(1, Integer.toString(parcelRead.getGenre().getGenreId())); //setting the sqlite values  by getting the values from genre object
            prep.setString(2, parcelRead.getGenre().getName());

            prep.executeUpdate(); //executing the update
            JOptionPane.showMessageDialog(null, "Inserted successfully to Data Base");
            System.out.print("Inserted succesfuly to Genre Table");

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*edit genre usde to edit genres by insreting the values directly o the sqlite statemnt and excuting it, check if return value is 0 indicating the operation was not succesful
     else edit is confirmed to user by confirmation message
    
     */
    public synchronized void editGenre() {
        selectSQL = "UPDATE genres SET GenreId='" + parcelRead.getGenre().getGenreId() + "' ,Name='" + parcelRead.getGenre().getName() + "'WHERE GenreId='" + parcelRead.getGenre().getGenreId() + "'";

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {

            int result = prep.executeUpdate(); // executing the update

            if (result == 0) { // if operatoin not successful (indic`ting  unavailable genre)
                JOptionPane.showMessageDialog(null, "Genre not available", "Genre unavilable", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.print("Updated succesfuly");
                JOptionPane.showMessageDialog(null, "Updated  succesfully");

            }

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     delete genre function is used to delet genres. checks if delete going to occur using genre name or id by
     checking the pre set string before the function call in the run mehtod.
    
     After connecting to database, makes sure thegenre name or id is valid by checking the return value from the datbase, if 0 then id or name not avilable, else
     deletion is successful.
    
     */
    public synchronized void deleteGenre() {

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            if ("Genre ID".equals(deleteWhat)) { //delete using genre id
                prep.setString(1, Integer.toString(parcelRead.getGenre().getGenreId())); //set the sqlite statment value to genre id
            } else if ("Genre Name".equals(deleteWhat)) {// delete using genre name
                prep.setString(1, (parcelRead.getGenre().getName()));;//set the sqlite statment value to genre name
            }
            int result = prep.executeUpdate(); // exdecute update and store return result to integer

            if (result == 0) { // check if genre is available
                JOptionPane.showMessageDialog(null, "Genre not available", "Genre unavilable", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.print("Deleted succesfuly");
                JOptionPane.showMessageDialog(null, "Deleted succesfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     getTrackByGenre function allows users to search for a track by filtering using genre. This happens by obtaiinig thr genre idmfrom the genre table that
     corresponds to the genre name inputed bybuser, and then getting all tracks that correspond to that genre id
     */
    public synchronized void getTracksByGenre(ArrayList populateList) {

        selectSQL = "SELECT GenreId FROM genres WHERE Name=?"; // swlitw statmen to get genre id

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            prep.setString(1, parcelRead.getTrack().getGenreSearchName()); //setting the value to the value inouted by user in the genre object
            ResultSet resultSet = prep.executeQuery(); // execute querry
            if (resultSet.next()) {
                genreId = resultSet.getInt(1); //store result in integer
            } else {
                JOptionPane.showMessageDialog(null, "Genre un available", "Genre unavilable", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
        selectSQL = "SELECT * FROM tracks WHERE GenreId=?"; // sqlite statemnt to get tracks with specific genre id
        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {
            prep.setString(1, Integer.toString(genreId)); // setting the sqlite value with the obtained genre id from previous executed querry
            ResultSet resultSet = prep.executeQuery(); //execute query and store return value in result set

            // now rows
            while (resultSet.next()) { // populate track list
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

    /*
     random track genereator allows user to get a random track . This happens by gettig a random number and using the number as a track id reference to get the track from the 
     tracks table
     */
    public synchronized void randomTrackGenerator(ArrayList populateList) {

        selectSQL = "SELECT * FROM tracks WHERE TrackId=?"; // sqlite statemnt to get track based on track id

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {

            prep.setString(1, Integer.toString(parcelRead.getTrack().getRandomNumber())); // setting sqlite value by getting the random number from track object

            ResultSet resultSet = prep.executeQuery();
            //  populate track list
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
    /*
    
     setUpperBound finction is used to determine the maximjum number the random numbe generator can generate. This occurs by getting  the last trackid field indicating the highest
     track id number. Thiis carried out to ensure no number higher than the highest track id is generated*/

    public synchronized void setUpperBound() {

        selectSQL = "SELECT TrackId FROM tracks ORDER BY TrackId DESC LIMIT 1 "; // sqlite statemen to get track id ordered by descending order and limiting the result to one

        try (Connection conn = ConnectionFactory.getConnection(); // auto close the connection object after try
                PreparedStatement prep = conn.prepareStatement(selectSQL);) {

            ResultSet resultSet = prep.executeQuery(); // excuting querry

            if (resultSet.next()) {
                int upperBound = resultSet.getInt(1); //stroe result in integer
                System.out.println("Git the id baby :" + upperBound);
                try {
                    objectOutputStream.writeObject(new Track(upperBound, true));// writting back the integer to the GUI class
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandlerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "UpperBound issue", "issue in setupperbound function", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteChinookCw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
