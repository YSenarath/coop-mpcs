package model.pos.payment;

public class CoopCreditPayment extends Payment {

    private int customerId;
    private double amountSettled;

    public CoopCreditPayment(int invoiceId, int paymentId, int customerId, double amount, double amountSettled) {
        super(invoiceId, paymentId, amount);
        this.customerId = customerId;
        this.amountSettled = amountSettled;
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

    /**
     * @return the amountSettled
     */
    public double getAmountSettled() {
        return amountSettled;
    }

    /**
     * @param amountSettled the amountSettled to set
     */
    public void setAmountSettled(double amountSettled) {
        this.amountSettled = amountSettled;
    }
}
