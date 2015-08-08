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
import model.pos.payment.CustomerVoucherPayment;

/**
 *
 * @author HP
 */
public class CustomerVoucherPaymentController implements DatabaseInterface {

    public static boolean addCustomerVoucherPayment(CustomerVoucherPayment customerVoucherPayment) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + CUSTOMER_VOUCHER_PAYMENT + " (bill_id,voucher_payment_id,voucher_id,amount) VALUES (?,?,?,?)";
        Object[] ob = {
            customerVoucherPayment.getInvoiceId(),
            customerVoucherPayment.getPaymentId(),
            customerVoucherPayment.getVoucherId(),
            customerVoucherPayment.getAmount()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static CustomerVoucherPayment getCustomerVoucherPayment(int invoiceNo) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CUSTOMER_VOUCHER_PAYMENT + " WHERE bill_id=? ";
        Object[] ob = {
            invoiceNo
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new CustomerVoucherPayment(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("voucher_payment_id"),
                    resultSet.getString("voucher_id"),
                    resultSet.getDouble("amount")
            );
        }
        return null;
    }

}
