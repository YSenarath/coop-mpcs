/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testSuite.dbTest;

import database.connector.DBConnection;
import util.definitions.AppConstants;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author Shehan
 */
public class DBTest {

    private static Logger logger = Logger.getLogger(DBTest.class);

    public static void main(String[] args) {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "admin");
        connectionProps.put("password", "admin");

        try {
            DBConnection.getConnectionToDB(AppConstants.SERVER, AppConstants.PORT, AppConstants.DATABASE, connectionProps);
            logger.info(" Connected to Database ");
            DBConnection.closeConnectionToDB();
            logger.info(" Disconnected from Database ");
        } catch (SQLException ex) {
            logger.error("DB connection error -", ex);
        }
    }
}
