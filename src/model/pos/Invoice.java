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

    private final int invoiceNo;
    private String username;//The cashier
    private int counterid;
    private Date date;
    private Time time;
    private int itemCount;
    private double netTotal;
    private double discount;
    private ArrayList<InvoiceItem> invoiceItems;//invoice has items
    private ArrayList<Payment> payments;//Invoice has payments

    public Invoice(int invoiceNo) {
        this.invoiceNo = invoiceNo;
        this.username = null;
        this.counterid=-1;
        this.date = null;
        this.time = null;
        this.itemCount = 0;
        this.netTotal = 0;
        this.discount = 0;
        this.invoiceItems = null;
        this.payments = null;
    }

    public Invoice(int invoiceNo, String username, int counterid, Date date, Time time, int itemCount, double netTotal, double discount, ArrayList<InvoiceItem> invoiceItems, ArrayList<Payment> payments) {
        this.invoiceNo = invoiceNo;
        this.username = username;
        this.counterid = counterid;
        this.date = date;
        this.time = time;
        this.itemCount = itemCount;
        this.netTotal = netTotal;
        this.discount = discount;
        this.invoiceItems = invoiceItems;
        this.payments = payments;
    }

    /**
     * @return the invoiceNo
     */
    public int getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the counterid
     */
    public int getCounterid() {
        return counterid;
    }

    /**
     * @param counterid the counterid to set
     */
    public void setCounterid(int counterid) {
        this.counterid = counterid;
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
