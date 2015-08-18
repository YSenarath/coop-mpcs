/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.util.Date;

/**
 *
 * @author Nadheesh
 */
public class CategoryDiscount {

    private String categoryId;
    private String departmentId;
    private double discount;
    private Date startDate;
    private Date endDate;
    private boolean promotional;
    private double quantity;
    private boolean membersOnly;

    public CategoryDiscount(Category category, double discount, Date startDate, Date endDate) {
        this.categoryId = category.getCategoryId();
        this.departmentId = category.getDepartmentId();
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotional = true;
        this.quantity = 0;
        this.membersOnly = false;
    }

    public CategoryDiscount(String categoryId, String departmentId, double discount, Date startDate, Date endDate, boolean promotional, double quantity, boolean memebersOnly) {
        this.categoryId = categoryId;
        this.departmentId = departmentId;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotional = promotional;
        this.quantity = quantity;
        this.membersOnly = memebersOnly;
    }

    public void setQuantityDiscount(double quantity) {
        this.promotional = false;
        this.quantity = quantity;
    }

    public void setMembersOnly() {
        this.membersOnly = true;
    }

    public CategoryDiscount(String categoryId, String departmentId) {
        this.categoryId = categoryId;
        this.departmentId = departmentId;
    }

    public CategoryDiscount() {
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
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
