/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pos;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.pos.CashPayment;

/**
 *
 * @author Shehan
 */
public class CashPaymentController implements DatabaseInterface {

    public static CashPayment getCashpayment(int invoiceNo) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CASH_PAYMENT + " WHERE bill_id=? ";
        Object[] ob = {
            invoiceNo
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return new CashPayment(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("cash_payment_id"),
                    resultSet.getDouble("amount"),
                    resultSet.getDouble("change_amount")
            );
        }
        return null;
    }
}
