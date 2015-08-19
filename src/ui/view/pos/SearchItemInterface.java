package ui.view.pos;

import controller.inventory.CategoryController;
import controller.inventory.DepartmentController;
import controller.inventory.ProductController;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import model.inventory.Category;
import model.inventory.Department;
import model.inventory.Product;
import org.apache.log4j.Logger;
import util.KeyValueContainer;
import util.Utilities;

class SearchItemInterface extends javax.swing.JInternalFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(SearchItemInterface.class);

    private final InvoiceInternalInterface invoiceInterface;
    private final CheckStockInterface checkStockInterface;

    //
    private DefaultComboBoxModel departmentComboBoxModel;
    private DefaultComboBoxModel categoryComboBoxModel;
    private ActionListener departmentComboBoxListner;
    private DefaultTableModel productTableModel;

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
// <editor-fold defaultstate="collapsed" desc="Key Bindings "> 
    private void performKeyBinding() {

        InputMap inputMap = containerPanel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = containerPanel.getActionMap();

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
            if (cmd.equalsIgnoreCase("Escape")) {
                logger.debug("CashWithdrawal Dialog - Escape Pressed ");
                cancelSearch();
            }
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Helper Methods">
    private void initUI(JDesktopPane desktopPane) {
        initComponents();
        Dimension desktopSize = desktopPane.getSize();
        Dimension jInternalFrameSize = this.getSize();
        this.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        this.departmentComboBoxModel = new DefaultComboBoxModel();
        this.categoryComboBoxModel = new DefaultComboBoxModel();
        this.productTableModel = (DefaultTableModel) productInfoTable.getModel();

        departmentComboBoxListner = (ActionEvent e) -> {
            if (departmentComboBox.getSelectedIndex() > -1) {
                setCategories(((KeyValueContainer) departmentComboBox.getSelectedItem()).getKey());
            }
        };
        departmentComboBox.addActionListener(departmentComboBoxListner);

        productInfoTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2) {
                    selectItem();
                }
            }
        });

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
        setDepartments();

        performKeyBinding();

    }

    //load departments to combo box
    private void setDepartments() {
        logger.debug("setDepartments invoked");

        try {
            departmentComboBox.removeActionListener(departmentComboBoxListner);
            departmentComboBoxModel.removeAllElements();
            ArrayList<Department> departments = DepartmentController.getDepartments();
            departments.stream().forEach((department) -> {
                departmentComboBoxModel.addElement(new KeyValueContainer(department.getDepartmentId(), department.getDepartmentName()));
            });

            departmentComboBox.setModel(departmentComboBoxModel);
            departmentComboBox.setSelectedIndex(-1);
            categoryComboBox.setSelectedIndex(-1);
            departmentComboBox.addActionListener(departmentComboBoxListner);
        } catch (SQLException ex) {
            logger.error("SQL Error : " + ex.getMessage());
        }
    }

    //load categories to 
    private void setCategories(String departmentId) {
        logger.debug("setCategories invoked");

        try {
            categoryComboBoxModel.removeAllElements();
            ArrayList<Category> categories = CategoryController.getCategories(departmentId);
            categories.stream().forEach((category) -> {
                categoryComboBoxModel.addElement(new KeyValueContainer(category.getCategoryId(), category.getCategoryName()));
            });

            categoryComboBox.setModel(categoryComboBoxModel);
            categoryComboBox.setSelectedIndex(-1);

        } catch (SQLException ex) {
            logger.error("SQL Error : " + ex.getMessage());
        }
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

    //Set the search criteria
    private void setSearchMethod() {
        txtProduct.setEnabled(chkBoxProduct.isSelected());

        departmentComboBox.setEnabled(chkBoxDepartment.isSelected());
        chkBoxCategory.setEnabled(chkBoxDepartment.isSelected());
        categoryComboBox.setEnabled(chkBoxDepartment.isSelected() && chkBoxCategory.isSelected());

        btnSearch.setEnabled(chkBoxProduct.isSelected() || chkBoxDepartment.isSelected());
        if (!chkBoxDepartment.isSelected()) {
            departmentComboBox.setSelectedIndex(-1);
        }
        if (!chkBoxCategory.isSelected()) {
            categoryComboBox.setSelectedIndex(-1);
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Main Methods"> 
    //Perform search
    private void search() {
        logger.debug("search invoked");

        try {

            btnSelect.setEnabled(false);
            //Validations
            String searchQuery = txtProduct.getText();
            String departmentId;
            String categoryId;

            if (chkBoxProduct.isSelected() && searchQuery.isEmpty()) {
                Utilities.showMsgBox("Please enter a valid query", "Not enough data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (chkBoxDepartment.isSelected() && departmentComboBox.getSelectedIndex() < 0) {
                Utilities.showMsgBox("Please select a department", "Not enough data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (chkBoxCategory.isSelected() && categoryComboBox.getSelectedIndex() < 0) {
                Utilities.showMsgBox("Please select a category", "Not enough data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            productTableModel.setRowCount(0);

            if (chkBoxProduct.isSelected() && chkBoxDepartment.isSelected() && chkBoxCategory.isSelected()) {
                logger.debug("Product, Department , Category search");

                departmentId = ((KeyValueContainer) departmentComboBox.getSelectedItem()).getKey();
                categoryId = ((KeyValueContainer) categoryComboBox.getSelectedItem()).getKey();

                ArrayList<Product> searchResults = ProductController.searchProducts(searchQuery, departmentId, categoryId);
                if (searchResults.isEmpty()) {
                    Utilities.showMsgBox("No product found", "Empty result", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                for (Product product : searchResults) {
                    Object[] ob = {
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getProductBarCode(),
                        product.getPackSize(),
                        product.getUnit()
                    };
                    productTableModel.addRow(ob);
                }
                btnSelect.setEnabled(true);

            } else if (chkBoxDepartment.isSelected() && chkBoxCategory.isSelected()) {
                logger.debug("Department,Category search");

                departmentId = ((KeyValueContainer) departmentComboBox.getSelectedItem()).getKey();
                categoryId = ((KeyValueContainer) categoryComboBox.getSelectedItem()).getKey();

                ArrayList<Product> searchResults = ProductController.searchProducts("", departmentId, categoryId);
                if (searchResults.isEmpty()) {
                    Utilities.showMsgBox("No product found", "Empty result", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                for (Product product : searchResults) {
                    Object[] ob = {
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getProductBarCode(),
                        product.getPackSize(),
                        product.getUnit()
                    };
                    productTableModel.addRow(ob);

                }
                btnSelect.setEnabled(true);
            } else if (chkBoxProduct.isSelected() && chkBoxDepartment.isSelected()) {
                logger.debug(" Product, Department search");

                departmentId = ((KeyValueContainer) departmentComboBox.getSelectedItem()).getKey();

                ArrayList<Product> searchResults = ProductController.searchProducts(searchQuery, departmentId);
                if (searchResults.isEmpty()) {
                    Utilities.showMsgBox("No product found", "Empty result", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                for (Product product : searchResults) {
                    Object[] ob = {
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getProductBarCode(),
                        product.getPackSize(),
                        product.getUnit()
                    };
                    productTableModel.addRow(ob);

                }
                btnSelect.setEnabled(true);
            } else if (chkBoxProduct.isSelected()) {
                logger.debug(" Product search");

                ArrayList<Product> searchResults = ProductController.searchProducts(searchQuery);
                if (searchResults.isEmpty()) {
                    Utilities.showMsgBox("No product found", "Empty result", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                for (Product product : searchResults) {
                    Object[] ob = {
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getProductBarCode(),
                        product.getPackSize(),
                        product.getUnit()
                    };
                    productTableModel.addRow(ob);

                }
                btnSelect.setEnabled(true);
            } else if (chkBoxDepartment.isSelected()) {
                logger.debug("Department search");

                departmentId = ((KeyValueContainer) departmentComboBox.getSelectedItem()).getKey();

                ArrayList<Product> searchResults = ProductController.searchProducts("", departmentId);
                if (searchResults.isEmpty()) {
                    Utilities.showMsgBox("No product found", "Empty result", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                for (Product product : searchResults) {
                    Object[] ob = {
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getProductBarCode(),
                        product.getPackSize(),
                        product.getUnit()
                    };
                    productTableModel.addRow(ob);

                }
                btnSelect.setEnabled(true);
            } else {
                Utilities.showMsgBox("Please select at least one search criteria", "No search criteria defined", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            logger.error("SQL Error : " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("Error while searching : " + ex.getMessage(), ex);
        }
    }

    //Clear fields
    private void clear() {
        logger.warn("clear invoked");

        productTableModel.setRowCount(0);
        departmentComboBox.setSelectedIndex(-1);
        categoryComboBox.setSelectedIndex(-1);
        txtProduct.setText("");
        txtProduct.requestFocus();
        btnSelect.setEnabled(false);
    }

    //Select Item
    private void selectItem() {
        logger.debug("selectItem Invoked");

        //get selected item from table
        int row = productInfoTable.getSelectedRow();
        int column = 0;
        if (row < 0) {
            Utilities.showMsgBox("Please select a product first", "no product selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String productId = productInfoTable.getValueAt(row, column).toString();

        if (invoiceInterface != null) {

            invoiceInterface.disableGlassPane(false);
            invoiceInterface.setSelectedProduct(productId);
        }
        if (checkStockInterface != null) {
            checkStockInterface.disableGlassPane();
            checkStockInterface.setSelectedProduct(productId);
        }

        this.dispose();
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

        btnGroup = new javax.swing.ButtonGroup();
        containerPanel = new javax.swing.JPanel();
        lblProduct = new javax.swing.JLabel();
        txtProduct = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        lblDepartment = new javax.swing.JLabel();
        lblCategory = new javax.swing.JLabel();
        departmentComboBox = new javax.swing.JComboBox();
        categoryComboBox = new javax.swing.JComboBox();
        itemInfoPanel = new javax.swing.JPanel();
        itemInfoSP = new javax.swing.JScrollPane();
        productInfoTable = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        btnSelect = new javax.swing.JButton();
        chkBoxProduct = new javax.swing.JCheckBox();
        chkBoxDepartment = new javax.swing.JCheckBox();
        chkBoxCategory = new javax.swing.JCheckBox();
        btnRefresh = new javax.swing.JButton();

        setClosable(true);
        setTitle("Search Item");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        containerPanel.setBorder(dropShadowBorder1);

        lblProduct.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblProduct.setText("Product");

        txtProduct.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

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

        departmentComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        departmentComboBox.setMaximumRowCount(5);
        departmentComboBox.setEnabled(false);

        categoryComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        categoryComboBox.setMaximumRowCount(5);
        categoryComboBox.setEnabled(false);

        productInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Code", "Name", "Description", "Barcode", "Pack Size", "Unit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productInfoTable.getTableHeader().setReorderingAllowed(false);
        itemInfoSP.setViewportView(productInfoTable);
        if (productInfoTable.getColumnModel().getColumnCount() > 0) {
            productInfoTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            productInfoTable.getColumnModel().getColumn(1).setPreferredWidth(125);
            productInfoTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            productInfoTable.getColumnModel().getColumn(3).setPreferredWidth(75);
            productInfoTable.getColumnModel().getColumn(4).setPreferredWidth(50);
            productInfoTable.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        javax.swing.GroupLayout itemInfoPanelLayout = new javax.swing.GroupLayout(itemInfoPanel);
        itemInfoPanel.setLayout(itemInfoPanelLayout);
        itemInfoPanelLayout.setHorizontalGroup(
            itemInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemInfoSP, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
        );
        itemInfoPanelLayout.setVerticalGroup(
            itemInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemInfoSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
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
        btnSelect.setEnabled(false);
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });

        chkBoxProduct.setSelected(true);
        chkBoxProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxProductActionPerformed(evt);
            }
        });

        chkBoxDepartment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkBoxDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxDepartmentActionPerformed(evt);
            }
        });

        chkBoxCategory.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chkBoxCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxCategoryActionPerformed(evt);
            }
        });

        btnRefresh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPanelLayout.createSequentialGroup()
                        .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkBoxProduct)
                            .addComponent(chkBoxDepartment)
                            .addComponent(chkBoxCategory, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(containerPanelLayout.createSequentialGroup()
                                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblProduct))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(containerPanelLayout.createSequentialGroup()
                                        .addComponent(txtProduct)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(containerPanelLayout.createSequentialGroup()
                                        .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 195, Short.MAX_VALUE)
                                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(containerPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(containerPanelLayout.createSequentialGroup()
                                .addComponent(lblDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(departmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        containerPanelLayout.setVerticalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkBoxProduct, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(departmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chkBoxDepartment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkBoxCategory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(itemInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void chkBoxProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxProductActionPerformed
        // TODO add your handling code here:
        setSearchMethod();
    }//GEN-LAST:event_chkBoxProductActionPerformed

    private void chkBoxDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxDepartmentActionPerformed
        // TODO add your handling code here:
        setSearchMethod();
    }//GEN-LAST:event_chkBoxDepartmentActionPerformed

    private void chkBoxCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxCategoryActionPerformed
        // TODO add your handling code here:
        setSearchMethod();
    }//GEN-LAST:event_chkBoxCategoryActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        setDepartments();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        cancelSearch();
    }//GEN-LAST:event_formInternalFrameClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSelect;
    private javax.swing.JComboBox categoryComboBox;
    private javax.swing.JCheckBox chkBoxCategory;
    private javax.swing.JCheckBox chkBoxDepartment;
    private javax.swing.JCheckBox chkBoxProduct;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JComboBox departmentComboBox;
    private javax.swing.JPanel itemInfoPanel;
    private javax.swing.JScrollPane itemInfoSP;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblProduct;
    private javax.swing.JTable productInfoTable;
    private javax.swing.JTextField txtProduct;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
}
