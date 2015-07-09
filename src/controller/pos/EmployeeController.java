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
import model.pos.Employee;
import util.definitions.AppConstants;

/**
 *
 * @author Shehan
 */
public class EmployeeController {

    public static Employee getEmployee(String employeeId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + AppConstants.EMPLOYEE + " WHERE employee_id=?";
        Object[] ob = {
            employeeId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new Employee(resultSet.getString("employee_id"), resultSet.getString("name"));
        }
        return null;
    }
}
