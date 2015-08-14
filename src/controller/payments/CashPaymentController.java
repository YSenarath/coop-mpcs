package controller.payments;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.pos.payment.CashPayment;

public class CashPaymentController implements DatabaseInterface {

    public static boolean addCashPayment(CashPayment cashPayment) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + CASH_PAYMENT + " (bill_id,cash_payment_id,amount,change_amount) VALUES (?,?,?,?)";
        Object[] ob = {
            cashPayment.getInvoiceId(),
            cashPayment.getPaymentId(),
            cashPayment.getAmount(),
            cashPayment.getChangeAmount()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static CashPayment getCashpayment(int invoiceNo) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CASH_PAYMENT + " WHERE bill_id=?";
        Object[] ob = {
            invoiceNo
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
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
