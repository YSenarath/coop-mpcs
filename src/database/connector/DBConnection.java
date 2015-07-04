package database.connector;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author Shehan
 */
public class DBConnection {

    private static Logger logger = Logger.getLogger(DBConnection.class);
    
    private static DBConnection dbConnetion;
    private Connection connection;

    private DBConnection(String server, String port, String database, Properties connectionProps) throws SQLException {
        DriverManager.registerDriver(new Driver());
        try {
            logger.debug("MYSQL string = jdbc:mysql://" + server + ":" + port + "/" + database + "," + connectionProps.getProperty("user") + "," + connectionProps.getProperty("password"));

            connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database, connectionProps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static DBConnection getDBConnection(String server, String port, String database, Properties connectionProps) throws SQLException {
        logger.debug("getDBConnection invoked");
        if (dbConnetion == null) {
            dbConnetion = new DBConnection(server, port, database, connectionProps);
        }
        return dbConnetion;
    }

    private Connection getConnection() {
        logger.debug("getConnection invoked");
        return connection;
    }

    private void closeConnection() throws SQLException {
          logger.debug("closeConnection invoked");
        if (connection != null) {
            connection.close();
            logger.debug("connection closed");
        }
    }

    /**
     *
     * @param server server location
     * @param port port number
     * @param database database to connect
     * @param connectionProps username and password
     * @return connection to DBMS
     * @throws SQLException
     */
    public static Connection getConnectionToDB(String server, String port, String database, Properties connectionProps) throws SQLException {
        logger.debug("getConnectionToDB invoked for : " + server + ", " + port + ", " + database + ", " + connectionProps.getProperty("user") + ", " + connectionProps.getProperty("password"));
       
        DBConnection dBConnection = getDBConnection(server, port, database, connectionProps);
        return dBConnection.getConnection();
    }

    public static void closeConnectionToDB() throws SQLException {
        logger.debug("closeConnectionToDB invoked");
        if (dbConnetion != null) {
            dbConnetion.closeConnection();
            dbConnetion = null;
            System.gc();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        logger.debug("finallize invoked");
        super.finalize();
        closeConnection();
    }

}
