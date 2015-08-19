package ui.view.report;

import controller.ledger.SupplierReturnNoteController;
import controller.supplier.SupplierController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.UIManager;
import model.ledger.SupplierReturnNote;
import model.supplier.Supplier;
import net.sf.jasperreports.engine.JRException;
import report.ledger.SRNListing;

public class SRNListingRequestInterfae extends javax.swing.JInternalFrame {
    //
    //
    // <editor-fold defaultstate="collapsed" desc="Variables">               
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SRNListingRequestInterfae.class);

    // </editor-fold>    
    //
    //
    // <editor-fold defaultstate="collapsed" desc="Interface">               
    public SRNListingRequestInterfae() {
        initComponents();
        initInterface();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboSupplier = new javax.swing.JComboBox();
        pickFromDate = new org.jdesktop.swingx.JXDatePicker();
        pickToDate = new org.jdesktop.swingx.JXDatePicker();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("From");

        jLabel3.setText("To");

        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("SRN Listing Report");

        jLabel5.setText("Supplier");

        comboSupplier.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All" }));

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrint))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pickFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                            .addComponent(pickToDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(60, 60, 60))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCancel, btnPrint});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(pickFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(pickToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(comboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrint)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnCancel, btnPrint});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        printDocument();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        close();
    }//GEN-LAST:event_btnCancelActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {

        }

        java.awt.EventQueue.invokeLater(() -> {
            new SRNListingRequestInterfae().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnPrint;
    private javax.swing.JComboBox comboSupplier;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private org.jdesktop.swingx.JXDatePicker pickFromDate;
    private org.jdesktop.swingx.JXDatePicker pickToDate;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    //
    //
    // <editor-fold defaultstate="collapsed" desc="Main Functions">               

    private void initInterface() {
        try {
            for (Supplier s : SupplierController.getAllSuppliers()) {
                comboSupplier.addItem(s);
            }
        } catch (SQLException ex) {
            logger.error("Error: " + ex.getMessage());
        }

        pickFromDate.setDate(util.Utilities.getToday());
        pickToDate.setDate(util.Utilities.getToday());
    }

    private void printDocument() {
        ArrayList<SupplierReturnNote> list;
        String supplierId = comboSupplier.getSelectedItem().toString();
        String supplierName = supplierId;
        Date fromDate = pickFromDate.getDate();
        Date toDate = pickToDate.getDate();
        try {
            if ("All".equals(supplierId)) {
                list = SupplierReturnNoteController.getSrn("All", pickFromDate.getDate(), pickToDate.getDate());
            } else {
                Supplier s = (Supplier) comboSupplier.getSelectedItem();
                list = SupplierReturnNoteController.getSrn((s).getSupplerID(), pickFromDate.getDate(), pickToDate.getDate());
                supplierName = s.getName();
            }
            try {
                SRNListing.produceListing(list, supplierName, fromDate, toDate);
            } catch (JRException ex) {
                util.Utilities.ShowErrorMsg(this, "Unable to print the document, Check the date inputs again.");
                logger.error(ex.getMessage());
            }
        } catch (SQLException ex) {
            util.Utilities.ShowErrorMsg(this, "Unable to print the document, Check the date inputs again.");
            logger.error(ex.getMessage());
        }
    }

    private void close() {
        this.dispose();
    }
    
    // </editor-fold>    
    //
    //
}
