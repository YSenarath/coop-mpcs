/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testSuite.dbTest;

import database.connector.DBConnection;
import java.sql.Connection;
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

        String server = "localhost";
        String port = "3306";
        String database = "ClinicManagementSystem";
        connectionProps.put("user", "root");
        connectionProps.put("password", "msw15931");

        try {
            Connection coonection = DBConnection.getConnectionToDB(server, port, database, connectionProps);
            logger.info(" Connected to Database ");
            DBConnection.closeConnectionToDB();
            logger.info(" Disconnected from Database ");
        } catch (SQLException ex) {
            logger.error("DB connection error -", ex);
        }
    }
}
