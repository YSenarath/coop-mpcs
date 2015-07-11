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
public class CashPayment extends Payment {

    private double changeAmount;

    public CashPayment(int billId, int paymentId, double amount, double changeAmount) {
        super(billId, paymentId, amount);
        this.changeAmount = changeAmount;
    }

    /**
     * @return the changeAmount
     */
    public double getChangeAmount() {
        return changeAmount;
    }

    /**
     * @param changeAmount the changeAmount to set
     */
    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

}
