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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import util.AddressFilter;
import util.NICFilter;
import util.StringFilter;
import util.TelePhoneNumberFilter;
import util.Utilities;

/**
 *
 * @author HP
 */
public class FinalCredit extends javax.swing.JInternalFrame {

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
    //private HashMap<Integer, CreditCustomer> availableCustomers;
    DefaultTableModel CustomerCreditDetailsTableModel;
    private int selectedIndex;
    DefaultTableModel CreditDetailsTableModel;
    DefaultComboBoxModel CreditDetailsCustomerComboBoxModel;
    CustomerCreditController l = new CustomerCreditController();

    DBHandler t;
    ResultSet rs;

    // CreditCustomer creditCustomer;
    // int coopIdNum = 0;
    /**
     * Creates new form finalCredit
     */
    public FinalCredit() {
        try {
            initComponents();
            chBoxes = new ArrayList<>();
            // cardPanel.setPreferredSize(new Dimension(800, 800));
            // setSize(new Dimension(1500, 1500));

            this.CustomerCreditDetailsTableModel = (DefaultTableModel) customerDetailsTbl.getModel();
            this.CreditDetailsTableModel = (DefaultTableModel) creditTbl.getModel();
            

            this.CreditDetailsCustomerComboBoxModel = (DefaultComboBoxModel) creditDetailsCustomerComboBox.getModel();
            customerDetailsTbl.setModel(DbUtils.resultSetToTableModel(CustomerCreditController.loadDetails()));
            customerDetailsTbl.setSelectionMode(0);
            loadDetails();
       

            /**
             *
             * @throws SQLException
             */
            //jnj - adding documnet filters============================================
            ((PlainDocument) addCustomerTeleTxt1.getDocument()).setDocumentFilter(new TelePhoneNumberFilter());
            ((PlainDocument) addCustomerNicTxt1.getDocument()).setDocumentFilter(new NICFilter());

            ((PlainDocument) editCustomerTeleTxt2.getDocument()).setDocumentFilter(new TelePhoneNumberFilter());
            ((PlainDocument) editCustomerNicTxt2.getDocument()).setDocumentFilter(new NICFilter());
            
              ((PlainDocument) addCustomerFullNameTxt1.getDocument()).setDocumentFilter(new StringFilter());
                ((PlainDocument) customerSearchTxt.getDocument()).setDocumentFilter(new StringFilter());
                  ((PlainDocument) addCustomerAddressTxt1.getDocument()).setDocumentFilter(new AddressFilter());
       ((PlainDocument) editCustomerAddressTxt2.getDocument()).setDocumentFilter(new AddressFilter());
       
            

            //=========================================================================
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    private void loadDetails() {

        try {
            logger.debug("loadDetails method invoked");
            CreditDetailsCustomerComboBoxModel.removeAllElements();
            ArrayList<CreditCustomer> customerDetails = CustomerCreditController.loadCustomers();

            for (CreditCustomer customer : customerDetails) {
                // availableCustomers.put(customer.getCustomerId(), customer);
                CreditDetailsCustomerComboBoxModel.addElement(customer.getCustomerName());
            }
            CreditDetailsCustomerComboBoxModel.setSelectedItem(null);
            //   CoopCreditPaymentController.getCoopCreditPaymentDetails(availableCustomers.CreditDetailsCustomerComboBoxModel.getSelectedItem().toString())
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    //helper methods
    public void chngTblModel() {

        try {
            logger.debug("chngTblMdl method invoked");
            customerDetailsTbl.setModel(DbUtils.resultSetToTableModel(CustomerCreditController.loadDetails()));
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    public void selectPayment() {
        double value = 0;
        for (int i = 0; i < CreditDetailsTableModel.getRowCount(); i++) {
            if (CreditDetailsTableModel.getValueAt(i, 0).equals(true)) {
                double paymentValue = Double.parseDouble(CreditDetailsTableModel.getValueAt(i, 3).toString()) - Double.parseDouble(CreditDetailsTableModel.getValueAt(i, 4).toString());
                value += paymentValue;
                paymentAmountCrdtTblTxt.setText(value + "");
            }

        }
    }

    public void editCustomer() {
        try {
            logger.debug("editCustomer method invoked");
            //All edit details are varified before this method
            int customerId = Integer.parseInt(editCustomerCoopIdTxt2.getText());

            CreditCustomer creditCustomer = new CreditCustomer(
                    customerId,
                    editCustomerFullNameTxt2.getText(),
                    editCustomerAddressTxt2.getText(),
                    editCustomerTeleTxt2.getText(),
                    editCustomerNicTxt2.getText().toUpperCase(),
                    0.0
            );

            boolean result = CustomerCreditController.EditCustomer(creditCustomer);
            if (result) {
                Utilities.showMsgBox("Changes saved", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                logger.error("Changes not saved");
            }
            chngTblModel();

            //CustomerCreditDetailsTableModel.removeRow(Integer.parseInt(editCustomerCoopIdTxt2.getText())-1);
            //  CustomerCreditDetailsTableModel.insertRow(Integer.parseInt(editCustomerCoopIdTxt2.getText())-1, ob);
            //CardLayout card = (CardLayout) cardPanel.getLayout();
            //card.show(cardPanel, "customerDetails");
            //      CreditCustomer creditCustomer = new CreditCustomer(customerId,ob[1].toString(),ob[2].toString(),customerTele,ob[3].toString(),10000);
            //CustomerCreditController.EditCustomer(creditCustomer, Integer.parseInt(ob[4].toString()));
            editCustomerCoopIdTxt2.setText("");
            editCustomerFullNameTxt2.setText("");
            editCustomerAddressTxt2.setText("");
            editCustomerNicTxt2.setText("");
            editCustomerTeleTxt2.setText("");
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    public void addCustomer() {
        try {
            logger.debug("addCustomer method invoked");
            //All add details are varified before this method
            int customerId = Integer.parseInt(addCustomerCoopIdTxt1.getText());

            CreditCustomer creditCustomer = new CreditCustomer(
                    customerId,
                    addCustomerFullNameTxt1.getText(),
                    addCustomerAddressTxt1.getText(),
                    addCustomerTeleTxt1.getText(),
                    addCustomerNicTxt1.getText().toUpperCase(),
                    0.0
            );

            CustomerCreditController.AddCustomer(creditCustomer);

            chngTblModel();

            //CustomerCreditDetailsTableModel.removeRow(Integer.parseInt(editCustomerCoopIdTxt2.getText())-1);
            //  CustomerCreditDetailsTableModel.insertRow(Integer.parseInt(editCustomerCoopIdTxt2.getText())-1, ob);
            //  CardLayout card = (CardLayout) cardPanel.getLayout();
            //card.show(cardPanel, "customerDetails");
            // CustomerCreditController.setEditDetails(creditCustomer, Integer.parseInt(ob[4].toString()));
            addCustomerCoopIdTxt1.setText("");
            addCustomerFullNameTxt1.setText("");
            addCustomerAddressTxt1.setText("");
            addCustomerNicTxt1.setText("");
            addCustomerTeleTxt1.setText("");
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    public void deleteCustomer(int i) {
        logger.debug("deleteCustomer method invoked");
        try {
            CustomerCreditController.deleteDeatils(i);
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        chngTblModel();
    }

    public void setCoopId() {
        logger.debug("setCoopId method invoked");
        try {
            int coopIdNum = CustomerCreditController.getLastCreditCustomerId();

            logger.info("last customer ID : " + coopIdNum);
            if (coopIdNum > 0) {
                addCustomerCoopIdTxt1.setText(String.valueOf(coopIdNum + 1));
            } else {
                addCustomerCoopIdTxt1.setText(String.valueOf(1));
            }

        } catch (SQLException ex) {
            logger.error("SQL Error : " + ex);
            return;
        }
        // addCustomerCoopIdTxt1.setText((Utilities.convertKeyToString(coopIdNum + 1, DatabaseInterface.CREDIT_CUSTOMER)));

    }

    public void showCustomerEditDetails(int i) {
        logger.debug("showCustomerDetails method invoked");
   try {
            CreditCustomer creditCustomer = CustomerCreditController.getCustomer(i);

            if (creditCustomer != null) {

                editCustomerFullNameTxt2.setText(creditCustomer.getCustomerName());
                editCustomerAddressTxt2.setText(creditCustomer.getCustomerAddress());
                editCustomerTeleTxt2.setText(creditCustomer.getTelephone());
                editCustomerNicTxt2.setText(creditCustomer.getNic());
                editCustomerCoopIdTxt2.setText(String.valueOf(creditCustomer.getCustomerId()));

                CardLayout card = (CardLayout) cardPanel.getLayout();
                card.show(cardPanel, "editCustomer");
            } else {
                Utilities.showMsgBox("No such customer found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException ex) {
            logger.error("SQL error : " + ex.getMessage());
            return;
        }

    }

    private void cleanCreditTbl() {
        creditDetailsCustomerComboBox.removeAllItems();
        creditDetailsSettlementNoTxt.setText("");
        CreditDetailsTableModel.setNumRows(0);
        billTxt1.setText("");
        paidAmountTxt1.setText("");
        paymentAmountCrdtTblTxt.setText("");
    }

    public void searchDetails() {
        logger.debug("searchDetails method invoked");
       

        try {

            String s = customerSearchTxt.getText();
            if(s==null){
                   Utilities.ShowErrorMsg((Component) this, (String) "Please enter a name first");
     
            }

            CreditCustomer ob = CustomerCreditController.searchDetails(s);
            if (ob != null) {
                customerSearchCustomerNameTxt.setText(ob.getCustomerName());
                customerSearchCoopIdTxt.setText(Integer.toString(CustomerCreditController.searchDetails(s).getCustomerId()));
                CardLayout card = (CardLayout) cardPanel.getLayout();
                card.show(cardPanel, "customerSearchResult");
                CreditDetailsCustomerComboBoxModel.removeAllElements();
                ArrayList<CreditCustomer> customerDetails = CustomerCreditController.loadCustomers();

                for (CreditCustomer customer : customerDetails) {
                    if (customer.getCustomerName().toLowerCase().equals(ob.getCustomerName().toLowerCase())) {
                        CreditDetailsCustomerComboBoxModel.addElement(customer.getCustomerName());
                        //creditDetailsSettlementNoTxt.setText(customer.getCustomerId()+"");
                        loadCreditDetails();
                        break;
                    }
                }

            } else {
                Utilities.showMsgBox("Search name not found", "Error", JOptionPane.WARNING_MESSAGE);
                return;

            }
        } catch (SQLException ex) {
            logger.debug("SQLException = " + ex.getMessage());
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);

            return;
        }

    }

    public void loadCreditDetails() {
        logger.debug("loadCreditDetails method invoked");
        paymentAmountCrdtTblTxt.setText("");
        try {
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
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        customerSearchTxt = new javax.swing.JTextField();
        installmentPaymentLbl2 = new javax.swing.JLabel();
        customerSearchResultPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        customerSearchCustomerNameLbl = new javax.swing.JLabel();
        customerSearchCoopIdTxt = new javax.swing.JLabel();
        customerSearchcoopIdLbl = new javax.swing.JLabel();
        customerSearchCustomerNameTxt = new javax.swing.JLabel();
        customerSearchCreditDetailsBtn = new javax.swing.JButton();
        customerSearchLBl = new javax.swing.JLabel();
        installmentPaymentPanel = new javax.swing.JPanel();
        installmentPaymentPanelSmall = new javax.swing.JPanel();
        installmetPaymentCustomerNameLbl = new javax.swing.JLabel();
        installmentPaymentSubmitBtn = new javax.swing.JButton();
        installmentPaymentLbl = new javax.swing.JLabel();
        InstallmentPaymentCancelBtn = new javax.swing.JButton();
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
        addCustomerBackBtn2 = new org.jdesktop.swingx.JXButton();
        creditDetails = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        creditTbl = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        billTotalLbl1 = new org.jdesktop.swingx.JXLabel();
        billTxt1 = new org.jdesktop.swingx.JXLabel();
        paidAmountlbl1 = new org.jdesktop.swingx.JXLabel();
        paidAmountTxt1 = new org.jdesktop.swingx.JXLabel();
        jPanel3 = new javax.swing.JPanel();
        checkAmountButton1 = new javax.swing.JButton();
        paymentAmountCrdtTblTxt = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        creditDetailsCreditorLbl = new javax.swing.JLabel();
        creditDetailsSettleentNoLbl = new javax.swing.JLabel();
        creditDetailsSettlementNoTxt = new javax.swing.JLabel();
        creditDetailsCustomerComboBox = new javax.swing.JComboBox();
        checkCreditDetailsBtn = new javax.swing.JButton();
        installmentPaymentLbl4 = new javax.swing.JLabel();
        creditTblBackBtn = new javax.swing.JButton();
        confirmBtn1 = new javax.swing.JButton();
        cancelBtn1 = new javax.swing.JButton();
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
        editCustomerBackBtn3 = new org.jdesktop.swingx.JXButton();
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

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jXTaskPaneContainer1.setBackground(new java.awt.Color(102, 102, 102));
        jXTaskPaneContainer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jXTaskPaneContainer1.setVerifyInputWhenFocusTarget(false);

        customerSearchTaskPane.setBackground(new java.awt.Color(204, 204, 204));
        customerSearchTaskPane.setSpecial(true);
        customerSearchTaskPane.setTitle("Customer Search");
        customerSearchTaskPane.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        customerSearchSearchLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
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

        manageCustomerCustomerDetailsLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        manageCustomerCustomerDetailsLbl.setText("Customer Details");
        manageCustomerCustomerDetailsLbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageCustomerCustomerDetailsLblActionPerformed(evt);
            }
        });
        manageCustomerTaskPane.getContentPane().add(manageCustomerCustomerDetailsLbl);

        manageCustomerAddCustomerLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
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

        creditManagementCreditDetailsLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        creditManagementCreditDetailsLbl.setText("Credit Details");
        creditManagementCreditDetailsLbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditManagementCreditDetailsLblActionPerformed(evt);
            }
        });
        creditManagementTaskPane.getContentPane().add(creditManagementCreditDetailsLbl);

        jXTaskPaneContainer1.add(creditManagementTaskPane);

        cardPanel.setPreferredSize(new java.awt.Dimension(1407, 800));
        cardPanel.setLayout(new java.awt.CardLayout());

        customerSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        customerSearch.setName("paymentHistory");

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel54.setFont(customerSearchcoopIdLbl.getFont());
        jLabel54.setText("Customer Name");

        custoemrSearchBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        custoemrSearchBtn.setText("Search");
        custoemrSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                custoemrSearchBtnActionPerformed(evt);
            }
        });

        customerSearchTxt.setFont(customerSearchcoopIdLbl.getFont());
        customerSearchTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerSearchTxtActionPerformed(evt);
            }
        });

        installmentPaymentLbl2.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl2.setText("Customer Search                            ");
        installmentPaymentLbl2.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl2.setOpaque(true);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(custoemrSearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(28, 28, 28)
                            .addComponent(customerSearchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(installmentPaymentLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(installmentPaymentLbl2)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerSearchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54))
                .addGap(18, 18, 18)
                .addComponent(custoemrSearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout customerSearchLayout = new javax.swing.GroupLayout(customerSearch);
        customerSearch.setLayout(customerSearchLayout);
        customerSearchLayout.setHorizontalGroup(
            customerSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerSearchLayout.createSequentialGroup()
                .addGap(329, 329, 329)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(282, Short.MAX_VALUE))
        );
        customerSearchLayout.setVerticalGroup(
            customerSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerSearchLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(400, 400, 400))
        );

        cardPanel.add(customerSearch, "customerSearch");

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        customerSearchCustomerNameLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        customerSearchCustomerNameLbl.setText("Customer Name");

        customerSearchCoopIdTxt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customerSearchCoopIdTxt.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        customerSearchcoopIdLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        customerSearchcoopIdLbl.setText("Coop Id");

        customerSearchCustomerNameTxt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customerSearchCustomerNameTxt.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        customerSearchCreditDetailsBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customerSearchCreditDetailsBtn.setText("Credit Details");
        customerSearchCreditDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerSearchCreditDetailsBtnActionPerformed(evt);
            }
        });

        customerSearchLBl.setBackground(new java.awt.Color(204, 204, 204));
        customerSearchLBl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customerSearchLBl.setText("Customer Search Result                            ");
        customerSearchLBl.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        customerSearchLBl.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(customerSearchLBl, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customerSearchcoopIdLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerSearchCustomerNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(customerSearchCustomerNameTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                            .addComponent(customerSearchCoopIdTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29)
                        .addComponent(customerSearchCreditDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(customerSearchLBl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(customerSearchCustomerNameLbl))
                            .addComponent(customerSearchCustomerNameTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customerSearchcoopIdLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerSearchCoopIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(customerSearchCreditDetailsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(30, 30, 30))))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {customerSearchCoopIdTxt, customerSearchCustomerNameLbl, customerSearchCustomerNameTxt, customerSearchcoopIdLbl});

        javax.swing.GroupLayout customerSearchResultPanelLayout = new javax.swing.GroupLayout(customerSearchResultPanel);
        customerSearchResultPanel.setLayout(customerSearchResultPanelLayout);
        customerSearchResultPanelLayout.setHorizontalGroup(
            customerSearchResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerSearchResultPanelLayout.createSequentialGroup()
                .addContainerGap(230, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(224, 224, 224))
        );
        customerSearchResultPanelLayout.setVerticalGroup(
            customerSearchResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerSearchResultPanelLayout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(170, Short.MAX_VALUE))
        );

        cardPanel.add(customerSearchResultPanel, "customerSearchResult");

        installmentPaymentPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        installmentPaymentPanel.setName("paymentHistory");
        installmentPaymentPanel.setOpaque(true);

        installmentPaymentPanelSmall.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        installmentPaymentPanelSmall.setPreferredSize(new java.awt.Dimension(450, 250));

        installmetPaymentCustomerNameLbl.setFont(customerSearchCustomerNameLbl.getFont());
        installmetPaymentCustomerNameLbl.setText("Customer Name");

        installmentPaymentSubmitBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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

        InstallmentPaymentCancelBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        InstallmentPaymentCancelBtn.setText("Cancel");
        InstallmentPaymentCancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InstallmentPaymentCancelBtnActionPerformed(evt);
            }
        });

        InstallmentPaymentCustomerNameLblValue.setFont(customerSearchCustomerNameLbl.getFont());

        InstallmetPaymentPaymentValue.setFont(customerSearchCustomerNameLbl.getFont());
        InstallmetPaymentPaymentValue.setText("Payment Amount");

        InstallmentPaymentPaymentAmountLbl.setFont(customerSearchCustomerNameLbl.getFont());

        javax.swing.GroupLayout installmentPaymentPanelSmallLayout = new javax.swing.GroupLayout(installmentPaymentPanelSmall);
        installmentPaymentPanelSmall.setLayout(installmentPaymentPanelSmallLayout);
        installmentPaymentPanelSmallLayout.setHorizontalGroup(
            installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(installmentPaymentPanelSmallLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(installmentPaymentPanelSmallLayout.createSequentialGroup()
                        .addComponent(InstallmentPaymentCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(207, 207, 207)
                        .addComponent(installmentPaymentSubmitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(installmentPaymentLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(installmentPaymentPanelSmallLayout.createSequentialGroup()
                        .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(installmetPaymentCustomerNameLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(InstallmetPaymentPaymentValue, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(InstallmentPaymentPaymentAmountLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(InstallmentPaymentCustomerNameLblValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(20, 20, 20))
        );
        installmentPaymentPanelSmallLayout.setVerticalGroup(
            installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(installmentPaymentPanelSmallLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(installmentPaymentLbl)
                .addGap(20, 20, 20)
                .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(installmetPaymentCustomerNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InstallmentPaymentCustomerNameLblValue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InstallmetPaymentPaymentValue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InstallmentPaymentPaymentAmountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(installmentPaymentPanelSmallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(installmentPaymentSubmitBtn)
                    .addComponent(InstallmentPaymentCancelBtn))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        installmentPaymentPanelSmallLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {InstallmentPaymentCustomerNameLblValue, InstallmentPaymentPaymentAmountLbl, InstallmetPaymentPaymentValue, installmetPaymentCustomerNameLbl});

        javax.swing.GroupLayout installmentPaymentPanelLayout = new javax.swing.GroupLayout(installmentPaymentPanel);
        installmentPaymentPanel.setLayout(installmentPaymentPanelLayout);
        installmentPaymentPanelLayout.setHorizontalGroup(
            installmentPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(installmentPaymentPanelLayout.createSequentialGroup()
                .addGap(278, 278, 278)
                .addComponent(installmentPaymentPanelSmall, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(282, Short.MAX_VALUE))
        );
        installmentPaymentPanelLayout.setVerticalGroup(
            installmentPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(installmentPaymentPanelLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(installmentPaymentPanelSmall, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(134, Short.MAX_VALUE))
        );

        cardPanel.add(installmentPaymentPanel, "installmentPayment");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        addCustomerAddBtn1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addCustomerAddBtn1.setText("Add");
        addCustomerAddBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addCustomerAddBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerAddBtn1ActionPerformed(evt);
            }
        });

        addCustomerFullNameLbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCustomerFullNameLbl1.setText("Full Name");

        addCustomerCoopIdLbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCustomerCoopIdLbl1.setText("Coop ID");

        addCustomerAddressLbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCustomerAddressLbl1.setText("Address");

        addCustomerFullNameTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCustomerFullNameTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerFullNameTxt1ActionPerformed(evt);
            }
        });

        addCustomerCoopIdTxt1.setEditable(false);
        addCustomerCoopIdTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCustomerCoopIdTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerCoopIdTxt1ActionPerformed(evt);
            }
        });

        addCustomerAddressTxt1.setColumns(20);
        addCustomerAddressTxt1.setRows(5);
        jScrollPane5.setViewportView(addCustomerAddressTxt1);

        addCustomerNicLbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCustomerNicLbl1.setText("NIC No");

        addCustomerTeleTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCustomerTeleTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerTeleTxt1ActionPerformed(evt);
            }
        });

        addCustomerTeleLbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCustomerTeleLbl1.setText("Telephone No");

        addCustomerNicTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCustomerNicTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerNicTxt1ActionPerformed(evt);
            }
        });

        newCustomerLbl.setBackground(new java.awt.Color(204, 204, 204));
        newCustomerLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newCustomerLbl.setText("New Customer");
        newCustomerLbl.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        newCustomerLbl.setOpaque(true);

        addCustomerBackBtn2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addCustomerBackBtn2.setText("Back");
        addCustomerBackBtn2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addCustomerBackBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerBackBtn2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(newCustomerLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addCustomerFullNameLbl1)
                            .addComponent(addCustomerCoopIdLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addCustomerNicLbl1)
                            .addComponent(addCustomerAddressLbl1)
                            .addComponent(addCustomerTeleLbl1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addCustomerFullNameTxt1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addCustomerCoopIdTxt1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addCustomerNicTxt1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addCustomerTeleTxt1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addCustomerBackBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addCustomerAddBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addCustomerCoopIdTxt1, addCustomerFullNameTxt1, addCustomerNicTxt1, addCustomerTeleTxt1, jScrollPane5});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addCustomerAddressLbl1, addCustomerCoopIdLbl1, addCustomerFullNameLbl1, addCustomerNicLbl1, addCustomerTeleLbl1});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(newCustomerLbl)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addCustomerFullNameLbl1)
                    .addComponent(addCustomerFullNameTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCustomerCoopIdTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCustomerCoopIdLbl1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCustomerNicTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCustomerNicLbl1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addCustomerAddressLbl1)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCustomerTeleTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCustomerTeleLbl1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCustomerAddBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCustomerBackBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addCustomerAddressLbl1, addCustomerCoopIdLbl1, addCustomerCoopIdTxt1, addCustomerNicLbl1, addCustomerNicTxt1, addCustomerTeleLbl1, addCustomerTeleTxt1});

        javax.swing.GroupLayout addCustomerLayout = new javax.swing.GroupLayout(addCustomer);
        addCustomer.setLayout(addCustomerLayout);
        addCustomerLayout.setHorizontalGroup(
            addCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addCustomerLayout.createSequentialGroup()
                .addContainerGap(286, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(280, 280, 280))
        );
        addCustomerLayout.setVerticalGroup(
            addCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addCustomerLayout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        cardPanel.add(addCustomer, "addCustomer");

        creditDetails.setPreferredSize(new java.awt.Dimension(148, 666));

        jPanel5.setPreferredSize(new java.awt.Dimension(1126, 775));

        creditTbl.setAutoCreateRowSorter(true);
        creditTbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        creditTbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
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
        if (creditTbl.getColumnModel().getColumnCount() > 0) {
            creditTbl.getColumnModel().getColumn(0).setHeaderValue("Select to Pay");
            creditTbl.getColumnModel().getColumn(1).setHeaderValue("Invoice No.");
            creditTbl.getColumnModel().getColumn(2).setHeaderValue("Date");
            creditTbl.getColumnModel().getColumn(3).setHeaderValue("Net Amount");
            creditTbl.getColumnModel().getColumn(4).setHeaderValue("Paid  Amount");
        }

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        billTotalLbl1.setText("Bill Total");
        billTotalLbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        billTxt1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        billTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        paidAmountlbl1.setText("Paid Amount");
        paidAmountlbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        paidAmountTxt1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paidAmountTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        checkAmountButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        checkAmountButton1.setText("CHECK AMOUNT");
        checkAmountButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAmountButton1ActionPerformed(evt);
            }
        });

        paymentAmountCrdtTblTxt.setEditable(false);
        paymentAmountCrdtTblTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        paymentAmountCrdtTblTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paymentAmountCrdtTblTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentAmountCrdtTblTxtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkAmountButton1)
                .addGap(18, 18, 18)
                .addComponent(paymentAmountCrdtTblTxt)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkAmountButton1)
                    .addComponent(paymentAmountCrdtTblTxt))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paidAmountlbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(billTotalLbl1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(billTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paidAmountTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(billTotalLbl1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(billTxt1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paidAmountlbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paidAmountTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {billTotalLbl1, billTxt1, paidAmountTxt1, paidAmountlbl1});

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        creditDetailsCreditorLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        creditDetailsCreditorLbl.setText("Creditor");

        creditDetailsSettleentNoLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        creditDetailsSettleentNoLbl.setText("Settlement No.");

        creditDetailsSettlementNoTxt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        creditDetailsSettlementNoTxt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        creditDetailsSettlementNoTxt.setText(" ");

        creditDetailsCustomerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditDetailsCustomerComboBoxActionPerformed(evt);
            }
        });

        checkCreditDetailsBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        checkCreditDetailsBtn.setText("Check");
        checkCreditDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkCreditDetailsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(creditDetailsCreditorLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditDetailsSettleentNoLbl))
                .addGap(32, 32, 32)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(creditDetailsCustomerComboBox, 0, 225, Short.MAX_VALUE)
                    .addComponent(creditDetailsSettlementNoTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(81, 81, 81)
                .addComponent(checkCreditDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(creditDetailsCustomerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditDetailsCreditorLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkCreditDetailsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(creditDetailsSettlementNoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditDetailsSettleentNoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        installmentPaymentLbl4.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl4.setText("Credit Details");
        installmentPaymentLbl4.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl4.setOpaque(true);

        creditTblBackBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        creditTblBackBtn.setText("Back");
        creditTblBackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditTblBackBtnActionPerformed(evt);
            }
        });

        confirmBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        confirmBtn1.setText("Confirm");
        confirmBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmBtn1ActionPerformed(evt);
            }
        });

        cancelBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancelBtn1.setText("Cancel");
        cancelBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(installmentPaymentLbl4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 10, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(creditTblBackBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(confirmBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(installmentPaymentLbl4)
                .addGap(20, 20, 20)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(creditTblBackBtn)
                    .addComponent(confirmBtn1)
                    .addComponent(cancelBtn1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout creditDetailsLayout = new javax.swing.GroupLayout(creditDetails);
        creditDetails.setLayout(creditDetailsLayout);
        creditDetailsLayout.setHorizontalGroup(
            creditDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creditDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 997, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        creditDetailsLayout.setVerticalGroup(
            creditDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creditDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardPanel.add(creditDetails, "creditDetails");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        editCustomerSaveBtn2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editCustomerSaveBtn2.setText("Save");
        editCustomerSaveBtn2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editCustomerSaveBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerSaveBtn2ActionPerformed(evt);
            }
        });

        editCustomerFullNameLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editCustomerFullNameLbl2.setText("Full Name");

        editCustomerCoopIdLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editCustomerCoopIdLbl2.setText("Coop ID");

        editCustomerAddressLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editCustomerAddressLbl2.setText("Address");

        editCustomerFullNameTxt2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        editCustomerCoopIdTxt2.setEditable(false);
        editCustomerCoopIdTxt2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editCustomerCoopIdTxt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerCoopIdTxt2ActionPerformed(evt);
            }
        });

        editCustomerAddressTxt2.setColumns(20);
        editCustomerAddressTxt2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editCustomerAddressTxt2.setRows(5);
        jScrollPane8.setViewportView(editCustomerAddressTxt2);

        editCustomerNicLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editCustomerNicLbl2.setText("NIC No");

        editCustomerTeleTxt2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editCustomerTeleTxt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerTeleTxt2ActionPerformed(evt);
            }
        });

        editCustomerTeleLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editCustomerTeleLbl2.setText("Telephone No");

        editCustomerNicTxt2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editCustomerNicTxt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerNicTxt2ActionPerformed(evt);
            }
        });

        editCustomerLbl2.setBackground(new java.awt.Color(204, 204, 204));
        editCustomerLbl2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editCustomerLbl2.setText("Edit Customer");
        editCustomerLbl2.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        editCustomerLbl2.setOpaque(true);

        editCustomerBackBtn3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editCustomerBackBtn3.setText("Back");
        editCustomerBackBtn3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editCustomerBackBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerBackBtn3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(editCustomerLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(editCustomerBackBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editCustomerSaveBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(editCustomerFullNameLbl2)
                                .addComponent(editCustomerCoopIdLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editCustomerNicLbl2)
                                .addComponent(editCustomerAddressLbl2)
                                .addComponent(editCustomerTeleLbl2))
                            .addGap(31, 31, 31)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(editCustomerFullNameTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editCustomerCoopIdTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editCustomerNicTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editCustomerTeleTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(20, 20, 20))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {editCustomerAddressLbl2, editCustomerCoopIdLbl2, editCustomerFullNameLbl2, editCustomerNicLbl2, editCustomerTeleLbl2});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {editCustomerCoopIdTxt2, editCustomerFullNameTxt2, editCustomerNicTxt2, editCustomerTeleTxt2, jScrollPane8});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(editCustomerLbl2)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(editCustomerFullNameLbl2))
                    .addComponent(editCustomerFullNameTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editCustomerCoopIdLbl2)
                    .addComponent(editCustomerCoopIdTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(editCustomerNicLbl2))
                    .addComponent(editCustomerNicTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editCustomerAddressLbl2)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(editCustomerTeleLbl2))
                    .addComponent(editCustomerTeleTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editCustomerSaveBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editCustomerBackBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {editCustomerAddressLbl2, editCustomerCoopIdLbl2, editCustomerCoopIdTxt2, editCustomerFullNameLbl2, editCustomerFullNameTxt2, editCustomerNicLbl2, editCustomerNicTxt2, editCustomerTeleLbl2, editCustomerTeleTxt2});

        javax.swing.GroupLayout editCustomerLayout = new javax.swing.GroupLayout(editCustomer);
        editCustomer.setLayout(editCustomerLayout);
        editCustomerLayout.setHorizontalGroup(
            editCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editCustomerLayout.createSequentialGroup()
                .addContainerGap(266, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(236, 236, 236))
        );
        editCustomerLayout.setVerticalGroup(
            editCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editCustomerLayout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        cardPanel.add(editCustomer, "editCustomer");

        customerDetails.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        customerDetailsTbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        customerDetailsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Coop Id", "Customer Name", "Address", "Tele No.", "NIC No.", "Current Credit"
            }
        ));
        customerDetailsTbl.setEditable(false);
        customerDetailsTbl.setSelectionBackground(new java.awt.Color(153, 153, 153));
        customerDetailsTbl.setShowGrid(true);
        jScrollPane6.setViewportView(customerDetailsTbl);

        jXPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        editCustomerBtn.setText("Edit");
        editCustomerBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCustomerBtnActionPerformed(evt);
            }
        });

        deleteCustomerBtn.setText("Delete");
        deleteCustomerBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCustomerBtnActionPerformed(evt);
            }
        });

        customerDetailsCreditDetailsBtn.setText("Credit Details");
        customerDetailsCreditDetailsBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
                .addGap(20, 20, 20)
                .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerDetailsCreditDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jXPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {customerDetailsCreditDetailsBtn, deleteCustomerBtn, editCustomerBtn});

        jXPanel2Layout.setVerticalGroup(
            jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(editCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customerDetailsCreditDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        newCustomerLbl1.setBackground(new java.awt.Color(204, 204, 204));
        newCustomerLbl1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newCustomerLbl1.setText("Customer Details                                                                                                                                                               ");
        newCustomerLbl1.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        newCustomerLbl1.setOpaque(true);

        javax.swing.GroupLayout customerDetailsLayout = new javax.swing.GroupLayout(customerDetails);
        customerDetails.setLayout(customerDetailsLayout);
        customerDetailsLayout.setHorizontalGroup(
            customerDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addGap(13, 13, 13)
                .addComponent(jXPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addGroup(customerDetailsLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(newCustomerLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 1004, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        customerDetailsLayout.setVerticalGroup(
            customerDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerDetailsLayout.createSequentialGroup()
                .addComponent(newCustomerLbl1)
                .addGap(20, 20, 20)
                .addGroup(customerDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(customerDetailsLayout.createSequentialGroup()
                        .addComponent(jXPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(328, Short.MAX_VALUE))
                    .addComponent(jScrollPane6)))
        );

        cardPanel.add(customerDetails, "customerDetails");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void installmentPaymentSubmitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installmentPaymentSubmitBtnActionPerformed
        // TODO add your handling code here:

      //  if ((InstallmentPaymentDatePicker.getDate() != null)) {
            try {
                logger.info("credit customer payment confirmation");
                CustomerCreditController.settleCustomerCredit((Double.parseDouble(InstallmentPaymentPaymentAmountLbl.getText()) - CustomerCreditController.getCustomer(Integer.parseInt(creditDetailsSettlementNoTxt.getText())).getCurrentCredit()), Integer.parseInt(creditDetailsSettlementNoTxt.getText()));
                for (int i = 0; i < CreditDetailsTableModel.getRowCount(); i++) {
                    if (CreditDetailsTableModel.getValueAt(i, 0).equals(true)) {
                        CoopCreditPaymentController.deleteDeatils(Integer.parseInt(CreditDetailsTableModel.getValueAt(i, 1).toString()));
                    }
                }

                Utilities.showMsgBox("Payment Successful", "Confirmed", JOptionPane.PLAIN_MESSAGE);
                InstallmentPaymentCustomerNameLblValue.setText(null);
              //  InstallmentPaymentDatePicker.setDate(null);
                InstallmentPaymentPaymentAmountLbl.setText(null);
                cleanCreditTbl();
                chngTblModel();
                CardLayout card = (CardLayout) cardPanel.getLayout();
                card.show(cardPanel, "customerDetails");
            } catch (SQLException ex) {
                logger.error("SQL exception has occured");
                Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
       // } else {
          //  Utilities.showMsgBox("Select the date", "Error", JOptionPane.WARNING_MESSAGE);
         //   return;
        //}

    }//GEN-LAST:event_installmentPaymentSubmitBtnActionPerformed

    private void InstallmentPaymentCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InstallmentPaymentCancelBtnActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "creditDetails");
        InstallmentPaymentCustomerNameLblValue.setText(null);
//  InstallmentPaymentDatePicker.setDate(null);
        InstallmentPaymentPaymentAmountLbl.setText(null);
    }//GEN-LAST:event_InstallmentPaymentCancelBtnActionPerformed

    private void deleteCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCustomerBtnActionPerformed
        // TODO add your handling code here:
//        for (int i = 0; i < getCustomerDetailsTbl().getRowCount(); i++) {
//            if (customerDetailsTbl.isRowSelected(i)) {
//
//                deleteCustomer(Integer.parseInt(customerDetailsTbl.getValueAt(i, 0).toString()));
//                break;
//            } else {
//                if (i == getCustomerDetailsTbl().getRowCount() - 1) {
//                    Utilities.showMsgBox("Select a row to delete details", "WARNING", JOptionPane.WARNING_MESSAGE);
//                }
//            }
//        }
        int row = customerDetailsTbl.getSelectedRow();
        if (row > -1) {
            System.out.println("Selected customer row : " + row);
            deleteCustomer(Integer.parseInt(customerDetailsTbl.getValueAt(row, 0).toString()));
        } else {
            Utilities.showMsgBox("Select a row to delete", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_deleteCustomerBtnActionPerformed

    private void custoemrSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_custoemrSearchBtnActionPerformed
        searchDetails();
        customerSearchTxt.setText("");
        //  CardLayout card = (CardLayout) cardPanel.getLayout();
        //  card.show(cardPanel, "customerSearchResult");
    }//GEN-LAST:event_custoemrSearchBtnActionPerformed

    private void customerDetailsCreditDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerDetailsCreditDetailsBtnActionPerformed
        // TODO add your handling code here:
        cleanCreditTbl();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "creditDetails");
        loadDetails();
    }//GEN-LAST:event_customerDetailsCreditDetailsBtnActionPerformed

    private void addCustomerAddBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerAddBtn1ActionPerformed
        logger.debug("validating addCustomer details ");
        //Validate name
        boolean isValid = false;
        if (!validateName(addCustomerFullNameTxt1.getText())) {
            Utilities.showMsgBox("Enter a valid name", "WARNING", JOptionPane.WARNING_MESSAGE);
            addCustomerFullNameTxt1.requestFocus();
            return;
        }
       else{
            try {
                isValid =CustomerCreditController.checkDataExistence(addCustomerFullNameTxt1.getText());
                if(!isValid){
                    logger.info("valid name entered");
                }
                else{
                    logger.error("this name exists");
                    Utilities.showMsgBox("This customer name already taken", " Error", JOptionPane.WARNING_MESSAGE);
                    addCustomerFullNameTxt1.setText("");
                    addCustomerFullNameTxt1.requestFocus();
                    return;
                    
                }
                logger.info("valid name");
            } catch (SQLException ex) {
               logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;   }
        }
        

        //Validate nic
        String nic = addCustomerNicTxt1.getText();
        if ((nic.length() == 10) && nic.toLowerCase().endsWith("v")) {
            try {
                logger.info("NIC no parse result : " + Integer.parseInt(nic.substring(0, 9)));
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Enter a valid nic number", "WARNING", JOptionPane.WARNING_MESSAGE);
                addCustomerNicTxt1.requestFocus();
                return;
            }
        } else {
            Utilities.showMsgBox("Enter a valid id number", "WARNING", JOptionPane.WARNING_MESSAGE);
            addCustomerNicTxt1.requestFocus();
            return;
        }
        String address = addCustomerAddressTxt1.getText();
         if (this.addCustomerAddressTxt1.getText() == null || this.addCustomerAddressTxt1.getText().trim().equals("")) {
                logger.debug((Object) "invalid detail or empty block found");
                Utilities.showMsgBox((String) "Give a valid address", (String) " Error", (int) 2);
                return;
            }
//
        //validate tel no
        String telNo = addCustomerTeleTxt1.getText();
        if ((telNo.length() == 11) && telNo.contains("-") && telNo.substring(3, 4).equals("-")) {
            try {
                logger.info("Tel no parse result : " + Long.parseLong(telNo.substring(0, 3) + telNo.substring(4, 11)));
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Enter a valid telephone number", "WARNING", JOptionPane.WARNING_MESSAGE);
                addCustomerTeleTxt1.requestFocus();
                return;
            }
        } else {
            Utilities.showMsgBox("Enter a valid telephone number", "WARNING", JOptionPane.WARNING_MESSAGE);
            addCustomerTeleTxt1.requestFocus();
            return;
        }

        addCustomer();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerDetails");
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

    private void customerSearchTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerSearchTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerSearchTxtActionPerformed

    private void customerSearchCreditDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerSearchCreditDetailsBtnActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "creditDetails");
    }//GEN-LAST:event_customerSearchCreditDetailsBtnActionPerformed

    private void editCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCustomerBtnActionPerformed
        // TODO add your handling code here:
//        for (int i = 0; i < getCustomerDetailsTbl().getRowCount(); i++) {
//            if (customerDetailsTbl.isRowSelected(i)) {
//
//                try {
//                    System.out.println("row" + i);
//                    editCustomerDetails(Integer.parseInt(customerDetailsTbl.getValueAt(i, 0).toString()));
//                } catch (SQLException ex) {
//                    java.util.logging.Logger.getLogger(FinalCredit.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                break;
//            } else {
//                if (i == getCustomerDetailsTbl().getRowCount() - 1) {
//                    Utilities.showMsgBox("Select a row to edit details", "WARNING", JOptionPane.WARNING_MESSAGE);
//                }
//            }
//        }
        selectedIndex = customerDetailsTbl.getSelectedRow();
        int row = customerDetailsTbl.getSelectedRow();
        if (row > -1) {
            System.out.println("Selected customer row : " + row);
            showCustomerEditDetails(Integer.parseInt(customerDetailsTbl.getValueAt(row, 0).toString()));
        } else {
            Utilities.showMsgBox("Select a row to edit details", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
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
        cleanCreditTbl();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "creditDetails");
        loadDetails();
    }//GEN-LAST:event_creditManagementCreditDetailsLblActionPerformed

    private void editCustomerSaveBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCustomerSaveBtn2ActionPerformed

        boolean isValid = true;
   
            if (this.editCustomerFullNameTxt2.getText() != null && !this.editCustomerFullNameTxt2.getText().trim().equals("") && this.validateName(this.editCustomerFullNameTxt2.getText().trim())) {
                logger.info("selected index :"+ selectedIndex);
                if (this.editCustomerFullNameTxt2.getText().trim().equals(this.customerDetailsTbl.getValueAt(this.selectedIndex, 1).toString().trim())) {
                    isValid = false;
                    logger.info("Names are equal" );
                } else {
                    try {
                        isValid = CustomerCreditController.checkDataExistence((String)this.editCustomerFullNameTxt2.getText().trim());
                    } catch (SQLException ex) {
                       logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
             return;   
                    }

                }
                if (isValid) {
                    logger.error((Object) "this name exists");
                    Utilities.showMsgBox((String) "This user name already taken", (String) " Error", (int) 2);
                    this.editCustomerFullNameTxt2.setText("");
                    this.editCustomerFullNameTxt2.requestFocus();
                    return;
                }
                logger.info((Object) "valid name entered");
            } else {
                logger.debug((Object) "invalid detail or empty block found");
                Utilities.showMsgBox((String) "Give a valid name", (String) " Error", (int) 2);
                this.editCustomerFullNameTxt2.setText("");
                this.editCustomerFullNameTxt2.requestFocus();
                return;
            }
            logger.info((Object) "valid name");

        //Validate nic
        String nic = editCustomerNicTxt2.getText();
        if ((nic.length() == 10) && nic.toLowerCase().endsWith("v")) {
            try {
                logger.info("NIC no parse result : " + Integer.parseInt(nic.substring(0, 9)));
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Enter a valid nic number", "WARNING", JOptionPane.WARNING_MESSAGE);
                editCustomerNicTxt2.requestFocus();
                return;
            }
        } else {
            Utilities.showMsgBox("Enter a valid id number", "WARNING", JOptionPane.WARNING_MESSAGE);
            editCustomerNicTxt2.requestFocus();
            return;
        }
          String address = editCustomerAddressTxt2.getText();
         if (this.editCustomerAddressTxt2.getText() == null || editCustomerAddressTxt2.getText().trim().equals("") ) {
                logger.debug((Object) "invalid detail or empty block found");
                Utilities.showMsgBox((String) "Give a valid address", (String) " Error", (int) 2);
                return;
            }
//
        //validate tel no
        String telNo = editCustomerTeleTxt2.getText();
        if ((telNo.length() == 11) && telNo.contains("-") && telNo.substring(3, 4).equals("-")) {

            try {
                logger.info("Tel no parse result : " + Long.parseLong(telNo.substring(0, 3) + telNo.substring(4, 11)));
            } catch (NumberFormatException ex) {
                Utilities.showMsgBox("Enter a valid telephone number", "WARNING", JOptionPane.WARNING_MESSAGE);
                editCustomerTeleTxt2.requestFocus();
                return;
            }
        } else {
            Utilities.showMsgBox("Enter a valid telephone number", "WARNING", JOptionPane.WARNING_MESSAGE);
            editCustomerTeleTxt2.requestFocus();
            return;
        }

        editCustomer();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerDetails");
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

        if (creditDetailsCustomerComboBox.getSelectedIndex() >= 0) {
            loadCreditDetails();
        } else {
            Utilities.showMsgBox("No item selected in creditDetailsCustomerComboBox", "WARNING", JOptionPane.WARNING_MESSAGE);

            return;
        }


    }//GEN-LAST:event_checkCreditDetailsBtnActionPerformed

    private void addCustomerFullNameTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerFullNameTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCustomerFullNameTxt1ActionPerformed

    private void confirmBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtn1ActionPerformed
        // TODO add your handling code here:
        boolean checked = false;
        for (int j = 0; j < creditTbl.getRowCount(); j++) {
            if (creditTbl.getValueAt(j, 0).equals(true)) {
                checked = true;
            }
        }
        if (checked) {
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
        } else {
            Utilities.showMsgBox("No payment selected", "WARNING", JOptionPane.WARNING_MESSAGE);
            logger.error("check whether the user selected a payment or not.");
            return;
        }

    }//GEN-LAST:event_confirmBtn1ActionPerformed

    private void cancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtn1ActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < creditTbl.getRowCount(); i++) {
            creditTbl.setValueAt(false, 0, i);
            
        }
        paymentAmountCrdtTblTxt.setText("");
    }//GEN-LAST:event_cancelBtn1ActionPerformed

    private void creditTblBackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditTblBackBtnActionPerformed
        // TODO add your handling code here:
        cleanCreditTbl();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerSearch");
    }//GEN-LAST:event_creditTblBackBtnActionPerformed

    private void checkAmountButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAmountButton1ActionPerformed
        // TODO add your handling code here:
        selectPayment();

    }//GEN-LAST:event_checkAmountButton1ActionPerformed

    private void paymentAmountCrdtTblTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentAmountCrdtTblTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentAmountCrdtTblTxtActionPerformed

    private void editCustomerBackBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCustomerBackBtn3ActionPerformed
        // TODO add your handling code here:
        
        editCustomerCoopIdTxt2.setText("");
            editCustomerFullNameTxt2.setText("");
            editCustomerAddressTxt2.setText("");
            editCustomerNicTxt2.setText("");
            editCustomerTeleTxt2.setText("");
                    CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerSearch");
    }//GEN-LAST:event_editCustomerBackBtn3ActionPerformed

    private void addCustomerBackBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerBackBtn2ActionPerformed
        // TODO add your handling code here:
       addCustomerCoopIdTxt1.setText("");
            addCustomerFullNameTxt1.setText("");
            addCustomerAddressTxt1.setText("");
            addCustomerNicTxt1.setText("");
            addCustomerTeleTxt1.setText("");
             CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerSearch");
    }//GEN-LAST:event_addCustomerBackBtn2ActionPerformed

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
                new FinalCredit().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton InstallmentPaymentCancelBtn;
    private javax.swing.JLabel InstallmentPaymentCustomerNameLblValue;
    private javax.swing.JLabel InstallmentPaymentPaymentAmountLbl;
    private javax.swing.JLabel InstallmetPaymentPaymentValue;
    private javax.swing.JPanel addCustomer;
    private org.jdesktop.swingx.JXButton addCustomerAddBtn1;
    private javax.swing.JLabel addCustomerAddressLbl1;
    private javax.swing.JTextArea addCustomerAddressTxt1;
    private org.jdesktop.swingx.JXButton addCustomerBackBtn2;
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
    private javax.swing.JButton checkAmountButton1;
    private javax.swing.JButton checkCreditDetailsBtn;
    private org.jdesktop.swingx.painter.CheckerboardPainter checkerboardPainter1;
    private javax.swing.JButton confirmBtn1;
    private javax.swing.JPanel creditDetails;
    private javax.swing.JLabel creditDetailsCreditorLbl;
    private javax.swing.JComboBox creditDetailsCustomerComboBox;
    private javax.swing.JLabel creditDetailsSettleentNoLbl;
    private javax.swing.JLabel creditDetailsSettlementNoTxt;
    private javax.swing.JButton creditManagementCreditDetailsLbl;
    private org.jdesktop.swingx.JXTaskPane creditManagementTaskPane;
    private javax.swing.JTable creditTbl;
    private javax.swing.JButton creditTblBackBtn;
    private javax.swing.JButton custoemrSearchBtn;
    private javax.swing.JPanel customerDetails;
    private org.jdesktop.swingx.JXButton customerDetailsCreditDetailsBtn;
    private org.jdesktop.swingx.JXTable customerDetailsTbl;
    private javax.swing.ButtonGroup customerGender;
    private javax.swing.JPanel customerSearch;
    private javax.swing.JLabel customerSearchCoopIdTxt;
    private javax.swing.JButton customerSearchCreditDetailsBtn;
    private javax.swing.JLabel customerSearchCustomerNameLbl;
    private javax.swing.JLabel customerSearchCustomerNameTxt;
    private javax.swing.JLabel customerSearchLBl;
    private javax.swing.JPanel customerSearchResultPanel;
    private javax.swing.JButton customerSearchSearchLbl;
    private org.jdesktop.swingx.JXTaskPane customerSearchTaskPane;
    private javax.swing.JTextField customerSearchTxt;
    private javax.swing.JLabel customerSearchcoopIdLbl;
    private org.jdesktop.swingx.JXButton deleteCustomerBtn;
    private javax.swing.JPanel editCustomer;
    private javax.swing.JLabel editCustomerAddressLbl2;
    private javax.swing.JTextArea editCustomerAddressTxt2;
    private org.jdesktop.swingx.JXButton editCustomerBackBtn3;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
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
    private javax.swing.JTextField paymentAmountCrdtTblTxt;
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
