package controller.ledger;

import controller.inventory.BatchController;
import database.connector.DBConnection;
import static database.connector.DatabaseInterface.GRN;
import static database.connector.DatabaseInterface.GRN_CANCEL;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.inventory.Batch;
import model.ledger.GRNCancel;

public class GoodRecieveNoteCancelController {

    public static GRNCancel getGrnCancel(String grnCancelNumber) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT * FROM " + GRN_CANCEL + " WHERE grn_cancel_id=? ";

        Object[] ob = {
            util.Utilities.convertKeyToInteger(grnCancelNumber)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return new GRNCancel(
                    util.Utilities.convertKeyToString(resultSet.getInt("grn_cancel_id"), GRN_CANCEL),
                    util.Utilities.convertKeyToString(resultSet.getInt("grn_number"), GRN),
                    util.Utilities.getDateFromString("cancel_date")
            );
        }

        return null;
    }

    public static boolean addGrnCancel(GRNCancel grnCancel) throws SQLException {

        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();

            String query = "INSERT INTO " + GRN_CANCEL + " (grn_cancel_id, grn_number, cancel_date) VALUES (?, ?, ?)";

            Object[] ob = {
                util.Utilities.convertKeyToInteger(grnCancel.getF16aNumberCancel()),
                util.Utilities.convertKeyToInteger(grnCancel.getF16aNumber()),
                grnCancel.getDate()
            };

            boolean retVal = DBHandler.setData(connection, query, ob) == 1;

            for (Batch it : grnCancel.getItems()) {
                BatchController.reduceQuantity(it.getRecievedQuantity(), it.getBatchId());
                BatchController.setGRN("G1", it.getBatchId());
                BatchController.setBatchInStock(false, util.Utilities.convertKeyToInteger(it.getBatchId()), util.Utilities.convertKeyToInteger(it.getProductId()));
            }

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

    public static String getNextGrnCancelID() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + GRN_CANCEL + "' AND table_schema = DATABASE( )";

        Object[] ob = {};

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return util.Utilities.convertKeyToString(resultSet.getInt("AUTO_INCREMENT"), GRN_CANCEL);
        }

        return util.Utilities.convertKeyToString(0, GRN_CANCEL);
    }

}
