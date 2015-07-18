/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pos;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import static database.connector.DatabaseInterface.COUNTER;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.pos.Counter;

/**
 *
 * @author Shehan
 */
public class CounterController implements DatabaseInterface {

    public static boolean updateCounterAmount(int counterId, double amountAddedToCounter) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE " + COUNTER + " SET current_amount=current_amount+? WHERE counter_id=?";
        Object[] ob = {
            amountAddedToCounter,
            counterId
        };
        return DBHandler.setData(connection, query, ob) > 0;
    }

    public static boolean setInitialAmount(int counterId, double initialAmount) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE " + COUNTER + " SET current_amount=? WHERE counter_id=?";
        Object[] ob = {
            initialAmount,
            counterId
        };
        return DBHandler.setData(connection, query, ob) > 0;
    }
    public static Counter getCounter(int counterId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + COUNTER + " WHERE counter_id=?";
        Object[] ob = {
            counterId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new Counter(
                    resultSet.getInt("counter_id"),
                    resultSet.getDouble("current_amount")
            );
        }
        return null;
    }

}
