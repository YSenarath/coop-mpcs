package ui.view.system;

import controller.user.UserController;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.people.User;
import org.apache.log4j.Logger;
import util.Utilities;

public class LogIn extends javax.swing.JFrame {

    private MainWindow mainUI;
    private String userType;
    private String userName;

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(LogIn.class);

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    private LogIn() {
        logger.debug("logIn constructor invoked");
        userType = null;
        userName = null;
        initComponents();
        try {
            setIconImage(new ImageIcon(getClass().getResource("/ui/view/system/resources/coop_icon.png")).getImage());
        } catch (Exception ex) {

        }
        setLocationRelativeTo(null);
        txtUserName.requestFocus();
    }
    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Helper Methods">

    private void initializeSystem() {
        logger.debug("initializeSystem invoked");

        if (mainUI == null) {
            mainUI = new MainWindow();
            mainUI.setLogInWindow(this);
        }

        //Configure the counter for 1st time
        String isFirstUse = Utilities.loadProperty("firstUseSystem");

        if (isFirstUse.equals("NULL")) {
            logger.info("First time ");
            new ConfigureDialog(this, true).setVisible(true);
        }

        String serverIp = Utilities.loadProperty("SERVER_IP");

        if (serverIp.equals("NULL")) {
            Utilities.showMsgBox("Please set initial Configuration", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(3);
        } else if (!Utilities.isValidDBConnection()) {
            Utilities.showMsgBox("Test connection to database failed. Please recheck username and password", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(3);
        } else {
            logger.info("DB connection established");
            if (isFirstUse.equals("NULL")) {
                logger.info("Initial connection passed");
                Utilities.saveProperty("firstUseSystem", "1");
            }
        }
    }

    //Check if user is authenticated
    private boolean isUserAuthenticated(String userName, char[] password, String requestedAccessLevel) throws Exception {
        logger.debug("isUserAuthenticated invoked");

        User user = UserController.getUser("user_name", userName);
        if (user != null) {
            char[] dbHash = user.getPassword().toCharArray();
            if (Utilities.isHashSame(dbHash, password)) {
                logger.info("User verified");

                if (user.getUserType().equals(User.CASHIER)) {
                    throw new Exception("User does not have administrator privilages");
                }
                if (requestedAccessLevel.equals(user.getUserType())) {
                    return true;
                }
                return false;
            } else {
                throw new Exception("Wrong password");
            }
        } else {
            throw new Exception("User " + userName + " not found");
        }

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
            btnOK.requestFocus();
        }
    }

    public String getLoggedUserType() {
        return userType;
    }

    public String getLoggedUserName() {
        return userName;
    }

    @Override
    public void setVisible(boolean b) {
        this.txtPassword.setText("");
        this.txtUserName.setText("");
        super.setVisible(b); //To change body of generated methods, choose Tools | Templates.
    }

// </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Login Methods">
    //Log in to application
    private void logInToSysyem() {
        logger.debug("logInToSysyem invoked");

        try {
            if (txtUserName.getText().equals("")) {
                txtPassword.setText("");
                txtUserName.requestFocus();
                logger.info("Empty user name");
                Utilities.ShowErrorMsg(this, "Login Error! \nUsername can't be Empty");
                return;
            }

            if (txtPassword.getPassword().length == 0) {
                txtPassword.requestFocus();
                logger.info("Empty password");
                Utilities.ShowErrorMsg(this, "Login Error! \nPassword can't be Empty");
                return;
            }

            String userName = txtUserName.getText();

            if (isUserAuthenticated(userName, txtPassword.getPassword(), User.INVENTORY)) {
                //Code to enter main UI with inventory cleark privilages
                this.userType = User.INVENTORY;
                logger.info("Logging in as inventory manager");
                this.userName = userName;
                initializeSystem();
                mainUI.initializeGUI();
                mainUI.setVisible(true);
                hideApp();
            } else if (isUserAuthenticated(userName, txtPassword.getPassword(), User.MANAGER)) {
                //Code to enter main UI with manager privilages
                logger.info("Logging in as manager");
                this.userType = User.MANAGER;
                this.userName = userName;
                initializeSystem();
                mainUI.initializeGUI();
                mainUI.setVisible(true);
                hideApp();
            } else {
                Utilities.showMsgBox("User not identified", "Login Failed", JOptionPane.ERROR_MESSAGE);
                logger.error("User not identified");
            }

        } catch (SQLException ex) {
            logger.error("SQL error : " + ex.getMessage());
            Utilities.showMsgBox("SQL error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
            Utilities.showMsgBox("Error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //Exit application
    private void hideApp() {
        logger.debug("exitApp invoked");
        this.setVisible(false);
    }

    private void exitApp() {
        System.exit(0);
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
        lblPassword = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("COOP LOG IN");
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

        lblPassword.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPassword.setText("Password");

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

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(containerPanelLayout.createSequentialGroup()
                        .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbluserName, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(142, 142, 142)
                        .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
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
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        logInToSysyem();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        exitApp();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtUserNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyReleased
        // TODO add your handling code here:
        txtUserNameKeyHandler(evt);
    }//GEN-LAST:event_txtUserNameKeyReleased

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        // TODO add your handling code here:
        txtPasswordKeyHandler(evt);
    }//GEN-LAST:event_txtPasswordKeyReleased

    public static void main(String args[]) {
        Utilities.setupMainUI();

//        try {
//            com.jtattoo.plaf.graphite.GraphiteLookAndFeel.setCurrentTheme(props);
//            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
//            //UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
//            //UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
//            // UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//            try {
//                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                    if ("Nimbus".equals(info.getName())) {
//                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                        break;
//                    }
//                }
//            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex1) {
//                java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//            }
//        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LogIn().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lbluserName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
// </editor-fold>

}
