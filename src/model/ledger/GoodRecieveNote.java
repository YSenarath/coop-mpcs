/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger;

import java.util.Date;
import model.supplier.Supplier;

/**
 *
 * @author Yasas
 */
public class GoodRecieveNote {

    private String f14Number;
    private String invoiceNo;
    private Date invoiceDate;
    private Supplier supplier;
    private String location;
    private String paymentType;

    public String getF14Number() {
        return f14Number;
    }

    public void setF14Number(String f14Number) {
        this.f14Number = f14Number;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

}
