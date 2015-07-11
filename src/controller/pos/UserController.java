/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pos;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.pos.User;
import util.definitions.AppConstants;

/**
 *
 * @author Shehan
 */
public class UserController {

    public static boolean isUserAuthenticated(String userName, String password) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + AppConstants.USER + " WHERE user_name=? AND password=?";
        Object[] ob = {
            userName,
            password
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        return resultSet.next();
    }

    public static User getUser(String search, String userName) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + AppConstants.USER + " WHERE " + search + "=? LIMIT 1";
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
        String query = "UPDATE " + AppConstants.USER + " SET isLoggedIn=? WHERE user_name=?";
        Object[] ob = {
            loginState,
            userName
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static ArrayList<User> getAllUsers() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "Select * From " + AppConstants.USER;
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

}
