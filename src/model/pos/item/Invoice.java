package model.pos.item;

import model.pos.payment.Payment;
import java.util.ArrayList;

public class Invoice {

    private int invoiceNo;
    private int shiftId;
    private int counterId;
    private String date;
    private String time;

    private int itemCount;
    private double netTotal;
    private double discount;

    private double amountPaid;

    private ArrayList<InvoiceItem> invoiceItems;//invoice has items
    private ArrayList<Payment> payments;//Invoice has payments

    public Invoice(int invoiceNo) {
        this.invoiceNo = invoiceNo;
        this.shiftId = 0;
        this.counterId = -1;
        this.date = null;
        this.time = null;

        this.itemCount = 0;
        this.netTotal = 0;
        this.discount = 0;

        this.amountPaid = 0;

        this.invoiceItems = null;
        this.payments = null;
    }

    public Invoice(int invoiceNo, int shiftId, int counterId, String date, String time, double netTotal) {
        this.invoiceNo = invoiceNo;
        this.shiftId = shiftId;
        this.counterId = counterId;
        this.date = date;
        this.time = time;
        this.itemCount = 0;
        this.netTotal = netTotal;
        this.discount = 0;
        this.amountPaid = 0;
        this.invoiceItems = null;
        this.payments = null;
    }

    /**
     * @return the invoiceNo
     */
    public int getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @param invoiceNo the invoiceNo to set
     */
    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * @return the shiftId
     */
    public int getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return the counterId
     */
    public int getCounterId() {
        return counterId;
    }

    /**
     * @param counterId the counterId to set
     */
    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
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
     * @return the amountPaid
     */
    public double getAmountPaid() {
        return amountPaid;
    }

    /**
     * @param amountPaid the amountPaid to set
     */
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
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
