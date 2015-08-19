/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.inventory;

import controller.inventory.CategoryController;
import controller.inventory.CategoryDiscountController;
import controller.inventory.DepartmentController;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.inventory.Category;
import model.inventory.Department;
import ui.view.inventory.ManageDepartment;
import util.Utilities;
import database.connector.DatabaseInterface;
import model.inventory.CategoryDiscount;

/**
 *
 * @author Nadheesh
 */
public class ManageDepartmentHandler {

    private final ManageDepartment gui;
    private ArrayList<Department> departments;
    private ArrayList<Category> categories;
    private ArrayList<CategoryDiscount> categoryDiscounts;

    private boolean initiating;

    public ManageDepartmentHandler(ManageDepartment gui) {
        this.gui = gui;
        initiating = false;

    }

    public void loadDepartmentCombo() throws SQLException {

        departments = DepartmentController.getDepartments();

        initiating = true;
        gui.getDepIdCombo().removeAllItems();
        gui.getDepIdCombo().addItem(" ");
        gui.getDepNameCombo().removeAllItems();
        gui.getDepNameCombo().addItem(" ");

        for (Department d : departments) {
            gui.getDepIdCombo().addItem(d.getDepartmentId());
            gui.getDepNameCombo().addItem(d.getDepartmentName());
        }
        initiating = false;

    }

    public void loadCategories(int index) throws SQLException {
        if (!initiating) {

            String departmentID = departments.get(index - 1).getDepartmentId();
            ((DefaultTableModel) gui.getCategoryTable().getModel()).setRowCount(0);
            categories = CategoryController.getCategories(departmentID);
            categoryDiscounts = CategoryDiscountController.getCategoryDiscountsForDepartment(departmentID);

            int catDisIndex = 0;
            String discount;
            String startDate;
            String endDate;

            for (Category c : categories) {

                if (catDisIndex < categoryDiscounts.size() && c.getCategoryId().equals(categoryDiscounts.get(catDisIndex).getCategoryId())) {
                    discount = categoryDiscounts.get(catDisIndex).getDiscount() + "%";
                    startDate = Utilities.getStringDate(categoryDiscounts.get(catDisIndex).getStartDate());
                    endDate = Utilities.getStringDate(categoryDiscounts.get(catDisIndex).getEndDate());
                    catDisIndex++;
                } else {
                    discount = "None";
                    startDate = "";
                    endDate = "";

                }
                ((DefaultTableModel) (gui.getCategoryTable().getModel())).addRow(new String[]{c.getCategoryId(), c.getCategoryName(), c.getDescription(), discount, startDate, endDate});

            }
        }

    }

    public boolean addDepartment() throws SQLException {

        String depId = gui.getDepIdText().getText().trim();
        String depName = gui.getDepNameText().getText().trim();

        if (depName.equals("")) {
            Utilities.ShowErrorMsg(gui, "Enter a valid Name");
            gui.getDepNameText().requestFocus();
            return false;
        }
        for (Department d : departments) {
            if (d.getDepartmentName().equals(depName)) {
                gui.getDepNameText().requestFocus();
                Utilities.ShowErrorMsg(gui, "The Department already exists");
                return false;
            }
        }
        Department d = new Department(depId, depName);
        if (DepartmentController.addDepartment(d)) {
            loadDepartmentCombo();
            setSelectedDepartment(depName);
            return true;
        }
        Utilities.ShowErrorMsg(gui, "Unknown ERROR");
        return false;
    }

    public boolean updateDepartment() throws SQLException {

        String depId = gui.getDepIdText().getText().trim();
        String depName = gui.getDepNameText().getText().trim();

        if (depName.equals("")) {
            Utilities.ShowErrorMsg(gui, "Enter a valid Name");
            gui.getDepNameText().requestFocus();
            return false;
        }
        if (!depName.equals(gui.getDepNameCombo().getSelectedItem().toString().trim())) {
            for (Department d : departments) {
                if (d.getDepartmentName().equals(depName)) {
                    Utilities.ShowErrorMsg(gui, "The Department already exists");
                    gui.getDepNameText().requestFocus();
                    return false;
                }

            }
        }

        Department d = new Department(depId, depName);
        if (DepartmentController.updateDepartment(d)) {
            loadDepartmentCombo();
            setSelectedDepartment(depName);
            return true;
        }
        Utilities.ShowErrorMsg(gui, "Unknown ERROR");
        return false;

    }

    public boolean removeDepartment() throws SQLException {
        int selectedIndex = gui.getDepIdCombo().getSelectedIndex();

        if (DepartmentController.removeDepartment(gui.getDepIdCombo().getSelectedItem().toString().trim())) {
            loadDepartmentCombo();
            gui.getDepIdCombo().setSelectedIndex(selectedIndex - 1);

            return true;
        }

        return false;
    }

    public boolean addCategory(int index) throws SQLException {

        String cId = gui.getIdText().getText().trim();
        String cName = gui.getNameText().getText().trim();
        String cDesc = gui.getDescText().getText().trim();
        String depId = departments.get(index - 1).getDepartmentId();

        if (cName.equals("") ) {
            Utilities.ShowErrorMsg(gui, "Emtpy Name Field");
            gui.getNameText().requestFocus();
            return false;
        }
        
        if (cDesc.equals("")) {
            Utilities.ShowErrorMsg(gui, "Emtpy Description Field");
            gui.getDescText().requestFocus();
            return false;
        }
        
        for (Category c : categories) {
            if (c.getCategoryName().equals(cName)) {
                Utilities.ShowErrorMsg(gui, "Duplicate Name : " + cName);
                gui.getNameText().requestFocus();
                return false;
            }
        }
        Category category = new Category(cId, cName, depId, cDesc);

        if (CategoryController.addCateogry(category)) {
            loadCategories(index);
            return true;
        }
        return false;
    }

    public boolean editCategory(int index) throws SQLException {

        String cId = gui.getIdText().getText().trim();
        String cName = gui.getNameText().getText().trim();
        String cDesc = gui.getDescText().getText().trim();
        String depId = departments.get(index - 1).getDepartmentId();

        if (cName.equals("")) {
            Utilities.ShowErrorMsg(gui, "Emtpy Name Field");
            gui.getNameText().requestFocus();
            return false;
        }
        
        if (cDesc.equals("")) {
            Utilities.ShowErrorMsg(gui, "Emtpy Description Field");
            gui.getDescText().requestFocus();
            return false;
        }
        if (!cName.equals(gui.getCategoryTable().getValueAt(gui.getCategoryTable().getSelectedRow(), 1).toString().trim())) {
            for (Category c : categories) {
                if (c.getCategoryName().equals(cName)) {
                    Utilities.ShowErrorMsg(gui, "Duplicate Category : " + cName);
                     gui.getNameText().requestFocus();
                    return false;
                }
            }
        }

        Category category = new Category(cId, cName, depId, cDesc);

        if (CategoryController.updateCategory(category)) {
            loadCategories(index);
            return true;
        }
        return false;

    }

    public boolean removeCategory() throws SQLException {
        int selectedIndex = gui.getCategoryTable().getSelectedRow();

        if (CategoryController.removeCategory(categories.get(selectedIndex))) {
            loadCategories(gui.getDepIdCombo().getSelectedIndex());
            return true;
        }
        return false;
    }

    private void setSelectedDepartment(String depName) {
        gui.getDepNameCombo().setSelectedItem(depName);
    }

    public String getDepartmentNextId() {
        if (!departments.isEmpty()) {
            int id = Utilities.convertKeyToInteger(departments.get(departments.size() - 1).getDepartmentId());
            return Utilities.convertKeyToString((id + 1), DatabaseInterface.DEPARTMENT);
        } else {
            return Utilities.convertKeyToString((1), DatabaseInterface.DEPARTMENT);
        }
    }

    public String getCategoryNextId() {
        if (!categories.isEmpty()) {

            int id = Utilities.convertKeyToInteger(categories.get(categories.size() - 1).getCategoryId());
            return Utilities.convertKeyToString((id + 1), DatabaseInterface.CATEGORY);

        } else {
            return Utilities.convertKeyToString((1), DatabaseInterface.CATEGORY);
        }

    }
}
