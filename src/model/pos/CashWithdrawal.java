package model.pos;

public class CashWithdrawal {

    private int withdrawalId;
    private String username;
    private int counterId;
    private String time;
    private String date;
    private double amount;

    public CashWithdrawal(int withdrawalId) {
        this.withdrawalId = withdrawalId;
        this.username = "";
        this.counterId = 0;
        this.time = "";
        this.date = "";
        this.amount = 0;
    }

    public CashWithdrawal(int withdrawalId, String username, int counterId, String time, String date, double amount) {
        this.withdrawalId = withdrawalId;
        this.username = username;
        this.counterId = counterId;
        this.time = time;
        this.date = date;
        this.amount = amount;
    }

    public boolean isValidWithdrawal() {
        return withdrawalId > 0 && !username.equals("") && counterId > 0 && !time.equals("") && !date.equals("") && amount > 0;
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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the counterId
     */
    public int getCounterId() {
        return counterId;
    }

    /**
     * @param counterId the counterId to set
     */
    public void setCounterId(int counterId) {
        this.counterId = counterId;
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
