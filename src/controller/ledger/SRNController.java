/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.ledger;

/**
 *
 * @author Yasas
 */
public class SRNController {
    /*
     public static GoodRecieveNote getSrn(int srnNumber) throws SQLException {
       
     Connection connection = DBConnection.getConnectionToDB();
     String query = "SELECT * FROM " + SRN + " WHERE srn_number=? ";
     Object[] ob = {
     srnNumber
     };
     ResultSet resultSet = DBHandler.getData(connection, query, ob);

     ArrayList<SRNItem> srnItem = BatchController.getAllAvailableBatchesOfGrn(grnNumber);

     while (resultSet.next()) {
     // int f16bNumber, String invoiceNo, Date invoiceDate,
     // Supplier supplier, String location, String paymentMethod
     return new GoodRecieveNote(
     resultSet.getInt("grn_number"),
     resultSet.getString("invoice_no"),
     resultSet.getDate("invoice_date"),
     SupplierController.getSupplier(resultSet.getInt("supplier_id")),
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

     public static boolean addSrn(SupplierReturnNote grn) throws SQLException {
     Connection connection = DBConnection.getConnectionToDB();

     String query = "INSERT INTO " + SRN + " (grn_number, invoice_no, invoice_date, supplier_id, location, payment_method, loading_fee, PurchasingBill_discount, sellingBill_discount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
     BatchController.addBatch(b);
     }

     return retVal;
     }

     public static int getNextSrnID() throws SQLException {
     Connection connection = DBConnection.getConnectionToDB();

     String query = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + SRN + "' AND table_schema = DATABASE( )";

     Object[] ob = {};

     ResultSet resultSet = DBHandler.getData(connection, query, ob);

     while (resultSet.next()) {
     return resultSet.getInt("AUTO_INCREMENT");
     }

     return 0;
     }
     */
}
