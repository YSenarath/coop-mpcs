package controller.pos;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.SQLException;
import model.pos.CounterLogin;
import database.connector.DatabaseInterface;
import java.sql.ResultSet;

public class CounterLoginController implements DatabaseInterface {

    public static boolean addCounterLogin(CounterLogin counterLogin) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + COUNTER_LOGIN + " (user_name,counter_id,log_in_time,log_in_date,initial_amount) VALUES(?,?,?,?,?)";
        Object[] ob = {
            counterLogin.getUserName(),
            counterLogin.getCounterId(),
            counterLogin.getLogInTime(),
            counterLogin.getLogInDate(),
            counterLogin.getInitialAmount()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static boolean endShift(CounterLogin counterLogin) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE " + COUNTER_LOGIN + " SET log_off_time=?,log_off_date=?,cash_withdrawals=?,log_off_amount_expected=?,log_off_amount_actual=?,shift_ended=true WHERE shift_id=? AND counter_id=? ";
        Object[] ob = {
            counterLogin.getLogOffTime(),
            counterLogin.getLogOffDate(),
            counterLogin.getCashWithdrawals(),
            counterLogin.getLogOffExpected(),
            counterLogin.getLogOffActual(),
            counterLogin.getShiftId(),
            counterLogin.getCounterId()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static CounterLogin getCounterLogin(int shiftId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + COUNTER_LOGIN + " WHERE shift_id=? ";
        Object[] ob = {
            shiftId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        if (resultSet.next()) {
            return new CounterLogin(
                    resultSet.getInt("shift_id"),
                    resultSet.getString("user_name"),
                    resultSet.getInt("counter_id"),
                    resultSet.getString("log_in_time"),
                    resultSet.getString("log_in_date"),
                    resultSet.getString("log_off_time"),
                    resultSet.getString("log_off_date"),
                    resultSet.getDouble("initial_amount"),
                    resultSet.getDouble("cash_withdrawals"),
                    resultSet.getDouble("log_off_amount_expected"),
                    resultSet.getDouble("log_off_amount_actual"),
                    resultSet.getBoolean("shift_ended")
            );
        }
        return null;
    }

    public static CounterLogin getLastCounterLogin(int counter) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + COUNTER_LOGIN + " WHERE counter_id=? ORDER BY shift_id DESC LIMIT 1 ";
        Object[] ob = {
            counter
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        if (resultSet.next()) {
            return new CounterLogin(
                    resultSet.getInt("shift_id"),
                    resultSet.getString("user_name"),
                    resultSet.getInt("counter_id"),
                    resultSet.getString("log_in_time"),
                    resultSet.getString("log_in_date"),
                    resultSet.getString("log_off_time"),
                    resultSet.getString("log_off_date"),
                    resultSet.getDouble("initial_amount"),
                    resultSet.getDouble("cash_withdrawals"),
                    resultSet.getDouble("log_off_amount_expected"),
                    resultSet.getDouble("log_off_amount_actual"),
                    resultSet.getBoolean("shift_ended")
            );
        }
        return null;
    }

}
