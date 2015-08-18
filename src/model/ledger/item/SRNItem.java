/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger.item;

/**
 *
 * @author SENARATH
 */
public class SRNItem extends Item {

    private String srnNumber;
    private double costPrice;
    private double salesPrice;

    public SRNItem(String srnNumber, String productID, double quantity, double costPrice, double salesPrice) {
        super(productID, quantity);
        this.srnNumber = srnNumber;
        this.costPrice = costPrice;
        this.salesPrice = salesPrice;
    }

    public String getSrnNumber() {
        return srnNumber;
    }

    public void setSrnNumber(String srnNumber) {
        this.srnNumber = srnNumber;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

}
