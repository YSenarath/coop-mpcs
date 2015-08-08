/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ledger;

import controller.inventory.BatchController;
import controller.supplier.SupplierController;
import database.connector.DBConnection;
import static database.connector.DatabaseInterface.GRN;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            grnNumber
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        ArrayList<Batch> batches = BatchController.getAllAvailableBatchesOfGrn(grnNumber);

        while (resultSet.next()) {
            // int f16bNumber, String invoiceNo, Date invoiceDate,
            // Supplier supplier, String location, String paymentMethod
            return new GoodRecieveNote(
                    resultSet.getInt("grn_number"),
                    resultSet.getString("invoice_no"),
                    resultSet.getDate("invoice_date"),
                    SupplierController.getSupplier(resultSet.getString("supplier_id")),
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

    public static boolean addGrn(GoodRecieveNote grn) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "INSERT INTO " + GRN + " (grn_number, invoice_no, invoice_date, supplier_id, location, payment_method, loading_fee, PurchasingBill_discount, sellingBill_discount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] ob = {
            grn.getF16bNumber(),
            grn.getInvoiceNo(),
            grn.getInvoiceDate(),
            grn.getSupplierId(),
            grn.getLocation(),
            grn.getPaymentMethod(),
            grn.getLoadingFee(),
            grn.getPurchasingBillDiscount(),
            grn.getSellingBillDiscount()
        };

        boolean retVal = DBHandler.setData(connection, query, ob) == 1;

        for (Batch b : grn.getBatches()) {
            BatchController.addNewBatch(b);
        }

        return retVal;
    }

    public static int getNextGrnID() throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();

        String query = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + GRN + "' AND table_schema = DATABASE( )";

        Object[] ob = {};

        ResultSet resultSet = DBHandler.getData(connection, query, ob);

        while (resultSet.next()) {
            return resultSet.getInt("AUTO_INCREMENT");
        }

        return 0;
    }

}
