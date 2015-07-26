package model.pos.item;

import java.util.ArrayList;

public class Refund {

    private final int refundId;
    private final int invoiceId;
    private final int ShiftId;
    private String refundTime;
    private String refundDate;
    private double amount;

    private ArrayList<RefundItem> refundItems;

    public Refund(int refundId, int invoiceId, int ShiftId) {
        this.refundId = refundId;
        this.invoiceId = invoiceId;
        this.ShiftId = ShiftId;
        this.refundTime = null;
        this.refundDate = null;
        this.amount = -1;
        this.refundItems = null;
    }

    public Refund(int refundId, int invoiceId, int ShiftId, String refundTime, String refundDate, double amount) {
        this.refundId = refundId;
        this.invoiceId = invoiceId;
        this.ShiftId = ShiftId;
        this.refundTime = refundTime;
        this.refundDate = refundDate;
        this.amount = amount;
        this.refundItems = null;
    }

    /**
     * @return the refundId
     */
    public int getRefundId() {
        return refundId;
    }

    /**
     * @return the invoiceId
     */
    public int getInvoiceId() {
        return invoiceId;
    }

    /**
     * @return the ShiftId
     */
    public int getShiftId() {
        return ShiftId;
    }

    /**
     * @return the refundTime
     */
    public String getRefundTime() {
        return refundTime;
    }

    /**
     * @param refundTime the refundTime to set
     */
    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    /**
     * @return the refundDate
     */
    public String getRefundDate() {
        return refundDate;
    }

    /**
     * @param refundDate the refundDate to set
     */
    public void setRefundDate(String refundDate) {
        this.refundDate = refundDate;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the refundItems
     */
    public ArrayList<RefundItem> getRefundItems() {
        return refundItems;
    }

    /**
     * @param refundItems the refundItems to set
     */
    public void setRefundItems(ArrayList<RefundItem> refundItems) {
        this.refundItems = refundItems;
    }

}
