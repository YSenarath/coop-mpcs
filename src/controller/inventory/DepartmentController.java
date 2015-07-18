package controller.inventory;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.inventory.Department;

public class DepartmentController implements DatabaseInterface {

    public static Department getDepartment(int departmentId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + DEPARTMENT + " WHERE department_id=? ";
        Object[] ob = {
            departmentId
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new Department(resultSet.getInt("department_id"), resultSet.getString("deparatment_name"));
        }
        return null;
    }
}
