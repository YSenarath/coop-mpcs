/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger;

import java.util.ArrayList;
import java.util.Date;
import model.inventory.Batch;

/**
 *
 * @author Yasas
 */
public class GRNCancel {

    private String f16aCancelNo;
    private String f16aNumber;
    private Date date;

    private final ArrayList<Batch> listOfBatches;

    public GRNCancel(String f16aCancelNo, String f16aNumber, Date date) {
        this.f16aCancelNo = f16aCancelNo;
        this.f16aNumber = f16aNumber;
        this.date = date;

        listOfBatches = new ArrayList<>();
    }

    public GRNCancel(String f16aCancelNo, String f16aNumber, Date date, ArrayList<Batch> listOfBatches) {
        this.f16aCancelNo = f16aCancelNo;
        this.f16aNumber = f16aNumber;
        this.date = date;
        this.listOfBatches = listOfBatches;
    }

    public String getF16aNumberCancel() {
        return f16aCancelNo;
    }

    public void setF16aNumberCancel(String f16aNumberCancel) {
        this.f16aCancelNo = f16aNumberCancel;
    }

    public String getF16aNumber() {
        return f16aNumber;
    }

    public void setF16aNumber(String f16aNumber) {
        this.f16aNumber = f16aNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void addBatch(Batch e) {
        listOfBatches.add(e);
    }

    public String getF16aCancelNo() {
        return f16aCancelNo;
    }

    public ArrayList<Batch> getItems() {
        return listOfBatches;
    }
}
