package sqlitechinookcw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 *
 * The purpose of this class is to encapsulate the connecting to the database.
 *
 *
 */
public class ConnectionFactory {

    public static final String DB_URL = "jdbc:sqlite:chinook/chinook.db";
    


    /**
     * Get a connection to our SQLite database.
     *
     * @return Connection object, remember to close this connection object after
     * using to avoid memory leaks, connection objects are expensive.
     * @throws java.sql.SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        System.out.println("Connection to SQLite has been established.");
        return conn;
    }

}
