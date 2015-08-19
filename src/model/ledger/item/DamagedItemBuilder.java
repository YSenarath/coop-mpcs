/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger.item;


public class DamagedItemBuilder {
    private String batchID;
    private double quantity;
    private String DamagedItemID;
    private double quantityDamaged;
    private double sellingPrice;

    public DamagedItemBuilder() {
    }

    public DamagedItemBuilder setBatchID(String batchID) {
        this.batchID = batchID;
        return this;
    }

    public DamagedItemBuilder setQuantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    public DamagedItemBuilder setDamagedItemID(String DamagedItemID) {
        this.DamagedItemID = DamagedItemID;
        return this;
    }

    public DamagedItemBuilder setQuantityDamaged(double quantityDamaged) {
        this.quantityDamaged = quantityDamaged;
        return this;
    }

    public DamagedItemBuilder setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public DamagedItem createDamagedItem() {
        return new DamagedItem(batchID, quantity, DamagedItemID, quantityDamaged, sellingPrice);
    }
    
}
