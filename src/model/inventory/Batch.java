/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import controller.inventory.ProductController;
import groovy.transform.ToString;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author Nadheesh
 */
public class Batch implements Serializable {

    private String batchId;
    private String productId;
    private String grnNumber;

    private double unitCost;
    private double costDiscount;
    private double unitPrice;
    private double quantity;
    private double recievedQuantity;
    private boolean inStock;
    private Date expirationDate;
    private Date notificationDate;
    private String supplierName;
    private boolean discounted;
    private double soldQty;
    private String supplierId;
    private String productName;

    public Batch(BatchBuilder builder) {

        this.batchId = builder.getBatchId();
        this.productId = builder.getProductId();
        this.productName = builder.getProductName();
        this.grnNumber = builder.getGrnNumber();
        this.unitCost = builder.getUnitCost();
        this.costDiscount = builder.getCostDiscount();
        this.unitPrice = builder.getUnitPrice();
        this.quantity = builder.getQuantity();
        this.recievedQuantity = builder.getRecievedQuantity();
        this.inStock = builder.isInStock();
        this.expirationDate = builder.getExpirationDate();
        this.notificationDate = builder.getNotificationDate();
        this.supplierName = builder.getSuppplierName();
        this.discounted = builder.isDiscounted();
        this.soldQty = builder.getSoldQty();
        this.supplierId = builder.getSuppplierID();
    }

    public double getRecievedQuantity() {
        return recievedQuantity;
    }

    public void setRecievedQuantity(double recievedQuantity) {
        this.recievedQuantity = recievedQuantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }

    public double getUnit_cost() {
        return unitCost;
    }

    public void setUnit_cost(double unit_cost) {
        this.unitCost = unit_cost;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getCostDiscount() {
        return costDiscount;
    }

    public void setCostDiscount(double costDiscount) {
        this.costDiscount = costDiscount;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public double getCost_discount() {
        return costDiscount;
    }

    public void setCost_discount(double cost_discount) {
        this.costDiscount = cost_discount;
    }

    public double getUnit_price() {
        return unitPrice;
    }

    public void setUnit_price(double unit_price) {
        this.unitPrice = unit_price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    /**
     * @return the soldQty
     */
    public double getSoldQty() {
        return soldQty;
    }

    /**
     * @param soldQty the soldQty to set
     */
    public void setSoldQty(double soldQty) {
        this.soldQty = soldQty;
    }

    @Override
    public String toString() {
        return getProductId();//To change body of generated methods, choose Tools | Templates.
    }

    public String getSupplierID() {
        return supplierId;
    }

    public String getProductName() {
        return productName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        try {
            return ProductController.getProduct(productId).getProductName();
        } catch (SQLException ex) {
            return "-";
        }
    }
}
