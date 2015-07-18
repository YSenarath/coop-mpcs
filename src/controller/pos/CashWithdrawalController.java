package controller.pos;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.pos.CashWithdrawal;

public class CashWithdrawalController implements DatabaseInterface {

    public static CashWithdrawal getLastWithdrawalId() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT cash_withdrawal_id FROM " + CASH_WITHDRAWAL + " ORDER BY cash_withdrawal_id DESC LIMIT 1";

        ResultSet resultSet = DBHandler.getData(connection, query);

        if (resultSet.next()) {
            return new CashWithdrawal(resultSet.getInt("cash_withdrawal_id"));
        }
        return null;
    }

    public static boolean addCashWithdrawal(CashWithdrawal cashWithdrawal) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + CASH_WITHDRAWAL + " (cash_withdrawal_id,user_name,counter_id,withdrawal_time,withdrawal_date,amount) VALUES (?,?,?,?,?,?) ";
        Object[] ob = {
            cashWithdrawal.getWithdrawalId(),
            cashWithdrawal.getUsername(),
            cashWithdrawal.getCounterId(),
            cashWithdrawal.getTime(),
            cashWithdrawal.getDate(),
            cashWithdrawal.getAmount()
        };
        return DBHandler.setData(connection, query, ob) == 1;

    }
}
