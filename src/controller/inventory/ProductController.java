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
import model.inventory.Product;
import model.inventory.ProductBuilder;
import util.Utilities;

/**
 *
 * @author Nadheesh
 */
public class ProductController {

    public static boolean addProduct(Product product) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        int id = Utilities.convertKeyToInteger(product.getProductId());

        Integer idObj;
        if (id == 0 || id == -1) {
            idObj = null;
        } else {
            idObj = new Integer(id);
        }

        String query = "INSERT INTO " + DatabaseInterface.PRODUCT + "(product_id, product_name, barcode,description,category_id, department_id,unit,pack_size,reorder_value,reorder_qty,max_qty ) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        int depAdded = -1;
        Object[] objs = {
            idObj,
            product.getProductName(),
            product.getProductBarCode(),
            product.getDescription(),
            Utilities.convertKeyToInteger(product.getCategoryId()),
            Utilities.convertKeyToInteger(product.getDepartmentId()),
            product.getUnit(),
            product.getPackSize(),
            product.getReorderValue(),
            product.getReorderQuantity(),
            product.getMaxQuantity()
        };

        depAdded = DBHandler.setData(connection, query, objs);
        return depAdded == 1;
    }

    public static ArrayList<Product> getProductIdentities() throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT product_id, product_name FROM " + DatabaseInterface.PRODUCT;

        ResultSet resultSet = DBHandler.getData(connection, query);

        ArrayList<Product> products = new ArrayList();

        while (resultSet.next()) {

            products.add(ProductBuilder.Product()
                    .withProductId(Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT))
                    .withProductName(resultSet.getString("product_name"))
                    .build());

        }
        return products;
    }

    public static Product getProduct(Product product) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT barcode,description,category_id, department_id,unit,pack_size,reorder_value,reorder_qty,max_qty FROM " + DatabaseInterface.PRODUCT + " WHERE product_id= ?";

        Object[] objs = {
            Utilities.convertKeyToInteger(product.getProductId())
        };

        ResultSet resultSet = DBHandler.getData(connection, query, objs);

        if (resultSet.next()) {
            product.setProductBarCode(resultSet.getLong("barcode"));
            product.setDescription(resultSet.getString("description"));
            product.setCategoryId(Utilities.convertKeyToString(resultSet.getInt("category_id"), DatabaseInterface.CATEGORY));
            product.setDepartmentId(Utilities.convertKeyToString(resultSet.getInt("department_id"), DatabaseInterface.DEPARTMENT));
            product.setUnit(resultSet.getString("unit"));
            product.setPackSize(resultSet.getDouble("pack_size"));
            product.setReorderValue(resultSet.getDouble("reorder_value"));
            product.setReorderQuantity(resultSet.getDouble("reorder_qty"));
            product.setMaxQuantity(resultSet.getDouble("max_qty"));
        }
        return product;
    }

    //
    //POS
    //
    public static ArrayList<Product> getAllAvailableProducts() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT DISTINCT p.* FROM " + DatabaseInterface.PRODUCT + " p ," + DatabaseInterface.BATCH + " b WHERE p.product_id=b.product_id AND b.recieved_qty>0 AND b.in_stock=true";
        ResultSet resultSet = DBHandler.getData(connection, query);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = ProductBuilder.Product()
                    .withProductId(Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT))
                    .withProductName(resultSet.getString("product_name"))
                    .withBarCode(resultSet.getLong("barcode"))
                    .withDescrition(resultSet.getString("description"))
                    .withCategory(Utilities.convertKeyToString(resultSet.getInt("category_id"), DatabaseInterface.CATEGORY))
                    .withDepartment(Utilities.convertKeyToString(resultSet.getInt("department_id"), DatabaseInterface.DEPARTMENT))
                    .withUnit(resultSet.getString("unit"))
                    .withPackSize(resultSet.getDouble("pack_size"))
                    .withReorderValue(resultSet.getDouble("reorder_value"))
                    .withReorderQuantity(resultSet.getDouble("reorder_qty"))
                    .withMaxQuantity(resultSet.getDouble("max_qty"))
                    .build();

            products.add(product);
        }
        return products;
    }

    public static ArrayList<Product> getAllProducts() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + DatabaseInterface.PRODUCT;
        ResultSet resultSet = DBHandler.getData(connection, query);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = ProductBuilder.Product()
                    .withProductId(Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT))
                    .withProductName(resultSet.getString("product_name"))
                    .withBarCode(resultSet.getLong("barcode"))
                    .withDescrition(resultSet.getString("description"))
                    .withCategory(Utilities.convertKeyToString(resultSet.getInt("category_id"), DatabaseInterface.CATEGORY))
                    .withDepartment(Utilities.convertKeyToString(resultSet.getInt("department_id"), DatabaseInterface.DEPARTMENT))
                    .withUnit(resultSet.getString("unit"))
                    .withPackSize(resultSet.getDouble("pack_size"))
                    .withReorderValue(resultSet.getDouble("reorder_value"))
                    .withReorderQuantity(resultSet.getDouble("reorder_qty"))
                    .withMaxQuantity(resultSet.getDouble("max_qty"))
                    .build();

            products.add(product);
        }
        return products;
    }

    public static Product getProduct(String productId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + DatabaseInterface.PRODUCT + " WHERE product_id=? ";
        Object[] ob = {
            Utilities.convertKeyToInteger(productId)
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        if (resultSet.next()) {
            Product product = ProductBuilder.Product()
                    .withProductId(Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT))
                    .withProductName(resultSet.getString("product_name"))
                    .withBarCode(resultSet.getLong("barcode"))
                    .withDescrition(resultSet.getString("description"))
                    .withCategory(Utilities.convertKeyToString(resultSet.getInt("category_id"), DatabaseInterface.CATEGORY))
                    .withDepartment(Utilities.convertKeyToString(resultSet.getInt("department_id"), DatabaseInterface.DEPARTMENT))
                    .withUnit(resultSet.getString("unit"))
                    .withPackSize(resultSet.getDouble("pack_size"))
                    .withReorderValue(resultSet.getDouble("reorder_value"))
                    .withReorderQuantity(resultSet.getDouble("reorder_qty"))
                    .withMaxQuantity(resultSet.getDouble("max_qty"))
                    .build();

            return product;

        }
        return null;
    }

    public static ArrayList<Product> searchProducts(String description) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + DatabaseInterface.PRODUCT + " WHERE product_name LIKE ? OR description LIKE ? ";
        Object[] ob = {
            "%" + description + "%",
            "%" + description + "%"
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = ProductBuilder.Product()
                    .withProductId(Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT))
                    .withProductName(resultSet.getString("product_name"))
                    .withBarCode(resultSet.getLong("barcode"))
                    .withDescrition(resultSet.getString("description"))
                    .withCategory(Utilities.convertKeyToString(resultSet.getInt("category_id"), DatabaseInterface.CATEGORY))
                    .withDepartment(Utilities.convertKeyToString(resultSet.getInt("department_id"), DatabaseInterface.DEPARTMENT))
                    .withUnit(resultSet.getString("unit"))
                    .withPackSize(resultSet.getDouble("pack_size"))
                    .withReorderValue(resultSet.getDouble("reorder_value"))
                    .withReorderQuantity(resultSet.getDouble("reorder_qty"))
                    .withMaxQuantity(resultSet.getDouble("max_qty"))
                    .build();

            products.add(product);
        }
        return products;
    }

    public static ArrayList<Product> searchProducts(String description, String departmentId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + DatabaseInterface.PRODUCT + " WHERE (product_name LIKE ? OR description LIKE ?) AND department_id=? ";
        Object[] ob = {
            "%" + description + "%",
            "%" + description + "%",
            Utilities.convertKeyToInteger(departmentId)
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = ProductBuilder.Product()
                    .withProductId(Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT))
                    .withProductName(resultSet.getString("product_name"))
                    .withBarCode(resultSet.getLong("barcode"))
                    .withDescrition(resultSet.getString("description"))
                    .withCategory(Utilities.convertKeyToString(resultSet.getInt("category_id"), DatabaseInterface.CATEGORY))
                    .withDepartment(Utilities.convertKeyToString(resultSet.getInt("department_id"), DatabaseInterface.DEPARTMENT))
                    .withUnit(resultSet.getString("unit"))
                    .withPackSize(resultSet.getDouble("pack_size"))
                    .withReorderValue(resultSet.getDouble("reorder_value"))
                    .withReorderQuantity(resultSet.getDouble("reorder_qty"))
                    .withMaxQuantity(resultSet.getDouble("max_qty"))
                    .build();

            products.add(product);
        }
        return products;
    }

    public static ArrayList<Product> searchProducts(String description, String departmentId, String categoryId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + DatabaseInterface.PRODUCT + " WHERE (product_name LIKE ? OR description LIKE ?) AND department_id=? AND category_id=? ";
        Object[] ob = {
            "%" + description + "%",
            "%" + description + "%",
            Utilities.convertKeyToInteger(departmentId),
            Utilities.convertKeyToInteger(categoryId)
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = ProductBuilder.Product()
                    .withProductId(Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.PRODUCT))
                    .withProductName(resultSet.getString("product_name"))
                    .withBarCode(resultSet.getLong("barcode"))
                    .withDescrition(resultSet.getString("description"))
                    .withCategory(Utilities.convertKeyToString(resultSet.getInt("category_id"), DatabaseInterface.CATEGORY))
                    .withDepartment(Utilities.convertKeyToString(resultSet.getInt("department_id"), DatabaseInterface.DEPARTMENT))
                    .withUnit(resultSet.getString("unit"))
                    .withPackSize(resultSet.getDouble("pack_size"))
                    .withReorderValue(resultSet.getDouble("reorder_value"))
                    .withReorderQuantity(resultSet.getDouble("reorder_qty"))
                    .withMaxQuantity(resultSet.getDouble("max_qty"))
                    .build();

            products.add(product);
        }
        return products;
    }
    
    
    
    //new nadheesh upadated method
    
    public static boolean updateProduct(Product product) throws SQLException{
        
        Connection connection = DBConnection.getConnectionToDB();
        
        String query = "UPDATE " + DatabaseInterface.PRODUCT + " SET product_name = ? , barcode = ?,description =? ,category_id =? , department_id = ?,unit = ? ,"
                + " pack_size = ?, reorder_value = ? , reorder_qty = ?, max_qty = ?  WHERE product_id = ?  " ;
        
        Object[] obj  = {
            product.getProductName(),
            product.getProductBarCode(),
            product.getDescription(),
            Utilities.convertKeyToInteger(product.getCategoryId()),
            Utilities.convertKeyToInteger(product.getDepartmentId()),
            product.getUnit(),
            product.getPackSize(),
            product.getReorderValue(),
            product.getReorderQuantity(),
            product.getMaxQuantity(),
            Utilities.convertKeyToInteger(product.getProductId())
        };
        int added = -1;
        added = DBHandler.setData(connection, query, obj);
        
        return  added == 1 ;
    }
    
    public static boolean removeProduct(String productId) throws SQLException{
        
        Connection connection = DBConnection.getConnectionToDB();
        String query = "DELETE FROM " + DatabaseInterface.PRODUCT + " WHERE product_id=?  " ;
        
        Object[] obj  = {
            Utilities.convertKeyToInteger(productId)
        };
        int added = -1;
        added = DBHandler.setData(connection, query, obj);
        
        return  added == 1 ;
    }
    
}
