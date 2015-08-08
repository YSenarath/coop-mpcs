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
import model.pos.payment.PoshanaPayment;

/**
 *
 * @author HP
 */
public class PoshanaPaymentController implements DatabaseInterface {

    public static boolean addPoshanaPayment(PoshanaPayment poshanaPayment) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + COOP_CREDIT_PAYMENT + " (bill_id,poshana_payment_id,stamp_id,customer_name,amount) VALUES (?,?,?,?,?)";
        Object[] ob = {
            poshanaPayment.getInvoiceId(),
            poshanaPayment.getPaymentId(),
            poshanaPayment.getStampId(),
            poshanaPayment.getCustomerName(),
            poshanaPayment.getAmount()

        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static PoshanaPayment getPoshanaPayment(int invoiceNo) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CUSTOMER_VOUCHER_PAYMENT + " WHERE bill_id=? ";
        Object[] ob = {
            invoiceNo
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new PoshanaPayment(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("poshana_payment_id"),
                    resultSet.getString("stamp_id"),
                    resultSet.getString("customer_name"),
                    resultSet.getDouble("amount")
            );
        }
        return null;
    }

}
