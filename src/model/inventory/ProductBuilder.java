/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

/**
 *
 * @author Nadheesh
 */
public class ProductBuilder implements Builder {

    private String productId;
    private String productName;
    private long productBarCode;
    private String description;
    private String categoryId;
    private String departmentId;
    private String unit;
    private double packSize;
    private double reorderQuantity; //reorder notify quantity
    private double reorderValue; //reorder notify value
    private double maxQuantity;
    private double totalQuantity;
    private double reorderLevel;
    
    public static ProductBuilder Product() {
        return new ProductBuilder();
    }
    
   
    public ProductBuilder withProductId(String pId) {
        this.productId = pId;
        return this;
    }

    public ProductBuilder withProductName(String pName) {
        this.productName = pName;
        return this;
    }

    public ProductBuilder withBarCode(long barcode) {
        this.productBarCode = barcode;
        return this;
    }

    public ProductBuilder withDescrition(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder withCategory(String catId) {
        this.categoryId = catId;
        return this;
    }

    public ProductBuilder withDepartment(String depId) {
        this.departmentId = depId;
        return this;
    }

    public ProductBuilder withUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public ProductBuilder withPackSize(double packSize) {
        this.packSize = packSize;
        return this;
    }

    public ProductBuilder withMaxQuantity(double maxQuantity) {
        this.maxQuantity = maxQuantity;
        return this;
    }

    public ProductBuilder withReorderQuantity(double reorderQty) {
        this.reorderQuantity = reorderQty;
        return this;
    }
    
    public ProductBuilder withReorderLevel(double reorderLevel) {
        this.reorderLevel= reorderLevel;
        return this;
    }

    public ProductBuilder withReorderValue(double reorderValue) {
        this.reorderValue = reorderValue;
        return this;
    }

    public ProductBuilder withTotalQuantity(double totalQuantity) {

        this.totalQuantity = totalQuantity;

        return this;
    }

    @Override
    public Product build() {
        return new Product(this);
    }

    public String getProductId() {
        return productId;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public long getProductBarCode() {
        return productBarCode;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getUnit() {
        return unit;
    }

    public double getPackSize() {
        return packSize;
    }

    public double getReorderQuantity() {
        return reorderQuantity;
    }

    public double getReorderValue() {
        return reorderValue;
    }

    public double getMaxQuantity() {
        return maxQuantity;
    }

    public double getReorderLevel() {
        return reorderLevel;
    }

}
