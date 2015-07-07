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
public class LoginController {
    //1.Get User info from the database as a result set
    //2.Authenticate user
    //Add edit delete user

    public static ArrayList<User> getAllCashRecipts() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "Select * From " + AppConstants.USER;
        ResultSet resultSet = DBHandler.getData(connection, query);
        ArrayList<User> allUsers = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User(resultSet.getString("user_name"), resultSet.getString("password"));
            allUsers.add(user);
        }
        
        return allUsers;
    }
}
