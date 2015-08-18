/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.inventory;

import controller.inventory.BatchController;
import controller.inventory.CategoryController;
import controller.inventory.CategoryDiscountController;
import controller.inventory.DepartmentController;
import controller.inventory.ProductController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import model.inventory.Batch;
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
public class DiscountInterfaceHandler {

    private final DiscountInterface gui;
    private boolean initiating;

    //category wise discounts
    private ArrayList<Department> departments;
    private ArrayList<Category> categories;

    //batch wise discounts
    private ArrayList<Batch> batches;
    private ArrayList<Product> products;

    public DiscountInterfaceHandler(DiscountInterface gui) {
        this.gui = gui;
        initiating = false;

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
                                    return;
                                }

                            }

                            if (CategoryDiscountController.addCateogryDiscount(categoryDiscount, isReplacing)) {
                                gui.getDiscountError("Discount successfully added");
                                gui.dispose();
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

//    public void saveBatchDiscount() throws SQLException {
//
//        int selIndex = gui.getIdText2().getSelectedIndex() - 1;
//
//        if (selIndex >= 0) {
//            Batch batch = batches.get(selIndex);
//
//            try {
//                double discount = Double.parseDouble(gui.getDisText().getText());
//
//                if (discount < 100 && discount > 0) {
//
//                    Date today = InventoryUtil.getTody();
//                    Date startDate = gui.getStartDate().getDate();
//
//                    if (startDate != null && startDate.compareTo(today) >= 0) {
//
//                        Date endDate = gui.getEndDate().getDate();
//
//                        if (endDate != null && endDate.compareTo(today) >= 0 && endDate.compareTo(startDate) > 0) {
//
//                            CategoryDiscount categoryDiscount = new CategoryDiscount(category, discount, startDate, endDate);
//
//                            boolean promotinal = gui.getProRadio().isSelected();
//                            boolean all = gui.getDisToAll().isSelected();
//
//                            if (!promotinal) {
//
//                                try {
//
//                                    double quantity = Double.parseDouble(gui.getQuantityText().getText());
//
//                                    if (quantity <= 0) {
//                                        gui.getDiscountError("Enter valid Quantity");
//                                        return;
//                                    }
//                                    categoryDiscount.setQuantityDiscount(quantity);
//
//                                } catch (NumberFormatException ex) {
//
//                                    gui.getDiscountError("Enter valid Quantity");
//                                }
//
//                            }
//                            if (!all) {
//
//                                categoryDiscount.setMembersOnly();
//                            }
//
//                            //------------access database--------------------------------------------------
//                            boolean isReplacing = false;
//                            int ans = -1;
//                            if (category.isDiscounted()) {
//
//                                ans = JOptionPane.showConfirmDialog(gui, "This Category is already discounted. \n Do you want to proceed replacing previous discount?", "Discount Confirmation", 2, 3);
//
//                                if (ans == 0) {
//                                    isReplacing = true;
//                                } else if (ans == 2) {
//                                    return;
//                                }
//
//                            }
//
//                            if (CategoryDiscountController.addCateogryDiscount(categoryDiscount, isReplacing)) {
//                                gui.getDiscountError("Discount successfully added");
//                                gui.dispose();
//                            } else {
//                                gui.getDiscountError("Saving the discount failed - UNKNOWN ERROR");
//                            }
//
//                            //-----------------------------------------------------------------------------
//                        } else if (endDate == null) {
//                            gui.getDiscountError("Please select End Date");
//                        } else if (endDate.compareTo(today) <= 0) {
//                            gui.getDiscountError("End Date is Expired");
//                        } else {
//                            gui.getDiscountError("Select End Date after start date");
//
//                        }
//
//                    } else if (startDate == null) {
//                        gui.getDiscountError("Please select Start Date");
//                    } else {
//                        gui.getDiscountError("Start Date is Expired");
//                    }
//
//                } else if (discount == 0) {
//                    gui.getDiscountError("Discount can't be zero");
//
//                } else {
//                    gui.getDiscountError("Enter a Valid Discount");
//                }
//
//            } catch (NumberFormatException ex) {
//                gui.getDiscountError("Enter a Valid Discount");
//            }
//
//        }
//    }
}
