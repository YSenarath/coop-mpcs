package controller.ledger;

import controller.inventory.BatchController;
import model.ledger.SupplierReturnNote;
import controller.ledger.item.SRNItemController;
import controller.supplier.SupplierController;
import database.connector.DBConnection;
import static database.connector.DatabaseInterface.SRN;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import model.ledger.item.SRNItem;

/**
 *
 * @author Yasas
 */
public class SupplierReturnNoteController {

    public static SupplierReturnNote getSrn(String srnNumber) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT * FROM " + SRN + " WHERE srn_number=? ";

        Object[] ob = {
            util.Utilities.convertKeyToInteger(srnNumber)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<SRNItem> srnItemList = SRNItemController.getAllAvailableItems(srnNumber);

        while (resultSet.next()) {

            return new SupplierReturnNote(
                    util.Utilities.convertKeyToString(resultSet.getInt("srn_number"), SRN),
                    util.Utilities.convertKeyToString(resultSet.getInt("grn_number"), SRN),
                    resultSet.getDate("srn_date"),
                    SupplierController.getSupplier(resultSet.getString("supplier_id")),
                    resultSet.getString("location"),
                    srnItemList
            );
        }
        return null;
    }

    public static boolean addSrn(SupplierReturnNote srn) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();

            String query = "INSERT INTO " + SRN + " (srn_number, grn_number, srn_date, supplier_id, location) VALUES (?, ?, ?, ?, ?)";

            Object[] ob = {
                util.Utilities.convertKeyToInteger(srn.getF19Number()),
                util.Utilities.convertKeyToInteger(srn.getGrnNumber()),
                srn.getDate(),
                util.Utilities.convertKeyToInteger(srn.getSupplier().getSupplerID()),
                srn.getLocation()
            };

            boolean retVal = DBHandler.setData(connection, query, ob) == 1;

            for (SRNItem it : srn.getItems()) {
                SRNItemController.addNewItem(it);
                BatchController.reduceQuantity(it.getQuantity(), it.getBatchID());
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

    public static String getNextSrnID() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + SRN + "' AND table_schema = DATABASE( )";

        Object[] ob = {};

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return util.Utilities.convertKeyToString(resultSet.getInt("AUTO_INCREMENT"), SRN);
        }

        return util.Utilities.convertKeyToString(0, SRN);
    }

    public static ArrayList<SupplierReturnNote> getSrn(String supplierId, Date from, Date to) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT * FROM " + SRN;

        Object[] ob = new Object[]{};

        if (!"All".equals(supplierId)) {
            query += " WHERE supplier_id=? ";

            ob = new Object[]{
                util.Utilities.convertKeyToInteger(supplierId)
            };
        }

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<SupplierReturnNote> output = new ArrayList<>();

        while (resultSet.next()) {

            String srnId = util.Utilities.convertKeyToString(resultSet.getInt("srn_number"), SRN);

            ArrayList<SRNItem> batches = SRNItemController.getAllAvailableItems(srnId);

            Date srnDate = resultSet.getDate("srn_date");

            if (util.Utilities.isDateBetweenRange(srnDate, from, to)) {
                output.add(new SupplierReturnNote(
                        srnId,
                        resultSet.getString("grn_number"),
                        srnDate,
                        SupplierController.getSupplier(supplierId),
                        resultSet.getString("location"),
                        batches
                ));
            }
        }

        return output;
    }

}
