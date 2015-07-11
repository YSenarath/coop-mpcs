/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.pos;

import controller.inventory.BatchDiscountController;
import controller.inventory.CategoryController;
import controller.inventory.CategoryDiscountController;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;
import model.inventory.Batch;
import model.inventory.BatchDiscount;
import model.inventory.Category;
import model.inventory.CategoryDiscount;
import model.inventory.Product;
import org.apache.log4j.Logger;
import ui.handler.pos.InvoiceHandler;
import util.DoubleDocFilter;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class InvoiceInternalInterface extends javax.swing.JInternalFrame {

    private static final Logger logger = Logger.getLogger(InvoiceInternalInterface.class);
    private final POSMDIInterface parent;
    private final JDesktopPane desktopPane;

    private JInternalFrame searchItemInterface;
    private SelectPriceInterface selectPriceInterface;

    private Map<Integer, Product> availableProductMap;
    private Product processingProduct;

    DefaultComboBoxModel productComboBoxModel;
    DefaultTableModel invoiceItemTableModel;

    ActionListener productCodeListner;

    private final JPanel glassPanel;
    private final JLabel padding;

    private double netTotal;
    private double netDiscounts;
    private int totalItemCount;

    private final int NET_TOTAL_COLUMN = 7;
    private final int NET_DISCOUNT_COLUMN = 6;

    /**
     * Creates new form InvoiceInterface
     *
     * @param parent
     * @param desktopPane
     */
    public InvoiceInternalInterface(POSMDIInterface parent, JDesktopPane desktopPane) {
        logger.debug("InvoiceInternalInterface constructor invoked");

        initComponents();
        this.parent = parent;
        this.desktopPane = desktopPane;
        this.selectPriceInterface = null;
        this.searchItemInterface = null;
        this.processingProduct = null;
        this.productComboBoxModel = new DefaultComboBoxModel();
        this.invoiceItemTableModel = (DefaultTableModel) invoiceItemTable.getModel();

        netTotal = 0.00;
        netDiscounts = 0.00;
        totalItemCount = 0;

        productCodeListner = (ActionEvent e) -> {
            showProductDetails();
        };
        itemCodeComboBox.addActionListener(productCodeListner);

        PlainDocument doc = (PlainDocument) txtPrice.getDocument();
        doc.setDocumentFilter(new DoubleDocFilter());

        doc = (PlainDocument) txtQty.getDocument();
        doc.setDocumentFilter(new DoubleDocFilter());

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) invoiceItemTable.getDefaultRenderer(Object.class);
        renderer.setHorizontalAlignment(JLabel.RIGHT);

        txtPrice.setEnabled(false);
        txtQty.setEnabled(false);

        this.glassPanel = new JPanel(new GridLayout(0, 1));

        this.padding = new JLabel();

        glassPanel.setOpaque(
                false);
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
        glassPanel.setFocusCycleRoot(
                true);
        setGlassPane(glassPanel);

        showNextInvoiceId();

        loadSellebleProducts();
    }
    //Price selection ui will call thi to set the price

    public void setProductBatch(Batch selectedBatch) {
        processingProduct.setSelectedBatch(selectedBatch);
        setPropFromBatch();
    }

    //Disable the glassPanel pane
    public void disableGlassPane() {
        glassPanel.setVisible(false);
    }

    //Method to get next invoice Id
    private void showNextInvoiceId() {
        try {
            txtBillNumber.setText(InvoiceHandler.getNextInvoicelId());
        } catch (SQLException ex) {
            logger.error("Bill no error : " + ex.getMessage());
        }
    }

    //Load the products to the temporory object array and show on combo box
    private void loadSellebleProducts() {
        logger.debug("loadSellebleProducts invoked");
        try {

            ArrayList<Product> availableProducts = InvoiceHandler.getAllSellebleProducts();
            availableProductMap = new HashMap<>();

            itemCodeComboBox.removeActionListener(productCodeListner);
            productComboBoxModel.removeAllElements();

            availableProducts.stream().forEach((product) -> {
                availableProductMap.put(product.getProductId(), product);
                productComboBoxModel.addElement(new ProductComboItem(product.getProductId(), util.Utilities.formatId("P", 4, product.getProductId())));
            });
            itemCodeComboBox.getModel().setSelectedItem(null);

            itemCodeComboBox.setModel(productComboBoxModel);

            bill_clearSelectedItemSearch();

            itemCodeComboBox.requestFocus();
            itemCodeComboBox.addActionListener(productCodeListner);

        } catch (SQLException ex) {
            logger.error("Product load error : " + ex.getMessage());
        }
    }

    private void showProductDetails() {
        if (itemCodeComboBox.getSelectedIndex() > -1) {

            ProductComboItem productComboItem = (ProductComboItem) itemCodeComboBox.getSelectedItem();
            int productId = productComboItem.getItemCode();

            try {
                //Create cloned object
                processingProduct = (Product) util.Utilities.deepClone(availableProductMap.get(productId));

                txtProductName.setText(processingProduct.getName());
                txtProductDesc.setText(processingProduct.getDescription());
                lblUnit.setText(processingProduct.getUnit());

                if (processingProduct.getBatches().size() == 1) {
                    logger.debug("Product has one batch");
                    setProductBatch(processingProduct.getBatches().get(0));
                } else if (processingProduct.getBatches().size() > 1) {
                    logger.debug("Product has more than one batch");

                    if (selectPriceInterface != null) {
                        desktopPane.remove(selectPriceInterface);
                    }
                    selectPriceInterface = new SelectPriceInterface(this, desktopPane, productId, processingProduct.getBatches());
                    desktopPane.add(selectPriceInterface, new Integer(10));//Make the price selection UI be on top of desktop pane      
                    selectPriceInterface.setVisible(true);

                    glassPanel.setVisible(true);//Disable this UI
                    padding.requestFocus();  // required to trap key events

                }
                txtPrice.setEnabled(true);
                txtQty.setEnabled(true);
            } catch (IOException | ClassNotFoundException ex) {
                logger.error("Product clone error : " + ex.getMessage());
            }

        }

    }

    //Get the product price from the appropriate batch
    private void setPropFromBatch() {
        txtPrice.setText(String.valueOf(processingProduct.getSelectedBatch().getUnitPrice()));
        txtAvailableQty.setText(String.valueOf(processingProduct.getSelectedBatch().getQuantity()));
        txtQty.requestFocus();
    }

    //Add item to the bill item table
    private void bill_addItemToBillItemTable() {
        logger.warn("bill_addItemToBillItemTable being implemented");
        //When adding check for batch and catogory discounts
        //Chech if discounts in time frame
        try {
            if (processingProduct == null || txtQty.getText().isEmpty()) {
                logger.warn("Empty processingProduct or Qty");
                return;
            }

            double qty = Double.parseDouble(txtQty.getText());

            if (qty <= 0.00 || qty > processingProduct.getSelectedBatch().getQuantity()) {
                Utilities.showMsgBox("Invalid item quantity ", "Incorrect quantity", JOptionPane.WARNING_MESSAGE);
                txtQty.setText("");
                return;
            }
            //get the category discount
            double categorydDis = 0;
            //get category and check if discount available ,if so get the discount
            Category category = CategoryController.getBatchDiscount(processingProduct.getDepartmentId(), processingProduct.getCategoryId());
            if (category.isDiscounted()) {
                CategoryDiscount categoryDiscount = CategoryDiscountController.getCategoryDiscount(processingProduct.getDepartmentId(), processingProduct.getCategoryId());

                if (categoryDiscount != null
                        && (util.Utilities.isDateBetweenRange(util.Utilities.getCurrentDate(), categoryDiscount.getStartDate(), categoryDiscount.getEndDate()))) {

                    categorydDis = categoryDiscount.getDiscount() / 100;
                    logger.debug("Category discount :" + categorydDis);
                    //Check discount validity
                } else {
                    logger.debug("No category discounts found");
                }
            } else {
                logger.debug("Category discount disabled");
            }

            //get the batch discount
            double batchDis = 0;
            if (processingProduct.getSelectedBatch().isDiscounted()) {
                BatchDiscount batchDiscount = BatchDiscountController.getBatchDiscount(processingProduct.getProductId(), processingProduct.getSelectedBatch().getBatchId());
                if (batchDiscount != null
                        && (util.Utilities.isDateBetweenRange(util.Utilities.getCurrentDate(), batchDiscount.getStartDate(), batchDiscount.getEndDate()))) {

                    processingProduct.getSelectedBatch().setBatchDiscount(batchDiscount);
                    batchDis = batchDiscount.getDiscount() / 100;
                    logger.debug("batch discount :" + batchDis);
                } else {
                    logger.debug("No batch discounts found");
                }
            } else {
                logger.debug("batch discount disabled");
            }

            double totalDiscount = categorydDis + batchDis;

            int productCode = processingProduct.getProductId();
            int batchId = processingProduct.getSelectedBatch().getBatchId();
            String name = processingProduct.getName();
            String description = processingProduct.getDescription();
            double unitprice = processingProduct.getSelectedBatch().getUnitPrice();
            String discountPercentage = totalDiscount == 0.0 ? "0.00" : String.format("%.2f", (unitprice * qty * totalDiscount)) + " (" + String.format("%.2f", totalDiscount * 100) + "%)";
            double subTotal = unitprice * qty * (1 - totalDiscount);

            //Add item to the table
            Object[] ob = {
                productCode,
                batchId,
                name,
                description,
                qty,
                unitprice,
                discountPercentage,
                subTotal
            };
            invoiceItemTableModel.addRow(ob);

            bill_clearSelectedItemSearch();//Ready for next product

            calculateInvoiceParameters();//Update the invoice parameters
        } catch (SQLException ex) {
            logger.error("SQL  error : " + ex.getMessage(), ex);
        }

    }

    //Remove a added item from the bill item table
    private void bill_deleteItemFromBillItemTable() {
        logger.debug("bill_deleteItemFromBillItemTable invoked");

        //NOT IMPLEMNTED - When a row is deleted the bottem most row gets auto matically selected. If no row selected by cashier delete from the bottom
        DefaultTableModel billItemTableModel = (DefaultTableModel) invoiceItemTable.getModel();
        if (invoiceItemTable.getSelectedRow() != -1) {
            billItemTableModel.removeRow(invoiceItemTable.getSelectedRow());
            calculateInvoiceParameters();
        }
    }

    private void calculateInvoiceParameters() {
        netTotal = 0.00;
        netDiscounts = 0.00;
        totalItemCount = 0;
        for (int row = 0; row < invoiceItemTable.getRowCount(); row++) {
            netTotal += Double.parseDouble(invoiceItemTable.getValueAt(row, NET_TOTAL_COLUMN).toString());
            netDiscounts += Double.parseDouble((invoiceItemTable.getValueAt(row, NET_DISCOUNT_COLUMN).toString().split(" "))[0]);
            totalItemCount += 1;
        }
        lblNetTotal.setText(String.valueOf(netTotal));
        lblItemDiscounts.setText(String.valueOf(netDiscounts));
        lblitemCount.setText(String.valueOf(totalItemCount));
    }

    //Clear the current product fields in bill add item to bill
    private void bill_clearSelectedItemSearch() {
        logger.debug("bill_clearSelectedItemSearch invoked");

        itemCodeComboBox.setSelectedIndex(-1);
        itemCodeComboBox.setSelectedItem(null);
        txtProductName.setText("");
        txtProductDesc.setText("");
        txtAvailableQty.setText("");
        txtPrice.setText("0.00");
        txtQty.setText("0.00");
        txtPrice.setEnabled(false);
        txtQty.setEnabled(false);
        lblUnit.setText("");
    }

    //Load the item information to the bill 
    private void bill_loadLtemInfoToBill() {
        logger.warn("bill_loadLtemInfoToBill not implemented");
    }

    //Show the payment panel in the bill
    private void bill_showPaymentScreen() {
        if (totalItemCount < 1) {
            return;
        }
        CardLayout card = (CardLayout) invoicePanel.getLayout();
        card.next(invoicePanel);
    }

    //Show the add item panel in bill
    private void bill_showAddItemPanel() {
        CardLayout card = (CardLayout) invoicePanel.getLayout();
        card.previous(invoicePanel);
    }

    //Cancel current bill and show welocme screen
    private void bill_cancelBill() {
        logger.debug("bill_cancelBill invoked");
        parent.setIsMainActivityRunning(false);
        parent.setIsInvoiceRunning(false);
        this.dispose();
    }

    //Toggle payment options in payment scrren
    private void bill_togglePaymentOptions(java.awt.event.ActionEvent evt) {

        CardLayout card = (CardLayout) paymentDetailsPanel.getLayout();
        JComboBox paymentComboBox = (JComboBox) evt.getSource();
        String selectedOption = (String) paymentComboBox.getSelectedItem();
        if (null != selectedOption) {
            switch (selectedOption) {
                case "Cash":
                    card.show(paymentDetailsPanel, "cashCard");
                    break;
                case "Card":
                    card.show(paymentDetailsPanel, "bankCard");
                    break;
                case "Coop Credit":
                    card.show(paymentDetailsPanel, "coopCreditCard");
                    break;
                case "Poshana":
                    card.show(paymentDetailsPanel, "poshanaCard");
                    break;
                case "Voucher":
                    card.show(paymentDetailsPanel, "voucherCard");
                    break;
            }
        }
    }

    //Add a payment option
    private void bill_addPaymentOption() {
        logger.warn("bill_addPaymentOption not implemented");
    }

    //Remove a payment option
    private void bill_removePaymentOption() {
        logger.warn("bill_removePaymentOption not implemented");
    }

    //Search a item
    private void bill_searchItem() {
        logger.debug("bill_searchItem invoked");
//        if (searchItemInterface == null) {
//            searchItemInterface = new SearchItemInterface(desktopPane);
//        } else {
//            desktopPane.remove(searchItemInterface);
//        }
//        desktopPane.add(searchItemInterface);
//        searchItemInterface.setVisible(true);
//        setEnabled(false);
        new SearchItemDialog(this, true).setVisible(true);

    }

    //New sale
    private void bill_newSale() {
        CardLayout card = (CardLayout) cardPanel.getLayout();
        card.show(cardPanel, "invoiceCard");
    }

    //Hold sale
    private void bill_holdSale() {
        logger.warn("bill_holdSale not implemented");
    }

    //Restore sale
    private void bill_restoreSale() {
        logger.warn("bill_restoreSale not implemented");
    }

    //Confirm bill
    private void bill_confirm() {
        logger.warn("bill_confirm not implemented");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        interfaceContainerPanel = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        invoicePanel = new javax.swing.JPanel();
        itemAddPanel = new javax.swing.JPanel();
        itemTableSP = new javax.swing.JScrollPane();
        invoiceItemTable = new javax.swing.JTable();
        btnPayment = new javax.swing.JButton();
        btnInvoiceCancel = new javax.swing.JButton();
        billButtonPanel = new javax.swing.JPanel();
        btnAddItem = new javax.swing.JButton();
        btnDeleteItem = new javax.swing.JButton();
        btnClearItem = new javax.swing.JButton();
        productPanel = new javax.swing.JPanel();
        lblCode = new javax.swing.JLabel();
        lblProductDesc = new javax.swing.JLabel();
        lblQty = new javax.swing.JLabel();
        btnLoad = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        itemCodeComboBox = new javax.swing.JComboBox();
        lblUnit = new javax.swing.JLabel();
        txtProductDesc = new javax.swing.JTextField();
        lblPrice = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        lblProductName = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        txtPrice = new javax.swing.JFormattedTextField();
        txtQty = new javax.swing.JFormattedTextField();
        lblAvailableQtydisplay = new javax.swing.JLabel();
        txtAvailableQty = new javax.swing.JTextField();
        billSummeryPanel = new javax.swing.JPanel();
        lblItemNoDisplay = new javax.swing.JLabel();
        lblitemCount = new javax.swing.JLabel();
        lblNetDisplay = new javax.swing.JLabel();
        lblNetTotal = new javax.swing.JLabel();
        lblItemDiscountDisplay = new javax.swing.JLabel();
        lblItemDiscounts = new javax.swing.JLabel();
        lblBill = new javax.swing.JLabel();
        txtBillNumber = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        paymentPanel = new javax.swing.JPanel();
        btnPrevious = new javax.swing.JButton();
        paymentOptionsPanel = new javax.swing.JPanel();
        paymentOptionsSP = new javax.swing.JScrollPane();
        paymentsTable = new javax.swing.JTable();
        paymentInfoPanel = new javax.swing.JPanel();
        lblBillValueDisplay = new javax.swing.JLabel();
        lblBillValueVal = new javax.swing.JLabel();
        lblTotalDisplay = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        lblBillValueDisplay1 = new javax.swing.JLabel();
        lblBillValueDisplay2 = new javax.swing.JLabel();
        lblChangeDisplay = new javax.swing.JLabel();
        lblChangeVal = new javax.swing.JLabel();
        txtDiscountPercent = new javax.swing.JFormattedTextField();
        txtDiscountVal = new javax.swing.JFormattedTextField();
        paymentSelectorPanel = new javax.swing.JPanel();
        lblPaymentOption = new javax.swing.JLabel();
        paymentOptionComboBox = new javax.swing.JComboBox();
        paymentDetailsPanel = new javax.swing.JPanel();
        cashPaymentPanel = new javax.swing.JPanel();
        lblCashPaymentAmountDisplay = new javax.swing.JLabel();
        txtCashPaymentAmount = new javax.swing.JFormattedTextField();
        cardPaymentPanel = new javax.swing.JPanel();
        lblCashPaymentAmountDisplay1 = new javax.swing.JLabel();
        lblCardType = new javax.swing.JLabel();
        cardTypeComboBox = new javax.swing.JComboBox();
        lblCardNo = new javax.swing.JLabel();
        cardpaymentCardNo = new javax.swing.JFormattedTextField();
        txtCardPaymentAmount = new javax.swing.JFormattedTextField();
        coopCreditpaymentPanel = new javax.swing.JPanel();
        PoshanaPayment = new javax.swing.JPanel();
        VoucherPayment = new javax.swing.JPanel();
        btnAddPayment = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();

        setMaximizable(true);
        setResizable(true);
        setTitle("Invoice");
        setMinimumSize(new java.awt.Dimension(994, 728));

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        interfaceContainerPanel.setBorder(dropShadowBorder1);
        interfaceContainerPanel.setOpaque(false);

        cardPanel.setLayout(new java.awt.CardLayout());

        invoicePanel.setLayout(new java.awt.CardLayout());

        invoiceItemTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        invoiceItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Batch", "Name", "Description", "Qty", "Price", "Discount", "Sub total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoiceItemTable.setRowHeight(21);
        invoiceItemTable.getTableHeader().setReorderingAllowed(false);
        itemTableSP.setViewportView(invoiceItemTable);

        btnPayment.setText("Payment  > [ F12 ]");
        btnPayment.setToolTipText("");
        btnPayment.setPreferredSize(new java.awt.Dimension(100, 23));
        btnPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentActionPerformed(evt);
            }
        });

        btnInvoiceCancel.setText("Cancel");
        btnInvoiceCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvoiceCancelActionPerformed(evt);
            }
        });

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder2 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder2.setShadowColor(new java.awt.Color(204, 204, 204));
        dropShadowBorder2.setShadowSize(6);
        dropShadowBorder2.setShowLeftShadow(true);
        dropShadowBorder2.setShowTopShadow(true);
        billButtonPanel.setBorder(dropShadowBorder2);

        btnAddItem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAddItem.setText("Add [Enter]");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        btnDeleteItem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDeleteItem.setText("Delete [Del]");
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });

        btnClearItem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnClearItem.setText("Clear [ESC]");
        btnClearItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout billButtonPanelLayout = new javax.swing.GroupLayout(billButtonPanel);
        billButtonPanel.setLayout(billButtonPanelLayout);
        billButtonPanelLayout.setHorizontalGroup(
            billButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billButtonPanelLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(billButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDeleteItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        billButtonPanelLayout.setVerticalGroup(
            billButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClearItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder3 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder3.setShadowColor(new java.awt.Color(204, 204, 204));
        dropShadowBorder3.setShadowSize(6);
        dropShadowBorder3.setShowLeftShadow(true);
        dropShadowBorder3.setShowTopShadow(true);
        productPanel.setBorder(dropShadowBorder3);

        lblCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCode.setText("Code");

        lblProductDesc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductDesc.setText("Description");

        lblQty.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblQty.setText("Qty.");

        btnLoad.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLoad.setText("Load [ F3 ]");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        btnSearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSearch.setText("Search [ F5 ] ");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        itemCodeComboBox.setEditable(true);
        itemCodeComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        itemCodeComboBox.setMaximumRowCount(5);

        lblUnit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUnit.setToolTipText("");

        txtProductDesc.setEditable(false);
        txtProductDesc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblPrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPrice.setText("Price (Rs.)");

        btnRefresh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setText("Product name");

        txtProductName.setEditable(false);
        txtProductName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPrice.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblAvailableQtydisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblAvailableQtydisplay.setText("Available Qty.");

        txtAvailableQty.setEditable(false);
        txtAvailableQty.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtAvailableQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout productPanelLayout = new javax.swing.GroupLayout(productPanel);
        productPanel.setLayout(productPanelLayout);
        productPanelLayout.setHorizontalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProductName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblQty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblProductDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                            .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblAvailableQtydisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtAvailableQty, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addComponent(txtPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(269, 269, 269)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(productPanelLayout.createSequentialGroup()
                                .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        productPanelLayout.setVerticalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblProductDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAvailableQtydisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAvailableQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblQty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUnit))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        billSummeryPanel.setBackground(new java.awt.Color(204, 204, 204));
        billSummeryPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblItemNoDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemNoDisplay.setText("Item(s)");

        lblitemCount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblitemCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblitemCount.setText("0");
        lblitemCount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblNetDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNetDisplay.setText("Net Total (Rs.)");

        lblNetTotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNetTotal.setForeground(new java.awt.Color(204, 0, 51));
        lblNetTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNetTotal.setText("0.00");
        lblNetTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblItemDiscountDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemDiscountDisplay.setText("Item Discount");

        lblItemDiscounts.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblItemDiscounts.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblItemDiscounts.setText("0.00");
        lblItemDiscounts.setToolTipText("");
        lblItemDiscounts.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout billSummeryPanelLayout = new javax.swing.GroupLayout(billSummeryPanel);
        billSummeryPanel.setLayout(billSummeryPanelLayout);
        billSummeryPanelLayout.setHorizontalGroup(
            billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billSummeryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblItemNoDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblitemCount, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(lblItemDiscountDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblItemDiscounts, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(lblNetDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNetTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        billSummeryPanelLayout.setVerticalGroup(
            billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billSummeryPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(billSummeryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNetTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNetDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemDiscounts, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemDiscountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblitemCount, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemNoDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblBill.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBill.setText("Bill Number : ");

        txtBillNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtBillNumber.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBillNumber.setText("<Bill No>");
        txtBillNumber.setEnabled(false);

        btnReset.setText("Reset");

        javax.swing.GroupLayout itemAddPanelLayout = new javax.swing.GroupLayout(itemAddPanel);
        itemAddPanel.setLayout(itemAddPanelLayout);
        itemAddPanelLayout.setHorizontalGroup(
            itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemTableSP)
                    .addComponent(billSummeryPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(lblBill)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBillNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(billButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(itemAddPanelLayout.createSequentialGroup()
                        .addComponent(btnInvoiceCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        itemAddPanelLayout.setVerticalGroup(
            itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBill)
                    .addComponent(txtBillNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(billButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(itemTableSP, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billSummeryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(itemAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInvoiceCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        invoicePanel.add(itemAddPanel, "itemCard");

        btnPrevious.setText("<  Prevous");
        btnPrevious.setPreferredSize(new java.awt.Dimension(100, 23));
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        paymentOptionsSP.setBorder(null);

        paymentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Payment ID", "Option", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paymentsTable.getTableHeader().setReorderingAllowed(false);
        paymentOptionsSP.setViewportView(paymentsTable);

        javax.swing.GroupLayout paymentOptionsPanelLayout = new javax.swing.GroupLayout(paymentOptionsPanel);
        paymentOptionsPanel.setLayout(paymentOptionsPanelLayout);
        paymentOptionsPanelLayout.setHorizontalGroup(
            paymentOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paymentOptionsSP, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
        );
        paymentOptionsPanelLayout.setVerticalGroup(
            paymentOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paymentOptionsSP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder4 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder4.setShowLeftShadow(true);
        dropShadowBorder4.setShowTopShadow(true);
        paymentInfoPanel.setBorder(dropShadowBorder4);

        lblBillValueDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillValueDisplay.setText("Bill Value");

        lblBillValueVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillValueVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBillValueVal.setText("0.00");
        lblBillValueVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalDisplay.setText("Total");

        lblTotalVal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalVal.setText("0.00");
        lblTotalVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblBillValueDisplay1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillValueDisplay1.setText("Discount %");

        lblBillValueDisplay2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblBillValueDisplay2.setText("Discount value");

        lblChangeDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblChangeDisplay.setForeground(new java.awt.Color(204, 0, 51));
        lblChangeDisplay.setText("Change cash");

        lblChangeVal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblChangeVal.setForeground(new java.awt.Color(204, 0, 51));
        lblChangeVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChangeVal.setText("0.00");
        lblChangeVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtDiscountPercent.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
        txtDiscountPercent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountPercent.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtDiscountVal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
        txtDiscountVal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout paymentInfoPanelLayout = new javax.swing.GroupLayout(paymentInfoPanel);
        paymentInfoPanel.setLayout(paymentInfoPanelLayout);
        paymentInfoPanelLayout.setHorizontalGroup(
            paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBillValueDisplay1)
                    .addComponent(lblBillValueDisplay)
                    .addComponent(lblBillValueDisplay2))
                .addGap(18, 18, 18)
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblBillValueVal, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(txtDiscountPercent)
                    .addComponent(txtDiscountVal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblChangeDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblChangeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTotalDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        paymentInfoPanelLayout.setVerticalGroup(
            paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paymentInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBillValueVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillValueDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBillValueDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiscountPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblChangeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblChangeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTotalVal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTotalDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(paymentInfoPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paymentInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBillValueDisplay2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiscountVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder5 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder5.setShowLeftShadow(true);
        dropShadowBorder5.setShowTopShadow(true);
        paymentSelectorPanel.setBorder(dropShadowBorder5);

        lblPaymentOption.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPaymentOption.setText("Payment Type");

        paymentOptionComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paymentOptionComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash", "Card", "Coop Credit", "Poshana", "Voucher" }));
        paymentOptionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentOptionComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paymentSelectorPanelLayout = new javax.swing.GroupLayout(paymentSelectorPanel);
        paymentSelectorPanel.setLayout(paymentSelectorPanelLayout);
        paymentSelectorPanelLayout.setHorizontalGroup(
            paymentSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentSelectorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPaymentOption)
                .addGap(18, 18, 18)
                .addComponent(paymentOptionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        paymentSelectorPanelLayout.setVerticalGroup(
            paymentSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentSelectorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPaymentOption, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentOptionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder6 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder6.setShowLeftShadow(true);
        dropShadowBorder6.setShowTopShadow(true);
        paymentDetailsPanel.setBorder(dropShadowBorder6);
        paymentDetailsPanel.setLayout(new java.awt.CardLayout());

        lblCashPaymentAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashPaymentAmountDisplay.setText("Amount  (Rs.)");

        txtCashPaymentAmount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#.00"))));
        txtCashPaymentAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCashPaymentAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout cashPaymentPanelLayout = new javax.swing.GroupLayout(cashPaymentPanel);
        cashPaymentPanel.setLayout(cashPaymentPanelLayout);
        cashPaymentPanelLayout.setHorizontalGroup(
            cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCashPaymentAmountDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCashPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(254, Short.MAX_VALUE))
        );
        cashPaymentPanelLayout.setVerticalGroup(
            cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cashPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashPaymentAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCashPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(298, Short.MAX_VALUE))
        );

        paymentDetailsPanel.add(cashPaymentPanel, "cashCard");

        lblCashPaymentAmountDisplay1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashPaymentAmountDisplay1.setText("Amount  (Rs.)");

        lblCardType.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCardType.setText("Card Type");

        cardTypeComboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cardTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Select --", "AMEX", "MASTER", "VISA" }));

        lblCardNo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCardNo.setText("Card No ");

        cardpaymentCardNo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("####"))));
        cardpaymentCardNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cardpaymentCardNo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtCardPaymentAmount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#.00"))));
        txtCardPaymentAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCardPaymentAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout cardPaymentPanelLayout = new javax.swing.GroupLayout(cardPaymentPanel);
        cardPaymentPanel.setLayout(cardPaymentPanelLayout);
        cardPaymentPanelLayout.setHorizontalGroup(
            cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                        .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCashPaymentAmountDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCardPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                        .addComponent(lblCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardpaymentCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(179, Short.MAX_VALUE))
        );
        cardPaymentPanelLayout.setVerticalGroup(
            cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashPaymentAmountDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCardPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardType)
                    .addComponent(cardTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardNo)
                    .addComponent(cardpaymentCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(216, Short.MAX_VALUE))
        );

        paymentDetailsPanel.add(cardPaymentPanel, "bankCard");

        javax.swing.GroupLayout coopCreditpaymentPanelLayout = new javax.swing.GroupLayout(coopCreditpaymentPanel);
        coopCreditpaymentPanel.setLayout(coopCreditpaymentPanelLayout);
        coopCreditpaymentPanelLayout.setHorizontalGroup(
            coopCreditpaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        coopCreditpaymentPanelLayout.setVerticalGroup(
            coopCreditpaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        paymentDetailsPanel.add(coopCreditpaymentPanel, "coopCreditCard");

        javax.swing.GroupLayout PoshanaPaymentLayout = new javax.swing.GroupLayout(PoshanaPayment);
        PoshanaPayment.setLayout(PoshanaPaymentLayout);
        PoshanaPaymentLayout.setHorizontalGroup(
            PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        PoshanaPaymentLayout.setVerticalGroup(
            PoshanaPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        paymentDetailsPanel.add(PoshanaPayment, "poshanaCard");

        javax.swing.GroupLayout VoucherPaymentLayout = new javax.swing.GroupLayout(VoucherPayment);
        VoucherPayment.setLayout(VoucherPaymentLayout);
        VoucherPaymentLayout.setHorizontalGroup(
            VoucherPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        VoucherPaymentLayout.setVerticalGroup(
            VoucherPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        paymentDetailsPanel.add(VoucherPayment, "voucherCard");

        btnAddPayment.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAddPayment.setText("Add Payment [ F3 ]");
        btnAddPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPaymentActionPerformed(evt);
            }
        });

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnConfirm.setText("Confirm [ Enter ]");
        btnConfirm.setPreferredSize(new java.awt.Dimension(100, 23));
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paymentPanelLayout = new javax.swing.GroupLayout(paymentPanel);
        paymentPanel.setLayout(paymentPanelLayout);
        paymentPanelLayout.setHorizontalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paymentInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(paymentPanelLayout.createSequentialGroup()
                                .addComponent(btnAddPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(paymentDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(paymentSelectorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(23, 23, 23)
                        .addComponent(paymentOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paymentPanelLayout.setVerticalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addComponent(paymentSelectorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(paymentOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(paymentInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        invoicePanel.add(paymentPanel, "paymentCard");

        cardPanel.add(invoicePanel, "invoiceCard");

        javax.swing.GroupLayout interfaceContainerPanelLayout = new javax.swing.GroupLayout(interfaceContainerPanel);
        interfaceContainerPanel.setLayout(interfaceContainerPanelLayout);
        interfaceContainerPanelLayout.setHorizontalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE)
        );
        interfaceContainerPanelLayout.setVerticalGroup(
            interfaceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 978, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 753, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(interfaceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1.setLayer(interfaceContainerPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
        // TODO add your handling code here:
        bill_showPaymentScreen();
    }//GEN-LAST:event_btnPaymentActionPerformed

    private void btnInvoiceCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvoiceCancelActionPerformed
        // TODO add your handling code here:'
        bill_cancelBill();
    }//GEN-LAST:event_btnInvoiceCancelActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        // TODO add your handling code here:
        bill_addItemToBillItemTable();
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        // TODO add your handling code here:
        bill_deleteItemFromBillItemTable();
    }//GEN-LAST:event_btnDeleteItemActionPerformed

    private void btnClearItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearItemActionPerformed
        // TODO add your handling code here:
        bill_clearSelectedItemSearch();
    }//GEN-LAST:event_btnClearItemActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        // TODO add your handling code here:
        bill_loadLtemInfoToBill();
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        bill_searchItem();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        bill_showAddItemPanel();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void paymentOptionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentOptionComboBoxActionPerformed
        // TODO add your handling code here:
        bill_togglePaymentOptions(evt);
    }//GEN-LAST:event_paymentOptionComboBoxActionPerformed

    private void btnAddPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPaymentActionPerformed
        // TODO add your handling code here:
        bill_addPaymentOption();
    }//GEN-LAST:event_btnAddPaymentActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        bill_removePaymentOption();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        // TODO add your handling code here:
        bill_confirm();
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        loadSellebleProducts();
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PoshanaPayment;
    private javax.swing.JPanel VoucherPayment;
    private javax.swing.JPanel billButtonPanel;
    private javax.swing.JPanel billSummeryPanel;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnAddPayment;
    private javax.swing.JButton btnClearItem;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnInvoiceCancel;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnPayment;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JPanel cardPaymentPanel;
    private javax.swing.JComboBox cardTypeComboBox;
    private javax.swing.JFormattedTextField cardpaymentCardNo;
    private javax.swing.JPanel cashPaymentPanel;
    private javax.swing.JPanel coopCreditpaymentPanel;
    private javax.swing.JPanel interfaceContainerPanel;
    private javax.swing.JTable invoiceItemTable;
    private javax.swing.JPanel invoicePanel;
    private javax.swing.JPanel itemAddPanel;
    private javax.swing.JComboBox itemCodeComboBox;
    private javax.swing.JScrollPane itemTableSP;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLabel lblAvailableQtydisplay;
    private javax.swing.JLabel lblBill;
    private javax.swing.JLabel lblBillValueDisplay;
    private javax.swing.JLabel lblBillValueDisplay1;
    private javax.swing.JLabel lblBillValueDisplay2;
    private javax.swing.JLabel lblBillValueVal;
    private javax.swing.JLabel lblCardNo;
    private javax.swing.JLabel lblCardType;
    private javax.swing.JLabel lblCashPaymentAmountDisplay;
    private javax.swing.JLabel lblCashPaymentAmountDisplay1;
    private javax.swing.JLabel lblChangeDisplay;
    private javax.swing.JLabel lblChangeVal;
    private javax.swing.JLabel lblCode;
    private javax.swing.JLabel lblItemDiscountDisplay;
    private javax.swing.JLabel lblItemDiscounts;
    private javax.swing.JLabel lblItemNoDisplay;
    private javax.swing.JLabel lblNetDisplay;
    private javax.swing.JLabel lblNetTotal;
    private javax.swing.JLabel lblPaymentOption;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblProductDesc;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblQty;
    private javax.swing.JLabel lblTotalDisplay;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JLabel lblitemCount;
    private javax.swing.JPanel paymentDetailsPanel;
    private javax.swing.JPanel paymentInfoPanel;
    private javax.swing.JComboBox paymentOptionComboBox;
    private javax.swing.JPanel paymentOptionsPanel;
    private javax.swing.JScrollPane paymentOptionsSP;
    private javax.swing.JPanel paymentPanel;
    private javax.swing.JPanel paymentSelectorPanel;
    private javax.swing.JTable paymentsTable;
    private javax.swing.JPanel productPanel;
    private javax.swing.JTextField txtAvailableQty;
    private javax.swing.JTextField txtBillNumber;
    private javax.swing.JFormattedTextField txtCardPaymentAmount;
    private javax.swing.JFormattedTextField txtCashPaymentAmount;
    private javax.swing.JFormattedTextField txtDiscountPercent;
    private javax.swing.JFormattedTextField txtDiscountVal;
    private javax.swing.JFormattedTextField txtPrice;
    private javax.swing.JTextField txtProductDesc;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JFormattedTextField txtQty;
    // End of variables declaration//GEN-END:variables
}
