package ui.view.pos;

import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;
import util.IntegerFilter;
import util.Utilities;

class ConfigureDialog extends javax.swing.JDialog {

// <editor-fold defaultstate="collapsed" desc="Variables">
    
    private static final Logger logger = Logger.getLogger(ConfigureDialog.class);

// </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public ConfigureDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ((PlainDocument) txtCounterId.getDocument()).setDocumentFilter(new IntegerFilter());

        String userNameProperty = Utilities.loadProperty("userName");
        String passwordProperty = Utilities.loadProperty("password");

        String counterId = Utilities.loadProperty("counter");
        String serverIp = Utilities.loadProperty("SERVER_IP");

        if (!userNameProperty.equals("NULL")) {
            txtUserName.setText(userNameProperty);
        }
        if (!passwordProperty.equals("NULL")) {
            txtPassword.setText(passwordProperty);
        }

        if (!counterId.equals("NULL")) {
            txtCounterId.setText(counterId);
        }
        if (!serverIp.equals("NULL")) {
            txtServerIp.setText(serverIp);
        }
        txtUserName.requestFocus();
        setLocationRelativeTo(null);
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Methods">
    private void saveSettings() {
        logger.debug("saveSettings invoked ");

        if (txtUserName.getText().trim().isEmpty()) {
            txtUserName.requestFocus();
            return;
        }

        if (txtPassword.getPassword().length == 0) {
            txtPassword.requestFocus();
            return;
        }
        if (txtCounterId.getText().trim().isEmpty()) {
            txtCounterId.requestFocus();
            return;
        }
        if (txtServerIp.getText().trim().isEmpty()) {
            txtServerIp.requestFocus();
            return;
        }

        String userName = txtUserName.getText();
        String password = new String(txtPassword.getPassword());
        String ipAddress = txtServerIp.getText().trim();

        if (!Utilities.isValidIPv4Address(ipAddress)) {
            Utilities.showMsgBox("Please enter a valid IP address", "Error", JOptionPane.WARNING_MESSAGE);

            txtServerIp.requestFocus();
            return;
        }
        int counterId = -1;
        try {
            counterId = Integer.parseInt(txtCounterId.getText().trim());
        } catch (NumberFormatException ex) {
            Utilities.showMsgBox("Please enter a valid counter ID", "Error", JOptionPane.WARNING_MESSAGE);
            txtServerIp.requestFocus();
            return;
        }

        if (!Utilities.isValidDBConnection(userName, password,ipAddress)) {
            Utilities.showMsgBox("Test connection to database failed. Please recheck username and password", "Warning", JOptionPane.WARNING_MESSAGE);
            logger.error("DB connection failed");
            txtUserName.setText("");
            txtPassword.setText("");
            txtUserName.requestFocus();
            return;
        } else {
            logger.info("DB connection passed");
        }

        Utilities.saveProperty("counter", String.valueOf(counterId));
        Utilities.saveProperty("SERVER_IP", ipAddress);

        Utilities.saveProperty("userName", userName);
        Utilities.saveProperty("password", password);

        //dispose();
        Utilities.showMsgBox("Settings saved successfully", "POS", JOptionPane.WARNING_MESSAGE);
    }

    private void testConnection() {
        logger.debug("testConnection invoked ");

        if (!Utilities.isValidDBConnection()) {
            Utilities.showMsgBox("Test connection to database failed. Please recheck username and password", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            Utilities.showMsgBox("Test connection to database passed.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void configuration_exit() {
        logger.debug("configuration_exit invoked ");
        dispose();
    }

    // </editor-fold>
    //
    //
    //  
// <editor-fold defaultstate="collapsed" desc="Netbeans generated Code">  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        ContainerPanel = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtCounterId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtServerIp = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnTestConnection = new javax.swing.JButton();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Configuration");
        setType(java.awt.Window.Type.POPUP);

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        ContainerPanel.setBorder(dropShadowBorder1);
        ContainerPanel.setToolTipText("");

        btnExit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Counter ID");

        txtCounterId.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtCounterId.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Server IP /Name");

        txtServerIp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtServerIp.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("DB user Name");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("DB Password");

        txtUserName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtUserName.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        btnTestConnection.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnTestConnection.setText("Test Connection");
        btnTestConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestConnectionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ContainerPanelLayout = new javax.swing.GroupLayout(ContainerPanel);
        ContainerPanel.setLayout(ContainerPanelLayout);
        ContainerPanelLayout.setHorizontalGroup(
            ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTestConnection, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUserName)
                    .addComponent(txtPassword)
                    .addComponent(txtCounterId)
                    .addGroup(ContainerPanelLayout.createSequentialGroup()
                        .addGap(0, 62, Short.MAX_VALUE)
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtServerIp))
                .addContainerGap())
        );
        ContainerPanelLayout.setVerticalGroup(
            ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCounterId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtServerIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTestConnection, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        configuration_exit();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        saveSettings();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnTestConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestConnectionActionPerformed
        // TODO add your handling code here:
        testConnection();
    }//GEN-LAST:event_btnTestConnectionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ContainerPanel;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTestConnection;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtCounterId;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtServerIp;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
}
