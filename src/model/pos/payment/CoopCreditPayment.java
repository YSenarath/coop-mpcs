package model.pos.payment;

public class CoopCreditPayment extends Payment {

    private int customerId;

    public CoopCreditPayment(int invoiceId, int paymentId, int customerId, double amount) {
        super(invoiceId, paymentId, amount);
        this.customerId = customerId;
    }

    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

}
