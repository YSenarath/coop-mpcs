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
public class ManageDepartment extends Manage{
    
    
    public ManageDepartment(){
        
        super.title = "Manage Departments";
        super.colCount=6;
        super.colSizes=new int[] {100,150,250,150,200,200};
        super.colTitle =new String[] { "Category ID", "Category Name", "Descripton", "Discount", "Discount Start Date", "Discount Finish Date"};
        super.colTypes=new Class [] {java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class};
        super.idString="Department";
        super.nameString= "Department";
        super.tableName="Categories";
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
