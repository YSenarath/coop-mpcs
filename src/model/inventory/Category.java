/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.util.ArrayList;

/**
 *
 * @author Nadheesh
 */
public class Category {

    private String categoryId;
    private String categoryName;
    private String departmentId;
    private String description;
    private boolean discounted;
    private ArrayList<Product> products;

    public Category(String categoryId, String departmentId) {
        this.categoryId = categoryId;
        this.departmentId = departmentId;
    }

    public Category(String categoryId, String categoryName, String departmentId, String description, boolean discounted, ArrayList<Product> products) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.departmentId = departmentId;
        this.description = description;
        this.discounted = discounted;
        this.products = products;
    }

    public Category(String categoryId, String categoryName, String departmentId, String description, boolean discounted) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.departmentId = departmentId;
        this.description = description;
        this.discounted = discounted;
    }

    public Category(String categoryId, String categoryName, String departmentId, String description, ArrayList<Product> products) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.departmentId = departmentId;
        this.description = description;
        this.products = products;
    }

    public Category(String categoryId, String categoryName, String departmentId, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.departmentId = departmentId;
        this.description = description;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
