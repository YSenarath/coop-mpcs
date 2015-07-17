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
import model.inventory.BatchDiscount;
import database.connector.DatabaseInterface;

/**
 *
 * @author Shehan
 */
public class BatchDiscountController implements DatabaseInterface {

    public static BatchDiscount getBatchDiscount(int productId, int batchId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + BATCH_DISCOUNT + " WHERE batch_id=? AND product_id=?";
        Object[] ob = {
            batchId,
            productId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        if (resultSet.next()) {
            return new BatchDiscount(
                    resultSet.getInt("batch_id"),
                    resultSet.getInt("product_id"),
                    resultSet.getDouble("discount"),
                    resultSet.getString("start_date"),
                    resultSet.getString("end_date"),
                    resultSet.getBoolean("promotional"),
                    resultSet.getDouble("qty"),
                    resultSet.getBoolean("members_only")
            );
        }
        return null;
    }
}
