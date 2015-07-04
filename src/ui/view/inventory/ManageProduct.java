/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.inventory;

/**
 *
 * @author Nadheesh
 */
public class ManageProduct extends Manage {

    public ManageProduct() {
        super.colCount = 6;
        super.colSizes = new int[]{100, 150, 250, 100, 100, 150};
        super.colTitle = new String[]{"Batch No", "GRN number", "Supplier", "Cost Per Item", "Price Per Item", "Expiration Date"};
        super.colTypes = new Class[]{java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class};
        super.idString = "Product";
        super.nameString = "Product";
        super.tableName = "Batches";

        init();
    }

    @Override
    public void addButtonPressed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editButtonPressed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeButtonPressed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearButtemPressed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
