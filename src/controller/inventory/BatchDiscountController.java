/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.inventory;

import controller.pos.CounterController;
import controller.pos.CounterLoginController;
import controller.user.UserController;
import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String query = "SELECT batch_id, discount, start_date, end_date , promotional , qty , members_only FROM " + DatabaseInterface.BATCH_DISCOUNT + " WHERE product_id=? AND batch_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(productId),
            Utilities.convertKeyToInteger(batchId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {

            return new BatchDiscount(batchId, productId, resultSet.getDouble("discount"), resultSet.getDate("start_date"), resultSet.getDate("end_date"), resultSet.getBoolean("promotional"), resultSet.getInt("qty"), resultSet.getBoolean("members_only"));

        }
        return null;
    }

    public static HashMap<String, BatchDiscount> getBatchDiscounts(String productId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT batch_id, discount, start_date, end_date , promotional , qty , members_only FROM " + DatabaseInterface.BATCH_DISCOUNT + " WHERE product_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(productId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        HashMap< String, BatchDiscount> batchDiscounts = new HashMap<>();

        while (resultSet.next()) {

            String batchId = Utilities.convertKeyToString(resultSet.getInt("batch_id"), DatabaseInterface.BATCH);

            batchDiscounts.put(batchId + productId, (new BatchDiscount(batchId, productId, resultSet.getDouble("discount"), resultSet.getDate("start_date"), resultSet.getDate("end_date"), resultSet.getBoolean("promotional"), resultSet.getInt("qty"), resultSet.getBoolean("members_only"))));

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
                String query2 = "UPDATE " + DatabaseInterface.BATCH_DISCOUNT + " SET discount = ? , start_date = ? , end_date = ? , promotional = ? , qty = ? , members_only = ? WHERE batch_id = ? AND product_id = ? ";
                DBHandler.setData(connection, query2, objs);
            } else {
                String query = "INSERT INTO " + DatabaseInterface.BATCH_DISCOUNT + " (discount, start_date, end_date ,promotional ,qty , members_only,batch_id, product_id ) VALUES (?,?,?,?,?,?,?,?)";
                DBHandler.setData(connection, query, objs);
                CategoryController.setDiscounted(connection, cid, did);
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

}
