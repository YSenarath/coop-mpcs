package controller.pos;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.pos.CashWithdrawal;

public class CashWithdrawalController implements DatabaseInterface {

    public static int getLastWithdrawalId() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT cash_withdrawal_id FROM " + CASH_WITHDRAWAL + " ORDER BY cash_withdrawal_id DESC LIMIT 1";

        ResultSet resultSet = DBHandler.getData(connection, query);

        if (resultSet.next()) {
            return resultSet.getInt("cash_withdrawal_id");
        }
        return -1;
    }

    public static boolean addCashWithdrawal(CashWithdrawal cashWithdrawal) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + CASH_WITHDRAWAL + " (cash_withdrawal_id,shift_id,withdrawal_time,withdrawal_date,amount) VALUES (?,?,?,?,?) ";
        Object[] ob = {
            cashWithdrawal.getWithdrawalId(),
            cashWithdrawal.getShiftId(),
            cashWithdrawal.getTime(),
            cashWithdrawal.getDate(),
            cashWithdrawal.getAmount()
        };
        return DBHandler.setData(connection, query, ob) == 1;

    }

    public static ArrayList<CashWithdrawal> getCashWithdrawals(int shiftId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CASH_WITHDRAWAL + " WHERE shift_id=?";
        Object[] ob = {
            shiftId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        ArrayList<CashWithdrawal> cashWithdrawals = new ArrayList();
        while (resultSet.next()) {
            CashWithdrawal cashWithdrawal = new CashWithdrawal(
                    resultSet.getInt("cash_withdrawal_id"),
                    resultSet.getInt("shift_id"),
                    resultSet.getString("withdrawal_time"),
                    resultSet.getString("withdrawal_date"),
                    resultSet.getDouble("amount")
            );
            cashWithdrawals.add(cashWithdrawal);

        }
        return cashWithdrawals;
    }
}
