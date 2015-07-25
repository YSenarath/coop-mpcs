/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.io.Serializable;
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

    private boolean discounted;
    private double soldQty;

    public Batch(BatchBuilder builder) {

        this.batchId = builder.getBatchId();
        this.productId = builder.getProductId();
        this.grnNumber = builder.getGrnNumber();
        this.unitCost = builder.getUnitCost();
        this.costDiscount = builder.getCostDiscount();
        this.unitPrice = builder.getUnitPrice();
        this.quantity = builder.getQuantity();
        this.recievedQuantity = builder.getRecievedQuantity();
        this.inStock = builder.isInStock();
        this.expirationDate = builder.getExpirationDate();
        this.notificationDate = builder.getNotificationDate();
        this.discounted = builder.isDiscounted();
        this.soldQty = builder.getSoldQty();
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

}
