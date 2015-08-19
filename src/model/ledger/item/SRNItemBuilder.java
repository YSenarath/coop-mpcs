/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger.item;


public class SRNItemBuilder {
    private String srnNumber;
    private String batch;
    private double quantity;
    private double costPrice;
    private double salesPrice;

    public SRNItemBuilder() {
    }

    public SRNItemBuilder setSrnNumber(String srnNumber) {
        this.srnNumber = srnNumber;
        return this;
    }

    public SRNItemBuilder setBatchID(String productID) {
        this.batch = productID;
        return this;
    }

    public SRNItemBuilder setQuantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    public SRNItemBuilder setCostPrice(double costPrice) {
        this.costPrice = costPrice;
        return this;
    }

    public SRNItemBuilder setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
        return this;
    }

    public SRNItem createSRNItem() {
        return new SRNItem(srnNumber, batch, quantity, costPrice, salesPrice);
    }
    
}
