/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger;

import java.util.ArrayList;
import java.util.Date;
import model.ledger.item.DamagedItem;

/**
 *
 * @author Yasas
 */
public class DamageStock {
    
    private String f17Number;
    private Date date;
    private String location;
    
    ArrayList<DamagedItem> items;
    
    public DamageStock(String f17Number, Date date, String location) {
        this.f17Number = f17Number;
        this.date = date;
        this.location = location;
        this.items = new ArrayList<>();
    }
    
    public DamageStock(String f17Number, Date date, String location, ArrayList<DamagedItem> items) {
        this.f17Number = f17Number;
        this.date = date;
        this.location = location;
        this.items = items;
    }
    
    public String getF17Number() {
        return f17Number;
    }
    
    public void setF17Number(String f17Number) {
        this.f17Number = f17Number;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public ArrayList<DamagedItem> getItems() {
        return items;
    }
    
    public void setItems(ArrayList<DamagedItem> items) {
        this.items = items;
    }
    
    public void addItem(DamagedItem itm) {
        items.add(itm);
    }
    
}
