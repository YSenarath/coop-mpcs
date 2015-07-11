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
 * @author Shehan
 */
public class Product implements Serializable{

    private int productId;
    private String name;
    private long barcode;
    private String description;
    private int categoryId;
    private int departmentId;

    private String unit;
    private double packSize;
    private double reorderValue;
    private double reorderQty;
    private double maxQty;

    private ArrayList<Batch> batches;
    private Batch selectedBatch;

    public Product(int productId, String name, long barcode, String description, int categoryId, int departmentId, String unit, double packSize, double reorderValue, double reorderQty, double maxQty) {
        this.productId = productId;
        this.name = name;
        this.barcode = barcode;
        this.description = description;
        this.categoryId = categoryId;
        this.departmentId = departmentId;
        this.unit = unit;
        this.packSize = packSize;
        this.reorderValue = reorderValue;
        this.reorderQty = reorderQty;
        this.maxQty = maxQty;
        this.batches = new ArrayList();
        this.selectedBatch = null;
    }

    /**
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the barcode
     */
    public long getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the departmentId
     */
    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the packSize
     */
    public double getPackSize() {
        return packSize;
    }

    /**
     * @param packSize the packSize to set
     */
    public void setPackSize(double packSize) {
        this.packSize = packSize;
    }

    /**
     * @return the reorderValue
     */
    public double getReorderValue() {
        return reorderValue;
    }

    /**
     * @param reorderValue the reorderValue to set
     */
    public void setReorderValue(double reorderValue) {
        this.reorderValue = reorderValue;
    }

    /**
     * @return the reorderQty
     */
    public double getReorderQty() {
        return reorderQty;
    }

    /**
     * @param reorderQty the reorderQty to set
     */
    public void setReorderQty(double reorderQty) {
        this.reorderQty = reorderQty;
    }

    /**
     * @return the maxQty
     */
    public double getMaxQty() {
        return maxQty;
    }

    /**
     * @param maxQty the maxQty to set
     */
    public void setMaxQty(double maxQty) {
        this.maxQty = maxQty;
    }

    /**
     * @return the batches
     */
    public ArrayList<Batch> getBatches() {
        return batches;
    }

    /**
     * @param batches the batches to set
     */
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

}
