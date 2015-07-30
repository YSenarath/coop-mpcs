/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.inventory;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.inventory.Department;
import util.Utilities;

/**
 *
 * @author Nadheesh
 */
public class DepartmentController {

    public static boolean addDepartment(Department department) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        int id = Utilities.convertKeyToInteger(department.getDepartmentId());
        Integer idObj;
        if (id == 0 || id == -1) {
            idObj = null;
        } else {
            idObj = new Integer(id);
        }

        String query = "INSERT INTO " + DatabaseInterface.DEPARTMENT + "(department_id, department_name) VALUES (?,?)";
        int depAdded = -1;
        Object[] objs = {
            idObj,
            department.getDepartmentName()
        };
        depAdded = DBHandler.setData(connection, query, objs);
        return depAdded == 1;
    }

    public static ArrayList<Department> getDepartments() throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT department_id, department_name FROM " + DatabaseInterface.DEPARTMENT;

        ResultSet resultSet = DBHandler.getData(connection, query);

        ArrayList<Department> deps = new ArrayList();

        while (resultSet.next()) {

            deps.add(new Department(
                    Utilities.convertKeyToString(resultSet.getInt("department_id"), DatabaseInterface.DEPARTMENT),
                    resultSet.getString("department_name")
            ));

        }
        return deps;
    }

    public static Department getDepartment(String departmentId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT department_name FROM " + DatabaseInterface.DEPARTMENT + " WHERE department_id = ? ";

        Object[] obj = {
            Utilities.convertKeyToInteger(departmentId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, obj);

        if (resultSet.next()) {
            return new Department(
                    departmentId,
                    resultSet.getString("department_name"));
        }
        return null;

    }
}
