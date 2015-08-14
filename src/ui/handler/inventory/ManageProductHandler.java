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
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
                    if (pro != null) {
                        gui.getNameSearchField().setText(pro.getProductName());
                        try {
                            displayProduct(pro);
                        } catch (SQLException ex) {
                            logger.error("Database Error : " + ex.getLocalizedMessage());
                        }
                    }
                }
            }

        };

    }

    public void loadProductCombo() throws SQLException {

        products = ProductController.getProductIdentities();

        initiating = true;
        gui.getProductIdCombo().removeAllItems();
        gui.getProductIdCombo().addItem(" ");
        gui.getProductNameCombo().removeAllItems();
        gui.getProductNameCombo().addItem(" ");

        for (Product p : products) {
            gui.getProductIdCombo().addItem(p.getProductId());
            gui.getProductNameCombo().addItem(p.getProductName());
        }
        initiating = false;

    }

    public void loadDepartmentCombo(boolean isID) throws SQLException {

        if (departments.isEmpty()) {
            departments = DepartmentController.getDepartments();
        }

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

    public void loadBatches(int index) throws SQLException {
        if (index > 0) {
            String productId = products.get(index - 1).getProductId();
            String productName = products.get(index - 1).getProductName();
            ((DefaultTableModel) gui.getBatchTable().getModel()).setRowCount(0);
            batches = BatchController.getBatches(productId);

            double qty = 0;
            double totalProfit = 0;
            double profit;

            for (Batch b : batches) {
                System.out.println(b.getBatchId());
                if (b.isInStock()) {
                    System.out.println(b.getBatchId());
                    profit = (b.getUnit_price() - b.getUnit_cost()) * b.getRecievedQuantity();
                    ((DefaultTableModel) (gui.getBatchTable().getModel())).addRow(new String[]{b.getBatchId(), b.getGrnNumber(), "Yasas", "YASAS", b.getUnit_cost() + "", b.getUnit_price() + "", b.getRecievedQuantity() + "", profit + "", Utilities.getStringDate(b.getExpirationDate()), Utilities.getStringDate(b.getNotificationDate())});

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
        } else {
            JOptionPane.showMessageDialog(gui, "Select Product first", "Invalid Selection", 2);
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

        if (pName == null || pName.equals("")) {
            Utilities.ShowErrorMsg(gui.getUpdateProduct(), "Product Name can't be empty");
            return false;
        }
        if (gui.getpBarcodeTB1().getText().trim() == null || gui.getpBarcodeTB1().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Barcode can't be empty.\nEnter the Product Barcode", "Empty Field Error", 2);
            return false;
        }
        try {
            barcode = Long.parseLong(gui.getpBarcodeTB1().getText().trim());
            if (barcode < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getpBarcodeTB1().getText() + "\" is not a valid Barcode.\nEnter a valid Barcode", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getpBarcodeTB1().getText() + "\" is not a valid Barcode.\nEnter a valid Barcode", "Invalid Data", 2);
            return false;
        }
        if (desc == null || desc.equals("")) {
            Utilities.ShowErrorMsg(gui.getUpdateProduct(), "Product Description can't be empty");
            return false;
        }
        try {
            roQty = Double.parseDouble(gui.getRoValue().getText().trim());
            if (roQty < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoValue().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoValue().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
            return false;
        }

        try {
            roValue = Double.parseDouble(gui.getRoQty().getText().trim());
            if (roValue < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoQty().getText() + "\" is not a valid Value.\nEnter a valid Value", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoQty().getText() + "\" is not a valid Value.\nEnter a valid Value", "Invalid Data", 2);
            return false;
        }

        try {
            roMax = Double.parseDouble(gui.getMaxQtyTB1().getText().trim());
            if (roMax < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getMaxQtyTB1().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getMaxQtyTB1().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
            return false;
        }

        try {
            pack = Double.parseDouble(gui.getSizeTB1().getText().trim());
            if (pack < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getSizeTB1().getText() + "\" is not a valid Pack Size.\nEnter a valid Pack Size", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getSizeTB1().getText() + "\" is not a valid Pack Size.\nEnter a valid Pack Size", "Invalid Data", 2);
            return false;
        }

        int index = gui.getDepCombo().getSelectedIndex();
        if (index > 0) {
            depID = departments.get(index - 1).getDepartmentId();
        } else {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Select the Department", "Empty Field Error", 2);
            return false;
        }

        index = gui.getCatCombo().getSelectedIndex();
        if (index > 0) {
            catID = categories.get(index - 1).getCategoryId();
        } else {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Select the Category", "Empty Field Error", 2);
            return false;
        }

        if (!pid.matches("^P[0-9]{5}$") && !pid.isEmpty()) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + pid + "\" is not a valid Product ID.\nEnter a valid Product ID", "Invalid Product ID", 2);
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

        if (pName == null || pName.equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Name can't be empty.\nEnter the Product Name", "Empty Field Error", 2);
            return false;
        }

        if (desc == null || desc.equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Description can't be empty.\nEnter the Product Description", "Empty Field Error", 2);
            return false;
        }

        if (unit == null || unit.equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Unit can't be empty.\nEnter the Product Unit", "Empty Field", 2);
            return false;
        }

        Product product = ProductBuilder.Product().withProductId(pid).withProductName(pName).withDescrition(desc).withBarCode(barcode).withCategory(catID).withDepartment(depID).withUnit(unit).withPackSize(pack).withReorderQuantity(roQty).withReorderValue(roValue).withMaxQuantity(roMax).build();

        if (ProductController.addProduct(product)) {
            loadProductCombo();
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
        }

    }

    public void clearFields() {

        gui.getpDesTB().setText("");
        gui.getpBarcodeTB().setText("");
        gui.getDepIdTB().setText("");
        gui.getDepNameTB().setText("");
        gui.getCatIdTB().setText("");
        gui.getCatNameTB().setText("");
        gui.getSizeTB().setText("0");
        gui.getUnitTB().setText("");
        gui.getRoQtyTB().setText("0");
        gui.getRoValueTB().setText("0");
        gui.getMaxQtyTB().setText("0");
    }

    public String getNextIndex() {
        int index = Utilities.convertKeyToInteger(products.get(products.size() - 1).getProductId());
        return Utilities.convertKeyToString(index + 1, DatabaseInterface.PRODUCT);
    }

    public boolean removeProduct() throws SQLException {
        return ProductController.removeProduct(gui.getProductIdCombo().getSelectedItem().toString());
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

        if (pName == null || pName.equals("")) {
            Utilities.ShowErrorMsg(gui.getUpdateProduct(), "Product Name can't be empty");
            return false;
        }
        if (gui.getpBarcodeTB1().getText().trim() == null || gui.getpBarcodeTB1().getText().trim().equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Barcode can't be empty.\nEnter the Product Barcode", "Empty Field Error", 2);
            return false;
        }
        try {
            barcode = Long.parseLong(gui.getpBarcodeTB1().getText().trim());
            if (barcode < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getpBarcodeTB1().getText() + "\" is not a valid Barcode.\nEnter a valid Barcode", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getpBarcodeTB1().getText() + "\" is not a valid Barcode.\nEnter a valid Barcode", "Invalid Data", 2);
            return false;
        }
        if (desc == null || desc.equals("")) {
            Utilities.ShowErrorMsg(gui.getUpdateProduct(), "Product Description can't be empty");
            return false;
        }
        try {
            roQty = Double.parseDouble(gui.getRoValue().getText().trim());
            if (roQty < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoValue().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoValue().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
            return false;
        }

        try {
            roValue = Double.parseDouble(gui.getRoQty().getText().trim());
            if (roValue < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoQty().getText() + "\" is not a valid Value.\nEnter a valid Value", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getRoQty().getText() + "\" is not a valid Value.\nEnter a valid Value", "Invalid Data", 2);
            return false;
        }

        try {
            roMax = Double.parseDouble(gui.getMaxQtyTB1().getText().trim());
            if (roMax < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getMaxQtyTB1().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getMaxQtyTB1().getText() + "\" is not a valid Quantity.\nEnter a valid Quantity", "Invalid Data", 2);
            return false;
        }

        try {
            pack = Double.parseDouble(gui.getSizeTB1().getText().trim());
            if (pack < 0) {
                JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getSizeTB1().getText() + "\" is not a valid Pack Size.\nEnter a valid Pack Size", "Invalid Data", 2);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + gui.getSizeTB1().getText() + "\" is not a valid Pack Size.\nEnter a valid Pack Size", "Invalid Data", 2);
            return false;
        }

        int index = gui.getDepCombo().getSelectedIndex();
        if (index > 0) {
            depID = departments.get(index - 1).getDepartmentId();
        } else {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Select the Department", "Empty Field Error", 2);
            return false;
        }

        index = gui.getCatCombo().getSelectedIndex();
        if (index > 0) {
            catID = categories.get(index - 1).getCategoryId();
        } else {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Select the Category", "Empty Field Error", 2);
            return false;
        }

        if (!pid.matches("^P[0-9]{5}$") && !pid.isEmpty()) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! \"" + pid + "\" is not a valid Product ID.\nEnter a valid Product ID", "Invalid Product ID", 2);
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

        if (pName == null || pName.equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Name can't be empty.\nEnter the Product Name", "Empty Field Error", 2);
            return false;
        }

        if (desc == null || desc.equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Description can't be empty.\nEnter the Product Description", "Empty Field Error", 2);
            return false;
        }

        if (unit == null || unit.equals("")) {
            JOptionPane.showMessageDialog(gui.getUpdateProduct(), "Error! Product Unit can't be empty.\nEnter the Product Unit", "Empty Field", 2);
            return false;
        }

        Product product = ProductBuilder.Product().withProductId(pid).withProductName(pName).withDescrition(desc).withBarCode(barcode).withCategory(catID).withDepartment(depID).withUnit(unit).withPackSize(pack).withReorderQuantity(roQty).withReorderValue(roValue).withMaxQuantity(roMax).build();

        if (ProductController.updateProduct(product)) {
            loadProductCombo();
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

}
