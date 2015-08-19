/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.employee;

import controller.credit.EmployeeCreditController;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;
import model.creditManagement.Employee;
import net.proteanit.sql.DbUtils;
import util.StringFilter;
import util.Utilities;

/**
 *
 * @author HP
 */
public class ManageEmployees extends javax.swing.JInternalFrame {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ManageEmployees.class);

    private ArrayList<Employee> availableEmployees;
    private int selectedIndex;

    public ManageEmployees() {
        this.initComponents();
        try {
            this.loadComboBoxes();
        } catch (SQLException ex) {
            logger.error((Object) ex);
        }

        ((PlainDocument) addEmployeeFullNameTxt1.getDocument()).setDocumentFilter(new StringFilter());
        ((PlainDocument) addEmployeePositionTxt.getDocument()).setDocumentFilter(new StringFilter());
        ((PlainDocument) editEmployeeFullNameTxt2.getDocument()).setDocumentFilter(new StringFilter());
        ((PlainDocument) editEmployeePositionTxt1.getDocument()).setDocumentFilter(new StringFilter());

    }

    public void loadTableModel() {
        try {
            this.manageEmployeeTbl.setModel(DbUtils.resultSetToTableModel((ResultSet) EmployeeCreditController.loadDetails()));
            this.manageEmployeeTbl.getTableHeader().setFont(new Font("Segoe UI", 1, 16));
            manageEmployeeTbl.setSelectionMode(0);
        } catch (SQLException ex) {
            logger.debug((Object) ex);
            Utilities.showMsgBox((String) ex.getMessage(), (String) "Error", (int) 2);
        }
    }

    public void loadComboBoxes() throws SQLException {
        this.availableEmployees = EmployeeCreditController.loadEmployees();
        this.employeeComboBox.removeAllItems();
        this.employeeComboBox.setSelectedIndex(-1);
        for (Employee e : this.availableEmployees) {
            this.employeeComboBox.addItem(e);
        }
    }

    public void searchEmployeeDetails() {
        if (this.employeeComboBox.getSelectedIndex() >= 0) {
            Employee e = (Employee) this.employeeComboBox.getSelectedItem();
            this.employeeSearchEmployeeIdrslt.setText(Utilities.convertKeyToString((int) e.getEmployeeId(), (String) "employee"));
            this.employeeSearchFullNamerslt.setText(e.getEmployeeName());
            this.employeeSearchPositionRslt.setText(e.getPosition());
            if (e.isVoucherIssued()) {
                employeeVoucherTB.setText("Used");
            } else {
                employeeVoucherTB.setText("Not Used");
            }
            CardLayout card = (CardLayout) this.subCardPanel.getLayout();
            card.show(this.subCardPanel, "employeeSearchResult");
        } else {
            Utilities.ShowErrorMsg((Component) this, (String) "Please select Employee Name First");
        }
    }

    public boolean validateName(String txt) {
        logger.debug((Object) "validateName invoked");
        String regx = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
        Pattern pattern = Pattern.compile(regx, 2);
        Matcher matcher = pattern.matcher((CharSequence) txt);
        return matcher.find();
    }

    public void addEmployee() {
        boolean isValid = true;
        try {
            if (this.addEmployeeFullNameTxt1.getText() != null && !this.addEmployeeFullNameTxt1.getText().trim().equals("") && this.validateName(this.addEmployeeFullNameTxt1.getText().trim())) {
                isValid = EmployeeCreditController.checkDataExistence((String) this.addEmployeeFullNameTxt1.getText().trim());
                if (isValid) {
                    logger.error((Object) "this name exists");
                    Utilities.showMsgBox((String) "This user name already taken", (String) " Error", (int) 2);
                    this.addEmployeeFullNameTxt1.setText("");
                    this.addEmployeeFullNameTxt1.requestFocus();
                    return;
                }
                logger.info((Object) "valid name entered");
            } else {
                logger.debug((Object) "invalid detail or empty block found");
                Utilities.showMsgBox((String) "Give a valid name", (String) " Error", (int) 2);
                this.addEmployeeFullNameTxt1.setText("");
                this.addEmployeeFullNameTxt1.requestFocus();
                return;
            }
            logger.info((Object) "valid name");
            if (this.addEmployeePositionTxt.getText() == null || this.addEmployeePositionTxt.getText().trim().equals("") || !this.validateName(this.addEmployeePositionTxt.getText().trim())) {
                logger.debug((Object) "invalid detail or empty block found");
                Utilities.showMsgBox((String) "Give a valid position", (String) " Error", (int) 2);
                return;
            }
            logger.info((Object) "valid position");
            Employee employee = new Employee(Utilities.convertKeyToInteger((String) this.addEmployeeIdTxt1.getText().trim()), this.addEmployeeFullNameTxt1.getText().trim(), this.addEmployeePositionTxt.getText().trim(), false);
            EmployeeCreditController.addEmployee((Employee) employee);
            this.loadTableModel();
            this.addEmployeeIdTxt1.setText("");
            this.addEmployeeFullNameTxt1.setText("");
            this.addEmployeePositionTxt.setText("");
            CardLayout card = (CardLayout) this.cardPanel.getLayout();
            card.show(this.cardPanel, "manageEmployees");
        } catch (SQLException ex) {
            logger.debug((Object) ("SQLException = " + ex));
            Utilities.showMsgBox((String) ex.getMessage(), (String) "Error", (int) 2);
        }
    }

    public void saveEditDetails() {
        boolean isValid = true;
        try {
            if (this.editEmployeeFullNameTxt2.getText() != null && !this.editEmployeeFullNameTxt2.getText().trim().equals("") && this.validateName(this.editEmployeeFullNameTxt2.getText().trim())) {
                if (this.editEmployeeFullNameTxt2.getText().trim().equals(this.manageEmployeeTbl.getValueAt(this.selectedIndex, 1).toString().trim())) {
                    isValid = false;
                } else {
                    isValid = EmployeeCreditController.checkDataExistence((String) this.editEmployeeFullNameTxt2.getText().trim());

                }
                if (isValid) {
                    logger.error((Object) "this name exists");
                    Utilities.showMsgBox((String) "This user name already taken", (String) " Error", (int) 2);
                    this.editEmployeeFullNameTxt2.setText("");
                    this.editEmployeeFullNameTxt2.requestFocus();
                    return;
                }
                logger.info((Object) "valid name entered");
            } else {
                logger.debug((Object) "invalid detail or empty block found");
                Utilities.showMsgBox((String) "Give a valid name", (String) " Error", (int) 2);
                this.editEmployeeFullNameTxt2.setText("");
                this.editEmployeeFullNameTxt2.requestFocus();
                return;
            }
            logger.info((Object) "valid name");
            if (this.editEmployeePositionTxt1.getText() == null || this.editEmployeePositionTxt1.getText().trim().equals("") || !this.validateName(this.editEmployeePositionTxt1.getText().trim())) {
                logger.debug((Object) "invalid detail or empty block found");
                Utilities.showMsgBox((String) "Give a valid position", (String) " Error", (int) 2);
                return;
            }
            logger.info((Object) "valid position");
            Employee employee = new Employee(Utilities.convertKeyToInteger((String) this.editEmployeeIdTxt2.getText().trim()), this.editEmployeeFullNameTxt2.getText().trim(), this.editEmployeePositionTxt1.getText().trim(), true);
            EmployeeCreditController.setEditDetails((Employee) employee);
            this.loadTableModel();
            this.editEmployeeIdTxt2.setText("");
            this.editEmployeeFullNameTxt2.setText("");
            this.editEmployeePositionTxt1.setText("");
            CardLayout card = (CardLayout) this.cardPanel.getLayout();
            card.show(this.cardPanel, "manageEmployees");
        } catch (SQLException ex) {
            logger.debug((Object) ("SQLException = " + ex));
            Utilities.showMsgBox((String) ex.getMessage(), (String) "Error", (int) 2);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton1 = new javax.swing.JButton();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        jXTaskPane1 = new org.jdesktop.swingx.JXTaskPane();
        taskPaneSearchBtn = new javax.swing.JButton();
        jXTaskPane2 = new org.jdesktop.swingx.JXTaskPane();
        taskPaneManageEmployeesBtn = new javax.swing.JButton();
        taskPaneManageEmployeesBtn1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        subCardPanel = new javax.swing.JPanel();
        employeeSearch = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        employeeSearchEmployeeNameLbl = new javax.swing.JLabel();
        employeeSearchSearchBtn = new javax.swing.JButton();
        installmentPaymentLbl3 = new javax.swing.JLabel();
        employeeComboBox = new javax.swing.JComboBox();
        employeeSearchResultPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        installmentPaymentLbl2 = new javax.swing.JLabel();
        searchResultsOKBtn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        employeeSearchEmployeeId = new javax.swing.JLabel();
        employeeSearchEmployeeIdrslt = new javax.swing.JLabel();
        employeeSearchFullNameLbl = new javax.swing.JLabel();
        employeeSearchFullNamerslt = new javax.swing.JLabel();
        employeeSearchPosition = new javax.swing.JLabel();
        employeeSearchPositionRslt = new javax.swing.JLabel();
        employeeSearchCredit = new javax.swing.JLabel();
        employeeVoucherTB = new javax.swing.JLabel();
        addEmployeePanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        addEmployeeAddBtn = new org.jdesktop.swingx.JXButton();
        addEmployeeFullNameLbl2 = new javax.swing.JLabel();
        addEmployeeFullNameTxt1 = new javax.swing.JTextField();
        addEmployeePositionLbl = new javax.swing.JLabel();
        addEmployeePositionTxt = new javax.swing.JTextField();
        installmentPaymentLbl4 = new javax.swing.JLabel();
        addEmployeeIdLbl1 = new javax.swing.JLabel();
        addEmployeeIdTxt1 = new javax.swing.JTextField();
        addEmployeeAddBtn1 = new org.jdesktop.swingx.JXButton();
        manageEmployees = new javax.swing.JPanel();
        manageEmployeesEditPanel = new org.jdesktop.swingx.JXPanel();
        edittEmployeeBtn1 = new org.jdesktop.swingx.JXButton();
        deleteEmployeeBtn1 = new org.jdesktop.swingx.JXButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        manageEmployeeTbl = new org.jdesktop.swingx.JXTable();
        employeeDetailsLbl = new javax.swing.JLabel();
        editEmployeePanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        editEmployeeSaveBtn1 = new org.jdesktop.swingx.JXButton();
        editEmployeeFullNameLbl3 = new javax.swing.JLabel();
        editEmployeeFullNameTxt2 = new javax.swing.JTextField();
        editEmployeePositionLbl1 = new javax.swing.JLabel();
        editEmployeePositionTxt1 = new javax.swing.JTextField();
        editEmployeeLbl = new javax.swing.JLabel();
        editEmployeeIdLbl2 = new javax.swing.JLabel();
        editEmployeeIdTxt2 = new javax.swing.JTextField();
        editEmployeeBackBtn2 = new org.jdesktop.swingx.JXButton();
        emptyLayer = new javax.swing.JPanel();

        jButton1.setText("Add Employee");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(null);

        jXTaskPaneContainer1.setBackground(java.awt.SystemColor.activeCaptionBorder);
        jXTaskPaneContainer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jXTaskPane1.setBackground(new java.awt.Color(204, 204, 204));
        jXTaskPane1.setSpecial(true);
        jXTaskPane1.setTitle("Employee Search");
        jXTaskPane1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        taskPaneSearchBtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        taskPaneSearchBtn.setText("Search");
        taskPaneSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taskPaneSearchBtnActionPerformed(evt);
            }
        });
        jXTaskPane1.getContentPane().add(taskPaneSearchBtn);

        jXTaskPaneContainer1.add(jXTaskPane1);

        jXTaskPane2.setBackground(new java.awt.Color(204, 204, 204));
        jXTaskPane2.setSpecial(true);
        jXTaskPane2.setTitle("Manage Employees");
        jXTaskPane2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        taskPaneManageEmployeesBtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        taskPaneManageEmployeesBtn.setText("Manage Employees");
        taskPaneManageEmployeesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taskPaneManageEmployeesBtnActionPerformed(evt);
            }
        });
        jXTaskPane2.getContentPane().add(taskPaneManageEmployeesBtn);

        taskPaneManageEmployeesBtn1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        taskPaneManageEmployeesBtn1.setText("Add  Employee");
        taskPaneManageEmployeesBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taskPaneManageEmployeesBtn1ActionPerformed(evt);
            }
        });
        jXTaskPane2.getContentPane().add(taskPaneManageEmployeesBtn1);
        taskPaneManageEmployeesBtn1.getAccessibleContext().setAccessibleName("AddEmployee");

        jXTaskPaneContainer1.add(jXTaskPane2);

        cardPanel.setPreferredSize(new java.awt.Dimension(1075, 978));
        cardPanel.setLayout(new java.awt.CardLayout());

        subCardPanel.setLayout(new java.awt.CardLayout());

        employeeSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        employeeSearch.setName("paymentHistory");

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        employeeSearchEmployeeNameLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        employeeSearchEmployeeNameLbl.setText("Employee Name");

        employeeSearchSearchBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        employeeSearchSearchBtn.setText("Search");
        employeeSearchSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeSearchSearchBtnActionPerformed(evt);
            }
        });

        installmentPaymentLbl3.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        installmentPaymentLbl3.setText("Search");
        installmentPaymentLbl3.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl3.setOpaque(true);

        employeeComboBox.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(employeeSearchSearchBtn)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(employeeSearchEmployeeNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(employeeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(installmentPaymentLbl3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(installmentPaymentLbl3)
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeSearchEmployeeNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeComboBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(employeeSearchSearchBtn)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout employeeSearchLayout = new javax.swing.GroupLayout(employeeSearch);
        employeeSearch.setLayout(employeeSearchLayout);
        employeeSearchLayout.setHorizontalGroup(
            employeeSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeSearchLayout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(258, Short.MAX_VALUE))
        );
        employeeSearchLayout.setVerticalGroup(
            employeeSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeSearchLayout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        subCardPanel.add(employeeSearch, "employeeSearch");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        installmentPaymentLbl2.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        installmentPaymentLbl2.setText("Employee Details");
        installmentPaymentLbl2.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl2.setOpaque(true);

        searchResultsOKBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        searchResultsOKBtn.setText("OK");
        searchResultsOKBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchResultsOKBtnActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        employeeSearchEmployeeId.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        employeeSearchEmployeeId.setText("Employee Id");

        employeeSearchEmployeeIdrslt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        employeeSearchEmployeeIdrslt.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        employeeSearchFullNameLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        employeeSearchFullNameLbl.setText("Full Name");

        employeeSearchFullNamerslt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        employeeSearchFullNamerslt.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        employeeSearchPosition.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        employeeSearchPosition.setText("Position");

        employeeSearchPositionRslt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        employeeSearchPositionRslt.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        employeeSearchCredit.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        employeeSearchCredit.setText("Voucher");

        employeeVoucherTB.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        employeeVoucherTB.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employeeSearchEmployeeId)
                    .addComponent(employeeSearchFullNameLbl)
                    .addComponent(employeeSearchPosition)
                    .addComponent(employeeSearchCredit))
                .addGap(49, 49, 49)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(employeeSearchEmployeeIdrslt, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(employeeSearchFullNamerslt, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(employeeSearchPositionRslt, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(employeeVoucherTB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {employeeSearchCredit, employeeSearchEmployeeId, employeeSearchFullNameLbl, employeeSearchPosition});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employeeSearchEmployeeIdrslt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeSearchEmployeeId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(employeeSearchFullNameLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(employeeSearchFullNamerslt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(employeeSearchPosition, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addComponent(employeeSearchPositionRslt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employeeSearchCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeVoucherTB, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {employeeSearchCredit, employeeSearchEmployeeId, employeeSearchEmployeeIdrslt, employeeSearchFullNameLbl, employeeSearchPosition});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(installmentPaymentLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(searchResultsOKBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(installmentPaymentLbl2)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchResultsOKBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout employeeSearchResultPanelLayout = new javax.swing.GroupLayout(employeeSearchResultPanel);
        employeeSearchResultPanel.setLayout(employeeSearchResultPanelLayout);
        employeeSearchResultPanelLayout.setHorizontalGroup(
            employeeSearchResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeSearchResultPanelLayout.createSequentialGroup()
                .addGap(242, 242, 242)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(241, Short.MAX_VALUE))
        );
        employeeSearchResultPanelLayout.setVerticalGroup(
            employeeSearchResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeSearchResultPanelLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        subCardPanel.add(employeeSearchResultPanel, "employeeSearchResult");

        cardPanel.add(subCardPanel, "subCardPanel");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        addEmployeeAddBtn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addEmployeeAddBtn.setText("Add");
        addEmployeeAddBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addEmployeeAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeAddBtnActionPerformed(evt);
            }
        });

        addEmployeeFullNameLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addEmployeeFullNameLbl2.setText("  Full Name");

        addEmployeeFullNameTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addEmployeeFullNameTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeFullNameTxt1ActionPerformed(evt);
            }
        });

        addEmployeePositionLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addEmployeePositionLbl.setText("  Position");

        addEmployeePositionTxt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addEmployeePositionTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeePositionTxtActionPerformed(evt);
            }
        });

        installmentPaymentLbl4.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        installmentPaymentLbl4.setText("Add Employee");
        installmentPaymentLbl4.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl4.setOpaque(true);

        addEmployeeIdLbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addEmployeeIdLbl1.setText("  Employee Id");

        addEmployeeIdTxt1.setEditable(false);
        addEmployeeIdTxt1.setBackground(new java.awt.Color(255, 255, 255));
        addEmployeeIdTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addEmployeeIdTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeIdTxt1ActionPerformed(evt);
            }
        });

        addEmployeeAddBtn1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addEmployeeAddBtn1.setText("Back");
        addEmployeeAddBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addEmployeeAddBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeAddBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(addEmployeeIdLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(addEmployeeFullNameLbl2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(addEmployeeAddBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(addEmployeePositionLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(addEmployeeFullNameTxt1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                                .addComponent(addEmployeeIdTxt1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(addEmployeePositionTxt))
                            .addComponent(addEmployeeAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(installmentPaymentLbl4, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addEmployeeFullNameLbl2, addEmployeeIdLbl1, addEmployeePositionLbl});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(installmentPaymentLbl4)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addEmployeeIdTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addEmployeeIdLbl1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addEmployeeFullNameTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addEmployeeFullNameLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addEmployeePositionLbl)
                    .addComponent(addEmployeePositionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addEmployeeAddBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addEmployeeAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addEmployeeFullNameLbl2, addEmployeeIdLbl1, addEmployeePositionLbl});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addEmployeeFullNameTxt1, addEmployeeIdTxt1, addEmployeePositionTxt});

        javax.swing.GroupLayout addEmployeePanelLayout = new javax.swing.GroupLayout(addEmployeePanel);
        addEmployeePanel.setLayout(addEmployeePanelLayout);
        addEmployeePanelLayout.setHorizontalGroup(
            addEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addEmployeePanelLayout.createSequentialGroup()
                .addGap(240, 240, 240)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(242, Short.MAX_VALUE))
        );
        addEmployeePanelLayout.setVerticalGroup(
            addEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addEmployeePanelLayout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
        );

        cardPanel.add(addEmployeePanel, "addEmployee");

        manageEmployees.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        manageEmployees.setPreferredSize(new java.awt.Dimension(1025, 605));

        manageEmployeesEditPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        edittEmployeeBtn1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        edittEmployeeBtn1.setText("Edit");
        edittEmployeeBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        edittEmployeeBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edittEmployeeBtn1ActionPerformed(evt);
            }
        });

        deleteEmployeeBtn1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        deleteEmployeeBtn1.setText("Delete");
        deleteEmployeeBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteEmployeeBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEmployeeBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout manageEmployeesEditPanelLayout = new javax.swing.GroupLayout(manageEmployeesEditPanel);
        manageEmployeesEditPanel.setLayout(manageEmployeesEditPanelLayout);
        manageEmployeesEditPanelLayout.setHorizontalGroup(
            manageEmployeesEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageEmployeesEditPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(manageEmployeesEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edittEmployeeBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteEmployeeBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        manageEmployeesEditPanelLayout.setVerticalGroup(
            manageEmployeesEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageEmployeesEditPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(edittEmployeeBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteEmployeeBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane5.setPreferredSize(new java.awt.Dimension(150, 362));

        manageEmployeeTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EmployeeId", "Full Name", "Position"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        manageEmployeeTbl.setEditable(false);
        manageEmployeeTbl.setPreferredSize(new java.awt.Dimension(150, 0));
        manageEmployeeTbl.setShowGrid(true);
        jScrollPane5.setViewportView(manageEmployeeTbl);
        manageEmployeeTbl.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (manageEmployeeTbl.getColumnModel().getColumnCount() > 0) {
            manageEmployeeTbl.getColumnModel().getColumn(0).setPreferredWidth(30);
            manageEmployeeTbl.getColumnModel().getColumn(0).setMaxWidth(30);
            manageEmployeeTbl.getColumnModel().getColumn(2).setPreferredWidth(60);
            manageEmployeeTbl.getColumnModel().getColumn(2).setMaxWidth(80);
        }

        employeeDetailsLbl.setBackground(new java.awt.Color(204, 204, 204));
        employeeDetailsLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        employeeDetailsLbl.setText("Employee Details");
        employeeDetailsLbl.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        employeeDetailsLbl.setOpaque(true);

        javax.swing.GroupLayout manageEmployeesLayout = new javax.swing.GroupLayout(manageEmployees);
        manageEmployees.setLayout(manageEmployeesLayout);
        manageEmployeesLayout.setHorizontalGroup(
            manageEmployeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageEmployeesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(manageEmployeesEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
            .addGroup(manageEmployeesLayout.createSequentialGroup()
                .addComponent(employeeDetailsLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        manageEmployeesLayout.setVerticalGroup(
            manageEmployeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageEmployeesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(employeeDetailsLbl)
                .addGap(19, 19, 19)
                .addGroup(manageEmployeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(manageEmployeesEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardPanel.add(manageEmployees, "manageEmployees");

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        editEmployeeSaveBtn1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editEmployeeSaveBtn1.setText("Save");
        editEmployeeSaveBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editEmployeeSaveBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeSaveBtn1ActionPerformed(evt);
            }
        });

        editEmployeeFullNameLbl3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editEmployeeFullNameLbl3.setText("  Full Name");

        editEmployeeFullNameTxt2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editEmployeeFullNameTxt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeFullNameTxt2ActionPerformed(evt);
            }
        });

        editEmployeePositionLbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editEmployeePositionLbl1.setText("  Position");

        editEmployeePositionTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editEmployeePositionTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeePositionTxt1ActionPerformed(evt);
            }
        });

        editEmployeeLbl.setBackground(new java.awt.Color(204, 204, 204));
        editEmployeeLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editEmployeeLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        editEmployeeLbl.setText("Edit Employee");
        editEmployeeLbl.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        editEmployeeLbl.setOpaque(true);

        editEmployeeIdLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editEmployeeIdLbl2.setText("  Employee Id");

        editEmployeeIdTxt2.setEditable(false);
        editEmployeeIdTxt2.setBackground(new java.awt.Color(255, 255, 255));
        editEmployeeIdTxt2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editEmployeeIdTxt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeIdTxt2ActionPerformed(evt);
            }
        });

        editEmployeeBackBtn2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editEmployeeBackBtn2.setText("Back");
        editEmployeeBackBtn2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editEmployeeBackBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeBackBtn2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(editEmployeeBackBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editEmployeeSaveBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(editEmployeeLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(editEmployeePositionLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editEmployeeFullNameLbl3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(editEmployeeIdLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(editEmployeeFullNameTxt2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                            .addComponent(editEmployeeIdTxt2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(editEmployeePositionTxt1))
                        .addGap(10, 10, 10)))
                .addGap(20, 20, 20))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {editEmployeeFullNameLbl3, editEmployeeIdLbl2, editEmployeePositionLbl1});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editEmployeeLbl)
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editEmployeeIdTxt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editEmployeeIdLbl2))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editEmployeeFullNameTxt2)
                    .addComponent(editEmployeeFullNameLbl3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editEmployeePositionLbl1)
                    .addComponent(editEmployeePositionTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editEmployeeSaveBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editEmployeeBackBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {editEmployeeFullNameLbl3, editEmployeeIdLbl2, editEmployeePositionLbl1});

        javax.swing.GroupLayout editEmployeePanel1Layout = new javax.swing.GroupLayout(editEmployeePanel1);
        editEmployeePanel1.setLayout(editEmployeePanel1Layout);
        editEmployeePanel1Layout.setHorizontalGroup(
            editEmployeePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editEmployeePanel1Layout.createSequentialGroup()
                .addContainerGap(188, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(310, 310, 310))
        );
        editEmployeePanel1Layout.setVerticalGroup(
            editEmployeePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editEmployeePanel1Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardPanel.add(editEmployeePanel1, "editEmployee");

        javax.swing.GroupLayout emptyLayerLayout = new javax.swing.GroupLayout(emptyLayer);
        emptyLayer.setLayout(emptyLayerLayout);
        emptyLayerLayout.setHorizontalGroup(
            emptyLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 894, Short.MAX_VALUE)
        );
        emptyLayerLayout.setVerticalGroup(
            emptyLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        cardPanel.add(emptyLayer, "emptyPane");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 894, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 913, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addEmployeeAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeeAddBtnActionPerformed
        // TODO add your handling code here:

        addEmployee();
    }//GEN-LAST:event_addEmployeeAddBtnActionPerformed

    private void addEmployeeFullNameTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeeFullNameTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addEmployeeFullNameTxt1ActionPerformed

    private void addEmployeePositionTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeePositionTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addEmployeePositionTxtActionPerformed

    private void edittEmployeeBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edittEmployeeBtn1ActionPerformed
        // TODO add your handling code here:

        this.selectedIndex = this.manageEmployeeTbl.getSelectedRow();
        if (this.manageEmployeeTbl.getRowCount() > 0) {
            if (this.selectedIndex >= 0) {
                this.editEmployeeIdTxt2.setText(Utilities.convertKeyToString((int) Integer.parseInt(this.manageEmployeeTbl.getValueAt(this.selectedIndex, 0).toString().trim()), (String) "employee"));
                this.editEmployeeFullNameTxt2.setText(this.manageEmployeeTbl.getValueAt(this.selectedIndex, 1).toString());
                this.editEmployeePositionTxt1.setText(this.manageEmployeeTbl.getValueAt(this.selectedIndex, 2).toString());
                CardLayout card = (CardLayout) this.cardPanel.getLayout();
                card.show(this.cardPanel, "editEmployee");
            } else {
                Utilities.showMsgBox((String) "Select a row to edit details", (String) "WARNING", (int) 2);
            }
        } else {
            Utilities.showMsgBox((String) "Table is Empty", (String) "WARNING", (int) 2);
        }

    }//GEN-LAST:event_edittEmployeeBtn1ActionPerformed

    private void deleteEmployeeBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEmployeeBtn1ActionPerformed
        
        if (this.manageEmployeeTbl.getRowCount() > 0) {
            if (manageEmployeeTbl.getSelectedRow() >= 0) {
                int ans = JOptionPane.showConfirmDialog(this," Are you sure, you want to delete "+getManageEmployeeTbl().getValueAt(manageEmployeeTbl.getSelectedRow(), 1).toString() + "?", "Confirmation", 0);
                if (ans == 0){
                this.deleteEmployeeDetails(Integer.parseInt(this.getManageEmployeeTbl().getValueAt(manageEmployeeTbl.getSelectedRow(), 0).toString()));
                }
            } else {
                Utilities.showMsgBox((String) "Select a row to delete", (String) "WARNING", (int) 2);
            }
        }else {
            Utilities.ShowWarningMsg(this, "Table is empty");
        }
    }//GEN-LAST:event_deleteEmployeeBtn1ActionPerformed

    private void employeeSearchSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeSearchSearchBtnActionPerformed
        searchEmployeeDetails();


    }//GEN-LAST:event_employeeSearchSearchBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "addEmployee");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void taskPaneManageEmployeesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taskPaneManageEmployeesBtnActionPerformed
        // TODO add your handling code here:
        this.loadTableModel();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "manageEmployees");
    }//GEN-LAST:event_taskPaneManageEmployeesBtnActionPerformed

    private void taskPaneSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taskPaneSearchBtnActionPerformed
        // TODO add your handling code here:

        try {
            this.loadComboBoxes();
        } catch (SQLException ex) {
            logger.error((Object) ex);
        }
        CardLayout card = (CardLayout) this.cardPanel.getLayout();
        card.show(this.cardPanel, "subCardPanel");
        CardLayout card1 = (CardLayout) this.subCardPanel.getLayout();
        card1.show(this.subCardPanel, "employeeSearch");

    }//GEN-LAST:event_taskPaneSearchBtnActionPerformed

    private void addEmployeeIdTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeeIdTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addEmployeeIdTxt1ActionPerformed

    private void taskPaneManageEmployeesBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taskPaneManageEmployeesBtn1ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) this.cardPanel.getLayout();
        card.show(this.cardPanel, "addEmployee");
        this.setEmployeeId();
    }//GEN-LAST:event_taskPaneManageEmployeesBtn1ActionPerformed

    private void editEmployeeSaveBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeeSaveBtn1ActionPerformed

        this.saveEditDetails();
    }//GEN-LAST:event_editEmployeeSaveBtn1ActionPerformed

    private void editEmployeeFullNameTxt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeeFullNameTxt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editEmployeeFullNameTxt2ActionPerformed

    private void editEmployeePositionTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeePositionTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editEmployeePositionTxt1ActionPerformed

    private void editEmployeeIdTxt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeeIdTxt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editEmployeeIdTxt2ActionPerformed

    private void searchResultsOKBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchResultsOKBtnActionPerformed
        // TODO add your handling code here:

        this.employeeSearchEmployeeIdrslt.setText("");
        this.employeeSearchFullNamerslt.setText("");
        this.employeeSearchPositionRslt.setText("");
        CardLayout card = (CardLayout) this.cardPanel.getLayout();
        card.show(this.cardPanel, "emptyPane");


    }//GEN-LAST:event_searchResultsOKBtnActionPerformed

    private void editEmployeeBackBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeeBackBtn2ActionPerformed
        // TODO add your handling code here:
        this.loadTableModel();
        this.editEmployeeFullNameTxt2.setText("");
        this.editEmployeeIdTxt2.setText("");
        this.editEmployeePositionTxt1.setText("");
        CardLayout card = (CardLayout) this.cardPanel.getLayout();
        card.show(this.cardPanel, "manageEmployees");
    }//GEN-LAST:event_editEmployeeBackBtn2ActionPerformed

    private void addEmployeeAddBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeeAddBtn1ActionPerformed
        // TODO add your handling code here:
        this.loadTableModel();
        this.addEmployeeIdTxt1.setText("");
        this.addEmployeeFullNameTxt1.setText("");
        this.addEmployeePositionTxt.setText("");
        CardLayout card = (CardLayout) this.cardPanel.getLayout();
        card.show(this.cardPanel, "manageEmployees");
    }//GEN-LAST:event_addEmployeeAddBtn1ActionPerformed

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
            java.util.logging.Logger.getLogger(ManageEmployees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageEmployees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageEmployees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageEmployees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new ManageEmployees().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXButton addEmployeeAddBtn;
    private org.jdesktop.swingx.JXButton addEmployeeAddBtn1;
    private javax.swing.JLabel addEmployeeFullNameLbl2;
    private javax.swing.JTextField addEmployeeFullNameTxt1;
    private javax.swing.JLabel addEmployeeIdLbl1;
    private javax.swing.JTextField addEmployeeIdTxt1;
    private javax.swing.JPanel addEmployeePanel;
    private javax.swing.JLabel addEmployeePositionLbl;
    private javax.swing.JTextField addEmployeePositionTxt;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel cardPanel;
    private org.jdesktop.swingx.JXButton deleteEmployeeBtn1;
    private org.jdesktop.swingx.JXButton editEmployeeBackBtn2;
    private javax.swing.JLabel editEmployeeFullNameLbl3;
    private javax.swing.JTextField editEmployeeFullNameTxt2;
    private javax.swing.JLabel editEmployeeIdLbl2;
    private javax.swing.JTextField editEmployeeIdTxt2;
    private javax.swing.JLabel editEmployeeLbl;
    private javax.swing.JPanel editEmployeePanel1;
    private javax.swing.JLabel editEmployeePositionLbl1;
    private javax.swing.JTextField editEmployeePositionTxt1;
    private org.jdesktop.swingx.JXButton editEmployeeSaveBtn1;
    private org.jdesktop.swingx.JXButton edittEmployeeBtn1;
    private javax.swing.JComboBox employeeComboBox;
    private javax.swing.JLabel employeeDetailsLbl;
    private javax.swing.JPanel employeeSearch;
    private javax.swing.JLabel employeeSearchCredit;
    private javax.swing.JLabel employeeSearchEmployeeId;
    private javax.swing.JLabel employeeSearchEmployeeIdrslt;
    private javax.swing.JLabel employeeSearchEmployeeNameLbl;
    private javax.swing.JLabel employeeSearchFullNameLbl;
    private javax.swing.JLabel employeeSearchFullNamerslt;
    private javax.swing.JLabel employeeSearchPosition;
    private javax.swing.JLabel employeeSearchPositionRslt;
    private javax.swing.JPanel employeeSearchResultPanel;
    private javax.swing.JButton employeeSearchSearchBtn;
    private javax.swing.JLabel employeeVoucherTB;
    private javax.swing.JPanel emptyLayer;
    private javax.swing.JLabel installmentPaymentLbl2;
    private javax.swing.JLabel installmentPaymentLbl3;
    private javax.swing.JLabel installmentPaymentLbl4;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane5;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane2;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    private org.jdesktop.swingx.JXTable manageEmployeeTbl;
    private javax.swing.JPanel manageEmployees;
    private org.jdesktop.swingx.JXPanel manageEmployeesEditPanel;
    private javax.swing.JButton searchResultsOKBtn;
    private javax.swing.JPanel subCardPanel;
    private javax.swing.JButton taskPaneManageEmployeesBtn;
    private javax.swing.JButton taskPaneManageEmployeesBtn1;
    private javax.swing.JButton taskPaneSearchBtn;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the ManageEmployeesTableModel
     */
    /**
     * @return the manageEmployeeTbl
     */
    public org.jdesktop.swingx.JXTable getManageEmployeeTbl() {
        return manageEmployeeTbl;
    }

    /**
     * @param manageEmployeeTbl the manageEmployeeTbl to set
     */
    public void setManageEmployeeTbl(org.jdesktop.swingx.JXTable manageEmployeeTbl) {
        this.manageEmployeeTbl = manageEmployeeTbl;
    }

    public void setEmployeeId() {
        if (this.availableEmployees.size() > 0) {
            try {
                this.addEmployeeIdTxt1.setText(Utilities.convertKeyToString((int) (EmployeeCreditController.getLastEmployeeId() + 1), (String) "employee"));
            } catch (SQLException ex) {
                logger.error((Object) ex);
                this.addEmployeeIdTxt1.setText(Utilities.convertKeyToString((int) (this.availableEmployees.get(this.availableEmployees.size() - 1).getEmployeeId() + 1), (String) "employee"));
            }
        } else if (this.availableEmployees.isEmpty()) {
            this.addEmployeeIdTxt1.setText(Utilities.convertKeyToString((int) 1, (String) "employee"));
        }
    }

    public void editEmployeeDetails(int i) {
        try {
            EmployeeCreditController.getDetails((int) i);
            CardLayout card = (CardLayout) this.cardPanel.getLayout();
            card.show(this.cardPanel, "editEmployee");
            this.editEmployeeFullNameTxt2.setText(EmployeeCreditController.getDetails((int) i).getEmployeeName());
            this.editEmployeeIdTxt2.setText(Integer.toString(EmployeeCreditController.getDetails((int) i).getEmployeeId()));
            this.editEmployeePositionTxt1.setText(EmployeeCreditController.getDetails((int) i).getPosition());
        } catch (SQLException ex) {
            logger.debug((Object) ("SQLException = " + ex.getMessage()));
            Utilities.showMsgBox((String) ex.getMessage(), (String) "Error", (int) 2);
            return;
        }
    }

    public void setEditDetails() {
        boolean isValid = true;
        int EmployeeId = 0;
        if (this.validateName(this.editEmployeeFullNameTxt2.getText()) && this.editEmployeeFullNameTxt2.getText() != null) {
            isValid = false;
            if (isValid) {
                logger.error((Object) "this name exists");
                Utilities.showMsgBox((String) "This user name already taken", (String) " Error", (int) 2);
                this.editEmployeeFullNameTxt2.setText("");
                this.editEmployeeFullNameTxt2.requestFocus();
                return;
            }
            logger.info((Object) "valid name entered");
        } else {
            logger.debug((Object) "invalid detail or empty block found");
            Utilities.showMsgBox((String) "Give a valid name", (String) " Error", (int) 2);
            return;
        }
        logger.info((Object) "valid name");
        if (!(this.validateName(this.editEmployeePositionTxt1.getText()) && this.editEmployeePositionTxt1.getText() != null)) {
            logger.debug((Object) "invalid detail or empty block found");
            Utilities.showMsgBox((String) "Give a valid position", (String) " Error", (int) 2);
            return;
        }
        logger.info((Object) "valid position");
        Object[] ob = new Object[]{this.editEmployeeFullNameTxt2.getText(), this.editEmployeePositionTxt1.getText()};
        try {
            EmployeeId = Integer.parseInt(this.editEmployeeIdTxt2.getText());
        } catch (NumberFormatException x) {
            Utilities.showMsgBox((String) "System Error", (String) "Error", (int) 2);
        }
        this.loadTableModel();
        CardLayout card = (CardLayout) this.cardPanel.getLayout();
        card.show(this.cardPanel, "manageEmployees");
        this.editEmployeeFullNameTxt2.setText(null);
        this.editEmployeeIdTxt2.setText(null);
        this.editEmployeePositionTxt1.setText(null);
    }

    public void deleteEmployeeDetails(int i) {
        try {
            EmployeeCreditController.deleteDeatils((int) i);
            this.loadTableModel();
        } catch (SQLException ex) {
            logger.debug((Object) ("SQLException = " + ex.getMessage()));
            Utilities.showMsgBox((String) ex.getMessage(), (String) "Error", (int) 2);

        }
    }

}
