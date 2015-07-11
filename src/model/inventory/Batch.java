/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.io.Serializable;

/**
 *
 * @author Shehan
 */
public class Batch implements Serializable {

    private int batchId;
    private int productId;
    private int grnNo;

    private double unitCost;
    private double costDiscount;
    private double unitPrice;
    private double quantity;
    private String expDate;
    private String notifiDate;
    private double recievedQty;
    private boolean inStock;

    private boolean discounted;
    private BatchDiscount batchDiscount;

    public Batch(int batchId, int productId, int grnNo, double unitCost, double costDiscount, double unitPrice, double quantity, String expDate, String notifiDate, double recievedQty, boolean inStock, boolean discounted) {
        this.batchId = batchId;
        this.productId = productId;
        this.grnNo = grnNo;
        this.unitCost = unitCost;
        this.costDiscount = costDiscount;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.expDate = expDate;
        this.notifiDate = notifiDate;
        this.recievedQty = recievedQty;
        this.inStock = inStock;
        this.discounted = discounted;
        this.batchDiscount = null;
    }

    /**
     * @return the batchId
     */
    public int getBatchId() {
        return batchId;
    }

    /**
     * @param batchId the batchId to set
     */
    public void setBatchId(int batchId) {
        this.batchId = batchId;
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
     * @return the grnNo
     */
    public int getGrnNo() {
        return grnNo;
    }

    /**
     * @param grnNo the grnNo to set
     */
    public void setGrnNo(int grnNo) {
        this.grnNo = grnNo;
    }

    /**
     * @return the unitCost
     */
    public double getUnitCost() {
        return unitCost;
    }

    /**
     * @param unitCost the unitCost to set
     */
    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    /**
     * @return the costDiscount
     */
    public double getCostDiscount() {
        return costDiscount;
    }

    /**
     * @param costDiscount the costDiscount to set
     */
    public void setCostDiscount(double costDiscount) {
        this.costDiscount = costDiscount;
    }

    /**
     * @return the unitPrice
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the expDate
     */
    public String getExpDate() {
        return expDate;
    }

    /**
     * @param expDate the expDate to set
     */
    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    /**
     * @return the notifiDate
     */
    public String getNotifiDate() {
        return notifiDate;
    }

    /**
     * @param notifiDate the notifiDate to set
     */
    public void setNotifiDate(String notifiDate) {
        this.notifiDate = notifiDate;
    }

    /**
     * @return the recievedQty
     */
    public double getRecievedQty() {
        return recievedQty;
    }

    /**
     * @param recievedQty the recievedQty to set
     */
    public void setRecievedQty(double recievedQty) {
        this.recievedQty = recievedQty;
    }

    /**
     * @return the inStock
     */
    public boolean isInStock() {
        return inStock;
    }

    /**
     * @param inStock the inStock to set
     */
    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    /**
     * @return the discounted
     */
    public boolean isDiscounted() {
        return discounted;
    }

    /**
     * @param discounted the discounted to set
     */
    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    /**
     * @return the batchDiscount
     */
    public BatchDiscount getBatchDiscount() {
        return batchDiscount;
    }

    /**
     * @param batchDiscount the batchDiscount to set
     */
    public void setBatchDiscount(BatchDiscount batchDiscount) {
        this.batchDiscount = batchDiscount;
    }

}
