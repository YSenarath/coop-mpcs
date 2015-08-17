package ui.view.pos;

import controller.pos.TransactionController;
import controller.user.UserController;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import model.people.User;
import model.pos.item.Refund;
import org.apache.log4j.Logger;
import report.pos.ReportGenerator;
import util.Utilities;

class RefundVarification extends javax.swing.JDialog {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(RefundVarification.class);
    private final POSMDIInterface parent;
    private final Refund refund;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public RefundVarification(POSMDIInterface parent, Refund refund, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        this.refund = refund;
        initComponents();
        setLocationRelativeTo(null);
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Methods">
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

    private void cancelRefund() {
        logger.debug("cancelRefund invoked");

        this.dispose();
    }

    private void confirmRefund() {
        logger.debug("confirmRefund invoked");

        //Validate user
        if (txtUserName.getText().trim().isEmpty()) {
            txtUserName.requestFocus();
            return;
        }
        if (txtPassword.getPassword().length == 0) {
            txtPassword.requestFocus();
            return;
        }

        try {
            if (isUserAuthenticated(txtUserName.getText().trim(), txtPassword.getPassword(), User.CASHIER)) {
                boolean result = TransactionController.performRefundTransaction(refund);
                if (result) {
                    ReportGenerator.generateRefund(parent.getCounterLogin(), refund);
                    logger.info("Refund complete");
                    Utilities.showMsgBox("Refund complete", "POS", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    logger.info("Refund faild");
                    Utilities.showMsgBox("Refund not successfull", "POS", JOptionPane.ERROR);
                }
                this.dispose();
            } else {
                Utilities.showMsgBox("User varification error ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            Utilities.showMsgBox("User varification error : " + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void txtUserNameKeyHandler(KeyEvent evt) {
        logger.debug("txtUserNameKeyHandler invoked");

        if (txtUserName.getText().trim().isEmpty()) {
            txtUserName.requestFocus();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPassword.requestFocus();
        }

    }

    private void txtPasswordKeyPressHandler(KeyEvent evt) {
        logger.debug("txtPasswordKeyPressHandler invoked");

        if (new String(txtPassword.getPassword()).trim().isEmpty()) {
            txtPassword.requestFocus();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnConfirm.requestFocus();
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Netbeans generated Code">  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnConfirm = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("User Varification");
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        jPanel1.setBorder(dropShadowBorder1);

        btnConfirm.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnConfirm.setText("Confirm");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("User Name");

        txtUserName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtUserName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUserNameKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Password");
        jLabel3.setToolTipText("");

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtPassword.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 179, Short.MAX_VALUE)
                        .addComponent(btnConfirm)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUserName))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPassword)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnConfirm))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        // TODO add your handling code here:
        confirmRefund();
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:

        cancelRefund();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtUserNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyReleased
        // TODO add your handling code here:
        txtUserNameKeyHandler(evt);
    }//GEN-LAST:event_txtUserNameKeyReleased

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        // TODO add your handling code here:
        txtPasswordKeyPressHandler(evt);
    }//GEN-LAST:event_txtPasswordKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
