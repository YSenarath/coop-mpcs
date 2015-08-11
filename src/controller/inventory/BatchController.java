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
import model.inventory.Batch;
import model.inventory.BatchBuilder;
import model.pos.item.InvoiceItem;
import model.pos.item.RefundItem;
import util.Utilities;

/**
 *
 * @author Nadheesh
 */
public class BatchController {

    public static ArrayList<Batch> getBatches(String productId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT batch_id, grn_number, unit_cost, unit_price, qty, exp_date, notify_date, recieved_qty, sold_qty, in_stock, discounted FROM " + DatabaseInterface.BATCH + " WHERE product_id=?  AND in_stock = ?";

        Object[] ob = {
            Utilities.convertKeyToInteger(productId),
            true
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Batch> batches = new ArrayList();
        String id;

        while (resultSet.next()) {
            id = Utilities.convertKeyToString(resultSet.getInt("batch_id"), DatabaseInterface.BATCH);

            batches.add(
                    (BatchBuilder.Batch())
                    .withBatchId(id)
                    .withGRNNumber(resultSet.getString("grn_number"))
                    .withProductId(productId)
                    .withUnitCost(resultSet.getDouble("unit_cost"))
                    .withUnitPrice(resultSet.getDouble("unit_price"))
                    .withQuantity(resultSet.getDouble("qty"))
                    .withExpirationDate(resultSet.getDate("exp_date"))
                    .withNotificationDate(resultSet.getDate("notify_date"))
                    .withRecievedQuantity(resultSet.getDouble("recieved_qty"))
                    .withSoldQty(resultSet.getDouble("sold_qty"))
                    .withDiscounted(resultSet.getBoolean("discounted"))
                    .withInStock(resultSet.getBoolean("in_stock"))
                    .build()
            );
        }
        return batches;
    }

    public static ArrayList<Batch> getAllBatches(String productId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT batch_id, grn_number, unit_cost, unit_price, qty, exp_date, notify_date, recieved_qty, sold_qty, in_stock, discounted FROM " + DatabaseInterface.BATCH + " WHERE product_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(productId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Batch> batches = new ArrayList();
        String id;

        while (resultSet.next()) {
            id = Utilities.convertKeyToString(resultSet.getInt("batch_id"), DatabaseInterface.BATCH);

            batches.add(
                    (BatchBuilder.Batch())
                    .withBatchId(id)
                    .withGRNNumber(resultSet.getString("grn_number"))
                    .withProductId(productId)
                    .withUnitCost(resultSet.getDouble("unit_cost"))
                    .withUnitPrice(resultSet.getDouble("unit_price"))
                    .withQuantity(resultSet.getDouble("qty"))
                    .withExpirationDate(resultSet.getDate("exp_date"))
                    .withNotificationDate(resultSet.getDate("notify_date"))
                    .withRecievedQuantity(resultSet.getDouble("recieved_qty"))
                    .withSoldQty(resultSet.getDouble("sold_qty"))
                    .withDiscounted(resultSet.getBoolean("discounted"))
                    .withInStock(resultSet.getBoolean("in_stock"))
                    .build()
            );
        }
        return batches;
    }

    public static boolean addBatch(Batch batch) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "INSERT INTO " + DatabaseInterface.BATCH + " (batch_id, grn_number, unit_cost, unit_price, qty, exp_date, notify_date, recieved_qty ,  discounted ) VALUES (?,?,?,?,?,?,?,?,?)";
        int isAdded = -1;
        Object[] objs = {
            Utilities.convertKeyToInteger(batch.getBatchId()),
            Utilities.convertKeyToInteger(batch.getGrnNumber()),
            batch.getUnit_cost(),
            batch.getUnit_price(),
            batch.getQuantity(),
            batch.getExpirationDate(),
            batch.getNotificationDate(),
            batch.getRecievedQuantity(),
            batch.isDiscounted()
        };

        isAdded = DBHandler.setData(connection, query, objs);
        return isAdded == 1;
    }

    public static boolean addNewBatch(Batch batch) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "INSERT INTO " + DatabaseInterface.BATCH + " (product_id, grn_number, unit_cost, unit_price, qty, exp_date, notify_date, recieved_qty ,  discounted ) VALUES (?,?,?,?,?,?,?,?,?)";
        int isAdded = -1;
        
        Object[] objs = {
            util.Utilities.convertKeyToInteger(batch.getProductId()),
            util.Utilities.convertKeyToInteger(batch.getGrnNumber()),
            batch.getUnit_cost(),
            batch.getUnit_price(),
            batch.getQuantity(),
            batch.getExpirationDate(),
            batch.getNotificationDate(),
            batch.getRecievedQuantity(),
            batch.isDiscounted()
        };

        isAdded = DBHandler.setData(connection, query, objs);
        return isAdded == 1;
    }

    public static boolean setDiscounted(Connection connection, int bid, int pid) throws SQLException {

        String query = "UPDATE " + DatabaseInterface.BATCH + " SET discounted= TRUE WHERE batch_id=? AND product_id=?";
        int depAdded = -1;
        Object[] objs = {
            bid,
            pid
        };

        depAdded = DBHandler.setData(connection, query, objs);
        return depAdded == 1;
    }

    //POS
    public static Batch getBatch(int productId, int batchId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT grn_number, unit_cost, unit_price, qty, exp_date, notify_date, recieved_qty, sold_qty, in_stock, discounted FROM " + DatabaseInterface.BATCH + " WHERE product_id=? AND batch_id=?";

        Object[] ob = {
            productId,
            batchId
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {

            return (BatchBuilder.Batch())
                    .withBatchId(Utilities.convertKeyToString(batchId, DatabaseInterface.BATCH))
                    .withGRNNumber(resultSet.getString("grn_number"))
                    .withProductId(Utilities.convertKeyToString(productId, DatabaseInterface.PRODUCT))
                    .withUnitCost(resultSet.getDouble("unit_cost"))
                    .withUnitPrice(resultSet.getDouble("unit_price"))
                    .withQuantity(resultSet.getDouble("qty"))
                    .withExpirationDate(resultSet.getDate("exp_date"))
                    .withNotificationDate(resultSet.getDate("notify_date"))
                    .withRecievedQuantity(resultSet.getDouble("recieved_qty"))
                    .withSoldQty(resultSet.getDouble("sold_qty"))
                    .withDiscounted(resultSet.getBoolean("discounted"))
                    .withInStock(resultSet.getBoolean("in_stock"))
                    .build();

        }
        return null;
    }

    public static boolean setSoldQty(InvoiceItem invoiceItem) throws SQLException {
        //update sold amount,if sold amount = recieved amount ,in_stock is disabled

        Connection connection = DBConnection.getConnectionToDB();

        String query = "UPDATE " + DatabaseInterface.BATCH + " SET sold_qty=sold_qty+? WHERE batch_id=? AND product_id=?";
        Object[] ob = {
            invoiceItem.getQty(),
            invoiceItem.getBatchId(),
            invoiceItem.getProductId()
        };

        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static boolean setRefundQty(RefundItem refundItem) throws SQLException {
        //update sold amount,if sold amount = recieved amount ,in_stock is disabled

        Connection connection = DBConnection.getConnectionToDB();

        String query = "UPDATE " + DatabaseInterface.BATCH + " SET sold_qty=sold_qty-? WHERE batch_id=? AND product_id=?";
        Object[] ob = {
            refundItem.getQty(),
            refundItem.getBatchId(),
            refundItem.getProductId()
        };

        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static boolean setBatchInStock(boolean inStock, int batchId, int productId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "UPDATE " + DatabaseInterface.BATCH + " SET in_stock=? WHERE batch_id=? AND product_id=?";
        Object[] ob = {
            inStock,
            batchId,
            productId
        };

        return DBHandler.setData(connection, query, ob) == 1;
    }

    // GRN
    public static ArrayList<Batch> getAllAvailableBatchesOfGrn(String grnNumber) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT batch_id, unit_cost, product_id, unit_price, qty, exp_date, notify_date, recieved_qty, sold_qty, in_stock, discounted FROM " + DatabaseInterface.BATCH + " WHERE grn_number=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(grnNumber)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Batch> batches = new ArrayList();
        String id;

        while (resultSet.next()) {
            id = Utilities.convertKeyToString(resultSet.getInt("batch_id"), DatabaseInterface.BATCH);

            batches.add(
                    (BatchBuilder.Batch())
                    .withBatchId(id)
                    .withGRNNumber(grnNumber)
                    .withProductId(resultSet.getString("product_id"))
                    .withUnitCost(resultSet.getDouble("unit_cost"))
                    .withUnitPrice(resultSet.getDouble("unit_price"))
                    .withQuantity(resultSet.getDouble("qty"))
                    .withExpirationDate(resultSet.getDate("exp_date"))
                    .withNotificationDate(resultSet.getDate("notify_date"))
                    .withRecievedQuantity(resultSet.getDouble("recieved_qty"))
                    .withSoldQty(resultSet.getDouble("sold_qty"))
                    .withDiscounted(resultSet.getBoolean("discounted"))
                    .withInStock(resultSet.getBoolean("in_stock"))
                    .build()
            );
        }
        return batches;
    }
}
