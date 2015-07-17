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
import model.inventory.CategoryDiscount;
import database.connector.DatabaseInterface;

/**
 *
 * @author Shehan
 */
public class CategoryDiscountController implements DatabaseInterface {

    public static CategoryDiscount getCategoryDiscount(int departmentId, int categoryId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CATEGORY_DISCOUNT + " WHERE category_id=? AND department_id=?";
        Object[] ob = {
            categoryId,
            departmentId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            return new CategoryDiscount(
                    resultSet.getInt("category_id"),
                    resultSet.getInt("department_id"),
                    resultSet.getDouble("discount"),
                    resultSet.getString("start_date"),
                    resultSet.getString("end_date"),
                    resultSet.getBoolean("promotional"),
                    resultSet.getDouble("qty"),
                    resultSet.getBoolean("members_only"));
        }
        return null;
    }
}
