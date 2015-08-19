/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ledger.item;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.ledger.item.SRNItem;
import model.ledger.item.SRNItemBuilder;
import util.Utilities;

/**
 *
 * @author Yasas
 */
public class SRNItemController {

    public static ArrayList<SRNItem> getAllAvailableItems(String srnNumber) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + DatabaseInterface.SRN_ITEM + " WHERE srn_no=?";

        Object[] ob = {
            Utilities.convertKeyToInteger(srnNumber)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<SRNItem> items = new ArrayList();
        String id;

        while (resultSet.next()) {

            SRNItem itm = new SRNItemBuilder().
                    setSrnNumber(Utilities.convertKeyToString(resultSet.getInt("srn_no"), DatabaseInterface.SRN))
                    .setBatchID(Utilities.convertKeyToString(resultSet.getInt("batch_id"), DatabaseInterface.BATCH))
                    .setQuantity(resultSet.getDouble("qty"))
                    .setCostPrice(resultSet.getDouble("cost"))
                    .setSalesPrice(resultSet.getDouble("price"))
                    .createSRNItem();
            items.add(itm);
        }
        return items;
    }

    public static boolean addNewItem(SRNItem it) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "INSERT INTO " + DatabaseInterface.SRN_ITEM + " (srn_no, batch_id, qty, cost, price) VALUES (?,?,?,?,?)";

        Object[] objs = {
            util.Utilities.convertKeyToInteger(it.getSrnNumber()),
            util.Utilities.convertKeyToInteger(it.getBatchID()),
            it.getQuantity(),
            it.getCostPrice(),
            it.getSalesPrice()
        };

        return (DBHandler.setData(connection, query, objs) == 1);
    }
    
}
