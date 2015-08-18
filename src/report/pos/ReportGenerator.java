package report.pos;

import controller.inventory.ProductController;
import controller.payments.CardPaymentController;
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
public final class ReportGenerator {

    private static final Logger logger = Logger.getLogger(ReportGenerator.class);

    private ReportGenerator() {

    }

    private static void showReport(JasperPrint jasperPrint) throws Exception {
        logger.debug("showReport invoked ");

        if (jasperPrint != null) {
            JasperViewer.viewReport(jasperPrint, false);
        }

    }

    private static void saveAsPDF(JasperPrint jasperPrint, String outFileName) throws Exception {
        logger.debug("saveAsPDF invoked ");

        if (jasperPrint != null && outFileName != null && !outFileName.isEmpty()) {
            FileUtil.mkdir("reports/output/pos/");

            java.util.Date date = new java.util.Date(System.currentTimeMillis());

            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
            String dateString = formatter.format(date);

            // Create a PDF exporter
            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName + "_" + dateString + ".pdf"));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);

            exporter.exportReport();
            logger.info("PDF file creadted");
        }
    }

    public static void generateCashierSignOnSlip(CounterLogin counterLogin) {
        logger.debug("generateCashierSignOnSlip invoked ");

        String jasperFileName = "/model/report/pos/cashierSignOn.jasper";
        String outFileName = "reports/output/pos/cashierSignOn";

        HashMap hashMap = new HashMap();
        try {
            hashMap.put("shiftNo", Utilities.convertKeyToString(counterLogin.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            hashMap.put("cashierName", counterLogin.getUserName());
            hashMap.put("counter", counterLogin.getCounterId());
            hashMap.put("onDate", counterLogin.getLogInDate());
            hashMap.put("onTime", Utilities.convert24hTo12h(counterLogin.getLogInTime()));
            hashMap.put("amount", counterLogin.getInitialAmount());

            JasperPrint jasperPrint = JasperFillManager.fillReport(ReportGenerator.class.getResourceAsStream(jasperFileName), hashMap, new JREmptyDataSource());

            showReport(jasperPrint);
            saveAsPDF(jasperPrint, outFileName);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateCashierSignOffSlip(CounterLogin counterLogin) {
        logger.debug("generateCashierSignOffSlip invoked ");

        String jasperFileName = "/model/report/pos/cashierSignOff.jasper";
        String outFileName = "reports/output/pos/cashierSignOff";

        HashMap hashMap = new HashMap();
        try {
            hashMap.put("shiftNo", Utilities.convertKeyToString(counterLogin.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            hashMap.put("cashierName", counterLogin.getUserName());
            hashMap.put("counter", counterLogin.getCounterId());
            hashMap.put("onDate", counterLogin.getLogInDate());
            hashMap.put("onTime", Utilities.convert24hTo12h(counterLogin.getLogInTime()));
            hashMap.put("initialAmount", counterLogin.getInitialAmount());
            hashMap.put("finalAmount", counterLogin.getLogOffActual());
            hashMap.put("offTime", Utilities.convert24hTo12h(counterLogin.getLogOffTime()));
            hashMap.put("offDate", counterLogin.getLogOffDate());
            hashMap.put("cashWithdrawal", counterLogin.getCashWithdrawals());

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
            hashMap.put("amexCard", amexAmount);
            hashMap.put("masterCard", masterAmount);
            hashMap.put("visaCard", visaAmount);

            ArrayList<CancelledItemsBean> cancelledItemsBeans = new ArrayList<>();
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
                jasperPrint = JasperFillManager.fillReport(ReportGenerator.class.getResourceAsStream(jasperFileName), hashMap, beanColDataSource);
            } else {
                jasperPrint = JasperFillManager.fillReport(ReportGenerator.class.getResourceAsStream(jasperFileName), hashMap, new JREmptyDataSource());
            }

            showReport(jasperPrint);
            saveAsPDF(jasperPrint, outFileName);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateManagerSignOffSlip(CounterLogin counterLogin) {
        logger.debug("generateManagerSignOffSlip invoked ");

        String jasperFileName = "/model/report/pos/managerSignOff.jasper";
        String outFileName = "reports/output/pos/managerSignOff";

        HashMap hashMap = new HashMap();
        try {
            hashMap.put("shiftNo", Utilities.convertKeyToString(counterLogin.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            hashMap.put("cashierName", counterLogin.getUserName());
            hashMap.put("counter", counterLogin.getCounterId());
            hashMap.put("initialAmount", counterLogin.getInitialAmount());
            hashMap.put("cashWithdrawal", counterLogin.getCashWithdrawals());
            hashMap.put("onDate", counterLogin.getLogInDate());
            hashMap.put("onTime", Utilities.convert24hTo12h(counterLogin.getLogInTime()));
            hashMap.put("offTime", Utilities.convert24hTo12h(counterLogin.getLogOffTime()));
            hashMap.put("offDate", counterLogin.getLogOffDate());
            hashMap.put("date", Utilities.getStringDate(Utilities.getCurrentDate()));
            hashMap.put("time", Utilities.getCurrentTime(false));

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
            JasperPrint jasperPrint = JasperFillManager.fillReport(ReportGenerator.class.getResourceAsStream(jasperFileName), hashMap, beanColDataSource);

            showReport(jasperPrint);
            saveAsPDF(jasperPrint, outFileName);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateCashWithdrawalSlip(CashWithdrawal cashWithdrawal, CounterLogin counterLogin) {
        logger.debug("generateCashWithdrawalSlip invoked ");

        String jasperFileName = "/model/report/pos/cashWithdrawal.jasper";
        String outFileName = "reports/output/pos/cashWithdrawal";

        HashMap hashMap = new HashMap();
        try {
            hashMap.put("withdrawalId", Utilities.convertKeyToString(cashWithdrawal.getWithdrawalId(), DatabaseInterface.CASH_WITHDRAWAL));
            hashMap.put("shiftId", Utilities.convertKeyToString(counterLogin.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            hashMap.put("cashierName", counterLogin.getUserName());
            hashMap.put("counter", counterLogin.getCounterId());
            hashMap.put("date", cashWithdrawal.getDate());
            hashMap.put("time", Utilities.convert24hTo12h(cashWithdrawal.getTime()));
            hashMap.put("amount", cashWithdrawal.getAmount());

            JasperPrint jasperPrint = JasperFillManager.fillReport(ReportGenerator.class.getResourceAsStream(jasperFileName), hashMap, new JREmptyDataSource());

            showReport(jasperPrint);
            saveAsPDF(jasperPrint, outFileName);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateInvoice(CounterLogin counterLogin, Invoice invoice) {
        logger.debug("generateInvoice invoked ");

        String jasperFileName = "/model/report/pos/invoice.jasper";
        String outFileName = "reports/output/pos/invoice";

        HashMap hashMap = new HashMap();
        try {
            hashMap.put("invoiceNo", Utilities.convertKeyToString(invoice.getInvoiceNo(), DatabaseInterface.INVOICE));
            hashMap.put("cashier", counterLogin.getUserName());
            hashMap.put("date", invoice.getDate());
            hashMap.put("time", Utilities.convert24hTo12h(invoice.getTime()));
            hashMap.put("itemCount", invoice.getItemCount());

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

                    hashMap.put("payment_cash", cashPayment.getAmount());
                    hashMap.put("change", cashPayment.getChangeAmount());
                } else if (payment instanceof CardPayment) {
                    CardPayment cardPayment = (CardPayment) payment;
                    switch (cardPayment.getCardType()) {
                        case CardPayment.AMEX:
                            hashMap.put("payment_amex", cardPayment.getAmount());
                            break;
                        case CardPayment.MASTER:
                            hashMap.put("payment_master", cardPayment.getAmount());
                            break;
                        case CardPayment.VISA:
                            hashMap.put("payment_visa", cardPayment.getAmount());
                            break;
                    }

                } else if (payment instanceof CoopCreditPayment) {
                    CoopCreditPayment coopPayment = (CoopCreditPayment) payment;

                    hashMap.put("payment_coop", coopPayment.getAmount());
                } else if (payment instanceof PoshanaPayment) {
                    PoshanaPayment poshanaPayment = (PoshanaPayment) payment;

                    hashMap.put("payment_poshana", poshanaPayment.getAmount());
                } else if (payment instanceof CustomerVoucherPayment) {
                    CustomerVoucherPayment customerVoucherPayment = (CustomerVoucherPayment) payment;
                    hashMap.put("payment_voucher", customerVoucherPayment.getAmount());

                } else if (payment instanceof EmployeeVoucherPayment) {
                    EmployeeVoucherPayment employeeVoucherPayment = (EmployeeVoucherPayment) payment;

                    hashMap.put("payment_voucher", employeeVoucherPayment.getAmount());
                }
            }

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(invoiceItemBeans);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ReportGenerator.class.getResourceAsStream(jasperFileName), hashMap, beanColDataSource);

            showReport(jasperPrint);
            saveAsPDF(jasperPrint, outFileName);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateRefund(CounterLogin counterLogin, Refund refund) {
        logger.debug("generateRefund invoked ");

        String jasperFileName = "/model/report/pos/refund.jasper";
        String outFileName = "reports/output/pos/refund";

        HashMap hashMap = new HashMap();
        try {
            hashMap.put("invoiceNo", Utilities.convertKeyToString(refund.getInvoiceId(), DatabaseInterface.INVOICE));
            hashMap.put("refundNo", Utilities.convertKeyToString(refund.getRefundId(), DatabaseInterface.REFUND));
            hashMap.put("cashier", counterLogin.getUserName());
            hashMap.put("date", refund.getRefundDate());
            hashMap.put("time", Utilities.convert24hTo12h(refund.getRefundTime()));

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
            hashMap.put("itemCount", itemCount);

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(invoiceItemBeans);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ReportGenerator.class.getResourceAsStream(jasperFileName), hashMap, beanColDataSource);

            showReport(jasperPrint);
            saveAsPDF(jasperPrint, outFileName);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

    public static void generateInvoiceCopy(Invoice invoice) {
        logger.debug("generateInvoiceCopy invoked ");

        String jasperFileName = "/model/report/pos/invoice.jasper";
        String outFileName = "reports/output/pos/invoice_copy";

        HashMap hashMap = new HashMap();
        try {
            hashMap.put("invoiceNo", Utilities.convertKeyToString(invoice.getInvoiceNo(), DatabaseInterface.INVOICE));
            hashMap.put("cashier", CounterLoginController.getCounterLogin(invoice.getShiftId()).getUserName());
            hashMap.put("date", invoice.getDate());
            hashMap.put("time", Utilities.convert24hTo12h(invoice.getTime()));
            hashMap.put("itemCount", invoice.getItemCount());
            hashMap.put("billCopy", "Invoice copy");

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

                    hashMap.put("payment_cash", cashPayment.getAmount());
                    hashMap.put("change", cashPayment.getChangeAmount());
                } else if (payment instanceof CardPayment) {
                    CardPayment cardPayment = (CardPayment) payment;
                    switch (cardPayment.getCardType()) {
                        case CardPayment.AMEX:
                            hashMap.put("payment_amex", cardPayment.getAmount());
                            break;
                        case CardPayment.MASTER:
                            hashMap.put("payment_master", cardPayment.getAmount());
                            break;
                        case CardPayment.VISA:
                            hashMap.put("payment_visa", cardPayment.getAmount());
                            break;
                    }

                } else if (payment instanceof CoopCreditPayment) {
                    CoopCreditPayment coopPayment = (CoopCreditPayment) payment;

                    hashMap.put("payment_coop", coopPayment.getAmount());
                } else if (payment instanceof PoshanaPayment) {
                    PoshanaPayment poshanaPayment = (PoshanaPayment) payment;

                    hashMap.put("payment_poshana", poshanaPayment.getAmount());
                } else if (payment instanceof CustomerVoucherPayment) {
                    CustomerVoucherPayment customerVoucherPayment = (CustomerVoucherPayment) payment;

                    hashMap.put("payment_voucher", customerVoucherPayment.getAmount());
                } else if (payment instanceof EmployeeVoucherPayment) {
                    EmployeeVoucherPayment employeeVoucherPayment = (EmployeeVoucherPayment) payment;

                    hashMap.put("payment_voucher", employeeVoucherPayment.getAmount());
                }
            }

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(invoiceItemBeans);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ReportGenerator.class.getResourceAsStream(jasperFileName), hashMap, beanColDataSource);

            showReport(jasperPrint);
            saveAsPDF(jasperPrint, outFileName);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }
}
