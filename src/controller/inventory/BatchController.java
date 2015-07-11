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
import java.util.ArrayList;
import model.inventory.Batch;
import util.definitions.AppConstants;

/**
 *
 * @author Shehan
 */
public class BatchController {

    public static ArrayList<Batch> getAllAvailableBatches(int productId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + AppConstants.BATCH + " b WHERE b.product_id=? AND b.quantity>0 AND b.in_stock=true";
        Object[] ob = {
            productId
        };
        ResultSet resultSet = DBHandler.getData(connection, query,ob);

        ArrayList<Batch> batches = new ArrayList();
        while (resultSet.next()) {
            Batch batch = new Batch(
                    resultSet.getInt("batch_id"),
                    resultSet.getInt("product_id"),
                    resultSet.getInt("grn_number"),
                    resultSet.getDouble("unit_cost"),
                    resultSet.getDouble("cost_discount"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDouble("quantity"),
                    resultSet.getString("expiration_date"),
                    resultSet.getString("notification_date"),
                    resultSet.getDouble("recieved_quantity"),
                    resultSet.getBoolean("in_stock"),
                    resultSet.getBoolean("discounted")
            );
            batches.add(batch);
        }
        return batches;
    }
}
