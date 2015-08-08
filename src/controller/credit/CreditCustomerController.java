/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.credit;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.creditManagement.CreditCustomer;

/**
 *
 * @author HP
 */
public class CreditCustomerController implements DatabaseInterface {

    CreditCustomer creditCustomer;

    DefaultTableModel CustomerCreditDetailsTableModel;

    public static CreditCustomer getDetails(int id) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CREDIT_CUSTOMER + " WHERE customer_id=? ";
        Object[] ob = {
            id
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new CreditCustomer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("customer_name"),
                    resultSet.getString("customer_address"),
                    resultSet.getInt("customer_telephone"),
                    resultSet.getString("customer_nic"),
                    resultSet.getDouble("current_credit")
            );
        }
        return null;
    }

    public static ResultSet loadDetails() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CREDIT_CUSTOMER;

        ResultSet resultSet = DBHandler.getData(connection, query);
        return resultSet;
    }

    public static CreditCustomer getDetails() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CREDIT_CUSTOMER;

        ResultSet resultSet = DBHandler.getData(connection, query);

        if (resultSet.next()) {
            return new CreditCustomer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("customer_name"),
                    resultSet.getString("customer_address"),
                    resultSet.getInt("customer_telephone"),
                    resultSet.getString("customer_nic"),
                    resultSet.getDouble("current_credit")
            );
        }
        return null;
    }

    public static boolean setDetails(CreditCustomer creditCustomer) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();

            connection.setAutoCommit(false);
            String query = "INSERT INTO " + CREDIT_CUSTOMER + " (customer_name,customer_address,customer_telephone,customer_nic,customer_id,current_credit) VALUES (?,?,?,?,?,?) ";
            Object[] creditCustomerObj = {
                creditCustomer.getCustomerName(),
                creditCustomer.getCustomerAddress(),
                creditCustomer.getTelephone(),
                creditCustomer.getNic(),
                creditCustomer.getCustomerId(),
                creditCustomer.getCurrentCredit()
            };
            DBHandler.setData(connection, query, creditCustomerObj);
            connection.commit();
            return true;
        } catch (Exception ex) {
            if (connection != null) {
                try {
                    connection.rollback();

                } catch (SQLException ex1) {
                }
            }

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex1) {
                }
            }
        }
        return false;
    }

    public static boolean setEditDetails(CreditCustomer creditCustomer, int i) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);

            String query = "UPDATE " + CREDIT_CUSTOMER + "SET customer_name = ? , customer_address = ? , customer_telephone = ? , customer_nic = ? ,  current_credit = ? WHERE customer_id = ? ";

            Object[] creditCustomerObj = {
                creditCustomer.getCustomerName(),
                creditCustomer.getCustomerAddress(),
                creditCustomer.getTelephone(),
                creditCustomer.getNic(),
                creditCustomer.getCurrentCredit(),
                i
            };

            DBHandler.setData(connection, query, creditCustomerObj);
            connection.commit();
            return true;
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                }
            }

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex1) {
                }
            }
        }
        return false;
    }

    public static CreditCustomer searchDetails(String name) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CREDIT_CUSTOMER + " WHERE customer_name=? ";
        Object[] ob = {
            name
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new CreditCustomer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("customer_name"),
                    resultSet.getString("customer_address"),
                    resultSet.getInt("customer_telephone"),
                    resultSet.getString("customer_nic"),
                    resultSet.getDouble("current_credit")
            );
        }
        return null;

    }

    public static ArrayList<CreditCustomer> loadCustomers() throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT *  FROM " + CREDIT_CUSTOMER;

        ResultSet resultSet = DBHandler.getData(connection, query);
        ArrayList<CreditCustomer> customers = new ArrayList();
        while (resultSet.next()) {
            CreditCustomer customer = new CreditCustomer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("customer_name"),
                    resultSet.getString("customer_address"),
                    resultSet.getInt("customer_telephone"),
                    resultSet.getString("customer_nic"),
                    resultSet.getDouble("current_credit")
            );
            customers.add(customer);
        }

        return customers;
    }
}

/*   public static void setDetails(CreditCustomer creditCustomer, int i) throws SQLException {
 Connection connection2 = null;
 /*     String employeeIdNum = resultSet.getString("employee_id_num");
 String sql = "SELECT employee_name FROM" + EMPLOYEE + " WHERE employee_id=?" ;
 ResultSet resultSet1 = DBHandler.getData(connection, sql, ob);
 'UPDATE tutorials_tbl
 SET tutorial_title="Learning JAVA"
 WHERE tutorial_id=3';*/
/*    try {
 connection2 = DBConnection.getConnectionToDB();
 connection2.setAutoCommit(false);
 String query2 = "UPDATE " + CREDIT_CUSTOMER + "  (customer_name,customer_address,customer_telephone,customer_nic,current_credit) VALUES (?,?,?,?,?,?) " + "WHERE customer_id=i";
 Object[] creditCustomerObj = {
 creditCustomer.getCustomerName(),
 creditCustomer.getCustomerAddress(),
 creditCustomer.getTelephone(),
 creditCustomer.getNic(),
 creditCustomer.getCurrentCredit(),};
 DBHandler.setData(connection2, query2, creditCustomerObj);
 connection2.commit();

 } catch (Exception ex) {
 Logger.getLogger(CreditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
 if (connection2 != null) {
 try {
 connection2.rollback();
 } catch (SQLException ex1) {
 Logger.getLogger(CreditCustomerController.class.getName()).log(Level.SEVERE, null, ex1);
 }
 }

 } finally {
 if (connection2 != null) {
 try {
 connection2.setAutoCommit(true);
 } catch (SQLException ex1) {
 Logger.getLogger(CreditCustomerController.class.getName()).log(Level.SEVERE, null, ex1);
 }
 }
 }

 }

 }*/
