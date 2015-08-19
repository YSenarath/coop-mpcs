package controller.ledger.item;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.SQLException;
import model.inventory.Batch;

public class GRNItemController {

    public static boolean addNewItem(Batch it) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "INSERT INTO " + DatabaseInterface.GRN_ITEM + " (batch_id, price, qty, recieved_qty) VALUES (?,?,?,?)";

        Object[] objs = {
            util.Utilities.convertKeyToInteger(it.getBatchId()),
            it.getUnit_price(),
            it.getQuantity(),
            it.getRecievedQuantity()
        };

        return (DBHandler.setData(connection, query, objs) == 1);
    }

}
