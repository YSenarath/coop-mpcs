package ui.view.report;

import controller.ledger.GoodRecieveNoteController;
import java.awt.Color;
import java.sql.SQLException;
import model.ledger.GoodRecieveNote;
import net.sf.jasperreports.engine.JRException;
import report.ledger.GRNCopy;

public class GRNRequestInterface extends javax.swing.JInternalFrame {

    //
    //
    // <editor-fold defaultstate="collapsed" desc="Variables">               
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GRNRequestInterface.class);

    // </editor-fold>    
    //
    //
    // <editor-fold defaultstate="collapsed" desc="Interface">               
    public GRNRequestInterface() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtGRNID = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        txtGRNID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGRNIDKeyReleased(evt);
            }
        });

        jLabel1.setText("GRN ID");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("GRN Document Copy");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGRNID, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(26, 26, 26))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCancel, jButton2});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtGRNID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        PrintDocument();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        close();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtGRNIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNIDKeyReleased
        updateInterface();
    }//GEN-LAST:event_txtGRNIDKeyReleased

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GRNRequestInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            new GRNRequestInterface().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtGRNID;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    //
    //
    // <editor-fold defaultstate="collapsed" desc="Main Functions">  

    private void PrintDocument() {
        try {
            String GRNId = txtGRNID.getText();
            GoodRecieveNote grn = GoodRecieveNoteController.getGrn(GRNId);
            if (grn != null) {
                GRNCopy.produceCopyTest(grn);
            } else {
                util.Utilities.ShowErrorMsg(this, "Unable to find GRN with that ID!");
                logger.error("Unable to find GRN with that ID!");
            }
        } catch (SQLException | JRException ex) {
            util.Utilities.ShowErrorMsg(this, "Can't create GRN repot.");
            logger.error(ex.getMessage());
        }
    }

    private void close() {
        this.dispose();
    }

    private void updateInterface() {
        String grnText = txtGRNID.getText();
        try {
            if (GoodRecieveNoteController.GRNExists(grnText)) {
                txtGRNID.setBackground(Color.WHITE);
            } else {
                txtGRNID.setBackground(Color.PINK);
            }
        } catch (SQLException ex) {
                txtGRNID.setBackground(Color.PINK);
        }
    }
    // </editor-fold>    
    //
    //
}
