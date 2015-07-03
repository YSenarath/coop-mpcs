/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import model.unit.Unit;

/**
 *
 * @author Nadheesh
 */
public class Product {
    private int productCode;
    private BarCode productBarCode;
    private String description;
    private Unit unit;
    private int reorderNotifyQuantity;
}
