/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ledger;

import controller.inventory.BatchController;
import controller.ledger.item.DamagedStockItemController;
import database.connector.DBConnection;
import static database.connector.DatabaseInterface.DAMAGED_STOCK;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.ledger.DamageStock;
import model.ledger.item.DamagedItem;

/**
 *
 * @author Yasas
 */
public class DamagedStockController {

    public static DamageStock getDamagedStock(String damagedStockNumber) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT * FROM " + DAMAGED_STOCK + " WHERE damaged_stock_id=? ";

        Object[] ob = {
            util.Utilities.convertKeyToInteger(damagedStockNumber)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<DamagedItem> srnItemList = DamagedStockItemController.getAllAvailableItems(damagedStockNumber);

        while (resultSet.next()) {

            return new DamageStock(
                    util.Utilities.convertKeyToString(resultSet.getInt("damaged_stock_id"), DAMAGED_STOCK),
                    resultSet.getDate("ds_date"),
                    resultSet.getString("location"),
                    srnItemList);

        }
        return null;
    }

    public static boolean addDamagedStock(DamageStock stock) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);
            String query = "INSERT INTO " + DAMAGED_STOCK + " (damaged_stock_id, ds_date, location) VALUES (?, ?, ?)";

            Object[] ob = {
                util.Utilities.convertKeyToInteger(stock.getF17Number()),
                stock.getDate(),
                stock.getLocation()
            };

            boolean retVal = DBHandler.setData(connection, query, ob) == 1;

            for (DamagedItem it : stock.getItems()) {
                DamagedStockItemController.addNewItem(it);
                BatchController.reduceQuantity(it.getQuantityDamaged(), it.getBatchID());
            }

            connection.commit();
            return retVal;
        } catch (Exception err0) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException err1) {
                }
            }
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException err2) {
                }
            }
        }
    }

    public static String getNextDamagedStockID() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + DAMAGED_STOCK + "' AND table_schema = DATABASE( )";

        Object[] ob = {};

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return util.Utilities.convertKeyToString(resultSet.getInt("AUTO_INCREMENT"), DAMAGED_STOCK);
        }

        return util.Utilities.convertKeyToString(0, DAMAGED_STOCK);
    }

}
