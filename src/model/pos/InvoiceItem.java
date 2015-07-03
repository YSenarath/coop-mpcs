/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.pos;

/**
 *
 * @author Shehan
 */
public class InvoiceItem {

    private String itemCode;
    private int purchaseQty;
    private double unitPrice;

    public InvoiceItem(String itemCode, int purchaseQty, double unitPrice) {
        this.itemCode = itemCode;
        this.purchaseQty = purchaseQty;
        this.unitPrice = unitPrice;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the purchaseQty
     */
    public int getPurchaseQty() {
        return purchaseQty;
    }

    /**
     * @param purchaseQty the purchaseQty to set
     */
    public void setPurchaseQty(int purchaseQty) {
        this.purchaseQty = purchaseQty;
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

}
