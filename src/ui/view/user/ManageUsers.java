/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.user;

import controller.credit.EmployeeCreditController;
import controller.user.UserController;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;
import model.creditManagement.Employee;
import model.people.User;
import net.proteanit.sql.DbUtils;
import ui.view.credit.FinalCredit;
import util.PasswordFilter;
import util.Utilities;

/**
 *
 * @author HP
 */
public class ManageUsers extends javax.swing.JInternalFrame {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FinalCredit.class);

    DefaultComboBoxModel addUserFullNameComboBox1Model;

   // private HashMap<Integer, User> users;
    /**
     * Creates new form ManageEmployees
     */
    public ManageUsers() {
        initComponents();
        addUserFullNameComboBox1Model = (DefaultComboBoxModel) comboModelUsers.getModel();
        loadDetails();
        chngTblModel();
        userTable.setSelectionMode(0);
        
           ((PlainDocument) addUserPasswordTxt.getDocument()).setDocumentFilter(new PasswordFilter());
       ((PlainDocument) addUserUserNameTxt1.getDocument()).setDocumentFilter(new PasswordFilter());
      ((PlainDocument) editUserUserNameTxt2.getDocument()).setDocumentFilter(new PasswordFilter());
       ((PlainDocument) editUserPasswordTxt1.getDocument()).setDocumentFilter(new PasswordFilter());
    
    }

    public void loadDetails() {
        logger.debug("loadDetails method invoked");
        try {
            addUserFullNameComboBox1Model.removeAllElements();
            ArrayList<User> userDetails = UserController.getAllUsers();

            for (User user : userDetails) {

                addUserFullNameComboBox1Model.addElement(user.getUserName());
            }
            addUserFullNameComboBox1Model.setSelectedItem(null);
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    public boolean validateName(String txt) {
        logger.debug("validateName invoked");

        String regx = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();

    }

    public void addUserValidation() {
        //Get password char array from 'txtPassword' password field

//Get hashed password string
        logger.debug("addUserValidations method invoked");
//checkvalidations
        String hashedPassword = "";
        String hashedcnfrmPassword = "";
        boolean isValid = false;
       
      /*  if (addUserUserNameTxt1.getText() != null && validateName(addUserUserNameTxt1.getText()) ) {
            try {
                isValid = UserController.checkDataExistence(addUserUserNameTxt1.getText());
                if(!isValid){
                    logger.info("valid name entered");
                }
                else{
                    logger.error("this name exists");
                    Utilities.showMsgBox("This user name already taken", " Error", JOptionPane.WARNING_MESSAGE);
                    addUserUserNameTxt1.setText("");
                    addUserUserNameTxt1.requestFocus();
                    return;
                    
                }
            } catch (SQLException ex) {
                logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
               
            }
        } else {
            logger.error("invalid name");
            Utilities.showMsgBox("invalid name", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        */
           if (this.addUserUserNameTxt1.getText() != null && !this.addUserUserNameTxt1.getText().trim().equals("") ) {
            try {
                isValid = UserController.checkDataExistence((String) this.addUserUserNameTxt1.getText().trim());
            } catch (SQLException ex) {
                logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
                  }
                if (isValid) {
                    logger.error((Object) "this name exists");
                    Utilities.showMsgBox((String) "This user name already taken", (String) " Error", (int) 2);
                    this.addUserUserNameTxt1.setText("");
                    this.addUserUserNameTxt1.requestFocus();
                    return;
                }
                logger.info((Object) "valid name entered");
            } else {
                logger.debug((Object) "invalid detail or empty block found");
                Utilities.showMsgBox((String) "Give a valid name", (String) " Error", (int) 2);
                this.addUserUserNameTxt1.setText("");
                this.addUserUserNameTxt1.requestFocus();
                return;
            }
            logger.info((Object) "valid name");
        char[] password;
        password = addUserPasswordTxt.getPassword();
        
        if (addUserPasswordTxt.getPassword() != null ) {
            hashedPassword = Utilities.getSHA1(password);
            
            logger.info("valid password");
            if(hashedPassword.length()<5){
                
                Utilities.showMsgBox("Password strength is weak","Password Error", WIDTH);
            }
        } else {
            logger.error(" empty block found");
            Utilities.showMsgBox("Give a password", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.addUserPasswordTxt.getPassword() == null ) {
                logger.debug((Object) "invalid detail or empty block found");
                Utilities.showMsgBox((String) "Give a valid password", (String) " Error", (int) 2);
                return;
            }
            logger.info((Object) "valid password");
        char[] passwordCnfrm;
        passwordCnfrm = addUserPasswordTxt.getPassword();
        if (addUserPasswordTxt.getPassword() != null) {
            hashedcnfrmPassword = Utilities.getSHA1(passwordCnfrm);
            logger.info("valid cnfrmpassword");
        } else {
            logger.error(" empty block found");
            Utilities.showMsgBox("Give a password", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (hashedPassword.equals(hashedcnfrmPassword)) {

            logger.info("confirm password");
        } else {
            logger.error("not the same password.");
            Utilities.showMsgBox("Recheck the password", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (addUserUserLevelComboBox1.getSelectedItem() != null) {

            addUser();
        } else {
            logger.error("user level not selected");
            Utilities.showMsgBox("Recheck the password", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    private void addUser() {
        logger.debug("addUser method invoked");
        try {

            User user = new User(
                    addUserUserNameTxt1.getText(),
                    Utilities.getSHA1(addUserPasswordTxt.getPassword()),
                    addUserUserLevelComboBox1.getSelectedItem().toString(),
                    false
            );
            logger.info("details loaded to an userobject");
            System.out.println(addUserUserLevelComboBox1.getSelectedItem().toString());
            UserController.addUser(user);
            logger.info("new user added");
            Utilities.showMsgBox("New user details added to the database", "Confirm", JOptionPane.WARNING_MESSAGE);

        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        chngTblModel();
          CardLayout card = (CardLayout) cardPanel.getLayout();
            card.show(cardPanel, "manageUsers");
        addUserUserNameTxt1.setText("");
        addUserPasswordTxt.setText("");
        addUserPasswordTxtcnfrm.setText("");
        

    }

    public void showUserEditDetails(String name) {
        logger.debug("showUserEditDetails method invoked");

        User user = null;
        try {
            user = UserController.getUser("user_name", name);
        } catch (SQLException ex) {
            Logger.getLogger(ManageUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (user != null) {
            editUserUserNameTxt2.setText(user.getUserName());
           jLabel1.setText(user.getPassword());

            editUserUserLevelComboBox2.setSelectedItem(user.getUserType());
           

           CardLayout card = (CardLayout) cardPanel.getLayout();
            card.show(cardPanel, "editUser");
            

        } else {
            return;
        }
    }

    public void editUser() {
        logger.debug("editUser method invoked");
        boolean isValid = true;
        try {
            if (editUserUserNameTxt2.getText() != null && validateName(editUserUserNameTxt2.getText())) {
                isValid = UserController.checkDataExistence(editUserUserNameTxt2.getText());
                if(!isValid){
                    logger.info("valid name entered");
                }
                else{
                    logger.error("this name exists");
                    Utilities.showMsgBox("This user name already taken", " Error", JOptionPane.WARNING_MESSAGE);
                    editUserUserNameTxt2.setText("");
                    editUserUserNameTxt2.requestFocus();
                    return;
                    
                }
            logger.info("valid name entered");
        } else {
            logger.error("invalid name");
            Utilities.showMsgBox("invalid name", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
            String newPassword="";
            char[] password;
        password = editUserPasswordTxt1.getPassword();
        if (editUserPasswordTxt1.getPassword() != null) {
            newPassword = Utilities.getSHA1(password);
            logger.info("valid password");
            if(newPassword == jLabel1.getText()){
                logger.info("password matches with the database");  
            }
            else{
                logger.error("Not matches with previous password");
            Utilities.showMsgBox("Incorrect password entered.", " Error", JOptionPane.WARNING_MESSAGE);
            return;
            }
        } else {
            logger.error(" empty block found");
            Utilities.showMsgBox("Give a password", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
            
            //All edit details are varified before this method
            //   int customerId = Integer.parseInt(editCustomerCoopIdTxt2.getText());
            User user = new User(
                    editUserUserNameTxt2.getText(),
                    newPassword,
                    editUserUserLevelComboBox2.getSelectedItem().toString(),
                    false
            );

            boolean result = UserController.editUser(user);
            if (result) {
                Utilities.showMsgBox("Changes saved", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                logger.error("Changes not saved");
            }
            chngTblModel();

            editUserUserNameTxt2.setText("");
            editUserPasswordTxt1.setText("");

        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    public void chngTblModel() {
        logger.debug("chngTblMdl method invoked");
        try {
            userTable.setModel(DbUtils.resultSetToTableModel(UserController.loadDetails()));
            logger.info("user details loaded to the table.");
        } catch (SQLException ex) {
            logger.debug("SQLException = " + ex.getMessage());
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        parentPanel = new javax.swing.JPanel();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        jXTaskPane1 = new org.jdesktop.swingx.JXTaskPane();
        taskPaneSearchUserBtn = new javax.swing.JButton();
        jXTaskPane2 = new org.jdesktop.swingx.JXTaskPane();
        taskPaneUserDetailsBtn = new javax.swing.JButton();
        taskPaneAddUserBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        userSearch = new javax.swing.JPanel();
        subCardPanel = new javax.swing.JPanel();
        userSearchPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        userSearchUserNameLbl = new javax.swing.JLabel();
        userSeachUserSearchBtn = new javax.swing.JButton();
        customerSearchLBl2 = new javax.swing.JLabel();
        comboModelUsers = new javax.swing.JComboBox();
        userSearchResultPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        userSearchUserName = new javax.swing.JLabel();
        userDeatilsUserLevelLbl = new javax.swing.JLabel();
        userDetailsAdministratorRadioBtn = new javax.swing.JRadioButton();
        userDetailsManagerRadioBtn = new javax.swing.JRadioButton();
        userDetailsUserNameTxt = new javax.swing.JLabel();
        userDetailsLevel1RadioBtn = new javax.swing.JRadioButton();
        installmentPaymentLbl2 = new javax.swing.JLabel();
        userDetailsOkjButton1 = new javax.swing.JButton();
        addUserPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        addUserActivateBtn1 = new org.jdesktop.swingx.JXButton();
        addUserUserNameLbl2 = new javax.swing.JLabel();
        addUserPasswordLbl2 = new javax.swing.JLabel();
        addUserUserLevelLbl2 = new javax.swing.JLabel();
        addUserOnlineUsersLbl2 = new javax.swing.JLabel();
        addUserUserNameTxt1 = new javax.swing.JTextField();
        addUserUserLevelComboBox1 = new javax.swing.JComboBox();
        jScrollPane6 = new javax.swing.JScrollPane();
        addUserOnlineUsersTxt1 = new javax.swing.JTextArea();
        customerSearchLBl = new javax.swing.JLabel();
        addUserPasswordTxt = new javax.swing.JPasswordField();
        addUserPasswordLblcnfrm = new javax.swing.JLabel();
        addUserPasswordTxtcnfrm = new javax.swing.JPasswordField();
        addUserCancelBtn = new org.jdesktop.swingx.JXButton();
        manageUsersPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        userTable = new org.jdesktop.swingx.JXTable();
        manageUserEditPanel = new javax.swing.JPanel();
        editUserBtn3 = new org.jdesktop.swingx.JXButton();
        deleteUserBtn3 = new org.jdesktop.swingx.JXButton();
        customerSearchLBl1 = new javax.swing.JLabel();
        editUserPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        editUserSaveBtn = new org.jdesktop.swingx.JXButton();
        editUserUserNameLbl3 = new javax.swing.JLabel();
        editUserPasswordLbl3 = new javax.swing.JLabel();
        editUserUserLevelLbl3 = new javax.swing.JLabel();
        editUserUserNameTxt2 = new javax.swing.JTextField();
        editUserUserLevelComboBox2 = new javax.swing.JComboBox();
        customerSearchLBl3 = new javax.swing.JLabel();
        editUserPasswordTxt1 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        editUserCancelBtn1 = new org.jdesktop.swingx.JXButton();

        setClosable(true);

        parentPanel.setFocusable(false);

        jXTaskPaneContainer1.setBackground(new java.awt.Color(102, 102, 102));
        jXTaskPaneContainer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jXTaskPane1.setTitle("Search");
        jXTaskPane1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jXTaskPane1.getContentPane().setLayout(new org.jdesktop.swingx.VerticalLayout());

        taskPaneSearchUserBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        taskPaneSearchUserBtn.setText("Search User");
        taskPaneSearchUserBtn.setFocusable(false);
        taskPaneSearchUserBtn.setPreferredSize(new java.awt.Dimension(90, 29));
        taskPaneSearchUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taskPaneSearchUserBtnActionPerformed(evt);
            }
        });
        jXTaskPane1.getContentPane().add(taskPaneSearchUserBtn);

        jXTaskPane2.setTitle("Manage Users");
        jXTaskPane2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jXTaskPane2.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        taskPaneUserDetailsBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        taskPaneUserDetailsBtn.setText("User Details");
        taskPaneUserDetailsBtn.setFocusable(false);
        taskPaneUserDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taskPaneUserDetailsBtnActionPerformed(evt);
            }
        });
        jXTaskPane2.getContentPane().add(taskPaneUserDetailsBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 126, -1));

        taskPaneAddUserBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        taskPaneAddUserBtn.setText("Add User");
        taskPaneAddUserBtn.setFocusable(false);
        taskPaneAddUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taskPaneAddUserBtnActionPerformed(evt);
            }
        });
        jXTaskPane2.getContentPane().add(taskPaneAddUserBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 126, -1));

        javax.swing.GroupLayout jXTaskPaneContainer1Layout = new javax.swing.GroupLayout(jXTaskPaneContainer1);
        jXTaskPaneContainer1.setLayout(jXTaskPaneContainer1Layout);
        jXTaskPaneContainer1Layout.setHorizontalGroup(
            jXTaskPaneContainer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXTaskPaneContainer1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jXTaskPaneContainer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jXTaskPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXTaskPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                .addContainerGap())
        );
        jXTaskPaneContainer1Layout.setVerticalGroup(
            jXTaskPaneContainer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXTaskPaneContainer1Layout.createSequentialGroup()
                .addComponent(jXTaskPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jXTaskPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        cardPanel.setPreferredSize(new java.awt.Dimension(1075, 978));
        cardPanel.setLayout(new java.awt.CardLayout());

        userSearch.setPreferredSize(new java.awt.Dimension(619, 551));

        subCardPanel.setPreferredSize(new java.awt.Dimension(1140, 690));
        subCardPanel.setLayout(new java.awt.CardLayout());

        userSearchPanel.setToolTipText("");
        userSearchPanel.setName("paymentHistory");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        userSearchUserNameLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        userSearchUserNameLbl.setText("User  Name");

        userSeachUserSearchBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        userSeachUserSearchBtn.setText("Search");
        userSeachUserSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSeachUserSearchBtnActionPerformed(evt);
            }
        });

        customerSearchLBl2.setBackground(new java.awt.Color(204, 204, 204));
        customerSearchLBl2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customerSearchLBl2.setText("Search");
        customerSearchLBl2.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        customerSearchLBl2.setOpaque(true);

        comboModelUsers.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        comboModelUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboModelUsersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(customerSearchLBl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userSeachUserSearchBtn)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(userSearchUserNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboModelUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(customerSearchLBl2)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userSearchUserNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboModelUsers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(userSeachUserSearchBtn)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout userSearchPanelLayout = new javax.swing.GroupLayout(userSearchPanel);
        userSearchPanel.setLayout(userSearchPanelLayout);
        userSearchPanelLayout.setHorizontalGroup(
            userSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userSearchPanelLayout.createSequentialGroup()
                .addGap(270, 270, 270)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        userSearchPanelLayout.setVerticalGroup(
            userSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userSearchPanelLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        subCardPanel.add(userSearchPanel, "userSearch");

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        userSearchUserName.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        userSearchUserName.setText("User Name");

        userDeatilsUserLevelLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        userDeatilsUserLevelLbl.setText("User Level");

        buttonGroup1.add(userDetailsAdministratorRadioBtn);
        userDetailsAdministratorRadioBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        userDetailsAdministratorRadioBtn.setText("Administrator");

        buttonGroup1.add(userDetailsManagerRadioBtn);
        userDetailsManagerRadioBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        userDetailsManagerRadioBtn.setText("Manager");

        userDetailsUserNameTxt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        buttonGroup1.add(userDetailsLevel1RadioBtn);
        userDetailsLevel1RadioBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        userDetailsLevel1RadioBtn.setText("Level 1");

        installmentPaymentLbl2.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl2.setText("User Details");
        installmentPaymentLbl2.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl2.setOpaque(true);

        userDetailsOkjButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        userDetailsOkjButton1.setText("OK");
        userDetailsOkjButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userDetailsOkjButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(installmentPaymentLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(userDeatilsUserLevelLbl)
                        .addGap(43, 43, 43)
                        .addComponent(userDetailsManagerRadioBtn)
                        .addGap(40, 40, 40)
                        .addComponent(userDetailsAdministratorRadioBtn)
                        .addGap(40, 40, 40)
                        .addComponent(userDetailsLevel1RadioBtn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(userSearchUserName)
                        .addGap(40, 40, 40)
                        .addComponent(userDetailsUserNameTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(userDetailsOkjButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(installmentPaymentLbl2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(userSearchUserName)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(userDetailsUserNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userDetailsManagerRadioBtn)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(userDetailsAdministratorRadioBtn, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(userDeatilsUserLevelLbl))
                    .addComponent(userDetailsLevel1RadioBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(userDetailsOkjButton1)
                .addGap(30, 30, 30))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {userDeatilsUserLevelLbl, userDetailsUserNameTxt, userSearchUserName});

        javax.swing.GroupLayout userSearchResultPanelLayout = new javax.swing.GroupLayout(userSearchResultPanel);
        userSearchResultPanel.setLayout(userSearchResultPanelLayout);
        userSearchResultPanelLayout.setHorizontalGroup(
            userSearchResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userSearchResultPanelLayout.createSequentialGroup()
                .addGap(220, 220, 220)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(214, Short.MAX_VALUE))
        );
        userSearchResultPanelLayout.setVerticalGroup(
            userSearchResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userSearchResultPanelLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(359, Short.MAX_VALUE))
        );

        subCardPanel.add(userSearchResultPanel, "userSearchResult");

        javax.swing.GroupLayout userSearchLayout = new javax.swing.GroupLayout(userSearch);
        userSearch.setLayout(userSearchLayout);
        userSearchLayout.setHorizontalGroup(
            userSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 924, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        userSearchLayout.setVerticalGroup(
            userSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        cardPanel.add(userSearch, "userSearchDetails");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        addUserActivateBtn1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addUserActivateBtn1.setText("Activate");
        addUserActivateBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addUserActivateBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserActivateBtn1ActionPerformed(evt);
            }
        });

        addUserUserNameLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addUserUserNameLbl2.setText("User Name");

        addUserPasswordLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addUserPasswordLbl2.setText("Password");

        addUserUserLevelLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addUserUserLevelLbl2.setText("User Level");

        addUserOnlineUsersLbl2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addUserOnlineUsersLbl2.setText("Online Users");

        addUserUserNameTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addUserUserNameTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserUserNameTxt1ActionPerformed(evt);
            }
        });

        addUserUserLevelComboBox1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addUserUserLevelComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Manager", "Cashier", "Inventory" }));
        addUserUserLevelComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserUserLevelComboBox1ActionPerformed(evt);
            }
        });

        addUserOnlineUsersTxt1.setEditable(false);
        addUserOnlineUsersTxt1.setColumns(20);
        addUserOnlineUsersTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addUserOnlineUsersTxt1.setRows(5);
        jScrollPane6.setViewportView(addUserOnlineUsersTxt1);

        customerSearchLBl.setBackground(new java.awt.Color(204, 204, 204));
        customerSearchLBl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customerSearchLBl.setText("Add User");
        customerSearchLBl.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        customerSearchLBl.setOpaque(true);

        addUserPasswordTxt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        addUserPasswordLblcnfrm.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addUserPasswordLblcnfrm.setText("Confirm Password");

        addUserPasswordTxtcnfrm.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        addUserCancelBtn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addUserCancelBtn.setText("Cancel");
        addUserCancelBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addUserCancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserCancelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(customerSearchLBl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(addUserPasswordLblcnfrm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addUserUserLevelLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addUserPasswordLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addUserUserNameLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addUserOnlineUsersLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(addUserCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(addUserUserLevelComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addUserPasswordTxtcnfrm, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addUserPasswordTxt, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addUserUserNameTxt1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(152, 152, 152)
                                .addComponent(addUserActivateBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(customerSearchLBl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addUserUserNameLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addUserUserNameTxt1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addUserPasswordLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addUserPasswordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addUserPasswordLblcnfrm, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addUserPasswordTxtcnfrm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addUserUserLevelComboBox1)
                    .addComponent(addUserUserLevelLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addUserOnlineUsersLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addUserActivateBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addUserCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout addUserPanelLayout = new javax.swing.GroupLayout(addUserPanel);
        addUserPanel.setLayout(addUserPanelLayout);
        addUserPanelLayout.setHorizontalGroup(
            addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addUserPanelLayout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(249, Short.MAX_VALUE))
        );
        addUserPanelLayout.setVerticalGroup(
            addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addUserPanelLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jPanel2.getAccessibleContext().setAccessibleParent(cardPanel);

        cardPanel.add(addUserPanel, "addUser");

        manageUsersPanel.setPreferredSize(new java.awt.Dimension(1075, 605));

        userTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User  Name", "User Level "
            }
        ));
        userTable.setMinimumSize(new java.awt.Dimension(45, 0));
        userTable.setPreferredScrollableViewportSize(new java.awt.Dimension(165, 360));
        userTable.setShowGrid(true);
        jScrollPane7.setViewportView(userTable);

        manageUserEditPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        manageUserEditPanel.setPreferredSize(new java.awt.Dimension(135, 107));

        editUserBtn3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editUserBtn3.setText("Edit");
        editUserBtn3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editUserBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUserBtn3ActionPerformed(evt);
            }
        });

        deleteUserBtn3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        deleteUserBtn3.setText("Delete");
        deleteUserBtn3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteUserBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUserBtn3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout manageUserEditPanelLayout = new javax.swing.GroupLayout(manageUserEditPanel);
        manageUserEditPanel.setLayout(manageUserEditPanelLayout);
        manageUserEditPanelLayout.setHorizontalGroup(
            manageUserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageUserEditPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(manageUserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editUserBtn3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteUserBtn3, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        manageUserEditPanelLayout.setVerticalGroup(
            manageUserEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageUserEditPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(editUserBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteUserBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        customerSearchLBl1.setBackground(new java.awt.Color(204, 204, 204));
        customerSearchLBl1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customerSearchLBl1.setText("Manage User");
        customerSearchLBl1.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        customerSearchLBl1.setOpaque(true);

        javax.swing.GroupLayout manageUsersPanelLayout = new javax.swing.GroupLayout(manageUsersPanel);
        manageUsersPanel.setLayout(manageUsersPanelLayout);
        manageUsersPanelLayout.setHorizontalGroup(
            manageUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manageUsersPanelLayout.createSequentialGroup()
                .addGroup(manageUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(customerSearchLBl1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(manageUsersPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(manageUserEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        manageUsersPanelLayout.setVerticalGroup(
            manageUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manageUsersPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(customerSearchLBl1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(manageUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageUserEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        cardPanel.add(manageUsersPanel, "manageUsers");

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        editUserSaveBtn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editUserSaveBtn.setText("Save");
        editUserSaveBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editUserSaveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUserSaveBtnActionPerformed(evt);
            }
        });

        editUserUserNameLbl3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editUserUserNameLbl3.setText("User Name");

        editUserPasswordLbl3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editUserPasswordLbl3.setText("Password");

        editUserUserLevelLbl3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editUserUserLevelLbl3.setText("User Level");

        editUserUserNameTxt2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editUserUserNameTxt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUserUserNameTxt2ActionPerformed(evt);
            }
        });

        editUserUserLevelComboBox2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        editUserUserLevelComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MANAGER", "CASHIER", "INVENTORY" }));
        editUserUserLevelComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUserUserLevelComboBox2ActionPerformed(evt);
            }
        });

        customerSearchLBl3.setBackground(new java.awt.Color(204, 204, 204));
        customerSearchLBl3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customerSearchLBl3.setText("Edit User");
        customerSearchLBl3.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        customerSearchLBl3.setOpaque(true);

        editUserPasswordTxt1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N

        jLabel1.setForeground(new java.awt.Color(240, 240, 240));
        jLabel1.setText("jLabel1");
        jLabel1.setOpaque(true);

        editUserCancelBtn1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editUserCancelBtn1.setText("Cancel");
        editUserCancelBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editUserCancelBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUserCancelBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(customerSearchLBl3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(editUserUserLevelLbl3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(editUserPasswordLbl3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(editUserUserNameLbl3, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(editUserUserLevelComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, 252, Short.MAX_VALUE)
                                    .addComponent(editUserPasswordTxt1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(editUserUserNameTxt2, javax.swing.GroupLayout.Alignment.LEADING)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(editUserCancelBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editUserSaveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editUserCancelBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(customerSearchLBl3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editUserUserNameLbl3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editUserUserNameTxt2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editUserPasswordLbl3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editUserPasswordTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editUserUserLevelComboBox2)
                            .addComponent(editUserUserLevelLbl3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(editUserSaveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout editUserPanel1Layout = new javax.swing.GroupLayout(editUserPanel1);
        editUserPanel1.setLayout(editUserPanel1Layout);
        editUserPanel1Layout.setHorizontalGroup(
            editUserPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editUserPanel1Layout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(282, Short.MAX_VALUE))
        );
        editUserPanel1Layout.setVerticalGroup(
            editUserPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editUserPanel1Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(318, Short.MAX_VALUE))
        );

        cardPanel.add(editUserPanel1, "editUser");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 924, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout parentPanelLayout = new javax.swing.GroupLayout(parentPanel);
        parentPanel.setLayout(parentPanelLayout);
        parentPanelLayout.setHorizontalGroup(
            parentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        parentPanelLayout.setVerticalGroup(
            parentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parentPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jXTaskPaneContainer1.getAccessibleContext().setAccessibleParent(this);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(parentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(parentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addUserActivateBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserActivateBtn1ActionPerformed
        addUserValidation();

    }//GEN-LAST:event_addUserActivateBtn1ActionPerformed

    private void addUserUserNameTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserUserNameTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addUserUserNameTxt1ActionPerformed

    private void addUserUserLevelComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserUserLevelComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addUserUserLevelComboBox1ActionPerformed

    private void taskPaneSearchUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taskPaneSearchUserBtnActionPerformed
        // TODO add your handling code here:
        cleanUi();
        loadDetails();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "userSearchDetails");
    }//GEN-LAST:event_taskPaneSearchUserBtnActionPerformed

    private void taskPaneAddUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taskPaneAddUserBtnActionPerformed
        //try {
            cleanUi();
            //addUserOnlineUsersTxt1.setText(UserController.getLoggedInUsers().getUserName());

            CardLayout card = (CardLayout) cardPanel.getLayout();
            card.show(cardPanel, "addUser");
        //} catch (SQLException ex) {
           // logger.error("SQL exception has occured");
           // Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
           // return;
        //}
    }//GEN-LAST:event_taskPaneAddUserBtnActionPerformed

    private void taskPaneUserDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taskPaneUserDetailsBtnActionPerformed
        // TODO add your handling code here:
        cleanUi();
        chngTblModel();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "manageUsers");
    }//GEN-LAST:event_taskPaneUserDetailsBtnActionPerformed

    private void editUserBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUserBtn3ActionPerformed
        // TODO add your handling code here:
        int row = userTable.getSelectedRow();
        if (row > -1) {
            System.out.println("Selected customer row : " + row);
            showUserEditDetails(userTable.getValueAt(row, 0).toString());
        } else {
            Utilities.showMsgBox("Select a row to edit details", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }//GEN-LAST:event_editUserBtn3ActionPerformed
    public void cleanUi() {
        // comboModelUsers.setSelectedItem(null);
        userDetailsUserNameTxt.setText("");
        userDetailsManagerRadioBtn.setSelected(false);
        userDetailsLevel1RadioBtn.setSelected(false);
        userDetailsAdministratorRadioBtn.setSelected(false);
        // addUserFullNameComboBox1Model.setSelectedItem(null);
        addUserOnlineUsersTxt1.setText("");
        addUserPasswordTxt.setText("");
        addUserPasswordTxtcnfrm.setText("");
        addUserUserNameTxt1.setText("");
        editUserPasswordTxt1.setText("");

        editUserUserNameTxt2.setText("");
        editUserUserLevelComboBox2.setSelectedItem(null);

    }

    public void deleteUser(String i) {
        logger.debug("deleteUser method invoked");
        try {
            UserController.deleteDeatils(i);
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        chngTblModel();
    }

    private void deleteUserBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUserBtn3ActionPerformed
        // TODO add your handling code here:
        int row = userTable.getSelectedRow();
        if (row > -1) {
            System.out.println("Selected customer row : " + row);
            deleteUser(userTable.getValueAt(row, 0).toString());
        } else {
            Utilities.showMsgBox("Select a row to delete", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }//GEN-LAST:event_deleteUserBtn3ActionPerformed

    private void userSeachUserSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSeachUserSearchBtnActionPerformed
        cleanUi();
        
        try {
            userDetailsManagerRadioBtn.setSelected(false);
            userDetailsLevel1RadioBtn.setSelected(false);
            userDetailsAdministratorRadioBtn.setSelected(false);
            // TODO add your handling code here:
            userDetailsUserNameTxt.setText(comboModelUsers.getSelectedItem().toString());
            User user = UserController.getUser("user_name", comboModelUsers.getSelectedItem().toString());
            if (user != null) {
                userDetailsUserNameTxt.setText(user.getUserName());
                if (null != user.getUserType()) {
                    switch (user.getUserType()) {
                        case "MANAGER":
                            userDetailsManagerRadioBtn.setSelected(true);
                            userDetailsLevel1RadioBtn.setSelected(false);
                            userDetailsAdministratorRadioBtn.setSelected(false);
                            break;
                        case "CASHIER":
                            userDetailsManagerRadioBtn.setSelected(false);
                            userDetailsLevel1RadioBtn.setSelected(true);
                            userDetailsAdministratorRadioBtn.setSelected(false);
                            break;
                        case "INVENTORY":
                            userDetailsManagerRadioBtn.setSelected(false);
                            userDetailsLevel1RadioBtn.setSelected(false);
                            userDetailsAdministratorRadioBtn.setSelected(true);
                            break;
                    }
                }
            } else {
                Utilities.showMsgBox("SystemError", "Error", JOptionPane.WARNING_MESSAGE);
            }
            CardLayout card2 = (CardLayout) subCardPanel.getLayout();
            card2.show(subCardPanel, "userSearchResult");
            // userNameComboBox1.setSelectedItem(null);
        } catch (SQLException ex) {
            logger.error("SQL exception has occured");
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_userSeachUserSearchBtnActionPerformed

    private void editUserSaveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUserSaveBtnActionPerformed
        // TODO add your handling code here:
        if (editUserUserNameTxt2.getText() != null && validateName(editUserUserNameTxt2.getText())) {
            logger.info("valid name entered");
        } else {
            logger.error("invalid name");
            Utilities.showMsgBox("invalid name", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (editUserPasswordTxt1.getPassword() != null) {
            logger.info("valid password");
        } else {
            logger.error(" empty block found");
            Utilities.showMsgBox("Give a password", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
       
        if (editUserPasswordTxt1.getPassword() != null) {
            logger.info("valid password");
        } else {
            logger.error(" empty block found");
            Utilities.showMsgBox("Give a password", " Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
       

        editUser();
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "customerDetails");

    }//GEN-LAST:event_editUserSaveBtnActionPerformed

    private void editUserUserNameTxt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUserUserNameTxt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editUserUserNameTxt2ActionPerformed

    private void editUserUserLevelComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUserUserLevelComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editUserUserLevelComboBox2ActionPerformed

    private void addUserCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserCancelBtnActionPerformed
        // TODO add your handling code here:
        addUserPasswordTxt.setText("");
        addUserUserNameTxt1.setText("");
        addUserUserLevelComboBox1.setSelectedItem(null);
        addUserPasswordTxtcnfrm.setText("");
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "userSearchDetails");

    }//GEN-LAST:event_addUserCancelBtnActionPerformed

    private void userDetailsOkjButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userDetailsOkjButton1ActionPerformed
        // TODO add your handling code here:
        userDetailsUserNameTxt.setText("");
        userDetailsManagerRadioBtn.setSelected(false);
        userDetailsAdministratorRadioBtn.setSelected(false);
        userDetailsLevel1RadioBtn.setSelected(false);
        logger.debug("debugging");
        CardLayout card2 = (CardLayout) subCardPanel.getLayout();
        card2.show(subCardPanel, "userSearch");
    }//GEN-LAST:event_userDetailsOkjButton1ActionPerformed

    private void editUserCancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUserCancelBtn1ActionPerformed
        // TODO add your handling code here:
         editUserUserNameTxt2.setText("");
            editUserPasswordTxt1.setText("");
             CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "manageUsers");


    }//GEN-LAST:event_editUserCancelBtn1ActionPerformed

    private void comboModelUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboModelUsersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboModelUsersActionPerformed

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
            java.util.logging.Logger.getLogger(ManageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ManageUsers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXButton addUserActivateBtn1;
    private org.jdesktop.swingx.JXButton addUserCancelBtn;
    private javax.swing.JLabel addUserOnlineUsersLbl2;
    private javax.swing.JTextArea addUserOnlineUsersTxt1;
    private javax.swing.JPanel addUserPanel;
    private javax.swing.JLabel addUserPasswordLbl2;
    private javax.swing.JLabel addUserPasswordLblcnfrm;
    private javax.swing.JPasswordField addUserPasswordTxt;
    private javax.swing.JPasswordField addUserPasswordTxtcnfrm;
    private javax.swing.JComboBox addUserUserLevelComboBox1;
    private javax.swing.JLabel addUserUserLevelLbl2;
    private javax.swing.JLabel addUserUserNameLbl2;
    private javax.swing.JTextField addUserUserNameTxt1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JComboBox comboModelUsers;
    private javax.swing.JLabel customerSearchLBl;
    private javax.swing.JLabel customerSearchLBl1;
    private javax.swing.JLabel customerSearchLBl2;
    private javax.swing.JLabel customerSearchLBl3;
    private org.jdesktop.swingx.JXButton deleteUserBtn3;
    private org.jdesktop.swingx.JXButton editUserBtn3;
    private org.jdesktop.swingx.JXButton editUserCancelBtn1;
    private javax.swing.JPanel editUserPanel1;
    private javax.swing.JLabel editUserPasswordLbl3;
    private javax.swing.JPasswordField editUserPasswordTxt1;
    private org.jdesktop.swingx.JXButton editUserSaveBtn;
    private javax.swing.JComboBox editUserUserLevelComboBox2;
    private javax.swing.JLabel editUserUserLevelLbl3;
    private javax.swing.JLabel editUserUserNameLbl3;
    private javax.swing.JTextField editUserUserNameTxt2;
    private javax.swing.JLabel installmentPaymentLbl2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane2;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    private javax.swing.JPanel manageUserEditPanel;
    private javax.swing.JPanel manageUsersPanel;
    private javax.swing.JPanel parentPanel;
    private javax.swing.JPanel subCardPanel;
    private javax.swing.JButton taskPaneAddUserBtn;
    private javax.swing.JButton taskPaneSearchUserBtn;
    private javax.swing.JButton taskPaneUserDetailsBtn;
    private javax.swing.JLabel userDeatilsUserLevelLbl;
    private javax.swing.JRadioButton userDetailsAdministratorRadioBtn;
    private javax.swing.JRadioButton userDetailsLevel1RadioBtn;
    private javax.swing.JRadioButton userDetailsManagerRadioBtn;
    private javax.swing.JButton userDetailsOkjButton1;
    private javax.swing.JLabel userDetailsUserNameTxt;
    private javax.swing.JButton userSeachUserSearchBtn;
    private javax.swing.JPanel userSearch;
    private javax.swing.JPanel userSearchPanel;
    private javax.swing.JPanel userSearchResultPanel;
    private javax.swing.JLabel userSearchUserName;
    private javax.swing.JLabel userSearchUserNameLbl;
    private org.jdesktop.swingx.JXTable userTable;
    // End of variables declaration//GEN-END:variables
}
