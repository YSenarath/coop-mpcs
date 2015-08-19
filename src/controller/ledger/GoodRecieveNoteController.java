/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ledger;

import controller.inventory.BatchController;
import controller.ledger.item.GRNItemController;
import controller.supplier.SupplierController;
import database.connector.DBConnection;
import static database.connector.DatabaseInterface.GRN;
import static database.connector.DatabaseInterface.SUPPLIER;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import model.inventory.Batch;
import model.ledger.GoodRecieveNote;

/**
 *
 * @author Yasas
 */
public class GoodRecieveNoteController {

    public static GoodRecieveNote getGrn(String grnNumber) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + GRN + " WHERE grn_number=? ";
        Object[] ob = {
            util.Utilities.convertKeyToInteger(grnNumber)
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Batch> batches = BatchController.getAllAvailableBatchesOfGrn(grnNumber);

        while (resultSet.next()) {
            // int f16bNumber, String invoiceNo, Date invoiceDate,
            // Supplier supplier, String location, String paymentMethod
            return new GoodRecieveNote(
                    util.Utilities.convertKeyToString(resultSet.getInt("grn_number"), GRN),
                    resultSet.getString("invoice_no"),
                    resultSet.getDate("invoice_date"),
                    SupplierController.getSupplier(util.Utilities.convertKeyToString(resultSet.getInt("supplier_id"), SUPPLIER)),
                    resultSet.getString("location"),
                    resultSet.getString("payment_method"),
                    resultSet.getDouble("loading_fee"),
                    resultSet.getDouble("PurchasingBill_discount"),
                    resultSet.getDouble("sellingBill_discount"),
                    batches
            );
        }
        return null;
    }

    public static ArrayList<GoodRecieveNote> getGrn(String supplierId, Date from, Date to) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT * FROM " + GRN;

        Object[] ob = new Object[]{};

        if (!"All".equals(supplierId)) {
            query += " WHERE supplier_id=? ";

            ob = new Object[]{
                util.Utilities.convertKeyToInteger(supplierId)
            };
        }

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<GoodRecieveNote> output = new ArrayList<>();

        while (resultSet.next()) {

            String grnId = util.Utilities.convertKeyToString(resultSet.getInt("grn_number"), GRN);

            ArrayList<Batch> batches = BatchController.getAllAvailableBatchesOfGrn(grnId);

            Date grnDate = resultSet.getDate("invoice_date");

            if (util.Utilities.isDateBetweenRange(grnDate, from, to)) {
                output.add(new GoodRecieveNote(
                        grnId,
                        resultSet.getString("invoice_no"),
                        grnDate,
                        SupplierController.getSupplier(supplierId),
                        resultSet.getString("location"),
                        resultSet.getString("payment_method"),
                        resultSet.getDouble("loading_fee"),
                        resultSet.getDouble("PurchasingBill_discount"),
                        resultSet.getDouble("sellingBill_discount"),
                        batches
                ));
            }
        }

        return output;
    }

    public static boolean addGrn(GoodRecieveNote grn) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "INSERT INTO " + GRN + " (grn_number, invoice_no, invoice_date, supplier_id, location, payment_method, loading_fee, PurchasingBill_discount, sellingBill_discount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] ob = {
            util.Utilities.convertKeyToInteger(grn.getF16bNumber()),
            grn.getInvoiceNo(),
            grn.getInvoiceDate(),
            util.Utilities.convertKeyToInteger(grn.getSupplierId()),
            grn.getLocation(),
            grn.getPaymentMethod(),
            grn.getLoadingFee(),
            grn.getPurchasingBillDiscount(),
            grn.getSellingBillDiscount()
        };

        boolean retVal = DBHandler.setData(connection, query, ob) == 1;

        for (Batch b : grn.getBatches()) {
            BatchController.addBatch(b);
            GRNItemController.addNewItem(b);
        }

        return retVal;
    }

    public static String getNextGrnID() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + GRN + "' AND table_schema = DATABASE( )";

        Object[] ob = {};

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return util.Utilities.convertKeyToString(resultSet.getInt("AUTO_INCREMENT"), GRN);
        }

        return util.Utilities.convertKeyToString(0, GRN);
    }

    public static boolean GRNExists(String grnNumber) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT * FROM " + GRN + " WHERE grn_number=? ";

        Object[] ob = {
            util.Utilities.convertKeyToInteger(grnNumber)
        };

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return true;
        }
        return false;
    }

}
