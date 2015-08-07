/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.credit;

import database.connector.DBConnection;

import static database.connector.DatabaseInterface.EMPLOYEE;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.creditManagement.Employee;

/**
 *
 * @author HP
 */
public class EmployeeController {

    public static Employee getDetails() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + EMPLOYEE;

        ResultSet resultSet = DBHandler.getData(connection, query);

        if (resultSet.next()) {
            return new Employee(
                    resultSet.getInt("employee_id"),
                    resultSet.getString("employee_name"),
                    resultSet.getString("position"),
                    resultSet.getBoolean("voucher_issued")
            );
        }
        return null;
    }

    public static Employee getDetails(int id) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + EMPLOYEE + " WHERE employee_id=? ";
        Object[] ob = {
            id
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new Employee(
                    resultSet.getInt("employee_id"),
                    resultSet.getString("name"),
                    resultSet.getString("position"),
                    resultSet.getBoolean("voucher_issued")
            );
        }
        return null;
    }

    public static Employee searchDetails(String name) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + EMPLOYEE + " WHERE name=? ";
        Object[] ob = {
            name
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new Employee(
                    resultSet.getInt("employee_id"),
                    resultSet.getString("name"),
                    resultSet.getString("position"),
                    resultSet.getBoolean("voucher_issued")
            );
        }
        return null;

    }

    public static ResultSet loadDetails() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT employee_id,name,position FROM " + EMPLOYEE;

        ResultSet resultSet = DBHandler.getData(connection, query);
        return resultSet;
    }

    public static boolean addEmployee(Employee employee) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + EMPLOYEE + " (employee_id,name,position,voucher_issued) VALUES (?,?,?,?) ";
        Object[] ob = {
            employee.getEmployeeId(),
            employee.getEmployeeName(),
            employee.getPosition(),
            employee.isVoucherIssued()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static boolean setEditDetails(Employee employee, int i) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);

            String query = "UPDATE " + EMPLOYEE + " SET name = ? , position = ?  WHERE employee_id = ? ";

            Object[] creditCustomerObj = {
                employee.getEmployeeName(),
                employee.getPosition(),
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

    public static ArrayList<Employee> loadEmployees() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT *  FROM " + EMPLOYEE;

        ResultSet resultSet = DBHandler.getData(connection, query);
        ArrayList<Employee> employees = new ArrayList();
        while (resultSet.next()) {
            Employee employee = new Employee(
                    resultSet.getInt("employee_id"),
                    resultSet.getString("employee_name"),
                    resultSet.getString("position"),
                    resultSet.getBoolean("voucher_issued")
            );
            employees.add(employee);
        }

        return employees;
    }

}
