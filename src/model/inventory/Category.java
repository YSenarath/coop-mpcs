/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.util.Date;
import model.unit.Percentage;

/**
 *
 * @author Nadheesh
 */
public class Category {
    private int categoryId;
    private int categoryName;
    private String description;
    private Percentage discount;
    private Date discountStartDate;
    private Date discountStopDate;
    private boolean isContainerItem;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(int categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Percentage getDiscount() {
        return discount;
    }

    public void setDiscount(Percentage discount) {
        this.discount = discount;
    }

    public Date getDiscountStartDate() {
        return discountStartDate;
    }

    public void setDiscountStartDate(Date discountStartDate) {
        this.discountStartDate = discountStartDate;
    }

    public Date getDiscountStopDate() {
        return discountStopDate;
    }

    public void setDiscountStopDate(Date discountStopDate) {
        this.discountStopDate = discountStopDate;
    }

    public boolean isIsContainerItem() {
        return isContainerItem;
    }

    public void setIsContainerItem(boolean isContainerItem) {
        this.isContainerItem = isContainerItem;
    }
}
