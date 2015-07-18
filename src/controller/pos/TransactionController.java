/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pos;

import controller.inventory.BatchController;
import database.connector.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import model.pos.CardPayment;
import model.pos.CashPayment;
import model.pos.CoopCreditPayment;
import model.pos.CustomerVoucherPayment;
import model.pos.EmployeeVoucherPayment;
import model.pos.Invoice;
import model.pos.InvoiceItem;
import model.pos.Payment;
import model.pos.PoshanaPayment;
import org.apache.log4j.Logger;

/**
 *
 * @author Shehan
 */
public class TransactionController {

    private static final Logger logger = Logger.getLogger(TransactionController.class);

    public static boolean performTransaction(Invoice invoice) {
        //Update database- batches, invoice,invoice items,invoice payments(5 tables), counter total
        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();

            connection.setAutoCommit(false);
            logger.debug("Connection setAutoCommit(false)");

            //Add to invoice table
            boolean result = InvoiceController.addInvoice(invoice);
            logger.info("Add invoice : " + result);

            for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                //Add to invoice item table
                result = InvoiceItemController.addInvoiceItem(invoiceItem);
                logger.info("Add invoice item : " + result);

                //Update batch item
                result = BatchController.updateBatchItem(invoiceItem);
                logger.info("Update batch : " + result);
            }

            for (Payment payment : invoice.getPayments()) {

                if (payment instanceof CashPayment) {
                    //Add cash payment
                    CashPayment cashPayment = (CashPayment) payment;
                    result = CashPaymentController.addCashPayment(cashPayment);
                    logger.info("Add cash payment : " + result);

                    //Update counter amount
                    double amountAddedToCounter = cashPayment.getAmount() - cashPayment.getChangeAmount();
                    result = CounterController.updateCounterAmount(invoice.getCounterId(), amountAddedToCounter);
                    logger.info("Update counter amount : " + result);

                } else if (payment instanceof CardPayment) {
                    //Add card payment
                    CardPayment cardPayment = (CardPayment) payment;
                    result = CardPaymentController.addCardPayment(cardPayment);
                    logger.info("Add card payment : " + result);

                } else if (payment instanceof CoopCreditPayment) {
                    throw new Exception("Not implemented");
                } else if (payment instanceof PoshanaPayment) {
                    throw new Exception("Not implemented");
                } else if (payment instanceof CustomerVoucherPayment) {
                    throw new Exception("Not implemented");
                } else if (payment instanceof EmployeeVoucherPayment) {
                    throw new Exception("Not implemented");
                }
            }

            connection.commit();
            return true;
        } catch (Exception err0) {
            logger.error("Exception occurred " + err0.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                    logger.debug("Connection rolledback");
                } catch (SQLException err1) {
                    logger.error("SQLException occurred " + err1.getMessage());
                }
            }
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    logger.debug("Connection setAutoCommit(true)");
                } catch (SQLException err2) {
                    logger.error("SQLException occurred " + err2.getMessage());
                }
            }
        }

    }
}
