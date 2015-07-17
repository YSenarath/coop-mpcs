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
public class CategoryDiscount {

    private int categoryId;
    private int departmentId;
    private double discount;
    private String startDate;
    private String endDate;
    private boolean promotional;
    private double quantity;
    private boolean membersOnly;

    public CategoryDiscount(int categoryId, int departmentId, double discount, String startDate, String endDate, boolean promotional, double quantity, boolean membersOnly) {
        this.categoryId = categoryId;
        this.departmentId = departmentId;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotional = promotional;
        this.quantity = quantity;
        this.membersOnly = membersOnly;
    }

    /**
     * @return the categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the departmentId
     */
    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
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
