package model.pos.payment;

public abstract class Payment {

    public static final String CASH = "Cash";
    public static final String CREDIT_CARD = "Credit Card";
    public static final String COOP_CRDIT = "Coop Credit";
    public static final String POSHANA = "Poshana";
    public static final String VOUCHER = "Voucher";

    private final int invoiceId;
    private final int paymentId;
    private double amount;

    protected Payment(int invoiceId, int paymentId, double amount) {
        this.invoiceId = invoiceId;
        this.paymentId = paymentId;
        this.amount = amount;
    }

    /**
     * @return the invoiceId
     */
    public int getInvoiceId() {
        return invoiceId;
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
