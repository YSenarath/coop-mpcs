package model.pos;

public class EmployeeVoucherPayment extends VoucherPayment {

    private int employeeId;

    public EmployeeVoucherPayment(int invoiceId, int paymentId, String voucherId, int employeeId, double amount) {
        super(invoiceId, paymentId, voucherId, amount);
        this.employeeId = employeeId;
    }

    /**
     * @return the employeeId
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

}
