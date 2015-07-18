/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pos;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.pos.Invoice;
import database.connector.DatabaseInterface;
import java.util.ArrayList;

/**
 *
 * @author Shehan
 */
public class InvoiceController implements DatabaseInterface {

    public static boolean addInvoice(Invoice invoice) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + INVOICE + " (bill_id,user_name,counter_id,bill_time,bill_date,amount) VALUES (?,?,?,?,?,?) ";

        Object[] ob = {
            invoice.getInvoiceNo(),
            invoice.getUserName(),
            invoice.getCounterId(),
            invoice.getTime(),
            invoice.getDate(),
            invoice.getNetTotal()
        };
        return DBHandler.setData(connection, query, ob) > 0;
    }

    public static ArrayList<Invoice> getAllInvoiceIds() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT bill_id FROM " + INVOICE;

        ResultSet resultSet = DBHandler.getData(connection, query);
        ArrayList<Invoice> invoiceIDs = new ArrayList();
        while (resultSet.next()) {
            Invoice invoice = new Invoice(
                    resultSet.getInt("bill_id")
            );
            invoiceIDs.add(invoice);
        }

        return invoiceIDs;
    }

    public static Invoice getInvoice(int invoiceId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + INVOICE + " WHERE bill_id=?";
        Object[] ob = {
            invoiceId,};
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {

            return new Invoice(
                    resultSet.getInt("bill_id"),
                    resultSet.getString("user_name"),
                    resultSet.getInt("counter_id"),
                    resultSet.getString("bill_date"),
                    resultSet.getString("bill_time"),
                    resultSet.getDouble("amount")
            );
        }
        return null;
    }

    public static Invoice getLastInvoiceId() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT bill_id FROM " + INVOICE + " ORDER BY bill_id DESC LIMIT 1";

        ResultSet resultSet = DBHandler.getData(connection, query);

        if (resultSet.next()) {

            return new Invoice(resultSet.getInt("bill_id"));
        }
        return null;
    }

}
