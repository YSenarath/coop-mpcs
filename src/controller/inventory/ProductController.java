package controller.inventory;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.inventory.Product;
import database.connector.DatabaseInterface;

public class ProductController implements DatabaseInterface {

    public static ArrayList<Product> getAllAvailableProducts() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT DISTINCT p.* FROM " + PRODUCT + " p ," + BATCH + " b WHERE p.product_id=b.product_id AND b.recieved_qty>0 AND b.in_stock=true";
        ResultSet resultSet = DBHandler.getData(connection, query);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getLong("barcode"),
                    resultSet.getString("description"),
                    resultSet.getInt("category_id"),
                    resultSet.getInt("department_id"),
                    resultSet.getString("unit"),
                    resultSet.getDouble("pack_size"),
                    resultSet.getDouble("reorder_value"),
                    resultSet.getDouble("reorder_qty"),
                    resultSet.getDouble("max_qty")
            );
            products.add(product);
        }
        return products;
    }

    public static ArrayList<Product> getAllProducts() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + PRODUCT;
        ResultSet resultSet = DBHandler.getData(connection, query);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getLong("barcode"),
                    resultSet.getString("description"),
                    resultSet.getInt("category_id"),
                    resultSet.getInt("department_id"),
                    resultSet.getString("unit"),
                    resultSet.getDouble("pack_size"),
                    resultSet.getDouble("reorder_value"),
                    resultSet.getDouble("reorder_qty"),
                    resultSet.getDouble("max_qty")
            );
            products.add(product);
        }
        return products;
    }

    public static Product getProduct(int productId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + PRODUCT + " WHERE product_id=? ";
        Object[] ob = {
            productId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getLong("barcode"),
                    resultSet.getString("description"),
                    resultSet.getInt("category_id"),
                    resultSet.getInt("department_id"),
                    resultSet.getString("unit"),
                    resultSet.getDouble("pack_size"),
                    resultSet.getDouble("reorder_value"),
                    resultSet.getDouble("reorder_qty"),
                    resultSet.getDouble("max_qty")
            );
        }
        return null;
    }

    public static ArrayList<Product> searchProducts(String description) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + PRODUCT + " WHERE product_name LIKE ? OR description LIKE ? ";
        Object[] ob = {
            "%" + description + "%",
            "%" + description + "%"
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getLong("barcode"),
                    resultSet.getString("description"),
                    resultSet.getInt("category_id"),
                    resultSet.getInt("department_id"),
                    resultSet.getString("unit"),
                    resultSet.getDouble("pack_size"),
                    resultSet.getDouble("reorder_value"),
                    resultSet.getDouble("reorder_qty"),
                    resultSet.getDouble("max_qty")
            );
            products.add(product);
        }
        return products;
    }

    public static ArrayList<Product> searchProducts(String description, int departmentId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + PRODUCT + " WHERE (product_name LIKE ? OR description LIKE ?) AND department_id=? ";
        Object[] ob = {
            "'%" + description + "%'",
            "'%" + description + "%'",
            departmentId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getLong("barcode"),
                    resultSet.getString("description"),
                    resultSet.getInt("category_id"),
                    resultSet.getInt("department_id"),
                    resultSet.getString("unit"),
                    resultSet.getDouble("pack_size"),
                    resultSet.getDouble("reorder_value"),
                    resultSet.getDouble("reorder_qty"),
                    resultSet.getDouble("max_qty")
            );
            products.add(product);
        }
        return products;
    }

    public static ArrayList<Product> searchProducts(String description, int departmentId, int categoryId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + PRODUCT + " WHERE (product_name LIKE ? OR description LIKE ?) AND department_id=? AND category_id=? ";
        Object[] ob = {
            "'%" + description + "%'",
            "'%" + description + "%'",
            departmentId,
            categoryId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getLong("barcode"),
                    resultSet.getString("description"),
                    resultSet.getInt("category_id"),
                    resultSet.getInt("department_id"),
                    resultSet.getString("unit"),
                    resultSet.getDouble("pack_size"),
                    resultSet.getDouble("reorder_value"),
                    resultSet.getDouble("reorder_qty"),
                    resultSet.getDouble("max_qty")
            );
            products.add(product);
        }
        return products;
    }
}
