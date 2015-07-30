package model.pos.payment;

public class CashPayment extends Payment {

    private double changeAmount;

    public CashPayment(int invoiceId, int paymentId, double amount, double changeAmount) {
        super(invoiceId, paymentId, amount);
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
