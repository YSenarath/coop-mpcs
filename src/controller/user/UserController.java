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
    
    public static ResultSet loadDetails() throws SQLException {
        
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT user_name,access_level FROM " + USER;

        ResultSet resultSet = DBHandler.getData(connection, query);
        System.out.println("data loaded to the table");
        return resultSet;
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

    public static void addUser(User user) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + USER + " (user_name,password,access_level,isLoggedIn) VALUES (?,?,?,?) ";
        Object[] ob = {
            user.getUserName(),
            user.getPassword(),
            user.getUserType(),
            user.isLoggedin()
        };
        DBHandler.setData(connection, query, ob);

    }

    public static User getLoggedInUsers() throws SQLException {

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

        return null;
    }
    
    public static boolean editUser(User user) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);

            String query = "UPDATE " + USER + " SET  user_name = ? , password = ? , access_level = ? , isLoggedIn = ? WHERE user_name = ? ";

           Object[] ob = {
            user.getUserName(),
            user.getPassword(),
            user.dataTruncation(user.getUserType()),
            user.isLoggedin(),
            user.getUserName()
        };

            DBHandler.setData(connection, query, ob);
            connection.commit();
            return true;
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    throw ex1;
                }
            }

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex1) {
                    throw ex1;
                }
            }
        }
        return false;
    }
    
    public static boolean deleteDeatils(String name) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "DELETE FROM " + USER + " WHERE user_name=?";

        Object[] ob = {
            name
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }
    
    public static boolean checkDataExistence(String name) throws SQLException{
         Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + USER + " WHERE user_name=? ";
        Object[] ob = {
            name
        };
        ResultSet resultSet = DBHandler.getData(connection, query,ob);

        if (resultSet.next()) {
            return true;
           
        }
        return false;
    }
}
