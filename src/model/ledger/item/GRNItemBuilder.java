/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger.item;


public class GRNItemBuilder {
    private String GrnItemID;
    private String batchId;
    private double unitPrice;
    private double quantity;
    private double recievedQuantity;

    public GRNItemBuilder() {
    }

    public GRNItemBuilder setGrnItemID(String GrnItemID) {
        this.GrnItemID = GrnItemID;
        return this;
    }

    public GRNItemBuilder setBatchId(String batchId) {
        this.batchId = batchId;
        return this;
    }

    public GRNItemBuilder setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public GRNItemBuilder setQuantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    public GRNItemBuilder setRecievedQuantity(double recievedQuantity) {
        this.recievedQuantity = recievedQuantity;
        return this;
    }

    public GRNItem createGRNItem() {
        return new GRNItem(GrnItemID, batchId, unitPrice, quantity, recievedQuantity);
    }
    
}
