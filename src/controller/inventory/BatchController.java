package controller.inventory;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.inventory.Batch;
import database.connector.DatabaseInterface;
import model.pos.InvoiceItem;

public class BatchController implements DatabaseInterface {

    public static ArrayList<Batch> getAllAvailableBatches(int productId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + BATCH + " b WHERE b.product_id=? AND b.qty>0 AND b.in_stock=true";
        Object[] ob = {
            productId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Batch> batches = new ArrayList();
        while (resultSet.next()) {
            Batch batch = new Batch(
                    resultSet.getInt("batch_id"),
                    resultSet.getInt("product_id"),
                    resultSet.getInt("grn_number"),
                    resultSet.getDouble("unit_cost"),
                    resultSet.getDouble("cost_discount"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDouble("qty"),
                    resultSet.getString("exp_date"),
                    resultSet.getString("notify_date"),
                    resultSet.getDouble("recieved_qty"),
                    resultSet.getBoolean("in_stock"),
                    resultSet.getBoolean("discounted")
            );
            batches.add(batch);
        }
        return batches;
    }

    public static ArrayList<Batch> getAllBatches(int productId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + BATCH + " b WHERE b.product_id=? AND b.qty>0 ";
        Object[] ob = {
            productId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Batch> batches = new ArrayList();
        while (resultSet.next()) {
            Batch batch = new Batch(
                    resultSet.getInt("batch_id"),
                    resultSet.getInt("product_id"),
                    resultSet.getInt("grn_number"),
                    resultSet.getDouble("unit_cost"),
                    resultSet.getDouble("cost_discount"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDouble("qty"),
                    resultSet.getString("exp_date"),
                    resultSet.getString("notify_date"),
                    resultSet.getDouble("recieved_qty"),
                    resultSet.getBoolean("in_stock"),
                    resultSet.getBoolean("discounted")
            );
            batches.add(batch);
        }
        return batches;
    }

    public static boolean reduceBatchQty(InvoiceItem invoiceItem) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE " + BATCH + " SET qty=qty-? WHERE batch_id=? AND product_id=? ";
        Object[] ob = {
            invoiceItem.getQty(),
            invoiceItem.getBatchId(),
            invoiceItem.getProductId()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }
    
    public static boolean increaseBatchQty(InvoiceItem invoiceItem) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE " + BATCH + " SET qty=qty+? WHERE batch_id=? AND product_id=? ";
        Object[] ob = {
            invoiceItem.getQty(),
            invoiceItem.getBatchId(),
            invoiceItem.getProductId()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }
}
