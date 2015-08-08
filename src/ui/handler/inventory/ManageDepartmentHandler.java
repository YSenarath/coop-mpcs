/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.inventory;

import controller.inventory.CategoryController;
import controller.inventory.DepartmentController;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.inventory.Category;
import model.inventory.Department;
import ui.view.inventory.ManageDepartment;
import util.Utilities;
import database.connector.DatabaseInterface;

/**
 *
 * @author Nadheesh
 */
public class ManageDepartmentHandler {

    private final ManageDepartment gui;
    private ArrayList<Department> departments;
    private ArrayList<Category> categories;

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

            for (Category c : categories) {

                ((DefaultTableModel) (gui.getCategoryTable().getModel())).addRow(new String[]{c.getCategoryId() + "", c.getCategoryName(), c.getDescription(), "", "", ""});

            }
        }

    }

    public boolean addDepartment() throws SQLException {

        String depId = gui.getDepIdText().getText().trim();
        String depName = gui.getDepNameText().getText().trim();

//        if (!depId.matches("^D[0-9]{2}$") && !depId.isEmpty()) {
//            gui.setIdWarning("Not a valid ID");
//            return false;
//        }
        if (depName.equals("")) {
            Utilities.ShowErrorMsg(gui, "Enter a valid Name");
            return false;
        }
        for (Department d : departments) {
            if (d.getDepartmentName().equals(depName)) {
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

//        if (!depId.matches("^D[0-9]{2}$") && !depId.isEmpty()) {
//            gui.setIdWarning("Not a valid ID");
//            return false;
//        }
        if (depName.equals("")) {
            Utilities.ShowErrorMsg(gui, "Enter a valid Name");
            return false;
        }

        for (Department d : departments) {
            if (d.getDepartmentName().equals(depName)) {
                Utilities.ShowErrorMsg(gui, "The Department already exists");
                return false;
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

        DepartmentController.removeDepartment(gui.getDepIdCombo().getSelectedItem().toString().trim());
        loadDepartmentCombo();
        gui.getDepIdCombo().setSelectedIndex(selectedIndex - 1);

        return false;
    }

    public boolean addCategory(int index) throws SQLException {

        String cId = gui.getIdText().getText().trim();
        String cName = gui.getNameText().getText().trim();
        String cDesc = gui.getDescText().getText().trim();
        String depId = departments.get(index - 1).getDepartmentId();

        if (!cId.matches("^C[0-9]{4}$") && !cId.isEmpty()) {
            gui.setIdComment("Invalid ID");
            return false;
        }
        if (cName.equals("") || cDesc.equals("")) {
            gui.setIdComment("Emtpy Field");
            return false;
        }
        for (Category c : categories) {
            if (c.getCategoryId().equals(cId)) {
                gui.setIdComment("Duplicate ID");
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
            System.out.println(categories.isEmpty() + "  " +categories.size());
            int id = Utilities.convertKeyToInteger(categories.get(categories.size() - 1).getCategoryId());
            return Utilities.convertKeyToString((id + 1), DatabaseInterface.CATEGORY);

        } else {
            return Utilities.convertKeyToString((1), DatabaseInterface.CATEGORY);
        }

    }
}
