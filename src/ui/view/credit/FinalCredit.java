/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.credit;

import controller.payments.CoopCreditPaymentController;
import controller.credit.CustomerCreditController;
import controller.pos.InvoiceController;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;
import model.creditManagement.CreditCustomer;
import model.pos.item.Invoice;
import model.pos.payment.CoopCreditPayment;
import net.proteanit.sql.DbUtils;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.painter.Painter;
import util.NICFilter;
import util.TelePhoneNumberFilter;
import util.Utilities;

/**
 *
 * @author HP
 */
public class FinalCredit extends javax.swing.JFrame {

    private Painter p;
    //Variables
    private static final Logger logger = Logger.getLogger(FinalCredit.class);
    private final int CUSTOMER_NAME = 0;
    private final int CUSTOMER_ADDRESS = 1;
    private final int TELEPHONE = 2;
    private final int NIC = 3;
    private final int CUSTOMER_ID = 4;
    private final int CURRENT_CREDIT = 5;

    private ArrayList<JCheckBox> chBoxes;
    private HashMap<Integer, CreditCustomer> availableCustomers;
    DefaultTableModel CustomerCreditDetailsTableModel;
    DefaultComboBoxModel CreditDetailsCustomerComboBoxModel;
    CustomerCreditController l = new CustomerCreditController();

    DBHandler t;
    ResultSet rs;

    CreditCustomer creditCustomer;
    int coopIdNum = 0;

    /**
     * Creates new form finalCredit
     */
    public FinalCredit() throws SQLException {
        initComponents();
        chBoxes = new ArrayList<>();
        cardPanel.setPreferredSize(new Dimension(800, 800));
        setSize(new Dimension(1500, 1500));

        this.CustomerCreditDetailsTableModel = (DefaultTableModel) customerDetailsTbl.getModel();
        this.CreditDetailsCustomerComboBoxModel = (DefaultComboBoxModel) creditDetailsCustomerComboBox.getModel();
        customerDetailsTbl.setModel(DbUtils.resultSetToTableModel(CustomerCreditController.loadDetails()));
        loadDetails();
        /**
         *
         * @throws SQLException
         */

        //jnj - adding documnet filters============================================
        ((PlainDocument) addCustomerTeleTxt1.getDocument()).setDocumentFilter(new TelePhoneNumberFilter());
        ((PlainDocument) addCustomerNicTxt1.getDocument()).setDocumentFilter(new NICFilter());

        //=========================================================================
    }

    public void loadDetails() throws SQLException {

        CreditDetailsCustomerComboBoxModel.removeAllElements();
        ArrayList<CreditCustomer> customerDetails = CustomerCreditController.loadCustomers();

        for (CreditCustomer customer : customerDetails) {
            // availableCustomers.put(customer.getCustomerId(), customer);
            CreditDetailsCustomerComboBoxModel.addElement(customer.getCustomerName());
        }
        CreditDetailsCustomerComboBoxModel.setSelectedItem(null);
        //   CoopCreditPaymentController.getCoopCreditPaymentDetails(availableCustomers.CreditDetailsCustomerComboBoxModel.getSelectedItem().toString())

    }

    //helper methods
    public void chngTblModel() throws SQLException {

        customerDetailsTbl.setModel(DbUtils.resultSetToTableModel(CustomerCreditController.loadDetails()));

    }

    public void setEditDetails() throws SQLException {
        int customerId = 0;
        int customerTele = 0;
        Object[] ob = {
            editCustomerCoopIdTxt2.getText(),
            editCustomerFullNameTxt2.getText(),
            editCustomerAddressTxt2.getText(),
            editCustomerNicTxt2.getText(),
            editCustomerTeleTxt2.getText(),};

        try {
            customerId = Integer.parseInt(editCustomerCoopIdTxt2.getText());

        } catch (NumberFormatException x) {
            Utilities.showMsgBox("System Error", "Error", JOptionPane.WARNING_MESSAGE);
        }
        try {
            customerTele = Integer.parseInt(editCustomerTeleTxt2.getText());

        } catch (NumberFormatException x) {
            Utilities.showMsgBox("System Error", "Error", JOptionPane.WARNING_MESSAGE);
        }
        CreditCustomer creditCustomer = new CreditCustomer(customerId, ob[1].toString(), ob[2].toString(), customerTele, ob[3].toString(), 10000);

        CustomerCreditController.setEditDetails(creditCustomer, Integer.parseInt(editCustomerCoopIdTxt2.getText()));
        chngTblModel();

        //CustomerCreditDetailsTableModel.removeRow(Integer.parseInt(editCustomerCoopIdTxt2.getText())-1);
        //  CustomerCreditDetailsTableModel.insertRow(Integer.parseInt(editCustomerCoopIdTxt2.getText())-1, ob);
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerDetails");
        //      CreditCustomer creditCustomer = new CreditCustomer(customerId,ob[1].toString(),ob[2].toString(),customerTele,ob[3].toString(),10000);

        CustomerCreditController.setEditDetails(creditCustomer, Integer.parseInt(ob[4].toString()));
        editCustomerCoopIdTxt2.setText(null);
        editCustomerFullNameTxt2.setText(null);
        editCustomerAddressTxt2.setText(null);
        editCustomerNicTxt2.setText(null);
        editCustomerTeleTxt2.setText(null);

    }

    public void addCustomer() throws SQLException {
        int customerId = 0;
        int customerTele = 0;
        Object[] ob = {
            //  Utilities.convertKeyToInteger(addCustomerCoopIdTxt1.getText()),
            addCustomerCoopIdTxt1.getText(),
            addCustomerFullNameTxt1.getText(),
            addCustomerAddressTxt1.getText(),
            addCustomerNicTxt1.getText(),
            addCustomerTeleTxt1.getText(),};

        try {
            customerId = Integer.parseInt(addCustomerCoopIdTxt1.getText());

        } catch (NumberFormatException x) {
            Utilities.showMsgBox("System Error", "Error", JOptionPane.WARNING_MESSAGE);
        }
        try {
            customerTele = Integer.parseInt(addCustomerTeleTxt1.getText());

        } catch (NumberFormatException x) {
            Utilities.showMsgBox("System Error", "Error", JOptionPane.WARNING_MESSAGE);
        }
        CreditCustomer creditCustomer = new CreditCustomer(customerId, ob[1].toString(), ob[2].toString(), customerTele, ob[3].toString(), 0.00);

        CustomerCreditController.setDetails(creditCustomer);
        chngTblModel();

        //CustomerCreditDetailsTableModel.removeRow(Integer.parseInt(editCustomerCoopIdTxt2.getText())-1);
        //  CustomerCreditDetailsTableModel.insertRow(Integer.parseInt(editCustomerCoopIdTxt2.getText())-1, ob);
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerDetails");
        CustomerCreditController.setEditDetails(creditCustomer, Integer.parseInt(ob[4].toString()));
        addCustomerCoopIdTxt1.setText(null);
        addCustomerFullNameTxt1.setText(null);
        addCustomerAddressTxt1.setText(null);
        addCustomerNicTxt1.setText(null);
        addCustomerTeleTxt1.setText(null);

    }

    public void deleteCustomerDetails(int i) {
        try {
            CustomerCreditController.deleteDeatils(i);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            chngTblModel();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCoopId() {
        try {
            coopIdNum = CustomerCreditController.getLastCreditCustomerId();
        } catch (SQLException ex) {
            coopIdNum = 1;
        }
        System.out.println(coopIdNum);
        // addCustomerCoopIdTxt1.setText((Utilities.convertKeyToString(coopIdNum + 1, DatabaseInterface.CREDIT_CUSTOMER)));
        addCustomerCoopIdTxt1.setText(coopIdNum + 1 + "");
    }

    public void editCustomerDetails(int i) throws SQLException {
        int customerId = 0;
        int customerTele = 0;

        CustomerCreditController.getDetails(i);
        //      CreditCustomer creditCustomer = new CreditCustomer(ob[0].toString(),ob[1].toString(),Integer.parseInt(ob[2].toString()),ob[3].toString(),Integer.parseInt(ob[4].toString()),Integer.parseInt(ob[5].toString()));

        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "editCustomer");
        editCustomerFullNameTxt2.setText(CustomerCreditController.getDetails(i).getCustomerName());
        editCustomerAddressTxt2.setText(CustomerCreditController.getDetails(i).getCustomerAddress());
        editCustomerTeleTxt2.setText(Integer.toString(CustomerCreditController.getDetails(i).getTelephone()));
        editCustomerNicTxt2.setText(CustomerCreditController.getDetails(i).getNic());
        editCustomerCoopIdTxt2.setText(Integer.toString(CustomerCreditController.getDetails(i).getCustomerId()));
    }

    public void searchDetails() throws SQLException {
        String s = jTextField1.getText();

        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerSearchResult");

        customerSearchCustomerNameTxt.setText(CustomerCreditController.searchDetails(s).getCustomerName());
        customerSearchCoopIdTxt.setText(Integer.toString(CustomerCreditController.searchDetails(s).getCustomerId()));

    }

    public void loadCreditDetails() throws SQLException {
        CreditCustomer customer = (CustomerCreditController.searchDetails(CreditDetailsCustomerComboBoxModel.getSelectedItem().toString()));
        double paidAmount = 0;
        double billTotal = 0;
        creditDetailsSettlementNoTxt.setText(customer.getCustomerId() + "");
        ((DefaultTableModel) creditTbl.getModel()).setRowCount(0);
        ArrayList<CoopCreditPayment> details = CoopCreditPaymentController.loadDetails(customer.getCustomerId());
        for (CoopCreditPayment c : details) {
            int row = creditTbl.getRowCount();
            paidAmount += c.getAmountSettled();
            billTotal += c.getAmount();
            Invoice invoice = InvoiceController.getInvoice(c.getInvoiceId());
            ((DefaultTableModel) creditTbl.getModel()).addRow(new Object[]{false, c.getInvoiceId() + "", invoice.getDate(), c.getAmount() + "", c.getAmountSettled() + ""});

//            ((DefaultTableModel)creditTbl.getModel()).setRowCount(row+ 1);
//            creditTbl.setValueAt(c.getInvoiceId() , row , 1);
//            creditTbl.setValueAt(invoice.getDate() , row , 2);
//            creditTbl.setValueAt(c.getAmount() , row , 3);
//            creditTbl.setValueAt(c.getAmountSettled() , row , 4);
        }
        billTxt1.setText(billTotal + "");
        paidAmountTxt1.setText(paidAmount + "");

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        customerGender = new javax.swing.ButtonGroup();
        glossPainter1 = new org.jdesktop.swingx.painter.GlossPainter();
        glossPainter2 = new org.jdesktop.swingx.painter.GlossPainter();
        alphaPainter1 = new org.jdesktop.swingx.painter.AlphaPainter();
        shapePainter1 = new org.jdesktop.swingx.painter.ShapePainter();
        rectanglePainter1 = new org.jdesktop.swingx.painter.RectanglePainter();
        busyPainter1 = new org.jdesktop.swingx.painter.BusyPainter();
        taskPaneSearch = new javax.swing.ButtonGroup();
        jXTitledPanel3 = new org.jdesktop.swingx.JXTitledPanel();
        jXTitledPanel4 = new org.jdesktop.swingx.JXTitledPanel();
        checkerboardPainter1 = new org.jdesktop.swingx.painter.CheckerboardPainter();
        glossPainter3 = new org.jdesktop.swingx.painter.GlossPainter();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        customerSearchTaskPane = new org.jdesktop.swingx.JXTaskPane();
        customerSearchSearchLbl = new javax.swing.JButton();
        manageCustomerTaskPane = new org.jdesktop.swingx.JXTaskPane();
        manageCustomerCustomerDetailsLbl = new javax.swing.JButton();
        manageCustomerAddCustomerLbl = new javax.swing.JButton();
        creditManagementTaskPane = new org.jdesktop.swingx.JXTaskPane();
        creditManagementCreditDetailsLbl = new javax.swing.JButton();
        cardPanel = new javax.swing.JPanel();
        customerSearch = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        custoemrSearchBtn = new javax.swing.JButton();
        customerSearchBackBtn = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        installmentPaymentLbl2 = new javax.swing.JLabel();
        customerSearchResultPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        customerSearchCustomerNameLbl = new javax.swing.JLabel();
        customerSearchCoopIdTxt = new javax.swing.JLabel();
        customerSearchcoopIdLbl = new javax.swing.JLabel();
        customerSearchCustomerNameTxt = new javax.swing.JLabel();
        customerSearchCreditDetailsBtn = new javax.swing.JButton();
        customerSearchOkBtn = new javax.swing.JButton();
        customerSearchLBl = new javax.swing.JLabel();
        installmentPaymentPanel = new javax.swing.JPanel();
        installmentPaymentPanelSmall = new javax.swing.JPanel();
        installmetPaymentCustomerNameLbl = new javax.swing.JLabel();
        InstallmentPaymentDateLbl = new javax.swing.JLabel();
        installmentPaymentSubmitBtn = new javax.swing.JButton();
        installmentPaymentLbl = new javax.swing.JLabel();
        InstallmentPaymentCancelBtn = new javax.swing.JButton();
        InstallmentPaymentDatePicker = new org.jdesktop.swingx.JXDatePicker();
        InstallmentPaymentCustomerNameLblValue = new javax.swing.JLabel();
        InstallmetPaymentPaymentValue = new javax.swing.JLabel();
        InstallmentPaymentPaymentAmountLbl = new javax.swing.JLabel();
        addCustomer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        addCustomerAddBtn1 = new org.jdesktop.swingx.JXButton();
        addCustomerFullNameLbl1 = new javax.swing.JLabel();
        addCustomerCoopIdLbl1 = new javax.swing.JLabel();
        addCustomerAddressLbl1 = new javax.swing.JLabel();
        addCustomerFullNameTxt1 = new javax.swing.JTextField();
        addCustomerCoopIdTxt1 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        addCustomerAddressTxt1 = new javax.swing.JTextArea();
        addCustomerNicLbl1 = new javax.swing.JLabel();
        addCustomerTeleTxt1 = new javax.swing.JTextField();
        addCustomerTeleLbl1 = new javax.swing.JLabel();
        addCustomerNicTxt1 = new javax.swing.JTextField();
        newCustomerLbl = new javax.swing.JLabel();
        creditDetails = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        creditTbl = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        billTotalLbl1 = new org.jdesktop.swingx.JXLabel();
        billTxt1 = new org.jdesktop.swingx.JXLabel();
        paidAmountTxt1 = new org.jdesktop.swingx.JXLabel();
        paidAmountlbl1 = new org.jdesktop.swingx.JXLabel();
        cancelBtn1 = new javax.swing.JButton();
        confirmBtn1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        creditDetailsCreditorLbl = new javax.swing.JLabel();
        creditDetailsSettleentNoLbl = new javax.swing.JLabel();
        creditDetailsSettlementNoTxt = new javax.swing.JLabel();
        creditDetailsCustomerComboBox = new javax.swing.JComboBox();
        creditDetailsSettleentDateLbl1 = new javax.swing.JLabel();
        creditDetailsSettlementDateTxt1 = new javax.swing.JLabel();
        installmentPaymentLbl4 = new javax.swing.JLabel();
        checkCreditDetailsBtn = new javax.swing.JButton();
        editCustomer = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        editCustomerSaveBtn2 = new org.jdesktop.swingx.JXButton();
        editCustomerFullNameLbl2 = new javax.swing.JLabel();
        editCustomerCoopIdLbl2 = new javax.swing.JLabel();
        editCustomerAddressLbl2 = new javax.swing.JLabel();
        editCustomerFullNameTxt2 = new javax.swing.JTextField();
        editCustomerCoopIdTxt2 = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        editCustomerAddressTxt2 = new javax.swing.JTextArea();
        editCustomerNicLbl2 = new javax.swing.JLabel();
        editCustomerTeleTxt2 = new javax.swing.JTextField();
        editCustomerTeleLbl2 = new javax.swing.JLabel();
        editCustomerNicTxt2 = new javax.swing.JTextField();
        editCustomerLbl2 = new javax.swing.JLabel();
        customerDetails = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        customerDetailsTbl = new org.jdesktop.swingx.JXTable();
        jXPanel2 = new org.jdesktop.swingx.JXPanel();
        editCustomerBtn = new org.jdesktop.swingx.JXButton();
        deleteCustomerBtn = new org.jdesktop.swingx.JXButton();
        customerDetailsCreditDetailsBtn = new org.jdesktop.swingx.JXButton();
        newCustomerLbl1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jXTitledPanel3Layout = new javax.swing.GroupLayout(jXTitledPanel3.getContentContainer());
        jXTitledPanel3.getContentContainer().setLayout(jXTitledPanel3Layout);
        jXTitledPanel3Layout.setHorizontalGroup(
            jXTitledPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jXTitledPanel3Layout.setVerticalGroup(
            jXTitledPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jXTitledPanel4Layout = new javax.swing.GroupLayout(jXTitledPanel4.getContentContainer());
        jXTitledPanel4.getContentContainer().setLayout(jXTitledPanel4Layout);
        jXTitledPanel4Layout.setHorizontalGroup(
            jXTitledPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jXTitledPanel4Layout.setVerticalGroup(
            jXTitledPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jXTaskPaneContainer1.setBackground(new java.awt.Color(102, 102, 102));
        jXTaskPaneContainer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jXTaskPaneContainer1.setVerifyInputWhenFocusTarget(false);

        customerSearchTaskPane.setBackground(new java.awt.Color(204, 204, 204));
        customerSearchTaskPane.setSpecial(true);
        customerSearchTaskPane.setTitle("Customer Search");
        customerSearchTaskPane.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        customerSearchSearchLbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        customerSearchSearchLbl.setText("Search");
        customerSearchSearchLbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerSearchSearchLblActionPerformed(evt);
            }
        });
        customerSearchTaskPane.getContentPane().add(customerSearchSearchLbl);

        jXTaskPaneContainer1.add(customerSearchTaskPane);

        manageCustomerTaskPane.setBackground(new java.awt.Color(204, 204, 204));
        manageCustomerTaskPane.setSpecial(true);
        manageCustomerTaskPane.setTitle("Manage Customer");
        manageCustomerTaskPane.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        manageCustomerTaskPane.getContentPane().setLayout(new org.jdesktop.swingx.VerticalLayout());

        manageCustomerCustomerDetailsLbl.setText("Customer Details");
        manageCustomerCustomerDetailsLbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageCustomerCustomerDetailsLblActionPerformed(evt);
            }
        });
        manageCustomerTaskPane.getContentPane().add(manageCustomerCustomerDetailsLbl);

        manageCustomerAddCustomerLbl.setText("Add Customer");
        manageCustomerAddCustomerLbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageCustomerAddCustomerLblActionPerformed(evt);
            }
        });
        manageCustomerTaskPane.getContentPane().add(manageCustomerAddCustomerLbl);

        jXTaskPaneContainer1.add(manageCustomerTaskPane);

        creditManagementTaskPane.setBackground(new java.awt.Color(204, 204, 204));
        //UIManager.put("TaskPane.titleBackgroundGradientStart", java.awt.Color(255, 255, 255));
        //UIManager.put("TaskPane.titleBackgroundGradientEnd", java.awt.Color(204, 204, 204));
        creditManagementTaskPane.setSpecial(true);
        creditManagementTaskPane.setTitle("Credit Management");
        creditManagementTaskPane.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        creditManagementCreditDetailsLbl.setText("Credit Details");
        creditManagementCreditDetailsLbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditManagementCreditDetailsLblActionPerformed(evt);
            }
        });
        creditManagementTaskPane.getContentPane().add(creditManagementCreditDetailsLbl);

        jXTaskPaneContainer1.add(creditManagementTaskPane);

        cardPanel.setLayout(new java.awt.CardLayout());

        customerSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        customerSearch.setName("paymentHistory");

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        java.awt.GridBagLayout jPanel8Layout = new java.awt.GridBagLayout();
        jPanel8Layout.columnWidths = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        jPanel8Layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        jPanel8.setLayout(jPanel8Layout);

        jLabel54.setText("Customer Name/Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        jPanel8.add(jLabel54, gridBagConstraints);

        custoemrSearchBtn.setText("Search");
        custoemrSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                custoemrSearchBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 46;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jPanel8.add(custoemrSearchBtn, gridBagConstraints);

        customerSearchBackBtn.setText("Back");
        customerSearchBackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerSearchBackBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 42;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipady = 4;
        jPanel8.add(customerSearchBackBtn, gridBagConstraints);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 18;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 31;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jPanel8.add(jTextField1, gridBagConstraints);

        installmentPaymentLbl2.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl2.setText("Customer Search                            ");
        installmentPaymentLbl2.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl2.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 47;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(11, 1, 11, 12);
        jPanel8.add(installmentPaymentLbl2, gridBagConstraints);

        javax.swing.GroupLayout customerSearchLayout = new javax.swing.GroupLayout(customerSearch);
        customerSearch.setLayout(customerSearchLayout);
        customerSearchLayout.setHorizontalGroup(
            customerSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerSearchLayout.createSequentialGroup()
                .addContainerGap(607, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(400, 400, 400))
        );
        customerSearchLayout.setVerticalGroup(
            customerSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerSearchLayout.createSequentialGroup()
                .addContainerGap(316, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(400, 400, 400))
        );

        cardPanel.add(customerSearch, "customerSearch");

        customerSearchResultPanel.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        customerSearchCustomerNameLbl.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(9, 14, 7, 13);
        jPanel4.add(customerSearchCustomerNameLbl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.ipady = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 10, 0, 29);
        jPanel4.add(customerSearchCoopIdTxt, gridBagConstraints);

        customerSearchcoopIdLbl.setText("Coop Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 4, 0);
        jPanel4.add(customerSearchcoopIdLbl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 244;
        gridBagConstraints.ipady = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 29);
        jPanel4.add(customerSearchCustomerNameTxt, gridBagConstraints);

        customerSearchCreditDetailsBtn.setText("Credit Details");
        customerSearchCreditDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerSearchCreditDetailsBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 275;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 11, 5, 10);
        jPanel4.add(customerSearchCreditDetailsBtn, gridBagConstraints);

        customerSearchOkBtn.setText("OK");
        customerSearchOkBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerSearchOkBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 81, 37, 11);
        jPanel4.add(customerSearchOkBtn, gridBagConstraints);

        customerSearchLBl.setBackground(new java.awt.Color(204, 204, 204));
        customerSearchLBl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customerSearchLBl.setText("Customer Search Result                            ");
        customerSearchLBl.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        customerSearchLBl.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 8);
        jPanel4.add(customerSearchLBl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(234, 489, 235, 490);
        customerSearchResultPanel.add(jPanel4, gridBagConstraints);

        cardPanel.add(customerSearchResultPanel, "customerSearchResult");

        installmentPaymentPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        installmentPaymentPanel.setName("paymentHistory");
        installmentPaymentPanel.setOpaque(true);
        java.awt.GridBagLayout installmentPaymentPanelLayout = new java.awt.GridBagLayout();
        installmentPaymentPanelLayout.columnWidths = new int[] {0};
        installmentPaymentPanelLayout.rowHeights = new int[] {0};
        installmentPaymentPanel.setLayout(installmentPaymentPanelLayout);

        installmentPaymentPanelSmall.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        installmentPaymentPanelSmall.setPreferredSize(new java.awt.Dimension(450, 250));

        installmetPaymentCustomerNameLbl.setText("Customer Name");

        InstallmentPaymentDateLbl.setText("Date Settlement");

        installmentPaymentSubmitBtn.setText("Submit");
        installmentPaymentSubmitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installmentPaymentSubmitBtnActionPerformed(evt);
            }
        });

        installmentPaymentLbl.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl.setText("Installment Payment");
        installmentPaymentLbl.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl.setOpaque(true);

        InstallmentPaymentCancelBtn.setText("Cancel");
        InstallmentPaymentCancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InstallmentPaymentCancelBtnActionPerformed(evt);
            }
        });

        InstallmetPaymentPaymentValue.setText("Payment Amount");

        javax.swing.GroupLayout installmentPaymentPanelSmallLayout = new javax.swing.GroupLayout(installmentPaymentPanelSmall);
        installmentPaymentPanelSmall.setLayout(installmentPaymentPanelSmallLayout);
        installmentPaymentPanelSmallLayout.setHorizontalGroup(
            installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(installmentPaymentPanelSmallLayout.createSequentialGroup()
                .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(installmentPaymentPanelSmallLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(installmentPaymentLbl))
                    .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(installmentPaymentPanelSmallLayout.createSequentialGroup()
                            .addGap(247, 247, 247)
                            .addComponent(InstallmentPaymentCancelBtn)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                            .addComponent(installmentPaymentSubmitBtn))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, installmentPaymentPanelSmallLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(installmetPaymentCustomerNameLbl)
                                .addComponent(InstallmentPaymentDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(InstallmetPaymentPaymentValue, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(47, 47, 47)
                            .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(InstallmentPaymentDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                                .addComponent(InstallmentPaymentPaymentAmountLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(InstallmentPaymentCustomerNameLblValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(47, 47, 47))
        );
        installmentPaymentPanelSmallLayout.setVerticalGroup(
            installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(installmentPaymentPanelSmallLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(installmentPaymentLbl)
                .addGap(18, 18, 18)
                .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(installmetPaymentCustomerNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InstallmentPaymentCustomerNameLblValue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InstallmentPaymentDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InstallmentPaymentDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InstallmetPaymentPaymentValue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InstallmentPaymentPaymentAmountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(installmentPaymentSubmitBtn)
                    .addComponent(InstallmentPaymentCancelBtn))
                .addGap(0, 70, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.insets = new java.awt.Insets(173, 211, 314, 282);
        installmentPaymentPanel.add(installmentPaymentPanelSmall, gridBagConstraints);

        cardPanel.add(installmentPaymentPanel, "installmentPayment");

        java.awt.GridBagLayout addCustomerLayout = new java.awt.GridBagLayout();
        addCustomerLayout.columnWidths = new int[] {0};
        addCustomerLayout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0};
        addCustomer.setLayout(addCustomerLayout);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        addCustomerAddBtn1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addCustomerAddBtn1.setText("Add");
        addCustomerAddBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerAddBtn1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 67;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 147, 13, 12);
        jPanel1.add(addCustomerAddBtn1, gridBagConstraints);

        addCustomerFullNameLbl1.setText("Full Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 0);
        jPanel1.add(addCustomerFullNameLbl1, gridBagConstraints);

        addCustomerCoopIdLbl1.setText("Coop ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        jPanel1.add(addCustomerCoopIdLbl1, gridBagConstraints);

        addCustomerAddressLbl1.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        jPanel1.add(addCustomerAddressLbl1, gridBagConstraints);

        addCustomerFullNameTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerFullNameTxt1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel1.add(addCustomerFullNameTxt1, gridBagConstraints);

        addCustomerCoopIdTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerCoopIdTxt1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel1.add(addCustomerCoopIdTxt1, gridBagConstraints);

        addCustomerAddressTxt1.setColumns(20);
        addCustomerAddressTxt1.setRows(5);
        jScrollPane5.setViewportView(addCustomerAddressTxt1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 183;
        gridBagConstraints.ipady = 73;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel1.add(jScrollPane5, gridBagConstraints);

        addCustomerNicLbl1.setText("NIC No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 0);
        jPanel1.add(addCustomerNicLbl1, gridBagConstraints);

        addCustomerTeleTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerTeleTxt1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel1.add(addCustomerTeleTxt1, gridBagConstraints);

        addCustomerTeleLbl1.setText("Telephone No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 0);
        jPanel1.add(addCustomerTeleLbl1, gridBagConstraints);

        addCustomerNicTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerNicTxt1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel1.add(addCustomerNicTxt1, gridBagConstraints);

        newCustomerLbl.setBackground(new java.awt.Color(204, 204, 204));
        newCustomerLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newCustomerLbl.setText("New Customer");
        newCustomerLbl.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        newCustomerLbl.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 13, 0, 12);
        jPanel1.add(newCustomerLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(24, 19, 27, 29);
        addCustomer.add(jPanel1, gridBagConstraints);

        cardPanel.add(addCustomer, "addCustomer");

        creditDetails.setPreferredSize(new java.awt.Dimension(148, 666));
        creditDetails.setLayout(new java.awt.GridBagLayout());

        jPanel5.setPreferredSize(new java.awt.Dimension(1126, 775));

        creditTbl.setAutoCreateRowSorter(true);
        creditTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select to Pay", "Invoice No.", "Date", "Net Amount", "Paid  Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        creditTbl.setFillsViewportHeight(true);
        jScrollPane7.setViewportView(creditTbl);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        billTotalLbl1.setText("Bill Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 23, 0, 0);
        jPanel6.add(billTotalLbl1, gridBagConstraints);

        billTxt1.setText("jXLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 55, 0, 0);
        jPanel6.add(billTxt1, gridBagConstraints);

        paidAmountTxt1.setText("jXLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 55, 0, 0);
        jPanel6.add(paidAmountTxt1, gridBagConstraints);

        paidAmountlbl1.setText("Paid Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 23, 0, 0);
        jPanel6.add(paidAmountlbl1, gridBagConstraints);

        cancelBtn1.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 55, 11, 15);
        jPanel6.add(cancelBtn1, gridBagConstraints);

        confirmBtn1.setText("Confirm");
        confirmBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmBtn1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 23, 11, 0);
        jPanel6.add(confirmBtn1, gridBagConstraints);

        creditDetailsCreditorLbl.setText("Creditor");

        creditDetailsSettleentNoLbl.setText("Settlement No.");

        creditDetailsCustomerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditDetailsCustomerComboBoxActionPerformed(evt);
            }
        });

        creditDetailsSettleentDateLbl1.setText("Settlement Date");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(creditDetailsCreditorLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(creditDetailsSettleentNoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(creditDetailsSettleentDateLbl1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(creditDetailsSettlementNoTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(creditDetailsCustomerComboBox, 0, 373, Short.MAX_VALUE)
                    .addComponent(creditDetailsSettlementDateTxt1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(creditDetailsCustomerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(creditDetailsCreditorLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(creditDetailsSettleentNoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(creditDetailsSettlementNoTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(creditDetailsSettleentDateLbl1, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(creditDetailsSettlementDateTxt1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );

        installmentPaymentLbl4.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl4.setText("Credit Details");
        installmentPaymentLbl4.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl4.setOpaque(true);

        checkCreditDetailsBtn.setText("Check");
        checkCreditDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkCreditDetailsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(67, 67, 67)
                            .addComponent(checkCreditDetailsBtn)
                            .addContainerGap())
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1106, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(installmentPaymentLbl4, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(619, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(checkCreditDetailsBtn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(installmentPaymentLbl4)
                    .addContainerGap(713, Short.MAX_VALUE)))
        );

        creditDetails.add(jPanel5, new java.awt.GridBagConstraints());

        cardPanel.add(creditDetails, "creditDetails");

        editCustomer.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        editCustomerSaveBtn2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editCustomerSaveBtn2.setText("Save");
        editCustomerSaveBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerSaveBtn2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 67;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 147, 13, 12);
        jPanel2.add(editCustomerSaveBtn2, gridBagConstraints);

        editCustomerFullNameLbl2.setText("Full Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 0);
        jPanel2.add(editCustomerFullNameLbl2, gridBagConstraints);

        editCustomerCoopIdLbl2.setText("Coop ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        jPanel2.add(editCustomerCoopIdLbl2, gridBagConstraints);

        editCustomerAddressLbl2.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        jPanel2.add(editCustomerAddressLbl2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel2.add(editCustomerFullNameTxt2, gridBagConstraints);

        editCustomerCoopIdTxt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerCoopIdTxt2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel2.add(editCustomerCoopIdTxt2, gridBagConstraints);

        editCustomerAddressTxt2.setColumns(20);
        editCustomerAddressTxt2.setRows(5);
        jScrollPane8.setViewportView(editCustomerAddressTxt2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 183;
        gridBagConstraints.ipady = 73;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel2.add(jScrollPane8, gridBagConstraints);

        editCustomerNicLbl2.setText("NIC No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 0);
        jPanel2.add(editCustomerNicLbl2, gridBagConstraints);

        editCustomerTeleTxt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerTeleTxt2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel2.add(editCustomerTeleTxt2, gridBagConstraints);

        editCustomerTeleLbl2.setText("Telephone No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 0);
        jPanel2.add(editCustomerTeleLbl2, gridBagConstraints);

        editCustomerNicTxt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerNicTxt2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 31, 0, 12);
        jPanel2.add(editCustomerNicTxt2, gridBagConstraints);

        editCustomerLbl2.setBackground(new java.awt.Color(204, 204, 204));
        editCustomerLbl2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editCustomerLbl2.setText("Edit Customer");
        editCustomerLbl2.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        editCustomerLbl2.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 13, 0, 12);
        jPanel2.add(editCustomerLbl2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(24, 19, 27, 29);
        editCustomer.add(jPanel2, gridBagConstraints);

        cardPanel.add(editCustomer, "editCustomer");

        customerDetails.setLayout(new java.awt.GridBagLayout());

        customerDetailsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Coop Id", "Customer Name", "Address", "Tele No.", "NIC No.", "Current Credit"
            }
        ));
        customerDetailsTbl.setSelectionBackground(new java.awt.Color(153, 153, 153));
        customerDetailsTbl.setShowGrid(true);
        jScrollPane6.setViewportView(customerDetailsTbl);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 697;
        gridBagConstraints.ipady = 507;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 5, 23, 5);
        customerDetails.add(jScrollPane6, gridBagConstraints);

        editCustomerBtn.setText("Edit");
        editCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerBtnActionPerformed(evt);
            }
        });

        deleteCustomerBtn.setText("Delete");
        deleteCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCustomerBtnActionPerformed(evt);
            }
        });

        customerDetailsCreditDetailsBtn.setText("Credit Details");
        customerDetailsCreditDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerDetailsCreditDetailsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jXPanel2Layout = new javax.swing.GroupLayout(jXPanel2);
        jXPanel2.setLayout(jXPanel2Layout);
        jXPanel2Layout.setHorizontalGroup(
            jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerDetailsCreditDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {customerDetailsCreditDetailsBtn, deleteCustomerBtn, editCustomerBtn});

        jXPanel2Layout.setVerticalGroup(
            jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerDetailsCreditDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 19);
        customerDetails.add(jXPanel2, gridBagConstraints);

        newCustomerLbl1.setBackground(new java.awt.Color(204, 204, 204));
        newCustomerLbl1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newCustomerLbl1.setText("Customer Details                                                                                                                                                               ");
        newCustomerLbl1.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        newCustomerLbl1.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 161;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 26);
        customerDetails.add(newCustomerLbl1, gridBagConstraints);

        cardPanel.add(customerDetails, "customerDetails");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void installmentPaymentSubmitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installmentPaymentSubmitBtnActionPerformed
        // TODO add your handling code here:
        if ((InstallmentPaymentDatePicker.getDate() != null)) {
            Utilities.showMsgBox("Payment Successful", "Confirmed", JOptionPane.PLAIN_MESSAGE);
            InstallmentPaymentCustomerNameLblValue.setText(null);
            InstallmentPaymentDatePicker.setDate(null);
            InstallmentPaymentPaymentAmountLbl.setText(null);
        } else {
            Utilities.showMsgBox("Select the date", "Error", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_installmentPaymentSubmitBtnActionPerformed

    private void InstallmentPaymentCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InstallmentPaymentCancelBtnActionPerformed
        // TODO add your handling code here:
        InstallmentPaymentCustomerNameLblValue.setText(null);
        InstallmentPaymentDatePicker.setDate(null);
        InstallmentPaymentPaymentAmountLbl.setText(null);
    }//GEN-LAST:event_InstallmentPaymentCancelBtnActionPerformed

    private void deleteCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCustomerBtnActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < getCustomerDetailsTbl().getRowCount(); i++) {
            if (customerDetailsTbl.isRowSelected(i)) {

                deleteCustomerDetails(Integer.parseInt(customerDetailsTbl.getValueAt(i, 0).toString()));
                break;
            } else {
                if (i == getCustomerDetailsTbl().getRowCount() - 1) {
                    Utilities.showMsgBox("Select a row to delete details", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

    }//GEN-LAST:event_deleteCustomerBtnActionPerformed

    private void custoemrSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_custoemrSearchBtnActionPerformed
        try {
            // TODO add your handling code here:
            searchDetails();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTextField1.setText(null);
        //  CardLayout card = (CardLayout) cardPanel.getLayout();
        //  card.show(cardPanel, "customerSearchResult");
    }//GEN-LAST:event_custoemrSearchBtnActionPerformed

    private void customerSearchBackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerSearchBackBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerSearchBackBtnActionPerformed

    private void customerDetailsCreditDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerDetailsCreditDetailsBtnActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "creditDetails");
    }//GEN-LAST:event_customerDetailsCreditDetailsBtnActionPerformed

    private void addCustomerAddBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerAddBtn1ActionPerformed
        validateName(addCustomerFullNameTxt1.getText());
        String nic = addCustomerNicTxt1.getText();
        if ((nic.length() == 10) && (nic.endsWith("v") == true)) {
            try {
                Integer.parseInt(nic.substring(0, 8));
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Enter a valid nic number", "WARNING", JOptionPane.WARNING_MESSAGE);

            }
        } else {
            Utilities.showMsgBox("Enter a valid id number", "WARNING", JOptionPane.WARNING_MESSAGE);

        }

        if ((addCustomerTeleTxt1.getText().length() == 10)) {
            try {
                Integer.parseInt(nic.substring(0, 8));
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Enter a valid telephone number", "WARNING", JOptionPane.WARNING_MESSAGE);

            }
        } else {
            Utilities.showMsgBox("Enter a valid telephone number", "WARNING", JOptionPane.WARNING_MESSAGE);

        }

        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerDetails");
        try {
            addCustomer();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addCustomerAddBtn1ActionPerformed

    private void addCustomerCoopIdTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerCoopIdTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCustomerCoopIdTxt1ActionPerformed

    private void addCustomerTeleTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerTeleTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCustomerTeleTxt1ActionPerformed

    private void addCustomerNicTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerNicTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCustomerNicTxt1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void customerSearchCreditDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerSearchCreditDetailsBtnActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "creditDetails");
    }//GEN-LAST:event_customerSearchCreditDetailsBtnActionPerformed

    private void customerSearchOkBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerSearchOkBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerSearchOkBtnActionPerformed

    private void editCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCustomerBtnActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < getCustomerDetailsTbl().getRowCount(); i++) {
            if (customerDetailsTbl.isRowSelected(i)) {

                try {
                    System.out.println("row" + i);
                    editCustomerDetails(Integer.parseInt(customerDetailsTbl.getValueAt(i, 0).toString()));
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            } else {
                if (i == getCustomerDetailsTbl().getRowCount() - 1) {
                    Utilities.showMsgBox("Select a row to edit details", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_editCustomerBtnActionPerformed

    private void customerSearchSearchLblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerSearchSearchLblActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerSearch");

    }//GEN-LAST:event_customerSearchSearchLblActionPerformed

    private void manageCustomerCustomerDetailsLblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageCustomerCustomerDetailsLblActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerDetails");
    }//GEN-LAST:event_manageCustomerCustomerDetailsLblActionPerformed

    private void manageCustomerAddCustomerLblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageCustomerAddCustomerLblActionPerformed
        // TODO add your handling code here:
        setCoopId();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "addCustomer");
    }//GEN-LAST:event_manageCustomerAddCustomerLblActionPerformed

    private void creditManagementCreditDetailsLblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditManagementCreditDetailsLblActionPerformed
        // TODO add your handling code here:

        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "creditDetails");
        try {

            loadDetails();

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_creditManagementCreditDetailsLblActionPerformed

    private void editCustomerSaveBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCustomerSaveBtn2ActionPerformed
        try {
            validateName(editCustomerFullNameTxt2.getText());
            String nic = editCustomerNicTxt2.getText();
            if ((nic.length() == 10) && (nic.endsWith("v") == true)) {
                try {
                    Integer.parseInt(nic.substring(0, 8));
                } catch (NumberFormatException ex) {
                    Utilities.showMsgBox("Enter a valid nic number", "WARNING", JOptionPane.WARNING_MESSAGE);

                }
            } else {
                Utilities.showMsgBox("Enter a valid id number", "WARNING", JOptionPane.WARNING_MESSAGE);

            }

            if ((editCustomerTeleTxt2.getText().length() == 10)) {
                try {
                    Integer.parseInt(nic.substring(0, 8));
                } catch (NumberFormatException ex) {
                    Utilities.showMsgBox("Enter a valid telephone number", "WARNING", JOptionPane.WARNING_MESSAGE);

                }
            } else {
                Utilities.showMsgBox("Enter a valid telephone number", "WARNING", JOptionPane.WARNING_MESSAGE);

            }
            // TODO add your handling code here:
            setEditDetails();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_editCustomerSaveBtn2ActionPerformed

    private void editCustomerCoopIdTxt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCustomerCoopIdTxt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editCustomerCoopIdTxt2ActionPerformed

    private void editCustomerTeleTxt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCustomerTeleTxt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editCustomerTeleTxt2ActionPerformed

    private void editCustomerNicTxt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCustomerNicTxt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editCustomerNicTxt2ActionPerformed

    private void creditDetailsCustomerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditDetailsCustomerComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_creditDetailsCustomerComboBoxActionPerformed

    private void checkCreditDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkCreditDetailsBtnActionPerformed
        // TODO add your handling code here:

        if (creditDetailsCustomerComboBox.getSelectedIndex() > 0) {
            // Object ob = CreditDetailsCustomerComboBoxModel.getSelectedItem();
            // CreditDetailsCustomerComboBoxModel.removeAllElements();
            // CreditDetailsCustomerComboBoxModel.addElement(ob);

            try {
                loadCreditDetails();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_checkCreditDetailsBtnActionPerformed

    private void addCustomerFullNameTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerFullNameTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCustomerFullNameTxt1ActionPerformed

    private void confirmBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtn1ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "installmentPayment");
        InstallmentPaymentCustomerNameLblValue.setText(creditDetailsCustomerComboBox.getSelectedItem().toString());
        double billValue = 0;
        double paidAmount = 0;
        double payment = 0;
        for (int i = 0; i < creditTbl.getRowCount(); i++) {
            if (creditTbl.getValueAt(i, 0).equals(true)) {
                billValue = Double.parseDouble(creditTbl.getValueAt(i, 3).toString());
                paidAmount = Double.parseDouble(creditTbl.getValueAt(i, 4).toString());
                payment += billValue - paidAmount;
            }
        }
        InstallmentPaymentPaymentAmountLbl.setText(payment + "");


    }//GEN-LAST:event_confirmBtn1ActionPerformed
    public boolean validateName(String txt) {
        logger.debug("validateName invoked");

        String regx = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();

    }

    public void validateNumbers(String x) {
        try {
            int l = Integer.parseInt(x);
        } catch (NumberFormatException ex) {
            Utilities.showMsgBox("Enter a valid id", "WARNING", JOptionPane.WARNING_MESSAGE);

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FinalCredit().setVisible(true);
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton InstallmentPaymentCancelBtn;
    private javax.swing.JLabel InstallmentPaymentCustomerNameLblValue;
    private javax.swing.JLabel InstallmentPaymentDateLbl;
    private org.jdesktop.swingx.JXDatePicker InstallmentPaymentDatePicker;
    private javax.swing.JLabel InstallmentPaymentPaymentAmountLbl;
    private javax.swing.JLabel InstallmetPaymentPaymentValue;
    private javax.swing.JPanel addCustomer;
    private org.jdesktop.swingx.JXButton addCustomerAddBtn1;
    private javax.swing.JLabel addCustomerAddressLbl1;
    private javax.swing.JTextArea addCustomerAddressTxt1;
    private javax.swing.JLabel addCustomerCoopIdLbl1;
    private javax.swing.JTextField addCustomerCoopIdTxt1;
    private javax.swing.JLabel addCustomerFullNameLbl1;
    private javax.swing.JTextField addCustomerFullNameTxt1;
    private javax.swing.JLabel addCustomerNicLbl1;
    private javax.swing.JTextField addCustomerNicTxt1;
    private javax.swing.JLabel addCustomerTeleLbl1;
    private javax.swing.JTextField addCustomerTeleTxt1;
    private org.jdesktop.swingx.painter.AlphaPainter alphaPainter1;
    private org.jdesktop.swingx.JXLabel billTotalLbl1;
    private org.jdesktop.swingx.JXLabel billTxt1;
    private org.jdesktop.swingx.painter.BusyPainter busyPainter1;
    private javax.swing.JButton cancelBtn1;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JButton checkCreditDetailsBtn;
    private org.jdesktop.swingx.painter.CheckerboardPainter checkerboardPainter1;
    private javax.swing.JButton confirmBtn1;
    private javax.swing.JPanel creditDetails;
    private javax.swing.JLabel creditDetailsCreditorLbl;
    private javax.swing.JComboBox creditDetailsCustomerComboBox;
    private javax.swing.JLabel creditDetailsSettleentDateLbl1;
    private javax.swing.JLabel creditDetailsSettleentNoLbl;
    private javax.swing.JLabel creditDetailsSettlementDateTxt1;
    private javax.swing.JLabel creditDetailsSettlementNoTxt;
    private javax.swing.JButton creditManagementCreditDetailsLbl;
    private org.jdesktop.swingx.JXTaskPane creditManagementTaskPane;
    private javax.swing.JTable creditTbl;
    private javax.swing.JButton custoemrSearchBtn;
    private javax.swing.JPanel customerDetails;
    private org.jdesktop.swingx.JXButton customerDetailsCreditDetailsBtn;
    private org.jdesktop.swingx.JXTable customerDetailsTbl;
    private javax.swing.ButtonGroup customerGender;
    private javax.swing.JPanel customerSearch;
    private javax.swing.JButton customerSearchBackBtn;
    private javax.swing.JLabel customerSearchCoopIdTxt;
    private javax.swing.JButton customerSearchCreditDetailsBtn;
    private javax.swing.JLabel customerSearchCustomerNameLbl;
    private javax.swing.JLabel customerSearchCustomerNameTxt;
    private javax.swing.JLabel customerSearchLBl;
    private javax.swing.JButton customerSearchOkBtn;
    private javax.swing.JPanel customerSearchResultPanel;
    private javax.swing.JButton customerSearchSearchLbl;
    private org.jdesktop.swingx.JXTaskPane customerSearchTaskPane;
    private javax.swing.JLabel customerSearchcoopIdLbl;
    private org.jdesktop.swingx.JXButton deleteCustomerBtn;
    private javax.swing.JPanel editCustomer;
    private javax.swing.JLabel editCustomerAddressLbl2;
    private javax.swing.JTextArea editCustomerAddressTxt2;
    private org.jdesktop.swingx.JXButton editCustomerBtn;
    private javax.swing.JLabel editCustomerCoopIdLbl2;
    private javax.swing.JTextField editCustomerCoopIdTxt2;
    private javax.swing.JLabel editCustomerFullNameLbl2;
    private javax.swing.JTextField editCustomerFullNameTxt2;
    private javax.swing.JLabel editCustomerLbl2;
    private javax.swing.JLabel editCustomerNicLbl2;
    private javax.swing.JTextField editCustomerNicTxt2;
    private org.jdesktop.swingx.JXButton editCustomerSaveBtn2;
    private javax.swing.JLabel editCustomerTeleLbl2;
    private javax.swing.JTextField editCustomerTeleTxt2;
    private org.jdesktop.swingx.painter.GlossPainter glossPainter1;
    private org.jdesktop.swingx.painter.GlossPainter glossPainter2;
    private org.jdesktop.swingx.painter.GlossPainter glossPainter3;
    private javax.swing.JLabel installmentPaymentLbl;
    private javax.swing.JLabel installmentPaymentLbl2;
    private javax.swing.JLabel installmentPaymentLbl4;
    private javax.swing.JPanel installmentPaymentPanel;
    private javax.swing.JPanel installmentPaymentPanelSmall;
    private javax.swing.JButton installmentPaymentSubmitBtn;
    private javax.swing.JLabel installmetPaymentCustomerNameLbl;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextField jTextField1;
    private org.jdesktop.swingx.JXPanel jXPanel2;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    private org.jdesktop.swingx.JXTitledPanel jXTitledPanel3;
    private org.jdesktop.swingx.JXTitledPanel jXTitledPanel4;
    private javax.swing.JButton manageCustomerAddCustomerLbl;
    private javax.swing.JButton manageCustomerCustomerDetailsLbl;
    private org.jdesktop.swingx.JXTaskPane manageCustomerTaskPane;
    private javax.swing.JLabel newCustomerLbl;
    private javax.swing.JLabel newCustomerLbl1;
    private org.jdesktop.swingx.JXLabel paidAmountTxt1;
    private org.jdesktop.swingx.JXLabel paidAmountlbl1;
    private org.jdesktop.swingx.painter.RectanglePainter rectanglePainter1;
    private org.jdesktop.swingx.painter.ShapePainter shapePainter1;
    private javax.swing.ButtonGroup taskPaneSearch;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the customerDetailsTbl
     */
    public org.jdesktop.swingx.JXTable getCustomerDetailsTbl() {
        return customerDetailsTbl;
    }

    /**
     * @param customerDetailsTbl the customerDetailsTbl to set
     */
    public void setCustomerDetailsTbl(org.jdesktop.swingx.JXTable customerDetailsTbl) {
        this.customerDetailsTbl = customerDetailsTbl;
    }

}
