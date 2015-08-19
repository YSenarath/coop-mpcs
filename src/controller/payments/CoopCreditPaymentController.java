/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.payments;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.pos.payment.CoopCreditPayment;

/**
 *
 * @author HP
 */
public class CoopCreditPaymentController implements DatabaseInterface {

    public static CoopCreditPayment getCoopCreditPayment(int invoiceNo) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + COOP_CREDIT_PAYMENT + " WHERE bill_id=? ";
        Object[] ob = {
            invoiceNo
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new CoopCreditPayment(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("coop_credit_payment_id"),
                    resultSet.getInt("customer_id"),
                    resultSet.getDouble("amount"),
                    resultSet.getDouble("amount_settled")
            );
        }
        return null;
    }
   
    public static boolean deleteDeatils(int id) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "DELETE FROM " + COOP_CREDIT_PAYMENT + " WHERE bill_id=?";

        Object[] ob = {
            id
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }
    
    public static boolean addCoopPayment(CoopCreditPayment coopPayment) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + COOP_CREDIT_PAYMENT + " (bill_id,coop_credit_payment_id,customer_id,amount) VALUES (?,?,?,?)";
        Object[] ob = {
            coopPayment.getInvoiceId(),
            coopPayment.getPaymentId(),
            coopPayment.getCustomerId(),
            coopPayment.getAmount()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static ResultSet loadCoopDetails() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT bill_id,amount,amount_settled FROM " + COOP_CREDIT_PAYMENT;

        ResultSet resultSet = DBHandler.getData(connection, query);
        return resultSet;
    }

    //Add method to settle payment
    public static CoopCreditPayment getCoopCreditPaymentDetails(int customerId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + COOP_CREDIT_PAYMENT + " WHERE customer_id=? ";
        Object[] ob = {
            customerId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new CoopCreditPayment(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("coop_credit_payment_id"),
                    resultSet.getInt("customer_id"),
                    resultSet.getDouble("amount"),
                    resultSet.getDouble("amount_settled")
            );
        }
        return null;
    }

    public static ArrayList<CoopCreditPayment> loadDetails(int customerId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT *  FROM " + COOP_CREDIT_PAYMENT + " WHERE customer_id = ? ";

        Object[] obj = {
            customerId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, obj);
        ArrayList<CoopCreditPayment> coopPayments = new ArrayList();
        while (resultSet.next()) {
            CoopCreditPayment coopPayment = new CoopCreditPayment(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("coop_credit_payment_id"),
                    resultSet.getInt("customer_id"),
                    resultSet.getDouble("amount"),
                    resultSet.getDouble("amount_settled")
            );
            coopPayments.add(coopPayment);
        }

        return coopPayments;
    }

}
