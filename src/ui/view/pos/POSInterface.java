/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.pos;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Shehan
 */
public class POSInterface extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(POSInterface.class);

    private boolean isCashierLogedIn;

    public POSInterface() {
        initComponents();
        initializeGUI();
        initializeStates();

    }

    private void initializeGUI() {
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/images/pos/pos_icon.png")).getImage());
        ActionListener timerListener = (ActionEvent e) -> {
            Date currentDate = new Date();

            String strDate = new SimpleDateFormat("EEE, d MMM yyyy").format(currentDate);
            String strTime = new SimpleDateFormat("h:mm a").format(currentDate);

            lblDate.setText(strDate);
            lblBillDateVal.setText(strDate);

            lblTime.setText(strTime);
            lblBillTimeVal.setText(strTime);
        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();

        //billTaskPane.setCollapsed(true);
        billTaskPane.setCollapsed(false);

        otherTaskPane.setCollapsed(true);
        settingsTaskPane.setCollapsed(true);

    }

    private void initializeStates() {
        btnCachierLog.setText("Cashier logged off");
        btnManagerLog.setText("Manager logged off");
        isCashierLogedIn = false;
    }

    //user log in ui changes
    private void setLogControls() {
        logger.debug("setLogControles invoked");
        if (isCashierLogedIn) {
            btnCachierLog.setText("Cashier logged in");

            lblCashier.setText("Test");
            lblCounter.setText("1");
        } else {
            btnCachierLog.setText("Cashier logged off");

            lblCashier.setText("<Cashier name>");
            lblCounter.setText("<counter>");

            CardLayout card = (CardLayout) cardPanel.getLayout();
            card.show(cardPanel, "welcomeCard");
            billTaskPane.setCollapsed(true);
            otherTaskPane.setCollapsed(true);
            settingsTaskPane.setCollapsed(true);
        }
    }

    //Add item to the bill item table
    private void bill_addItemToBillItemTable() {
        logger.warn("bill_addItemToBillItemTable not implemented");
    }

    //Remove a added item from the bill item table
    private void bill_deleteItemFromBillItemTable() {
        logger.debug("bill_deleteItemFromBillItemTable invoked");

        //NOT IMPLEMNTED - When a row is deleted the bottem most row gets auto matically selected. If no row selected by user delete from the bottom
        DefaultTableModel billItemTableModel = (DefaultTableModel) billItemTable.getModel();
        if (billItemTable.getSelectedRow() != -1) {
            billItemTableModel.removeRow(billItemTable.getSelectedRow());

        }
    }

    //Clear the current product fields in bill add item to bill
    private void bill_clearSelectedItemSearch() {
        logger.debug("bill_clearSelectedItemSearch invoked");

        itemCodeComboBox.setSelectedIndex(-1);
        txtProductDesc.setText("");
        txtQty.setText("");
        lblUnit.setText("<Unit>");
    }

    //Load the item information to the bill 
    private void bill_loadLtemInfoToBill() {
        logger.warn("bill_loadLtemInfoToBill not implemented");
    }

    //Show the payment panel in the bill
    private void bill_showPaymentScreen() {
        CardLayout card = (CardLayout) invoicePanel.getLayout();
        card.next(invoicePanel);
    }

    //Show the add item panel in bill
    private void bill_showAddItemPanel() {
        CardLayout card = (CardLayout) invoicePanel.getLayout();
        card.previous(invoicePanel);
    }

    //Cancel current bill and show welocme screen
    private void bill_cancelBill() {
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "welcomeCard");
        //billTaskPane.setCollapsed(true);
        //otherTaskPane.setCollapsed(true);
        //settingsTaskPane.setCollapsed(true);
    }

    //Show the main bill screen
    private void bill_showInvoicePanel() {

        if (isCashierLogedIn) {
            // billTaskPane.setCollapsed(false);
        } else {
            logger.error("Cashier not logged in");
            //  billTaskPane.setCollapsed(true);
        }
    }

    //Toggle payment options in payment scrren
    private void bill_togglePaymentOptions(java.awt.event.ActionEvent evt) {
        CardLayout card = (CardLayout) paymentDetailsPanel.getLayout();
        JComboBox paymentComboBox = (JComboBox) evt.getSource();
        String selectedOption = (String) paymentComboBox.getSelectedItem();
        if (null != selectedOption) {
            switch (selectedOption) {
                case "Cash":
                    card.show(paymentDetailsPanel, "cashCard");
                    break;
                case "Card":
                    card.show(paymentDetailsPanel, "bankCard");
                    break;
                case "Credit":
                    card.show(paymentDetailsPanel, "creditCard");
                    break;
                case "Stamp":
                    card.show(paymentDetailsPanel, "stampCard");
                    break;
                case "Special":
                    card.show(paymentDetailsPanel, "specialCard");
                    break;
            }
        }
    }

    //Add a payment option
    private void bill_addPaymentOption() {
        logger.warn("bill_addPaymentOption not implemented");
    }

    //Remove a payment option
    private void bill_removePaymentOption() {
        logger.warn("bill_removePaymentOption not implemented");
    }

    //Search a item
    private void bill_searchItem() {
        //NOT IMPLEMENTED - send a search string to the search UI
        new SearchItemDialog(this, true).setVisible(true);
    }

    //New sale
    private void bill_newSale() {
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "invoiceCard");
    }

    //Hold sale
    private void bill_holdSale() {
        logger.warn("bill_holdSale not implemented");
    }

    //Restore sale
    private void bill_restoreSale() {
        logger.warn("bill_restoreSale not implemented");
    }

    //Confirm bill
    private void bill_confirm() {
        logger.warn("bill_confirm not implemented");
    }
     
    //Show bill copy screen
    private void billCopy_ShowPanel() {
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "billCopyCard");
        //billTaskPane.setCollapsed(true);
    }

    //Print a copy of the bill
    private void billCopy_printBill() {
        logger.warn("billCopy_printBill not implemented");
    }

    //Cancel bill copy and show the welcome screen
    private void billCopy_cancel() {
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "welcomeCard");
        billTaskPane.setCollapsed(true);
        otherTaskPane.setCollapsed(true);
        settingsTaskPane.setCollapsed(true);
    }

    //Show refund screen
    private void refund_showRefundPanel() {
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "refundCard");
        //billTaskPane.setCollapsed(true);
    }

    //Create a new bill from refund
    private void refund_newBill() {
        logger.warn("refund_newBill not implemented");
    }

    //Cancel a refund
    private void refund_cancel() {
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "welcomeCard");
        billTaskPane.setCollapsed(true);
        otherTaskPane.setCollapsed(true);
        settingsTaskPane.setCollapsed(true);
    }

    //Show the cash withdrawal UI
    private void cashWithdrawal_showUI() {
        new CashWithdrawalDialog(this, true).setVisible(true);
    }

    //Show the check stock UI
    private void chechStock() {
        new CheckStockDialog(this, true).setVisible(true);
    }

    //Show the settings task panel
    private void settings_showTaskPane(java.awt.event.MouseEvent evt) {
        if (!isCashierLogedIn) {
            logger.error("Cashier not logged in");
            settingsTaskPane.setCollapsed(true);
            evt.consume();
        }
    }

    //Show the other task panel
    private void other_showTaskPane(java.awt.event.MouseEvent evt) {
        if (!isCashierLogedIn) {
            logger.error("Cashier not logged in");
            otherTaskPane.setCollapsed(true);
            evt.consume();
        }
    }

    //Show configure UI
    private void configure_show() {
        logger.warn("configure_show not implemented");
    }

    //Mange cashier login
    private void cashier_logIn() {
        // TODO add your handling code here:

        /*
         Important :
         1.NOT IMPLEMENTED - Show intial amount of drawayer  UI at log on
         2.NOT IMPLEMENTED - Show log of UI to print the summery for cashier - 
         */
        logger.warn("NOT IMPLEMENTED - Show intial amount of drawayer  UI at log on");
        logger.warn("NOT IMPLEMENTED - Show log of UI to print the summery for cashier");

        if (!isCashierLogedIn) {
            logger.info("Cashier logged on");
            isCashierLogedIn = true;
        } else {
            logger.info("Cashier logged off");
            isCashierLogedIn = false;
        }
        setLogControls();

    }

    //Manager features
    private void manager_logIn() {
        logger.warn("manager_logIn not implemented");
    }

    private static void setupUI() {

        //try {
        //    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        //}
        Properties props = new Properties();

        //RGB colours
        String buttonClolor = "200 200 200";
        String controlClolor = "200 200 200";

        String menuColor = "222 222 222";
        String menuBackgroundColor = "224 224 224";

        String selectionBackgroundColor = "240 240 240";
        String selectionForegroundColor = "67 148 103";

        String rollOverClolor = "114 114 114";

        String frameColor = "171 171 171";
        String windowTitleColor = "10 10 10";

        //Customize Theme
        props.put("logoString", "");

        props.put("linuxStyleScrollBar", "on");
        props.put("centerWindowTitle", "on");
        props.put("textAntiAliasing", "on");
        props.put("textAntiAliasingMode", "default");
        props.put("toolbarDecorated", "off");
        props.put("windowDecoration", "on");
        props.put("dynamicLayout", "on");
        props.put("darkTexture", "off");

        props.put("buttonColor", buttonClolor);//button colours
        props.put("buttonColorLight", buttonClolor);
        props.put("buttonColorDark", buttonClolor);

        props.put("controlColor", controlClolor);//Control colours
        props.put("controlColorLight", controlClolor);
        props.put("controlColorDark", controlClolor);

        props.put("menuColorLight", menuColor);//menu colours
        props.put("menuColorDark", menuColor);
        props.put("menuBackgroundColor", menuBackgroundColor);

        props.put("selectionBackgroundColor", selectionBackgroundColor);//hilighted text
        props.put("selectionForegroundColor", selectionForegroundColor);

        props.put("rolloverColor", rollOverClolor); //on hovering
        props.put("rolloverColorLight", rollOverClolor);
        props.put("rolloverColorDark", rollOverClolor);

        props.put("frameColor", frameColor);
        props.put("windowTitleColorLight", windowTitleColor);//Windows boarder colours
        props.put("windowTitleColorDark", windowTitleColor);
        props.put("disabledForegroundColor", windowTitleColor);

        try {
            com.jtattoo.plaf.graphite.GraphiteLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ey) {
                System.exit(3);
            }
            System.exit(3);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        uiPanel = new javax.swing.JPanel();
        mainLayerPanel = new javax.swing.JLayeredPane();
        counterInfoPanel = new javax.swing.JPanel();
        lblCashierDisplay = new javax.swing.JLabel();
        lblCashier = new javax.swing.JLabel();
        lblCounterDisplay = new javax.swing.JLabel();
        lblCounter = new javax.swing.JLabel();
        lblDateDisplay = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblTimeDisplay = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        sidePanel = new javax.swing.JPanel();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        billTaskPane = new org.jdesktop.swingx.JXTaskPane();
        btnNewSale = new javax.swing.JButton();
        btnCheckStock = new javax.swing.JButton();
        btnHoldSale = new javax.swing.JButton();
        btnRestoreSale = new javax.swing.JButton();
        otherTaskPane = new org.jdesktop.swingx.JXTaskPane();
        btnBillRefund = new javax.swing.JButton();
        btnBillCopy = new javax.swing.JButton();
        btnCashWithdrawal = new javax.swing.JButton();
        settingsTaskPane = new org.jdesktop.swingx.JXTaskPane();
        btnConfigure = new javax.swing.JButton();
        interfaceContainerPanel = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        welcomePanel = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        invoicePanel = new javax.swing.JPanel();
        itemAddPanel = new javax.swing.JPanel();
        itemTableSP = new javax.swing.JScrollPane();
        billItemTable = new javax.swing.JTable();
        btnPayment = new javax.swing.JButton();
        btnInvoiceCancel = new javax.swing.JButton();
        billButtonPanel = new javax.swing.JPanel();
        btnAddItem = new javax.swing.JButton();
        btnDeleteItem = new javax.swing.JButton();
        btnClearItem = new javax.swing.JButton();
        productPanel = new javax.swing.JPanel();
        lblCode = new javax.swing.JLabel();
        lblProduct = new javax.swing.JLabel();
        lblQty = new javax.swing.JLabel();
        btnLoad = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        itemCodeComboBox = new javax.swing.JComboBox();
        lblUnit = new javax.swing.JLabel();
        txtQty = new javax.swing.JFormattedTextField();
        txtProductDesc = new javax.swing.JTextField();
        billSummeryPanel = new javax.swing.JPanel();
        lblItemNoDisplay = new javax.swing.JLabel();
        lblItems = new javax.swing.JLabel();
        lblNetDisplay = new javax.swing.JLabel();
        lblNetTotal = new javax.swing.JLabel();
        lblItemDiscountDisplay = new javax.swing.JLabel();
        lblItems1 = new javax.swing.JLabel();
        lblBill = new javax.swing.JLabel();
        txtBillNumber = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        paymentPanel = new javax.swing.JPanel();
        btnPrevious = new javax.swing.JButton();
        paymentOptionsPanel = new javax.swing.JPanel();
        paymentOptionsSP = new javax.swing.JScrollPane();
        paymentsTable = new javax.swing.JTable();
        paymentInfoPanel = new javax.swing.JPanel();
        lblBillValueDisplay = new javax.swing.JLabel();
        lblBillValueVal = new javax.swing.JLabel();
        lblTotalDisplay = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        lblBillValueDisplay1 = new javax.swing.JLabel();
        lblBillValueDisplay2 = new javax.swing.JLabel();
        lblChangeDisplay = new javax.swing.JLabel();
        lblChangeVal = new javax.swing.JLabel();
        txtDiscountPercent = new javax.swing.JFormattedTextField();
        txtDiscountVal = new javax.swing.JFormattedTextField();
        paymentSelectorPanel = new javax.swing.JPanel();
        lblPaymentOption = new javax.swing.JLabel();
        paymentOptionComboBox = new javax.swing.JComboBox();
        paymentDetailsPanel = new javax.swing.JPanel();
        cashPaymentPanel = new javax.swing.JPanel();
        lblCashPaymentAmountDisplay = new javax.swing.JLabel();
        txtCashPaymentAmount = new javax.swing.JFormattedTextField();
        cardPaymentPanel = new javax.swing.JPanel();
        lblCashPaymentAmountDisplay1 = new javax.swing.JLabel();
        lblCardType = new javax.swing.JLabel();
        cardTypeComboBox = new javax.swing.JComboBox();
        lblCardNo = new javax.swing.JLabel();
        cardpaymentCardNo = new javax.swing.JFormattedTextField();
        txtCardPaymentAmount = new javax.swing.JFormattedTextField();
        creditpaymentPanel = new javax.swing.JPanel();
        stampPayment = new javax.swing.JPanel();
        specialPayment = new javax.swing.JPanel();
        btnAddPayment = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();
        refundPanel = new javax.swing.JPanel();
        billPaymentSummeryPanel1 = new javax.swing.JPanel();
        lblRefundOtherAmountDisplay = new javax.swing.JLabel();
        lblRefundCashAmountDisplay = new javax.swing.JLabel();
        lblRefundCardAmountDisplay = new javax.swing.JLabel();
        lblRefundChangeCashDisplay = new javax.swing.JLabel();
        lblRefundCashAmountVal = new javax.swing.JLabel();
        lblRefundCardAmountVal = new javax.swing.JLabel();
        lblRefundOtherAmountVal = new javax.swing.JLabel();
        lblRefundChangeCashVal = new javax.swing.JLabel();
        lblRefundSubTotalDisplay = new javax.swing.JLabel();
        lblRefundSubTotalVal = new javax.swing.JLabel();
        lblRefundDiscountAmountDisplay = new javax.swing.JLabel();
        lblRefundDiscountAmountVal = new javax.swing.JLabel();
        lblRefundNetTotalDisplay = new javax.swing.JLabel();
        lblRefundNetTotalVal = new javax.swing.JLabel();
        btnNewBillFromRefund = new javax.swing.JButton();
        lblRefundCancelSubTotalDisplay = new javax.swing.JLabel();
        lblRefundCancelSubTotalVal = new javax.swing.JLabel();
        lblRefundCancelDiscountAmountDisplay = new javax.swing.JLabel();
        javax.swing.JLabel lblRefundCancelDiscountAmountVal = new javax.swing.JLabel();
        lblRefundCancelNetTotalDisplay = new javax.swing.JLabel();
        lblRefundCancelNetTotalVal = new javax.swing.JLabel();
        lblBillRefundDateDisplay = new javax.swing.JLabel();
        lblBillDate = new javax.swing.JLabel();
        btnRefundCancel = new javax.swing.JButton();
        billRefundItemPanel = new javax.swing.JPanel();
        billItemSP1 = new javax.swing.JScrollPane();
        billRefunditemTable = new javax.swing.JTable();
        javax.swing.JPanel billRefundInfoPanel = new javax.swing.JPanel();
        lblBill3 = new javax.swing.JLabel();
        txtSearchBillNO1 = new javax.swing.JTextField();
        lblBillRefundNoVal = new javax.swing.JLabel();
        lblBillRefundNoDisplay = new javax.swing.JLabel();
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
        billItemPanel = new javax.swing.JPanel();
        billItemSP = new javax.swing.JScrollPane();
        printItemTable = new javax.swing.JTable();
        billPaymentSummeryPanel = new javax.swing.JPanel();
        lblOtherAmountDisplay = new javax.swing.JLabel();
        lblCashAmountDisplay = new javax.swing.JLabel();
        lblCardAmountDisplay = new javax.swing.JLabel();
        lblChangeCashDisplay = new javax.swing.JLabel();
        lblCashAmountVal = new javax.swing.JLabel();
        lblCardAmountVal = new javax.swing.JLabel();
        lblOtherAmountVal = new javax.swing.JLabel();
        lblChangeCashVal = new javax.swing.JLabel();
        lblSubTotalDisplay = new javax.swing.JLabel();
        lblSubTotalVal = new javax.swing.JLabel();
        lblDiscountAmountDisplay = new javax.swing.JLabel();
        lblDiscountAmountVal = new javax.swing.JLabel();
        lblNetTotalDisplay = new javax.swing.JLabel();
        lblNetTotalVal = new javax.swing.JLabel();
        btnCancelPrint = new javax.swing.JButton();
        btnPrintBill = new javax.swing.JButton();
        lblBillPrintDateDisplay = new javax.swing.JLabel();
        lblBillPrintDate = new javax.swing.JLabel();
        btnCachierLog = new javax.swing.JButton();
        btnManagerLog = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        statusBar = new org.jdesktop.swingx.JXStatusBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MEGA COOP CITY POS");
        setMinimumSize(new java.awt.Dimension(400, 300));
        setName("invoiceFrame"); // NOI18N

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShadowSize(10);
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        counterInfoPanel.setBorder(dropShadowBorder1);
        counterInfoPanel.setPreferredSize(new java.awt.Dimension(224, 250));

        lblCashierDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashierDisplay.setText("Cashier");

        lblCashier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCashier.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCashier.setText("<Cashier name>");
        lblCashier.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblCounterDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCounterDisplay.setText("Counter");
        lblCounterDisplay.setToolTipText("");

        lblCounter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCounter.setText("<counter>");
        lblCounter.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblDateDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDateDisplay.setText("Date");

        lblDate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate.setText("<date>");
        lblDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblTimeDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTimeDisplay.setText("Time");

        lblTime.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTime.setText("<time>");
        lblTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout counterInfoPanelLayout = new javax.swing.GroupLayout(counterInfoPanel);
        counterInfoPanel.setLayout(counterInfoPanelLayout);
        counterInfoPanelLayout.setHorizontalGroup(
            counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(counterInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(counterInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblDateDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(counterInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblCashierDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(lblCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(counterInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(counterInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblCounterDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        counterInfoPanelLayout.setVerticalGroup(
            counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(counterInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCashierDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCounterDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 21, Short.MAX_VALUE)
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDateDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder2 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder2.setShowLeftShadow(true);
        dropShadowBorder2.setShowTopShadow(true);
        sidePanel.setBorder(dropShadowBorder2);

        jXTaskPaneContainer1.setBackground(new java.awt.Color(204, 204, 204));
        jXTaskPaneContainer1.setBorder(null);
        jXTaskPaneContainer1.setEnabled(false);
        org.jdesktop.swingx.VerticalLayout verticalLayout1 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout1.setGap(15);
        jXTaskPaneContainer1.setLayout(verticalLayout1);

        billTaskPane.setBackground(new java.awt.Color(102, 102, 102));
        billTaskPane.setForeground(new java.awt.Color(153, 153, 153));
        billTaskPane.setSpecial(true);
        billTaskPane.setTitle("Bill");
        billTaskPane.setEnabled(false);
        billTaskPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                billTaskPaneMouseClicked(evt);
            }
        });

        btnNewSale.setText("New Sale [ F7 ]");
        btnNewSale.setPreferredSize(new java.awt.Dimension(73, 30));
        btnNewSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewSaleActionPerformed(evt);
            }
        });
        billTaskPane.getContentPane().add(btnNewSale);

        btnCheckStock.setText("Check Stock [ F2 ]");
        btnCheckStock.setPreferredSize(new java.awt.Dimension(73, 30));
        btnCheckStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckStockActionPerformed(evt);
            }
        });
        billTaskPane.getContentPane().add(btnCheckStock);

        btnHoldSale.setText("Hold Sale [ F9 ]");
        btnHoldSale.setPreferredSize(new java.awt.Dimension(73, 30));
        btnHoldSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoldSaleActionPerformed(evt);
            }
        });
        billTaskPane.getContentPane().add(btnHoldSale);

        btnRestoreSale.setText("Restore Sale [F10 ]");
        btnRestoreSale.setPreferredSize(new java.awt.Dimension(73, 30));
        btnRestoreSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestoreSaleActionPerformed(evt);
            }
        });
        billTaskPane.getContentPane().add(btnRestoreSale);

        jXTaskPaneContainer1.add(billTaskPane);

        otherTaskPane.setSpecial(true);
        otherTaskPane.setTitle("Other");
        otherTaskPane.setEnabled(false);
        otherTaskPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                otherTaskPaneMouseClicked(evt);
            }
        });

        btnBillRefund.setText("Refund");
        btnBillRefund.setPreferredSize(new java.awt.Dimension(0, 30));
        btnBillRefund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillRefundActionPerformed(evt);
            }
        });
        otherTaskPane.getContentPane().add(btnBillRefund);

        btnBillCopy.setText("Bill Copy");
        btnBillCopy.setPreferredSize(new java.awt.Dimension(0, 30));
        btnBillCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillCopyActionPerformed(evt);
            }
        });
        otherTaskPane.getContentPane().add(btnBillCopy);

        btnCashWithdrawal.setText("Cash Withdrawal");
        btnCashWithdrawal.setPreferredSize(new java.awt.Dimension(0, 30));
        btnCashWithdrawal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashWithdrawalActionPerformed(evt);
            }
        });
        otherTaskPane.getContentPane().add(btnCashWithdrawal);

        jXTaskPaneContainer1.add(otherTaskPane);

        settingsTaskPane.setBackground(new java.awt.Color(102, 102, 102));
        settingsTaskPane.setForeground(new java.awt.Color(153, 153, 153));
        settingsTaskPane.setSpecial(true);
        settingsTaskPane.setTitle("Settings");
        settingsTaskPane.setToolTipText("");
        settingsTaskPane.setEnabled(false);
        settingsTaskPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsTaskPaneMouseClicked(evt);
            }
        });

        btnConfigure.setText("Configure");
        btnConfigure.setPreferredSize(new java.awt.Dimension(73, 30));
        btnConfigure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigureActionPerformed(evt);
            }
        });
        settingsTaskPane.getContentPane().add(btnConfigure);

        jXTaskPaneContainer1.add(settingsTaskPane);

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder3 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder3.setShowLeftShadow(true);
        dropShadowBorder3.setShowTopShadow(true);
        interfaceContainerPanel.setBorder(dropShadowBorder3);

        cardPanel.setLayout(new java.awt.CardLayout());

        welcomePanel.setBackground(new java.awt.Color(153, 153, 153));

        lblWelcome.setFont(new java.awt.Font("Tahoma", 2, 36)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome to MEGA COOP CITY POS");

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pos/coop_logo.png"))); // NOI18N

        javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                        .addGap(0, 431, Short.MAX_VALUE)
                        .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblWelcome)
                .addContainerGap())
        );

        cardPanel.add(welcomePanel, "welcomeCard");

        invoicePanel.setLayout(new java.awt.CardLayout());

        billItemTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        billItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Code", "Description", "Qty", "Price", "Discount", "Sub total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        billItemTable.setRowHeight(21);
        billItemTable.getTableHeader().setReorderingAllowed(false);
        itemTableSP.setViewportView(billItemTable);
        if (billItemTable.getColumnModel().getColumnCount() > 0) {
            billItemTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            billItemTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            billItemTable.getColumnModel().getColumn(2).setPreferredWidth(25);
            billItemTable.getColumnModel().getColumn(3).setPreferredWidth(25);
            billItemTable.getColumnModel().getColumn(4).setPreferredWidth(25);
            billItemTable.getColumnModel().getColumn(5).setPreferredWidth(100);
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

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder4 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder4.setShadowColor(new java.awt.Color(204, 204, 204));
        dropShadowBorder4.setShadowSize(6);
        dropShadowBorder4.setShowLeftShadow(true);
        dropShadowBorder4.setShowTopShadow(true);
        billButtonPanel.setBorder(dropShadowBorder4);

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

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder5 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder5.setShadowColor(new java.awt.Color(204, 204, 204));
        dropShadowBorder5.setShadowSize(6);
        dropShadowBorder5.setShowLeftShadow(true);
        dropShadowBorder5.setShowTopShadow(true);
        productPanel.setBorder(dropShadowBorder5);

        lblCode.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblCode.setText("Code");

        lblProduct.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblProduct.setText("Product");

        lblQty.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblQty.setText("Qty.");

        btnLoad.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLoad.setText("Load [ F3 ]");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

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

        lblUnit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblUnit.setText("<Unit>");

        txtQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setToolTipText("");
        txtQty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtProductDesc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout productPanelLayout = new javax.swing.GroupLayout(productPanel);
        productPanel.setLayout(productPanelLayout);
        productPanelLayout.setHorizontalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addComponent(lblQty, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLoad, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                .addContainerGap())
        );
        productPanelLayout.setVerticalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        billSummeryPanel.setBackground(new java.awt.Color(204, 204, 204));
        billSummeryPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblItemNoDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemNoDisplay.setText("Item(s)");

        lblItems.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItems.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblItems.setText("0");
        lblItems.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblNetDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNetDisplay.setText("Net Total (Rs.)");

        lblNetTotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNetTotal.setForeground(new java.awt.Color(204, 0, 51));
        lblNetTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNetTotal.setText("0.00");
        lblNetTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblItemDiscountDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemDiscountDisplay.setText("Item Discount");

        lblItems1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItems1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblItems1.setText("0.00");
        lblItems1.setToolTipText("");
        lblItems1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout billSummeryPanelLayout = new javax.swing.GroupLayout(billSummeryPanel);
        billSummeryPanel.setLayout(billSummeryPanelLayout);
        billSummeryPanelLayout.setHorizontalGroup(
            billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billSummeryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblItemNoDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblItems, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addComponent(lblItemDiscountDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblItems1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(lblItems1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemDiscountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItems, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(lblBill)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBillNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(billButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(btnInvoiceCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(itemTableSP, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
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

        paymentOptionsSP.setBorder(null);

        paymentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Payment ID", "Option", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paymentsTable.getTableHeader().setReorderingAllowed(false);
        paymentOptionsSP.setViewportView(paymentsTable);

        javax.swing.GroupLayout paymentOptionsPanelLayout = new javax.swing.GroupLayout(paymentOptionsPanel);
        paymentOptionsPanel.setLayout(paymentOptionsPanelLayout);
        paymentOptionsPanelLayout.setHorizontalGroup(
            paymentOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paymentOptionsSP, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
        );
        paymentOptionsPanelLayout.setVerticalGroup(
            paymentOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paymentOptionsSP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder6 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder6.setShowLeftShadow(true);
        dropShadowBorder6.setShowTopShadow(true);
        paymentInfoPanel.setBorder(dropShadowBorder6);

        lblBillValueDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillValueDisplay.setText("Bill Value");

        lblBillValueVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillValueVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillValueVal.setText("0.00");
        lblBillValueVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalDisplay.setText("Total");

        lblTotalVal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalVal.setText("0.00");
        lblTotalVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblBillValueDisplay1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillValueDisplay1.setText("Discount %");

        lblBillValueDisplay2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillValueDisplay2.setText("Discount value");

        lblChangeDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblChangeDisplay.setForeground(new java.awt.Color(204, 0, 51));
        lblChangeDisplay.setText("Change cash");

        lblChangeVal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblChangeVal.setForeground(new java.awt.Color(204, 0, 51));
        lblChangeVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChangeVal.setText("0.00");
        lblChangeVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtDiscountPercent.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
        txtDiscountPercent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountPercent.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtDiscountVal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
        txtDiscountVal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout paymentInfoPanelLayout = new javax.swing.GroupLayout(paymentInfoPanel);
        paymentInfoPanel.setLayout(paymentInfoPanelLayout);
        paymentInfoPanelLayout.setHorizontalGroup(
            paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBillValueDisplay1)
                    .addComponent(lblBillValueDisplay)
                    .addComponent(lblBillValueDisplay2))
                .addGap(18, 18, 18)
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblBillValueVal, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(txtDiscountPercent)
                    .addComponent(txtDiscountVal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 237, Short.MAX_VALUE)
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
                .addContainerGap()
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBillValueVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillValueDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBillValueDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiscountPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblChangeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblChangeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBillValueDisplay2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiscountVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder7 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder7.setShowLeftShadow(true);
        dropShadowBorder7.setShowTopShadow(true);
        paymentSelectorPanel.setBorder(dropShadowBorder7);

        lblPaymentOption.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPaymentOption.setText("Payment Type");

        paymentOptionComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paymentOptionComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash", "Card", "Credit", "Stamp", "Special" }));
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

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder8 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder8.setShowLeftShadow(true);
        dropShadowBorder8.setShowTopShadow(true);
        paymentDetailsPanel.setBorder(dropShadowBorder8);
        paymentDetailsPanel.setLayout(new java.awt.CardLayout());

        lblCashPaymentAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashPaymentAmountDisplay.setText("Amount  (Rs.)");

        txtCashPaymentAmount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#.00"))));
        txtCashPaymentAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCashPaymentAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout cashPaymentPanelLayout = new javax.swing.GroupLayout(cashPaymentPanel);
        cashPaymentPanel.setLayout(cashPaymentPanelLayout);
        cashPaymentPanelLayout.setHorizontalGroup(
            cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCashPaymentAmountDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCashPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(254, Short.MAX_VALUE))
        );
        cashPaymentPanelLayout.setVerticalGroup(
            cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashPaymentAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCashPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(298, Short.MAX_VALUE))
        );

        paymentDetailsPanel.add(cashPaymentPanel, "cashCard");

        lblCashPaymentAmountDisplay1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashPaymentAmountDisplay1.setText("Amount  (Rs.)");

        lblCardType.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCardType.setText("Card Type");

        cardTypeComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Select --", "AMEX", "MASTER", "VISA" }));

        lblCardNo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCardNo.setText("Card No ");

        cardpaymentCardNo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("####"))));
        cardpaymentCardNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cardpaymentCardNo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtCardPaymentAmount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#.00"))));
        txtCardPaymentAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCardPaymentAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout cardPaymentPanelLayout = new javax.swing.GroupLayout(cardPaymentPanel);
        cardPaymentPanel.setLayout(cardPaymentPanelLayout);
        cardPaymentPanelLayout.setHorizontalGroup(
            cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                        .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCashPaymentAmountDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCardPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                        .addComponent(lblCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardpaymentCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(179, Short.MAX_VALUE))
        );
        cardPaymentPanelLayout.setVerticalGroup(
            cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashPaymentAmountDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCardPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardType)
                    .addComponent(cardTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardNo)
                    .addComponent(cardpaymentCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(216, Short.MAX_VALUE))
        );

        paymentDetailsPanel.add(cardPaymentPanel, "bankCard");

        javax.swing.GroupLayout creditpaymentPanelLayout = new javax.swing.GroupLayout(creditpaymentPanel);
        creditpaymentPanel.setLayout(creditpaymentPanelLayout);
        creditpaymentPanelLayout.setHorizontalGroup(
            creditpaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        creditpaymentPanelLayout.setVerticalGroup(
            creditpaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        paymentDetailsPanel.add(creditpaymentPanel, "creditCard");

        javax.swing.GroupLayout stampPaymentLayout = new javax.swing.GroupLayout(stampPayment);
        stampPayment.setLayout(stampPaymentLayout);
        stampPaymentLayout.setHorizontalGroup(
            stampPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        stampPaymentLayout.setVerticalGroup(
            stampPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        paymentDetailsPanel.add(stampPayment, "stampCard");

        javax.swing.GroupLayout specialPaymentLayout = new javax.swing.GroupLayout(specialPayment);
        specialPayment.setLayout(specialPaymentLayout);
        specialPaymentLayout.setHorizontalGroup(
            specialPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        specialPaymentLayout.setVerticalGroup(
            specialPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        paymentDetailsPanel.add(specialPayment, "specialCard");

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
                .addGap(18, 18, 18)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addComponent(paymentInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        invoicePanel.add(paymentPanel, "paymentCard");

        cardPanel.add(invoicePanel, "invoiceCard");

        billPaymentSummeryPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundOtherAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundOtherAmountDisplay.setText("Other ");

        lblRefundCashAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCashAmountDisplay.setText("Cash Amount");

        lblRefundCardAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCardAmountDisplay.setText("Card Amount");

        lblRefundChangeCashDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundChangeCashDisplay.setText("Change Cash");

        lblRefundCashAmountVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCashAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCashAmountVal.setText("<Amount>");
        lblRefundCashAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundCardAmountVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCardAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCardAmountVal.setText("<Amount>");
        lblRefundCardAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundOtherAmountVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundOtherAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundOtherAmountVal.setText("<Amount>");
        lblRefundOtherAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundChangeCashVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundChangeCashVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundChangeCashVal.setText("<Amount>");
        lblRefundChangeCashVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundSubTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundSubTotalDisplay.setText("Sub Total");

        lblRefundSubTotalVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundSubTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundSubTotalVal.setText("<Amount>");
        lblRefundSubTotalVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundDiscountAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundDiscountAmountDisplay.setText("Discount Amount");

        lblRefundDiscountAmountVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundDiscountAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundDiscountAmountVal.setText("<Amount>");
        lblRefundDiscountAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundNetTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundNetTotalDisplay.setText("Net Total");

        lblRefundNetTotalVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundNetTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundNetTotalVal.setText("<Amount>");
        lblRefundNetTotalVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnNewBillFromRefund.setText("New Bill");
        btnNewBillFromRefund.setToolTipText("");
        btnNewBillFromRefund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewBillFromRefundActionPerformed(evt);
            }
        });

        lblRefundCancelSubTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCancelSubTotalDisplay.setText("Cancel Sub");

        lblRefundCancelSubTotalVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCancelSubTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCancelSubTotalVal.setText("<Amount>");
        lblRefundCancelSubTotalVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundCancelDiscountAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCancelDiscountAmountDisplay.setText("Cancel Discount Amount");

        lblRefundCancelDiscountAmountVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCancelDiscountAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCancelDiscountAmountVal.setText("<Amount>");
        lblRefundCancelDiscountAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRefundCancelNetTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCancelNetTotalDisplay.setText("Cancel Net Total");

        lblRefundCancelNetTotalVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRefundCancelNetTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefundCancelNetTotalVal.setText("<Amount>");
        lblRefundCancelNetTotalVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblBillRefundDateDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillRefundDateDisplay.setText("Bill Date : ");

        lblBillDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillDate.setText("<date>");
        lblBillDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnRefundCancel.setText("Cancel");
        btnRefundCancel.setToolTipText("");
        btnRefundCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefundCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout billPaymentSummeryPanel1Layout = new javax.swing.GroupLayout(billPaymentSummeryPanel1);
        billPaymentSummeryPanel1.setLayout(billPaymentSummeryPanel1Layout);
        billPaymentSummeryPanel1Layout.setHorizontalGroup(
            billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addComponent(lblRefundChangeCashDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblRefundChangeCashVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefundCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNewBillFromRefund, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, billPaymentSummeryPanel1Layout.createSequentialGroup()
                                .addComponent(lblRefundCardAmountDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblRefundCardAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, billPaymentSummeryPanel1Layout.createSequentialGroup()
                                .addComponent(lblRefundOtherAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblRefundOtherAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, billPaymentSummeryPanel1Layout.createSequentialGroup()
                                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblRefundCashAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblBillRefundDateDisplay))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblBillDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblRefundCashAmountVal, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))))
                        .addGap(45, 45, 45)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                                .addComponent(lblRefundSubTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblRefundSubTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                                .addComponent(lblRefundDiscountAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblRefundDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                                .addComponent(lblRefundNetTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblRefundNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblRefundCancelNetTotalDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblRefundCancelDiscountAmountDisplay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblRefundCancelSubTotalDisplay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRefundCancelDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCancelSubTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCancelNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        billPaymentSummeryPanel1Layout.setVerticalGroup(
            billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addComponent(lblBillRefundDateDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addComponent(lblBillDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundCashAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCashAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRefundCardAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCardAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundOtherAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundOtherAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundCancelSubTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCancelSubTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundCancelDiscountAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCancelDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundCancelNetTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundCancelNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(billPaymentSummeryPanel1Layout.createSequentialGroup()
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundSubTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundSubTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundDiscountAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRefundNetTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRefundNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(billPaymentSummeryPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRefundChangeCashDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRefundChangeCashVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewBillFromRefund, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefundCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        billRefunditemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Description", "Price", "Qty", "Value", "Disc", "Sub total", "Refund Qty", "Refund"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        billRefunditemTable.setRowHeight(21);
        billRefunditemTable.getTableHeader().setReorderingAllowed(false);
        billItemSP1.setViewportView(billRefunditemTable);
        if (billRefunditemTable.getColumnModel().getColumnCount() > 0) {
            billRefunditemTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            billRefunditemTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            billRefunditemTable.getColumnModel().getColumn(2).setPreferredWidth(25);
            billRefunditemTable.getColumnModel().getColumn(3).setPreferredWidth(25);
            billRefunditemTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        javax.swing.GroupLayout billRefundItemPanelLayout = new javax.swing.GroupLayout(billRefundItemPanel);
        billRefundItemPanel.setLayout(billRefundItemPanelLayout);
        billRefundItemPanelLayout.setHorizontalGroup(
            billRefundItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billItemSP1)
        );
        billRefundItemPanelLayout.setVerticalGroup(
            billRefundItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billItemSP1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );

        lblBill3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBill3.setText("Bill Number : ");

        txtSearchBillNO1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSearchBillNO1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSearchBillNO1.setText("<Bill No>");

        lblBillRefundNoVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillRefundNoVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBillRefundNoVal.setText("<Refund No>");
        lblBillRefundNoVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblBillRefundNoDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillRefundNoDisplay.setText("Bill refund No :");

        javax.swing.GroupLayout billRefundInfoPanelLayout = new javax.swing.GroupLayout(billRefundInfoPanel);
        billRefundInfoPanel.setLayout(billRefundInfoPanelLayout);
        billRefundInfoPanelLayout.setHorizontalGroup(
            billRefundInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billRefundInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBill3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearchBillNO1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblBillRefundNoDisplay)
                .addGap(18, 18, 18)
                .addComponent(lblBillRefundNoVal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        billRefundInfoPanelLayout.setVerticalGroup(
            billRefundInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billRefundInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billRefundInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBill3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearchBillNO1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillRefundNoVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillRefundNoDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        lblBill1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBill1.setText("Bill Number : ");

        txtSearchBillNO.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSearchBillNO.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSearchBillNO.setText("<Bill No>");

        lblBillDateVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillDateVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBillDateVal.setText("<date>");
        lblBillDateVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblBillDateDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillDateDisplay.setText("Date : ");

        lblBillTimeDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillTimeDisplay.setText("Time : ");

        lblBillTimeVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillTimeVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBillTimeVal.setText("<time>");
        lblBillTimeVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblBillCashierDisplay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillCashierDisplay.setText("Cashier : ");

        lblBillCashierVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillCashierVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBillCashierVal.setText("<Cashier name>");
        lblBillCashierVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout billInfoPanelLayout = new javax.swing.GroupLayout(billInfoPanel);
        billInfoPanel.setLayout(billInfoPanelLayout);
        billInfoPanelLayout.setHorizontalGroup(
            billInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBill1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearchBillNO, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                .addComponent(lblBillDateDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillDateVal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillTimeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillCashierDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillCashierVal, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        billInfoPanelLayout.setVerticalGroup(
            billInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBill1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearchBillNO, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillCashierVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillCashierDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillTimeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillDateVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillDateDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        printItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Description", "Price", "Qty", "Sub total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        printItemTable.setRowHeight(21);
        printItemTable.getTableHeader().setReorderingAllowed(false);
        billItemSP.setViewportView(printItemTable);
        if (printItemTable.getColumnModel().getColumnCount() > 0) {
            printItemTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            printItemTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            printItemTable.getColumnModel().getColumn(2).setPreferredWidth(25);
            printItemTable.getColumnModel().getColumn(3).setPreferredWidth(25);
            printItemTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        javax.swing.GroupLayout billItemPanelLayout = new javax.swing.GroupLayout(billItemPanel);
        billItemPanel.setLayout(billItemPanelLayout);
        billItemPanelLayout.setHorizontalGroup(
            billItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billItemSP)
        );
        billItemPanelLayout.setVerticalGroup(
            billItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billItemSP, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );

        billPaymentSummeryPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblOtherAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblOtherAmountDisplay.setText("Other ");

        lblCashAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCashAmountDisplay.setText("Cash Amount");

        lblCardAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCardAmountDisplay.setText("Card Amount");

        lblChangeCashDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblChangeCashDisplay.setText("Change Cash");

        lblCashAmountVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCashAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCashAmountVal.setText("<Amount>");
        lblCashAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblCardAmountVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCardAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCardAmountVal.setText("<Amount>");
        lblCardAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblOtherAmountVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblOtherAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOtherAmountVal.setText("<Amount>");
        lblOtherAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblChangeCashVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblChangeCashVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChangeCashVal.setText("<Amount>");
        lblChangeCashVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblSubTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSubTotalDisplay.setText("Sub Total");

        lblSubTotalVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSubTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubTotalVal.setText("<Amount>");
        lblSubTotalVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblDiscountAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDiscountAmountDisplay.setText("Discount Amount");

        lblDiscountAmountVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDiscountAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiscountAmountVal.setText("<Amount>");
        lblDiscountAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblNetTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNetTotalDisplay.setText("Net Total");

        lblNetTotalVal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNetTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNetTotalVal.setText("<Amount>");
        lblNetTotalVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnCancelPrint.setText("Cancel");
        btnCancelPrint.setToolTipText("");
        btnCancelPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelPrintActionPerformed(evt);
            }
        });

        btnPrintBill.setText("Print");
        btnPrintBill.setToolTipText("");
        btnPrintBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintBillActionPerformed(evt);
            }
        });

        lblBillPrintDateDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillPrintDateDisplay.setText("Bill Date");

        lblBillPrintDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillPrintDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillPrintDate.setText("<date>");
        lblBillPrintDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout billPaymentSummeryPanelLayout = new javax.swing.GroupLayout(billPaymentSummeryPanel);
        billPaymentSummeryPanel.setLayout(billPaymentSummeryPanelLayout);
        billPaymentSummeryPanelLayout.setHorizontalGroup(
            billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                        .addComponent(lblChangeCashDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblChangeCashVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addComponent(lblCardAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblCardAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addComponent(lblOtherAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblOtherAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCashAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblBillPrintDateDisplay))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblBillPrintDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblCashAmountVal, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))))
                        .addGap(45, 45, 45)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addComponent(lblSubTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblSubTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addComponent(lblDiscountAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addComponent(lblNetTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrintBill, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        billPaymentSummeryPanelLayout.setVerticalGroup(
            billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBillPrintDateDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBillPrintDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblCashAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCashAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblCardAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCardAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblOtherAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOtherAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(billPaymentSummeryPanelLayout.createSequentialGroup()
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblSubTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSubTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblDiscountAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDiscountAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblNetTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNetTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblChangeCashDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblChangeCashVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPaymentSummeryPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(billPaymentSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPrintBill, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout billCopyPanelLayout = new javax.swing.GroupLayout(billCopyPanel);
        billCopyPanel.setLayout(billCopyPanelLayout);
        billCopyPanelLayout.setHorizontalGroup(
            billCopyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billCopyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billCopyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(billInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(billItemPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(billPaymentSummeryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        billCopyPanelLayout.setVerticalGroup(
            billCopyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billCopyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(billInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billItemPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billPaymentSummeryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cardPanel.add(billCopyPanel, "billCopyCard");

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

        btnCachierLog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCachierLog.setText("Cashier Log ");
        btnCachierLog.setPreferredSize(new java.awt.Dimension(71, 35));
        btnCachierLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCachierLogActionPerformed(evt);
            }
        });

        btnManagerLog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnManagerLog.setText("Manager Log Off");
        btnManagerLog.setPreferredSize(new java.awt.Dimension(71, 35));
        btnManagerLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManagerLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainLayerPanelLayout = new javax.swing.GroupLayout(mainLayerPanel);
        mainLayerPanel.setLayout(mainLayerPanelLayout);
        mainLayerPanelLayout.setHorizontalGroup(
            mainLayerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainLayerPanelLayout.createSequentialGroup()
                .addGroup(mainLayerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCachierLog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnManagerLog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(counterInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainLayerPanelLayout.setVerticalGroup(
            mainLayerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayerPanelLayout.createSequentialGroup()
                .addComponent(counterInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCachierLog, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnManagerLog, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainLayerPanel.setLayer(counterInfoPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainLayerPanel.setLayer(sidePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainLayerPanel.setLayer(interfaceContainerPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainLayerPanel.setLayer(btnCachierLog, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainLayerPanel.setLayer(btnManagerLog, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout uiPanelLayout = new javax.swing.GroupLayout(uiPanel);
        uiPanel.setLayout(uiPanelLayout);
        uiPanelLayout.setHorizontalGroup(
            uiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainLayerPanel)
        );
        uiPanelLayout.setVerticalGroup(
            uiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainLayerPanel, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        statusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(uiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(uiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
        // TODO add your handling code here:
        bill_showPaymentScreen();
    }//GEN-LAST:event_btnPaymentActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        bill_showAddItemPanel();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnInvoiceCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvoiceCancelActionPerformed
        // TODO add your handling code here:'
        bill_cancelBill();
    }//GEN-LAST:event_btnInvoiceCancelActionPerformed

    private void billTaskPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billTaskPaneMouseClicked
        // TODO add your handling code here:
        bill_showInvoicePanel();
    }//GEN-LAST:event_billTaskPaneMouseClicked

    private void btnBillRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillRefundActionPerformed
        // TODO add your handling code here:
        refund_showRefundPanel();
    }//GEN-LAST:event_btnBillRefundActionPerformed

    private void btnBillCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillCopyActionPerformed
        // TODO add your handling code here:
        billCopy_ShowPanel();
    }//GEN-LAST:event_btnBillCopyActionPerformed

    private void btnCashWithdrawalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashWithdrawalActionPerformed
        // TODO add your handling code here:
        cashWithdrawal_showUI();
    }//GEN-LAST:event_btnCashWithdrawalActionPerformed

    private void btnCancelPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelPrintActionPerformed
        // TODO add your handling code here:
        billCopy_cancel();
    }//GEN-LAST:event_btnCancelPrintActionPerformed

    private void paymentOptionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentOptionComboBoxActionPerformed
        // TODO add your handling code here:
        bill_togglePaymentOptions(evt);
    }//GEN-LAST:event_paymentOptionComboBoxActionPerformed

    private void btnCheckStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckStockActionPerformed
        // TODO add your handling code here:
        chechStock();
    }//GEN-LAST:event_btnCheckStockActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        bill_searchItem();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnRefundCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefundCancelActionPerformed
        // TODO add your handling code here:
        refund_cancel();
    }//GEN-LAST:event_btnRefundCancelActionPerformed

    private void settingsTaskPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsTaskPaneMouseClicked
        // TODO add your handling code here:
        settings_showTaskPane(evt);
    }//GEN-LAST:event_settingsTaskPaneMouseClicked

    private void btnCachierLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCachierLogActionPerformed
        cashier_logIn();
    }//GEN-LAST:event_btnCachierLogActionPerformed

    private void otherTaskPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_otherTaskPaneMouseClicked
        // TODO add your handling code here:
        other_showTaskPane(evt);
    }//GEN-LAST:event_otherTaskPaneMouseClicked

    private void btnNewSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSaleActionPerformed
        // TODO add your handling code here:
        bill_newSale();
    }//GEN-LAST:event_btnNewSaleActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        // TODO add your handling code here:
        bill_deleteItemFromBillItemTable();
    }//GEN-LAST:event_btnDeleteItemActionPerformed

    private void btnClearItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearItemActionPerformed
        // TODO add your handling code here:
        bill_clearSelectedItemSearch();
    }//GEN-LAST:event_btnClearItemActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        // TODO add your handling code here:
        bill_addItemToBillItemTable();
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        // TODO add your handling code here:
        bill_loadLtemInfoToBill();
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnPrintBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintBillActionPerformed
        // TODO add your handling code here:
        billCopy_printBill();
    }//GEN-LAST:event_btnPrintBillActionPerformed

    private void btnConfigureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigureActionPerformed
        // TODO add your handling code here:
        configure_show();
    }//GEN-LAST:event_btnConfigureActionPerformed

    private void btnHoldSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoldSaleActionPerformed
        // TODO add your handling code here:
        bill_holdSale();
    }//GEN-LAST:event_btnHoldSaleActionPerformed

    private void btnRestoreSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreSaleActionPerformed
        // TODO add your handling code here:
        bill_restoreSale();
    }//GEN-LAST:event_btnRestoreSaleActionPerformed

    private void btnNewBillFromRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewBillFromRefundActionPerformed
        // TODO add your handling code here:
        refund_newBill();
    }//GEN-LAST:event_btnNewBillFromRefundActionPerformed

    private void btnAddPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPaymentActionPerformed
        // TODO add your handling code here:
        bill_addPaymentOption();
    }//GEN-LAST:event_btnAddPaymentActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        bill_removePaymentOption();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        // TODO add your handling code here:
        bill_confirm();
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnManagerLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManagerLogActionPerformed
        // TODO add your handling code here:
        manager_logIn();
    }//GEN-LAST:event_btnManagerLogActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        setupUI();

        java.awt.EventQueue.invokeLater(() -> {
            new POSInterface().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel billButtonPanel;
    private javax.swing.JPanel billCopyPanel;
    private javax.swing.JPanel billInfoPanel;
    private javax.swing.JPanel billItemPanel;
    private javax.swing.JScrollPane billItemSP;
    private javax.swing.JScrollPane billItemSP1;
    private javax.swing.JTable billItemTable;
    private javax.swing.JPanel billPaymentSummeryPanel;
    private javax.swing.JPanel billPaymentSummeryPanel1;
    private javax.swing.JPanel billRefundItemPanel;
    private javax.swing.JTable billRefunditemTable;
    private javax.swing.JPanel billSummeryPanel;
    private org.jdesktop.swingx.JXTaskPane billTaskPane;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnAddPayment;
    private javax.swing.JButton btnBillCopy;
    private javax.swing.JButton btnBillRefund;
    private javax.swing.JButton btnCachierLog;
    private javax.swing.JButton btnCancelPrint;
    private javax.swing.JButton btnCashWithdrawal;
    private javax.swing.JButton btnCheckStock;
    private javax.swing.JButton btnClearItem;
    private javax.swing.JButton btnConfigure;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnHoldSale;
    private javax.swing.JButton btnInvoiceCancel;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnManagerLog;
    private javax.swing.JButton btnNewBillFromRefund;
    private javax.swing.JButton btnNewSale;
    private javax.swing.JButton btnPayment;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnPrintBill;
    private javax.swing.JButton btnRefundCancel;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnRestoreSale;
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JPanel cardPaymentPanel;
    private javax.swing.JComboBox cardTypeComboBox;
    private javax.swing.JFormattedTextField cardpaymentCardNo;
    private javax.swing.JPanel cashPaymentPanel;
    private javax.swing.JPanel counterInfoPanel;
    private javax.swing.JPanel creditpaymentPanel;
    private javax.swing.JPanel interfaceContainerPanel;
    private javax.swing.JPanel invoicePanel;
    private javax.swing.JPanel itemAddPanel;
    private javax.swing.JComboBox itemCodeComboBox;
    private javax.swing.JScrollPane itemTableSP;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    private javax.swing.JLabel lblBill;
    private javax.swing.JLabel lblBill1;
    private javax.swing.JLabel lblBill3;
    private javax.swing.JLabel lblBillCashierDisplay;
    private javax.swing.JLabel lblBillCashierVal;
    private javax.swing.JLabel lblBillDate;
    private javax.swing.JLabel lblBillDateDisplay;
    private javax.swing.JLabel lblBillDateVal;
    private javax.swing.JLabel lblBillPrintDate;
    private javax.swing.JLabel lblBillPrintDateDisplay;
    private javax.swing.JLabel lblBillRefundDateDisplay;
    private javax.swing.JLabel lblBillRefundNoDisplay;
    private javax.swing.JLabel lblBillRefundNoVal;
    private javax.swing.JLabel lblBillTimeDisplay;
    private javax.swing.JLabel lblBillTimeVal;
    private javax.swing.JLabel lblBillValueDisplay;
    private javax.swing.JLabel lblBillValueDisplay1;
    private javax.swing.JLabel lblBillValueDisplay2;
    private javax.swing.JLabel lblBillValueVal;
    private javax.swing.JLabel lblCardAmountDisplay;
    private javax.swing.JLabel lblCardAmountVal;
    private javax.swing.JLabel lblCardNo;
    private javax.swing.JLabel lblCardType;
    private javax.swing.JLabel lblCashAmountDisplay;
    private javax.swing.JLabel lblCashAmountVal;
    private javax.swing.JLabel lblCashPaymentAmountDisplay;
    private javax.swing.JLabel lblCashPaymentAmountDisplay1;
    private javax.swing.JLabel lblCashier;
    private javax.swing.JLabel lblCashierDisplay;
    private javax.swing.JLabel lblChangeCashDisplay;
    private javax.swing.JLabel lblChangeCashVal;
    private javax.swing.JLabel lblChangeDisplay;
    private javax.swing.JLabel lblChangeVal;
    private javax.swing.JLabel lblCode;
    private javax.swing.JLabel lblCounter;
    private javax.swing.JLabel lblCounterDisplay;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDateDisplay;
    private javax.swing.JLabel lblDiscountAmountDisplay;
    private javax.swing.JLabel lblDiscountAmountVal;
    private javax.swing.JLabel lblItemDiscountDisplay;
    private javax.swing.JLabel lblItemNoDisplay;
    private javax.swing.JLabel lblItems;
    private javax.swing.JLabel lblItems1;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNetDisplay;
    private javax.swing.JLabel lblNetTotal;
    private javax.swing.JLabel lblNetTotalDisplay;
    private javax.swing.JLabel lblNetTotalVal;
    private javax.swing.JLabel lblOtherAmountDisplay;
    private javax.swing.JLabel lblOtherAmountVal;
    private javax.swing.JLabel lblPaymentOption;
    private javax.swing.JLabel lblProduct;
    private javax.swing.JLabel lblQty;
    private javax.swing.JLabel lblRefundCancelDiscountAmountDisplay;
    private javax.swing.JLabel lblRefundCancelNetTotalDisplay;
    private javax.swing.JLabel lblRefundCancelNetTotalVal;
    private javax.swing.JLabel lblRefundCancelSubTotalDisplay;
    private javax.swing.JLabel lblRefundCancelSubTotalVal;
    private javax.swing.JLabel lblRefundCardAmountDisplay;
    private javax.swing.JLabel lblRefundCardAmountVal;
    private javax.swing.JLabel lblRefundCashAmountDisplay;
    private javax.swing.JLabel lblRefundCashAmountVal;
    private javax.swing.JLabel lblRefundChangeCashDisplay;
    private javax.swing.JLabel lblRefundChangeCashVal;
    private javax.swing.JLabel lblRefundDiscountAmountDisplay;
    private javax.swing.JLabel lblRefundDiscountAmountVal;
    private javax.swing.JLabel lblRefundNetTotalDisplay;
    private javax.swing.JLabel lblRefundNetTotalVal;
    private javax.swing.JLabel lblRefundOtherAmountDisplay;
    private javax.swing.JLabel lblRefundOtherAmountVal;
    private javax.swing.JLabel lblRefundSubTotalDisplay;
    private javax.swing.JLabel lblRefundSubTotalVal;
    private javax.swing.JLabel lblSubTotalDisplay;
    private javax.swing.JLabel lblSubTotalVal;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTimeDisplay;
    private javax.swing.JLabel lblTotalDisplay;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JLayeredPane mainLayerPanel;
    private org.jdesktop.swingx.JXTaskPane otherTaskPane;
    private javax.swing.JPanel paymentDetailsPanel;
    private javax.swing.JPanel paymentInfoPanel;
    private javax.swing.JComboBox paymentOptionComboBox;
    private javax.swing.JPanel paymentOptionsPanel;
    private javax.swing.JScrollPane paymentOptionsSP;
    private javax.swing.JPanel paymentPanel;
    private javax.swing.JPanel paymentSelectorPanel;
    private javax.swing.JTable paymentsTable;
    private javax.swing.JTable printItemTable;
    private javax.swing.JPanel productPanel;
    private javax.swing.JPanel refundPanel;
    private org.jdesktop.swingx.JXTaskPane settingsTaskPane;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JPanel specialPayment;
    private javax.swing.JPanel stampPayment;
    private org.jdesktop.swingx.JXStatusBar statusBar;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextField txtBillNumber;
    private javax.swing.JFormattedTextField txtCardPaymentAmount;
    private javax.swing.JFormattedTextField txtCashPaymentAmount;
    private javax.swing.JFormattedTextField txtDiscountPercent;
    private javax.swing.JFormattedTextField txtDiscountVal;
    private javax.swing.JTextField txtProductDesc;
    private javax.swing.JFormattedTextField txtQty;
    private javax.swing.JTextField txtSearchBillNO;
    private javax.swing.JTextField txtSearchBillNO1;
    private javax.swing.JPanel uiPanel;
    private javax.swing.JPanel welcomePanel;
    // End of variables declaration//GEN-END:variables
}
