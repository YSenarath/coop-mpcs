/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.pos;

/**
 *
 * @author Shehan
 */
public abstract class Payment {

    public static final String CASH = "Cash";
    public static final String CREDIT_CARD = "Credit Card";
    public static final String COOP_CRDIT = "Coop Credit";
    public static final String POSHANA = "Poshana";
    public static final String VOUCHER = "Voucher";

    private final int billId;
    private final int paymentId;
    private double amount;

    protected Payment(int billId, int paymentId, double amount) {
        this.billId = billId;
        this.paymentId = paymentId;
        this.amount = amount;
    }

    /**
     * @return the billId
     */
    public int getBillId() {
        return billId;
    }

    /**
     * @return the paymentId
     */
    public int getPaymentId() {
        return paymentId;
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

}
