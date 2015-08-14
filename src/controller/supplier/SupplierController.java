/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.supplier;

import database.connector.DBConnection;
import static database.connector.DatabaseInterface.SUPPLIER;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.supplier.Supplier;

/**
 *
 * @author Yasas
 */
public class SupplierController {

    public static Supplier getSupplier(String supplierId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + SUPPLIER + " WHERE supplier_id=? ";
        Object[] ob = {
            util.Utilities.convertKeyToInteger(supplierId)
        };
    
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return new Supplier(
                    util.Utilities.convertKeyToString(resultSet.getInt("supplier_id"), SUPPLIER),
                    resultSet.getString("sup_name"),
                    resultSet.getString("contact_person"),
                    resultSet.getString("address"),
                    resultSet.getInt("tel_number"),
                    resultSet.getInt("fax_number"),
                    resultSet.getString("e_mail"),
                    resultSet.getDate("reg_date"),
                    resultSet.getDate("cancel_date")
            );
        }
        return null;
    }

    public static ArrayList<Supplier> getAllSuppliers() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + SUPPLIER;
        ResultSet resultSet = DBHandler.getData(connection, query);
        ArrayList<Supplier> suppliers = new ArrayList<>();
        while (resultSet.next()) {
            Supplier s = new Supplier(
                    util.Utilities.convertKeyToString(resultSet.getInt("supplier_id"), SUPPLIER),
                    resultSet.getString("sup_name"),
                    resultSet.getString("contact_person"),
                    resultSet.getString("address"),
                    resultSet.getInt("tel_number"),
                    resultSet.getInt("fax_number"),
                    resultSet.getString("e_mail"),
                    resultSet.getDate("reg_date"),
                    resultSet.getDate("cancel_date")
            );
            suppliers.add(s);
        }
        return suppliers;
    }

    public static boolean addSupplier(Supplier supplier) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        
        String query = "INSERT INTO " + SUPPLIER + " (supplier_id, sup_name, contact_person, address, tel_number, fax_number, e_mail, reg_date, cancel_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Object[] ob = {
            util.Utilities.convertKeyToInteger(supplier.getSupplerID()),
            supplier.getName(),
            supplier.getContactPerson(),
            supplier.getAddress(),
            supplier.getTelephoneNumber(),
            supplier.getFax(),
            supplier.getEmail(),
            supplier.getRagDate(),
            supplier.getCancelDate()
        };

        return DBHandler.setData(connection, query, ob) == 1;
    }
    
        public static String getNextDamagedStockID() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + SUPPLIER + "' AND table_schema = DATABASE( )";

        Object[] ob = {};

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return util.Utilities.convertKeyToString(resultSet.getInt("AUTO_INCREMENT"), SUPPLIER);
        }

        return util.Utilities.convertKeyToString(0, SUPPLIER);
    }
}
