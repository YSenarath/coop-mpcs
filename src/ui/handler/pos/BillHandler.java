/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.pos;

import controller.pos.InvoiceController;
import java.sql.SQLException;
import model.pos.Invoice;
import static util.Utilities.formatId;

/**
 *
 * @author Shehan
 */
public class BillHandler {

    public String getNewBillId() throws SQLException {
        Invoice invoice = InvoiceController.getLastBillId();
        if (invoice != null) {
            return formatId("B", 5, invoice.getInvoiceNo());
        }
        return formatId("B", 5, 1);
    }
}
