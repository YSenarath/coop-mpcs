/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger.item;

/**
 *
 * @author Yasas
 */
public class DamagedItem extends Item {

    private String DamagedItemID;
    private double quantityDamaged;
    private double sellingPrice;

    public DamagedItem(String batchID, double quantity, String DamagedItemID, double quantityDamaged, double sellingPrice) {
        super(batchID, quantity);
        this.DamagedItemID = DamagedItemID;
        this.quantityDamaged = quantityDamaged;
        this.sellingPrice = sellingPrice;
    }

    public String getDamagedItemID() {
        return DamagedItemID;
    }

    public void setDamagedItemID(String DamagedItemNumber) {
        this.DamagedItemID = DamagedItemNumber;
    }

    public double getQuantityDamaged() {
        return quantityDamaged;
    }

    public void setQuantityDamaged(double quantityDamaged) {
        this.quantityDamaged = quantityDamaged;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

}
