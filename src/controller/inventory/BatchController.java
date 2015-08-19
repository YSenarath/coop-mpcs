/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.inventory;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import static database.connector.DatabaseInterface.PRODUCT;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

    //methods by JNJ=============================================================================
   
    public static ArrayList<Batch> getBatches(String productId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT batch_id, "
                + "grn_number, "
                + "unit_cost, "
                + "unit_price, "
                + "qty, "
                + "exp_date, "
                + "notify_date, "
                + "recieved_qty, "
                + "sold_qty, "
                + "in_stock, "
                + "discounted FROM " 
                + DatabaseInterface.BATCH 
                + " WHERE product_id=?  "
                + "AND in_stock = ?";

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
                    .withInStock(true)
                    .build()
            );
        }
        return batches;
    }

    public static boolean addNewBatch(Batch batch) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "INSERT INTO "
                + DatabaseInterface.BATCH
                + " (product_id, "
                + "grn_number, "
                + "unit_cost, "
                + "unit_price, "
                + "qty, "
                + "exp_date, "
                + "notify_date, "
                + "recieved_qty ,  "
                + "discounted ) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";

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

    public static boolean setDiscounted(Connection connection, boolean isDiscounted, int bid, int pid) throws SQLException {

        String query = "UPDATE "
                + DatabaseInterface.BATCH
                + " SET discounted= ? "
                + "WHERE batch_id=? "
                + "AND product_id=?";
        int depAdded = -1;
        Object[] objs = {
            isDiscounted,
            bid,
            pid
        };

        depAdded = DBHandler.setData(connection, query, objs);
        return depAdded == 1;
    }

    //updated method by JNJ --> 8/16/2015
    public static ArrayList<Batch> getBatchesOfProduct(String productId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT batch_id, "
                + "b.grn_number, "
                + "b.unit_cost, "
                + "b.unit_price, "
                + "b.qty, "
                + "b.exp_date, "
                + "b.notify_date, "
                + "b.recieved_qty, "
                + "b.sold_qty, "
                + "s.supplier_id, "
                + "s.sup_name, "
                + "b.discounted FROM "
                + DatabaseInterface.BATCH + " b "
                + " JOIN " + DatabaseInterface.GRN + " g "
                + " ON g.grn_number=b.grn_number"
                + " JOIN " + DatabaseInterface.SUPPLIER + " s "
                + " ON s.supplier_id=g.supplier_id"
                + " WHERE b.product_id=?  "
                + " AND b.in_stock = ? "
                + " ORDER BY batch_id";

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
                    .withGRNNumber(Utilities.convertKeyToString(resultSet.getInt("grn_number"), DatabaseInterface.GRN))
                    .withProductId(productId)
                    .withUnitCost(resultSet.getDouble("unit_cost"))
                    .withUnitPrice(resultSet.getDouble("unit_price"))
                    .withQuantity(resultSet.getDouble("qty"))
                    .withExpirationDate(resultSet.getDate("exp_date"))
                    .withNotificationDate(resultSet.getDate("notify_date"))
                    .withRecievedQuantity(resultSet.getDouble("recieved_qty"))
                    .withSoldQty(resultSet.getDouble("sold_qty"))
                    .withDiscounted(resultSet.getBoolean("discounted"))
                    .withSupplierName(resultSet.getString("sup_name"))
                    .withSupplierID(Utilities.convertKeyToString(resultSet.getInt("supplier_id"), DatabaseInterface.SUPPLIER))
                    .withInStock(true)
                    .build()
            );
        }
        return batches;
    }

    public static ArrayList<Batch> getCloseToExpireBatches() throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT batch_id, "
                + "b.grn_number, "
                + "b.exp_date, "
                + "b.product_id, "
                + "p.product_name, "
                + "b.notify_date, "
                + "b.recieved_qty, "
                + "s.supplier_id, "
                + "s.sup_name "
                + " FROM "
                + DatabaseInterface.BATCH + " b "
                + " JOIN " + DatabaseInterface.GRN + " g "
                + " ON g.grn_number=b.grn_number "
                + " JOIN " + DatabaseInterface.SUPPLIER + " s "
                + " ON s.supplier_id=g.supplier_id"
                + " JOIN " + DatabaseInterface.PRODUCT + " p "
                + " ON p.product_id=b.product_id "
                + " WHERE b.in_stock = ? "
                + " AND ((notify_date IS NULL AND exp_date <= ?) OR notify_date <= ?) "
                + " ORDER BY product_id";

        Object[] ob = {
            true,
            Utilities.getCurrentDate(),
            Utilities.getCurrentDate()
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Batch> batches = new ArrayList();

        while (resultSet.next()) {

            batches.add(
                    (BatchBuilder.Batch())
                    .withBatchId(Utilities.convertKeyToString(resultSet.getInt("batch_id"), DatabaseInterface.BATCH))
                    .withGRNNumber(Utilities.convertKeyToString(resultSet.getInt("grn_number"), DatabaseInterface.GRN))
                    .withProductId(Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT))
                    .withProductName(resultSet.getString("product_name"))
                    .withExpirationDate(resultSet.getDate("exp_date"))
                    .withNotificationDate(resultSet.getDate("notify_date"))
                    .withRecievedQuantity(resultSet.getDouble("recieved_qty"))
                    .withSupplierName(resultSet.getString("sup_name"))
                    .withSupplierID(Utilities.convertKeyToString(resultSet.getInt("supplier_id"), DatabaseInterface.SUPPLIER))
                    .withInStock(true)
                    .build()
            );
        }
        return batches;
    }

    public static boolean updateBatchWithNotifyDate(String productId, String batchId, Date notifyDate) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "UPDATE "
                + DatabaseInterface.BATCH
                + " SET notify_date=? "
                + " WHERE product_id = ? "
                + " AND batch_id = ?";

        int isAdded = -1;

        Object[] objs = {
            notifyDate,
            util.Utilities.convertKeyToInteger(productId),
            util.Utilities.convertKeyToInteger(batchId)
        };

        isAdded = DBHandler.setData(connection, query, objs);
        return isAdded == 1;
    }
    //============================================================================================

    //POS--by msw=================================================================================
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

    //Methods by aesky=========================================================================
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
                    .withProductId(util.Utilities.convertKeyToString(resultSet.getInt("product_id"), PRODUCT))
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

    public static boolean reduceQuantity(double quantity, String batchID) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "UPDATE " + DatabaseInterface.BATCH + " SET recieved_qty=recieved_qty-? WHERE batch_id=?";
        Object[] ob = {
            quantity,
            Utilities.convertKeyToInteger(batchID)
        };

        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static boolean setGRN(String grn, String batchID) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "UPDATE " + DatabaseInterface.BATCH + " SET grn_number=? WHERE batch_id=?";
        Object[] ob = {
            Utilities.convertKeyToInteger(grn),
            Utilities.convertKeyToInteger(batchID)
        };

        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static String getNextBatchID() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + DatabaseInterface.BATCH + "' AND table_schema = DATABASE( )";

        Object[] ob = {};

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return util.Utilities.convertKeyToString(resultSet.getInt("AUTO_INCREMENT"), DatabaseInterface.BATCH);
        }

        return util.Utilities.convertKeyToString(0, DatabaseInterface.BATCH);
    }

    // Needed in SRN
    public static ArrayList<Batch> getAllBatches(String batchID) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT product_id, grn_number, unit_cost, unit_price, qty, exp_date, notify_date, recieved_qty, sold_qty, in_stock, discounted FROM " + DatabaseInterface.BATCH + " WHERE batch_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(batchID)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Batch> batches = new ArrayList();
        String id;

        while (resultSet.next()) {
            id = Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT);

            batches.add(
                    (BatchBuilder.Batch())
                    .withBatchId(batchID)
                    .withGRNNumber(resultSet.getString("grn_number"))
                    .withProductId(id)
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

    //==============================================================================================
}
