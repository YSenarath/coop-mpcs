/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.pos;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

/**
 *
 * @author Shehan
 */
public class Invoice {

    private final String invoiceNo;
    //private Cashier cashier;//The cashier
    private Date date;
    private Time time;
    private int itemCount;
    private double subTotal;
    private double netTotal;
    private double discount;
    private ArrayList<InvoiceItem> invoiceItems;//invoice has items
    private ArrayList<Payment> payments;//Invoice has payments

    public Invoice(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        this.date = null;
        this.time = null;
        this.itemCount = 0;
        this.subTotal = 0;
        this.netTotal = 0;
        this.discount = 0;
        this.invoiceItems = null;
        this.payments = null;
    }

    public Invoice(String invoiceNo, Date date, Time time, int itemCount, double subTotal, double netTotal, double discount, ArrayList<InvoiceItem> invoiceItems, ArrayList<Payment> payments) {
        this.invoiceNo = invoiceNo;
        this.date = date;
        this.time = time;
        this.itemCount = itemCount;
        this.subTotal = subTotal;
        this.netTotal = netTotal;
        this.discount = discount;
        this.invoiceItems = invoiceItems;
        this.payments = payments;
    }

    /**
     * @return the invoiceNo
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public Time getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * @return the itemCount
     */
    public int getItemCount() {
        return itemCount;
    }

    /**
     * @param itemCount the itemCount to set
     */
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * @return the subTotal
     */
    public double getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    /**
     * @return the netTotal
     */
    public double getNetTotal() {
        return netTotal;
    }

    /**
     * @param netTotal the netTotal to set
     */
    public void setNetTotal(double netTotal) {
        this.netTotal = netTotal;
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
     * @return the invoiceItems
     */
    public ArrayList<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    /**
     * @param invoiceItems the invoiceItems to set
     */
    public void setInvoiceItems(ArrayList<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    /**
     * @return the payments
     */
    public ArrayList<Payment> getPayments() {
        return payments;
    }

    /**
     * @param payments the payments to set
     */
    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }

}
