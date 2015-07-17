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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.pos.CardPayment;
import model.pos.CashPayment;
import model.pos.CoopCreditPayment;
import model.pos.CustomerVoucherPayment;
import model.pos.EmployeeVoucherPayment;
import model.pos.InvoiceItem;
import model.pos.Payment;
import model.pos.PoshanaPayment;

/**
 *
 * @author Shehan
 */
public class InvoiceController implements DatabaseInterface {

    public static Invoice getLastInvoiceId() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT bill_id FROM " + INVOICE + " ORDER BY bill_id DESC LIMIT 1";

        ResultSet resultSet = DBHandler.getData(connection, query);

        if (resultSet.next()) {

            return new Invoice(resultSet.getInt("bill_id"));
        }
        return null;
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

    public static boolean performTransaction(Invoice invoice) {
        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);

            String query = "INSERT INTO " + INVOICE + " (bill_id,user_name,counter_id,bill_time,bill_date,amount) VALUES (?,?,?,?,?,?) ";

            Object[] invoiceObj = {
                invoice.getInvoiceNo(),
                invoice.getUserName(),
                invoice.getCounterId(),
                invoice.getTime(),
                invoice.getDate(),
                invoice.getNetTotal()
            };
            DBHandler.setData(connection, query, invoiceObj);

            for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                query = "INSERT INTO " + INVOICE_ITEMS + " (bill_id,batch_id,product_id,discount,price,qty) VALUES (?,?,?,?,?,?) ";
                Object[] invoiceItemObj = {
                    invoice.getInvoiceNo(),
                    invoiceItem.getBatchId(),
                    invoiceItem.getProductId(),
                    invoiceItem.getDiscount(),
                    invoiceItem.getPrice(),
                    invoiceItem.getQty()
                };
                DBHandler.setData(connection, query, invoiceItemObj);

                query = "UPDATE " + BATCH + " SET qty=qty-? WHERE batch_id=? AND product_id=? ";
                Object[] batchObj = {
                    invoiceItem.getQty(),
                    invoiceItem.getBatchId(),
                    invoiceItem.getProductId()
                };
                DBHandler.setData(connection, query, batchObj);
            }

            for (Payment payment : invoice.getPayments()) {
                if (payment instanceof CashPayment) {
                    CashPayment cashPayment = (CashPayment) payment;
                    query = "INSERT INTO " + CASH_PAYMENT + " (bill_id,cash_payment_id,amount,change_amount) VALUES (?,?,?,?) ";
                    Object[] cashPaymentObj = {
                        cashPayment.getBillId(),
                        cashPayment.getPaymentId(),
                        cashPayment.getAmount(),
                        cashPayment.getChangeAmount()
                    };
                    DBHandler.setData(connection, query, cashPaymentObj);

                    query = "UPDATE " + COUNTER + " SET current_amount=current_amount+? WHERE counter_id=?";
                    Object[] counterObj = {
                        cashPayment.getAmount() - cashPayment.getChangeAmount(),
                        invoice.getCounterId()
                    };
                    DBHandler.setData(connection, query, counterObj);

                } else if (payment instanceof CardPayment) {
                    CardPayment cardPayment = (CardPayment) payment;
                    query = "INSERT INTO " + CARD_PAYMENT + " (bill_id,card_payment_id,card_type,card_no,amount) VALUES (?,?,?,?,?) ";
                    Object[] cardPaymentObj = {
                        cardPayment.getBillId(),
                        cardPayment.getPaymentId(),
                        cardPayment.getCardType(),
                        cardPayment.getCardNo(),
                        cardPayment.getAmount(),};
                    DBHandler.setData(connection, query, cardPaymentObj);
                } else if (payment instanceof CoopCreditPayment) {
                    throw new Exception("Not implemented");
                } else if (payment instanceof PoshanaPayment) {
                    throw new Exception("Not implemented");
                } else if (payment instanceof CustomerVoucherPayment) {
                    throw new Exception("Not implemented");
                } else if (payment instanceof EmployeeVoucherPayment) {
                    throw new Exception("Not implemented");
                }
            }

            connection.commit();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex1) {
                    Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

    }

}
