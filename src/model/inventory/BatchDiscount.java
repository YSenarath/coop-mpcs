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
public class BatchDiscount {

    private int batchId;
    private int productId;
    private double discount;
    private String startDate;
    private String endDate;
    private boolean promotional;
    private double quantity;
    private boolean membersOnly;

    public BatchDiscount(int batchId, int productId, double discount, String startDate, String endDate, boolean promotional, double quantity, boolean membersOnly) {
        this.batchId = batchId;
        this.productId = productId;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotional = promotional;
        this.quantity = quantity;
        this.membersOnly = membersOnly;
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
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the promotional
     */
    public boolean isPromotional() {
        return promotional;
    }

    /**
     * @param promotional the promotional to set
     */
    public void setPromotional(boolean promotional) {
        this.promotional = promotional;
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
     * @return the membersOnly
     */
    public boolean isMembersOnly() {
        return membersOnly;
    }

    /**
     * @param membersOnly the membersOnly to set
     */
    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }
    
}
