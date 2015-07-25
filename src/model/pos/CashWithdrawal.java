package model.pos;

public class CashWithdrawal {

    private int withdrawalId;
    private int shiftId;
    private String time;
    private String date;
    private double amount;

    public CashWithdrawal(int withdrawalId) {
        this.withdrawalId = withdrawalId;
        this.shiftId = -1;
        this.time = null;
        this.date = null;
        this.amount = -1;
    }

    public CashWithdrawal(int withdrawalId, int shiftId, String time, String date, double amount) {
        this.withdrawalId = withdrawalId;
        this.shiftId = shiftId;
        this.time = time;
        this.date = date;
        this.amount = amount;
    }

    public boolean isValidWithdrawal() {
        return getWithdrawalId() > 0 && getShiftId() > 0 && !time.equals("") && !date.equals("") && getAmount() > 0;
    }

    /**
     * @return the withdrawalId
     */
    public int getWithdrawalId() {
        return withdrawalId;
    }

    /**
     * @param withdrawalId the withdrawalId to set
     */
    public void setWithdrawalId(int withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    /**
     * @return the shiftId
     */
    public int getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
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
