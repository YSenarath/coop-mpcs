package ui.view.pos;

import controller.credit.CustomerCreditController;
import controller.credit.EmployeeCreditController;
import controller.inventory.BatchController;
import controller.inventory.BatchDiscountController;
import controller.inventory.CategoryController;
import controller.inventory.CategoryDiscountController;
import controller.inventory.ProductController;
import controller.pos.InvoiceController;
import controller.pos.TransactionController;
import controller.settings.SettingsController;
import database.connector.DatabaseInterface;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;
import model.creditManagement.CreditCustomer;
import model.creditManagement.Employee;
import model.inventory.Batch;
import model.inventory.BatchDiscount;
import model.inventory.Category;
import model.inventory.CategoryDiscount;
import model.inventory.Product;
import model.pos.Memento;
import model.pos.payment.CardPayment;
import model.pos.payment.CashPayment;
import model.pos.item.Invoice;
import model.pos.item.InvoiceItem;
import model.pos.payment.CoopCreditPayment;
import model.pos.payment.CustomerVoucherPayment;
import model.pos.payment.EmployeeVoucherPayment;
import model.pos.payment.Payment;
import model.pos.payment.PoshanaPayment;
import org.apache.log4j.Logger;
import report.pos.ReportGenerator;
import util.CharactorLimitDocument;
import util.CurrencyFilter;
import util.IntegerFilter;
import util.KeyValueContainer;
import util.NameFilter;
import util.QuantityFilter;
import util.Utilities;
import static util.Utilities.doubleFormatComponentText;

class InvoiceInternalInterface extends javax.swing.JInternalFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(InvoiceInternalInterface.class);

    private final POSMDIInterface parent;
    private final JDesktopPane desktopPane;

    private SearchItemInterface searchItemInterface;
    private SelectPriceInterface selectPriceInterface;

    private final DefaultComboBoxModel productComboBoxModel;

    private final ActionListener productCodeListner;
    private final DefaultTableModel invoiceItemTableModel;
    private final DefaultTableModel invoicePaymentsTableModel;

    //Key binding
    private InputMap itemAddPanelInputMap;
    private ActionMap itemAddPanelActionMap;
    private InputMap paymentPanelInputMap;
    private ActionMap paymentPanelActionMap;
    //Glass pane
    private final JPanel glassPanel;
    private final JLabel padding;

    //Invoice
    private Invoice invoice;
    private Product processingProduct;
    private HashMap<String, Product> availableProductMap;

    //Yasara-Credit
    private final DefaultComboBoxModel employeeComboBoxModel;
    private final DefaultComboBoxModel coopCustomerComboBoxModel;
    ActionListener coopCustomerListner;
    private HashMap<Integer, CreditCustomer> customers;

    private final int PRODUCT_ID_COLUMN = 0;
    private final int BATCH_ID_COLUMN = 1;
    private final int DESCRIPTION_COLUMN = 3;
    private final int UNIT_PRICE_COLUMN = 4;
    private final int UNIT_QTY_COLUMN = 5;
    private final int NET_DISCOUNT_COLUMN = 6;
    private final int NET_TOTAL_COLUMN = 7;

    private final int PAYMENT_OPTION_COLUMN = 0;
    private final int PAYMENT_AMOUNT_COLUMN = 1;
    private final int PAYMENT_OFFSET_0_COLUMN = 2;
    private final int PAYMENT_OFFSET_1_COLUMN = 3;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public InvoiceInternalInterface(POSMDIInterface parent, JDesktopPane desktopPane) {
        logger.debug("InvoiceInternalInterface constructor invoked");

        initComponents();
        this.parent = parent;
        this.desktopPane = desktopPane;
        this.selectPriceInterface = null;
        this.searchItemInterface = null;

        this.productComboBoxModel = new DefaultComboBoxModel();
        this.invoiceItemTableModel = (DefaultTableModel) invoiceItemTable.getModel();
        this.invoicePaymentsTableModel = (DefaultTableModel) invoicePaymentsTable.getModel();

        this.invoice = null;
        this.processingProduct = null;
        productCodeListner = (ActionEvent e) -> {
            showProductDetails();
        };
        itemCodeComboBox.addActionListener(productCodeListner);

        ((PlainDocument) txtPrice.getDocument()).setDocumentFilter(new CurrencyFilter());
        ((PlainDocument) txtQty.getDocument()).setDocumentFilter(new QuantityFilter());

        ((PlainDocument) txtCardPaymentAmount.getDocument()).setDocumentFilter(new CurrencyFilter());
        ((PlainDocument) txtCashPaymentAmount.getDocument()).setDocumentFilter(new CurrencyFilter());

        txtPrice.setEnabled(false);
        txtQty.setEnabled(false);
        lblUnit.setText("");

        //yasara
        this.employeeComboBoxModel = (DefaultComboBoxModel) (voucherEmployeeNameComboBox.getModel());
        this.coopCustomerComboBoxModel = (DefaultComboBoxModel) coopCustomerNameComboBox.getModel();
        coopCustomerListner = (ActionEvent e) -> {
            setCoopCreditMax();
            coopCreditAmountTxt.setEnabled(true);
        };
        coopCustomerNameComboBox.addActionListener(coopCustomerListner);

        //Yasara
        ((PlainDocument) coopCreditAmountTxt.getDocument()).setDocumentFilter(new CurrencyFilter());

        ((PlainDocument) poshanaCustomerNameTxt.getDocument()).setDocumentFilter(new NameFilter());
        ((PlainDocument) poshanaIdTxt.getDocument()).setDocumentFilter(new IntegerFilter());
        ((PlainDocument) poshanaPaymentAmountTxt.getDocument()).setDocumentFilter(new CurrencyFilter());

        ((PlainDocument) voucherCustomerIdTxt.getDocument()).setDocumentFilter(new IntegerFilter());
        ((PlainDocument) voucherCustomerAmountTxt.getDocument()).setDocumentFilter(new CurrencyFilter());

        ((PlainDocument) voucherEmployeeVoucherAmount.getDocument()).setDocumentFilter(new CurrencyFilter());

        ((DefaultTableCellRenderer) invoiceItemTable.getDefaultRenderer(Object.class)).setHorizontalAlignment(JLabel.RIGHT);
        ((DefaultTableCellRenderer) invoicePaymentsTable.getDefaultRenderer(Object.class)).setHorizontalAlignment(JLabel.RIGHT);

        this.glassPanel = new JPanel(new GridLayout(0, 1));
        this.padding = new JLabel();

        glassPanel.setOpaque(false);
        glassPanel.add(padding);

        glassPanel.addMouseListener(
                new MouseAdapter() {
                });
        glassPanel.addMouseMotionListener(
                new MouseMotionAdapter() {
                });
        glassPanel.addKeyListener(
                new KeyAdapter() {
                });

        // make sure the focus won't leave the glass pane
        glassPanel.setFocusCycleRoot(true);
        setGlassPane(glassPanel);
        performKeyBinding();

        initializeInvoice();

        loadSellebleProducts();

    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Key Bindings "> 
    private void performKeyBinding() {
        logger.debug("performKeyBinding invoked");

        itemAddPanelInputMap = itemAddPanel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        itemAddPanelActionMap = itemAddPanel.getActionMap();

        paymentPanelInputMap = paymentPanel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        paymentPanelActionMap = paymentPanel.getActionMap();

        putKeyBinds();
    }

    private void putKeyBinds() {
        logger.debug("putKeyBinds invoked");

        itemAddPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "doEnterItemAction");
        itemAddPanelActionMap.put("doEnterItemAction", new keyBindingAction("EnterItem"));

        itemAddPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "doDeleteAction");
        itemAddPanelActionMap.put("doDeleteAction", new keyBindingAction("Delete"));

        itemAddPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "doEscapeAction");
        itemAddPanelActionMap.put("doEscapeAction", new keyBindingAction("Escape"));

        itemAddPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "doF5Action");
        itemAddPanelActionMap.put("doF5Action", new keyBindingAction("F5"));

        itemAddPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), "doF9Action");
        itemAddPanelActionMap.put("doF9Action", new keyBindingAction("F9"));

        itemAddPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "doF10Action");
        itemAddPanelActionMap.put("doF10Action", new keyBindingAction("F10"));

        itemAddPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "doF12Action");
        itemAddPanelActionMap.put("doF12Action", new keyBindingAction("F12"));

        paymentPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "doF3Action");
        paymentPanelActionMap.put("doF3Action", new keyBindingAction("F3"));

        paymentPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), "doF9Action");
        paymentPanelActionMap.put("doF9Action", new keyBindingAction("F9"));

        paymentPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "doF10Action");
        paymentPanelActionMap.put("doF10Action", new keyBindingAction("F10"));

        // paymentPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "doConfirmAction");
        // paymentPanelActionMap.put("doConfirmAction", new keyBindingAction("Confirm"));
    }

    private class keyBindingAction extends AbstractAction {

        private final String cmd;

        public keyBindingAction(String cmd) {
            this.cmd = cmd;
        }

        @Override
        public void actionPerformed(ActionEvent tf) {
            if (cmd.equalsIgnoreCase("EnterItem")) {
                logger.debug("InvoiceInternalInterface Interface - EnterItem Pressed ");
                btnAddItem.doClick();
            } else if (cmd.equalsIgnoreCase("Confirm")) {
                logger.debug("InvoiceInternalInterface Interface - Confirm Pressed ");
                btnConfirm.doClick();
            } else if (cmd.equalsIgnoreCase("Delete")) {
                logger.debug("InvoiceInternalInterface Interface - Delete Pressed ");
                btnDeleteItem.doClick();
            } else if (cmd.equalsIgnoreCase("Escape")) {
                logger.debug("InvoiceInternalInterface Interface - Escape Pressed ");
                btnClearItem.doClick();
            } else if (cmd.equalsIgnoreCase("F3")) {
                logger.debug("InvoiceInternalInterface Interface - F3 Pressed ");
                btnAddPayment.doClick();
            } else if (cmd.equalsIgnoreCase("F5")) {
                logger.debug("InvoiceInternalInterface Interface - F5 Pressed ");
                btnSearch.doClick();
            } else if (cmd.equalsIgnoreCase("F12")) {
                logger.debug("InvoiceInternalInterface Interface - F12 Pressed ");
                btnPayment.doClick();
            } else if (cmd.equalsIgnoreCase("F9")) {
                logger.debug("InvoiceInternalInterface Interface - F9 Pressed ");
                parent.holdBtnPerformClick();
            } else if (cmd.equalsIgnoreCase("F10")) {
                logger.debug("InvoiceInternalInterface Interface - F10 Pressed ");
                parent.restoreBtnPerformclick();
            }
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Helper Methods">
    //Get last billID
    //Price selection ui will call this to set the price
    public void setProductBatch(Batch selectedBatch) {
        logger.debug("setProductBatch invoked");

        //See if product expired if so give prompt to accept or reject
        if (isExpired(selectedBatch.getExpirationDate())) {
            logger.warn("Item Expired");
            Utilities.showMsgBox("This batch expired on " + selectedBatch.getExpirationDate(), "", JOptionPane.WARNING_MESSAGE);
            invoiceClearProductinfo();
            itemCodeComboBox.requestFocus();
        } else {
            processingProduct.setSelectedBatch(selectedBatch);
            setPropFromBatch();
            txtQty.requestFocus();
        }
    }

    //Disable the glassPanel pane
    public void disableGlassPane(boolean disableSearchInterface) {
        logger.debug("disableGlassPane invoked");

        putKeyBinds();

        glassPanel.setVisible(false);
        itemCodeComboBox.requestFocus();
        if (disableSearchInterface && searchItemInterface != null) {
            searchItemInterface.disableGlassPane();
        }
    }

    //Enable the glassPanel pane
    public void enableGlassPane(boolean enableSearchInterface) {
        logger.debug("enableGlassPane invoked");

        glassPanel.setVisible(true);//Disable this UI

        itemAddPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        itemAddPanelActionMap.remove("doEnterItemAction");

        itemAddPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        itemAddPanelActionMap.remove("doDeleteAction");

        itemAddPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        itemAddPanelActionMap.remove("doEscapeAction");

        itemAddPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        itemAddPanelActionMap.remove("doF5Action");

        itemAddPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        itemAddPanelActionMap.remove("doF9Action");

        itemAddPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
        itemAddPanelActionMap.remove("doF10Action");

        itemAddPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
        itemAddPanelActionMap.remove("doF12Action");

        paymentPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        paymentPanelActionMap.remove("doF3Action");

        paymentPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        paymentPanelActionMap.remove("doF9Action");

        paymentPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
        paymentPanelActionMap.remove("doF10Action");

        //  paymentPanelInputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        // paymentPanelActionMap.remove("doConfirmAction");
        if (enableSearchInterface && searchItemInterface != null) {
            searchItemInterface.enableGlassPane();
        }
        padding.requestFocus();  // required to trap key events
    }

    //Clear the current product fields in bill add item to bill
    public void invoiceClearProductinfo() {
        logger.debug("invoiceClearProductinfo invoked");

        processingProduct = null;
        itemCodeComboBox.setSelectedIndex(-1);
        itemCodeComboBox.setSelectedIndex(-1);
        itemCodeComboBox.requestFocus();

        txtProductName.setText("");
        txtProductDesc.setText("");
        txtAvailableQty.setText("");
        txtPrice.setText("");
        txtQty.setText("");
        txtPrice.setEnabled(false);
        txtQty.setEnabled(false);
        lblUnit.setText("");

    }

    //Get table data to hold sale
    public Memento saveToMemento() {
        logger.debug("saveToMemento invoked");

        //Get info from item ,and payment table
        //Get is member status
        if (invoiceItemTableModel.getRowCount() > 0) {
            logger.info("Saving started");

            Memento memento = new Memento(chkMember.isSelected(), getTableData(invoiceItemTable), getTableData(invoicePaymentsTable));
            resetInvoice();
            showAddItemPanel();
            logger.info("Saving ended");
            // parent.setHoldBtn(false);
            //parent.setRestoreBtn(true);
            return memento;
        } else {
            Utilities.showMsgBox("Empty sale cannot be put on hold", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
        return null;
    }

    //Reset the invoice
    public boolean restoreFromMemento(Memento memento) {
        logger.debug("restoreFromMemento invoked");

        logger.info("Restore started");
        //If in middle of transaction ask to finish or cancel current one and restore

        if (invoiceItemTableModel.getRowCount() > 0) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Clear current transaction and restore sale ?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        resetInvoice();
        showAddItemPanel();

        if (memento.isMember()) {
            chkMember.setSelected(true);
            convertToMemberInvoice();
        }

        setTableData(invoiceItemTable, memento.getItemData());
        setTableData(invoicePaymentsTable, memento.getPaymentData());

        //Remove voucher and poshana option if found on table
        for (int row = 0; row < invoicePaymentsTable.getRowCount(); row++) {
            if (invoicePaymentsTable.getValueAt(row, PAYMENT_OPTION_COLUMN).toString().equals(Payment.POSHANA)) {
                paymentOptionComboBox.removeItem(Payment.POSHANA);
            }
            if (invoicePaymentsTable.getValueAt(row, PAYMENT_OPTION_COLUMN).toString().equals(Payment.VOUCHER)) {
                paymentOptionComboBox.removeItem(Payment.VOUCHER);
            }
        }

        calculateItemParameters();
        calculatePaymentParameters();

        // parent.setHoldBtn(true);
        // parent.setRestoreBtn(false);
        logger.info("Restore ended");
        return true;
    }

    private Object[][] getTableData(JTable table) {
        logger.debug("getTableData invoked");

        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
        Object[][] tableData = new Object[nRow][nCol];
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                tableData[i][j] = dtm.getValueAt(i, j);
            }
        }
        return tableData;
    }

    private void setTableData(JTable table, Object[][] data) {
        logger.debug("getTableData invoked");

        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int nRow = data.length;
        for (int i = 0; i < nRow; i++) {
            dtm.addRow(data[i]);
        }
    }

    private int getLastInvoicelId() throws SQLException {
        logger.debug("getLastInvoicelId invoked");

        int lastInvoice = InvoiceController.getLastInvoiceId();
        if (lastInvoice > 0) {
            return lastInvoice;
        }
        return 0;
    }

    private static ArrayList<Product> getAllSellebleProducts() throws SQLException {
        logger.debug("getAllSellebleProducts invoked");

        ArrayList<Product> products = ProductController.getAllAvailableProducts();
        ArrayList<Product> availableProducts = new ArrayList();

        for (Product product : products) {
            product.setBatches(BatchController.getBatches(product.getProductId()));
            if (product.getBatches().size() > 0) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

    //Check if produce expired
    private boolean isExpired(Date expDate) {
        logger.debug("isExpired invoked");

        return !Utilities.isDateBeforeLimit(Utilities.getCurrentDate(), expDate);
    }

    //Get Category Discount
    private double getCategoryDiscount(double unitPrice, double qty) throws SQLException {
        logger.debug("getCategoryDiscount invoked");

        //get category and check if discount available ,if so get the category discount
        Category category = CategoryController.getCategory(processingProduct.getDepartmentId(), processingProduct.getCategoryId());
        if (category.isDiscounted()) {
            CategoryDiscount categoryDiscount = CategoryDiscountController.getCategoryDiscount(category.getDepartmentId(), category.getCategoryId());

            //if today is in discount date range
            if (categoryDiscount != null && (util.Utilities.isDateBetweenRange(util.Utilities.getCurrentDate(), categoryDiscount.getStartDate(), categoryDiscount.getEndDate()))) {
                //check if it is members only 
                if ((categoryDiscount.isMembersOnly() && chkMember.isSelected()) || (!categoryDiscount.isMembersOnly())) {
                    try {
                        double categoryDiscountPercent = categoryDiscount.getDiscount() / 100;
                        double discountEligibleQty;
                        if (categoryDiscount.isPromotional()) {
                            discountEligibleQty = qty;
                        } else {
                            double discountQtyDivider = categoryDiscount.getQuantity();
                            discountEligibleQty = Math.floor(qty / discountQtyDivider) * discountQtyDivider;

                        }
                        double totalCategoryDiscount = discountEligibleQty * unitPrice * categoryDiscountPercent;
                        logger.info("Category discount: " + totalCategoryDiscount);
                        return totalCategoryDiscount;

                    } catch (Exception ex) {
                        logger.error("Category Discount Exception : " + ex.getMessage(), ex);
                    }

                } else {
                    logger.info("Category discount only for member");
                }

            } else {
                logger.info("No category discounts found");
            }
        } else {
            logger.info("Category discount disabled");
        }
        return 0;
    }

    //Get Batch Discount
    private double getBatchDiscount(double unitPrice, double qty) throws SQLException {
        logger.debug("getBatchDiscount invoked");

        Batch selectedBatch = processingProduct.getSelectedBatch();
        if (selectedBatch.isDiscounted()) {
            BatchDiscount batchDiscount = BatchDiscountController.getBatchDiscount(selectedBatch.getProductId(), selectedBatch.getBatchId());
            if (batchDiscount != null
                    && (util.Utilities.isDateBetweenRange(util.Utilities.getCurrentDate(), batchDiscount.getStartDate(), batchDiscount.getEndDate()))) {

                // processingProduct.getSelectedBatch().setBatchDiscount(batchDiscount);
                //check if it is members only 
                if ((batchDiscount.isMembersOnly() && chkMember.isSelected()) || (!batchDiscount.isMembersOnly())) {
                    try {
                        double batchDiscountPercent = batchDiscount.getDiscount() / 100;
                        double discountEligibleQty;
                        if (batchDiscount.isPromotional()) {
                            discountEligibleQty = qty;
                        } else {
                            double discountQtyDivider = batchDiscount.getQuantity();
                            discountEligibleQty = Math.floor(qty / discountQtyDivider) * discountQtyDivider;
                        }
                        double totlalBatchDiscount = discountEligibleQty * unitPrice * batchDiscountPercent;
                        logger.info("Batch discount: " + totlalBatchDiscount);
                        return totlalBatchDiscount;

                    } catch (Exception ex) {
                        logger.error("Batch Discount Exception : " + ex.getMessage(), ex);
                    }
                } else {
                    logger.info("Bath discount only for member");
                }

            } else {
                logger.info("No batch discounts found");
            }
        } else {
            logger.info("Batch discount disabled");
        }
        return 0;
    }

    //Clean item add card
    private void cleanItemAddUI() {
        logger.debug("cleanItemAddUI invoked");

        invoiceClearProductinfo();
        invoiceItemTableModel.setRowCount(0);
        lblItemCount.setText("0");
        lblItemDiscounts.setText("0.00");
        lblNetTotal.setText("0.00");
    }

    //Clean payments card 
    private void cleanPaymentsUI() {
        logger.debug("cleanPaymentsUI invoked");

        invoicePaymentsTableModel.setRowCount(0);

        txtCashPaymentAmount.setText("");

        cardTypeComboBox.setSelectedIndex(-1);
        txtcardNo.setText("");
        txtcardNo.setEnabled(false);
        txtcardNo.setEditable(false);
        txtCardPaymentAmount.setText("0.00");
        txtCardPaymentAmount.setEnabled(false);

        lblBillValueVal.setText("0.00");
        lblChangeVal.setText("0.00");
        lblTotalVal.setText("0.00");

        //Yasara
        paymentOptionComboBox.removeAllItems();
        paymentOptionComboBox.addItem(Payment.CASH);
        paymentOptionComboBox.addItem(Payment.CREDIT_CARD);
        paymentOptionComboBox.addItem(Payment.COOP_CREDIT);
        paymentOptionComboBox.addItem(Payment.POSHANA);
        paymentOptionComboBox.addItem(Payment.VOUCHER);

        coopCreditAmountTxt.setText("0.00");
        coopCreditAmountTxt.setEnabled(false);
        redeemableAmountTxt.setText("0.00");
        coopCustomerNameComboBox.setSelectedIndex(-1);

        poshanaIdTxt.setText("");
        poshanaCustomerNameTxt.setText("");

        customerRadioButton.setSelected(true);
        employeeRadioButton.setSelected(false);
        CardLayout voucherPaymentCard = (CardLayout) voucherCard.getLayout();
        voucherPaymentCard.show(voucherCard, "customerCard");

        voucherEmployeeVoucherAmount.setText("0.00");
        voucherEmployeeNameComboBox.setSelectedIndex(-1);

        voucherCustomerIdTxt.setText("");
        voucherCustomerAmountTxt.setText("0.00");
    }

    //Get the product price from the appropriate batch
    private void setPropFromBatch() {
        logger.debug("setPropFromBatch invoked");

        txtPrice.setText(String.format("%.2f", processingProduct.getSelectedBatch().getUnit_price()));
        txtAvailableQty.setText(String.format("%.2f", processingProduct.getSelectedBatch().getRecievedQuantity() - processingProduct.getSelectedBatch().getSoldQty()));

    }

    //Recalculate the invoice item parameters
    private void calculateItemParameters() {
        logger.debug("calculateInvoiceParameters invoked");

        double netTotal = 0;
        double netDiscounts = 0;
        int totalItemCount = 0;

        for (int row = 0; row < invoiceItemTable.getRowCount(); row++) {
            netDiscounts += Double.parseDouble(invoiceItemTable.getValueAt(row, NET_DISCOUNT_COLUMN).toString());
            netTotal += Double.parseDouble(invoiceItemTable.getValueAt(row, NET_TOTAL_COLUMN).toString());
            totalItemCount += 1;
        }

        invoice.setNetTotal(netTotal);
        invoice.setDiscount(netDiscounts);
        invoice.setItemCount(totalItemCount);

        lblNetTotal.setText(String.format("%.2f", invoice.getNetTotal()));
        lblItemDiscounts.setText(String.format("%.2f", invoice.getDiscount()));
        lblItemCount.setText(String.format("%d", invoice.getItemCount()));
    }

    //Recalculate the invoice paymentparameters
    private void calculatePaymentParameters() {
        logger.debug("calculatePaymentParameters invoked");

        double totalAmountPaid = 0;
        for (int row = 0; row < invoicePaymentsTable.getRowCount(); row++) {
            totalAmountPaid += Double.parseDouble(invoicePaymentsTable.getValueAt(row, PAYMENT_AMOUNT_COLUMN).toString());
        }
        invoice.setAmountPaid(totalAmountPaid);
        lblTotalVal.setText(String.format("%.2f", invoice.getAmountPaid()));
        lblChangeVal.setText(String.format("%.2f", invoice.getNetTotal() < invoice.getAmountPaid() ? invoice.getAmountPaid() - invoice.getNetTotal() : 0.00));

        double remainingAmount = invoice.getNetTotal() - invoice.getAmountPaid();
        logger.info("Remaining amount : " + remainingAmount);

        txtCashPaymentAmount.setText(remainingAmount > 0 ? String.format("%.2f", remainingAmount) : "0.00");
        txtCardPaymentAmount.setText(remainingAmount > 0 ? String.format("%.2f", remainingAmount) : "0.00");

        coopCreditAmountTxt.setText(remainingAmount > 0 ? String.format("%.2f", remainingAmount) : "0.00");
        try {
            voucherEmployeeVoucherAmount.setText(SettingsController.getSetting("employee_voucher_amount").getValue());
        } catch (SQLException ex) {
            logger.error("SQL Exception has occured.", ex);
        }
        voucherCustomerAmountTxt.setText(remainingAmount > 0 ? String.format("%.2f", remainingAmount) : "0.00");
        try {
            poshanaPaymentAmountTxt.setText(SettingsController.getSetting("poshana_amount").getValue());
        } catch (SQLException ex) {
            logger.error("SQL Exception has occured.", ex);
        }
    }

    //Validate a given name
//    public boolean validateName(String txt) {
//        logger.debug("validateName invoked");
//
//        String regx = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
//        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(txt);
//        return matcher.find();
//
//    }
    //Handle txt Qty key press
    private void txtQtyKeyPressHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtQtyKeyPressHandler invoked");

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addItemToInvoiceItemTable();
        }
    }

    //Handle Payment amount key press
    private void txtCardNoKeyPressHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtCardNoKeyPressHandler invoked");

        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (cardTypeComboBox.getSelectedIndex() == 0 && txtcardNo.getText().toCharArray().length != 5) {
                Utilities.showMsgBox("Amex card should have 5 numbers for card number", "Incorrect card number", JOptionPane.WARNING_MESSAGE);
                txtcardNo.requestFocus();
            } else if (cardTypeComboBox.getSelectedIndex() == 1 && txtcardNo.getText().toCharArray().length != 4) {
                Utilities.showMsgBox("Master card should have 4 numbers for card number", "Incorrect card number", JOptionPane.WARNING_MESSAGE);
                txtcardNo.requestFocus();
            } else if (cardTypeComboBox.getSelectedIndex() == 2 && txtcardNo.getText().toCharArray().length != 4) {
                Utilities.showMsgBox("Visa card should have 4 numbers for card number", "Incorrect card number", JOptionPane.WARNING_MESSAGE);
                txtcardNo.requestFocus();
            } else {
                txtCardPaymentAmount.requestFocus();
            }
        }
    }

    //Handle Payment amount key press
    private void txtPaymentKeyPressHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtPaymentKeyPressHandler invoked");

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            paymentChooser();
        }
    }

    //Handle PoshanaCustomerName key press
    private void txtPoshanaCustomerNamePressHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtPoshanaCustomerNamePressHandler invoked");

        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
//            if (!validateName(poshanaCustomerNameTxt.getText())) {
//                Utilities.showMsgBox("Poshana customer name not valid", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
//                poshanaCustomerNameTxt.requestFocus();
//                return;
//            }
            poshanaIdTxt.requestFocus();
        }
    }

    //Handle txtPoshanaId key press
    private void txtPoshanaIdPressHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtPoshanaIdPressHandler invoked");

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (poshanaIdTxt.getText().trim().isEmpty()) {
                Utilities.showMsgBox("Poshana voucher id cannot be empty", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
                poshanaIdTxt.requestFocus();
                return;
            } else {
                try {
                    int poshanaVoucherId = Integer.parseInt(poshanaIdTxt.getText().trim());
                    if (poshanaVoucherId == 0) {
                        throw new NumberFormatException("Zero number");
                    }
                } catch (NumberFormatException ex) {
                    Utilities.showMsgBox("Invalid number", "Incorrect poshanaId number", JOptionPane.WARNING_MESSAGE);
                    poshanaIdTxt.requestFocus();
                    return;
                }
            }
            paymentChooser();
        }
    }

    //Handle txtVoucherCustomerIdP key press
    private void txtVoucherCustomerIdPressHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtVoucherCustomerIdPressHandler invoked");

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (voucherCustomerIdTxt.getText().trim().isEmpty()) {
                Utilities.showMsgBox("Poshana voucher id cannot be empty", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
                voucherCustomerIdTxt.requestFocus();
                return;
            } else {
                try {
                    int poshanaVoucherId = Integer.parseInt(voucherCustomerIdTxt.getText().trim());
                    if (poshanaVoucherId == 0) {
                        throw new NumberFormatException("Zero number");
                    }
                } catch (NumberFormatException ex) {
                    Utilities.showMsgBox("Invalid number", "Incorrect poshanaId number", JOptionPane.WARNING_MESSAGE);
                    voucherCustomerIdTxt.requestFocus();
                    return;
                }
            }
            voucherCustomerAmountTxt.requestFocus();
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Essential invoice methods">
    //Show the add item panel in bill
    private void showAddItemPanel() {
        logger.debug("showAddItemPanel invoked");

        CardLayout invoiceCard = (CardLayout) invoicePanel.getLayout();
        invoiceCard.show(invoicePanel, "itemCard");
        invoiceItemTable.getSelectionModel().clearSelection();
        itemCodeComboBox.requestFocus();
    }

    //Show the payment panel in the bill
    private void showPaymentPanel() {
        logger.debug("showPaymentScreen invoked");

        calculateItemParameters();
        if (invoice == null || invoice.getItemCount() < 1 || invoice.getNetTotal() < 0) {
            Utilities.showMsgBox("Invoice must have at least one item", "Error", JOptionPane.WARNING_MESSAGE);
            logger.warn("invoice must have at least one item and total must be >=0");
            return;
        }

        calculatePaymentParameters();

        paymentOptionComboBox.setSelectedIndex(0);

        lblBillValueVal.setText(String.format("%.2f", invoice.getNetTotal()));

        CardLayout invoiceCard = (CardLayout) invoicePanel.getLayout();
        invoiceCard.show(invoicePanel, "paymentCard");

    }

    //Private make a Member invoice
    private void convertToMemberInvoice() {
        logger.debug("convertToMemberInvoice invoked");

        logger.warn("To convert to normal invoice, reset the invoice");
        if (chkMember.isSelected()) {
            chkMember.setEnabled(false);
        }
    }

    //Clear items card as well as payments card
    private void resetInvoice() {
        logger.debug("resetInvoice invoked");

        this.processingProduct = null;

        cleanItemAddUI();
        cleanPaymentsUI();
        initializeInvoice();
        loadSellebleProducts();

        chkMember.setSelected(false);
        chkMember.setEnabled(true);
    }

    //Cancel current bill and show welocme screen
    private void cancelBill() {
        logger.debug("cancelBill invoked");
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        if (invoiceItemTableModel.getRowCount() > 0) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Discard current sale ?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult != JOptionPane.YES_OPTION) {
                return;
            } else {
                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            }
        }
        parent.setIsMainActivityRunning(false);
        parent.setIsInvoiceRunning(false);
        this.dispose();
    }

    //Search a item
    private void searchItem() {
        logger.debug("searchItem invoked");
        if (searchItemInterface != null) {
            desktopPane.remove(searchItemInterface);
        }
        searchItemInterface = new SearchItemInterface(desktopPane, this);
        desktopPane.add(searchItemInterface, new Integer(20));//On top of all internal frames
        searchItemInterface.setVisible(true);

    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Add item System">
    //Method to get next invoice Id
    private void initializeInvoice() {
        logger.debug("showNextInvoiceId invoked");

        try {
            //Setup a new invoice to work with
            invoice = new Invoice(getLastInvoicelId() + 1, parent.getCounterLogin().getShiftId());
            invoice.setDate(Utilities.getStringDate(Utilities.getCurrentDate()));
            invoice.setTime(Utilities.getCurrentTime(true));
            txtBillNumber.setText(Utilities.convertKeyToString(invoice.getInvoiceNo(), DatabaseInterface.INVOICE));

        } catch (SQLException ex) {
            logger.error("Bill no error : " + ex.getMessage());
        }
    }

    //Load the products to the temporory object array and show on combo box
    private void loadSellebleProducts() {
        logger.debug("loadSellebleProducts invoked");

        try {

            ArrayList<Product> availableProducts = getAllSellebleProducts();
            availableProductMap = new HashMap<>();

            itemCodeComboBox.removeActionListener(productCodeListner);
            productComboBoxModel.removeAllElements();

            for (Product product : availableProducts) {
                availableProductMap.put(product.getProductId(), product);
                productComboBoxModel.addElement(product.getProductId());
            }

            itemCodeComboBox.setModel(productComboBoxModel);
            itemCodeComboBox.setSelectedIndex(-1);

            itemCodeComboBox.addActionListener(productCodeListner);
            invoiceClearProductinfo();

        } catch (SQLException ex) {
            logger.error("Product load error : " + ex.getMessage());
        }
    }

    public void setSelectedProduct(String productId) {
        logger.debug("setSelectedProduct invoked");

        for (int i = 0; i < productComboBoxModel.getSize(); i++) {
            if (productComboBoxModel.getElementAt(i).equals(productId)) {
                itemCodeComboBox.setSelectedIndex(i);// can cause a recursive malfunction
                break;
            }
        }

    }

    //Display selected product details
    private void showProductDetails() {
        logger.debug("showProductDetails invoked");

        if (itemCodeComboBox.getSelectedIndex() > -1) {

            String productId = itemCodeComboBox.getSelectedItem().toString();

            try {
                //Create cloned object
                processingProduct = (Product) util.Utilities.deepClone(availableProductMap.get(productId));

                txtProductName.setText(processingProduct.getProductName());
                txtProductDesc.setText(processingProduct.getDescription());
                lblUnit.setText(processingProduct.getUnit());

                if (processingProduct.getBatches().size() == 1) {
                    logger.info("Product has one batch");
                    setProductBatch(processingProduct.getBatches().get(0));
                } else if (processingProduct.getBatches().size() > 1) {
                    logger.info("Product has more than one batch, showing price selection UI");

                    if (selectPriceInterface != null) {
                        desktopPane.remove(selectPriceInterface);
                    }
                    selectPriceInterface = new SelectPriceInterface(this, desktopPane, productId, processingProduct.getBatches());
                    desktopPane.add(selectPriceInterface, new Integer(10));//Make the price selection UI be on top of desktop pane      
                    selectPriceInterface.setVisible(true);

                    enableGlassPane(false);

                }
                txtPrice.setEnabled(true);
                txtQty.setEnabled(true);
            } catch (IOException | ClassNotFoundException ex) {
                logger.error("Product clone error : " + ex.getMessage());
            }
        }
    }

    //Add item to the bill item table
    private void addItemToInvoiceItemTable() {
        logger.debug("addItemToInvoiceItemTable invoked");

        try {
            // <editor-fold defaultstate="collapsed" desc="Validations">      
            if (invoice == null || processingProduct == null || processingProduct.getSelectedBatch() == null || txtQty.getText().isEmpty()) {
                logger.warn("Empty invoice or processingProduct or Qty");
                return;
            }

            double unitPrice;
            double qty;

            try {
                unitPrice = Double.parseDouble(txtPrice.getText());
                qty = Double.parseDouble(txtQty.getText());
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Please enter valid price and quantity ", "Incorrect quantity", JOptionPane.WARNING_MESSAGE);
                logger.error("NumberFormatException  error : " + ex.getMessage(), ex);
                txtQty.requestFocus();
                return;
            }

            //Entered amount more than the amount recieved for batch
            if (qty <= 0.00 || qty > processingProduct.getSelectedBatch().getRecievedQuantity() - processingProduct.getSelectedBatch().getSoldQty()) {
                Utilities.showMsgBox("Invalid item quantity ", "Incorrect quantity", JOptionPane.WARNING_MESSAGE);
                txtQty.setText("");
                txtQty.requestFocus();
                return;
            }

            //If unit is bulk , only whole number quantities allowed
            if (processingProduct.getUnit().toLowerCase().equals("bulk") && qty != Math.rint(qty)) {
                Utilities.showMsgBox("Bulk item cannot be a fraction", "Incorrect quantity", JOptionPane.WARNING_MESSAGE);
                txtQty.setText("");
                txtQty.requestFocus();
                return;
            }

            String productId = processingProduct.getProductId();
            String batchId = processingProduct.getSelectedBatch().getBatchId();

            int rowNumber = -1;
            double qtyInTable = 0;
            for (int row = 0; row < invoiceItemTableModel.getRowCount(); row++) {
                if (invoiceItemTable.getValueAt(row, PRODUCT_ID_COLUMN).toString().equals(productId)
                        && invoiceItemTable.getValueAt(row, BATCH_ID_COLUMN).toString().equals(batchId)) {

                    rowNumber = row;
                    qtyInTable = Double.parseDouble(invoiceItemTable.getValueAt(row, UNIT_QTY_COLUMN).toString());

                    if (qty + qtyInTable > processingProduct.getSelectedBatch().getRecievedQuantity() - processingProduct.getSelectedBatch().getSoldQty()) {
                        Utilities.showMsgBox("Total item quantity exceeds what is in the stock !", "Incorrect quantity", JOptionPane.WARNING_MESSAGE);
                        txtQty.requestFocus();
                        return;
                    }

                    break;
                }
            }

            // </editor-fold>
            //
            // <editor-fold defaultstate="collapsed" desc="Prepare product parameters">  
            double totalQty = qty + qtyInTable;
            double categorydDiscount = getCategoryDiscount(unitPrice, totalQty);
            double batchDiscount = getBatchDiscount(unitPrice, totalQty);
            double totalDiscount = categorydDiscount + batchDiscount;
            double subTotal = totalQty * unitPrice - totalDiscount;
            // </editor-fold>
            //
            // <editor-fold defaultstate="collapsed" desc="Add item to the table">  

            if (rowNumber > -1) {//item is already in table 
                invoiceItemTable.setValueAt(String.format("%.2f", totalQty), rowNumber, UNIT_QTY_COLUMN);//Qty
                invoiceItemTable.setValueAt(String.format("%.2f", totalDiscount), rowNumber, NET_DISCOUNT_COLUMN);//Discount value
                invoiceItemTable.setValueAt(String.format("%.2f", subTotal), rowNumber, NET_TOTAL_COLUMN);//Sub total
            } else {//add as a new item
                Object[] ob = {
                    productId,
                    batchId,
                    processingProduct.getProductName(),
                    processingProduct.getDescription(),
                    String.format("%.2f", unitPrice),
                    String.format("%.2f", totalQty),
                    String.format("%.2f", totalDiscount),
                    String.format("%.2f", subTotal)
                };
                invoiceItemTableModel.addRow(ob);
            }

            // </editor-fold>
            invoiceClearProductinfo();//Ready for next product

            calculateItemParameters();//Update the invoice parameters

        } catch (SQLException ex) {
            logger.error("SQL  error : " + ex.getMessage(), ex);
        }
    }

    //Remove a added item from the bill item table
    private void deleteItemFromBillItemTable() {
        logger.debug("deleteItemFromBillItemTable invoked");

        int row = invoiceItemTable.getSelectedRow();
        if (row != -1) {

            invoiceItemTableModel.removeRow(row);
            if ((invoiceItemTable.getRowCount() - 1) > -1) {
                invoiceItemTable.setRowSelectionInterval((invoiceItemTable.getRowCount() - 1), (invoiceItemTable.getRowCount() - 1));
            }
            calculateItemParameters();
        }

    }

// </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Payment System">
    //Toggle payment options in payment screen
    private void loadEmployeeVocherDetails() {
        logger.debug("yasara -loadEmployeeVocherDetails method invoked");
        // boolean employeeVoucherPayment = false;

        try {
            ArrayList<Employee> employeeDetails = EmployeeCreditController.loadComboBoxEmployees();
            employeeComboBoxModel.removeAllElements();
            for (Employee employee : employeeDetails) {
                employeeComboBoxModel.addElement(new KeyValueContainer(String.valueOf(employee.getEmployeeId()), employee.getEmployeeName()));
            }
            voucherEmployeeNameComboBox.setSelectedIndex(-1);
        } catch (SQLException ex) {
            logger.error("SQL  error : " + ex.getMessage(), ex);
        }
    }

    private void loadCoopCustomersComboBox() {
        logger.debug("Yasara -loadCoopCustomers invoked");

        try {
            coopCustomerNameComboBox.removeActionListener(coopCustomerListner);

            int customerCreditId = 0;
            boolean coopPaymentInTable = false;

            for (int row = 0; row < invoicePaymentsTable.getRowCount(); row++) {
                if (invoicePaymentsTable.getValueAt(row, PAYMENT_OPTION_COLUMN).toString().equals(Payment.COOP_CREDIT)) {
                    logger.info("customer found in payment table");

                    coopPaymentInTable = true;
                    customerCreditId = Integer.parseInt(invoicePaymentsTable.getValueAt(row, PAYMENT_OFFSET_0_COLUMN).toString());
                    break;
                }
            }

            if (coopPaymentInTable) {
                logger.debug("coopPaymentInTable");
                CreditCustomer creditCustomer = customers.get(customerCreditId);
                if (creditCustomer != null) {
                    coopCustomerNameComboBox.removeAllItems();
                    coopCustomerComboBoxModel.addElement(new KeyValueContainer(String.valueOf(creditCustomer.getCustomerId()), creditCustomer.getCustomerName()));
                } else {
                    Utilities.showMsgBox("Invalid credit customer selected", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
                    redeemableAmountTxt.setText(String.format("%.2f", 0.0));
                }
            } else {
                logger.debug("!coopPaymentInTable");
                coopCustomerComboBoxModel.removeAllElements();
                ArrayList<CreditCustomer> customerDetails = CustomerCreditController.loadComboBoxCustomers();
                customers = new HashMap();
                for (CreditCustomer customer : customerDetails) {
                    customers.put(customer.getCustomerId(), customer);
                    coopCustomerComboBoxModel.addElement(new KeyValueContainer(String.valueOf(customer.getCustomerId()), customer.getCustomerName()));
                }
                coopCustomerNameComboBox.setSelectedIndex(-1);
                redeemableAmountTxt.setText(String.format("%.2f", 0.0));
            }
        } catch (SQLException ex) {
            logger.error("SQL  error : " + ex.getMessage(), ex);
        } finally {
            coopCustomerNameComboBox.removeActionListener(coopCustomerListner);
            coopCustomerNameComboBox.addActionListener(coopCustomerListner);
            if (coopCustomerNameComboBox.getItemCount() > 1) {
                coopCreditAmountTxt.setEnabled(false);
            }
        }
    }

    private void togglePaymentOptions() {
        logger.debug("togglePaymentOptions invoked");

        //
        // When showing payment option remaining amount should be shown automatically
        //
        CardLayout paymentMethodCard = (CardLayout) paymentDetailsPanel.getLayout();
        String selectedOption = (String) paymentOptionComboBox.getSelectedItem();
        if (selectedOption != null && !selectedOption.isEmpty()) {
            calculatePaymentParameters();
            switch (selectedOption) {
                case Payment.CASH:
                    paymentMethodCard.show(paymentDetailsPanel, "cashCard");
                    break;
                case Payment.CREDIT_CARD:
                    paymentMethodCard.show(paymentDetailsPanel, "bankCard");
                    break;
                case Payment.COOP_CREDIT:
                    loadCoopCustomersComboBox();
                    paymentMethodCard.show(paymentDetailsPanel, "coopCreditCard");
                    break;
                case Payment.POSHANA:
                    paymentMethodCard.show(paymentDetailsPanel, "poshanaCard");
                    break;
                case Payment.VOUCHER:
                    paymentMethodCard.show(paymentDetailsPanel, "voucherCard");
                    break;
            }
        }
    }

    //Toggle card type
    private void toggleCardType() {
        logger.debug("toggleCardType invoked");

        String selectedOption = (String) cardTypeComboBox.getSelectedItem();
        if (cardTypeComboBox.getSelectedIndex() > -1 && selectedOption != null && !selectedOption.isEmpty()) {

            switch (selectedOption) {
                case CardPayment.AMEX:
                    txtcardNo.setDocument(new CharactorLimitDocument(5));
                    break;
                case CardPayment.MASTER:
                case CardPayment.VISA:
                    txtcardNo.setDocument(new CharactorLimitDocument(4));
                    break;
            }

            if (selectedOption.equals(CardPayment.AMEX) || selectedOption.equals(CardPayment.MASTER) || selectedOption.equals(CardPayment.VISA)) {
                txtcardNo.setEnabled(true);
                txtcardNo.setEditable(true);
                txtCardPaymentAmount.setEnabled(true);

                txtcardNo.requestFocus();
            }
        }
    }

    //Toggle voucher payment type
    private void toggleVoucherType() {
        logger.debug("toggleVoucherType invoked");

        CardLayout voucherPaymentCard = (CardLayout) voucherCard.getLayout();
        calculatePaymentParameters();
        if (employeeRadioButton.isSelected()) {
            loadEmployeeVocherDetails();
            voucherPaymentCard.show(voucherCard, "employeeCard");
        } else if (customerRadioButton.isSelected()) {
            voucherPaymentCard.show(voucherCard, "customerCard");
        }
    }

    private void resetPaymentsAfterDelete(String paymentType) {
        logger.debug("Yasara - changeRemainingValue invoked");

        if (paymentType.equals(Payment.COOP_CREDIT)) {
            loadCoopCustomersComboBox();
            setCoopCreditMax();
            coopCreditAmountTxt.setEnabled(false);
        }
        if (paymentType.equals(Payment.POSHANA)) {
            paymentOptionComboBox.insertItemAt(Payment.POSHANA, 3);

            poshanaIdTxt.setText("");
            poshanaCustomerNameTxt.setText("");
        }
        if (paymentType.equals(Payment.VOUCHER)) {
            paymentOptionComboBox.insertItemAt(Payment.VOUCHER, paymentOptionComboBox.getItemCount() );

            customerRadioButton.setSelected(true);
            voucherCustomerIdTxt.setText("");

            employeeRadioButton.setSelected(false);
            voucherEmployeeNameComboBox.setSelectedIndex(-1);

            CardLayout voucherPaymentCard = (CardLayout) voucherCard.getLayout();
            voucherPaymentCard.show(voucherCard, "customerCard");
        }
    }

    //Set coop credit customer specific limits
    private void setCoopCreditMax() {
        logger.debug("Yasara - setCoopCreditMax invoked");

        try {
            coopCustomerNameComboBox.removeActionListener(coopCustomerListner);

            double coopCreditInTable = 0;
            boolean coopPaymentInTable = false;
            int customerCreditId = 0;
            for (int row = 0; row < invoicePaymentsTable.getRowCount(); row++) {
                if (invoicePaymentsTable.getValueAt(row, PAYMENT_OPTION_COLUMN).toString().equals(Payment.COOP_CREDIT)) {
                    logger.info("customer found in payment table");

                    coopPaymentInTable = true;
                    coopCreditInTable = Double.parseDouble(invoicePaymentsTable.getValueAt(row, PAYMENT_AMOUNT_COLUMN).toString());
                    customerCreditId = Integer.parseInt(invoicePaymentsTable.getValueAt(row, PAYMENT_OFFSET_0_COLUMN).toString());
                    break;
                }
            }

            if (coopPaymentInTable) {
                logger.debug("coopPaymentInTable");
                coopCustomerNameComboBox.removeAllItems();
                CreditCustomer creditCustomer = customers.get(customerCreditId);
                if (creditCustomer != null) {
                    coopCustomerComboBoxModel.addElement(new KeyValueContainer(String.valueOf(creditCustomer.getCustomerId()), creditCustomer.getCustomerName()));
                    double redeemableCreditAmount
                            = Double.parseDouble(SettingsController.getSetting("customer_credit_limit").getValue())
                            - coopCreditInTable
                            - creditCustomer.getCurrentCredit();
                    redeemableAmountTxt.setText(String.format("%.2f", redeemableCreditAmount > 0 ? redeemableCreditAmount : 0));
                } else {
                    redeemableAmountTxt.setText(String.format("%.2f", 0.0));
                    Utilities.showMsgBox("Invalid credit customer selected", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                logger.debug("!coopPaymentInTable");
                if (coopCustomerNameComboBox.getSelectedItem() == null) {
                    redeemableAmountTxt.setText(String.format("%.2f", 0.0));
                    logger.warn("No selected customer");
                    redeemableAmountTxt.setText(String.format("%.2f", 0.0));
                    return;
                }

                int customerId = Integer.parseInt(((KeyValueContainer) coopCustomerNameComboBox.getSelectedItem()).getKey());
                logger.info("Customer ID :" + customerId);

                CreditCustomer creditCustomer = customers.get(customerId);
                if (creditCustomer != null) {
                    double coopCreditRedeemableAmount
                            = Double.parseDouble(SettingsController.getSetting("customer_credit_limit").getValue())
                            - creditCustomer.getCurrentCredit();
                    redeemableAmountTxt.setText(String.format("%.2f", coopCreditRedeemableAmount > 0 ? coopCreditRedeemableAmount : 0));
                } else {
                    Utilities.showMsgBox("Invalid credit customer id", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            logger.error("SQL error : " + ex.getMessage());
        } finally {
            coopCustomerNameComboBox.removeActionListener(coopCustomerListner);
            coopCustomerNameComboBox.addActionListener(coopCustomerListner);
        }
    }

    //Add a payment option
    private void paymentChooser() {
        logger.debug("addPaymentOption invoked");

        if (invoice.getAmountPaid() - invoice.getNetTotal() >= 0) {
            Utilities.showMsgBox("Invoice amount already fulfilled", "Payment compleate", JOptionPane.INFORMATION_MESSAGE);
            logger.warn("Invoice amount fulfilled");
            return;
        }

        String selectedOption = (String) paymentOptionComboBox.getSelectedItem();
        if (selectedOption != null && !selectedOption.isEmpty()) {
            switch (selectedOption) {
                case Payment.CASH:
                    handleCashPayment();
                    break;
                case Payment.CREDIT_CARD:
                    handleCardPayment();
                    break;
                case Payment.COOP_CREDIT:
                    handleCoopCreditPayment();
                    break;
                case Payment.POSHANA:
                    handlePoshanaPayment();
                    break;
                case Payment.VOUCHER:
                    handleVoucherPayment();
                    break;
            }
            calculatePaymentParameters();
        }
    }

    private void handleCashPayment() {
        logger.debug("handleCashPayment invoked");

        // <editor-fold defaultstate="collapsed" desc="Validations"> 
        if (invoice == null) {
            logger.error("null invoice");
            return;
        }

        //Validate cash payment
        double cashPaymentAmount = 0;
        try {
            cashPaymentAmount = Double.parseDouble(txtCashPaymentAmount.getText());
            if (cashPaymentAmount == 0.00) {
                throw new NumberFormatException("Zero payment");
            }
        } catch (NumberFormatException ex) {
            Utilities.showMsgBox("Invalid cash payment amount", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
            txtCashPaymentAmount.requestFocus();
            return;
        }

        // </editor-fold>
        //
        // <editor-fold defaultstate="collapsed" desc="Add payment to the table">
        //if payment option already in the table update it
        boolean itemInTable = false;
        for (int row = 0; row < invoicePaymentsTable.getRowCount(); row++) {
            if (invoicePaymentsTable.getValueAt(row, PAYMENT_OPTION_COLUMN).toString().equals(Payment.CASH)) {
                itemInTable = true;
                double cashPaymentInTable = Double.parseDouble(invoicePaymentsTable.getValueAt(row, PAYMENT_AMOUNT_COLUMN).toString());
                double newCashPaymentAmount = cashPaymentInTable + cashPaymentAmount;

                invoicePaymentsTable.setValueAt(String.format("%.2f", newCashPaymentAmount), row, PAYMENT_AMOUNT_COLUMN);//Cash Payment amount
                break;
            }
        }

        //Can enter any amount with no restriction
        if (!itemInTable) {
            Object[] ob = {
                Payment.CASH,
                String.format("%.2f", cashPaymentAmount),
                "",
                ""
            };
            invoicePaymentsTableModel.addRow(ob);
        }
        // </editor-fold>
        //
        logger.info("Cash payment added");
    }

    private void handleCardPayment() {
        logger.debug("handleCardPayment invoked");

        // <editor-fold defaultstate="collapsed" desc="Validations"> 
        if (invoice == null) {
            logger.error("null invoice");
            return;
        }

        //Validate card type
        String cardType = cardTypeComboBox.getSelectedIndex() > -1 ? cardTypeComboBox.getSelectedItem().toString() : "";

        String cardNumber = txtcardNo.getText();

        if (!cardType.equals(CardPayment.AMEX) && !cardType.equals(CardPayment.MASTER) && !cardType.equals(CardPayment.VISA)) {
            Utilities.showMsgBox("Please select a card type first", "", JOptionPane.WARNING_MESSAGE);
            paymentOptionComboBox.requestFocus();
            return;
        } else if (cardType.equals(CardPayment.AMEX) && cardNumber.toCharArray().length != 5) {
            Utilities.showMsgBox("Amex card should have 5 numbers for card number", "Incorrect card number", JOptionPane.WARNING_MESSAGE);
            txtcardNo.requestFocus();
            return;
        } else if (cardType.equals(CardPayment.MASTER) && cardNumber.toCharArray().length != 4) {
            Utilities.showMsgBox("Master card should have 4 numbers for card number", "Incorrect card number", JOptionPane.WARNING_MESSAGE);
            txtcardNo.requestFocus();
            return;
        } else if (cardType.equals(CardPayment.VISA) && cardNumber.toCharArray().length != 4) {
            Utilities.showMsgBox("Visa card should have 4 numbers for card number", "Incorrect card number", JOptionPane.WARNING_MESSAGE);
            txtcardNo.requestFocus();
            return;
        }

        //Validate card number
        try {
            int cardNo = Integer.parseInt(cardNumber);
            if (cardNo == 0) {
                throw new NumberFormatException("Zero number");
            }
        } catch (NumberFormatException ex) {
            Utilities.showMsgBox("Invalid number", "Incorrect card number", JOptionPane.WARNING_MESSAGE);
            txtcardNo.requestFocus();
            return;
        }

        //Validate card payment
        double cardPaymentAmount = 0;
        try {
            cardPaymentAmount = Double.parseDouble(txtCardPaymentAmount.getText());
            if (cardPaymentAmount == 0.00) {
                throw new NumberFormatException("Zero payment");
            }
        } catch (NumberFormatException ex) {
            Utilities.showMsgBox("Invalid card payment amount", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
            txtCardPaymentAmount.requestFocus();
            return;
        }

        //Payment should be <= remaining amount
        double remainingAmount = invoice.getNetTotal() - invoice.getAmountPaid();

        if (cardPaymentAmount > remainingAmount) {
            Utilities.showMsgBox("Card payment should be less than or equal to the remaining amount to be paid", "Invalid amount", JOptionPane.WARNING_MESSAGE);
            txtCardPaymentAmount.requestFocus();
            return;
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Add payment to the table">
        //if payment option already in the table update it
        boolean itemInTable = false;
        for (int row = 0; row < invoicePaymentsTable.getRowCount(); row++) {

            Object tableObject = invoicePaymentsTable.getValueAt(row, PAYMENT_OFFSET_0_COLUMN);//card type
            String cardTypeInTable = tableObject != null ? tableObject.toString() : "";

            tableObject = invoicePaymentsTable.getValueAt(row, PAYMENT_OFFSET_1_COLUMN);//card no
            String cardNoInTable = tableObject != null ? tableObject.toString() : "";

            if (cardTypeInTable.equals(cardType) && cardNoInTable.equals(cardNumber)) {
                itemInTable = true;
                double cardPaymentInTable = Double.parseDouble(invoicePaymentsTable.getValueAt(row, PAYMENT_AMOUNT_COLUMN).toString());
                double newCardPaymentAmount = cardPaymentInTable + cardPaymentAmount;
                invoicePaymentsTable.setValueAt(String.format("%.2f", newCardPaymentAmount), row, PAYMENT_AMOUNT_COLUMN);//Card Payment amount
                break;
            }
        }

        if (!itemInTable) {
            Object[] ob = {
                Payment.CREDIT_CARD + " : " + cardType,
                String.format("%.2f", cardPaymentAmount),
                cardType,//offset 0
                cardNumber//offset 1
            };
            invoicePaymentsTableModel.addRow(ob);
        }
        // </editor-fold>
        //
        logger.info("Card payment added");

        cardTypeComboBox.setSelectedIndex(-1);
        txtcardNo.setText("");
        txtCardPaymentAmount.setText("0.00");
        txtcardNo.setEnabled(false);
        txtcardNo.setEditable(false);
        txtCardPaymentAmount.setEnabled(false);

        //Reset the credit card
    }

    private void handleCoopCreditPayment() {
        logger.debug("Yasara - handleCoopCreditPayment invoked");

        if (invoice == null) {
            logger.error("null invoice");
            return;
        }

        if (coopCustomerNameComboBox.getSelectedIndex() < 0) {
            Utilities.showMsgBox("Please select a customer", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int customerId = Integer.parseInt(((KeyValueContainer) coopCustomerNameComboBox.getSelectedItem()).getKey());

        double coopCreditPaymentAmount = 0;
        double remainingCredit = 0;

        try {
            coopCreditPaymentAmount = Double.parseDouble(coopCreditAmountTxt.getText());
            if (coopCreditPaymentAmount == 0.00) {
                throw new NumberFormatException("Zero payment");
            }
        } catch (NumberFormatException x) {
            Utilities.showMsgBox("Invalid payment amount", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
            coopCreditAmountTxt.requestFocus();
            return;
        }

        try {
            remainingCredit = Double.parseDouble(redeemableAmountTxt.getText());
            if (remainingCredit == 0) {
                throw new NumberFormatException("Zero number");
            }
        } catch (NumberFormatException x) {
            Utilities.showMsgBox("Maximum redeemable amount reached", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (coopCreditPaymentAmount > remainingCredit) {
            Utilities.showMsgBox("Amount should be less than Maximum redeemable amount ", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
            coopCreditAmountTxt.requestFocus();
            return;
        }

        double remainingAmount = invoice.getNetTotal() - invoice.getAmountPaid();

        if (coopCreditPaymentAmount > remainingAmount) {
            Utilities.showMsgBox("Coop credit payment should be less than or equal to the remaining amount to be paid", "Invalid amount", JOptionPane.WARNING_MESSAGE);
            logger.warn("coopCreditPaymentAmount > remainingAmount");
            coopCreditAmountTxt.requestFocus();
            return;
        }

        //if payment option already in the table update it
        boolean coopCreditPaymentInTable = false;
        for (int row = 0; row < invoicePaymentsTable.getRowCount(); row++) {
            if (invoicePaymentsTable.getValueAt(row, PAYMENT_OPTION_COLUMN).toString().equals(Payment.COOP_CREDIT)) {
                coopCreditPaymentInTable = true;//customer id and offset id must be equal
                double coopCreditAmountInTable = Double.parseDouble(invoicePaymentsTable.getValueAt(row, PAYMENT_AMOUNT_COLUMN).toString());
                double newCoopCreditPaymentAmount = coopCreditAmountInTable + coopCreditPaymentAmount;

                invoicePaymentsTable.setValueAt(String.format("%.2f", newCoopCreditPaymentAmount), row, PAYMENT_AMOUNT_COLUMN);//Coop credit Payment coopAmount
                break;
            }
        }

        //Can enter any coopAmount with no restriction
        if (!coopCreditPaymentInTable) {
            Object[] ob = {
                Payment.COOP_CREDIT,
                String.format("%.2f", coopCreditPaymentAmount),
                customerId,//Offset 0
                ""
            };
            invoicePaymentsTableModel.addRow(ob);
        }

        logger.info("Coop Credit payment added");

        coopCreditAmountTxt.setEnabled(false);
        //
        loadCoopCustomersComboBox();
        setCoopCreditMax();
        //

        coopCustomerNameComboBox.setSelectedIndex(-1);
    }

    private void handlePoshanaPayment() {
        //Only one poshana payment should be enabled
        logger.debug("Yasara - handle Poshana Payment invoked");

        if (invoice == null) {
            logger.error("null invoice");
            return;
        }

        int poshanaVoucherId = 0;
        if (poshanaIdTxt.getText().trim().isEmpty()) {
            Utilities.showMsgBox("Poshana voucher id cannot be empty", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
            poshanaIdTxt.requestFocus();
            return;
        } else {
            try {
                poshanaVoucherId = Integer.parseInt(poshanaIdTxt.getText().trim());
                if (poshanaVoucherId == 0) {
                    throw new NumberFormatException("Zero number");
                }
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Invalid number", "Incorrect poshanaId number", JOptionPane.WARNING_MESSAGE);
                poshanaIdTxt.requestFocus();
                return;
            }
        }

//        if (!validateName(poshanaCustomerNameTxt.getText())) {
//            Utilities.showMsgBox("Poshana customer name not valid", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
//            poshanaCustomerNameTxt.requestFocus();
//            return;
//        }
        //Poshana amount must come from settings
        double poshanaAmount = 0;
        try {
            poshanaAmount = Double.parseDouble(SettingsController.getSetting("poshana_amount").getValue());
        } catch (SQLException ex) {
            logger.error("SQL error : " + ex.getMessage());
            return;
        }

        double remainingAmount = invoice.getNetTotal() - invoice.getAmountPaid();

        if (poshanaAmount <= 0 || poshanaAmount > remainingAmount) {
            Utilities.showMsgBox("Poshana payment should be less than or equal to the remaining amount to be paid", "Invalid amount", JOptionPane.WARNING_MESSAGE);
            logger.warn("poshanaAmount > remainingAmount");
            return;
        }

        Object[] ob = {
            Payment.POSHANA,
            String.format("%.2f", poshanaAmount),
            poshanaCustomerNameTxt.getText(),//Off set 0
            poshanaVoucherId //Off set 1
        };
        invoicePaymentsTableModel.addRow(ob);

        paymentOptionComboBox.setSelectedItem(Payment.CASH);
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "cashCard");
        paymentOptionComboBox.removeItem(Payment.POSHANA);
    }

    private void handleVoucherPayment() {
        logger.debug("Yasara - handle Voucher Payment invoked");

        if (invoice == null) {
            logger.error("null invoice");
            return;
        }
        if (customerRadioButton.isSelected()) {

            //Validate voucher payment
            double customerVoucherPaymentAmount = 0;
            try {
                customerVoucherPaymentAmount = Double.parseDouble(voucherCustomerAmountTxt.getText());
                if (customerVoucherPaymentAmount == 0.00) {
                    throw new NumberFormatException("Zero payment");
                }
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Invalid voucher payment amount", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
                voucherCustomerAmountTxt.requestFocus();
                return;
            }

            int customerVoucherId = 0;
            if (voucherCustomerIdTxt.getText().trim().isEmpty()) {
                Utilities.showMsgBox("Customer voucher id cannot be empty", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
                voucherCustomerIdTxt.requestFocus();
                return;
            } else {
                try {
                    customerVoucherId = Integer.parseInt(voucherCustomerIdTxt.getText().trim());
                    if (customerVoucherId == 0) {
                        throw new NumberFormatException("Zero number");
                    }
                } catch (NumberFormatException ex) {
                    Utilities.showMsgBox("Invalid number", "Incorrect voucher Id number", JOptionPane.WARNING_MESSAGE);
                    voucherCustomerIdTxt.requestFocus();
                    return;
                }
            }

            double remainingAmount = invoice.getNetTotal() - invoice.getAmountPaid();
            if (customerVoucherPaymentAmount > remainingAmount) {
                Utilities.showMsgBox("Voucher payment should be less than or equal to the remaining amount to be paid", "Invalid amount", JOptionPane.WARNING_MESSAGE);
                voucherCustomerAmountTxt.requestFocus();
                return;
            }

            //Allow to add the voucher only once
            Object[] ob = {
                Payment.VOUCHER,
                String.format("%.2f", customerVoucherPaymentAmount),
                customerVoucherId,//Offset 0
                ""
            };

            invoicePaymentsTableModel.addRow(ob);

            voucherCustomerIdTxt.setText("");
            voucherCustomerAmountTxt.setText("0.00");

            paymentOptionComboBox.setSelectedItem(Payment.CASH);
            CardLayout card = (CardLayout) cardPanel.getLayout();
            card.show(cardPanel, "cashCard");
            paymentOptionComboBox.removeItem(Payment.VOUCHER);
            logger.info("Customer voucher payment added");
            //
        } else if (employeeRadioButton.isSelected()) {

            if (voucherEmployeeNameComboBox.getSelectedIndex() < 0) {
                Utilities.showMsgBox("Please select an employee", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int employeeId = Integer.parseInt(((KeyValueContainer) voucherEmployeeNameComboBox.getSelectedItem()).getKey());

            //Enable amount text box when employee selected
            double employeeVoucherPaymentAmount = 0;
            try {
                employeeVoucherPaymentAmount = Double.parseDouble(voucherEmployeeVoucherAmount.getText());
                if (employeeVoucherPaymentAmount == 0.00) {
                    throw new NumberFormatException("Zero payment");
                }
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Invalid voucher payment amount", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
                voucherEmployeeVoucherAmount.requestFocus();
                return;
            }
            double remainingAmount = invoice.getNetTotal() - invoice.getAmountPaid();

            if (employeeVoucherPaymentAmount > remainingAmount) {
                Utilities.showMsgBox("Voucher payment should be less than or equal to the remaining amount to be paid", "Invalid amount", JOptionPane.WARNING_MESSAGE);
                voucherEmployeeVoucherAmount.requestFocus();
                return;
            }

            //Only one voucher can be entered per invoice
            Object[] ob = {
                Payment.VOUCHER,
                String.format("%.2f", employeeVoucherPaymentAmount),
                employeeId,//Offset 0
                ""
            };
            invoicePaymentsTableModel.addRow(ob);

            voucherEmployeeNameComboBox.setSelectedIndex(-1);
            paymentOptionComboBox.setSelectedItem(Payment.CASH);
            CardLayout card = (CardLayout) cardPanel.getLayout();
            card.show(cardPanel, "cashCard");
            paymentOptionComboBox.removeItem(Payment.VOUCHER);

            logger.info("Employee voucher payment added");
        }

    }

    //Remove a payment option
    private void removePaymentOption() {
        logger.debug("removePaymentOption invoked");

        int row = invoicePaymentsTable.getSelectedRow();
        if (row != -1) {
            String paymentType = invoicePaymentsTable.getValueAt(row, PAYMENT_OPTION_COLUMN).toString();
            invoicePaymentsTableModel.removeRow(row);
            if ((invoicePaymentsTable.getRowCount() - 1) > -1) {
                invoicePaymentsTable.setRowSelectionInterval((invoicePaymentsTable.getRowCount() - 1), (invoicePaymentsTable.getRowCount() - 1));
            }
            resetPaymentsAfterDelete(paymentType);
            calculatePaymentParameters();
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Finalize Invoice">
    //Confirm bill
    private void confirmInvoice() {
        logger.debug("confirmInvoice invoked");

        // <editor-fold defaultstate="collapsed" desc="Validation">        
        //double check 
        calculateItemParameters();
        calculatePaymentParameters();

        //Some thing wrong with items
        if (invoice == null || invoice.getItemCount() < 1 || invoice.getNetTotal() < 0) {
            logger.warn("invoice must have at least one item and total must be >=0");
            return;
        }

        //Some thing wrong with payments
        if (invoice == null || invoice.getNetTotal() > invoice.getAmountPaid()) {
            Utilities.showMsgBox("Invoice payment amount not fulfilled", "Warning", JOptionPane.ERROR_MESSAGE);
            logger.warn("invoice payment amount not fulfilled");
            return;
        }
        // </editor-fold>
        //
        // <editor-fold defaultstate="collapsed" desc="Get items">        

        //Get all items to an arrayList
        ArrayList<InvoiceItem> invoiceItemsList = new ArrayList();
        for (int row = 0; row < invoiceItemTable.getRowCount(); row++) {
            InvoiceItem invoiceItem = new InvoiceItem(
                    invoice.getInvoiceNo(),
                    Utilities.convertKeyToInteger(invoiceItemTable.getValueAt(row, PRODUCT_ID_COLUMN).toString()),
                    Utilities.convertKeyToInteger(invoiceItemTable.getValueAt(row, BATCH_ID_COLUMN).toString()),
                    Double.parseDouble(invoiceItemTable.getValueAt(row, UNIT_PRICE_COLUMN).toString()),
                    Double.parseDouble(invoiceItemTable.getValueAt(row, UNIT_QTY_COLUMN).toString()),
                    Double.parseDouble(invoiceItemTable.getValueAt(row, NET_DISCOUNT_COLUMN).toString()),
                    invoiceItemTable.getValueAt(row, DESCRIPTION_COLUMN).toString()
            );
            invoiceItemsList.add(invoiceItem);
        }

        //Add the items to the invoice
        invoice.setInvoiceItems(invoiceItemsList);
        // </editor-fold>
        //
        // <editor-fold defaultstate="collapsed" desc="Get payments">        

        //Get all payments to an arraylist
        ArrayList<Payment> invoicePaymntsList = new ArrayList();
        for (int row = 0; row < invoicePaymentsTable.getRowCount(); row++) {

            Object tableCellObject = invoicePaymentsTable.getValueAt(row, PAYMENT_OPTION_COLUMN);//payment option
            String paymentOption = tableCellObject.toString();

            tableCellObject = invoicePaymentsTable.getValueAt(row, PAYMENT_AMOUNT_COLUMN);//payment amount
            double paymentAmount = Double.parseDouble(tableCellObject.toString());

            tableCellObject = invoicePaymentsTable.getValueAt(row, PAYMENT_OFFSET_0_COLUMN);//off_set_0
            String off_set_0 = tableCellObject != null ? tableCellObject.toString() : "";

            tableCellObject = invoicePaymentsTable.getValueAt(row, PAYMENT_OFFSET_1_COLUMN);//off_set_1
            String off_set_1 = tableCellObject != null ? tableCellObject.toString() : "";

            if (paymentOption.contains(Payment.CASH)) {//Cash payment
                Double changeAmount = Double.valueOf(lblChangeVal.getText());//if any change add it to cash payment
                CashPayment cashPayment = new CashPayment(invoice.getInvoiceNo(), row + 1, paymentAmount, changeAmount);
                invoicePaymntsList.add(cashPayment);

            } else if (paymentOption.contains(Payment.CREDIT_CARD)) {//Coop credit payment
                String cardType = off_set_0;
                String cardNO = off_set_1;
                CardPayment cardPayment = new CardPayment(invoice.getInvoiceNo(), row + 1, cardType, cardNO, paymentAmount);
                invoicePaymntsList.add(cardPayment);

            } else if (paymentOption.contains(Payment.COOP_CREDIT)) {//Credit Payment
                logger.info("Payment.COOP_CREDIT selected");
                int customerId = Integer.parseInt(off_set_0);
                CoopCreditPayment cardPayment = new CoopCreditPayment(invoice.getInvoiceNo(), row + 1, customerId, paymentAmount);
                invoicePaymntsList.add(cardPayment);
            } else if (paymentOption.contains(Payment.POSHANA)) {//poshana payment
                logger.info("Payment.POSHANA selected");
                String customerName = off_set_0;
                String stampId = off_set_1;
                PoshanaPayment cardPayment = new PoshanaPayment(invoice.getInvoiceNo(), row + 1, stampId, customerName, paymentAmount);
                invoicePaymntsList.add(cardPayment);
            } else if (paymentOption.contains(Payment.VOUCHER)) {//Voucher payment
                if (customerRadioButton.isSelected()) {
                    logger.info("Payment.VOUCHER.Customer selected");
                    String voucherId = off_set_0;
                    CustomerVoucherPayment voucherPayment = new CustomerVoucherPayment(invoice.getInvoiceNo(), row + 1, voucherId, paymentAmount);
                    invoicePaymntsList.add(voucherPayment);
                }
                if (employeeRadioButton.isSelected()) {
                    logger.info("Payment.VOUCHER.Employee selected");
                    int employeeId = Integer.parseInt(off_set_0);
                    EmployeeVoucherPayment voucherPayment = new EmployeeVoucherPayment(invoice.getInvoiceNo(), row + 1, employeeId, paymentAmount);
                    invoicePaymntsList.add(voucherPayment);
                }
            }
        }
        invoice.setPayments(invoicePaymntsList);
        // </editor-fold>
        //
        // <editor-fold defaultstate="collapsed" desc="Perform Transaction">  
        //Update database- batches, invoice,invoice items,invoice payments(5 tables), counter total
//        logger.warn("Debug invoice print");
//        ReportGenerator.generateInvoice(parent.getCounterLogin(), invoice);
        //
        //
        boolean result = TransactionController.performInvoiceTransaction(invoice, Integer.valueOf(Utilities.loadProperty("counter")));
        if (result) {
            ReportGenerator.generateInvoice(parent.getCounterLogin(), invoice);
            //Reset invoice arraylists
            resetInvoice();
            showAddItemPanel();
            logger.info("Payment complete");
        } else {
            Utilities.showMsgBox("Something went wrong. Invice was not added to the database", "Error", JOptionPane.ERROR_MESSAGE);
        }
        //
        //
        // </editor-fold>
        //
    }
// </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Netbeans generated Code">

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        voucherPaymentBtnGroup = new javax.swing.ButtonGroup();
        jLayeredPane = new javax.swing.JLayeredPane();
        interfaceContainerPanel = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        invoicePanel = new javax.swing.JPanel();
        itemAddPanel = new javax.swing.JPanel();
        itemTableSP = new javax.swing.JScrollPane();
        invoiceItemTable = new javax.swing.JTable();
        btnPayment = new javax.swing.JButton();
        btnInvoiceExit = new javax.swing.JButton();
        productPanel = new javax.swing.JPanel();
        lblCode = new javax.swing.JLabel();
        lblProductDesc = new javax.swing.JLabel();
        lblQty = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        itemCodeComboBox = new javax.swing.JComboBox();
        lblUnit = new javax.swing.JLabel();
        txtProductDesc = new javax.swing.JTextField();
        lblPrice = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        lblProductName = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        lblAvailableQtydisplay = new javax.swing.JLabel();
        txtAvailableQty = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        billSummeryPanel = new javax.swing.JPanel();
        lblItemNoDisplay = new javax.swing.JLabel();
        lblItemCount = new javax.swing.JLabel();
        lblNetDisplay = new javax.swing.JLabel();
        lblNetTotal = new javax.swing.JLabel();
        lblItemDiscountDisplay = new javax.swing.JLabel();
        lblItemDiscounts = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        billButtonPanel = new javax.swing.JPanel();
        btnAddItem = new javax.swing.JButton();
        btnDeleteItem = new javax.swing.JButton();
        btnClearItem = new javax.swing.JButton();
        paymentPanel = new javax.swing.JPanel();
        btnPrevious = new javax.swing.JButton();
        paymentOptionsPanel = new javax.swing.JPanel();
        ivoicePaymentsSP = new javax.swing.JScrollPane();
        invoicePaymentsTable = new javax.swing.JTable();
        paymentInfoPanel = new javax.swing.JPanel();
        lblBillValueDisplay = new javax.swing.JLabel();
        lblBillValueVal = new javax.swing.JLabel();
        lblTotalDisplay = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        lblChangeDisplay = new javax.swing.JLabel();
        lblChangeVal = new javax.swing.JLabel();
        paymentSelectorPanel = new javax.swing.JPanel();
        lblPaymentOption = new javax.swing.JLabel();
        paymentOptionComboBox = new javax.swing.JComboBox();
        paymentDetailsPanel = new javax.swing.JPanel();
        cashPaymentPanel = new javax.swing.JPanel();
        lblCashPaymentAmountDisplay = new javax.swing.JLabel();
        txtCashPaymentAmount = new javax.swing.JTextField();
        cardPaymentPanel = new javax.swing.JPanel();
        lblCashPaymentAmountDisplay1 = new javax.swing.JLabel();
        lblCardType = new javax.swing.JLabel();
        cardTypeComboBox = new javax.swing.JComboBox();
        lblCardNo = new javax.swing.JLabel();
        txtCardPaymentAmount = new javax.swing.JTextField();
        txtcardNo = new javax.swing.JTextField();
        coopCreditPaymentPanel = new javax.swing.JPanel();
        coopCreditCustomernameLbl = new javax.swing.JLabel();
        coopCustomerNameComboBox = new javax.swing.JComboBox();
        coopCreditAmountLbl = new javax.swing.JLabel();
        coopCreditAmountTxt = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        redeemableAmountLbl = new javax.swing.JLabel();
        redeemableAmountTxt = new javax.swing.JLabel();
        PoshanaPayment = new javax.swing.JPanel();
        poshanaPaymentAmountLbl = new javax.swing.JLabel();
        poshanaPaymentAmountTxt = new javax.swing.JTextField();
        poshanaIdLbl = new javax.swing.JLabel();
        poshanaCustomerNameLbl = new javax.swing.JLabel();
        poshanaCustomerNameTxt = new javax.swing.JTextField();
        poshanaIdTxt = new javax.swing.JTextField();
        VoucherPayment = new javax.swing.JPanel();
        employeeRadioButton = new javax.swing.JRadioButton();
        customerRadioButton = new javax.swing.JRadioButton();
        voucherCard = new javax.swing.JPanel();
        customerCard = new javax.swing.JPanel();
        voucherIdLbl = new javax.swing.JLabel();
        voucherCustomerIdTxt = new javax.swing.JTextField();
        amountLbl = new javax.swing.JLabel();
        voucherCustomerAmountTxt = new javax.swing.JTextField();
        employeeCard = new javax.swing.JPanel();
        employeeNameLbl = new javax.swing.JLabel();
        voucherEmployeeNameComboBox = new javax.swing.JComboBox();
        amountEmpLbl = new javax.swing.JLabel();
        voucherEmployeeVoucherAmount = new javax.swing.JTextField();
        btnAddPayment = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();
        lblBill = new javax.swing.JLabel();
        txtBillNumber = new javax.swing.JTextField();
        chkMember = new javax.swing.JCheckBox();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Invoice");
        setMinimumSize(new java.awt.Dimension(1031, 610));
        setPreferredSize(new java.awt.Dimension(1031, 610));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        interfaceContainerPanel.setBorder(dropShadowBorder1);
        interfaceContainerPanel.setOpaque(false);

        cardPanel.setLayout(new java.awt.CardLayout());

        invoicePanel.setLayout(new java.awt.CardLayout());

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder2 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder2.setShowLeftShadow(true);
        dropShadowBorder2.setShowTopShadow(true);
        itemTableSP.setBorder(dropShadowBorder2);

        invoiceItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Batch", "Name", "Description", "Unit Price (Rs.)", "Qty", "Discount value (Rs.)", "Sub total (Rs.)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoiceItemTable.setRowHeight(21);
        invoiceItemTable.getTableHeader().setReorderingAllowed(false);
        itemTableSP.setViewportView(invoiceItemTable);
        if (invoiceItemTable.getColumnModel().getColumnCount() > 0) {
            invoiceItemTable.getColumnModel().getColumn(1).setMinWidth(0);
            invoiceItemTable.getColumnModel().getColumn(1).setPreferredWidth(0);
            invoiceItemTable.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        btnPayment.setText("Payment  > [ F12 ]");
        btnPayment.setToolTipText("");
        btnPayment.setPreferredSize(new java.awt.Dimension(100, 23));
        btnPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentActionPerformed(evt);
            }
        });

        btnInvoiceExit.setText("Exit");
        btnInvoiceExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvoiceExitActionPerformed(evt);
            }
        });

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder3 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder3.setShadowColor(new java.awt.Color(204, 204, 204));
        dropShadowBorder3.setShadowSize(6);
        dropShadowBorder3.setShowLeftShadow(true);
        dropShadowBorder3.setShowTopShadow(true);
        productPanel.setBorder(dropShadowBorder3);

        lblCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCode.setText("Code");

        lblProductDesc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductDesc.setText("Description");

        lblQty.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblQty.setText("Qty.");

        btnSearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSearch.setText("Search [ F5 ] ");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        itemCodeComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        itemCodeComboBox.setMaximumRowCount(5);

        lblUnit.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblUnit.setText("<unit>");
        lblUnit.setToolTipText("");

        txtProductDesc.setEditable(false);
        txtProductDesc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblPrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPrice.setText("Price (Rs.)");

        btnRefresh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setText("Product name");

        txtProductName.setEditable(false);
        txtProductName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblAvailableQtydisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblAvailableQtydisplay.setText("Available Qty.");

        txtAvailableQty.setEditable(false);
        txtAvailableQty.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtAvailableQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtPrice.setEditable(false);
        txtPrice.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPriceFocusLost(evt);
            }
        });

        txtQty.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQtyFocusLost(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtyKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout productPanelLayout = new javax.swing.GroupLayout(productPanel);
        productPanel.setLayout(productPanelLayout);
        productPanelLayout.setHorizontalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProductName, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblQty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblProductDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblAvailableQtydisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtProductName)
                            .addComponent(itemCodeComboBox, 0, 140, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtProductDesc)
                        .addGroup(productPanelLayout.createSequentialGroup()
                            .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtQty, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                .addComponent(txtAvailableQty))
                            .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(productPanelLayout.createSequentialGroup()
                                    .addGap(52, 52, 52)
                                    .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(productPanelLayout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 13, Short.MAX_VALUE))
        );
        productPanelLayout.setVerticalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAvailableQtydisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAvailableQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUnit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        billSummeryPanel.setBackground(new java.awt.Color(204, 204, 204));
        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder4 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder4.setShowLeftShadow(true);
        dropShadowBorder4.setShowTopShadow(true);
        billSummeryPanel.setBorder(dropShadowBorder4);

        lblItemNoDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemNoDisplay.setText("Item(s)");

        lblItemCount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblItemCount.setText("0");
        lblItemCount.setToolTipText("");
        lblItemCount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblNetDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNetDisplay.setText("Net Total (Rs.)");

        lblNetTotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNetTotal.setForeground(new java.awt.Color(204, 0, 51));
        lblNetTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNetTotal.setText("0.00");
        lblNetTotal.setToolTipText("");
        lblNetTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblItemDiscountDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemDiscountDisplay.setText("Item Discount (Rs.)");

        lblItemDiscounts.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemDiscounts.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblItemDiscounts.setText("0.00");
        lblItemDiscounts.setToolTipText("");
        lblItemDiscounts.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout billSummeryPanelLayout = new javax.swing.GroupLayout(billSummeryPanel);
        billSummeryPanel.setLayout(billSummeryPanelLayout);
        billSummeryPanelLayout.setHorizontalGroup(
            billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billSummeryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblItemNoDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblItemCount, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(lblItemDiscountDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblItemDiscounts, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNetDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNetTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        billSummeryPanelLayout.setVerticalGroup(
            billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billSummeryPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNetTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNetDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemDiscounts, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemDiscountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblItemCount, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblItemNoDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder5 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder5.setShadowColor(new java.awt.Color(204, 204, 204));
        dropShadowBorder5.setShadowSize(6);
        dropShadowBorder5.setShowLeftShadow(true);
        dropShadowBorder5.setShowTopShadow(true);
        billButtonPanel.setBorder(dropShadowBorder5);

        btnAddItem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAddItem.setText("Add [Enter]");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        btnDeleteItem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDeleteItem.setText("Delete [Del]");
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });

        btnClearItem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnClearItem.setText("Clear [ESC]");
        btnClearItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout billButtonPanelLayout = new javax.swing.GroupLayout(billButtonPanel);
        billButtonPanel.setLayout(billButtonPanelLayout);
        billButtonPanelLayout.setHorizontalGroup(
            billButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearItem, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        billButtonPanelLayout.setVerticalGroup(
            billButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClearItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout itemAddPanelLayout = new javax.swing.GroupLayout(itemAddPanel);
        itemAddPanel.setLayout(itemAddPanelLayout);
        itemAddPanelLayout.setHorizontalGroup(
            itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(billButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(btnInvoiceExit, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(itemTableSP)
                    .addComponent(billSummeryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        itemAddPanelLayout.setVerticalGroup(
            itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(billButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemTableSP, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billSummeryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInvoiceExit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        invoicePanel.add(itemAddPanel, "itemCard");

        btnPrevious.setText("<  Prevous");
        btnPrevious.setPreferredSize(new java.awt.Dimension(100, 23));
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder6 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder6.setShowLeftShadow(true);
        dropShadowBorder6.setShowTopShadow(true);
        ivoicePaymentsSP.setBorder(dropShadowBorder6);

        invoicePaymentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Payment Option", "Amount (Rs.)", "OffSet_0", "OffSet_1"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoicePaymentsTable.getTableHeader().setReorderingAllowed(false);
        ivoicePaymentsSP.setViewportView(invoicePaymentsTable);
        if (invoicePaymentsTable.getColumnModel().getColumnCount() > 0) {
            invoicePaymentsTable.getColumnModel().getColumn(2).setMinWidth(0);
            invoicePaymentsTable.getColumnModel().getColumn(2).setPreferredWidth(0);
            invoicePaymentsTable.getColumnModel().getColumn(2).setMaxWidth(0);
            invoicePaymentsTable.getColumnModel().getColumn(3).setMinWidth(0);
            invoicePaymentsTable.getColumnModel().getColumn(3).setPreferredWidth(0);
            invoicePaymentsTable.getColumnModel().getColumn(3).setMaxWidth(0);
        }

        javax.swing.GroupLayout paymentOptionsPanelLayout = new javax.swing.GroupLayout(paymentOptionsPanel);
        paymentOptionsPanel.setLayout(paymentOptionsPanelLayout);
        paymentOptionsPanelLayout.setHorizontalGroup(
            paymentOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ivoicePaymentsSP, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
        );
        paymentOptionsPanelLayout.setVerticalGroup(
            paymentOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ivoicePaymentsSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder7 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder7.setShowLeftShadow(true);
        dropShadowBorder7.setShowTopShadow(true);
        paymentInfoPanel.setBorder(dropShadowBorder7);

        lblBillValueDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblBillValueDisplay.setText("Bill Value (Rs.)");

        lblBillValueVal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblBillValueVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillValueVal.setText("0.00");
        lblBillValueVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalDisplay.setText("Total (Rs.)");

        lblTotalVal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalVal.setText("0.00");
        lblTotalVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblChangeDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblChangeDisplay.setForeground(new java.awt.Color(204, 0, 51));
        lblChangeDisplay.setText("Change cash (Rs.)");

        lblChangeVal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblChangeVal.setForeground(new java.awt.Color(204, 0, 51));
        lblChangeVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChangeVal.setText("0.00");
        lblChangeVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout paymentInfoPanelLayout = new javax.swing.GroupLayout(paymentInfoPanel);
        paymentInfoPanel.setLayout(paymentInfoPanelLayout);
        paymentInfoPanelLayout.setHorizontalGroup(
            paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBillValueDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblBillValueVal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblChangeDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblChangeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(lblTotalDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        paymentInfoPanelLayout.setVerticalGroup(
            paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                        .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblChangeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblBillValueDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblBillValueVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                        .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblChangeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder8 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder8.setShowLeftShadow(true);
        dropShadowBorder8.setShowTopShadow(true);
        paymentSelectorPanel.setBorder(dropShadowBorder8);

        lblPaymentOption.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPaymentOption.setText("Payment Type");

        paymentOptionComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paymentOptionComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash", "Credit Card", "Coop Credit", "Poshana", "Voucher" }));
        paymentOptionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentOptionComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paymentSelectorPanelLayout = new javax.swing.GroupLayout(paymentSelectorPanel);
        paymentSelectorPanel.setLayout(paymentSelectorPanelLayout);
        paymentSelectorPanelLayout.setHorizontalGroup(
            paymentSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentSelectorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPaymentOption)
                .addGap(18, 18, 18)
                .addComponent(paymentOptionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        paymentSelectorPanelLayout.setVerticalGroup(
            paymentSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentSelectorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPaymentOption, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentOptionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder9 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder9.setShowLeftShadow(true);
        dropShadowBorder9.setShowTopShadow(true);
        paymentDetailsPanel.setBorder(dropShadowBorder9);
        paymentDetailsPanel.setLayout(new java.awt.CardLayout());

        lblCashPaymentAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashPaymentAmountDisplay.setText("Amount  (Rs.)");

        txtCashPaymentAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCashPaymentAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCashPaymentAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCashPaymentAmountFocusLost(evt);
            }
        });
        txtCashPaymentAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashPaymentAmountKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout cashPaymentPanelLayout = new javax.swing.GroupLayout(cashPaymentPanel);
        cashPaymentPanel.setLayout(cashPaymentPanelLayout);
        cashPaymentPanelLayout.setHorizontalGroup(
            cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCashPaymentAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCashPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(182, Short.MAX_VALUE))
        );
        cashPaymentPanelLayout.setVerticalGroup(
            cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashPaymentAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCashPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(249, Short.MAX_VALUE))
        );

        paymentDetailsPanel.add(cashPaymentPanel, "cashCard");

        lblCashPaymentAmountDisplay1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashPaymentAmountDisplay1.setText("Amount  (Rs.)");

        lblCardType.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCardType.setText("Card Type");

        cardTypeComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AMEX", "MASTER", "VISA" }));
        cardTypeComboBox.setSelectedIndex(-1);
        cardTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardTypeComboBoxActionPerformed(evt);
            }
        });

        lblCardNo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCardNo.setText("Card No ");

        txtCardPaymentAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCardPaymentAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCardPaymentAmount.setEnabled(false);
        txtCardPaymentAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCardPaymentAmountFocusLost(evt);
            }
        });
        txtCardPaymentAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCardPaymentAmountKeyReleased(evt);
            }
        });

        txtcardNo.setEditable(false);
        txtcardNo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtcardNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtcardNo.setEnabled(false);
        txtcardNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcardNoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout cardPaymentPanelLayout = new javax.swing.GroupLayout(cardPaymentPanel);
        cardPaymentPanel.setLayout(cardPaymentPanelLayout);
        cardPaymentPanelLayout.setHorizontalGroup(
            cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                        .addComponent(lblCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                        .addComponent(lblCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                        .addComponent(lblCashPaymentAmountDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCardPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardPaymentPanelLayout.setVerticalGroup(
            cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardType)
                    .addComponent(cardTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardNo)
                    .addComponent(txtcardNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashPaymentAmountDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCardPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(167, Short.MAX_VALUE))
        );

        paymentDetailsPanel.add(cardPaymentPanel, "bankCard");

        coopCreditCustomernameLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        coopCreditCustomernameLbl.setText("Customer Name");

        coopCustomerNameComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        coopCreditAmountLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        coopCreditAmountLbl.setText("Amount  (Rs.)");

        coopCreditAmountTxt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        coopCreditAmountTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        coopCreditAmountTxt.setEnabled(false);
        coopCreditAmountTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                coopCreditAmountTxtFocusLost(evt);
            }
        });
        coopCreditAmountTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                coopCreditAmountTxtKeyReleased(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        redeemableAmountLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        redeemableAmountLbl.setText("Maximum Redeemable Amount ");

        redeemableAmountTxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        redeemableAmountTxt.setText("0.00");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(redeemableAmountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(redeemableAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(122, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(redeemableAmountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(redeemableAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout coopCreditPaymentPanelLayout = new javax.swing.GroupLayout(coopCreditPaymentPanel);
        coopCreditPaymentPanel.setLayout(coopCreditPaymentPanelLayout);
        coopCreditPaymentPanelLayout.setHorizontalGroup(
            coopCreditPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coopCreditPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(coopCreditPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(coopCreditPaymentPanelLayout.createSequentialGroup()
                        .addComponent(coopCreditCustomernameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(coopCustomerNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(coopCreditPaymentPanelLayout.createSequentialGroup()
                        .addComponent(coopCreditAmountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(coopCreditAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        coopCreditPaymentPanelLayout.setVerticalGroup(
            coopCreditPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coopCreditPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(coopCreditPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coopCreditCustomernameLbl)
                    .addComponent(coopCustomerNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(coopCreditPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coopCreditAmountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(coopCreditAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 158, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        paymentDetailsPanel.add(coopCreditPaymentPanel, "coopCreditCard");

        poshanaPaymentAmountLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        poshanaPaymentAmountLbl.setText("Amount  (Rs.)");

        poshanaPaymentAmountTxt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        poshanaPaymentAmountTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        poshanaPaymentAmountTxt.setEnabled(false);

        poshanaIdLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        poshanaIdLbl.setText("Poshana Id");

        poshanaCustomerNameLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        poshanaCustomerNameLbl.setText("Customer Name");

        poshanaCustomerNameTxt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        poshanaCustomerNameTxt.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        poshanaCustomerNameTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                poshanaCustomerNameTxtKeyReleased(evt);
            }
        });

        poshanaIdTxt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        poshanaIdTxt.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        poshanaIdTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                poshanaIdTxtKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout PoshanaPaymentLayout = new javax.swing.GroupLayout(PoshanaPayment);
        PoshanaPayment.setLayout(PoshanaPaymentLayout);
        PoshanaPaymentLayout.setHorizontalGroup(
            PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PoshanaPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PoshanaPaymentLayout.createSequentialGroup()
                        .addComponent(poshanaCustomerNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(poshanaCustomerNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PoshanaPaymentLayout.createSequentialGroup()
                        .addComponent(poshanaIdLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(poshanaIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PoshanaPaymentLayout.createSequentialGroup()
                        .addComponent(poshanaPaymentAmountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(poshanaPaymentAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        PoshanaPaymentLayout.setVerticalGroup(
            PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PoshanaPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(poshanaCustomerNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(poshanaCustomerNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(poshanaIdLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(poshanaIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(poshanaPaymentAmountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(poshanaPaymentAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(167, Short.MAX_VALUE))
        );

        paymentDetailsPanel.add(PoshanaPayment, "poshanaCard");

        voucherPaymentBtnGroup.add(employeeRadioButton);
        employeeRadioButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        employeeRadioButton.setText("Employee");
        employeeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeRadioButtonActionPerformed(evt);
            }
        });

        voucherPaymentBtnGroup.add(customerRadioButton);
        customerRadioButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        customerRadioButton.setSelected(true);
        customerRadioButton.setText("Customer");
        customerRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerRadioButtonActionPerformed(evt);
            }
        });

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder10 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder10.setShadowColor(new java.awt.Color(102, 102, 102));
        dropShadowBorder10.setShowLeftShadow(true);
        dropShadowBorder10.setShowTopShadow(true);
        voucherCard.setBorder(dropShadowBorder10);
        voucherCard.setLayout(new java.awt.CardLayout());

        voucherIdLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        voucherIdLbl.setText("Voucher Id");

        voucherCustomerIdTxt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        voucherCustomerIdTxt.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        voucherCustomerIdTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                voucherCustomerIdTxtKeyReleased(evt);
            }
        });

        amountLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        amountLbl.setText("Amount  (Rs.)");

        voucherCustomerAmountTxt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        voucherCustomerAmountTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        voucherCustomerAmountTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                voucherCustomerAmountTxtFocusLost(evt);
            }
        });
        voucherCustomerAmountTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                voucherCustomerAmountTxtKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout customerCardLayout = new javax.swing.GroupLayout(customerCard);
        customerCard.setLayout(customerCardLayout);
        customerCardLayout.setHorizontalGroup(
            customerCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(customerCardLayout.createSequentialGroup()
                        .addComponent(voucherIdLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(voucherCustomerIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(customerCardLayout.createSequentialGroup()
                        .addComponent(amountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(voucherCustomerAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        customerCardLayout.setVerticalGroup(
            customerCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(voucherIdLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(voucherCustomerIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(customerCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(amountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(voucherCustomerAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(148, Short.MAX_VALUE))
        );

        voucherCard.add(customerCard, "customerCard");

        employeeNameLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        employeeNameLbl.setText("Employee Name");

        voucherEmployeeNameComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        amountEmpLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        amountEmpLbl.setText("Amount  (Rs.)");

        voucherEmployeeVoucherAmount.setEditable(false);
        voucherEmployeeVoucherAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        voucherEmployeeVoucherAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        voucherEmployeeVoucherAmount.setEnabled(false);

        javax.swing.GroupLayout employeeCardLayout = new javax.swing.GroupLayout(employeeCard);
        employeeCard.setLayout(employeeCardLayout);
        employeeCardLayout.setHorizontalGroup(
            employeeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(employeeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(employeeCardLayout.createSequentialGroup()
                        .addComponent(employeeNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(voucherEmployeeNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(employeeCardLayout.createSequentialGroup()
                        .addComponent(amountEmpLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(voucherEmployeeVoucherAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        employeeCardLayout.setVerticalGroup(
            employeeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(employeeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeNameLbl)
                    .addComponent(voucherEmployeeNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(employeeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(amountEmpLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(voucherEmployeeVoucherAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(148, Short.MAX_VALUE))
        );

        voucherCard.add(employeeCard, "employeeCard");

        javax.swing.GroupLayout VoucherPaymentLayout = new javax.swing.GroupLayout(VoucherPayment);
        VoucherPayment.setLayout(VoucherPaymentLayout);
        VoucherPaymentLayout.setHorizontalGroup(
            VoucherPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VoucherPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VoucherPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(voucherCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(VoucherPaymentLayout.createSequentialGroup()
                        .addComponent(customerRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(employeeRadioButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        VoucherPaymentLayout.setVerticalGroup(
            VoucherPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VoucherPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VoucherPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeRadioButton)
                    .addComponent(customerRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(voucherCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        paymentDetailsPanel.add(VoucherPayment, "voucherCard");

        btnAddPayment.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAddPayment.setText("Add Payment [ F3 ]");
        btnAddPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPaymentActionPerformed(evt);
            }
        });

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnConfirm.setText("Confirm [ Enter ]");
        btnConfirm.setPreferredSize(new java.awt.Dimension(100, 23));
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paymentPanelLayout = new javax.swing.GroupLayout(paymentPanel);
        paymentPanel.setLayout(paymentPanelLayout);
        paymentPanelLayout.setHorizontalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paymentInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paymentPanelLayout.createSequentialGroup()
                        .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addComponent(btnAddPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paymentDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(paymentSelectorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(paymentOptionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        paymentPanelLayout.setVerticalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addComponent(paymentSelectorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(paymentOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        invoicePanel.add(paymentPanel, "paymentCard");

        cardPanel.add(invoicePanel, "invoiceCard");

        lblBill.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBill.setText("Bill Number : ");

        txtBillNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtBillNumber.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBillNumber.setToolTipText("");
        txtBillNumber.setEnabled(false);

        chkMember.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkMember.setText("Member");
        chkMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMemberActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout interfaceContainerPanelLayout = new javax.swing.GroupLayout(interfaceContainerPanel);
        interfaceContainerPanel.setLayout(interfaceContainerPanelLayout);
        interfaceContainerPanelLayout.setHorizontalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(interfaceContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBill)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBillNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(chkMember)
                .addContainerGap())
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        interfaceContainerPanelLayout.setVerticalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, interfaceContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBill)
                    .addComponent(txtBillNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkMember))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jLayeredPaneLayout = new javax.swing.GroupLayout(jLayeredPane);
        jLayeredPane.setLayout(jLayeredPaneLayout);
        jLayeredPaneLayout.setHorizontalGroup(
            jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1015, Short.MAX_VALUE)
            .addGroup(jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPaneLayout.setVerticalGroup(
            jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
            .addGroup(jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane.setLayer(interfaceContainerPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
        // TODO add your handling code here:
        showPaymentPanel();
    }//GEN-LAST:event_btnPaymentActionPerformed

    private void btnInvoiceExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvoiceExitActionPerformed
        // TODO add your handling code here:'
        cancelBill();
    }//GEN-LAST:event_btnInvoiceExitActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        // TODO add your handling code here:
        addItemToInvoiceItemTable();
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        // TODO add your handling code here:
        deleteItemFromBillItemTable();
    }//GEN-LAST:event_btnDeleteItemActionPerformed

    private void btnClearItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearItemActionPerformed
        // TODO add your handling code here:
        invoiceClearProductinfo();
    }//GEN-LAST:event_btnClearItemActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        searchItem();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        showAddItemPanel();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void paymentOptionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentOptionComboBoxActionPerformed
        // TODO add your handling code here:
        togglePaymentOptions();
    }//GEN-LAST:event_paymentOptionComboBoxActionPerformed

    private void btnAddPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPaymentActionPerformed
        // TODO add your handling code here:
        paymentChooser();
    }//GEN-LAST:event_btnAddPaymentActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        removePaymentOption();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        // TODO add your handling code here:
        confirmInvoice();
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        loadSellebleProducts();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txtPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPriceFocusLost
        // TODO add your handling code here:
        doubleFormatComponentText(txtPrice);
    }//GEN-LAST:event_txtPriceFocusLost

    private void txtQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusLost
        // TODO add your handling code here:
        doubleFormatComponentText(txtQty);
    }//GEN-LAST:event_txtQtyFocusLost

    private void txtCashPaymentAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCashPaymentAmountFocusLost
        // TODO add your handling code here:
        doubleFormatComponentText(txtCashPaymentAmount);
    }//GEN-LAST:event_txtCashPaymentAmountFocusLost

    private void txtCardPaymentAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCardPaymentAmountFocusLost
        // TODO add your handling code here:
        doubleFormatComponentText(txtCardPaymentAmount);
    }//GEN-LAST:event_txtCardPaymentAmountFocusLost

    private void txtQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyReleased
        // TODO add your handling code here:
        txtQtyKeyPressHandler(evt);
    }//GEN-LAST:event_txtQtyKeyReleased

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        resetInvoice();
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtCashPaymentAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashPaymentAmountKeyReleased
        // TODO add your handling code here:
        txtPaymentKeyPressHandler(evt);
    }//GEN-LAST:event_txtCashPaymentAmountKeyReleased

    private void txtCardPaymentAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCardPaymentAmountKeyReleased
        // TODO add your handling code here:
        txtPaymentKeyPressHandler(evt);
    }//GEN-LAST:event_txtCardPaymentAmountKeyReleased

    private void cardTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardTypeComboBoxActionPerformed
        // TODO add your handling code here:
        toggleCardType();
    }//GEN-LAST:event_cardTypeComboBoxActionPerformed

    private void txtcardNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcardNoKeyReleased
        // TODO add your handling code here:
        txtCardNoKeyPressHandler(evt);
    }//GEN-LAST:event_txtcardNoKeyReleased

    private void chkMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMemberActionPerformed
        // TODO add your handling code here:
        convertToMemberInvoice();
    }//GEN-LAST:event_chkMemberActionPerformed

    private void poshanaCustomerNameTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_poshanaCustomerNameTxtKeyReleased
        // TODO add your handling code here:
        txtPoshanaCustomerNamePressHandler(evt);
    }//GEN-LAST:event_poshanaCustomerNameTxtKeyReleased

    private void poshanaIdTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_poshanaIdTxtKeyReleased
        // TODO add your handling code here:
        txtPoshanaIdPressHandler(evt);
    }//GEN-LAST:event_poshanaIdTxtKeyReleased

    private void coopCreditAmountTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_coopCreditAmountTxtFocusLost
        // TODO add your handling code here:
        doubleFormatComponentText(coopCreditAmountTxt);
    }//GEN-LAST:event_coopCreditAmountTxtFocusLost

    private void coopCreditAmountTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coopCreditAmountTxtKeyReleased
        // TODO add your handling code here:
        txtPaymentKeyPressHandler(evt);
    }//GEN-LAST:event_coopCreditAmountTxtKeyReleased

    private void employeeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeRadioButtonActionPerformed
        // TODO add your handling code here:
        toggleVoucherType();
    }//GEN-LAST:event_employeeRadioButtonActionPerformed

    private void customerRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerRadioButtonActionPerformed
        // TODO add your handling code here:
        toggleVoucherType();
    }//GEN-LAST:event_customerRadioButtonActionPerformed

    private void voucherCustomerIdTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_voucherCustomerIdTxtKeyReleased
        // TODO add your handling code here:
        txtVoucherCustomerIdPressHandler(evt);
    }//GEN-LAST:event_voucherCustomerIdTxtKeyReleased

    private void voucherCustomerAmountTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_voucherCustomerAmountTxtFocusLost
        // TODO add your handling code here:
        doubleFormatComponentText(voucherCustomerAmountTxt);
    }//GEN-LAST:event_voucherCustomerAmountTxtFocusLost

    private void voucherCustomerAmountTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_voucherCustomerAmountTxtKeyReleased
        // TODO add your handling code here:
        txtPaymentKeyPressHandler(evt);
    }//GEN-LAST:event_voucherCustomerAmountTxtKeyReleased

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cancelBill();
    }//GEN-LAST:event_formInternalFrameClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PoshanaPayment;
    private javax.swing.JPanel VoucherPayment;
    private javax.swing.JLabel amountEmpLbl;
    private javax.swing.JLabel amountLbl;
    private javax.swing.JPanel billButtonPanel;
    private javax.swing.JPanel billSummeryPanel;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnAddPayment;
    private javax.swing.JButton btnClearItem;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnInvoiceExit;
    private javax.swing.JButton btnPayment;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JPanel cardPaymentPanel;
    private javax.swing.JComboBox cardTypeComboBox;
    private javax.swing.JPanel cashPaymentPanel;
    private javax.swing.JCheckBox chkMember;
    private javax.swing.JLabel coopCreditAmountLbl;
    private javax.swing.JTextField coopCreditAmountTxt;
    private javax.swing.JLabel coopCreditCustomernameLbl;
    private javax.swing.JPanel coopCreditPaymentPanel;
    private javax.swing.JComboBox coopCustomerNameComboBox;
    private javax.swing.JPanel customerCard;
    private javax.swing.JRadioButton customerRadioButton;
    private javax.swing.JPanel employeeCard;
    private javax.swing.JLabel employeeNameLbl;
    private javax.swing.JRadioButton employeeRadioButton;
    private javax.swing.JPanel interfaceContainerPanel;
    private javax.swing.JTable invoiceItemTable;
    private javax.swing.JPanel invoicePanel;
    private javax.swing.JTable invoicePaymentsTable;
    private javax.swing.JPanel itemAddPanel;
    private javax.swing.JComboBox itemCodeComboBox;
    private javax.swing.JScrollPane itemTableSP;
    private javax.swing.JScrollPane ivoicePaymentsSP;
    private javax.swing.JLayeredPane jLayeredPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAvailableQtydisplay;
    private javax.swing.JLabel lblBill;
    private javax.swing.JLabel lblBillValueDisplay;
    private javax.swing.JLabel lblBillValueVal;
    private javax.swing.JLabel lblCardNo;
    private javax.swing.JLabel lblCardType;
    private javax.swing.JLabel lblCashPaymentAmountDisplay;
    private javax.swing.JLabel lblCashPaymentAmountDisplay1;
    private javax.swing.JLabel lblChangeDisplay;
    private javax.swing.JLabel lblChangeVal;
    private javax.swing.JLabel lblCode;
    private javax.swing.JLabel lblItemCount;
    private javax.swing.JLabel lblItemDiscountDisplay;
    private javax.swing.JLabel lblItemDiscounts;
    private javax.swing.JLabel lblItemNoDisplay;
    private javax.swing.JLabel lblNetDisplay;
    private javax.swing.JLabel lblNetTotal;
    private javax.swing.JLabel lblPaymentOption;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblProductDesc;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblQty;
    private javax.swing.JLabel lblTotalDisplay;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JPanel paymentDetailsPanel;
    private javax.swing.JPanel paymentInfoPanel;
    private javax.swing.JComboBox paymentOptionComboBox;
    private javax.swing.JPanel paymentOptionsPanel;
    private javax.swing.JPanel paymentPanel;
    private javax.swing.JPanel paymentSelectorPanel;
    private javax.swing.JLabel poshanaCustomerNameLbl;
    private javax.swing.JTextField poshanaCustomerNameTxt;
    private javax.swing.JLabel poshanaIdLbl;
    private javax.swing.JTextField poshanaIdTxt;
    private javax.swing.JLabel poshanaPaymentAmountLbl;
    private javax.swing.JTextField poshanaPaymentAmountTxt;
    private javax.swing.JPanel productPanel;
    private javax.swing.JLabel redeemableAmountLbl;
    private javax.swing.JLabel redeemableAmountTxt;
    private javax.swing.JTextField txtAvailableQty;
    private javax.swing.JTextField txtBillNumber;
    private javax.swing.JTextField txtCardPaymentAmount;
    private javax.swing.JTextField txtCashPaymentAmount;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProductDesc;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtcardNo;
    private javax.swing.JPanel voucherCard;
    private javax.swing.JTextField voucherCustomerAmountTxt;
    private javax.swing.JTextField voucherCustomerIdTxt;
    private javax.swing.JComboBox voucherEmployeeNameComboBox;
    private javax.swing.JTextField voucherEmployeeVoucherAmount;
    private javax.swing.JLabel voucherIdLbl;
    private javax.swing.ButtonGroup voucherPaymentBtnGroup;
    // End of variables declaration//GEN-END:variables
 // </editor-fold>
}
