package model.pos.payment;

public class CustomerVoucherPayment extends VoucherPayment {

    public CustomerVoucherPayment(int invoiceId, int paymentId, String voucherId, double amount) {
        super(invoiceId, paymentId, voucherId, amount);
    }

}
