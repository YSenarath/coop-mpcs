package model.pos.payment;

public abstract class VoucherPayment extends Payment {

    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String CUSTOMER = "CUSTOMER";

    private String voucherId;

    public VoucherPayment(int invoiceId, int paymentId, String voucherId, double amount) {
        super(invoiceId, paymentId, amount);
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
