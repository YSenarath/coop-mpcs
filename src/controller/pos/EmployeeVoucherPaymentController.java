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
import java.util.ArrayList;
import model.pos.payment.EmployeeVoucherPayment;

/**
 *
 * @author HP
 */
public class EmployeeVoucherPaymentController implements DatabaseInterface {

    public static boolean addEmployeeVoucherPayment(EmployeeVoucherPayment employeeVoucherPayment) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + EMPLOYEE_VOUCHER_PAYMENT + " (bill_id,voucher_payment_id,employee_id,amount) VALUES (?,?,?,?)";
        Object[] ob = {
            employeeVoucherPayment.getInvoiceId(),
            employeeVoucherPayment.getPaymentId(),
            employeeVoucherPayment.getEmployeeId(),
            employeeVoucherPayment.getAmount()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static EmployeeVoucherPayment getEmployeeVoucherPayment(int invoiceNo) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + EMPLOYEE_VOUCHER_PAYMENT + " WHERE bill_id=? ";
        Object[] ob = {
            invoiceNo
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        if (resultSet.next()) {
            return new EmployeeVoucherPayment(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("voucher_payment_id"),
                    resultSet.getInt("employee_id"),
                    resultSet.getDouble("amount")
            );
        }
        return null;
    }

    public static ArrayList<EmployeeVoucherPayment> loadPayments() throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT *  FROM " + EMPLOYEE_VOUCHER_PAYMENT;

        ResultSet resultSet = DBHandler.getData(connection, query);
        ArrayList<EmployeeVoucherPayment> voucherPayments = new ArrayList();
        while (resultSet.next()) {
            EmployeeVoucherPayment voucherPayment = new EmployeeVoucherPayment(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("voucher_payment_id"),
                    resultSet.getInt("employee_id"),
                    resultSet.getDouble("amount")
            );
            voucherPayments.add(voucherPayment);
        }

        return voucherPayments;
    }

}
