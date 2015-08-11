package ui.view.pos;

import controller.pos.TransactionController;
import controller.pos.UserController;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;
import model.pos.CounterLogin;
import model.people.User;
import org.apache.log4j.Logger;
import util.DoubleFilter;
import util.Utilities;
import static util.Utilities.doubleFormatComponentText;
import static util.Utilities.setupUI;

class LogIn extends javax.swing.JFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(LogIn.class);

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    private LogIn() {
        logger.debug("logIn constructor invoked");

        initComponents();
        ((PlainDocument) txtIntialAmount.getDocument()).setDocumentFilter(new DoubleFilter());
        initializePOSSystem();
        setLocationRelativeTo(null);
        txtUserName.requestFocus();
    }
    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Helper Methods">

    private void initializePOSSystem() {
        logger.debug("initializePOSSystem invoked");

        //Configure the counter for 1st time
        String isFirstUse = Utilities.loadProperty("firstUse");

        if (isFirstUse.equals("NULL")) {
            logger.info("First time ");
            new ConfigureDialog(this, true).setVisible(true);
        }

        String counterId = Utilities.loadProperty("counter");
        String serverIp = Utilities.loadProperty("SERVER_IP");

        if (counterId.equals("NULL") || serverIp.equals("NULL")) {
            Utilities.showMsgBox("Please set initial Configuration", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(3);
        } else {
            if (isFirstUse.equals("NULL")) {
                Utilities.saveProperty("firstUse", "1");
            }
            txtIntialAmount.setText(String.format("%.2f", 0.0));
        }

    }

    //Check if user is authenticated
    private boolean isUserAuthenticated(String userName, char[] password, String requestedAccessLevel) throws Exception {
        logger.debug("isUserAuthenticated invoked");

        User user = UserController.getUser("user_name", userName);
        if (user != null) {
            char[] dbHash = user.getPassword().toCharArray();
            if (Utilities.isHashSame(dbHash, password)) {
                logger.info("User varified");
                if ((requestedAccessLevel.equals(User.MANAGER) || requestedAccessLevel.equals(User.INVENTORY)) && user.getUserType().equals(User.CASHIER)) {
                    throw new Exception("User does not have administrator privilages ");
                }
                if (!(user.getUserType().equals(User.MANAGER) || user.getUserType().equals(User.CASHIER))) {
                    throw new Exception("User does not have pos privilages ");
                }
                if (!user.getUserType().equals(User.MANAGER) && user.isLoggedin()) {
                    throw new Exception("User :" + userName + " is already logged in");
                }
                return true;
            } else {
                throw new Exception("Wrong password");
            }
        } else {
            throw new Exception("User " + userName + " not found");
        }

    }

    private boolean performCounterLogin(CounterLogin counterLogin) throws SQLException {
        logger.debug("performCounterLogin invoked");

        return TransactionController.performLogInTransaction(counterLogin);
    }

    private void txtUserNameKeyHandler(java.awt.event.KeyEvent evt) {
        if (txtUserName.getText().equals("")) {
            txtUserName.requestFocus();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPassword.requestFocus();
        }
    }

    private void txtPasswordKeyHandler(java.awt.event.KeyEvent evt) {
        if (new String(txtPassword.getPassword()).equals("")) {
            txtPassword.requestFocus();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtIntialAmount.requestFocus();
        }
    }

    private void txtInitialAmountKeyHandler(java.awt.event.KeyEvent evt) {
        if (txtIntialAmount.getText().equals("")) {
            txtIntialAmount.requestFocus();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOK.requestFocus();
        }
    }

// </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Login Methods">
    //Log in to application
    private void logInToPOS() {
        logger.debug("logInToPOS invoked");

        try {

            if (Utilities.loadProperty("counter").equals("NULL")) {
                Utilities.showMsgBox("Counter information not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (txtUserName.getText().equals("")) {
                txtPassword.setText("");
                txtUserName.requestFocus();
                return;
            }

            if (new String(txtPassword.getPassword()).equals("")) {
                txtPassword.requestFocus();
                return;
            }

            if (txtIntialAmount.getText().equals("")) {
                txtIntialAmount.requestFocus();
                return;
            }

            String userName = txtUserName.getText();
            double initialAmount = Double.valueOf(txtIntialAmount.getText());

            if (initialAmount < 5000) {
                Utilities.showMsgBox("Minimum intial amount must be Rs.5000", "Login Failed", JOptionPane.ERROR_MESSAGE);
                txtIntialAmount.requestFocus();
                return;
            }

            if (isUserAuthenticated(userName, txtPassword.getPassword(), User.CASHIER)) {
                CounterLogin counterLogin = new CounterLogin(
                        userName,
                        Integer.parseInt(Utilities.loadProperty("counter")),
                        Utilities.getCurrentTime(true),
                        Utilities.getStringDate(Utilities.getCurrentDate()),
                        initialAmount
                );
                if (performCounterLogin(counterLogin)) {
                    new POSMDIInterface(false).setVisible(true);
                }
                exitApp();
            } else {
                Utilities.showMsgBox("User not identified", "Login Failed", JOptionPane.ERROR_MESSAGE);
                logger.error("User not identified");
            }
        } catch (SQLException ex) {
            logger.error("SQL error : " + ex.getMessage());
            Utilities.showMsgBox("SQL error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            logger.error("Critial error : " + ex.getMessage());
            Utilities.showMsgBox("Critial error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
            Utilities.showMsgBox("Error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //Go to settings 
    private void configure() {
        logger.debug("configure invoked");

        try {
            String userName = txtUserName.getText();
            if (isUserAuthenticated(userName, txtPassword.getPassword(), User.MANAGER)) {
                new ConfigureDialog(this, true).setVisible(true);
                txtUserName.setText("");
                txtPassword.setText("");
            } else {
                Utilities.showMsgBox("User not identified", "Configuring Failed", JOptionPane.ERROR_MESSAGE);
                logger.error("User not identified");
            }
        } catch (SQLException ex) {
            Utilities.showMsgBox("SQL error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            logger.error("Critial error : " + ex.getMessage());
            Utilities.showMsgBox("Critial error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
            Utilities.showMsgBox("Error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //Exit application
    private void exitApp() {
        logger.debug("exitApp invoked");

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

        containerPanel = new javax.swing.JPanel();
        lbluserName = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        lblInitialCashAmount = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        btnConfigure = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();
        txtIntialAmount = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("COOP POS LOG IN");
        setResizable(false);

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        containerPanel.setBorder(dropShadowBorder1);

        lbluserName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbluserName.setText("User Name");

        txtUserName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUserNameKeyReleased(evt);
            }
        });

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });

        lblInitialCashAmount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblInitialCashAmount.setText("Initial Cash Amount (Rs.) ");
        lblInitialCashAmount.setToolTipText("");

        lblPassword.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPassword.setText("Password");

        btnConfigure.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnConfigure.setText("Configure");
        btnConfigure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigureActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnOK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOK.setText("Ok");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pos/coop_200.png"))); // NOI18N
        lblLogo.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        txtIntialAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtIntialAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIntialAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntialAmountFocusLost(evt);
            }
        });
        txtIntialAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIntialAmountKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                        .addComponent(btnConfigure)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(containerPanelLayout.createSequentialGroup()
                        .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(containerPanelLayout.createSequentialGroup()
                                    .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(142, 142, 142))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                                    .addComponent(lblInitialCashAmount)
                                    .addGap(26, 26, 26)))
                            .addGroup(containerPanelLayout.createSequentialGroup()
                                .addComponent(lbluserName, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(142, 142, 142)))
                        .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(txtIntialAmount))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        containerPanelLayout.setVerticalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addComponent(lblLogo)
                .addGap(18, 18, 18)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbluserName))
                .addGap(18, 18, 18)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInitialCashAmount)
                    .addComponent(txtIntialAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfigure, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
        logInToPOS();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        exitApp();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnConfigureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigureActionPerformed
        // TODO add your handling code here:
        configure();
    }//GEN-LAST:event_btnConfigureActionPerformed

    private void txtIntialAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntialAmountFocusLost
        // TODO add your handling code here:
        doubleFormatComponentText(txtIntialAmount);
    }//GEN-LAST:event_txtIntialAmountFocusLost

    private void txtUserNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyReleased
        // TODO add your handling code here:
        txtUserNameKeyHandler(evt);
    }//GEN-LAST:event_txtUserNameKeyReleased

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        // TODO add your handling code here:
        txtPasswordKeyHandler(evt);
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void txtIntialAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIntialAmountKeyReleased
        // TODO add your handling code here:
        txtInitialAmountKeyHandler(evt);
    }//GEN-LAST:event_txtIntialAmountKeyReleased

    public static void main(String args[]) {
        setupUI();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LogIn().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnConfigure;
    private javax.swing.JButton btnOK;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JLabel lblInitialCashAmount;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lbluserName;
    private javax.swing.JTextField txtIntialAmount;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
}
