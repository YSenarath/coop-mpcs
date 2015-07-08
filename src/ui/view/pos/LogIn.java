/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.pos;

import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.pos.Employee;
import org.apache.log4j.Logger;
import ui.handler.pos.LogInHandler;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class LogIn extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(LogIn.class);

    private final LogInHandler loginhandler;

    /**
     * Creates new form LogIn
     */
    private LogIn() {
        initComponents();
        loginhandler = new LogInHandler(this);
        setLocationRelativeTo(null);
    }

    //Log in to application
    private void logIn() {
        try {
            String userName = txtUsername.getText();
            double initialAmount = Double.valueOf(ftxtIntialAmount.getText());

            if (loginhandler.isUserAuthenticated(userName, txtPassword.getPassword())) {
                if (loginhandler.performCounterLogin(userName, initialAmount)) {
                    new POSMDIInterface(userName).setVisible(true);
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

    private void configure() {
        logger.warn("Not implemented - show application configuration UI, after authenticationg user privilage level");
        //On exiting config ui return to login screen
    }

    //Exit application
    private void exitApp() {
        this.dispose();
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

        containerPanel = new javax.swing.JPanel();
        lbluserName = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        ftxtIntialAmount = new javax.swing.JFormattedTextField();
        lblInitialCashAmount = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        btnConfigure = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("COOP POS LOG IN");
        setResizable(false);

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        containerPanel.setBorder(dropShadowBorder1);

        lbluserName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbluserName.setText("User Name");

        txtUsername.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        ftxtIntialAmount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#.00"))));
        ftxtIntialAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ftxtIntialAmount.setText("5000.00");
        ftxtIntialAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

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

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pos/oosd logo.png"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                        .addComponent(btnConfigure)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftxtIntialAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(54, 54, 54)))
                .addContainerGap())
        );
        containerPanelLayout.setVerticalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbluserName))
                .addGap(18, 18, 18)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInitialCashAmount)
                    .addComponent(ftxtIntialAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 30, Short.MAX_VALUE)
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
            .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
        logIn();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        exitApp();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnConfigureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigureActionPerformed
        // TODO add your handling code here:
        configure();
    }//GEN-LAST:event_btnConfigureActionPerformed

    /**
     * @param args the command line arguments
     */
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
    private javax.swing.JFormattedTextField ftxtIntialAmount;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblInitialCashAmount;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lbluserName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
