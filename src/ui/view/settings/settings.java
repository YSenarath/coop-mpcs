/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.settings;

import controller.settings.SettingsController;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;
import model.pos.Setting;
import util.CurrencyFilter;
import util.Utilities;

/**
 *
 * @author HP
 */
public class settings extends javax.swing.JInternalFrame {
private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(settings.class);

    /**
     * Creates new form settings
     */
    public settings() {
        initComponents();
        ((PlainDocument) settingsCoopCreditAmountTxt.getDocument()).setDocumentFilter(new CurrencyFilter());
          ((PlainDocument) settingsEmployeeVoucherAmountTxt.getDocument()).setDocumentFilter(new CurrencyFilter());
          ((PlainDocument) settingsPoshanaAmountTxt.getDocument()).setDocumentFilter(new CurrencyFilter());
          
    }

   public void changeSettings() {
        try {
            if(!settingsCoopCreditAmountTxt.getText().isEmpty()){
                settingsCoopCreditAmountTxt.setText(Double.parseDouble(settingsCoopCreditAmountTxt.getText())+"");
            SettingsController.setSetting(new Setting(settingCoopCreditLbl.getText(), settingsCoopCreditAmountTxt.getText()));
            }
            } catch (SQLException ex) {
          logger.error("SQLException = " + ex.getMessage());
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;    }
        try {
             if(!settingsEmployeeVoucherAmountTxt.getText().isEmpty()){
               settingsEmployeeVoucherAmountTxt.setText(Double.parseDouble(settingsEmployeeVoucherAmountTxt.getText())+"");
           
            SettingsController.setSetting(new Setting(settingsEmployeeVoucherLbl.getText(), settingsEmployeeVoucherAmountTxt.getText()));
        }} catch (SQLException ex) {
            logger.error("SQLException = " + ex.getMessage());
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;  }
        try {
             if(! settingsPoshanaAmountTxt.getText().isEmpty()){
                 settingsPoshanaAmountTxt.setText(Double.parseDouble( settingsPoshanaAmountTxt.getText())+"");
           
        }
            SettingsController.setSetting(new Setting(settingsPoshanaLbl.getText(), settingsPoshanaAmountTxt.getText()));
        } catch (SQLException ex) {
           logger.error("SQLException = " + ex.getMessage());
            Utilities.showMsgBox(ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return;  }
    }


    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        installmentPaymentLbl2 = new javax.swing.JLabel();
        settingCoopCreditLbl = new javax.swing.JLabel();
        settingsEmployeeVoucherLbl = new javax.swing.JLabel();
        settingsPoshanaLbl = new javax.swing.JLabel();
        settingsPoshanaAmountTxt = new javax.swing.JTextField();
        settingsCoopCreditAmountTxt = new javax.swing.JTextField();
        settingsEmployeeVoucherAmountTxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        installmentPaymentLbl2.setBackground(new java.awt.Color(204, 204, 204));
        installmentPaymentLbl2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        installmentPaymentLbl2.setText("Settings                      ");
        installmentPaymentLbl2.setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        installmentPaymentLbl2.setOpaque(true);

        settingCoopCreditLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        settingCoopCreditLbl.setText("Maximum Coop Credit Amount");

        settingsEmployeeVoucherLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        settingsEmployeeVoucherLbl.setText("Maximum Employee Voucher Amount");

        settingsPoshanaLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        settingsPoshanaLbl.setText("Maximum Poshana Amount");

        settingsPoshanaAmountTxt.setEditable(false);
        settingsPoshanaAmountTxt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        settingsPoshanaAmountTxt.setText("2000.00");

        settingsCoopCreditAmountTxt.setEditable(false);
        settingsCoopCreditAmountTxt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        settingsCoopCreditAmountTxt.setText("10000.00");
        settingsCoopCreditAmountTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsCoopCreditAmountTxtActionPerformed(evt);
            }
        });

        settingsEmployeeVoucherAmountTxt.setEditable(false);
        settingsEmployeeVoucherAmountTxt.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        settingsEmployeeVoucherAmountTxt.setText("1500.00");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Change Settings");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Confirm");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(installmentPaymentLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(settingsPoshanaLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(settingsEmployeeVoucherLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(settingCoopCreditLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton2))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(settingsEmployeeVoucherAmountTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(settingsPoshanaAmountTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(settingsCoopCreditAmountTxt, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(installmentPaymentLbl2)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingCoopCreditLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsCoopCreditAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingsEmployeeVoucherLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsEmployeeVoucherAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingsPoshanaLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsPoshanaAmountTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void settingsCoopCreditAmountTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsCoopCreditAmountTxtActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_settingsCoopCreditAmountTxtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        settingsCoopCreditAmountTxt.setEditable(true);
        settingsEmployeeVoucherAmountTxt.setEditable(true);
        settingsPoshanaAmountTxt.setEditable(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        changeSettings();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new settings().setVisible(true);
            }
        });

    }

    //Set poshana amount from settings table

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel installmentPaymentLbl2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel settingCoopCreditLbl;
    private javax.swing.JTextField settingsCoopCreditAmountTxt;
    private javax.swing.JTextField settingsEmployeeVoucherAmountTxt;
    private javax.swing.JLabel settingsEmployeeVoucherLbl;
    private javax.swing.JTextField settingsPoshanaAmountTxt;
    private javax.swing.JLabel settingsPoshanaLbl;
    // End of variables declaration//GEN-END:variables
}
