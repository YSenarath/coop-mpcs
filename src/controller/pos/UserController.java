package controller.pos;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.people.User;
import database.connector.DatabaseInterface;

public class UserController implements DatabaseInterface {

    public static ArrayList<User> getAllUsers() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "Select * From " + USER;
        ResultSet resultSet = DBHandler.getData(connection, query);

        ArrayList<User> allUsers = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("user_name"),
                    resultSet.getString("password"),
                    resultSet.getString("access_level"),
                    resultSet.getBoolean("isLoggedIn")
            );
            allUsers.add(user);
        }

        return allUsers;
    }

    public static User getUser(String search, String userName) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + USER + " WHERE " + search + "=? LIMIT 1";
        Object[] ob = {
            userName
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        if (resultSet.next()) {
            return new User(
                    resultSet.getString("user_name"),
                    resultSet.getString("password"),
                    resultSet.getString("access_level"),
                    resultSet.getBoolean("isLoggedIn")
            );
        }
        return null;
    }

    public static boolean isUserAuthenticated(String userName, String password) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + USER + " WHERE user_name=? AND password=?";
        Object[] ob = {
            userName,
            password
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        return resultSet.next();
    }

    public static boolean setUserLoginState(String userName, boolean loginState) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE " + USER + " SET isLoggedIn=? WHERE user_name=?";
        Object[] ob = {
            loginState,
            userName
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

}
