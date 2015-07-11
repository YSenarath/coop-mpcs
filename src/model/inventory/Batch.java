/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

/**
 *
 * @author Shehan
 */
public class Batch {

    private int batchId;
    private int productId;
    private int grnNo;
    private double unitCost;
    private double costDiscount;
    private double unitPrice;
    private double quantity;
    private String qty;
    private String notifiDate;
    private double recievedQty;
    private boolean inStock;
    private boolean discounted;

    public Batch(int batchId, int productId, int grnNo, double unitCost, double costDiscount, double unitPrice, double quantity, String qty, String notifiDate, double recievedQty, boolean inStock, boolean discounted) {
        this.batchId = batchId;
        this.productId = productId;
        this.grnNo = grnNo;
        this.unitCost = unitCost;
        this.costDiscount = costDiscount;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.qty = qty;
        this.notifiDate = notifiDate;
        this.recievedQty = recievedQty;
        this.inStock = inStock;
        this.discounted = discounted;
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
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
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

}
