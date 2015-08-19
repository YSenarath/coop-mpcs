/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.util.Date;

/**
 *
 * @author Nadheesh
 */
public class BatchBuilder implements Builder {

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
    private String suppplierName;
    private String suppplierID;
    private String productName;

    public static BatchBuilder Batch() {
        return new BatchBuilder();
    }

    public BatchBuilder withBatchId(String batchId) {
        this.batchId = batchId;
        return this;
    }

    public BatchBuilder withSupplierName(String name) {
        this.suppplierName = name;
        return this;
    }

    public BatchBuilder withSupplierID(String supID) {
        this.suppplierID = supID;
        return this;
    }

    public BatchBuilder withProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public BatchBuilder withGRNNumber(String grnNumber) {
        this.grnNumber = grnNumber;
        return this;
    }

    public BatchBuilder withUnitCost(double unitCost) {
        this.unitCost = unitCost;
        return this;
    }

    public BatchBuilder withUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public BatchBuilder withSoldQty(double soldQty) {
        this.soldQty = soldQty;
        return this;
    }

    public BatchBuilder withQuantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    public BatchBuilder withRecievedQuantity(double recievedQuantity) {
        this.recievedQuantity = recievedQuantity;
        return this;
    }

    public BatchBuilder withInStock(boolean inStock) {
        this.inStock = inStock;
        return this;
    }

    public BatchBuilder withExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public BatchBuilder withNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
        return this;
    }

    public BatchBuilder withDiscounted(boolean discounted) {
        this.discounted = discounted;
        return this;
    }

    public BatchBuilder withCostDiscount(double costDiscount) {
        this.costDiscount = costDiscount;
        return this;
    }

    public BatchBuilder withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    @Override
    public Batch build() {
        return new Batch(this);

    }

    public String getProductName() {
        return productName;
    }

    public String getSuppplierName() {
        return suppplierName;
    }

    public double getSoldQty() {
        return soldQty;
    }

    public String getBatchId() {
        return batchId;
    }

    public String getProductId() {
        return productId;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public double getCostDiscount() {
        return costDiscount;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getRecievedQuantity() {
        return recievedQuantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public boolean isDiscounted() {
        return discounted;
    }

    String getSuppplierID() {
        return suppplierID;
    }

}
