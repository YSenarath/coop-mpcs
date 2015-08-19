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
import model.inventory.Category;
import model.inventory.Department;
import util.Utilities;

/**
 *
 * @author Nadheesh
 */
public class CategoryController {

    public static ArrayList<Category> getCategories(String departmentId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT category_id, category_name, description , discounted FROM " + DatabaseInterface.CATEGORY + " WHERE department_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(departmentId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Category> categories = new ArrayList();

        while (resultSet.next()) {

            categories.add(new Category(
                    Utilities.convertKeyToString(resultSet.getInt("category_id"),
                            DatabaseInterface.CATEGORY),
                    resultSet.getString("category_name"),
                    departmentId,
                    resultSet.getString("description"),
                    resultSet.getBoolean("discounted")
            ));

        }
        return categories;
    }

    public static Category getCategory(String departmentId, String categoryId) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT category_id, category_name, description , discounted FROM " + DatabaseInterface.CATEGORY + " WHERE department_id=? AND category_id = ?";

        Object[] ob = {
            Utilities.convertKeyToInteger(departmentId),
            Utilities.convertKeyToInteger(categoryId)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        if (resultSet.next()) {
            return new Category(
                    categoryId,
                    resultSet.getString("category_name"),
                    departmentId,
                    resultSet.getString("description"),
                    resultSet.getBoolean("discounted")
            );
        }
        return null;
    }

    public static boolean addCateogry(Category category) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        int id = Utilities.convertKeyToInteger(category.getCategoryId());

        Integer idObj;

        if (id == 0 || id == -1) {
            idObj = null;
        } else {
            idObj = new Integer(id);
        };

        String query = "INSERT INTO " + DatabaseInterface.CATEGORY + " (category_id, category_name, description, department_id ) VALUES (?,?,?,?)";
        int depAdded = -1;
        Object[] objs = {
            idObj,
            category.getCategoryName(),
            category.getDescription(),
            Utilities.convertKeyToInteger(category.getDepartmentId())
        };
        depAdded = DBHandler.setData(connection, query, objs);
        return depAdded == 1;
    }

    public static boolean setDiscounted(Connection connection,boolean isDiscounted, int cid, int did) throws SQLException {

        String query = "UPDATE " + DatabaseInterface.CATEGORY + " SET discounted= ? WHERE category_id=? AND department_id=?";
        int depAdded = -1;
        Object[] objs = {
            isDiscounted,
            cid,
            did
        };

        depAdded = DBHandler.setData(connection, query, objs);
        return depAdded == 1;
    }

    public static boolean updateCategory(Category category) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE " + DatabaseInterface.CATEGORY + " SET category_name = ?, description = ?  WHERE category_id = ? AND department_id = ?  ";

        Object[] obj = {
            category.getCategoryName(),
            category.getDescription(),
            Utilities.convertKeyToInteger(category.getCategoryId()),
            Utilities.convertKeyToInteger(category.getDepartmentId())
        };
        int added = -1;
        added = DBHandler.setData(connection, query, obj);

        return added == 1;
    }

    public static boolean removeCategory(Category category) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "DELETE FROM " + DatabaseInterface.CATEGORY + " WHERE category_id = ? AND department_id=?   ";

        Object[] obj = {
            Utilities.convertKeyToInteger(category.getCategoryId()),
            Utilities.convertKeyToInteger(category.getDepartmentId())
        };
        int added = -1;
        added = DBHandler.setData(connection, query, obj);

        return added == 1;

    }
}
