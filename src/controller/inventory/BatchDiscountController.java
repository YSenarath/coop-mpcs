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

        Connection connection = DBConnection.getConnectionToDB();
        connection.setAutoCommit(false);

        int cid = Utilities.convertKeyToInteger(batchDiscount.getBatchId());
        int did = Utilities.convertKeyToInteger(batchDiscount.getProductId());

        int depAdded = -1;
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

        boolean set;

        if (isReplacing) {

            String query2 = "UPDATE " + DatabaseInterface.BATCH_DISCOUNT + " SET discount = ? , start_date = ? , end_date = ? , promotional = ? , qty = ? , members_only = ? WHERE batch_id = ? AND product_id = ? ";
            depAdded = DBHandler.setData(connection, query2, objs);
            set = true;

        } else {
            String query = "INSERT INTO " + DatabaseInterface.BATCH_DISCOUNT + " (discount, start_date, end_date ,promotional ,qty , members_only,batch_id, product_id ) VALUES (?,?,?,?,?,?,?,?)";
            depAdded = DBHandler.setData(connection, query, objs);
            set = CategoryController.setDiscounted(connection, cid, did);
        }

        if (set && depAdded == 1) {
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        }

        connection.rollback();
        connection.setAutoCommit(true);
        return false;
    }

}
