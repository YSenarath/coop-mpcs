package ui.view.pos;

import controller.inventory.BatchController;
import controller.inventory.CategoryController;
import controller.inventory.DepartmentController;
import controller.inventory.ProductController;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import model.inventory.Batch;
import model.inventory.Category;
import model.inventory.Department;
import model.inventory.Product;
import org.apache.log4j.Logger;

class CheckStockInterface extends javax.swing.JInternalFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(CheckStockInterface.class);

    private final POSMDIInterface parent;
    private final JDesktopPane desktopPane;
    private SearchItemInterface searchItemInterface;

    //Glass pane
    private final JPanel glassPanel;
    private final JLabel padding;

    //KeyMaps
    private InputMap inputMap;
    private ActionMap actionMap;

    //The three main POS UI's
    private final InvoiceInternalInterface invoiceInterface;
    private final BillRefundInternalInterface billRefundInterface;
    private final BillCopyInternalInterface billCopyInterface;

    private final ActionListener productCodeListner;

    private final DefaultComboBoxModel itemComboBoxModel;
    private final DefaultTableModel itemTableModel;
    private HashMap<String, Product> availableProductMap;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public CheckStockInterface(POSMDIInterface parent, InvoiceInternalInterface invoiceInterface,
            BillRefundInternalInterface billRefundInterface, BillCopyInternalInterface billCopyInterface, JDesktopPane desktopPane) {
        logger.debug("CheckStockInterface constructor invoked");

        initComponents();
        this.parent = parent;
        this.invoiceInterface = invoiceInterface;
        this.billRefundInterface = billRefundInterface;
        this.billCopyInterface = billCopyInterface;
        this.searchItemInterface = null;
        this.desktopPane = desktopPane;

        Dimension desktopSize = desktopPane.getSize();
        Dimension jInternalFrameSize = this.getSize();
        this.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        //Disable all other jframes
        if (invoiceInterface != null) {
            invoiceInterface.enableGlassPane(true);
        }
        if (billRefundInterface != null) {
            billRefundInterface.enableGlassPane();
        }
        if (billCopyInterface != null) {
            billCopyInterface.enableGlassPane();
        }

        productCodeListner = (ActionEvent e) -> {
            showInfo();
        };
        itemCodeComboBox.addActionListener(productCodeListner);

        this.itemComboBoxModel = new DefaultComboBoxModel();
        this.itemTableModel = (DefaultTableModel) itemInfoTable.getModel();

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
        performKeyBinding();

        loadSellebleProducts();
        itemCodeComboBox.requestFocus();
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Key Bindings "> 
    private void performKeyBinding() {

        inputMap = stockInfoPanel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        actionMap = stockInfoPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "doF5Action");
        actionMap.put("doF5Action", new keyBindingAction("F5"));

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "doEscapeAction");
        actionMap.put("doEscapeAction", new keyBindingAction("Escape"));

    }

    private class keyBindingAction extends AbstractAction {

        private final String cmd;

        public keyBindingAction(String cmd) {
            this.cmd = cmd;
        }

        @Override
        public void actionPerformed(ActionEvent tf) {
            if (cmd.equalsIgnoreCase("F5")) {
                logger.debug("CheckStockInterface Interface - F5 Pressed ");
                btnSearch.doClick();
            } else if (cmd.equalsIgnoreCase("Escape")) {
                logger.debug("CheckStockInterface Interface - Escape Pressed ");
                btnOk.doClick();
            }
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Methods">
    //Disable the glassPanel pane
    public void disableGlassPane() {
        logger.debug("disableGlassPane invoked");

        glassPanel.setVisible(false);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "doF5Action");
        actionMap.put("doF5Action", new keyBindingAction("F5"));

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "doEscapeAction");
        actionMap.put("doEscapeAction", new keyBindingAction("Escape"));
        itemCodeComboBox.requestFocus();
    }

    //Enable the glassPanel pane
    public void enableGlassPane() {
        logger.debug("enableGlassPane invoked");

        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        actionMap.remove("doF5Action");

        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        actionMap.remove("doEscapeAction");

        glassPanel.setVisible(true);//Disable this UI

        padding.requestFocus();  // required to trap key events
    }

    //Load the products to the temporory object array and show on combo box
    private void loadSellebleProducts() {
        logger.debug("loadSellebleProducts invoked");

        try {
            clearUI();
            ArrayList<Product> availableProducts = ProductController.getAllProducts();
            availableProductMap = new HashMap<>();

            itemCodeComboBox.removeActionListener(productCodeListner);

            itemComboBoxModel.removeAllElements();

            availableProducts.stream().forEach((product) -> {
                availableProductMap.put(product.getProductId(), product);
                itemComboBoxModel.addElement(product.getProductId());
            });
            itemCodeComboBox.getModel().setSelectedItem(null);

            itemCodeComboBox.setModel(itemComboBoxModel);

            itemCodeComboBox.setSelectedIndex(-1);

            itemCodeComboBox.addActionListener(productCodeListner);
        } catch (SQLException ex) {
            logger.error("Product load error : " + ex.getMessage());
        }
    }

    //get the information related to the product selected from search ui
    public void setSelectedProduct(String productId) {
        logger.debug("showInfo(int productId) invoked");

        //item from search , update the combo box
        for (int i = 0; i < itemComboBoxModel.getSize(); i++) {
            if (itemComboBoxModel.getElementAt(i).toString().equals(productId)) {
                itemCodeComboBox.setSelectedIndex(i);// can cause a recursive malfunction
                break;
            }
        }

    }

    //get the information related to the product selected
    private void showInfo() {
        logger.debug("showInfo invoked");

        if (itemCodeComboBox.getSelectedIndex() > -1) {

            String productId = itemCodeComboBox.getSelectedItem().toString();
            itemTableModel.setRowCount(0);

            try {

                Product product = availableProductMap.get(productId);
                txtName.setText(product.getProductName());
                txtProductDesc.setText(product.getDescription());
                txtUnit.setText(product.getUnit());
                txtBarcode.setText((String.valueOf(product.getProductBarCode())));
                Department department = DepartmentController.getDepartment(product.getDepartmentId());
                Category category = CategoryController.getCategory(product.getDepartmentId(), product.getCategoryId());
                txtDepartment.setText(department.getDepartmentName());
                txtCategory.setText(category.getCategoryName());

                ArrayList<Batch> batches = BatchController.getAllBatches(productId);
                for (Batch batch : batches) {
                    Object[] ob = {
                        batch.getBatchId(),
                        batch.getExpirationDate(),
                        batch.getRecievedQuantity() - batch.getSoldQty(),
                        String.format("%.2f", batch.getUnit_price()),
                        batch.isInStock()
                    };
                    itemTableModel.addRow(ob);
                }

            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
            }

        }

    }

    //Clear UI
    private void clearUI() {
        logger.debug("clearUI invoked");

        itemTableModel.setRowCount(0);
        txtName.setText("");
        txtProductDesc.setText("");
        txtUnit.setText("");
        txtDepartment.setText("");
        txtCategory.setText("");
        txtBarcode.setText("");
    }

//Show search item 
    private void searchItem() {
        logger.debug("searchItem invoked");

        if (searchItemInterface != null) {
            desktopPane.remove(searchItemInterface);
        }
        searchItemInterface = new SearchItemInterface(desktopPane, this);
        desktopPane.add(searchItemInterface, new Integer(60));//On top of all internal frames
        searchItemInterface.setVisible(true);
    }

    //Cancel check
    private void exitCheck() {
        logger.debug("exitCheck invoked");

        if (invoiceInterface != null) {
            invoiceInterface.disableGlassPane(true);
        }
        if (billRefundInterface != null) {
            billRefundInterface.disableGlassPane();
        }
        if (billCopyInterface != null) {
            billCopyInterface.disableGlassPane();
        }
        parent.enableCheckStockBtn();
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
        stockInfoPanel = new javax.swing.JPanel();
        lblCode = new javax.swing.JLabel();
        lblProductDesc = new javax.swing.JLabel();
        txtProductDesc = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        itemCodeComboBox = new javax.swing.JComboBox();
        btnRefresh = new javax.swing.JButton();
        lblBarcde = new javax.swing.JLabel();
        txtBarcode = new javax.swing.JTextField();
        lblDepartment = new javax.swing.JLabel();
        txtDepartment = new javax.swing.JTextField();
        lblCategory = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        lblUnit = new javax.swing.JLabel();
        txtUnit = new javax.swing.JTextField();
        lblProductname = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        itemInfoContainer = new javax.swing.JPanel();
        itemInfoSP = new javax.swing.JScrollPane();
        itemInfoTable = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();

        setTitle("Check Stock");

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        stockInfoPanel.setBorder(dropShadowBorder1);

        lblCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCode.setText("Code");

        lblProductDesc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProductDesc.setText("Description");

        txtProductDesc.setEditable(false);
        txtProductDesc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSearch.setText("Search [ F5 ] ");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        itemCodeComboBox.setEditable(true);
        itemCodeComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        itemCodeComboBox.setMaximumRowCount(5);

        btnRefresh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        lblBarcde.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBarcde.setText("Barcode");

        txtBarcode.setEditable(false);
        txtBarcode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtBarcode.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblDepartment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDepartment.setText("Department");

        txtDepartment.setEditable(false);
        txtDepartment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblCategory.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCategory.setText("Category");

        txtCategory.setEditable(false);
        txtCategory.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblUnit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUnit.setText("Unit ");

        txtUnit.setEditable(false);
        txtUnit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtUnit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblProductname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProductname.setText("Name");

        txtName.setEditable(false);
        txtName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout stockInfoPanelLayout = new javax.swing.GroupLayout(stockInfoPanel);
        stockInfoPanel.setLayout(stockInfoPanelLayout);
        stockInfoPanelLayout.setHorizontalGroup(
            stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stockInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(stockInfoPanelLayout.createSequentialGroup()
                        .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblProductDesc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(stockInfoPanelLayout.createSequentialGroup()
                                .addGap(156, 156, 156)
                                .addComponent(lblCategory)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(stockInfoPanelLayout.createSequentialGroup()
                                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, stockInfoPanelLayout.createSequentialGroup()
                                        .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(lblProductname)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblBarcde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUnit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE))
                    .addGroup(stockInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        stockInfoPanelLayout.setVerticalGroup(
            stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stockInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblBarcde, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblProductname, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        itemInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Batch", "Exp. Date", "Available Qty", "Price (Rs.)", "In stock"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
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
        itemInfoTable.getTableHeader().setReorderingAllowed(false);
        itemInfoSP.setViewportView(itemInfoTable);

        javax.swing.GroupLayout itemInfoContainerLayout = new javax.swing.GroupLayout(itemInfoContainer);
        itemInfoContainer.setLayout(itemInfoContainerLayout);
        itemInfoContainerLayout.setHorizontalGroup(
            itemInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemInfoSP)
        );
        itemInfoContainerLayout.setVerticalGroup(
            itemInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemInfoSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
        );

        btnOk.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnOk.setMnemonic('V');
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stockInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemInfoContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        containerPanelLayout.setVerticalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stockInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemInfoContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        searchItem();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        exitCheck();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        loadSellebleProducts();
    }//GEN-LAST:event_btnRefreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JComboBox itemCodeComboBox;
    private javax.swing.JPanel itemInfoContainer;
    private javax.swing.JScrollPane itemInfoSP;
    private javax.swing.JTable itemInfoTable;
    private javax.swing.JLabel lblBarcde;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblCode;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblProductDesc;
    private javax.swing.JLabel lblProductname;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JPanel stockInfoPanel;
    private javax.swing.JTextField txtBarcode;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtProductDesc;
    private javax.swing.JTextField txtUnit;
    // End of variables declaration//GEN-END:variables
 // </editor-fold>
}
