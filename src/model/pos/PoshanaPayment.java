package model.pos;

public class PoshanaPayment extends Payment {

    private String stampId;
    private String customerName;

    public PoshanaPayment(int invoiceId, int paymentId, String stampId, String customerName, double amount) {
        super(invoiceId, paymentId, amount);
        this.stampId = stampId;
        this.customerName = customerName;
    }

    /**
     * @return the stampId
     */
    public String getStampId() {
        return stampId;
    }

    /**
     * @param stampId the stampId to set
     */
    public void setStampId(String stampId) {
        this.stampId = stampId;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
