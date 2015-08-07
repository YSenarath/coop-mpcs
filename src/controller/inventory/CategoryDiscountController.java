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
import model.inventory.CategoryDiscount;
import util.Utilities;

/**
 *
 * @author Nadheesh
 */
public class CategoryDiscountController {

    public static HashMap<String, CategoryDiscount> getCategoryDiscounts(String departmentId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT category_id, discount, start_date, end_date , promotional , qty , members_only FROM " + DatabaseInterface.CATEGORY_DISCOUNT + " WHERE department_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(departmentId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        HashMap< String, CategoryDiscount> categoryDiscounts = new HashMap<>();

        while (resultSet.next()) {

            String categoryId = Utilities.convertKeyToString(resultSet.getInt("category_id"), DatabaseInterface.CATEGORY);

            categoryDiscounts.put(categoryId + departmentId, (new CategoryDiscount(categoryId, departmentId, resultSet.getDouble("discount"), resultSet.getDate("start_date"), resultSet.getDate("end_date"), resultSet.getBoolean("promotional"), resultSet.getInt("qty"), resultSet.getBoolean("members_only"))));

        }
        return categoryDiscounts;
    }

    public static CategoryDiscount getCategoryDiscount(String departmentId, String categoryId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT discount, start_date, end_date , promotional , qty , members_only FROM " + DatabaseInterface.CATEGORY_DISCOUNT + " WHERE department_id=? AND category_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(departmentId),
            Utilities.convertKeyToInteger(categoryId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        HashMap< String, CategoryDiscount> categoryDiscounts = new HashMap<>();

        if (resultSet.next()) {

            return new CategoryDiscount(
                    categoryId,
                    departmentId,
                    resultSet.getDouble("discount"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getBoolean("promotional"),
                    resultSet.getInt("qty"),
                    resultSet.getBoolean("members_only")
            );

        }
        return null;
    }

    public static boolean addCateogryDiscount(CategoryDiscount categoryDiscount, boolean isReplacing) throws SQLException {

        Connection connection = null;

        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);

            //Add your code here 
            int cid = Utilities.convertKeyToInteger(categoryDiscount.getCategoryId());
            int did = Utilities.convertKeyToInteger(categoryDiscount.getDepartmentId());

            Object[] objs = {
                categoryDiscount.getDiscount(),
                categoryDiscount.getStartDate(),
                categoryDiscount.getEndDate(),
                categoryDiscount.isPromotional(),
                categoryDiscount.getQuantity(),
                categoryDiscount.isMembersOnly(),
                cid,
                did
            };

            if (isReplacing) {

                String query2 = "UPDATE " + DatabaseInterface.CATEGORY_DISCOUNT + " SET discount = ? , start_date = ? , end_date = ? , promotional = ? , quantity = ? , members_only = ? WHERE category_id = ? AND department_id = ? ";
                DBHandler.setData(connection, query2, objs);

            } else {
                String query = "INSERT INTO " + DatabaseInterface.CATEGORY_DISCOUNT + " (discount, start_date, end_date ,promotional ,quantity , members_only,category_id, department_id ) VALUES (?,?,?,?,?,?,?,?)";
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
