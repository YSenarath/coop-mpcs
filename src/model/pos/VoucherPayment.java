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
public abstract class VoucherPayment extends Payment {

    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String CUSTOMER = "CUSTOMER";

    private String voucherId;

    public VoucherPayment(int billId, int paymentId, String voucherId, double amount) {
        super(billId, paymentId, amount);
        this.voucherId = voucherId;
    }

    /**
     * @return the voucherId
     */
    public String getVoucherId() {
        return voucherId;
    }

    /**
     * @param voucherId the voucherId to set
     */
    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

}
