/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.inventory;

import controller.inventory.BatchController;
import controller.inventory.BatchDiscountController;
import controller.inventory.CategoryController;
import controller.inventory.CategoryDiscountController;
import controller.inventory.DepartmentController;
import controller.inventory.ProductController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.inventory.Batch;
import model.inventory.BatchDiscount;
import model.inventory.Category;
import model.inventory.CategoryDiscount;
import model.inventory.Department;
import model.inventory.Product;
import ui.view.inventory.DiscountInterface;
import util.Utilities;

/**
 *
 * @author Nadheesh
 */
public final class DiscountInterfaceHandler {

    private final DiscountInterface gui;
    private boolean initiating;

    //category wise discounts
    private ArrayList<Department> departments;
    private ArrayList<Category> categories;

    //batch wise discounts
    private ArrayList<Batch> batches;
    private ArrayList<Product> products;

    //discount detail arrays
    private ArrayList<CategoryDiscount> catDiscounts;
    private ArrayList<BatchDiscount> batchDiscounts;

    public DiscountInterfaceHandler(DiscountInterface gui) {
        this.gui = gui;
        initiating = false;

        try {

            departments = DepartmentController.getDepartments();
            products = ProductController.getProductIdentities();

            loadTableWithCategoryDiscount();
        } catch (SQLException ex) {
            Logger.getLogger(DiscountInterfaceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadProductCombo() throws SQLException {

        products = ProductController.getProductIdentities();

        initiating = true;

        gui.getIdText1().removeAllItems();
        gui.getIdText1().addItem(" ");
        gui.getNameText1().removeAllItems();
        gui.getNameText1().addItem(" ");

        for (Product p : products) {

            gui.getIdText1().addItem(p.getProductId());
            gui.getNameText1().addItem(p.getProductName());
        }
        initiating = false;
    }

    public void loadDepartmentCombo() throws SQLException {

        departments = DepartmentController.getDepartments();

        initiating = true;
        gui.getIdText1().removeAllItems();
        gui.getIdText1().addItem(" ");
        gui.getNameText1().removeAllItems();
        gui.getNameText1().addItem(" ");

        for (Department d : departments) {

            gui.getIdText1().addItem(d.getDepartmentId());
            gui.getNameText1().addItem(d.getDepartmentName());
        }
        initiating = false;
    }

    public void loadCategories(int index) throws SQLException {

        if (!initiating) {

            String departmentID = departments.get(index - 1).getDepartmentId();
            categories = CategoryController.getCategories(departmentID);

            gui.getIdText2().removeAllItems();
            gui.getIdText2().addItem(" ");
            gui.getNameText2().removeAllItems();
            gui.getNameText2().addItem(" ");

            for (Category c : categories) {

                gui.getIdText2().addItem(c.getCategoryId());
                gui.getNameText2().addItem(c.getCategoryName());
            }
        }

    }

    public void loadBatches(int index) throws SQLException {

        if (!initiating) {
            batches = BatchController.getBatches(products.get(index - 1).getProductId());

            gui.getIdText2().removeAllItems();
            gui.getIdText2().addItem(" ");

            for (Batch b : batches) {

                gui.getIdText2().addItem(b.getBatchId());

            }
        }
    }

    public void saveCategoryDiscount() throws SQLException {

        int selIndex = gui.getIdText2().getSelectedIndex() - 1;

        if (selIndex >= 0) {
            Category category = categories.get(selIndex);

            try {
                double discount = Double.parseDouble(gui.getDisText().getText());

                if (discount < 100 && discount > 0) {

                    Date today = Utilities.getToday();
                    Date startDate = gui.getStartDate().getDate();

                    if (startDate != null && startDate.compareTo(today) >= 0) {

                        Date endDate = gui.getEndDate().getDate();

                        if (endDate != null && endDate.compareTo(today) >= 0 && endDate.compareTo(startDate) > 0) {

                            CategoryDiscount categoryDiscount = new CategoryDiscount(category, discount, startDate, endDate);

                            boolean promotinal = gui.getProRadio().isSelected();
                            boolean all = gui.getDisToAll().isSelected();

                            if (!promotinal) {
                                try {
                                    double quantity = Double.parseDouble(gui.getQuantityText().getText());

                                    if (quantity <= 0) {
                                        gui.getDiscountError("Enter valid Quantity");
                                        return;
                                    }
                                    categoryDiscount.setQuantityDiscount(quantity);

                                } catch (NumberFormatException ex) {

                                    gui.getDiscountError("Enter valid Quantity");
                                    
                                }
                            }
                            if (!all) {
                                categoryDiscount.setMembersOnly();
                            }

                            //------------access database--------------------------------------------------
                            boolean isReplacing = false;
                            int ans = -1;
                            if (category.isDiscounted()) {

                                ans = JOptionPane.showConfirmDialog(gui, "This Category is already discounted. \n Do you want to proceed replacing previous discount?", "Discount Confirmation", 2, 3);

                                if (ans == 0) {
                                    isReplacing = true;
                                } else if (ans == 2) {
                                    gui.getWinAddDiscount().setVisible(false);
                                    return;
                                }

                            }

                            if (deleteBatchDiscountsOnUserRequest(category)) {

                                if (CategoryDiscountController.addCateogryDiscount(categoryDiscount, isReplacing)) {
                                    gui.getDiscountError("Discount successfully added");
                                    gui.getWinAddDiscount().setVisible(false);
                                    loadTableWithCategoryDiscount();
                                } else {
                                    gui.getDiscountError("Saving the discount failed - UNKNOWN ERROR");
                                }
                            } else {
                                return;
                            }
                            //-----------------------------------------------------------------------------
                        } else if (endDate == null) {
                            gui.getDiscountError("Please select End Date");
                        } else if (endDate.compareTo(today) <= 0) {
                            gui.getDiscountError("End Date is Expired");
                        } else {
                            gui.getDiscountError("Select End Date after start date");

                        }

                    } else if (startDate == null) {
                        gui.getDiscountError("Please select Start Date");
                    } else {
                        gui.getDiscountError("Start Date is Expired");
                    }

                } else if (discount == 0) {
                    gui.getDiscountError("Discount can't be zero");

                } else {
                    gui.getDiscountError("Enter a Valid Discount");
                }

            } catch (NumberFormatException ex) {
                gui.getDiscountError("Enter a Valid Discount");
            }

        } else {
            gui.getDiscountError("Please Select a Department and a Catogery First");
        }

    }

    public void saveBatchDiscount() throws SQLException {

        int selIndex = gui.getIdText2().getSelectedIndex() - 1;

        if (selIndex >= 0) {
            Batch batch = batches.get(selIndex);

            try {
                double discount = Double.parseDouble(gui.getDisText().getText());

                if (discount < 100 && discount > 0) {

                    Date today = Utilities.getToday();
                    Date startDate = gui.getStartDate().getDate();

                    if (startDate != null && startDate.compareTo(today) >= 0) {

                        Date endDate = gui.getEndDate().getDate();

                        if (endDate != null && endDate.compareTo(today) >= 0 && endDate.compareTo(startDate) > 0) {

                            BatchDiscount batchDiscount = new BatchDiscount(batch, discount, startDate, endDate);

                            boolean promotinal = gui.getProRadio().isSelected();
                            boolean all = gui.getDisToAll().isSelected();

                            if (!promotinal) {

                                try {

                                    double quantity = Double.parseDouble(gui.getQuantityText().getText());

                                    if (quantity <= 0) {
                                        gui.getDiscountError("Enter valid Quantity");
                                        return;
                                    }
                                    batchDiscount.setQuantity(quantity);

                                } catch (NumberFormatException ex) {

                                    gui.getDiscountError("Enter valid Quantity");
                                }

                            }
                            if (!all) {

                                batchDiscount.setMembersOnly(true);
                            }

                            //------------access database--------------------------------------------------
                            boolean isReplacing = false;
                            int ans = -1;
                            if (batch.isDiscounted()) {

                                ans = JOptionPane.showConfirmDialog(gui, "This Batch is already discounted. \n Do you want to proceed replacing previous discount?", "Discount Confirmation", 2, 3);

                                if (ans == 0) {
                                    isReplacing = true;
                                } else if (ans == 2) {
                                    gui.getWinAddDiscount().setVisible(false);
                                    return;
                                }

                            }

                            if (BatchDiscountController.addBatchDiscount(batchDiscount, isReplacing)) {
                                gui.getDiscountError("Discount successfully added");
                                gui.getWinAddDiscount().setVisible(false);
                                loadTableWithBatchDiscount();
                            } else {
                                gui.getDiscountError("Saving the discount failed - UNKNOWN ERROR");
                            }

                            //-----------------------------------------------------------------------------
                        } else if (endDate == null) {
                            gui.getDiscountError("Please select End Date");
                        } else if (endDate.compareTo(today) <= 0) {
                            gui.getDiscountError("End Date is Expired");
                        } else {
                            gui.getDiscountError("Select End Date after start date");

                        }

                    } else if (startDate == null) {
                        gui.getDiscountError("Please select Start Date");
                    } else {
                        gui.getDiscountError("Start Date is Expired");
                    }

                } else if (discount == 0) {
                    gui.getDiscountError("Discount can't be zero");

                } else {
                    gui.getDiscountError("Enter a Valid Discount");
                }

            } catch (NumberFormatException ex) {
                gui.getDiscountError("Enter a Valid Discount");
            }

        }
    }

    public void loadTableWithCategoryDiscount() throws SQLException {

        catDiscounts = CategoryDiscountController.getCategoryDiscounts();
        int depIndex = -1;
        String depName = "";
        int newIndex;
        int catIndex;
        String disType = "";
        String qty = "";

        ((DefaultTableModel) (gui.getCatDisTable().getModel())).setRowCount(0);

        for (CategoryDiscount cd : catDiscounts) {

            newIndex = Utilities.convertKeyToInteger(cd.getDepartmentId());
            if (depIndex != newIndex) {
                depIndex = newIndex;
                depName = departments.get(depIndex - 1).getDepartmentName();
                categories = CategoryController.getCategories(cd.getDepartmentId());
            }

            if (cd.isPromotional()) {
                disType = "Promotional";
                qty = "N/A";
            } else {
                disType = "Quantity";
                qty = cd.getQuantity() + "";
            }
            catIndex = Utilities.convertKeyToInteger(cd.getCategoryId());

            ((DefaultTableModel) (gui.getCatDisTable().getModel())).addRow(new Object[]{
                cd.getDepartmentId() + "-" + cd.getCategoryId(),
                categories.get(catIndex - 1).getCategoryName(),
                depName,
                cd.getDiscount() + "",
                Utilities.getStringDate(cd.getStartDate()),
                Utilities.getStringDate(cd.getEndDate()),
                disType,
                qty,
                cd.isMembersOnly()
            });
        }
    }

    public void loadTableWithBatchDiscount() throws SQLException {

        batchDiscounts = BatchDiscountController.getAllBatchDiscounts();

        String disType = "";
        String qty = "";

        ((DefaultTableModel) (gui.getBatchDisTable().getModel())).setRowCount(0);

        for (BatchDiscount bd : batchDiscounts) {

            if (bd.isPromotional()) {
                disType = "Promotional";
                qty = "N/A";
            } else {
                disType = "Quantity";
                qty = bd.getQuantity() + "";
            }

            ((DefaultTableModel) (gui.getBatchDisTable().getModel())).addRow(new Object[]{
                bd.getProductId(),
                bd.getProductName(),
                bd.getBatchId(),
                bd.getDiscount(),
                Utilities.getStringDate(bd.getStartDate()),
                Utilities.getStringDate(bd.getEndDate()),
                disType,
                qty,
                bd.isMembersOnly()
            });
        }

    }

    private boolean deleteBatchDiscountsOnUserRequest(Category category) throws SQLException {

        ArrayList<BatchDiscount> bDiscounts = BatchDiscountController.getAllBatchDiscountsInCategory(category.getDepartmentId(), category.getCategoryId());
        ((DefaultTableModel) (gui.getRemovetBatchDisTable().getModel())).setRowCount(0);

        if (!bDiscounts.isEmpty()) {
            gui.getWinAddDiscount().setVisible(false);
            gui.getCatIdLabel().setText(category.getCategoryId());
            gui.getCatNameLabel().setText(category.getCategoryName());

            int choice = JOptionPane.showConfirmDialog(gui, "This category contains Batch(es) already discounted. \n"
                    + "1)Select \'Yes\' to select batches to remove discounts \n"
                    + "2)Select 'No' to continue without removing batches \n"
                    + "3)Select 'Cancel' to cancle the proceedure",
                    "Remove Batch Discounts", 1);

            if (choice == 0) {
                String disType = "";
                String qty = "";

                for (BatchDiscount bd : bDiscounts) {

                    if (bd.isPromotional()) {
                        disType = "Promotional";
                        qty = "N/A";
                    } else {
                        disType = "Quantity";
                        qty = bd.getQuantity() + "";
                    }

                    ((DefaultTableModel) (gui.getRemovetBatchDisTable().getModel())).addRow(new Object[]{
                        false,
                        bd.getProductId(),
                        bd.getProductName(),
                        bd.getBatchId(),
                        bd.getDiscount(),
                        Utilities.getStringDate(bd.getStartDate()),
                        Utilities.getStringDate(bd.getEndDate()),
                        disType,
                        qty,
                        bd.isMembersOnly()
                    });

                }
                gui.getWinRemoveBatch().setLocationRelativeTo(gui);
                gui.getWinRemoveBatch().setVisible(true);
                return true;

            } else if (choice == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    public void removeSelectedBatchDiscounts() throws SQLException {

        JTable table = gui.getRemovetBatchDisTable();
        for (int i = 0; i < table.getRowCount(); i++) {
            if ((boolean) table.getValueAt(i, 0)) {

                BatchDiscountController.removeBatchDiscount(table.getValueAt(i, 3).toString(), table.getValueAt(i, 1).toString());
            }
        }

        gui.getWinRemoveBatch().setVisible(false);
    }

    public void removeCategoryDiscount() throws SQLException {

        int row = gui.getCatDisTable().getSelectedRow();

        if (row >= 0) {
            int choice = JOptionPane.showConfirmDialog(gui,
                    "Do you want to Remove discount of : " + gui.getCatDisTable().getValueAt(row, 1).toString() + " ?",
                    "Remove Discout", 0);

            if (choice == 0) {
                String[] id = gui.getCatDisTable().getValueAt(row, 0).toString().split("-");
                CategoryDiscountController.removeCateogryDiscount(id[1], id[0]);
                loadTableWithCategoryDiscount();
            }
        }
    }

    public void removeBatchDiscount() throws SQLException {

        int row = gui.getBatchDisTable().getSelectedRow();

        if (row >= 0) {
            int choice = JOptionPane.showConfirmDialog(gui,
                    "Do you want to Remove discount of : " + gui.getBatchDisTable().getValueAt(row, 1).toString() + " ?",
                    "Remove Discout", 0);
            if (choice == 0) {
                String[] id = gui.getBatchDisTable().getValueAt(row, 0).toString().split("-");
                BatchDiscountController.removeBatchDiscount(gui.getBatchDisTable().getValueAt(row, 2).toString().trim(), gui.getBatchDisTable().getValueAt(row, 0).toString().trim());
                loadTableWithBatchDiscount();
            }
        }
    }

}
