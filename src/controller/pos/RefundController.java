package controller.pos;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.connector.DatabaseInterface;
import java.util.ArrayList;
import model.pos.item.Refund;

public class RefundController implements DatabaseInterface {

    public static boolean addRefund(Refund refund) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + REFUND + " (refund_id,bill_id,shift_id,refund_time,refund_date,amount) VALUES (?,?,?,?,?,?)";

        Object[] ob = {
            refund.getRefundId(),
            refund.getInvoiceId(),
            refund.getShiftId(),
            refund.getRefundTime(),
            refund.getRefundDate(),
            refund.getAmount()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static int getLastRefundId() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT refund_id FROM " + REFUND + " ORDER BY refund_id DESC LIMIT 1";

        ResultSet resultSet = DBHandler.getData(connection, query);

        if (resultSet.next()) {
            return resultSet.getInt("refund_id");
        }
        return -1;
    }

    public static Refund getRefund(int invoiceId, int refundId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + REFUND + " WHERE bill_id=? AND refund_id=?";
        Object[] ob = {
            invoiceId,
            refundId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {

            return new Refund(
                    resultSet.getInt("refund_id"),
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("shift_id"),
                    resultSet.getString("refund_time"),
                    resultSet.getString("refund_date"),
                    resultSet.getDouble("amount")
            );
        }
        return null;
    }

    public static ArrayList<Refund> getRefunds(int invoiceId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + REFUND + " WHERE bill_id=?";
        Object[] ob = {
            invoiceId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        ArrayList<Refund> refunds = new ArrayList();
        while (resultSet.next()) {

            Refund refund = new Refund(
                    resultSet.getInt("refund_id"),
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("shift_id"),
                    resultSet.getString("refund_time"),
                    resultSet.getString("refund_date"),
                    resultSet.getDouble("amount")
            );
            refunds.add(refund);
        }
        return refunds;
    }

    public static ArrayList<Refund> getRefundsFromShiftId(int shiftId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + REFUND + " WHERE shift_id=?";
        Object[] ob = {
            shiftId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        ArrayList<Refund> refunds = new ArrayList();
        while (resultSet.next()) {

            Refund refund = new Refund(
                    resultSet.getInt("refund_id"),
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("shift_id"),
                    resultSet.getString("refund_time"),
                    resultSet.getString("refund_date"),
                    resultSet.getDouble("amount")
            );
            refunds.add(refund);
        }
        return refunds;
    }
}
