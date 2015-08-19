/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Nadheesh
 */
public class Product implements Serializable {

    private String productId;
    private String productName;
    private long productBarCode;
    private String description;
    private String categoryId;
    private String departmentId;
    private String unit;
    private double packSize;
    private double reorderQuantity; 
    private double reorderValue; 
    private double maxQuantity;
    private ArrayList<Batch> batches;
    private  double totalQuantity;
    private double reOrderLevel ; //reordering notification
    
    //POS
    private Batch selectedBatch;
    
    
    public Product(ProductBuilder builder) {
        this.productId = builder.getProductId();
        this.productName = builder.getProductName();
        this.productBarCode = builder.getProductBarCode();
        this.description = builder.getDescription();
        this.categoryId = builder.getCategoryId();
        this.departmentId = builder.getDepartmentId();
        this.unit = builder.getUnit();
        this.packSize = builder.getPackSize();
        this.reorderQuantity = builder.getReorderQuantity();
        this.reorderValue = builder.getReorderValue();
        this.maxQuantity = builder.getMaxQuantity();
        this.selectedBatch = null;
        this.totalQuantity = builder.getTotalQuantity();
        this.reOrderLevel = builder.getReorderLevel();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductCode(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductBarCode() {
        return productBarCode;
    }

    public void setProductBarCode(long productBarCode) {
        this.productBarCode = productBarCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPackSize() {
        return packSize;
    }

    public void setPackSize(double packSize) {
        this.packSize = packSize;
    }

    public double getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(double reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public double getReorderValue() {
        return reorderValue;
    }

    public void setReorderValue(double reorderValue) {
        this.reorderValue = reorderValue;
    }

    public double getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(double maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public ArrayList<Batch> getBatches() {
        return batches;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setBatches(ArrayList<Batch> batches) {
        this.batches = batches;
    }

    /**
     * @return the selectedBatch
     */
    public Batch getSelectedBatch() {
        return selectedBatch;
    }

    /**
     * @param selectedBatch the selectedBatch to set
     */
    public void setSelectedBatch(Batch selectedBatch) {
        this.selectedBatch = selectedBatch;
    }

    public double getReorderLevel() {
        return reOrderLevel;
    }

}
