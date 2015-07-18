package ui.view.pos;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

public class SearchItemInterface extends javax.swing.JInternalFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(SearchItemInterface.class);

    private final InvoiceInternalInterface invoiceInterface;
    private final CheckStockInterface checkStockInterface;

    //Glass pane
    private JPanel glassPanel;
    private JLabel padding;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public SearchItemInterface(JDesktopPane desktopPane, InvoiceInternalInterface invoiceInterface) {
        initUI(desktopPane);
        this.invoiceInterface = invoiceInterface;
        this.checkStockInterface = null;
        invoiceInterface.enableGlassPane(false);

    }

    public SearchItemInterface(JDesktopPane desktopPane, CheckStockInterface checkStockInterface) {
        initUI(desktopPane);
        this.invoiceInterface = null;
        this.checkStockInterface = checkStockInterface;
        checkStockInterface.enableGlassPane();
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Methods">
    private void initUI(JDesktopPane desktopPane) {
        initComponents();
        Dimension desktopSize = desktopPane.getSize();
        Dimension jInternalFrameSize = this.getSize();
        this.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        this.glassPanel = new JPanel(new GridLayout(0, 1));
        this.padding = new JLabel();

        glassPanel.setOpaque(false);
        glassPanel.add(padding);

        glassPanel.addMouseListener(
                new MouseAdapter() {
                });
        glassPanel.addMouseMotionListener(
                new MouseMotionAdapter() {
                });
        glassPanel.addKeyListener(
                new KeyAdapter() {
                });

        // make sure the focus won't leave the glass pane
        glassPanel.setFocusCycleRoot(true);
        setGlassPane(glassPanel);
    }

    //Disable the glassPanel pane
    public void disableGlassPane() {
        logger.debug("disableGlassPane invoked");

        glassPanel.setVisible(false);
    }

    //Enable the glassPanel pane
    public void enableGlassPane() {
        logger.debug("enableGlassPane invoked");
        glassPanel.setVisible(true);//Disable this UI
        padding.requestFocus();  // required to trap key events
    }

    //Perform search
    private void search() {
        logger.warn("search not implemented");
    }

    //Clear fields
    private void clear() {
        logger.warn("clear not implemented");
    }

    //Select Item
    private void selectItem() {
        logger.warn("selectItem not implemented");

        if (invoiceInterface != null) {
            invoiceInterface.disableGlassPane(false);
        }
        if (checkStockInterface != null) {
            checkStockInterface.disableGlassPane();
        }
    }

    //Cancel Search
    private void cancelSearch() {
        if (invoiceInterface != null) {
            invoiceInterface.disableGlassPane(false);
        }
        if (checkStockInterface != null) {
            checkStockInterface.disableGlassPane();
        }
        this.dispose();
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
        lblCode = new javax.swing.JLabel();
        lblProduct = new javax.swing.JLabel();
        txtProduct = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        lblDepartment = new javax.swing.JLabel();
        lblCategory = new javax.swing.JLabel();
        itemCodeComboBox = new javax.swing.JComboBox();
        departmentComboBox = new javax.swing.JComboBox();
        categoryComboBox = new javax.swing.JComboBox();
        itemInfoPanel = new javax.swing.JPanel();
        itemInfoSP = new javax.swing.JScrollPane();
        itemInfoTable = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        btnSelect = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setTitle("Search Item");

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        containerPanel.setBorder(dropShadowBorder1);

        lblCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCode.setText("Code");

        lblProduct.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProduct.setText("Product");

        txtProduct.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSearch.setText("Search [ F5 ] ");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        lblDepartment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDepartment.setText("Department");

        lblCategory.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCategory.setText("Category");
        lblCategory.setToolTipText("");

        itemCodeComboBox.setEditable(true);
        itemCodeComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        itemCodeComboBox.setMaximumRowCount(5);

        departmentComboBox.setEditable(true);
        departmentComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        departmentComboBox.setMaximumRowCount(5);

        categoryComboBox.setEditable(true);
        categoryComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        categoryComboBox.setMaximumRowCount(5);

        itemInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Batch", "Exp. Date", "Stock", "Unit", "Discount", "Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemInfoTable.getTableHeader().setReorderingAllowed(false);
        itemInfoSP.setViewportView(itemInfoTable);

        javax.swing.GroupLayout itemInfoPanelLayout = new javax.swing.GroupLayout(itemInfoPanel);
        itemInfoPanel.setLayout(itemInfoPanelLayout);
        itemInfoPanelLayout.setHorizontalGroup(
            itemInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemInfoSP, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
        );
        itemInfoPanelLayout.setVerticalGroup(
            itemInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemInfoSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
        );

        btnClear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnSelect.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSelect.setText("Select");
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCancel.setMnemonic('E');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(containerPanelLayout.createSequentialGroup()
                        .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblCategory, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                .addComponent(lblCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lblDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(containerPanelLayout.createSequentialGroup()
                                .addComponent(txtProduct)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(containerPanelLayout.createSequentialGroup()
                                .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(containerPanelLayout.createSequentialGroup()
                                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(departmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(containerPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        containerPanelLayout.setVerticalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(departmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        search();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        // TODO add your handling code here:
        selectItem();
    }//GEN-LAST:event_btnSelectActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        cancelSearch();
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSelect;
    private javax.swing.JComboBox categoryComboBox;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JComboBox departmentComboBox;
    private javax.swing.JComboBox itemCodeComboBox;
    private javax.swing.JPanel itemInfoPanel;
    private javax.swing.JScrollPane itemInfoSP;
    private javax.swing.JTable itemInfoTable;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblCode;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblProduct;
    private javax.swing.JTextField txtProduct;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
}
