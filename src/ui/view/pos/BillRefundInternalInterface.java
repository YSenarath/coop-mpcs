package ui.view.pos;

import controller.payments.CoopCreditPaymentController;
import controller.inventory.ProductController;
import controller.payments.CardPaymentController;
import controller.payments.CashPaymentController;
import controller.payments.CustomerVoucherPaymentController;
import controller.payments.EmployeeVoucherPaymentController;
import controller.pos.InvoiceController;
import controller.pos.InvoiceItemController;
import controller.pos.RefundController;
import controller.pos.RefundItemController;
import database.connector.DatabaseInterface;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import model.pos.item.Invoice;
import model.pos.item.InvoiceItem;
import model.pos.item.Refund;
import model.pos.item.RefundItem;
import model.pos.payment.CardPayment;
import model.pos.payment.CashPayment;
import model.pos.payment.CoopCreditPayment;
import model.pos.payment.CustomerVoucherPayment;
import model.pos.payment.EmployeeVoucherPayment;
import org.apache.log4j.Logger;
import util.KeyValueContainer;
import util.Utilities;

class BillRefundInternalInterface extends javax.swing.JInternalFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(BillRefundInternalInterface.class);
    private final POSMDIInterface parent;
    private final JDesktopPane desktopPane;

    //Refund table
    private final DefaultTableModel refundTableModel;
    private int invoiceId;

    //KeyMaps
    private InputMap inputMap;
    private ActionMap actionMap;

    //Glass pane
    private final JPanel glassPanel;
    private final JLabel padding;

    //Constants
    private final int PRODUCT_ID_COLUMN = 0;
    private final int BATCH_ID_COLUMN = 1;
    private final int PRODUCT_DESC_COLUMN = 2;
    private final int PRODUCT_PRICE_COLUMN = 3;
    private final int PRODUCT_ORIGINAL_QTY_COLUMN = 4;
    private final int PRODUCT_REFUNDABLE_QTY_COLUMN = 5;
    private final int PRODUCT_AVAILABLE_DISC_COLUMN = 6;
    private final int PRODUCT_AVAILABLE_VALUE_COLUMN = 7;
    private final int PRODUCT_CHANGE_QTY_COLUMN = 8;
    private final int PRODUCT_CHANGE_VALUE_COLUMN = 9;
    private final int PRODUCT_CHANGE_DISC_COLUMN = 10;
    private final int PRODUCT_SUB_TOTAL_COLUMN = 11;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public BillRefundInternalInterface(POSMDIInterface parent, JDesktopPane desktopPane) {
        logger.debug("BillRefundInternalInterface constructor invoked");
        initComponents();
        this.parent = parent;
        this.desktopPane = desktopPane;

        Dimension desktopSize = desktopPane.getSize();
        Dimension jInternalFrameSize = this.getSize();
        this.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        this.invoiceId = -1;
//        JTextField field = new JTextField();
//        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
//            @Override
//            public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
//                    throws BadLocationException {
//                fb.insertString(off, str.replaceAll("[^0-9]", ""), attr);  // remove non-digits
//            }
//
//            @Override
//            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
//                    throws BadLocationException {
//                fb.replace(off, len, str.replaceAll("[^0-9]", ""), attr);  // remove non-digits
//            }
//        });
//        billRefundItemTable.getColumnModel().getColumn(PRODUCT_CHANGE_QTY_COLUMN).setCellEditor(new DefaultCellEditor(field));

        this.refundTableModel = (DefaultTableModel) billRefundItemTable.getModel();
        billRefundItemTable.setCellSelectionEnabled(true);
        ((DefaultTableCellRenderer) billRefundItemTable.getDefaultRenderer(Object.class)).setHorizontalAlignment(JLabel.RIGHT);
        this.refundTableModel.addTableModelListener(this::updateTableRow);

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

        initializeRefund();
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Key Bindings "> 
    private void performKeyBinding() {

        inputMap = interfaceContainerPanel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        actionMap = interfaceContainerPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "doEscapeAction");
        actionMap.put("doEscapeAction", new keyBindingAction("Escape"));

    }

    private class keyBindingAction extends AbstractAction {

        private final String cmd;

        public keyBindingAction(String cmd) {
            this.cmd = cmd;
        }

        @Override
        public void actionPerformed(ActionEvent tf) {
            if (cmd.equalsIgnoreCase("Escape")) {
                logger.debug("BillRefundInternal Interface - Escape Pressed ");
                cancelRefund();
            }
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Helper Methods">
    //Disable the glassPanel pane
    public void disableGlassPane() {
        logger.debug("disableGlassPane invoked");

        glassPanel.setVisible(false);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "doEscapeAction");
        actionMap.put("doEscapeAction", new keyBindingAction("Escape"));
    }

    //Enable the glassPanel pane
    public void enableGlassPane() {
        logger.debug("enableGlassPane invoked");

        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        actionMap.remove("doEscapeAction");

        glassPanel.setVisible(true);//Disable this UI
        padding.requestFocus();  // required to trap key events
    }

    //validate invoice number
    private boolean isValidInvoiceNumber(String invoiceNo) {
        logger.debug("isValidInvoiceNumber invoked");

        invoiceNo = invoiceNo.toUpperCase();
        if (invoiceNo.startsWith("I")) {
            invoiceNo = invoiceNo.substring(1);
            try {
                int no = Integer.parseInt(invoiceNo);
                return true;
            } catch (NumberFormatException ex) {

            }
        }
        return false;
    }

    //When user presses the enter key process the bill no and show the details
    private void txtLoadBillHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtLoadBillHandler invoked");

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            getInvoiceInformation();
        }
    }

    //clear UI
    private void cleanUI() {
        logger.debug("cleanUI invoked");

        invoiceId = -1;
        refundTableModel.setRowCount(0);
        lblBillDate.setText("");
        lblRefundCashAmountVal.setText("");
        lblRefundCardAmountVal.setText("");
        lblRefundCoopCreditVal.setText("");
        lblRefundChangeCashVal.setText("");
        lblRefundDiscountAmountVal.setText("");
        lblRefundNetTotalVal.setText("");
        lblRefundCancelDiscountAmountVal.setText("");
        lblRefundCancelNetTotalVal.setText("");
    }

    //Fill data to the table
    private void loadRefundInfo(int invoiceNumber) throws SQLException {
        logger.debug("loadRefundInfo invoked");

        ArrayList<InvoiceItem> invoiceItems = InvoiceItemController.getInvoiceItems(invoiceNumber);
        double netDiscount = 0;
        lblRefundDiscountAmountVal.setText(String.format("%.2f",
                invoiceItems.stream().map((InvoiceItem) -> InvoiceItem.getDiscount()).reduce(netDiscount, (accumulator, _item) -> accumulator + _item))
        );
        ArrayList<Refund> refunds = RefundController.getRefunds(invoiceNumber);
        if (refunds.isEmpty()) {
            logger.info("No previous refunds found");
            for (InvoiceItem invoiceItem : invoiceItems) {
                Object[] ob = {
                    new KeyValueContainer(String.valueOf(invoiceItem.getProductId()), Utilities.convertKeyToString(invoiceItem.getProductId(), DatabaseInterface.PRODUCT)),
                    invoiceItem.getBatchId(),
                    ProductController.getProduct(Utilities.convertKeyToString(invoiceItem.getProductId(), DatabaseInterface.PRODUCT)).getDescription(),
                    String.format("%.2f", invoiceItem.getUnitPrice()),
                    String.format("%.2f", invoiceItem.getQty()),
                    String.format("%.2f", invoiceItem.getQty()),
                    String.format("%.2f", invoiceItem.getDiscount()),
                    String.format("%.2f", invoiceItem.getUnitPrice() * invoiceItem.getQty() - invoiceItem.getDiscount()),
                    0.00,
                    "0.00",
                    "0.00",
                    "0.00"
                };
                refundTableModel.addRow(ob);
            }
        } else {
            logger.info("Previous refunds found");
            for (Refund refund : refunds) {
                refund.setRefundItems(RefundItemController.getRefundItems(refund.getRefundId()));
            }
            logger.info("No of previous refunds : " + refunds.size());
            for (InvoiceItem invoiceItem : invoiceItems) {
                double refundedQty = 0;
                double refundedDiscounts = 0;
                for (Refund refund : refunds) {
                    for (RefundItem refundItem : refund.getRefundItems()) {
                        if (invoiceItem.getProductId() == refundItem.getProductId() && invoiceItem.getBatchId() == refundItem.getBatchId()) {
                            refundedQty += refundItem.getQty();//returned item qty
                            refundedDiscounts += refundItem.getDiscount();//cancelld discounts
                        }
                    }
                }
                double refundableQty = invoiceItem.getQty() - refundedQty;
                double remainingDiscount = invoiceItem.getDiscount() - refundedDiscounts;
                Object[] ob = {
                    new KeyValueContainer(String.valueOf(invoiceItem.getProductId()), Utilities.convertKeyToString(invoiceItem.getProductId(), DatabaseInterface.PRODUCT)),
                    invoiceItem.getBatchId(),
                    ProductController.getProduct(Utilities.convertKeyToString(invoiceItem.getProductId(), DatabaseInterface.PRODUCT)).getDescription(),
                    String.format("%.2f", invoiceItem.getUnitPrice()),
                    String.format("%.2f", invoiceItem.getQty()),
                    String.format("%.2f", refundableQty >= 0 ? refundableQty : 0.00),
                    String.format("%.2f", remainingDiscount >= 0 ? remainingDiscount : 0.00),
                    String.format("%.2f", remainingDiscount >= 0 ? invoiceItem.getUnitPrice() * refundableQty - remainingDiscount : invoiceItem.getUnitPrice() * refundableQty),
                    0.00,
                    "0.00",
                    "0.00",
                    "0.00"
                };
                refundTableModel.addRow(ob);
            }
        }
        updateValues();
        billRefundItemTable.changeSelection(0, PRODUCT_CHANGE_QTY_COLUMN, false, false);
        billRefundItemTable.requestFocus();
    }

    //Take action on table edit
    private void updateTableRow(TableModelEvent e) {
        logger.debug("updateTableRow invoked");

        int row = e.getFirstRow();
        int column = e.getColumn();
        try {
            if (column == PRODUCT_CHANGE_QTY_COLUMN) {
                boolean isValidQty = false;

                double refundableQty = 0;
                double changeQty = 0;
                double unitPrice = 0;
                double availableDiscount = 0;

                unitPrice = Double.parseDouble(billRefundItemTable.getValueAt(row, PRODUCT_PRICE_COLUMN).toString());
                refundableQty = Double.parseDouble(billRefundItemTable.getValueAt(row, PRODUCT_REFUNDABLE_QTY_COLUMN).toString());
                availableDiscount = Double.parseDouble(billRefundItemTable.getValueAt(row, PRODUCT_AVAILABLE_DISC_COLUMN).toString());

                String productId = ((KeyValueContainer) billRefundItemTable.getValueAt(row, PRODUCT_ID_COLUMN)).getValue();

                String productUnit = ProductController.getProduct(productId).getUnit();

                try {
                    changeQty = Double.parseDouble(billRefundItemTable.getValueAt(row, PRODUCT_CHANGE_QTY_COLUMN).toString());
                    if (refundableQty < changeQty) {
                        Utilities.showMsgBox("Please enter correct amount", "Invalid amount", JOptionPane.ERROR_MESSAGE);
                        throw new Exception("ChangeQty> refundableQty");
                    } else if (productUnit.toLowerCase().equals("bulk") && changeQty != Math.rint(changeQty)) {
                        Utilities.showMsgBox("Bulk item amount cannot be a fraction", "Incorrect quantity", JOptionPane.WARNING_MESSAGE);
                        throw new Exception("Bulk product cannot be fraction");
                    } else {
                        isValidQty = true;
                    }
                } catch (Exception ex) {
                    logger.warn("Error : " + ex.getMessage());
                    billRefundItemTable.setValueAt(0, row, column);
                    billRefundItemTable.requestFocus();
                }

                double changeValue = 0;
                double changeDiscount = 0;
                double subTotal = 0;
                if (isValidQty) {
                    changeValue = changeQty * unitPrice;
                    if (refundableQty > 0) {
                        changeDiscount = (availableDiscount / refundableQty) * changeQty;
                    }

                    subTotal = changeValue - changeDiscount;
                    if (subTotal < 0) {
                        logger.warn("Negetive sub Total");
                        subTotal = 0;
                    }
                }
                billRefundItemTable.setValueAt(String.format("%.2f", changeValue), row, PRODUCT_CHANGE_VALUE_COLUMN);
                billRefundItemTable.setValueAt(String.format("%.2f", changeDiscount), row, PRODUCT_CHANGE_DISC_COLUMN);
                billRefundItemTable.setValueAt(String.format("%.2f", subTotal), row, PRODUCT_SUB_TOTAL_COLUMN);
                updateValues();
            }
        } catch (SQLException ex) {
            logger.error("Error : " + ex.getMessage());

        }
    }

    //Update refund values
    private void updateValues() {
        logger.debug("updateValues invoked");

        double cancelTotal = 0;
        double cancelDiscounts = 0;

        for (int row = 0; row < billRefundItemTable.getRowCount(); row++) {
            cancelDiscounts += Double.parseDouble(billRefundItemTable.getValueAt(row, PRODUCT_CHANGE_DISC_COLUMN).toString());
            cancelTotal += Double.parseDouble(billRefundItemTable.getValueAt(row, PRODUCT_SUB_TOTAL_COLUMN).toString());
        }

        lblRefundCancelNetTotalVal.setText(String.format("%.2f", cancelTotal));
        lblRefundCancelDiscountAmountVal.setText(String.format("%.2f", cancelDiscounts));
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Refund System"> 
    private void initializeRefund() {
        logger.debug("initializeRefund invoked");

        try {
            int lastRefundId = RefundController.getLastRefundId();
            if (lastRefundId > 0) {
                lblBillRefundNoVal.setText(Utilities.convertKeyToString(lastRefundId + 1, DatabaseInterface.REFUND));
            } else {
                lblBillRefundNoVal.setText(Utilities.convertKeyToString(1, DatabaseInterface.REFUND));
            }
        } catch (SQLException ex) {
            logger.error("SQL error : " + ex.getMessage(), ex);
        }
    }

    private void getInvoiceInformation() {
        logger.debug("getInvoiceInformation invoked");

        cleanUI();

        String billNumber = txtBillNo.getText();
        try {
            if (!isValidInvoiceNumber(billNumber)) {
                throw new Exception();
            }
            int invoiceNumber = Integer.parseInt(billNumber.substring(1));
            invoiceId = invoiceNumber;
            logger.info("Invoice number : " + invoiceNumber);

            Invoice invoice = InvoiceController.getInvoice(invoiceNumber);

            if (invoice == null) {
                throw new Exception("No such bill found");
            }

            lblBillDate.setText(invoice.getDate());
            lblRefundNetTotalVal.setText(String.format("%.2f", invoice.getNetTotal()));

            //Load items
            loadRefundInfo(invoiceNumber);

            //Cash Payments            
            CashPayment cashPayment = CashPaymentController.getCashpayment(invoiceNumber);
            if (cashPayment != null) {
                lblRefundCashAmountVal.setText(String.format("%.2f", cashPayment.getAmount()));
                lblRefundChangeCashVal.setText(String.format("%.2f", cashPayment.getChangeAmount()));
            } else {
                lblRefundCashAmountVal.setText("0.00");
                lblRefundChangeCashVal.setText("0.00");
            }

            //Card payments
            ArrayList<CardPayment> cardPayments = CardPaymentController.getCardPayments(invoiceNumber);
            double amexAmount = 0;
            double masterAmount = 0;
            double visaAmount = 0;
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
            lblRefundCardAmountVal.setText(String.format("%.2f", amexAmount + masterAmount + visaAmount));

            //Coop credit
            CoopCreditPayment coopCreditPayment = CoopCreditPaymentController.getCoopCreditPayment(invoiceNumber);
            if (coopCreditPayment != null) {
                lblRefundCoopCreditVal.setText(String.format("%.2f", coopCreditPayment.getAmount()));
            } else {
                lblRefundCoopCreditVal.setText("0.00");
            }

            //customer Voucher
            CustomerVoucherPayment customerVoucherPayment = CustomerVoucherPaymentController.getCustomerVoucherPayment(invoiceNumber);
            if (customerVoucherPayment != null) {
                lblRefundVoucherVal.setText(String.format("%.2f", customerVoucherPayment.getAmount()));
            } else {
                lblRefundVoucherVal.setText("0.00");
            }

            //Employee voucher
            EmployeeVoucherPayment employeeVoucherPayment = EmployeeVoucherPaymentController.getEmployeeVoucherPayment(invoiceNumber);
            if (employeeVoucherPayment != null) {
                lblRefundVoucherVal.setText(String.format("%.2f", employeeVoucherPayment.getAmount()));
            } else {
                lblRefundVoucherVal.setText("0.00");
            }

        } catch (Exception ex) {
            Utilities.showMsgBox("Invalid bill number : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Create a new bill from refund
    private void performRefund() {
        logger.debug("performRefund invoked");

        //Create refund object
        if (invoiceId == -1 || lblRefundCancelNetTotalVal.getText().trim().isEmpty() || Double.parseDouble(lblRefundCancelNetTotalVal.getText().trim()) == 0 || billRefundItemTable.getRowCount() == 0) {
            Utilities.showMsgBox("Please enter valid data ", "Error", JOptionPane.ERROR_MESSAGE);
            txtBillNo.requestFocus();
            return;
        }
        int refundId = Utilities.convertKeyToInteger(lblBillRefundNoVal.getText());
        int shiftId = parent.getCounterLogin().getShiftId();
        Refund refund = new Refund(refundId, invoiceId, shiftId);

        ArrayList<RefundItem> refundItems = new ArrayList();

        //Get all refunded items from table and add to the refund object
        for (int row = 0; row < billRefundItemTable.getRowCount(); row++) {
            String tableCellVal = billRefundItemTable.getValueAt(row, PRODUCT_CHANGE_QTY_COLUMN).toString().trim();
            if (!tableCellVal.isEmpty()) {
                double refundQty = Double.parseDouble(tableCellVal);
                if (refundQty > 0) {
                    int productId = Integer.parseInt(((KeyValueContainer) billRefundItemTable.getValueAt(row, PRODUCT_ID_COLUMN)).getKey());
                    int batchId = Integer.parseInt(billRefundItemTable.getValueAt(row, BATCH_ID_COLUMN).toString());
                    double unitPice = Double.parseDouble(billRefundItemTable.getValueAt(row, PRODUCT_PRICE_COLUMN).toString());
                    double refundDiscount = Double.parseDouble(billRefundItemTable.getValueAt(row, PRODUCT_CHANGE_DISC_COLUMN).toString());

                    RefundItem refundItem = new RefundItem(refundId, productId, batchId, unitPice, refundQty, refundDiscount);
                    logger.info("Refund item : " + refundItem);
                    refundItems.add(refundItem);
                }
            }
        }
        if (refundItems.isEmpty()) {
            Utilities.showMsgBox("No returned items found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        refund.setRefundItems(refundItems);
        refund.setAmount(Double.parseDouble(lblRefundCancelNetTotalVal.getText().trim()));
        refund.setRefundTime(Utilities.getCurrentTime(true));
        refund.setRefundDate(Utilities.getStringDate(Utilities.getCurrentDate()));

        new RefundVarification(parent, refund, true).setVisible(true);

        parent.setIsMainActivityRunning(false);
        parent.setIsBillRefundRunning(false);
        this.dispose();
    }

    //Cancel a refund
    private void cancelRefund() {
        logger.debug("refund_cancel invoked");

        parent.setIsMainActivityRunning(false);
        parent.setIsBillRefundRunning(false);

        this.dispose();
    }
    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Netbeans generated Code">  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        interfaceContainerPanel = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        refundPanel = new javax.swing.JPanel();
        billPaymentSummeryPanel1 = new javax.swing.JPanel();
        lblRefundCoopCredit = new javax.swing.JLabel();
        lblRefundCashAmountDisplay = new javax.swing.JLabel();
        lblRefundCardAmountDisplay = new javax.swing.JLabel();
        lblRefundChangeCashDisplay = new javax.swing.JLabel();
        lblRefundCashAmountVal = new javax.swing.JLabel();
        lblRefundCardAmountVal = new javax.swing.JLabel();
        lblRefundCoopCreditVal = new javax.swing.JLabel();
        lblRefundChangeCashVal = new javax.swing.JLabel();
        lblRefundDiscountAmountDisplay = new javax.swing.JLabel();
        lblRefundDiscountAmountVal = new javax.swing.JLabel();
        lblRefundNetTotalDisplay = new javax.swing.JLabel();
        lblRefundNetTotalVal = new javax.swing.JLabel();
        btnConfirm = new javax.swing.JButton();
        lblRefundCancelDiscountAmountDisplay = new javax.swing.JLabel();
        lblRefundCancelDiscountAmountVal = new javax.swing.JLabel();
        lblRefundCancelNetTotalDisplay = new javax.swing.JLabel();
        lblRefundCancelNetTotalVal = new javax.swing.JLabel();
        lblRefundVoucher = new javax.swing.JLabel();
        lblRefundVoucherVal = new javax.swing.JLabel();
        billRefundItemPanel = new javax.swing.JPanel();
        billItemSP1 = new javax.swing.JScrollPane();
        billRefundItemTable = new javax.swing.JTable(){
            //  Place cell in edit mode when it 'gains focus'

            public void changeSelection(
                int row, int column, boolean toggle, boolean extend)
            {
                super.changeSelection(row, column, toggle, extend);

                if (editCellAt(row, column))
                {
                    Component editor = getEditorComponent();
                    editor.requestFocusInWindow();
                    //          ((JTextComponent)editor).selectAll();
                }
            }
            @Override
            public Component prepareEditor(TableCellEditor editor, int row, int col) {
                Component c = super.prepareEditor(editor, row, col);
                c.setBackground(new Color(204,255,204));
                return c;
            }

        };
        javax.swing.JPanel billRefundInfoPanel = new javax.swing.JPanel();
        lblBill3 = new javax.swing.JLabel();
        txtBillNo = new javax.swing.JTextField();
        lblBillRefundNoVal = new javax.swing.JLabel();
        lblBillRefundNoDisplay = new javax.swing.JLabel();
        lblBillRefundDateDisplay = new javax.swing.JLabel();
        lblBillDate = new javax.swing.JLabel();

        setMaximizable(true);
        setResizable(true);
        setTitle("Bill Refund");
        setMinimumSize(new java.awt.Dimension(934, 598));

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        interfaceContainerPanel.setBorder(dropShadowBorder1);

        cardPanel.setLayout(new java.awt.CardLayout());

        billPaymentSummeryPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundCoopCredit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCoopCredit.setText("Coop Credit (Rs.)");

        lblRefundCashAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCashAmountDisplay.setText("Cash Amount (Rs.)");

        lblRefundCardAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCardAmountDisplay.setText("Card Amount (Rs.)");

        lblRefundChangeCashDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundChangeCashDisplay.setText("Change Cash (Rs.)");

        lblRefundCashAmountVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefundCashAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCashAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundCardAmountVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefundCardAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCardAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundCoopCreditVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefundCoopCreditVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCoopCreditVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundChangeCashVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefundChangeCashVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundChangeCashVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundDiscountAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundDiscountAmountDisplay.setText("Discount Amount (Rs.)");

        lblRefundDiscountAmountVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefundDiscountAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundDiscountAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundNetTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundNetTotalDisplay.setText("Net Total (Rs.)");
        lblRefundNetTotalDisplay.setToolTipText("");

        lblRefundNetTotalVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefundNetTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundNetTotalVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnConfirm.setText("Confirm");
        btnConfirm.setToolTipText("");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        lblRefundCancelDiscountAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCancelDiscountAmountDisplay.setText("Cancel Discount Amount (Rs.)");

        lblRefundCancelDiscountAmountVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefundCancelDiscountAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCancelDiscountAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundCancelNetTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCancelNetTotalDisplay.setText("Cancel Net Total (Rs.)");

        lblRefundCancelNetTotalVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefundCancelNetTotalVal.setForeground(new java.awt.Color(204, 0, 51));
        lblRefundCancelNetTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCancelNetTotalVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundVoucher.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundVoucher.setText("Voucher (Rs.)");

        lblRefundVoucherVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRefundVoucherVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundVoucherVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout billPaymentSummeryPanel1Layout = new javax.swing.GroupLayout(billPaymentSummeryPanel1);
        billPaymentSummeryPanel1.setLayout(billPaymentSummeryPanel1Layout);
        billPaymentSummeryPanel1Layout.setHorizontalGroup(
            billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, billPaymentSummeryPanel1Layout.createSequentialGroup()
                            .addComponent(lblRefundCardAmountDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblRefundCardAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, billPaymentSummeryPanel1Layout.createSequentialGroup()
                            .addComponent(lblRefundCoopCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblRefundCoopCreditVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, billPaymentSummeryPanel1Layout.createSequentialGroup()
                            .addComponent(lblRefundCashAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblRefundCashAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addComponent(lblRefundVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblRefundVoucherVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRefundChangeCashDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRefundNetTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRefundDiscountAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRefundDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundChangeCashVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblRefundCancelNetTotalDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblRefundCancelDiscountAmountDisplay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRefundCancelDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCancelNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnConfirm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        billPaymentSummeryPanel1Layout.setVerticalGroup(
            billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPaymentSummeryPanel1Layout.createSequentialGroup()
                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblRefundCashAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRefundCashAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblRefundCardAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRefundCardAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblRefundCoopCreditVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRefundCoopCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblRefundVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRefundVoucherVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblRefundChangeCashDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRefundChangeCashVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblRefundDiscountAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRefundDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblRefundNetTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRefundNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundCancelDiscountAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCancelDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundCancelNetTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCancelNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        billRefundItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Batch Id", "Description", "Price", "Original Qty", "Refundable Qty", "Disc", "Value", "Change Qty", "Change value", "Change Dis", "Sub total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        billRefundItemTable.setRowHeight(21);
        billRefundItemTable.getTableHeader().setReorderingAllowed(false);
        billItemSP1.setViewportView(billRefundItemTable);
        if (billRefundItemTable.getColumnModel().getColumnCount() > 0) {
            billRefundItemTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            billRefundItemTable.getColumnModel().getColumn(1).setMinWidth(0);
            billRefundItemTable.getColumnModel().getColumn(1).setMaxWidth(0);
            billRefundItemTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            billRefundItemTable.getColumnModel().getColumn(3).setPreferredWidth(50);
            billRefundItemTable.getColumnModel().getColumn(4).setPreferredWidth(50);
            billRefundItemTable.getColumnModel().getColumn(5).setPreferredWidth(50);
            billRefundItemTable.getColumnModel().getColumn(6).setPreferredWidth(50);
            billRefundItemTable.getColumnModel().getColumn(7).setPreferredWidth(50);
            billRefundItemTable.getColumnModel().getColumn(8).setPreferredWidth(50);
            billRefundItemTable.getColumnModel().getColumn(9).setPreferredWidth(50);
            billRefundItemTable.getColumnModel().getColumn(10).setPreferredWidth(50);
        }

        javax.swing.GroupLayout billRefundItemPanelLayout = new javax.swing.GroupLayout(billRefundItemPanel);
        billRefundItemPanel.setLayout(billRefundItemPanelLayout);
        billRefundItemPanelLayout.setHorizontalGroup(
            billRefundItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billItemSP1)
        );
        billRefundItemPanelLayout.setVerticalGroup(
            billRefundItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billItemSP1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
        );

        lblBill3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBill3.setText("Bill Number : ");

        txtBillNo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtBillNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBillNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBillNoKeyReleased(evt);
            }
        });

        lblBillRefundNoVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillRefundNoVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillRefundNoVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblBillRefundNoDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillRefundNoDisplay.setText("Bill refund No :");

        lblBillRefundDateDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillRefundDateDisplay.setText("Bill Date : ");

        lblBillDate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout billRefundInfoPanelLayout = new javax.swing.GroupLayout(billRefundInfoPanel);
        billRefundInfoPanel.setLayout(billRefundInfoPanelLayout);
        billRefundInfoPanelLayout.setHorizontalGroup(
            billRefundInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billRefundInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBill3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblBillRefundDateDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblBillDate, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblBillRefundNoDisplay)
                .addGap(18, 18, 18)
                .addComponent(lblBillRefundNoVal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        billRefundInfoPanelLayout.setVerticalGroup(
            billRefundInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billRefundInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billRefundInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblBillDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, billRefundInfoPanelLayout.createSequentialGroup()
                        .addGroup(billRefundInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBill3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBillRefundNoVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBillRefundNoDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblBillRefundDateDisplay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout refundPanelLayout = new javax.swing.GroupLayout(refundPanel);
        refundPanel.setLayout(refundPanelLayout);
        refundPanelLayout.setHorizontalGroup(
            refundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(refundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(refundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(billRefundInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(billRefundItemPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(billPaymentSummeryPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        refundPanelLayout.setVerticalGroup(
            refundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(refundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(billRefundInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billRefundItemPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billPaymentSummeryPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cardPanel.add(refundPanel, "refundCard");

        javax.swing.GroupLayout interfaceContainerPanelLayout = new javax.swing.GroupLayout(interfaceContainerPanel);
        interfaceContainerPanel.setLayout(interfaceContainerPanelLayout);
        interfaceContainerPanelLayout.setHorizontalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        interfaceContainerPanelLayout.setVerticalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 960, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 568, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        // TODO add your handling code here:
        performRefund();
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void txtBillNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBillNoKeyReleased
        // TODO add your handling code here:
        txtLoadBillHandler(evt);
    }//GEN-LAST:event_txtBillNoKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane billItemSP1;
    private javax.swing.JPanel billPaymentSummeryPanel1;
    private javax.swing.JPanel billRefundItemPanel;
    private javax.swing.JTable billRefundItemTable;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JPanel interfaceContainerPanel;
    private javax.swing.JLabel lblBill3;
    private javax.swing.JLabel lblBillDate;
    private javax.swing.JLabel lblBillRefundDateDisplay;
    private javax.swing.JLabel lblBillRefundNoDisplay;
    private javax.swing.JLabel lblBillRefundNoVal;
    private javax.swing.JLabel lblRefundCancelDiscountAmountDisplay;
    private javax.swing.JLabel lblRefundCancelDiscountAmountVal;
    private javax.swing.JLabel lblRefundCancelNetTotalDisplay;
    private javax.swing.JLabel lblRefundCancelNetTotalVal;
    private javax.swing.JLabel lblRefundCardAmountDisplay;
    private javax.swing.JLabel lblRefundCardAmountVal;
    private javax.swing.JLabel lblRefundCashAmountDisplay;
    private javax.swing.JLabel lblRefundCashAmountVal;
    private javax.swing.JLabel lblRefundChangeCashDisplay;
    private javax.swing.JLabel lblRefundChangeCashVal;
    private javax.swing.JLabel lblRefundCoopCredit;
    private javax.swing.JLabel lblRefundCoopCreditVal;
    private javax.swing.JLabel lblRefundDiscountAmountDisplay;
    private javax.swing.JLabel lblRefundDiscountAmountVal;
    private javax.swing.JLabel lblRefundNetTotalDisplay;
    private javax.swing.JLabel lblRefundNetTotalVal;
    private javax.swing.JLabel lblRefundVoucher;
    private javax.swing.JLabel lblRefundVoucherVal;
    private javax.swing.JPanel refundPanel;
    private javax.swing.JTextField txtBillNo;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
}
