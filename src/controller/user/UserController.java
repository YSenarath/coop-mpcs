package controller.user;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.people.User;
import database.connector.DatabaseInterface;
import static database.connector.DatabaseInterface.USER;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static boolean setUserLoginState(String userName, boolean loginState) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE " + USER + " SET isLoggedIn=? WHERE user_name=?";
        Object[] ob = {
            loginState,
            userName
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static void addUser(User user) {
        try {
            Connection connection = DBConnection.getConnectionToDB();
            String query = "INSERT INTO " + USER + " (user_name,password,access_level,isLoggedIn) VALUES (?,?,?,?) ";
            Object[] ob = {
                user.getUserName(),
                user.getPassword(),
                user.getUserType(),
                user.isLoggedin()
            };
            DBHandler.setData(connection, query, ob);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static User getLoggedInUsers() {
        try {
            Connection connection = DBConnection.getConnectionToDB();
            String query = "SELECT * FROM " + USER + " WHERE isLoggedIn =? ";
            Object[] ob = {
                true
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

        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
