package sqlitechinookcw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The purpose of this class is to encapsulate the connecting to the database.
 *
 * This seems rather simple here as we are just getting a single connection and
 * then returning it, however this implementation could in theory be much more
 * complicated e.g. by implementation of connection pooling (connection objects
 * are expensive).
 *
 * @author Chris Bass
 */
public class ConnectionFactory {
    public static final String DB_URL = "jdbc:sqlite:chinook/chinook.db";
    public static final String DB_USERNAME = "";
    public static final String DB_PASSWORD = "";

    /**
     * Get a connection to our SQLite database.
     *
     * @return Connection object, remember to close this connection object after
     * using to avoid memory leaks, connection objects are expensive.
     * @throws java.sql.SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        System.out.println("Connection to SQLite has been established.");
        return conn;
    }

}
