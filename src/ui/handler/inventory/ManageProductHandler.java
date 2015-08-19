/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.inventory;

import controller.inventory.BatchController;
import controller.inventory.CategoryController;
import controller.inventory.DepartmentController;
import controller.inventory.ProductController;
import database.connector.DatabaseInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.inventory.Batch;
import model.inventory.Category;
import model.inventory.Department;
import model.inventory.Product;
import model.inventory.ProductBuilder;
import org.apache.log4j.Logger;
import ui.view.inventory.ManageProduct;
import util.Utilities;

/**
 *
 * @author Nadheesh
 */
public class ManageProductHandler {

    private final ManageProduct gui;
    private ArrayList<Product> products;
    private ArrayList<Batch> batches;
    private ArrayList<Department> departments;
    private ArrayList<Category> categories;
    private final static Logger logger = Logger.getLogger(ManageProductHandler.class);
    private boolean initiating;
    private boolean depChanged;
    private ActionListener nameItemListener;
    private ActionListener idItemListener;
    private Product selectedPro;

    public ManageProductHandler(ManageProduct gui) {
        this.gui = gui;
        initiating = false;
        departments = new ArrayList<>();

        //action listner for search popup items
        nameItemListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                logger.info("Name menu item was pressed :"
                        + event.getActionCommand());

                String name = event.getActionCommand();

                if (name != null || !name.equals("")) {
                    Product pro = searchProduct(name);
                    selectedPro = pro;
                    if (pro != null) {
                        setSearchFieldTexts();
                        try {
                            displayProduct(pro);
                        } catch (SQLException ex) {
                            logger.error("Database Error : " + ex.getLocalizedMessage());
                        }
                    } else {
                        selectedPro = null;
                        Utilities.ShowErrorMsg(gui, "Error! : The choosed Product is missing");
                    }
                } else {
                    selectedPro = null;
                    setSearchFieldTexts();
                    clearFields();
                }
            }

        };

        idItemListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                logger.info("ID menu item was pressed :"
                        + event.getActionCommand());

                String id = event.getActionCommand();

                if (id != null || !id.equals("")) {
                    Product pro = searchProduct(id);
                    selectedPro = pro;
                    if (pro != null) {
                        setSearchFieldTexts();
                        try {
                            displayProduct(pro);
                        } catch (SQLException ex) {
                            logger.error("Database Error : " + ex.getLocalizedMessage());
                        }
                    } else {
                        Utilities.ShowErrorMsg(gui, "Error! : The choosed Product is missing");
                    }
                } else {
                    selectedPro = null;
                    setSearchFieldTexts();
                    clearFields();
                }
            }

        };

    }

    public void loadProducts() throws SQLException {

        products = ProductController.getProductIdentities();

    }

    public void loadDepartmentCombo(boolean isID) throws SQLException {

        departments = DepartmentController.getDepartments();

        initiating = true;

        gui.getDepCombo().removeAllItems();
        gui.getDepCombo().addItem(" ");

        for (Department d : departments) {
            if (isID) {
                gui.getDepCombo().addItem(d.getDepartmentId());
            } else {
                gui.getDepCombo().addItem(d.getDepartmentName());
            }
        }
        initiating = false;
    }

    public void loadCategories(int index, boolean isID) throws SQLException {
        if (!initiating && index > 0) {
            String departmentID = departments.get(index - 1).getDepartmentId();

            gui.getCatCombo().removeAllItems();
            gui.getCatCombo().addItem(" ");

            categories = CategoryController.getCategories(departmentID);

            for (Category c : categories) {
                if (isID) {
                    gui.getCatCombo().addItem(c.getCategoryId());
                } else {
                    gui.getCatCombo().addItem(c.getCategoryName());
                }

            }
        } else {
            gui.getCatCombo().removeAllItems();
            gui.getCatCombo().addItem(" ");

        }
    }

    public void loadBatches() throws SQLException {

        String productId = selectedPro.getProductId();
        String productName = selectedPro.getProductName();
        ((DefaultTableModel) gui.getBatchTable().getModel()).setRowCount(0);
        batches = BatchController.getBatchesOfProduct(productId);

        double qty = 0;
        double totalProfit = 0;
        double profit;

        for (Batch b : batches) {
            if (b.isInStock()) {

                profit = (b.getUnit_price() - b.getUnit_cost()) * b.getRecievedQuantity();
                ((DefaultTableModel) (gui.getBatchTable().getModel())).addRow(new Object[]{
                    b.getBatchId(),
                    b.getGrnNumber(),
                    b.getSupplierID(),
                    b.getSupplierName(),
                    b.getUnit_cost() + "",
                    b.getUnit_price() + "",
                    b.getRecievedQuantity() + "",
                    profit + "",
                    Utilities.getStringDate(b.getExpirationDate()),
                    Utilities.getStringDate(b.getNotificationDate()),
                    b.isDiscounted()
                });

                qty += b.getRecievedQuantity();
                totalProfit += profit;
            }
        }

        gui.getpIdText().setText(productId);
        gui.getpNameText().setText(productName);

        gui.getTotalQtyText().setText(qty + "");
        gui.getProfitText().setText(totalProfit + "");

        if (gui.getBatchTable().getRowCount() == 0) {
            JOptionPane.showMessageDialog(gui, "Product - " + productName + " is out of stock", "Out of Stock", 1);
        }

    }

    public boolean addProduct() throws SQLException {

        String pid = gui.getpIdTB().getText().trim();

        String pName = gui.getpNameTB().getText().trim();
        String desc = gui.getpDesTB1().getText().trim();
        String unit = gui.getUnitTB1().getText().trim();

        long barcode;
        double roQty;
        double roValue;
        double roMax;
        double pack;

        String depID;
        String catID;

        if (!pid.matches("^P[0-9]{5}$") && !pid.isEmpty()) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + pid + "\" is not a valid Product ID.\nEnter a valid Product ID", "Invalid Product ID", 2);
            gui.getpIdTB().requestFocus();
            return false;
        }

        if (pName == null || pName.equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Name can't be empty.\nEnter the Product Name", "Empty Field Error", 2);
            gui.getpNameTB().requestFocus();
            return false;
        }

        if (gui.getpBarcodeTB1().getText().trim() == null || gui.getpBarcodeTB1().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Barcode can't be empty.\nEnter the Product Barcode", "Empty Field Error", 2);
            gui.getpBarcodeTB1().requestFocus();
            return false;
        }
        try {
            barcode = Long.parseLong(gui.getpBarcodeTB1().getText().trim());
            if (barcode < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getpBarcodeTB1().getText() + "\" is not a valid Barcode.\nEnter a valid Barcode", "Invalid Data", 2);
                gui.getpBarcodeTB1().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getpBarcodeTB1().getText() + "\" is not a valid Barcode.\nEnter a valid Barcode", "Invalid Data", 2);
            gui.getpBarcodeTB1().requestFocus();
            return false;
        }
        if (desc == null || desc.equals("")) {
            Utilities.ShowErrorMsg(gui.getUpdateProduct(), "Product Description can't be empty");
            gui.getpDesTB1().requestFocus();
            return false;
        }
        try {
            roValue = Double.parseDouble(gui.getRoValue().getText().trim());
            if (roValue < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoValue().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
                gui.getRoValue().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoValue().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
            gui.getRoValue().requestFocus();
            return false;
        }

        try {
            roQty = Double.parseDouble(gui.getRoQty().getText().trim());
            if (roQty < 0) {
                gui.getRoQty().requestFocus();
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoQty().getText() + "\" is not a valid Value.\nEnter a valid Value", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoQty().getText() + "\" is not a valid Value.\nEnter a valid Value", "Invalid Data", 2);
            gui.getRoQty().requestFocus();
            return false;
        }

        try {
            roMax = Double.parseDouble(gui.getMaxQtyTB1().getText().trim());
            if (roMax < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getMaxQtyTB1().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
                gui.getMaxQtyTB1().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getMaxQtyTB1().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
            gui.getMaxQtyTB1().requestFocus();
            return false;
        }

        try {
            pack = Double.parseDouble(gui.getSizeTB1().getText().trim());
            if (pack < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getSizeTB1().getText() + "\" is not a valid Pack Size.\nEnter a valid Pack Size", "Invalid Data", 2);
                gui.getSizeTB1().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getSizeTB1().getText() + "\" is not a valid Pack Size.\nEnter a valid Pack Size", "Invalid Data", 2);
            gui.getSizeTB1().requestFocus();
            return false;
        }

        int index = gui.getDepCombo().getSelectedIndex();
        if (index > 0) {
            depID = departments.get(index - 1).getDepartmentId();
        } else {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Select the Department", "Empty Field Error", 2);
            gui.getDepCombo().requestFocus();
            return false;
        }

        index = gui.getCatCombo().getSelectedIndex();
        if (index > 0) {
            catID = categories.get(index - 1).getCategoryId();
        } else {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Select the Category", "Empty Field Error", 2);
            gui.getCatCombo().requestFocus();
            return false;
        }

        if (!catID.matches("^C[0-9]{4}$")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + catID + "\" is not a valid Category ID.\nEnter a valid Category ID", "Invalid Category ID", 2);
            gui.getpIdTB().requestFocus();
            return false;
        }

        if (!depID.matches("^D[0-9]{2}$")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + depID + "\" is not a valid Department ID.\nEnter a valid Department ID", "Invalid Departmentt ID", 2);
            gui.getpIdTB().requestFocus();
            return false;
        }

        if (unit == null || unit.equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Unit can't be empty.\nEnter the Product Unit", "Empty Field", 2);
            gui.getUnitTB1().requestFocus();
            return false;
        }

        Product product = ProductBuilder.Product().withProductId(pid).withProductName(pName).withDescrition(desc).withBarCode(barcode).withCategory(catID).withDepartment(depID).withUnit(unit).withPackSize(pack).withReorderQuantity(roQty).withReorderValue(roValue).withMaxQuantity(roMax).build();

        if (ProductController.addProduct(product)) {
            loadProducts();
            selectedPro = product;
            displayProduct(product);
            setSearchFieldTexts();
            gui.getUpdateProduct().setVisible(false);
            return true;
        }

        JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Unexpected Error", "Error", 0);
        return false;

    }

    public void displayProduct(Product pro) throws SQLException {
        clearFields();

        if (pro != null) {
            Product product = ProductController.getProduct(pro);

            Department department = DepartmentController.getDepartment(product.getDepartmentId());
            Category category = CategoryController.getCategory(product.getDepartmentId(), product.getCategoryId());

            gui.getpDesTB().setText(product.getDescription());
            gui.getpBarcodeTB().setText(product.getProductBarCode() + "");
            gui.getDepIdTB().setText(product.getDepartmentId());
            gui.getDepNameTB().setText(department.getDepartmentName());
            gui.getCatIdTB().setText(product.getCategoryId());
            gui.getCatNameTB().setText(category.getCategoryName());
            gui.getSizeTB().setText(product.getPackSize() + "");
            gui.getUnitTB().setText(product.getUnit());
            gui.getRoQtyTB().setText(product.getReorderQuantity() + "");
            gui.getRoValueTB().setText(product.getReorderValue() + "");
            gui.getMaxQtyTB().setText(product.getMaxQuantity() + "");
            gui.getUnitText().setText(product.getUnit());
            gui.getQtyTB().setText(product.getTotalQuantity() + "");
            if (product.getReorderLevel() > 0) {
                gui.getReOrderLevelTB().setText(product.getReorderLevel() + "");
            } else {
                gui.getReOrderLevelTB().setText("Not Set");
            }
        }

    }

    public void clearFields() {

        gui.getpDesTB().setText("");
        gui.getpBarcodeTB().setText("");
        gui.getDepIdTB().setText("");
        gui.getDepNameTB().setText("");
        gui.getCatIdTB().setText("");
        gui.getCatNameTB().setText("");
        gui.getSizeTB().setText("0.00");
        gui.getUnitTB().setText("");
        gui.getRoQtyTB().setText("0.00");
        gui.getRoValueTB().setText("0.00");
        gui.getMaxQtyTB().setText("0.00");
        gui.getUnitText().setText("");
        gui.getQtyTB().setText("0.00");
        gui.getReOrderLevelTB().setText("0.00");
    }

    public String getNextIndex() {
        if (!products.isEmpty()) {
            int index = Utilities.convertKeyToInteger(products.get(products.size() - 1).getProductId());
            return Utilities.convertKeyToString(index + 1, DatabaseInterface.PRODUCT);
        } else {
            return Utilities.convertKeyToString(1, DatabaseInterface.PRODUCT);
        }
    }

    public boolean removeProduct() throws SQLException {
        if (ProductController.removeProduct(selectedPro.getProductId())) {
            loadProducts();
            selectedPro = null;
            setSearchFieldTexts();
            clearFields();
            return true;
        }
        return false;

    }

    public boolean editProduct() throws SQLException {
        String pid = gui.getpIdTB().getText().trim();

        String pName = gui.getpNameTB().getText().trim();
        String desc = gui.getpDesTB1().getText().trim();
        String unit = gui.getUnitTB1().getText().trim();

        long barcode;
        double roQty;
        double roValue;
        double roMax;
        double pack;

        String depID;
        String catID;

        if (!pid.matches("^P[0-9]{5}$") && !pid.isEmpty()) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + pid + "\" is not a valid Product ID.\nEnter a valid Product ID", "Invalid Product ID", 2);
            return false;
        }

        if (pName == null || pName.equals("")) {
            Utilities.ShowErrorMsg(gui.getUpdateProduct(), "Product Name can't be empty");
            gui.getpNameTB().requestFocus();
            return false;
        }
        if (gui.getpBarcodeTB1().getText().trim() == null || gui.getpBarcodeTB1().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Barcode can't be empty.\nEnter the Product Barcode", "Empty Field Error", 2);
            gui.getpBarcodeTB1().requestFocus();
            return false;
        }
        try {
            barcode = Long.parseLong(gui.getpBarcodeTB1().getText().trim());
            if (barcode < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getpBarcodeTB1().getText() + "\" is not a valid Barcode.\nEnter a valid Barcode", "Invalid Data", 2);
                gui.getpBarcodeTB1().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getpBarcodeTB1().getText() + "\" is not a valid Barcode.\nEnter a valid Barcode", "Invalid Data", 2);
            gui.getpBarcodeTB1().requestFocus();
            return false;
        }
        if (desc == null || desc.equals("")) {
            Utilities.ShowErrorMsg(gui.getUpdateProduct(), "Product Description can't be empty");
            gui.getpDesTB1().requestFocus();
            return false;
        }
        try {
            roValue = Double.parseDouble(gui.getRoValue().getText().trim());
            if (roValue < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoValue().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
                gui.getRoValue().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoValue().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
            gui.getRoValue().requestFocus();
            return false;
        }

        try {
            roQty = Double.parseDouble(gui.getRoQty().getText().trim());
            if (roQty < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoQty().getText() + "\" is not a valid Value.\nEnter a valid Value", "Invalid Data", 2);
                gui.getRoQty().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoQty().getText() + "\" is not a valid Value.\nEnter a valid Value", "Invalid Data", 2);
            gui.getRoQty().requestFocus();
            return false;
        }

        try {
            roMax = Double.parseDouble(gui.getMaxQtyTB1().getText().trim());
            if (roMax < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getMaxQtyTB1().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
                gui.getMaxQtyTB1().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getMaxQtyTB1().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
            gui.getMaxQtyTB1().requestFocus();
            return false;
        }

        try {
            pack = Double.parseDouble(gui.getSizeTB1().getText().trim());
            if (pack < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getSizeTB1().getText() + "\" is not a valid Pack Size.\nEnter a valid Pack Size", "Invalid Data", 2);
                gui.getSizeTB1().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getSizeTB1().getText() + "\" is not a valid Pack Size.\nEnter a valid Pack Size", "Invalid Data", 2);
            gui.getSizeTB1().requestFocus();
            return false;
        }

        int index = gui.getDepCombo().getSelectedIndex();
        if (index > 0) {
            depID = departments.get(index - 1).getDepartmentId();
        } else {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Select the Department", "Empty Field Error", 2);
            gui.getDepCombo().requestFocus();
            return false;
        }

        index = gui.getCatCombo().getSelectedIndex();
        if (index > 0) {
            catID = categories.get(index - 1).getCategoryId();
        } else {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Select the Category", "Empty Field Error", 2);
            gui.getCatCombo().requestFocus();
            return false;
        }

        if (!catID.matches("^C[0-9]{4}$") && !pid.isEmpty()) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + catID + "\" is not a valid Category ID.\nEnter a valid Category ID", "Invalid Category ID", 2);
            return false;
        }

        if (!depID.matches("^D[0-9]{2}$") && !pid.isEmpty()) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + depID + "\" is not a valid Department ID.\nEnter a valid Department ID", "Invalid Departmentt ID", 2);
            return false;
        }

        if (unit == null || unit.equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Unit can't be empty.\nEnter the Product Unit", "Empty Field", 2);
            gui.getUnitTB1().requestFocus();
            return false;
        }

        Product product = ProductBuilder.Product().withProductId(pid).withProductName(pName).withDescrition(desc).withBarCode(barcode).withCategory(catID).withDepartment(depID).withUnit(unit).withPackSize(pack).withReorderQuantity(roQty).withReorderValue(roValue).withMaxQuantity(roMax).build();

        if (ProductController.updateProduct(product)) {
            logger.info("Product Edit Succeded");
            loadProducts();
            displayProduct(ProductController.getProduct(selectedPro.getProductId()));
            selectedPro.setProductName(pName);
            setSearchFieldTexts();
            gui.getUpdateProduct().setVisible(false);
            return true;
        }

        JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Unexpected Error", "Error", 0);
        return false;
    }

    public void searchNameMatches(String name) {

        gui.getNamePopUp().removeAll();

        for (Product p : products) {
            if (p.getProductName().toLowerCase().contains(name.toLowerCase())) {
                JMenuItem item = new JMenuItem(p.getProductName());
                item.addActionListener(nameItemListener);
                gui.getNamePopUp().add(item);
            }
        }
    }

    public void searchIdMatches(String id) {
        gui.getIDPopUp().removeAll();

        for (Product p : products) {

            if (p.getProductId().toLowerCase().contains(id.toLowerCase())) {
                JMenuItem item = new JMenuItem(p.getProductId());
                item.addActionListener(idItemListener);
                gui.getIDPopUp().add(item);
            }
        }
    }

    public Product searchProduct(String text) {

        for (Product p : products) {
            if (p.getProductId().trim().equals(text.trim()) || p.getProductName().trim().equals(text.trim())) {
                return p;
            }
        }
        return null;
    }

    public void setSearchFieldTexts() {

        gui.getNameSearchField().setFindPopupMenu(null);
        gui.getIdSearchField().setFindPopupMenu(null);

        if (selectedPro != null) {
            gui.getNameSearchField().setText(selectedPro.getProductName());
            gui.getIdSearchField().setText(selectedPro.getProductId());
        } else {
            gui.getIdSearchField().setText("");
            gui.getNameSearchField().setText("");
        }

        gui.getNameSearchField().setFindPopupMenu(gui.getNamePopUp());
        gui.getIdSearchField().setFindPopupMenu(gui.getIDPopUp());
    }

    public void showSetNotifiation() {
        gui.getWinSetNotification().setLocationRelativeTo(gui);
        JTable batchTable = gui.getBatchTable();
        if (batchTable.getRowCount() > 0) {
            int row = batchTable.getSelectedRow();
            if (row >= 0) {
                gui.getpNameLable().setText(gui.getpIdText().getText() + "   " + gui.getpNameText().getText());
                gui.getpBatchIdLabel().setText(batchTable.getValueAt(row, 0).toString());
                if (batchTable.getValueAt(row, 8) != null) {
                    gui.getpExpDateLabel().setText(batchTable.getValueAt(row, 8).toString());
                }
                gui.getpSetNotifyBox().setDate(null);
                gui.getWinSetNotification().setVisible(true);
            } else {
                Utilities.ShowWarningMsg(gui, "Select a batch first");
            }
        } else {
            Utilities.ShowErrorMsg(gui, "Table is Empty");
        }
    }

    public void setNotification() throws SQLException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date notifyDate = gui.getpSetNotifyBox().getDate();
        if (gui.getpExpDateLabel().getText() != null || !gui.getpExpDateLabel().getText().equals("")) {
            try {
                if (notifyDate == null) {
                    Utilities.ShowErrorMsg(gui, "Notification Date is not a valid value");
                } else if (notifyDate.after(Utilities.getToday())) {
                    Utilities.ShowErrorMsg(gui, "Notification Date is an expired date");
                } else if (notifyDate.before(sf.parse(gui.getpExpDateLabel().getText()))) {

                    Utilities.ShowErrorMsg(gui, "Notification Date is after Expired date");

                } else {
                    String productId = gui.getpIdText().getText();
                    String batchId = gui.getpBatchIdLabel().getText();
                    BatchController.updateBatchWithNotifyDate(productId, batchId, notifyDate);
                    loadBatches();
                    gui.getWinSetNotification().setVisible(false);
                }
            } catch (ParseException ex) {
                Utilities.ShowErrorMsg(gui, "Notification Date is not a valid value");
            }
        }

    }

    public int getDepartment() {
        if (departments != null) {
            return departments.size();
        }
        return 0;
    }

    public int getCategories() {
        if (categories != null) {
            return categories.size();
        }
        return 0;
    }

}
