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
import java.util.HashMap;
import model.inventory.BatchDiscount;
import util.Utilities;

/**
 *
 * @author Nadheesh
 */
public class BatchDiscountController {

    public static BatchDiscount getBatchDiscount(String productId, String batchId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT batch_id, "
                + "discount, "
                + "start_date, "
                + "promotional , "
                + "qty , "
                + "members_only FROM "
                + DatabaseInterface.BATCH_DISCOUNT
                + " WHERE product_id=? "
                + " AND batch_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(productId),
            Utilities.convertKeyToInteger(batchId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {

            return new BatchDiscount(batchId,
                    productId,
                    resultSet.getDouble("discount"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getBoolean("promotional"),
                    resultSet.getInt("qty"),
                    resultSet.getBoolean("members_only"));

        }
        return null;
    }

    public static HashMap<String, BatchDiscount> getBatchDiscounts(String productId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT batch_id, "
                + "discount, "
                + "start_date, "
                + "end_date , "
                + "promotional , "
                + "qty , "
                + "members_only FROM "
                + DatabaseInterface.BATCH_DISCOUNT
                + " WHERE product_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(productId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        HashMap< String, BatchDiscount> batchDiscounts = new HashMap<>();

        while (resultSet.next()) {

            String batchId = Utilities.convertKeyToString(resultSet.getInt("batch_id"), DatabaseInterface.BATCH);

            batchDiscounts.put(batchId + productId, (new BatchDiscount(
                    batchId,
                    productId,
                    resultSet.getDouble("discount"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getBoolean("promotional"),
                    resultSet.getInt("qty"),
                    resultSet.getBoolean("members_only")
            )));

        }
        return batchDiscounts;
    }

    public static boolean addBatchDiscount(BatchDiscount batchDiscount, boolean isReplacing) throws SQLException {

        Connection connection = null;

        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);

            //Add your code here 
            int cid = Utilities.convertKeyToInteger(batchDiscount.getBatchId());
            int did = Utilities.convertKeyToInteger(batchDiscount.getProductId());

            Object[] objs = {
                batchDiscount.getDiscount(),
                batchDiscount.getStartDate(),
                batchDiscount.getEndDate(),
                batchDiscount.isPromotional(),
                batchDiscount.getQuantity(),
                batchDiscount.isMembersOnly(),
                cid,
                did
            };

            if (isReplacing) {
                String query2 = "UPDATE "
                        + DatabaseInterface.BATCH_DISCOUNT
                        + " SET discount = ? , "
                        + "start_date = ? , "
                        + "end_date = ? , "
                        + "promotional = ? , "
                        + "qty = ? , "
                        + "members_only = ? "
                        + "WHERE batch_id = ? "
                        + "AND product_id = ? ";

                DBHandler.setData(connection, query2, objs);
            } else {
                String query = "INSERT INTO "
                        + DatabaseInterface.BATCH_DISCOUNT
                        + " (discount, "
                        + "start_date, "
                        + "end_date ,"
                        + "promotional ,"
                        + "qty , "
                        + "members_only,"
                        + "batch_id, "
                        + "product_id ) "
                        + "VALUES (?,?,?,?,?,?,?,?)";

                DBHandler.setData(connection, query, objs);
                BatchController.setDiscounted(connection, true, cid, did);
            }

            connection.commit();
            return true;
        } catch (Exception err0) {
            //logger.error("Exception occurred " + err0.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                    // logger.debug("Connection rolledback");
                } catch (SQLException err1) {
                    // logger.error("SQLException occurred " + err1.getMessage());
                }
            }
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    //  logger.debug("Connection setAutoCommit(true)");
                } catch (SQLException err2) {
                    //  logger.error("SQLException occurred " + err2.getMessage());
                }
            }
        }
    }

    public static ArrayList<BatchDiscount> getAllBatchDiscounts() throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT "
                + "bd.batch_id, "
                + "bd.product_id, "
                + "bd.discount, "
                + "bd.start_date, "
                + "bd.end_date , "
                + "bd.promotional , "
                + "bd.qty , "
                + "bd.members_only, "
                + "p.product_name "
                + "FROM " + DatabaseInterface.BATCH_DISCOUNT + " bd "
                + "JOIN " + DatabaseInterface.PRODUCT + " p "
                + "ON p.product_id=bd.product_id "
                + "JOIN " + DatabaseInterface.BATCH + " b "
                + "ON b.batch_id=bd.batch_id AND b.product_id=bd.product_id "
                + "WHERE b.in_stock = ? "
                + "ORDER BY bd.product_id";

        Object[] ob = {
            true
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<BatchDiscount> batchDiscounts = new ArrayList<>();

        while (resultSet.next()) {

            batchDiscounts.add(new BatchDiscount(
                    Utilities.convertKeyToString(resultSet.getInt("batch_id"), DatabaseInterface.BATCH),
                    Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT),
                    resultSet.getString("product_name"),
                    resultSet.getDouble("discount"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getBoolean("promotional"),
                    resultSet.getInt("qty"),
                    resultSet.getBoolean("members_only")
            ));

        }
        return batchDiscounts;
    }

    public static ArrayList<BatchDiscount> getAllBatchDiscountsInCategory(String departmentId, String categoryId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT "
                + "bd.batch_id, "
                + "bd.product_id, "
                + "bd.discount, "
                + "bd.start_date, "
                + "bd.end_date , "
                + "bd.promotional , "
                + "bd.qty , "
                + "bd.members_only, "
                + "p.product_name "
                + "FROM " + DatabaseInterface.BATCH_DISCOUNT + " bd "
                + "JOIN " + DatabaseInterface.PRODUCT + " p "
                + "ON p.product_id=bd.product_id "
                + "JOIN " + DatabaseInterface.BATCH + " b "
                + "ON b.batch_id=bd.batch_id AND b.product_id=bd.product_id "
                + "WHERE p.department_id = ? AND p.category_id = ? AND b.in_stock = ? "
                + "ORDER BY bd.product_id ";

        Object[] ob = {
            Utilities.convertKeyToInteger(departmentId),
            Utilities.convertKeyToInteger(categoryId),
            true
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<BatchDiscount> batchDiscounts = new ArrayList<>();

        while (resultSet.next()) {

            batchDiscounts.add(new BatchDiscount(
                    Utilities.convertKeyToString(resultSet.getInt("batch_id"), DatabaseInterface.BATCH),
                    Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT),
                    resultSet.getString("product_name"),
                    resultSet.getDouble("discount"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getBoolean("promotional"),
                    resultSet.getInt("qty"),
                    resultSet.getBoolean("members_only")
            ));

        }
        return batchDiscounts;
    }

    // removeBatchDiscount
    public static boolean removeBatchDiscount(String batchId, String productId) throws SQLException {

        Connection connection = null;

        try {
            connection = DBConnection.getConnectionToDB();

            int bid = Utilities.convertKeyToInteger(batchId);
            int pid = Utilities.convertKeyToInteger(productId);

            String query = "DELETE FROM "
                    + DatabaseInterface.BATCH_DISCOUNT
                    + " WHERE batch_id = ? AND product_id=?   ";

            Object[] obj = {
                bid,
                pid
            };

            DBHandler.setData(connection, query, obj);
            BatchController.setDiscounted(connection, false, bid, pid);

            connection.commit();
            return true;

        } catch (Exception err0) {
            //logger.error("Exception occurred " + err0.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                    // logger.debug("Connection rolledback");
                } catch (SQLException err1) {
                    // logger.error("SQLException occurred " + err1.getMessage());
                }
            }
            return false;

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    //  logger.debug("Connection setAutoCommit(true)");
                } catch (SQLException err2) {
                    //  logger.error("SQLException occurred " + err2.getMessage());
                }
            }
        }
    }

}
