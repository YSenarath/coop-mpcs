package ui.view.pos;

import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;
import util.IntegerFilter;
import util.Utilities;
import static util.Utilities.setupUI;

class ConfigureDialog extends javax.swing.JDialog {

// <editor-fold defaultstate="collapsed" desc="Variables">
    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public ConfigureDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ((PlainDocument) txtCounterId.getDocument()).setDocumentFilter(new IntegerFilter());

        String counterId = Utilities.loadProperty("counter");
        String serverIp = Utilities.loadProperty("SERVER_IP");

        if (!counterId.equals("NULL")) {
            txtCounterId.setText(counterId);
        }
        if (!serverIp.equals("NULL")) {
            txtServerIp.setText(serverIp);
        }
        txtCounterId.requestFocus();
        setLocationRelativeTo(null);
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Methods">
    private void saveSettings() {
        if (txtCounterId.getText().trim().isEmpty()) {
            txtCounterId.requestFocus();
            return;
        }
        if (txtServerIp.getText().trim().isEmpty()) {
            txtServerIp.requestFocus();
            return;
        }
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

        Utilities.saveProperty("counter", String.valueOf(counterId));
        Utilities.saveProperty("SERVER_IP", ipAddress);

        dispose();
        Utilities.showMsgBox("Settings saved successfully", "POS", JOptionPane.WARNING_MESSAGE);
    }

    private void configuration_exit() {
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
        txtServerIp.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ContainerPanelLayout = new javax.swing.GroupLayout(ContainerPanel);
        ContainerPanel.setLayout(ContainerPanelLayout);
        ContainerPanelLayout.setHorizontalGroup(
            ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ContainerPanelLayout.createSequentialGroup()
                        .addGap(0, 234, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ContainerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCounterId))
                    .addGroup(ContainerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtServerIp)))
                .addContainerGap())
        );
        ContainerPanelLayout.setVerticalGroup(
            ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCounterId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtServerIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(ContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
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

     /**
      * @param args the command line arguments
      */
     public static void main(String args[]) {

         setupUI();

         /* Create and display the dialog */
         java.awt.EventQueue.invokeLater(new Runnable() {

             public void run() {
                 ConfigureDialog dialog = new ConfigureDialog(new javax.swing.JFrame(), true);
                 dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                     @Override
                     public void windowClosing(java.awt.event.WindowEvent e) {
                         System.exit(0);
                     }
                 });
                 dialog.setVisible(true);
             }
         });
     }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ContainerPanel;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtCounterId;
    private javax.swing.JTextField txtServerIp;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
}
