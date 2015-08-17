package report.pos;

import controller.inventory.ProductController;
import controller.payments.CardPaymentController;
import controller.payments.CashPaymentController;
import controller.payments.CoopCreditPaymentController;
import controller.payments.CustomerVoucherPaymentController;
import controller.payments.EmployeeVoucherPaymentController;
import controller.payments.PoshanaPaymentController;
import controller.pos.CounterLoginController;
import controller.pos.InvoiceController;
import controller.pos.RefundController;
import controller.pos.RefundItemController;
import database.connector.DatabaseInterface;
import java.util.ArrayList;
import java.util.HashMap;
import model.inventory.Product;
import model.pos.CashWithdrawal;
import model.pos.CounterLogin;
import model.pos.item.Invoice;
import model.pos.item.InvoiceItem;
import model.pos.item.Refund;
import model.pos.item.RefundItem;
import model.pos.payment.CardPayment;
import model.pos.payment.CashPayment;
import model.pos.payment.CoopCreditPayment;
import model.pos.payment.CustomerVoucherPayment;
import model.pos.payment.EmployeeVoucherPayment;
import model.pos.payment.Payment;
import model.pos.payment.PoshanaPayment;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;
import org.apache.velocity.texen.util.FileUtil;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class ReportGenerator {

    private static final Logger logger = Logger.getLogger(ReportGenerator.class);

    private ReportGenerator() {

    }

    public static void generateCashierSignOnSlip(CounterLogin counterLogin) {
        logger.debug("generateCashierSignOnSlip invoked ");

        String fileName = "/model/report/pos/cashierSignOn.jasper";
        String outFileName = "reports/output/pos/cashierSignOn.pdf";
        FileUtil.mkdir("reports/output/pos/");
        HashMap hm = new HashMap();
        try {
            hm.put("shiftNo", Utilities.convertKeyToString(counterLogin.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            hm.put("cashierName", counterLogin.getUserName());
            hm.put("counter", counterLogin.getCounterId());
            hm.put("onDate", counterLogin.getLogInDate());
            hm.put("onTime", Utilities.convert24hTo12h(counterLogin.getLogInTime()));
            hm.put("amount", counterLogin.getInitialAmount());

            JasperPrint jasperPrint = JasperFillManager.fillReport(new ReportGenerator().getClass().getResourceAsStream(fileName), hm, new JREmptyDataSource());

            if (jasperPrint != null) {
                JasperViewer.viewReport(jasperPrint, false);
                // Create a PDF exporter
//                JRPdfExporter exporter = new JRPdfExporter();
//
//                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName));
//                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//                exporter.setConfiguration(configuration);
//
//                exporter.exportReport();
            }
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateCashierSignOffSlip(CounterLogin counterLogin) {
        logger.debug("generateCashierSignOffSlip invoked ");

        String fileName = "/model/report/pos/cashierSignOff.jasper";
        String outFileName = "reports/output/pos/cashierSignOff.pdf";
        FileUtil.mkdir("reports/output/pos/");
        HashMap hm = new HashMap();
        try {
            hm.put("shiftNo", Utilities.convertKeyToString(counterLogin.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            hm.put("cashierName", counterLogin.getUserName());
            hm.put("counter", counterLogin.getCounterId());
            hm.put("onDate", counterLogin.getLogInDate());
            hm.put("onTime", Utilities.convert24hTo12h(counterLogin.getLogInTime()));
            hm.put("initialAmount", counterLogin.getInitialAmount());
            hm.put("finalAmount", counterLogin.getLogOffActual());
            hm.put("offTime", Utilities.convert24hTo12h(counterLogin.getLogOffTime()));
            hm.put("offDate", counterLogin.getLogOffDate());
            hm.put("cashWithdrawal", counterLogin.getCashWithdrawals());

            double amexAmount = 0;
            double masterAmount = 0;
            double visaAmount = 0;

            ArrayList<Integer> invoiceIdList = InvoiceController.getInvoicesFromShift(counterLogin.getShiftId());

            for (Integer i : invoiceIdList) {
                ArrayList<CardPayment> cardPayments = CardPaymentController.getCardPayments(i.intValue());
                for (CardPayment cardPayment : cardPayments) {
                    switch (cardPayment.getCardType()) {
                        case CardPayment.AMEX:
                            amexAmount += cardPayment.getAmount();
                            break;
                        case CardPayment.MASTER:
                            masterAmount += cardPayment.getAmount();
                            break;
                        case CardPayment.VISA:
                            visaAmount += cardPayment.getAmount();
                            break;
                    }
                }
            }
            hm.put("amexCard", amexAmount);
            hm.put("masterCard", masterAmount);
            hm.put("visaCard", visaAmount);

            ArrayList<CancelledItemsBean> cancelledItemsBeans = new ArrayList<CancelledItemsBean>();
            ArrayList<Refund> refunds = RefundController.getRefundsFromShiftId(counterLogin.getShiftId());

            for (Refund refund : refunds) {
                String invoiceId = Utilities.convertKeyToString(refund.getInvoiceId(), DatabaseInterface.INVOICE);
                ArrayList<RefundItem> refundItems = RefundItemController.getRefundItems(refund.getRefundId());
                for (RefundItem refundItem : refundItems) {
                    Product product = ProductController.getProduct(Utilities.convertKeyToString(refundItem.getProductId(), DatabaseInterface.PRODUCT));
                    CancelledItemsBean cancelledItemsBean = new CancelledItemsBean(invoiceId, product.getProductId(), product.getProductName(), String.format("%.2f", refundItem.getQty()));
                    cancelledItemsBeans.add(cancelledItemsBean);
                }
            }

            JasperPrint jasperPrint = null;
            if (!cancelledItemsBeans.isEmpty()) {
                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(cancelledItemsBeans);
                jasperPrint = JasperFillManager.fillReport(new ReportGenerator().getClass().getResourceAsStream(fileName), hm, beanColDataSource);
            } else {
                jasperPrint = JasperFillManager.fillReport(new ReportGenerator().getClass().getResourceAsStream(fileName), hm, new JREmptyDataSource());
            }

            if (jasperPrint != null) {
                JasperViewer.viewReport(jasperPrint, false);
                // Create a PDF exporter
//                JRPdfExporter exporter = new JRPdfExporter();
//
//                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName));
//                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//                exporter.setConfiguration(configuration);
//
//                exporter.exportReport();
            }
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateManagerSignOffSlip(CounterLogin counterLogin) {
        logger.debug("generateManagerSignOffSlip invoked ");

        String fileName = "/model/report/pos/managerSignOff.jasper";
        String outFileName = "reports/output/pos/managerSignOff.pdf";
        FileUtil.mkdir("reports/output/pos/");
        HashMap hm = new HashMap();
        try {
            hm.put("shiftNo", Utilities.convertKeyToString(counterLogin.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            hm.put("cashierName", counterLogin.getUserName());
            hm.put("counter", counterLogin.getCounterId());
            hm.put("initialAmount", counterLogin.getInitialAmount());
            hm.put("cashWithdrawal", counterLogin.getCashWithdrawals());
            hm.put("onDate", counterLogin.getLogInDate());
            hm.put("onTime", Utilities.convert24hTo12h(counterLogin.getLogInTime()));
            hm.put("offTime", Utilities.convert24hTo12h(counterLogin.getLogOffTime()));
            hm.put("offDate", counterLogin.getLogOffDate());
            hm.put("date", Utilities.getStringDate(Utilities.getCurrentDate()));
            hm.put("time", Utilities.getCurrentTime(false));

//            double cashAmount = 0;
            double amexAmount = 0;
            double masterAmount = 0;
            double visaAmount = 0;

            double coopCreditAmount = 0;

            double customerVoucherAmount = 0;

            double employeeVoucherAmount = 0;

            double PoshanaAmount = 0;

            ArrayList<Integer> invoiceIdList = InvoiceController.getInvoicesFromShift(counterLogin.getShiftId());
            ArrayList<IncomeBean> incomeBeans = new ArrayList();

            for (Integer i : invoiceIdList) {

//                CashPayment cashPayment = CashPaymentController.getCashPayment(i.intValue());
//                if (cashPayment != null) {
//                    cashAmount += cashPayment.getAmount() - cashPayment.getChangeAmount();
//                }
                ArrayList<CardPayment> cardPayments = CardPaymentController.getCardPayments(i.intValue());
                for (CardPayment cardPayment : cardPayments) {
                    switch (cardPayment.getCardType()) {
                        case CardPayment.AMEX:
                            amexAmount += cardPayment.getAmount();
                            break;
                        case CardPayment.MASTER:
                            masterAmount += cardPayment.getAmount();
                            break;
                        case CardPayment.VISA:
                            visaAmount += cardPayment.getAmount();
                            break;
                    }
                }

                CoopCreditPayment coopCreditPayment = CoopCreditPaymentController.getCoopCreditPayment(i.intValue());
                if (coopCreditPayment != null) {
                    coopCreditAmount += coopCreditPayment.getAmount();
                }

                CustomerVoucherPayment customerVoucherPayment = CustomerVoucherPaymentController.getCustomerVoucherPayment((i.intValue()));
                if (customerVoucherPayment != null) {
                    customerVoucherAmount += customerVoucherPayment.getAmount();
                }

                EmployeeVoucherPayment employeeVoucherPayment = EmployeeVoucherPaymentController.getEmployeeVoucherPayment(i.intValue());
                if (employeeVoucherPayment != null) {
                    employeeVoucherAmount += employeeVoucherPayment.getAmount();

                }

                PoshanaPayment poshanaPayment = PoshanaPaymentController.getPoshanaPayment(i.intValue());
                if (poshanaPayment != null) {
                    PoshanaAmount += poshanaPayment.getAmount();
                }

            }
            incomeBeans.add(new IncomeBean("Cash", counterLogin.getLogOffExpected(), counterLogin.getLogOffActual(), counterLogin.getLogOffActual() - counterLogin.getLogOffExpected()));

            incomeBeans.add(new IncomeBean("AMEX", amexAmount, amexAmount, 0));
            incomeBeans.add(new IncomeBean("MASTER", masterAmount, masterAmount, 0));
            incomeBeans.add(new IncomeBean("VISA", visaAmount, visaAmount, 0));

            incomeBeans.add(new IncomeBean("Coop Credit", coopCreditAmount, coopCreditAmount, 0));

            incomeBeans.add(new IncomeBean("Voucher (C)", customerVoucherAmount, customerVoucherAmount, 0));
            incomeBeans.add(new IncomeBean("Vocher (E)", employeeVoucherAmount, employeeVoucherAmount, 0));

            incomeBeans.add(new IncomeBean("Poshana ", PoshanaAmount, PoshanaAmount, 0));

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(incomeBeans);
            JasperPrint jasperPrint = JasperFillManager.fillReport(new ReportGenerator().getClass().getResourceAsStream(fileName), hm, beanColDataSource);

            if (jasperPrint != null) {
                JasperViewer.viewReport(jasperPrint, false);

                // Create a PDF exporter
//                JRPdfExporter exporter = new JRPdfExporter();
//
//                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName));
//                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//                exporter.setConfiguration(configuration);
//
//                exporter.exportReport();
            }
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateCashWithdrawalSlip(CashWithdrawal cashWithdrawal, CounterLogin counterLogin) {
        logger.debug("generateCashWithdrawalSlip invoked ");

        String fileName = "/model/report/pos/cashWithdrawal.jasper";
        String outFileName = "reports/output/pos/cashWithdrawal.pdf";
        FileUtil.mkdir("reports/output/pos/");
        HashMap hm = new HashMap();
        try {
            hm.put("withdrawalId", Utilities.convertKeyToString(cashWithdrawal.getWithdrawalId(), DatabaseInterface.CASH_WITHDRAWAL));
            hm.put("shiftId", Utilities.convertKeyToString(counterLogin.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            hm.put("cashierName", counterLogin.getUserName());
            hm.put("counter", counterLogin.getCounterId());
            hm.put("date", cashWithdrawal.getDate());
            hm.put("time", Utilities.convert24hTo12h(cashWithdrawal.getTime()));
            hm.put("amount", cashWithdrawal.getAmount());

            JasperPrint jasperPrint = JasperFillManager.fillReport(new ReportGenerator().getClass().getResourceAsStream(fileName), hm, new JREmptyDataSource());
            if (jasperPrint != null) {
                JasperViewer.viewReport(jasperPrint, false);

                // Create a PDF exporter
//                JRPdfExporter exporter = new JRPdfExporter();
//
//                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName));
//                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//                exporter.setConfiguration(configuration);
//
//                exporter.exportReport();
            }
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateInvoice(CounterLogin counterLogin, Invoice invoice) {
        logger.debug("generateInvoice invoked ");

        String fileName = "/model/report/pos/invoice.jasper";
        String outFileName = "reports/output/pos/invoice.pdf";
        FileUtil.mkdir("reports/output/pos/");
        HashMap hm = new HashMap();
        try {
            hm.put("invoiceNo", Utilities.convertKeyToString(invoice.getInvoiceNo(), DatabaseInterface.INVOICE));
            hm.put("cashier", counterLogin.getUserName());
            hm.put("date", invoice.getDate());
            hm.put("time", Utilities.convert24hTo12h(invoice.getTime()));
            hm.put("itemCount", invoice.getItemCount());

            ArrayList<InvoiceItemBean> invoiceItemBeans = new ArrayList<>();

            for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                InvoiceItemBean invoiceItemBean = new InvoiceItemBean(
                        invoiceItem.getDesc(),
                        Utilities.convertKeyToString(invoiceItem.getProductId(), DatabaseInterface.PRODUCT),
                        invoiceItem.getQty(),
                        invoiceItem.getUnitPrice(),
                        invoiceItem.getDiscount(),
                        invoiceItem.getQty() * invoiceItem.getUnitPrice() - invoiceItem.getDiscount()
                );

                invoiceItemBeans.add(invoiceItemBean);
            }

            for (Payment payment : invoice.getPayments()) {

                if (payment instanceof CashPayment) {
                    CashPayment cashPayment = (CashPayment) payment;

                    hm.put("payment_cash", cashPayment.getAmount());
                    hm.put("change", cashPayment.getChangeAmount());
                } else if (payment instanceof CardPayment) {
                    CardPayment cardPayment = (CardPayment) payment;
                    switch (cardPayment.getCardType()) {
                        case CardPayment.AMEX:
                            hm.put("payment_amex", cardPayment.getAmount());
                            break;
                        case CardPayment.MASTER:
                            hm.put("payment_master", cardPayment.getAmount());
                            break;
                        case CardPayment.VISA:
                            hm.put("payment_visa", cardPayment.getAmount());
                            break;
                    }

                } else if (payment instanceof CoopCreditPayment) {
                    CoopCreditPayment coopPayment = (CoopCreditPayment) payment;

                    hm.put("payment_coop", coopPayment.getAmount());
                } else if (payment instanceof PoshanaPayment) {
                    PoshanaPayment poshanaPayment = (PoshanaPayment) payment;

                    hm.put("payment_poshana", poshanaPayment.getAmount());
                } else if (payment instanceof CustomerVoucherPayment) {
                    CustomerVoucherPayment customerVoucherPayment = (CustomerVoucherPayment) payment;

                    hm.put("payment_voucher", customerVoucherPayment.getAmount());
                } else if (payment instanceof EmployeeVoucherPayment) {
                    EmployeeVoucherPayment employeeVoucherPayment = (EmployeeVoucherPayment) payment;

                    hm.put("payment_voucher", employeeVoucherPayment.getAmount());
                }
            }

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(invoiceItemBeans);
            JasperPrint jasperPrint = JasperFillManager.fillReport(new ReportGenerator().getClass().getResourceAsStream(fileName), hm, beanColDataSource);

            if (jasperPrint != null) {
                JasperViewer.viewReport(jasperPrint, false);
                // Create a PDF exporter
//                JRPdfExporter exporter = new JRPdfExporter();
//
//                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName));
//                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//                exporter.setConfiguration(configuration);
//
//                exporter.exportReport();
            }
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateRefund(CounterLogin counterLogin, Refund refund) {
        logger.debug("generateRefund invoked ");

        String fileName = "/model/report/pos/refund.jasper";
        String outFileName = "reports/output/pos/refund.pdf";
        FileUtil.mkdir("reports/output/pos/");
        HashMap hm = new HashMap();
        try {
            hm.put("invoiceNo", Utilities.convertKeyToString(refund.getInvoiceId(), DatabaseInterface.INVOICE));
            hm.put("refundNo", Utilities.convertKeyToString(refund.getRefundId(), DatabaseInterface.REFUND));
            hm.put("cashier", counterLogin.getUserName());
            hm.put("date", refund.getRefundDate());
            hm.put("time", Utilities.convert24hTo12h(refund.getRefundTime()));

            ArrayList<InvoiceItemBean> invoiceItemBeans = new ArrayList<>();
            int itemCount = 0;
            for (RefundItem refundItem : refund.getRefundItems()) {
                InvoiceItemBean invoiceItemBean = new InvoiceItemBean(
                        refundItem.getDesc(),
                        Utilities.convertKeyToString(refundItem.getProductId(), DatabaseInterface.PRODUCT),
                        refundItem.getQty(),
                        refundItem.getUnitPrice(),
                        refundItem.getDiscount(),
                        refundItem.getQty() * refundItem.getUnitPrice() - refundItem.getDiscount()
                );
                itemCount += 1;
                invoiceItemBeans.add(invoiceItemBean);
            }
            hm.put("itemCount", itemCount);

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(invoiceItemBeans);
            JasperPrint jasperPrint = JasperFillManager.fillReport(new ReportGenerator().getClass().getResourceAsStream(fileName), hm, beanColDataSource);

            if (jasperPrint != null) {
                JasperViewer.viewReport(jasperPrint, false);
                // Create a PDF exporter
//                JRPdfExporter exporter = new JRPdfExporter();
//
//                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName));
//                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//                exporter.setConfiguration(configuration);
//
//                exporter.exportReport();
            }
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateInvoiceCopy(Invoice invoice) {
        logger.debug("generateInvoiceCopy invoked ");

        String fileName = "/model/report/pos/invoice.jasper";
        String outFileName = "reports/output/pos/invoice_copy.pdf";
        FileUtil.mkdir("reports/output/pos/");
        HashMap hm = new HashMap();
        try {
            hm.put("invoiceNo", Utilities.convertKeyToString(invoice.getInvoiceNo(), DatabaseInterface.INVOICE));
            hm.put("cashier", CounterLoginController.getCounterLogin(invoice.getShiftId()).getUserName());
            hm.put("date", invoice.getDate());
            hm.put("time", Utilities.convert24hTo12h(invoice.getTime()));
            hm.put("itemCount", invoice.getItemCount());
            hm.put("billCopy", "Invoice copy");

            ArrayList<InvoiceItemBean> invoiceItemBeans = new ArrayList<>();

            for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                InvoiceItemBean invoiceItemBean = new InvoiceItemBean(
                        invoiceItem.getDesc(),
                        Utilities.convertKeyToString(invoiceItem.getProductId(), DatabaseInterface.PRODUCT),
                        invoiceItem.getQty(),
                        invoiceItem.getUnitPrice(),
                        invoiceItem.getDiscount(),
                        invoiceItem.getQty() * invoiceItem.getUnitPrice() - invoiceItem.getDiscount()
                );

                invoiceItemBeans.add(invoiceItemBean);
            }

            for (Payment payment : invoice.getPayments()) {

                if (payment instanceof CashPayment) {
                    CashPayment cashPayment = (CashPayment) payment;

                    hm.put("payment_cash", cashPayment.getAmount());
                    hm.put("change", cashPayment.getChangeAmount());
                } else if (payment instanceof CardPayment) {
                    CardPayment cardPayment = (CardPayment) payment;
                    switch (cardPayment.getCardType()) {
                        case CardPayment.AMEX:
                            hm.put("payment_amex", cardPayment.getAmount());
                            break;
                        case CardPayment.MASTER:
                            hm.put("payment_master", cardPayment.getAmount());
                            break;
                        case CardPayment.VISA:
                            hm.put("payment_visa", cardPayment.getAmount());
                            break;
                    }

                } else if (payment instanceof CoopCreditPayment) {
                    CoopCreditPayment coopPayment = (CoopCreditPayment) payment;

                    hm.put("payment_coop", coopPayment.getAmount());
                } else if (payment instanceof PoshanaPayment) {
                    PoshanaPayment poshanaPayment = (PoshanaPayment) payment;

                    hm.put("payment_poshana", poshanaPayment.getAmount());
                } else if (payment instanceof CustomerVoucherPayment) {
                    CustomerVoucherPayment customerVoucherPayment = (CustomerVoucherPayment) payment;

                    hm.put("payment_voucher", customerVoucherPayment.getAmount());
                } else if (payment instanceof EmployeeVoucherPayment) {
                    EmployeeVoucherPayment employeeVoucherPayment = (EmployeeVoucherPayment) payment;

                    hm.put("payment_voucher", employeeVoucherPayment.getAmount());
                }
            }

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(invoiceItemBeans);
            JasperPrint jasperPrint = JasperFillManager.fillReport(new ReportGenerator().getClass().getResourceAsStream(fileName), hm, beanColDataSource);

            if (jasperPrint != null) {
                JasperViewer.viewReport(jasperPrint, false);
                // Create a PDF exporter
//                JRPdfExporter exporter = new JRPdfExporter();
//
//                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName));
//                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//                exporter.setConfiguration(configuration);
//
//                exporter.exportReport();
            }
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }
}
