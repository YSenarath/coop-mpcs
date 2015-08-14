package database.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DBHandler {

    //Do not instantiate this class
    private DBHandler() {
    }

    public static int setData(Connection connection, String query, Object[] ob) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < ob.length; i++) {
            preparedStatement.setObject(i + 1, ob[i]);
        }
        int result = preparedStatement.executeUpdate();
        return result;
    }

    public static int setData(Connection connection, String query) throws SQLException {
        PreparedStatement preparedStatement
                = connection.prepareStatement(query);
        int result = preparedStatement.executeUpdate();
        return result;
    }

    public static ResultSet getData(Connection connection, String query, Object[] ob) throws SQLException {
        PreparedStatement preparedStatement
                = connection.prepareStatement(query);
        for (int i = 0; i < ob.length; i++) {
            preparedStatement.setObject(i + 1, ob[i]);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static ResultSet getData(Connection connection, String query) throws SQLException {
        PreparedStatement preparedStatement
                = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
