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
public class CustomerVoucherPayment extends VoucherPayment{

    public CustomerVoucherPayment(int invoiceId, int paymentId, String voucherId, double amount) {
        super(invoiceId, paymentId, voucherId, amount);
    }
    
}
