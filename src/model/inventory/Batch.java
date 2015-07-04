/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.util.Date;
import model.unit.LKR;

/**
 *
 * @author Nadheesh
 */
public class Batch {

    private int batchId;
    private String grnNumber;
    private Product product;
    private float quantity;
    private LKR costPerItem;
    private LKR pricePerItem;
    private Date expirationDate;
}
