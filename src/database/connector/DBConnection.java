package database.connector;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;
import util.definitions.AppConstants;

/**
 *
 * @author Shehan
 */
public class DBConnection {

    private static final Logger logger = Logger.getLogger(DBConnection.class);

    private static DBConnection dbConnetion;
    private Connection connection;

    private DBConnection() throws SQLException {
        DriverManager.registerDriver(new Driver());
        try {
            Properties connectionProps = new Properties();
            connectionProps.put("user", AppConstants.MYSQL_USER_NAME);
            connectionProps.put("password", AppConstants.MYSQL_PASSWORD);
            logger.debug("MYSQL connection = jdbc:mysql://" + AppConstants.SERVER + ":" + AppConstants.PORT + "/" + AppConstants.DATABASE + "," + connectionProps.getProperty("user") + "," + connectionProps.getProperty("password"));

            connection = DriverManager.getConnection("jdbc:mysql://" + AppConstants.SERVER + ":" + AppConstants.PORT + "/" + AppConstants.DATABASE, connectionProps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static DBConnection getDBConnection() throws SQLException {
        if (dbConnetion == null) {
            synchronized (DBConnection.class) {
                dbConnetion = new DBConnection();
            }
        }
        return dbConnetion;
    }

    private Connection getConnection() {
        return connection;
    }

    private void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            logger.debug("connection closed");
        }
    }

    /**
     *
     * @return connection to DBMS
     * @throws SQLException
     */
    public static Connection getConnectionToDB() throws SQLException {
        return getDBConnection().getConnection();
    }

    public static void closeConnectionToDB() throws SQLException {
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
