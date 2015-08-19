/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger.item;

import java.io.Serializable;

public class GRNItem implements Serializable {
    private String GrnItemID;
    private String batchId;

    private double unitPrice;
    private double quantity;
    private double recievedQuantity;

    public GRNItem(String GrnItemID, String batchId, double unitPrice, double quantity, double recievedQuantity) {
        this.GrnItemID = GrnItemID;
        this.batchId = batchId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.recievedQuantity = recievedQuantity;
    }

    public String getGrnItemID() {
        return GrnItemID;
    }

    public void setGrnItemID(String GrnItemID) {
        this.GrnItemID = GrnItemID;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRecievedQuantity() {
        return recievedQuantity;
    }

    public void setRecievedQuantity(double recievedQuantity) {
        this.recievedQuantity = recievedQuantity;
    }
    
    @Override
    public String toString() {
        return getBatchId();//To change body of generated methods, choose Tools | Templates.
    }
}
