/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pos;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import static database.connector.DatabaseInterface.INVOICE_ITEMS;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.pos.InvoiceItem;

/**
 *
 * @author Shehan
 */
public class InvoiceItemController implements DatabaseInterface {

    public static boolean addInvoiceItem(InvoiceItem invoiceItem) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + INVOICE_ITEMS + " (bill_id,batch_id,product_id,discount,unit_price,qty) VALUES (?,?,?,?,?,?) ";
        Object[] ob = {
            invoiceItem.getInvoiceId(),
            invoiceItem.getBatchId(),
            invoiceItem.getProductId(),
            invoiceItem.getDiscount(),
            invoiceItem.getUnitPrice(),
            invoiceItem.getQty()
        };
        return DBHandler.setData(connection, query, ob) > 0;
    }

    public static ArrayList<InvoiceItem> getInvoiceItems(int invoiceNo) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + INVOICE_ITEMS + " WHERE bill_id=?";
        Object[] ob = {
            invoiceNo
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        ArrayList<InvoiceItem> invoiceItems = new ArrayList();

        while (resultSet.next()) {
            InvoiceItem invoiceItem = new InvoiceItem(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("product_id"),
                    resultSet.getInt("batch_id"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDouble("qty"),
                    resultSet.getDouble("discount")
            );
            invoiceItems.add(invoiceItem);
        }

        return invoiceItems;

    }
}
