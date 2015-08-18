/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger;

import java.util.ArrayList;
import java.util.Date;
import model.inventory.Batch;
import model.supplier.Supplier;

/**
 *
 * @author Yasas
 */
public class GoodRecieveNote {

    private String f16bNumber;
    private String invoiceNo;
    private Date invoiceDate;
    private Supplier supplier;
    private String location;
    private String paymentMethod;
    private double loadingFee;
    private double purchasingBillDiscount;
    private double sellingBillDiscount;

    private ArrayList<Batch> batches;

    public GoodRecieveNote(String f16bNumber, String invoiceNo, Date invoiceDate, Supplier supplier, String location, String paymentMethod, double loadingFee, double purchasingBillDiscount, double sellingBillDiscount) {
        this.f16bNumber = f16bNumber;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.supplier = supplier;
        this.location = location;
        this.paymentMethod = paymentMethod;
        this.loadingFee = loadingFee;
        this.purchasingBillDiscount = purchasingBillDiscount;
        this.sellingBillDiscount = sellingBillDiscount;
        this.batches = new ArrayList<>();
    }

    public GoodRecieveNote(String f16bNumber, String invoiceNo, Date invoiceDate, Supplier supplier, String location, String paymentMethod, double loadingFee, double purchasingBillDiscount, double sellingBillDiscount, ArrayList<Batch> batces) {
        this.f16bNumber = f16bNumber;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.supplier = supplier;
        this.location = location;
        this.paymentMethod = paymentMethod;
        this.loadingFee = loadingFee;
        this.purchasingBillDiscount = purchasingBillDiscount;
        this.sellingBillDiscount = sellingBillDiscount;
        this.batches = batces;
    }

    public String getF16bNumber() {
        return f16bNumber;
    }

    public void setF16bNumber(String f16bNumber) {
        this.f16bNumber = f16bNumber;
    }

    public void addBatch(Batch batch) {
        this.batches.add(batch);
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ArrayList<Batch> getBatches() {
        return batches;
    }

    public String getSupplierId() {
        return this.supplier.getSupplerID();
    }

    public double getLoadingFee() {
        return this.loadingFee;
    }

    public double getPurchasingBillDiscount() {
        return this.purchasingBillDiscount;
    }

    public double getSellingBillDiscount() {
        return this.sellingBillDiscount;
    }

    public void setLoadingFee(double loadingFee) {
        this.loadingFee = loadingFee;
    }

    public void setPurchasingBillDiscount(double purchasingBillDiscount) {
        this.purchasingBillDiscount = purchasingBillDiscount;
    }

    public void setSellingBillDiscount(double sellingBillDiscount) {
        this.sellingBillDiscount = sellingBillDiscount;
    }

    public void setBatches(ArrayList<Batch> batches) {
        this.batches = batches;
    }

}
