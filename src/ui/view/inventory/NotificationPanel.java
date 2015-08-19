/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.inventory;

import java.awt.CardLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JTable;
import org.apache.log4j.Logger;
import ui.handler.inventory.NotificationPanelHandler;

/**
 *
 * @author Nadheesh
 */
public class NotificationPanel extends javax.swing.JInternalFrame {

    /**
     * Creates new form NotificationPanel
     */
    private NotificationPanelHandler handler;
    private final static Logger logger = Logger.getLogger(NotificationPanel.class);

    public NotificationPanel() {

       
        initComponents();
        handler = new NotificationPanelHandler(this);
        handler.notifyUser();
        
        try {
            handler.loadCounts();
            handler.setButtonNamesWithCounts();
        } catch (SQLException ex) {
            logger.error(ex);
        }

    }

    @Override
    public void setVisible(boolean isVisible){
        
        super.setVisible(isVisible);
        
        if (handler != null ){
        try {
            handler.loadCounts();
            handler.setButtonNamesWithCounts();
        } catch (SQLException ex) {
            logger.error(ex);
        }
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

        expireTitlePanel = new org.jdesktop.swingx.JXTitledPanel();
        jPanel1 = new javax.swing.JPanel();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        jXTaskPane1 = new org.jdesktop.swingx.JXTaskPane();
        aboutToExpireB = new javax.swing.JButton();
        expiredBatchesB = new javax.swing.JButton();
        jXTaskPane2 = new org.jdesktop.swingx.JXTaskPane();
        aboutToFinishB = new javax.swing.JButton();
        outOfStockB = new javax.swing.JButton();
        cardPanel = new javax.swing.JPanel();
        emptyPanel = new javax.swing.JPanel();
        expirationPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        expireTable = new javax.swing.JTable();
        expirationTitle = new javax.swing.JLabel();
        quantityPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lowQuantityTable = new javax.swing.JTable();
        reorderTitlePanel = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);

        expireTitlePanel.setTitle("Notifications");
        expireTitlePanel.setTitleFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        expireTitlePanel.setMinimumSize(new java.awt.Dimension(1286, 481));

        jXTaskPaneContainer1.setBackground(java.awt.SystemColor.controlShadow);

        jXTaskPane1.setScrollOnExpand(true);
        jXTaskPane1.setSpecial(true);
        jXTaskPane1.setTitle("Expiration");
        jXTaskPane1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        aboutToExpireB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        aboutToExpireB.setText("About To Expire");
        aboutToExpireB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutToExpireBActionPerformed(evt);
            }
        });
        jXTaskPane1.getContentPane().add(aboutToExpireB);

        expiredBatchesB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        expiredBatchesB.setText("Expired Batches");
        expiredBatchesB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiredBatchesBActionPerformed(evt);
            }
        });
        jXTaskPane1.getContentPane().add(expiredBatchesB);

        jXTaskPaneContainer1.add(jXTaskPane1);

        jXTaskPane2.setScrollOnExpand(true);
        jXTaskPane2.setSpecial(true);
        jXTaskPane2.setTitle("Re-Ordering");
        jXTaskPane2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        aboutToFinishB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        aboutToFinishB.setText("About To Finish");
        aboutToFinishB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutToFinishBActionPerformed(evt);
            }
        });
        jXTaskPane2.getContentPane().add(aboutToFinishB);

        outOfStockB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        outOfStockB.setText("Out Of Stock");
        outOfStockB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outOfStockBActionPerformed(evt);
            }
        });
        jXTaskPane2.getContentPane().add(outOfStockB);

        jXTaskPaneContainer1.add(jXTaskPane2);

        cardPanel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cardPanel.setMinimumSize(new java.awt.Dimension(940, 450));
        cardPanel.setPreferredSize(new java.awt.Dimension(940, 450));
        cardPanel.setLayout(new java.awt.CardLayout());

        emptyPanel.setMaximumSize(new java.awt.Dimension(940, 450));
        emptyPanel.setMinimumSize(new java.awt.Dimension(940, 450));
        emptyPanel.setName(""); // NOI18N

        javax.swing.GroupLayout emptyPanelLayout = new javax.swing.GroupLayout(emptyPanel);
        emptyPanel.setLayout(emptyPanelLayout);
        emptyPanelLayout.setHorizontalGroup(
            emptyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
        );
        emptyPanelLayout.setVerticalGroup(
            emptyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        cardPanel.add(emptyPanel, "card4");

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        expirationPanel.setBorder(dropShadowBorder1);
        expirationPanel.setMaximumSize(new java.awt.Dimension(940, 450));
        expirationPanel.setMinimumSize(new java.awt.Dimension(940, 450));
        expirationPanel.setPreferredSize(new java.awt.Dimension(940, 450));

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder2 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder2.setShowLeftShadow(true);
        dropShadowBorder2.setShowTopShadow(true);
        jScrollPane2.setBorder(dropShadowBorder2);

        expireTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Batch ID", "GRN number", "Supplier", "Quantity", "Expire Date", "Notification Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        expireTable.setFillsViewportHeight(true);
        expireTable.setShowHorizontalLines(false);
        expireTable.setShowVerticalLines(false);
        expireTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(expireTable);
        expireTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (expireTable.getColumnModel().getColumnCount() > 0) {
            expireTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            expireTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            expireTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            expireTable.getColumnModel().getColumn(3).setResizable(false);
            expireTable.getColumnModel().getColumn(3).setPreferredWidth(150);
            expireTable.getColumnModel().getColumn(4).setResizable(false);
            expireTable.getColumnModel().getColumn(4).setPreferredWidth(250);
            expireTable.getColumnModel().getColumn(5).setPreferredWidth(200);
            expireTable.getColumnModel().getColumn(6).setPreferredWidth(200);
            expireTable.getColumnModel().getColumn(7).setPreferredWidth(200);
        }

        expirationTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        expirationTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        expirationTitle.setText(" ");
        expirationTitle.setToolTipText("");
        expirationTitle.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout expirationPanelLayout = new javax.swing.GroupLayout(expirationPanel);
        expirationPanel.setLayout(expirationPanelLayout);
        expirationPanelLayout.setHorizontalGroup(
            expirationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expirationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(expirationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(expirationTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        expirationPanelLayout.setVerticalGroup(
            expirationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, expirationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(expirationTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cardPanel.add(expirationPanel, "expirationPanel");

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder3 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder3.setShowLeftShadow(true);
        dropShadowBorder3.setShowTopShadow(true);
        quantityPanel.setBorder(dropShadowBorder3);
        quantityPanel.setMaximumSize(new java.awt.Dimension(940, 450));
        quantityPanel.setMinimumSize(new java.awt.Dimension(940, 450));
        quantityPanel.setPreferredSize(new java.awt.Dimension(940, 450));

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder4 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder4.setShowLeftShadow(true);
        dropShadowBorder4.setShowTopShadow(true);
        jScrollPane3.setBorder(dropShadowBorder4);

        lowQuantityTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Quantity", "Unit", "Re-Order Level"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        lowQuantityTable.setColumnSelectionAllowed(true);
        lowQuantityTable.setFillsViewportHeight(true);
        lowQuantityTable.setShowHorizontalLines(false);
        lowQuantityTable.setShowVerticalLines(false);
        lowQuantityTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(lowQuantityTable);
        lowQuantityTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (lowQuantityTable.getColumnModel().getColumnCount() > 0) {
            lowQuantityTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            lowQuantityTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            lowQuantityTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            lowQuantityTable.getColumnModel().getColumn(3).setPreferredWidth(200);
            lowQuantityTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        }

        reorderTitlePanel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        reorderTitlePanel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        reorderTitlePanel.setText("  ");
        reorderTitlePanel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout quantityPanelLayout = new javax.swing.GroupLayout(quantityPanel);
        quantityPanel.setLayout(quantityPanelLayout);
        quantityPanelLayout.setHorizontalGroup(
            quantityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quantityPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(quantityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
                    .addComponent(reorderTitlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        quantityPanelLayout.setVerticalGroup(
            quantityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, quantityPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reorderTitlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cardPanel.add(quantityPanel, "quantityPanel");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout expireTitlePanelLayout = new javax.swing.GroupLayout(expireTitlePanel.getContentContainer());
        expireTitlePanel.getContentContainer().setLayout(expireTitlePanelLayout);
        expireTitlePanelLayout.setHorizontalGroup(
            expireTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expireTitlePanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        expireTitlePanelLayout.setVerticalGroup(
            expireTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(expireTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1179, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(expireTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aboutToExpireBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutToExpireBActionPerformed
        expirationTitle.setText("About To Expire Batches");
        showExpirationCard(false);
    }//GEN-LAST:event_aboutToExpireBActionPerformed

    private void aboutToFinishBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutToFinishBActionPerformed
        reorderTitlePanel.setText("About To Finish Products");
        showReOrderCard(false);
    }//GEN-LAST:event_aboutToFinishBActionPerformed

    private void expiredBatchesBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expiredBatchesBActionPerformed
        expirationTitle.setText("Expired Batches");
        showExpirationCard(true);
    }//GEN-LAST:event_expiredBatchesBActionPerformed

    private void outOfStockBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outOfStockBActionPerformed
        reorderTitlePanel.setText("Out Of Stock Products");
        showReOrderCard(true);
    }//GEN-LAST:event_outOfStockBActionPerformed

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
            java.util.logging.Logger.getLogger(NotificationPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NotificationPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NotificationPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NotificationPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    public JTable getExpireTable() {
        return expireTable;
    }

   

    public JTable getLowQuantityTable() {
        return lowQuantityTable;
    }

    public JButton getAboutToExpireB() {
        return aboutToExpireB;
    }

    public JButton getAboutToFinishB() {
        return aboutToFinishB;
    }

    public JButton getExpiredBatchesB() {
        return expiredBatchesB;
    }

    public JButton getOutOfStockB() {
        return outOfStockB;
    }

    private void showReOrderCard(boolean onlyOutOfStock) {

        try {
            handler.showAboutToFinish(onlyOutOfStock);
        } catch (SQLException ex) {
            logger.error(ex);
        }
        ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "quantityPanel");

    }

    public void showExpirationCard(boolean onlyExpiration) {

        try {
            handler.showCloseToExpireBatches(onlyExpiration);
        } catch (SQLException ex) {
            logger.error(ex);
        }
        ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "expirationPanel");

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutToExpireB;
    private javax.swing.JButton aboutToFinishB;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JPanel emptyPanel;
    private javax.swing.JPanel expirationPanel;
    private javax.swing.JLabel expirationTitle;
    private javax.swing.JTable expireTable;
    private org.jdesktop.swingx.JXTitledPanel expireTitlePanel;
    private javax.swing.JButton expiredBatchesB;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane2;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    private javax.swing.JTable lowQuantityTable;
    private javax.swing.JButton outOfStockB;
    private javax.swing.JPanel quantityPanel;
    private javax.swing.JLabel reorderTitlePanel;
    // End of variables declaration//GEN-END:variables

}