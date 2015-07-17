/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.inventory;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.inventory.Category;
import database.connector.DatabaseInterface;

/**
 *
 * @author Shehan
 */
public class CategoryController implements DatabaseInterface {

    public static Category getCategory(int departmentId, int categoryId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CATAGORY + " WHERE category_id=? AND department_id=?";
        Object[] ob = {
            categoryId,
            departmentId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        
        if (resultSet.next()) {
            return new Category(
                    resultSet.getInt("category_id"),
                    resultSet.getInt("department_id"),
                    resultSet.getString("category_name"),
                    resultSet.getString("description"),
                    resultSet.getBoolean("discounted")
            );
        }
        return null;
    }
}
