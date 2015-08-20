package ui.view.ledger;

import controller.inventory.ProductController;
import controller.ledger.GoodRecieveNoteCancelController;
import controller.ledger.GoodRecieveNoteController;
import database.connector.DBConnection;
import java.sql.SQLException;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import model.inventory.Batch;
import model.inventory.Product;
import model.ledger.GRNCancel;

public class GRNCancelInterface extends javax.swing.JInternalFrame implements BatchSelectorInterface {
    //
    //
    // <editor-fold defaultstate="collapsed" desc="Variables">

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GRNCancelInterface.class);

    private final DefaultTableModel model;

    // </editor-fold>
    //
    //
    // <editor-fold defaultstate="collapsed" desc="Netbeans generated">
    public GRNCancelInterface() {
        initComponents();
        initInterface();

        model = (DefaultTableModel) itemDataTable.getModel();
        model.addTableModelListener(new TableModelListenerImpl(model));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtF16aCancelNumber = new javax.swing.JTextField();
        txtF16aNumber = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemDataTable = new javax.swing.JTable();
        btnNewItem = new javax.swing.JButton();
        btnDeleteItem = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        lblTitle = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("F16A Cancel Number");

        jLabel2.setText("Date");

        jLabel8.setText("F16A Number");

        txtF16aCancelNumber.setEditable(false);

        itemDataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Item Code", "Description", "Discount", "Cost Price", "Selling Price", "Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemDataTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        itemDataTable.setColumnSelectionAllowed(true);
        itemDataTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        itemDataTable.setRowHeight(30);
        itemDataTable.getTableHeader().setReorderingAllowed(false);
        itemDataTable.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                itemDataTableInputMethodTextChanged(evt);
            }
        });
        jScrollPane1.setViewportView(itemDataTable);
        itemDataTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (itemDataTable.getColumnModel().getColumnCount() > 0) {
            itemDataTable.getColumnModel().getColumn(0).setResizable(false);
            itemDataTable.getColumnModel().getColumn(0).setPreferredWidth(20);
            itemDataTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            itemDataTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            itemDataTable.getColumnModel().getColumn(3).setResizable(false);
            itemDataTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            itemDataTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            itemDataTable.getColumnModel().getColumn(5).setPreferredWidth(100);
            itemDataTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        btnNewItem.setText("Add");
        btnNewItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewItemActionPerformed(evt);
            }
        });

        btnDeleteItem.setText("Delete");
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });

        jButton3.setText("Record");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtF16aNumber)
                            .addComponent(txtF16aCancelNumber)
                            .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnNewItem)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDeleteItem, btnNewItem, jButton3});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtF16aCancelNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtF16aNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(11, 11, 11)
                        .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeleteItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnNewItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lblTitle.setBackground(java.awt.SystemColor.textHighlight);
        lblTitle.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setLabelFor(this);
        lblTitle.setText("Good Recieve Note - Cancel");
        lblTitle.setAlignmentY(0.0F);
        lblTitle.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTitle.setName(""); // NOI18N
        lblTitle.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewItemActionPerformed
        addNewItem();
    }//GEN-LAST:event_btnNewItemActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        deleteItem();
    }//GEN-LAST:event_btnDeleteItemActionPerformed

    private void itemDataTableInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_itemDataTableInputMethodTextChanged

    }//GEN-LAST:event_itemDataTableInputMethodTextChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        recordGrnCancel();
    }//GEN-LAST:event_jButton3ActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GRNCancelInterface.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GRNCancelInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnNewItem;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JTable itemDataTable;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtF16aCancelNumber;
    private javax.swing.JTextField txtF16aNumber;
    // End of variables declaration//GEN-END:variables

    // </editor-fold>
    //
    //
    // <editor-fold defaultstate="collapsed" desc="GRN Cancel Methods">
    private boolean isItemValid(int row) {
        if (row >= 0 && row < model.getRowCount()) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                if (model.getValueAt(row, i) == "") {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void initInterface() {

        datePicker.setDate(util.Utilities.getToday());

        try {
            String cancelNo = GoodRecieveNoteCancelController.getNextGrnCancelID();
            txtF16aCancelNumber.setText(cancelNo);
        } catch (SQLException ex) {
            logger.error("Error: " + ex.getMessage());
        }

        // ((PlainDocument) txtSellingBillDiscount.getDocument()).setDocumentFilter(new DoubleFilter());
    }

    private void deleteItem() {
        if (itemDataTable.getSelectedRow() >= 0 && itemDataTable.getSelectedRow() < model.getRowCount()) {
            model.removeRow(itemDataTable.getSelectedRow());
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(i + 1, i, 0);
            }
        }
    }

    private void addNewItem() {
        try {
            if (!GoodRecieveNoteController.GRNExists(txtF16aNumber.getText())) {
                util.Utilities.ShowWarningMsg(this, "Enter valid GRN number first.");
                return;
            }
        } catch (SQLException ex) {
            util.Utilities.ShowWarningMsg(this, "Invalid GRN.");
            logger.error(ex.getMessage());
        }
        if (model.getRowCount() == 0 || isItemValid(model.getRowCount() - 1)) {
            txtF16aNumber.setEnabled(false);
            BatchSelectionDialog d = new BatchSelectionDialog(this);
            ((JDesktopPane) this.getParent()).add(d);
            d.centerOnDesktop();
            d.disableGRNSelection(txtF16aNumber.getText());
            d.setVisible(true);
        }
    }

    @Override
    public void batchSelectionResponce(Batch b) {
        this.model.addRow(new Object[]{
            model.getRowCount() + 1,
            b,
            "",
            b.getCost_discount(),
            b.getUnit_cost(),
            b.getUnit_price(),
            b.getQuantity()});
    }

    private void recordGrnCancel() {

        GRNCancel grnCancel = new GRNCancel(
                txtF16aCancelNumber.getText(),
                txtF16aNumber.getText(),
                datePicker.getDate()
        );

        if (model.getRowCount() <= 0) {
            int i = util.Utilities.showButtonMsg("This GRN Cancel dosen't contailn any items. Do you still want to add this?",
                    "Alart!", JOptionPane.YES_NO_OPTION);
            if (i == 1) {
                return;
            }
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            grnCancel.addBatch((Batch) model.getValueAt(i, 1));
        }

        try {
            GoodRecieveNoteCancelController.addGrnCancel(grnCancel);
            util.Utilities.showMsgBox("GRN Cancel successful", "GRN Cancel Success", 0);
            this.dispose();
        } catch (SQLException ex) {
            util.Utilities.showMsgBox("Unable to add GRN Cancel", "GRN Cancel Error", 0);
            logger.error(ex.getMessage());
        }
    }

    private static class TableModelListenerImpl implements TableModelListener {

        DefaultTableModel model;

        public TableModelListenerImpl(DefaultTableModel model) {
            this.model = model;
        }

        @Override
        public void tableChanged(TableModelEvent e) {
            for (int row = 0; row < model.getRowCount(); row++) {
                try {
                    if (e.getColumn() != 2) {
                        // Auto generated Column
                        String productId = model.getValueAt(row, 1).toString();
                        fillProductDetails(row, productId);
                    } else if (e.getColumn() != 8 && e.getColumn() != 9) {
                        // Auto generated Column
                        // Double result = Double.parseDouble(model.getValueAt(row, 6).toString()) * Integer.parseInt(model.getValueAt(row, 3).toString());
                        // model.setValueAt(result.toString(), row, 8);
                    }
                    // model.fireTableCellUpdated(row, 8);
                } catch (Exception ex) {

                }
            }
        }

        private void fillProductDetails(int row, String productId) {
            Product result;
            try {
                result = ProductController.getProduct(productId);
                model.setValueAt(result.getProductName(), row, 2);
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
                model.setValueAt("", row, 2);
            } finally {
                try {
                    DBConnection.closeConnectionToDB();
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                }
            }
        }
    }
    // </editor-fold>
    //
    //
}
