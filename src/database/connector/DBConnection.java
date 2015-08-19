package database.connector;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;
import util.Utilities;

public final class DBConnection implements DatabaseInterface {

    private static final Logger logger = Logger.getLogger(DBConnection.class);

    private static DBConnection dbConnetion;
    private final Connection connection;

    private DBConnection() throws SQLException {
        DriverManager.registerDriver(new Driver());

        //Default login
        String serverIp = SERVER;
        String dbUserName = MYSQL_USER_NAME;
        String dbPassword = MYSQL_PASSWORD;

        //From settings
        String serverProperty = Utilities.loadProperty("SERVER_IP");
        String userNameProperty = Utilities.loadProperty("userName");
        String passwordProperty = Utilities.loadProperty("password");

        if (!serverProperty.equals("NULL")) {
            logger.info("Server IP from settings");
            serverIp = serverProperty;
        }
        if (!userNameProperty.equals("NULL")) {
            logger.debug("Username from settings");
            dbUserName = userNameProperty;
        } else {
            logger.warn("Username from DatabaseInterface");
        }
        if (!passwordProperty.equals("NULL")) {
            logger.debug("Password from settings");
            dbPassword = passwordProperty;
        } else {
            logger.warn("Username from DatabaseInterface");
        }

        Properties connectionProps = new Properties();
        connectionProps.put("user", dbUserName);
        connectionProps.put("password", dbPassword);

        logger.info("MYSQL connection = jdbc:mysql://" + serverIp + ":" + PORT + "/" + DATABASE + "," + connectionProps.getProperty("user") + "," + connectionProps.getProperty("password"));
        connection = DriverManager.getConnection("jdbc:mysql://" + serverIp + ":" + PORT + "/" + DATABASE, connectionProps);
    }

    private static DBConnection getDBConnection() throws SQLException {
        if (dbConnetion == null) {
            synchronized (DBConnection.class) {
                if (dbConnetion == null) {
                    dbConnetion = new DBConnection();
                }
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

    public static boolean isValidDBConnection(String userNameProperty, String passwordProperty, String serverIP) {
        try {
            DriverManager.registerDriver(new Driver());

            Properties connectionProps = new Properties();
            connectionProps.put("user", userNameProperty);
            connectionProps.put("password", passwordProperty);

            Connection tmpConn = DriverManager.getConnection("jdbc:mysql://" + serverIP + ":" + PORT + "/" + DATABASE, connectionProps);
            if (tmpConn != null) {
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
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
