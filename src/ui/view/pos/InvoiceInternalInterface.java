/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.pos;

import controller.inventory.BatchController;
import util.KeyValueContainer;
import controller.inventory.BatchDiscountController;
import controller.inventory.CategoryController;
import controller.inventory.CategoryDiscountController;
import controller.inventory.ProductController;
import controller.pos.InvoiceController;
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
import java.util.HashMap;
import java.util.IllegalFormatConversionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;
import model.inventory.Batch;
import model.inventory.BatchDiscount;
import model.inventory.Category;
import model.inventory.CategoryDiscount;
import model.inventory.Product;
import model.pos.CardPayment;
import model.pos.CashPayment;
import model.pos.Invoice;
import model.pos.InvoiceItem;
import model.pos.Payment;
import org.apache.log4j.Logger;
import util.CharactorLimitDocument;
import util.DoubleFilter;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class InvoiceInternalInterface extends javax.swing.JInternalFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(InvoiceInternalInterface.class);

    private final POSMDIInterface parent;
    private final JDesktopPane desktopPane;

    private SearchItemInterface searchItemInterface;
    private SelectPriceInterface selectPriceInterface;

    DefaultComboBoxModel productComboBoxModel;
    ActionListener productCodeListner;
    DefaultTableModel invoiceItemTableModel;
    DefaultTableModel invoicePaymentsTableModel;

    //Glass pane
    private final JPanel glassPanel;
    private final JLabel padding;

    //
    //--  Invoice
    private Invoice invoice;
    private Product processingProduct;
    private HashMap<Integer, Product> availableProductMap;

    private final int PRODUCT_ID_COLUMN = 0;
    private final int BATCH_ID_COLUMN = 1;
    private final int UNIT_PRICE_COLUMN = 4;
    private final int UNIT_QTY_COLUMN = 5;
    private final int NET_DISCOUNT_COLUMN = 7;
    private final int NET_TOTAL_COLUMN = 8;

    private final int PAYMENT_OPTION_COLUMN = 0;
    private final int PAYMENT_AMOUNT_COLUMN = 1;
    private final int PAYMENT_OFFSET_0_COLUMN = 2;
    private final int PAYMENT_OFFSET_1_COLUMN = 3;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Creates new form InvoiceInterface
     *
     * @param parent
     * @param desktopPane
     */
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

        ((PlainDocument) txtPrice.getDocument()).setDocumentFilter(new DoubleFilter());
        ((PlainDocument) txtQty.getDocument()).setDocumentFilter(new DoubleFilter());

        ((PlainDocument) txtCardPaymentAmount.getDocument()).setDocumentFilter(new DoubleFilter());

        ((PlainDocument) txtCashPaymentAmount.getDocument()).setDocumentFilter(new DoubleFilter());

        ((DefaultTableCellRenderer) invoiceItemTable.getDefaultRenderer(Object.class)).setHorizontalAlignment(JLabel.RIGHT);
        ((DefaultTableCellRenderer) invoicePaymentsTable.getDefaultRenderer(Object.class)).setHorizontalAlignment(JLabel.RIGHT);

        txtPrice.setEnabled(false);
        txtQty.setEnabled(false);

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

        showNewInvoiceId();
        loadSellebleProducts();
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Helper Methods">
    //Get last billID
    public static int getLastInvoicelId() throws SQLException {
        Invoice invoice = InvoiceController.getLastInvoiceId();
        if (invoice != null) {
            return invoice.getInvoiceNo();
        }
        return 0;
    }

    public static ArrayList<Product> getAllProducts() throws SQLException {
        return ProductController.getAllProducts();
    }

    public static ArrayList<Product> getAllSellebleProducts() throws SQLException {
        ArrayList<Product> products = ProductController.getAllAvailableProducts();
        ArrayList<Product> availableProducts = new ArrayList();

        for (Product product : products) {
            product.setBatches(BatchController.getAllAvailableBatches(product.getProductId()));
            if (product.getBatches().size() > 0) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

    //Price selection ui will call this to set the price
    public void setProductBatch(Batch selectedBatch) {
        logger.debug("setProductBatch invoked");

        processingProduct.setSelectedBatch(selectedBatch);
        setPropFromBatch();
        txtQty.requestFocus();
    }

    //Disable the glassPanel pane
    public void disableGlassPane(boolean disableSearchInterface) {
        logger.debug("disableGlassPane invoked");

        glassPanel.setVisible(false);

        if (disableSearchInterface && searchItemInterface != null) {
            searchItemInterface.disableGlassPane();
        }
    }

    //Enable the glassPanel pane
    public void enableGlassPane(boolean enableSearchInterface) {
        logger.debug("enableGlassPane invoked");
        glassPanel.setVisible(true);//Disable this UI
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
        itemCodeComboBox.setSelectedItem(null);
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

    //Clean item add card
    private void cleanItemAddUI() {
        logger.debug("cleanItemAddUI invoked");

        invoiceClearProductinfo();
        invoiceItemTableModel.setRowCount(0);
        lblItemCount.setText("");
        lblItemDiscounts.setText("");
        lblNetTotal.setText("");
    }

    //Clean payments card 
    private void cleanPaymentsUI() {
        logger.debug("cleanPaymentsUI invoked");

        invoicePaymentsTableModel.setRowCount(0);

        txtCashPaymentAmount.setText("");

        cardTypeComboBox.setSelectedIndex(-1);
        txtcardNo.setText(" ");
        txtCardPaymentAmount.setText("0.00");
        txtcardNo.setEnabled(false);
        txtCardPaymentAmount.setEnabled(false);

        lblBillValueVal.setText("");
        lblChangeVal.setText("");
        lblTotalVal.setText("");
    }

    //Get the product price from the appropriate batch
    private void setPropFromBatch() {
        logger.debug("setPropFromBatch invoked");

        txtPrice.setText(String.format("%.2f", processingProduct.getSelectedBatch().getUnitPrice()));
        txtAvailableQty.setText(String.format("%.2f", processingProduct.getSelectedBatch().getQuantity()));

    }

    //double Format lable 
    private void doubleFormatComponentText(JLabel jLabel) {
        logger.debug("formatlblBillValueAmount invoked for jLabel");

        if (jLabel.getText().equals("") || jLabel.getText().isEmpty()) {
            return;
        }

        try {
            jLabel.setText(String.format("%.2f", Double.parseDouble(jLabel.getText())));
        } catch (NumberFormatException | IllegalFormatConversionException ex) {
            jLabel.setText("");
        }
    }

    //double Format text filed 
    private void doubleFormatComponentText(JTextField jTextField) {
        logger.debug("formatlblBillValueAmount invoked for jTextField");

        if (jTextField.getText().equals("") || jTextField.getText().isEmpty()) {
            return;
        }

        try {
            jTextField.setText(String.format("%.2f", Double.parseDouble(jTextField.getText())));
        } catch (NumberFormatException | IllegalFormatConversionException ex) {
            jTextField.setText("");
        }
    }

    //Recalculate the invoice item parameters
    private void calculateItemParameters() {
        logger.debug("calculateInvoiceParameters invoked");

        double netTotal = 0;
        double netDiscounts = 0;
        int totalItemCount = 0;

        for (int row = 0; row < invoiceItemTable.getRowCount(); row++) {
            netTotal += Double.parseDouble(invoiceItemTable.getValueAt(row, NET_TOTAL_COLUMN).toString());
            netDiscounts += Double.parseDouble((invoiceItemTable.getValueAt(row, NET_DISCOUNT_COLUMN).toString().split(" "))[0]);
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
    }

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
            addPaymentOption();
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

        CardLayout card = (CardLayout) invoicePanel.getLayout();
        card.previous(invoicePanel);
    }

    //Show the payment panel in the bill
    private void showPaymentPanel() {
        logger.debug("showPaymentScreen invoked");

        calculateItemParameters();
        if (invoice == null || invoice.getItemCount() < 1 || invoice.getNetTotal() < 0) {
            logger.warn("invoice must have at least one item and total must be >=0");
            // return;
        }

        calculatePaymentParameters();

        paymentOptionComboBox.setSelectedIndex(0);

        lblBillValueVal.setText(String.format("%.2f", invoice.getNetTotal()));

        CardLayout invoiceCard = (CardLayout) invoicePanel.getLayout();
        invoiceCard.next(invoicePanel);

    }

    //Clear items card as well as payments card
    private void resetInvoice() {
        logger.debug("resetInvoice invoked");

        this.processingProduct = null;

        cleanItemAddUI();
        cleanPaymentsUI();
        showNewInvoiceId();
        loadSellebleProducts();
    }

    //Cancel current bill and show welocme screen
    private void cancelBill() {
        logger.debug("cancelBill invoked");

        parent.setIsMainActivityRunning(false);
        parent.setIsInvoiceRunning(false);
        this.dispose();
    }

    //Search a item
    private void searchItem() {
        logger.debug("searchItem invoked");
        if (searchItemInterface == null) {
            searchItemInterface = new SearchItemInterface(desktopPane, this);
        } else {
            desktopPane.remove(searchItemInterface);
        }
        desktopPane.add(searchItemInterface, new Integer(20));//On top of all internal frames
        searchItemInterface.setVisible(true);

    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Add item System">
    //Method to get next invoice Id
    private void showNewInvoiceId() {
        logger.debug("showNextInvoiceId invoked");

        try {
            invoice = new Invoice(getLastInvoicelId() + 1);
            invoice.setUserName(parent.getUserName());
            invoice.setCounterId(Integer.valueOf(Utilities.loadProperty("counter")));
            invoice.setDate(Utilities.getCurrentDate());
            invoice.setTime(Utilities.getCurrentTime(true));
            txtBillNumber.setText(Utilities.formatId("B", 5, invoice.getInvoiceNo()));

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

            availableProducts.stream().forEach((product) -> {
                availableProductMap.put(product.getProductId(), product);
                productComboBoxModel.addElement(new KeyValueContainer(product.getProductId(), util.Utilities.formatId("P", 4, product.getProductId())));
            });
            itemCodeComboBox.getModel().setSelectedItem(null);

            itemCodeComboBox.setModel(productComboBoxModel);
            // AutoCompleteDecorator.decorate(itemCodeComboBox);

            itemCodeComboBox.addActionListener(productCodeListner);
            invoiceClearProductinfo();

        } catch (SQLException ex) {
            logger.error("Product load error : " + ex.getMessage());
        }
    }

    //Display selected product details
    private void showProductDetails() {
        logger.debug("showProductDetails invoked");

        if (itemCodeComboBox.getSelectedIndex() > -1) {

            KeyValueContainer productComboItem = (KeyValueContainer) itemCodeComboBox.getSelectedItem();
            int productId = productComboItem.getKey();

            try {
                //Create cloned object
                processingProduct = (Product) util.Utilities.deepClone(availableProductMap.get(productId));

                txtProductName.setText(processingProduct.getName());
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
            if (processingProduct == null || txtQty.getText().isEmpty()) {
                logger.warn("Empty processingProduct or Qty");
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

            if (qty <= 0.00 || qty > processingProduct.getSelectedBatch().getQuantity()) {
                Utilities.showMsgBox("Invalid item quantity ", "Incorrect quantity", JOptionPane.WARNING_MESSAGE);
                txtQty.setText("");
                txtQty.requestFocus();
                return;
            }
            if (processingProduct.getUnit().toLowerCase().equals("bulk") && qty != Math.rint(qty)) {
                Utilities.showMsgBox("Bulk item cannot be a fraction", "Incorrect quantity", JOptionPane.WARNING_MESSAGE);
                txtQty.setText("");
                txtQty.requestFocus();
                return;
            }
            // </editor-fold>
            //
            // <editor-fold defaultstate="collapsed" desc="Category Discount">  
            //get the category discount
            double categorydDis = 0;
            //get category and check if discount available ,if so get the category discount
            Category category = CategoryController.getCategory(processingProduct.getDepartmentId(), processingProduct.getCategoryId());
            if (category.isDiscounted()) {
                CategoryDiscount categoryDiscount = CategoryDiscountController.getCategoryDiscount(processingProduct.getDepartmentId(), processingProduct.getCategoryId());

                if (categoryDiscount != null
                        && (util.Utilities.isDateBetweenRange(util.Utilities.getCurrentDate(), categoryDiscount.getStartDate(), categoryDiscount.getEndDate()))) {

                    categorydDis = categoryDiscount.getDiscount() / 100;
                    logger.info("Category discount :" + categorydDis);
                    //Check discount validity
                } else {
                    logger.info("No category discounts found");
                }
            } else {
                logger.info("Category discount disabled");
            }
            // </editor-fold>
            //
            // <editor-fold defaultstate="collapsed" desc="Batch Discount">  
            //get the batch discount
            double batchDis = 0;
            if (processingProduct.getSelectedBatch().isDiscounted()) {
                BatchDiscount batchDiscount = BatchDiscountController.getBatchDiscount(processingProduct.getProductId(), processingProduct.getSelectedBatch().getBatchId());
                if (batchDiscount != null
                        && (util.Utilities.isDateBetweenRange(util.Utilities.getCurrentDate(), batchDiscount.getStartDate(), batchDiscount.getEndDate()))) {

                    processingProduct.getSelectedBatch().setBatchDiscount(batchDiscount);
                    batchDis = batchDiscount.getDiscount() / 100;
                    logger.info("batch discount :" + batchDis);
                } else {
                    logger.info("No batch discounts found");
                }
            } else {
                logger.info("batch discount disabled");
            }
            // </editor-fold>
            //
            // <editor-fold defaultstate="collapsed" desc="Prepare product parameters">  
            double totalDiscount = categorydDis + batchDis;

            int productId = processingProduct.getProductId();
            int batchId = processingProduct.getSelectedBatch().getBatchId();

            double itemDiscount = unitPrice * qty * totalDiscount;
            double subTotal = unitPrice * qty * (1 - totalDiscount);
            // </editor-fold>
            //
            // <editor-fold defaultstate="collapsed" desc="Add item to the table">  

            //if item is already in the table update it
            boolean itemInTable = false;
            for (int row = 0; row < invoiceItemTableModel.getRowCount(); row++) {
                if (((KeyValueContainer) invoiceItemTable.getValueAt(row, PRODUCT_ID_COLUMN)).getKey() == productId
                        && Integer.parseInt(invoiceItemTable.getValueAt(row, BATCH_ID_COLUMN).toString()) == batchId) {

                    itemInTable = true;

                    double qtyInTable = Double.parseDouble(invoiceItemTable.getValueAt(row, UNIT_QTY_COLUMN).toString());
                    double discountInTable = Double.parseDouble(invoiceItemTable.getValueAt(row, NET_DISCOUNT_COLUMN).toString());
                    double subTotalInTable = Double.parseDouble(invoiceItemTable.getValueAt(row, NET_TOTAL_COLUMN).toString());

                    double newQty = qtyInTable + qty;

                    if (newQty > processingProduct.getSelectedBatch().getQuantity()) {
                        Utilities.showMsgBox("Total item quantity exceeds what is in the stock !", "Incorrect quantity", JOptionPane.WARNING_MESSAGE);
                        txtQty.requestFocus();
                        return;
                    }
                    double newTotalDiscount = discountInTable + itemDiscount;
                    double newSubTotal = subTotalInTable + subTotal;

                    invoiceItemTable.setValueAt(String.format("%.2f", newQty), row, UNIT_QTY_COLUMN);//Qty
                    invoiceItemTable.setValueAt(String.format("%.2f", newTotalDiscount), row, NET_DISCOUNT_COLUMN);//Discount value
                    invoiceItemTable.setValueAt(String.format("%.2f", newSubTotal), row, NET_TOTAL_COLUMN);//Sub total
                    break;
                }
            }

            if (!itemInTable) {

                String name = processingProduct.getName();
                String description = processingProduct.getDescription();

                Object[] ob = {
                    new KeyValueContainer(productId, util.Utilities.formatId("P", 4, productId)),
                    batchId,
                    name,
                    description,
                    String.format("%.2f", unitPrice),
                    String.format("%.2f", qty),
                    String.format("%.2f", totalDiscount * 100) + "%",
                    String.format("%.2f", itemDiscount),
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
    private void togglePaymentOptions() {
        logger.debug("togglePaymentOptions invoked");

        //
        // When showing payment option remaining amount should be shown automatically
        //
        double remainingAmount = invoice.getNetTotal() - invoice.getAmountPaid();

        CardLayout paymentMethodCard = (CardLayout) paymentDetailsPanel.getLayout();
        String selectedOption = (String) paymentOptionComboBox.getSelectedItem();
        if (selectedOption != null && !selectedOption.isEmpty()) {
            switch (selectedOption) {
                case Payment.CASH:
                    calculatePaymentParameters();

                    paymentMethodCard.show(paymentDetailsPanel, "cashCard");
                    break;
                case Payment.CREDIT_CARD:
                    calculatePaymentParameters();
                    paymentMethodCard.show(paymentDetailsPanel, "bankCard");
                    break;
                case Payment.COOP_CRDIT:
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

            if (selectedOption.equals(CardPayment.AMEX) || selectedOption.equals(CardPayment.MASTER) || selectedOption.equals(CardPayment.MASTER)) {
                txtcardNo.setEnabled(true);
                txtCardPaymentAmount.setEnabled(true);

                txtcardNo.requestFocus();
            }
        }
    }

    //Add a payment option
    private void addPaymentOption() {
        logger.debug("addPaymentOption invoked");

        if (invoice.getAmountPaid() - invoice.getNetTotal() >= 0) {
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
                case Payment.COOP_CRDIT:
                    handleCoopCreditPayment();
                    break;
                case Payment.POSHANA:
                    handlePoshanaPayment();
                    break;
                case Payment.VOUCHER:
                    handleVoucherPayment();
                    break;
            }
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

        calculatePaymentParameters();
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
        txtcardNo.setText(" ");
        txtCardPaymentAmount.setText("0.00");
        txtcardNo.setEnabled(false);
        txtCardPaymentAmount.setEnabled(false);
        calculatePaymentParameters();

//Reset the credit card
    }

    private void handleCoopCreditPayment() {
        logger.warn("handleCoopCreditPayment not implemented");
    }

    private void handlePoshanaPayment() {
        logger.warn("handlePoshanaPayment not implemented");
    }

    private void handleVoucherPayment() {
        logger.warn("handleVoucherPayment not implemented");
    }

    //Remove a payment option
    private void removePaymentOption() {
        logger.debug("removePaymentOption invoked");

        if (invoicePaymentsTable.getSelectedRow() != -1) {
            invoicePaymentsTableModel.removeRow(invoicePaymentsTable.getSelectedRow());
            if ((invoicePaymentsTable.getRowCount() - 1) > -1) {
                invoicePaymentsTable.setRowSelectionInterval((invoicePaymentsTable.getRowCount() - 1), (invoicePaymentsTable.getRowCount() - 1));
            }
        }
        calculatePaymentParameters();
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
                    ((KeyValueContainer) invoiceItemTable.getValueAt(row, PRODUCT_ID_COLUMN)).getKey(),
                    Integer.parseInt(invoiceItemTable.getValueAt(row, BATCH_ID_COLUMN).toString()),
                    Double.parseDouble(invoiceItemTable.getValueAt(row, UNIT_PRICE_COLUMN).toString()),
                    Double.parseDouble(invoiceItemTable.getValueAt(row, UNIT_QTY_COLUMN).toString()),
                    Double.parseDouble(invoiceItemTable.getValueAt(row, NET_DISCOUNT_COLUMN).toString()),
                    Double.parseDouble(invoiceItemTable.getValueAt(row, UNIT_PRICE_COLUMN).toString())
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
//                if (changeAmount > 0 && paymentAmount < invoice.getAmountPaid()) {//Confirm excess pay is only by cash 
//                    Utilities.showMsgBox("Excess can be paid only in cash", "Incorrect payment", JOptionPane.WARNING_MESSAGE);
//                    logger.warn("Excess can be paid only in cash");
//                    return;
//                }
                CashPayment cashPayment = new CashPayment(invoice.getInvoiceNo(), row + 1, paymentAmount, changeAmount);
                invoicePaymntsList.add(cashPayment);

            } else if (paymentOption.contains(Payment.CREDIT_CARD)) {//Coop credit payment
                String cardType = off_set_0;
                String cardNO = off_set_1;
                CardPayment cardPayment = new CardPayment(invoice.getInvoiceNo(), row + 1, cardType, cardNO, paymentAmount);
                invoicePaymntsList.add(cardPayment);

            } else if (paymentOption.contains(Payment.COOP_CRDIT)) {//Credit Payment
                logger.error("COOP_CRDIT Not implemented yet");
            } else if (paymentOption.contains(Payment.POSHANA)) {//poshana payment
                logger.error("POSHANA Not implemented yet");
            } else if (paymentOption.contains(Payment.VOUCHER)) {//Voucher payment
                logger.error("VOUCHER Not implemented yet");
            }
        }
        invoice.setPayments(invoicePaymntsList);
        // </editor-fold>
        //
        //Update database- batches, invoice,invoice items,invoice payments(5 tables), counter total,
        //Reset invoice arraylists
        InvoiceController.performTransaction(invoice);
        resetInvoice();
        showAddItemPanel();
        logger.info("Payment complete");
    }
// </editor-fold>
    //
    //
// <editor-fold defaultstate="collapsed" desc="Netbeans generated Code">

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        interfaceContainerPanel = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        invoicePanel = new javax.swing.JPanel();
        itemAddPanel = new javax.swing.JPanel();
        itemTableSP = new javax.swing.JScrollPane();
        invoiceItemTable = new javax.swing.JTable();
        btnPayment = new javax.swing.JButton();
        btnInvoiceCancel = new javax.swing.JButton();
        billButtonPanel = new javax.swing.JPanel();
        btnAddItem = new javax.swing.JButton();
        btnDeleteItem = new javax.swing.JButton();
        btnClearItem = new javax.swing.JButton();
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
        lblBill = new javax.swing.JLabel();
        txtBillNumber = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
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
        coopCreditpaymentPanel = new javax.swing.JPanel();
        PoshanaPayment = new javax.swing.JPanel();
        VoucherPayment = new javax.swing.JPanel();
        btnAddPayment = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();

        setMaximizable(true);
        setResizable(true);
        setTitle("Invoice");
        setMinimumSize(new java.awt.Dimension(1050, 750));
        setPreferredSize(new java.awt.Dimension(1050, 750));

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        interfaceContainerPanel.setBorder(dropShadowBorder1);
        interfaceContainerPanel.setOpaque(false);

        cardPanel.setLayout(new java.awt.CardLayout());

        invoicePanel.setLayout(new java.awt.CardLayout());

        invoiceItemTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        invoiceItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Batch", "Name", "Description", "Price (Rs.)", "Qty", "Discount %", "Discount value (Rs.)", "Sub total (Rs.)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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

        btnInvoiceCancel.setText("Cancel");
        btnInvoiceCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvoiceCancelActionPerformed(evt);
            }
        });

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder2 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder2.setShadowColor(new java.awt.Color(204, 204, 204));
        dropShadowBorder2.setShadowSize(6);
        dropShadowBorder2.setShowLeftShadow(true);
        dropShadowBorder2.setShowTopShadow(true);
        billButtonPanel.setBorder(dropShadowBorder2);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billButtonPanelLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(billButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDeleteItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        billButtonPanelLayout.setVerticalGroup(
            billButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClearItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        itemCodeComboBox.setEditable(true);
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
                    .addComponent(lblProductName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                        .addComponent(lblUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 135, Short.MAX_VALUE))
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtProductName)
                            .addComponent(itemCodeComboBox, 0, 140, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
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
        billSummeryPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblItemNoDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemNoDisplay.setText("Item(s)");

        lblItemCount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblItemCount.setText("0");
        lblItemCount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblNetDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNetDisplay.setText("Net Total (Rs.)");

        lblNetTotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNetTotal.setForeground(new java.awt.Color(204, 0, 51));
        lblNetTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNetTotal.setText("0.00");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(lblItemDiscountDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblItemDiscounts, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(lblNetDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNetTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        billSummeryPanelLayout.setVerticalGroup(
            billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billSummeryPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNetTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNetDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemDiscounts, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemDiscountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemCount, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemNoDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblBill.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBill.setText("Bill Number : ");

        txtBillNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtBillNumber.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBillNumber.setText("<Bill No>");
        txtBillNumber.setEnabled(false);

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout itemAddPanelLayout = new javax.swing.GroupLayout(itemAddPanel);
        itemAddPanel.setLayout(itemAddPanelLayout);
        itemAddPanelLayout.setHorizontalGroup(
            itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemTableSP)
                    .addComponent(billSummeryPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(btnInvoiceCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(lblBill)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBillNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(billButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        itemAddPanelLayout.setVerticalGroup(
            itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBill)
                    .addComponent(txtBillNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(billButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(itemTableSP, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billSummeryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInvoiceCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(ivoicePaymentsSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        paymentOptionsPanelLayout.setVerticalGroup(
            paymentOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ivoicePaymentsSP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder4 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder4.setShowLeftShadow(true);
        dropShadowBorder4.setShowTopShadow(true);
        paymentInfoPanel.setBorder(dropShadowBorder4);

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
                .addGap(57, 57, 57)
                .addComponent(lblBillValueVal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(lblChangeDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblChangeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTotalDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        paymentInfoPanelLayout.setVerticalGroup(
            paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paymentInfoPanelLayout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblChangeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblChangeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBillValueVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBillValueDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder5 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder5.setShowLeftShadow(true);
        dropShadowBorder5.setShowTopShadow(true);
        paymentSelectorPanel.setBorder(dropShadowBorder5);

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

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder6 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder6.setShowLeftShadow(true);
        dropShadowBorder6.setShowTopShadow(true);
        paymentDetailsPanel.setBorder(dropShadowBorder6);
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
                .addComponent(lblCashPaymentAmountDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCashPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(279, Short.MAX_VALUE))
        );
        cashPaymentPanelLayout.setVerticalGroup(
            cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashPaymentPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashPaymentAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCashPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(289, Short.MAX_VALUE))
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
                .addContainerGap(220, Short.MAX_VALUE))
        );
        cardPaymentPanelLayout.setVerticalGroup(
            cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
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
                .addContainerGap(207, Short.MAX_VALUE))
        );

        paymentDetailsPanel.add(cardPaymentPanel, "bankCard");

        javax.swing.GroupLayout coopCreditpaymentPanelLayout = new javax.swing.GroupLayout(coopCreditpaymentPanel);
        coopCreditpaymentPanel.setLayout(coopCreditpaymentPanelLayout);
        coopCreditpaymentPanelLayout.setHorizontalGroup(
            coopCreditpaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 629, Short.MAX_VALUE)
        );
        coopCreditpaymentPanelLayout.setVerticalGroup(
            coopCreditpaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        paymentDetailsPanel.add(coopCreditpaymentPanel, "coopCreditCard");

        javax.swing.GroupLayout PoshanaPaymentLayout = new javax.swing.GroupLayout(PoshanaPayment);
        PoshanaPayment.setLayout(PoshanaPaymentLayout);
        PoshanaPaymentLayout.setHorizontalGroup(
            PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 629, Short.MAX_VALUE)
        );
        PoshanaPaymentLayout.setVerticalGroup(
            PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        paymentDetailsPanel.add(PoshanaPayment, "poshanaCard");

        javax.swing.GroupLayout VoucherPaymentLayout = new javax.swing.GroupLayout(VoucherPayment);
        VoucherPayment.setLayout(VoucherPaymentLayout);
        VoucherPaymentLayout.setHorizontalGroup(
            VoucherPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 629, Short.MAX_VALUE)
        );
        VoucherPaymentLayout.setVerticalGroup(
            VoucherPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
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
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(paymentPanelLayout.createSequentialGroup()
                                .addComponent(btnAddPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(paymentDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(paymentSelectorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(23, 23, 23)
                        .addComponent(paymentOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paymentPanelLayout.setVerticalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addComponent(paymentSelectorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(paymentOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(51, 51, 51)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(paymentInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        invoicePanel.add(paymentPanel, "paymentCard");

        cardPanel.add(invoicePanel, "invoiceCard");

        javax.swing.GroupLayout interfaceContainerPanelLayout = new javax.swing.GroupLayout(interfaceContainerPanel);
        interfaceContainerPanel.setLayout(interfaceContainerPanelLayout);
        interfaceContainerPanelLayout.setHorizontalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 968, Short.MAX_VALUE)
        );
        interfaceContainerPanelLayout.setVerticalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1034, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 753, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1.setLayer(interfaceContainerPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
        // TODO add your handling code here:
        showPaymentPanel();
    }//GEN-LAST:event_btnPaymentActionPerformed

    private void btnInvoiceCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvoiceCancelActionPerformed
        // TODO add your handling code here:'
        cancelBill();
    }//GEN-LAST:event_btnInvoiceCancelActionPerformed

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
        addPaymentOption();
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PoshanaPayment;
    private javax.swing.JPanel VoucherPayment;
    private javax.swing.JPanel billButtonPanel;
    private javax.swing.JPanel billSummeryPanel;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnAddPayment;
    private javax.swing.JButton btnClearItem;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnInvoiceCancel;
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
    private javax.swing.JPanel coopCreditpaymentPanel;
    private javax.swing.JPanel interfaceContainerPanel;
    private javax.swing.JTable invoiceItemTable;
    private javax.swing.JPanel invoicePanel;
    private javax.swing.JTable invoicePaymentsTable;
    private javax.swing.JPanel itemAddPanel;
    private javax.swing.JComboBox itemCodeComboBox;
    private javax.swing.JScrollPane itemTableSP;
    private javax.swing.JScrollPane ivoicePaymentsSP;
    private javax.swing.JLayeredPane jLayeredPane1;
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
    private javax.swing.JPanel productPanel;
    private javax.swing.JTextField txtAvailableQty;
    private javax.swing.JTextField txtBillNumber;
    private javax.swing.JTextField txtCardPaymentAmount;
    private javax.swing.JTextField txtCashPaymentAmount;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProductDesc;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtcardNo;
    // End of variables declaration//GEN-END:variables
 // </editor-fold>
}
