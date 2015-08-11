/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ledger.item;

import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.ResultSet;
import util.Utilities;
import database.connector.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import model.ledger.item.DamagedItem;
import model.ledger.item.DamagedItemBuilder;

/**
 *
 * @author Yasas
 */
public class DamagedStockItemController {

    public static ArrayList<DamagedItem> getAllAvailableItems(String damagedStockNumber) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + DatabaseInterface.DAMAGED_STOCK_ITEM + " WHERE damaged_stock_id=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(damagedStockNumber)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<DamagedItem> items = new ArrayList();
        String id;

        while (resultSet.next()) {

            DamagedItem itm = new DamagedItemBuilder()
                    .setProductID(Utilities.convertKeyToString(resultSet.getInt("product_id"), DatabaseInterface.DAMAGED_STOCK))
                    .setQuantity(resultSet.getDouble("qty"))
                    .setDamagedItemID(Utilities.convertKeyToString(resultSet.getInt("damaged_stock_id"), DatabaseInterface.DAMAGED_STOCK))
                    .setQuantityDamaged(resultSet.getDouble("qty_damaged"))
                    .setSellingPrice(resultSet.getDouble("price"))
                    .createDamagedItem();
            items.add(itm);

        }
        return items;
    }

    public static boolean addNewItem(DamagedItem it) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "INSERT INTO " + DatabaseInterface.DAMAGED_STOCK_ITEM + " (damaged_stock_id, product_id, qty, qty_damaged, price) VALUES (?,?,?,?,?)";

        Object[] objs = {
            util.Utilities.convertKeyToInteger(it.getDamagedItemID()),
            util.Utilities.convertKeyToInteger(it.getProductID()),
            it.getQuantity(),
            it.getQuantityDamaged(),
            it.getSellingPrice()
        };

        return (DBHandler.setData(connection, query, objs) == 1);
    }

}
