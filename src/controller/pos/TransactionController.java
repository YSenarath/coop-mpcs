package controller.pos;

//import controller.inventory.BatchController;
import controller.inventory.BatchController;
import database.connector.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import model.inventory.Batch;
import model.pos.payment.CardPayment;
import model.pos.payment.CashPayment;
import model.pos.CashWithdrawal;
import model.pos.payment.CoopCreditPayment;
import model.pos.CounterLogin;
import model.pos.payment.CustomerVoucherPayment;
import model.pos.payment.EmployeeVoucherPayment;
import model.pos.item.Invoice;
import model.pos.item.InvoiceItem;
import model.pos.payment.Payment;
import model.pos.payment.PoshanaPayment;
import org.apache.log4j.Logger;

public class TransactionController {

    private static final Logger logger = Logger.getLogger(TransactionController.class);

    public static boolean performLogInTransaction(CounterLogin counterLogin) {
        logger.debug("performLogInTransaction invoked");

        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);
            logger.debug("Connection setAutoCommit(false)");

            //Change user login state
            boolean result = UserController.setUserLoginState(counterLogin.getUserName(), true);
            logger.info("User login state : " + result);

            //Add counter login
            result = CounterLoginController.addCounterLogin(counterLogin);
            logger.info("Add counter login : " + result);

            //Update counter amount
            result = CounterController.setCounterAmount(counterLogin.getCounterId(), counterLogin.getInitialAmount());
            logger.info("Update counter amount : " + result);

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

    public static boolean performLogOffTransaction(CounterLogin counterLogin) {
        logger.debug("performLogInTransaction invoked");

        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);
            logger.debug("Connection setAutoCommit(false)");

            boolean result = UserController.setUserLoginState(counterLogin.getUserName(), false);
            logger.info("User loged off : " + result);

            result = CounterLoginController.endShift(counterLogin);
            logger.info("Shift ended : " + result);

            //Reset counter amount to 0
            result = CounterController.setCounterAmount(counterLogin.getCounterId(), 0);
            logger.info("Rest counter amount to 0 : " + result);

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

    public static boolean performInvoiceTransaction(Invoice invoice,int counterId) {
        logger.debug("performInvoiceTransaction invoked");
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

                boolean inStock = true;
                Batch batch = BatchController.getBatch(invoiceItem.getProductId(), invoiceItem.getBatchId());
                if (invoiceItem.getQty() + batch.getSoldQty() >= batch.getRecievedQuantity()) {
                    inStock = false;
                }
                //Update batch item
                result = BatchController.setSoldQty(invoiceItem);
                logger.info("Update batch sold amount: " + result);

                //Set batch enabled or disabled
                if (!inStock) {
                    result = BatchController.setBatchInStock(inStock, invoiceItem.getBatchId(), invoiceItem.getProductId());
                    logger.info("Set batch in stock: " + result);
                }
            }

            for (Payment payment : invoice.getPayments()) {

                if (payment instanceof CashPayment) {
                    //Add cash payment
                    CashPayment cashPayment = (CashPayment) payment;
                    result = CashPaymentController.addCashPayment(cashPayment);
                    logger.info("Add cash payment : " + result);

                    //Update counter amount
                    double amountAddedToCounter = cashPayment.getAmount() - cashPayment.getChangeAmount();
                    result = CounterController.addToCounterAmount(counterId, amountAddedToCounter);
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

    public static boolean performCashWithdrawalTransaction(CashWithdrawal cashWithdrawal) {
        logger.debug("performInvoiceTransaction invoked");

        Connection connection = null;
        try {
            connection = DBConnection.getConnectionToDB();
            connection.setAutoCommit(false);
            logger.debug("Connection setAutoCommit(false)");

            //Perform withdrawal
            boolean result = CashWithdrawalController.addCashWithdrawal(cashWithdrawal);
            logger.info("Cash withdrawal : " + result);

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
