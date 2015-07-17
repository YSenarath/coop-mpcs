/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.pos;

import controller.inventory.BatchController;
import controller.inventory.BatchDiscountController;
import controller.inventory.CategoryController;
import controller.inventory.CategoryDiscountController;
import controller.inventory.DepartmentController;
import controller.inventory.ProductController;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import model.inventory.Batch;
import model.inventory.BatchDiscount;
import model.inventory.Category;
import model.inventory.CategoryDiscount;
import model.inventory.Department;
import model.inventory.Product;
import org.apache.log4j.Logger;
import util.KeyValueContainer;

/**
 *
 * @author Shehan
 */
public class CheckStockInterface extends javax.swing.JInternalFrame {

    private static final Logger logger = Logger.getLogger(CheckStockInterface.class);

    private final POSMDIInterface parent;
    private final JDesktopPane desktopPane;
    private SearchItemInterface searchItemInterface;

    //Glass pane
    private final JPanel glassPanel;
    private final JLabel padding;

    //The three main POS UI's
    private final InvoiceInternalInterface invoiceInterface;
    private final BillRefundInternalInterface billRefundInterface;
    private final BillCopyInternalInterface billCopyInterface;

    ActionListener productCodeListner;

    DefaultComboBoxModel itemComboBoxModel;
    DefaultTableModel itemTableModel;
    private HashMap<Integer, Product> availableProductMap;
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

        loadSellebleProducts();
    }

    // </editor-fold>
//
//
//
//
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

    //Load the products to the temporory object array and show on combo box
    private void loadSellebleProducts() {
        logger.debug("loadSellebleProducts invoked");

        try {

            ArrayList<Product> availableProducts = ProductController.getAllProducts();
            availableProductMap = new HashMap<>();

            itemCodeComboBox.removeActionListener(productCodeListner);
            itemComboBoxModel.removeAllElements();

            availableProducts.stream().forEach((product) -> {
                availableProductMap.put(product.getProductId(), product);
                itemComboBoxModel.addElement(new KeyValueContainer(product.getProductId(), util.Utilities.formatId("P", 4, product.getProductId())));
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
    public void showInfo(int productId, boolean setComboBox) {
        logger.debug("showInfo(int productId) invoked");

        if (setComboBox) {
            itemCodeComboBox.removeActionListener(productCodeListner);
            for (int i = 0; i < itemComboBoxModel.getSize(); i++) {
                KeyValueContainer keyValueContainer = (KeyValueContainer) itemComboBoxModel.getElementAt(i);
                if (keyValueContainer.getKey() == productId) {
                    itemCodeComboBox.setSelectedIndex(i);// can cause a recursive malfunction
                    break;
                }
            }
            itemCodeComboBox.addActionListener(productCodeListner);
        }
        itemTableModel.setRowCount(0);

        try {

            Product product = availableProductMap.get(productId);
            txtProductDesc.setText(product.getDescription());
            txtBarcode.setText((String.valueOf(product.getBarcode())));

            Department department = DepartmentController.getDepartment(product.getDepartmentId());
            Category category = CategoryController.getCategory(product.getDepartmentId(), product.getCategoryId());
            txtDepartment.setText(department.getName());
            txtCategory.setText(category.getName());

            if (category.isDiscounted()) {
                CategoryDiscount categoryDiscount = CategoryDiscountController.getCategoryDiscount(product.getDepartmentId(), product.getCategoryId());
                if (util.Utilities.isDateBetweenRange(util.Utilities.getCurrentDate(), categoryDiscount.getStartDate(), categoryDiscount.getEndDate())) {
                    txtCategoryDiscount.setText(String.valueOf(categoryDiscount.getDiscount() + "%"));
                } else {
                    txtCategoryDiscount.setText("0.00%");
                }
            } else {
                txtCategoryDiscount.setText("0.00%");
            }

            ArrayList<Batch> batches = BatchController.getAllBatches(productId);
            for (Batch batch : batches) {
                double batchDiscountAmount = 0;
                if (batch.isDiscounted()) {
                    BatchDiscount batchDiscount = BatchDiscountController.getBatchDiscount(productId, batch.getBatchId());
                    if (util.Utilities.isDateBetweenRange(util.Utilities.getCurrentDate(), batchDiscount.getStartDate(), batchDiscount.getEndDate())) {
                        batchDiscountAmount = batchDiscount.getDiscount();
                    }
                }

                Object[] ob = {
                    batch.getBatchId(),
                    batch.getExpDate(),
                    batch.getQuantity(),
                    product.getUnit(),
                    String.format("%.2f", batchDiscountAmount),
                    String.format("%.2f", batch.getUnitPrice()),
                    batch.isInStock()
                };
                itemTableModel.addRow(ob);
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

    }

    //get the information related to the product selected
    private void showInfo() {
        logger.debug("showInfo invoked");

        itemTableModel.setRowCount(0);

        if (itemCodeComboBox.getSelectedIndex() > -1) {

            KeyValueContainer productComboItem = (KeyValueContainer) itemCodeComboBox.getSelectedItem();
            int productId = productComboItem.getKey();
            showInfo(productId, false);
        }

    }

//Show search item 
    private void searchItem() {
        logger.debug("searchItem invoked");

        if (searchItemInterface == null) {
            searchItemInterface = new SearchItemInterface(desktopPane, this);
        } else {
            desktopPane.remove(searchItemInterface);
        }
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
        this.dispose();
    }

//
//
//    
// <editor-fold defaultstate="collapsed" desc="Netbeans generated Code">
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        containerPanel = new javax.swing.JPanel();
        stockInfoPanel = new javax.swing.JPanel();
        lblCode = new javax.swing.JLabel();
        lblProduct = new javax.swing.JLabel();
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
        txtCategoryDiscount = new javax.swing.JTextField();
        lblCategoryDiscount = new javax.swing.JLabel();
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

        lblProduct.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProduct.setText("Product");

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

        txtCategoryDiscount.setEditable(false);
        txtCategoryDiscount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCategoryDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblCategoryDiscount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCategoryDiscount.setText("C. Discount");

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
                            .addComponent(lblProduct, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(stockInfoPanelLayout.createSequentialGroup()
                                .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(stockInfoPanelLayout.createSequentialGroup()
                                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtProductDesc, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, stockInfoPanelLayout.createSequentialGroup()
                                        .addGap(156, 156, 156)
                                        .addComponent(lblCategory)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(24, 24, 24)
                                .addComponent(lblCategoryDiscount)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCategoryDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(lblBarcde)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(stockInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        stockInfoPanelLayout.setVerticalGroup(
            stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stockInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stockInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCategoryDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategoryDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBarcde, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        itemInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Batch", "Exp. Date", "Qty", "Unit", "Batch Discount (%)", "Price (Rs.)", "In stock"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
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
            .addComponent(itemInfoSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
        );

        btnOk.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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
    private javax.swing.JLabel lblCategoryDiscount;
    private javax.swing.JLabel lblCode;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblProduct;
    private javax.swing.JPanel stockInfoPanel;
    private javax.swing.JTextField txtBarcode;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtCategoryDiscount;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtProductDesc;
    // End of variables declaration//GEN-END:variables
 // </editor-fold>
}
