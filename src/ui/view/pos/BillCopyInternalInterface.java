package ui.view.pos;

import controller.credit.CoopCreditPaymentController;
import controller.inventory.ProductController;
import controller.pos.CardPaymentController;
import controller.pos.CashPaymentController;
import controller.pos.CounterLoginController;
import controller.pos.CustomerVoucherPaymentController;
import controller.pos.EmployeeVoucherPaymentController;
import controller.pos.InvoiceController;
import controller.pos.InvoiceItemController;
import controller.pos.PoshanaPaymentController;
import database.connector.DatabaseInterface;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import model.pos.payment.CardPayment;
import model.pos.payment.CashPayment;
import model.pos.item.Invoice;
import model.pos.item.InvoiceItem;
import model.pos.payment.CoopCreditPayment;
import model.pos.payment.CustomerVoucherPayment;
import model.pos.payment.EmployeeVoucherPayment;
import model.pos.payment.PoshanaPayment;
import org.apache.log4j.Logger;
import util.Utilities;

 class BillCopyInternalInterface extends javax.swing.JInternalFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(BillCopyInternalInterface.class);
    private final POSMDIInterface parent;
    private final JDesktopPane desktopPane;

    private final DefaultTableModel printItemTableModel;
    private boolean billPrintReady;

    //KeyMaps
    private InputMap inputMap;
    private ActionMap actionMap;

    //Glass pane
    private final JPanel glassPanel;
    private final JLabel padding;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public BillCopyInternalInterface(POSMDIInterface parent, JDesktopPane desktopPane) {
        logger.debug("BillCopyInternalInterface constructor invoked");

        initComponents();
        this.parent = parent;
        this.desktopPane = desktopPane;

        Dimension desktopSize = desktopPane.getSize();
        Dimension jInternalFrameSize = this.getSize();
        this.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        this.printItemTableModel = (DefaultTableModel) printItemTable.getModel();
        this.billPrintReady = false;

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
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Key Bindings "> 
    private void performKeyBinding() {

        inputMap = billCopyPanel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        actionMap = billCopyPanel.getActionMap();

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
                logger.debug("BillCopyInternal Interface - Escape Pressed ");
                cancelPrint();
            }
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Methods">
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

    //When user presses the enter key process the bill no and show the details
    private void txtBillSearchHandler(java.awt.event.KeyEvent evt) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            getInvoiceInformation();
        }
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

    private void cleanUI() {
        logger.debug("cleanUI invoked");

        this.billPrintReady = false;
        lblBillDateVal.setText("");
        lblBillTimeVal.setText("");
        lblShiftVal.setText("");
        lblBillCashierVal.setText("");

        printItemTableModel.setRowCount(0);

        txtNetAmount.setText("");
        txtDiscounts.setText("");
        txtTotalPayments.setText("");

        txtCashPayment.setText("");
        txtChange.setText("");

        txtAmexCardPayment.setText("");
        txtMasterCardPayment.setText("");
        txtVisaCardPayment.setText("");

        txtCoopCreditPayment.setText("");
        txtPoshana.setText("");
        txtVoucher.setText("");
    }

    //Show bill details
    private void getInvoiceInformation() {
        logger.debug("getInvoiceInformation invoked");
        cleanUI();
        
        String billNumber = txtSearchBillNO.getText();
        try {
            if (!isValidInvoiceNumber(billNumber)) {
                throw new Exception();
            }
            int invoiceNumber = Integer.parseInt(billNumber.substring(1));
            logger.info("Invoice number : " + invoiceNumber);

            Invoice invoice = InvoiceController.getInvoice(invoiceNumber);

            if (invoice == null) {
                throw new Exception("No such bill found");
            }

            lblBillDateVal.setText(invoice.getDate());
            lblBillTimeVal.setText(Utilities.convert24hTo12h(invoice.getTime()));
            lblShiftVal.setText(Utilities.convertKeyToString(invoice.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            lblBillCashierVal.setText(CounterLoginController.getCounterLogin(invoice.getShiftId()).getUserName());
            txtNetAmount.setText(String.format("%.2f", invoice.getNetTotal()));

            ArrayList<InvoiceItem> invoiceItems = InvoiceItemController.getInvoiceItems(invoiceNumber);
            double netDiscount = 0;
            for (InvoiceItem InvoiceItem : invoiceItems) {
                Object[] ob = {
                    Utilities.convertKeyToString(InvoiceItem.getProductId(), DatabaseInterface.PRODUCT),
                    ProductController.getProduct(Utilities.convertKeyToString(InvoiceItem.getProductId(), DatabaseInterface.PRODUCT)).getDescription(),
                    String.format("%.2f", InvoiceItem.getUnitPrice()),
                    String.format("%.2f", InvoiceItem.getQty()),
                    String.format("%.2f", InvoiceItem.getDiscount()),
                    String.format("%.2f", InvoiceItem.getUnitPrice() * InvoiceItem.getQty() - InvoiceItem.getDiscount())
                };
                printItemTableModel.addRow(ob);
                netDiscount += InvoiceItem.getDiscount();
            }
            txtDiscounts.setText(String.format("%.2f", netDiscount));

            double totalPayments = 0;
            //Cash Payments            
            CashPayment cashPayment = CashPaymentController.getCashpayment(invoiceNumber);
            if (cashPayment != null) {
                txtCashPayment.setText(String.format("%.2f", cashPayment.getAmount()));
                txtChange.setText(String.format("%.2f", cashPayment.getChangeAmount()));
                totalPayments += cashPayment.getAmount();
            }else{
                txtCashPayment.setText("0.00");
                txtChange.setText("0.00");
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
            txtAmexCardPayment.setText(String.format("%.2f", amexAmount));
            txtMasterCardPayment.setText(String.format("%.2f", masterAmount));
            txtVisaCardPayment.setText(String.format("%.2f", visaAmount));

            //Coop credit
            CoopCreditPayment coopCreditPayment = CoopCreditPaymentController.getCoopCreditPayment(invoiceNumber);
            if (coopCreditPayment != null) {
                txtCoopCreditPayment.setText(String.format("%.2f", coopCreditPayment.getAmount()));
            } else {
                txtCoopCreditPayment.setText("0.00");
            }

            //Poshana payment
            PoshanaPayment poshanaPayment = PoshanaPaymentController.getPoshanaPayment(invoiceNumber);
            if (poshanaPayment != null) {
                txtPoshana.setText(String.format("%.2f", poshanaPayment.getAmount()));
            } else {
                txtPoshana.setText("0.00");
            }

            //customer Voucher
            CustomerVoucherPayment customerVoucherPayment = CustomerVoucherPaymentController.getCustomerVoucherPayment(invoiceNumber);
            if (customerVoucherPayment != null) {
                txtVoucher.setText(String.format("%.2f", customerVoucherPayment.getAmount()));
            } else {
                txtVoucher.setText("0.00");
            }

            //Employee voucher
            EmployeeVoucherPayment employeeVoucherPayment = EmployeeVoucherPaymentController.getEmployeeVoucherPayment(invoiceNumber);
            if (employeeVoucherPayment != null) {
                txtVoucher.setText(String.format("%.2f", employeeVoucherPayment.getAmount()));
            } else {
                txtVoucher.setText("0.00");
            }

            totalPayments = totalPayments + amexAmount + masterAmount + visaAmount;
            txtTotalPayments.setText(String.format("%.2f", totalPayments));

            this.billPrintReady = true;
        } catch (Exception ex) {
            Utilities.showMsgBox("Invalid bill number : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Print a copy of the bill
    private void printBill() {
        logger.debug("printBill invoked");

        if (billPrintReady) {
            util.Utilities.showMsgBox("Printing bill ", "Successfull", JOptionPane.INFORMATION_MESSAGE);
            cleanUI();
            txtSearchBillNO.setText("");
            txtSearchBillNO.requestFocus();
            billPrintReady = false;
        } else {
            Utilities.showMsgBox("Please select a bill first", "Nothing to print", JOptionPane.ERROR_MESSAGE);
            txtSearchBillNO.requestFocus();
        }

    }

    //Cancel bill copy and show the welcome screen
    private void cancelPrint() {
        logger.debug("cancelPrint invoked");

        parent.setIsMainActivityRunning(false);
        parent.setIsBillCopyRunning(false);
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
        billCopyPanel = new javax.swing.JPanel();
        billInfoPanel = new javax.swing.JPanel();
        lblBill1 = new javax.swing.JLabel();
        txtSearchBillNO = new javax.swing.JTextField();
        lblBillDateVal = new javax.swing.JLabel();
        lblBillDateDisplay = new javax.swing.JLabel();
        lblBillTimeDisplay = new javax.swing.JLabel();
        lblBillTimeVal = new javax.swing.JLabel();
        lblBillCashierDisplay = new javax.swing.JLabel();
        lblBillCashierVal = new javax.swing.JLabel();
        lblShiftDisplay = new javax.swing.JLabel();
        lblShiftVal = new javax.swing.JLabel();
        billItemPanel = new javax.swing.JPanel();
        billItemSP = new javax.swing.JScrollPane();
        printItemTable = new javax.swing.JTable();
        billPaymentSummeryPanel = new javax.swing.JPanel();
        btnPrintBill = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtCashPayment = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAmexCardPayment = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtChange = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtMasterCardPayment = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtVisaCardPayment = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCoopCreditPayment = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtPoshana = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtVoucher = new javax.swing.JTextField();
        txtNetAmount = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtDiscounts = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtTotalPayments = new javax.swing.JTextField();

        setTitle("Bill Copy");
        setMinimumSize(new java.awt.Dimension(926, 630));

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        interfaceContainerPanel.setBorder(dropShadowBorder1);

        lblBill1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBill1.setText("Bill Number : ");

        txtSearchBillNO.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSearchBillNO.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSearchBillNO.setToolTipText("");
        txtSearchBillNO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchBillNOKeyReleased(evt);
            }
        });

        lblBillDateVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillDateVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBillDateVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblBillDateDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillDateDisplay.setText("Date : ");

        lblBillTimeDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillTimeDisplay.setText("Time : ");

        lblBillTimeVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillTimeVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBillTimeVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblBillCashierDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillCashierDisplay.setText("Cashier : ");

        lblBillCashierVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillCashierVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillCashierVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblShiftDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblShiftDisplay.setText("Shift : ");

        lblShiftVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblShiftVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShiftVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout billInfoPanelLayout = new javax.swing.GroupLayout(billInfoPanel);
        billInfoPanel.setLayout(billInfoPanelLayout);
        billInfoPanelLayout.setHorizontalGroup(
            billInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBill1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearchBillNO, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(lblShiftDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblShiftVal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblBillCashierDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillCashierVal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblBillDateDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillDateVal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblBillTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillTimeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        billInfoPanelLayout.setVerticalGroup(
            billInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(billInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblShiftVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblShiftDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblBill1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSearchBillNO, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBillCashierVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBillCashierDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBillTimeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBillTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBillDateVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBillDateDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        printItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Description", "Price (Rs.)", "Qty", "Discount (Rs.)", "Sub total (Rs.)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        printItemTable.setRowHeight(21);
        printItemTable.getTableHeader().setReorderingAllowed(false);
        billItemSP.setViewportView(printItemTable);

        billPaymentSummeryPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnPrintBill.setText("Print");
        btnPrintBill.setToolTipText("");
        btnPrintBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintBillActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Cash Payment (Rs.)");

        txtCashPayment.setEditable(false);
        txtCashPayment.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCashPayment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("AMEX Card (Rs.)");

        txtAmexCardPayment.setEditable(false);
        txtAmexCardPayment.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtAmexCardPayment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Change (Rs.)");

        txtChange.setEditable(false);
        txtChange.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtChange.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("MASTER Card (Rs.)");

        txtMasterCardPayment.setEditable(false);
        txtMasterCardPayment.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtMasterCardPayment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("VISA Card (Rs.)");

        txtVisaCardPayment.setEditable(false);
        txtVisaCardPayment.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtVisaCardPayment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("COOP Credit (Rs.)");

        txtCoopCreditPayment.setEditable(false);
        txtCoopCreditPayment.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCoopCreditPayment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Poshana (Rs.)");

        txtPoshana.setEditable(false);
        txtPoshana.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtPoshana.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Voucher (Rs.)");

        txtVoucher.setEditable(false);
        txtVoucher.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtVoucher.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtNetAmount.setEditable(false);
        txtNetAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtNetAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Net Amount (Rs.)");

        txtDiscounts.setEditable(false);
        txtDiscounts.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtDiscounts.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Discount (Rs.)");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Total Payments (Rs.)");

        txtTotalPayments.setEditable(false);
        txtTotalPayments.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTotalPayments.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout billPaymentSummeryPanelLayout = new javax.swing.GroupLayout(billPaymentSummeryPanel);
        billPaymentSummeryPanel.setLayout(billPaymentSummeryPanelLayout);
        billPaymentSummeryPanelLayout.setHorizontalGroup(
            billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addGap(154, 154, 154)
                                .addComponent(txtVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPrintBill, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel8))
                                .addGap(30, 30, 30)
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCoopCreditPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                        .addComponent(txtAmexCardPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18)
                                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtChange, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                                .addComponent(txtMasterCardPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtVisaCardPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtCashPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)))
                        .addGap(0, 122, Short.MAX_VALUE))
                    .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(58, 58, 58)
                        .addComponent(txtPoshana, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTotalPayments)
                            .addComponent(txtNetAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                            .addComponent(txtDiscounts))))
                .addContainerGap())
        );
        billPaymentSummeryPanelLayout.setVerticalGroup(
            billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtCashPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtVisaCardPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txtMasterCardPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtAmexCardPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtCoopCreditPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtPoshana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtDiscounts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNetAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(57, 57, 57))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTotalPayments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))
                                .addGap(18, 18, 18)))
                        .addComponent(btnPrintBill, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout billItemPanelLayout = new javax.swing.GroupLayout(billItemPanel);
        billItemPanel.setLayout(billItemPanelLayout);
        billItemPanelLayout.setHorizontalGroup(
            billItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billItemSP)
            .addComponent(billPaymentSummeryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        billItemPanelLayout.setVerticalGroup(
            billItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billItemPanelLayout.createSequentialGroup()
                .addComponent(billItemSP, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billPaymentSummeryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout billCopyPanelLayout = new javax.swing.GroupLayout(billCopyPanel);
        billCopyPanel.setLayout(billCopyPanelLayout);
        billCopyPanelLayout.setHorizontalGroup(
            billCopyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billCopyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billCopyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(billInfoPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(billItemPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        billCopyPanelLayout.setVerticalGroup(
            billCopyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billCopyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(billInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billItemPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout interfaceContainerPanelLayout = new javax.swing.GroupLayout(interfaceContainerPanel);
        interfaceContainerPanel.setLayout(interfaceContainerPanelLayout);
        interfaceContainerPanelLayout.setHorizontalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billCopyPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        interfaceContainerPanelLayout.setVerticalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billCopyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 945, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintBillActionPerformed
        // TODO add your handling code here:
        printBill();
    }//GEN-LAST:event_btnPrintBillActionPerformed

    private void txtSearchBillNOKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchBillNOKeyReleased
        // TODO add your handling code here:
        txtBillSearchHandler(evt);
    }//GEN-LAST:event_txtSearchBillNOKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel billCopyPanel;
    private javax.swing.JPanel billInfoPanel;
    private javax.swing.JPanel billItemPanel;
    private javax.swing.JScrollPane billItemSP;
    private javax.swing.JPanel billPaymentSummeryPanel;
    private javax.swing.JButton btnPrintBill;
    private javax.swing.JPanel interfaceContainerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblBill1;
    private javax.swing.JLabel lblBillCashierDisplay;
    private javax.swing.JLabel lblBillCashierVal;
    private javax.swing.JLabel lblBillDateDisplay;
    private javax.swing.JLabel lblBillDateVal;
    private javax.swing.JLabel lblBillTimeDisplay;
    private javax.swing.JLabel lblBillTimeVal;
    private javax.swing.JLabel lblShiftDisplay;
    private javax.swing.JLabel lblShiftVal;
    private javax.swing.JTable printItemTable;
    private javax.swing.JTextField txtAmexCardPayment;
    private javax.swing.JTextField txtCashPayment;
    private javax.swing.JTextField txtChange;
    private javax.swing.JTextField txtCoopCreditPayment;
    private javax.swing.JTextField txtDiscounts;
    private javax.swing.JTextField txtMasterCardPayment;
    private javax.swing.JTextField txtNetAmount;
    private javax.swing.JTextField txtPoshana;
    private javax.swing.JTextField txtSearchBillNO;
    private javax.swing.JTextField txtTotalPayments;
    private javax.swing.JTextField txtVisaCardPayment;
    private javax.swing.JTextField txtVoucher;
    // End of variables declaration//GEN-END:variables
 // </editor-fold>
}
