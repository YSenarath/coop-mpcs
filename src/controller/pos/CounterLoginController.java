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
        String query = "INSERT INTO " + COUNTER_LOGIN + " (user_name,counter_id,login_time,login_date,initial_amount) VALUES(?,?,?,?,?)";
        Object[] ob = {
            counterLogin.getUserName(),
            counterLogin.getCounterId(),
            counterLogin.getTime(),
            counterLogin.getDate(),
            counterLogin.getInitialAmount()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static boolean endShift(int shiftId, int counter) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE " + COUNTER_LOGIN + " SET shift_ended=true WHERE shift_id=? AND counter_id=? ";
        Object[] ob = {
            shiftId,
            counter
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
                    resultSet.getString("login_time"),
                    resultSet.getString("login_date"),
                    resultSet.getDouble("initial_amount"),
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
                    resultSet.getString("login_time"),
                    resultSet.getString("login_date"),
                    resultSet.getDouble("initial_amount"),
                    resultSet.getBoolean("shift_ended")
            );
        }
        return null;
    }

}
