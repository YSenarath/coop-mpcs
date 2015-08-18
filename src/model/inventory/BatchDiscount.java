/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Nadheesh
 */
public class BatchDiscount implements Serializable {

    private String batchId;
    private String productId;
    private double discount;
    private Date startDate;
    private Date endDate;
    private boolean promotional;
    private double quantity;
    private boolean membersOnly;

    public BatchDiscount(String batchId, String productId, double discount, Date startDate, Date endDate, boolean promotional, double quantity, boolean memebersOnly) {
        this.batchId = batchId;
        this.productId = productId;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotional = promotional;
        this.quantity = quantity;
        this.membersOnly = memebersOnly;
    }

    public BatchDiscount(String batchId, String productId) {
        this.batchId = batchId;
        this.productId = productId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isPromotional() {
        return promotional;
    }

    public void setPromotional(boolean promotional) {
        this.promotional = promotional;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }

}
