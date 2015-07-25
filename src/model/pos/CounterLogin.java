package model.pos;

public class CounterLogin {

    private int shiftId;
    private String userName;
    private int counterId;
    private String logInTime;
    private String logInDate;
    private String logOffTime;
    private String logOffDate;
    private double initialAmount;
    private double cashWithdrawals;
    private double logOffExpected;
    private double logOffActual;
    private boolean shiftEnded;

    public CounterLogin(String userName, int counterId, String logInTime, String logInDate, double initialAmount) {
        this.shiftId = -1;
        this.userName = userName;
        this.counterId = counterId;
        this.logInTime = logInTime;
        this.logInDate = logInDate;
        this.initialAmount = initialAmount;
        this.cashWithdrawals = -1;
        this.logOffExpected = -1;
        this.logOffActual = -1;
        this.shiftEnded = false;
    }

    public CounterLogin(int shiftId, String userName, int counterId, String logInTime, String logInDate, String logOffTime, String logOffDate, double initialAmount, double cashWithdrawals, double logOffExpected, double logOffActual, boolean shiftEnded) {
        this.shiftId = shiftId;
        this.userName = userName;
        this.counterId = counterId;
        this.logInTime = logInTime;
        this.logInDate = logInDate;
        this.logOffTime = logOffTime;
        this.logOffDate = logOffDate;
        this.initialAmount = initialAmount;
        this.cashWithdrawals = cashWithdrawals;
        this.logOffExpected = logOffExpected;
        this.logOffActual = logOffActual;
        this.shiftEnded = shiftEnded;
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
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
     * @return the logInTime
     */
    public String getLogInTime() {
        return logInTime;
    }

    /**
     * @param logInTime the logInTime to set
     */
    public void setLogInTime(String logInTime) {
        this.logInTime = logInTime;
    }

    /**
     * @return the logInDate
     */
    public String getLogInDate() {
        return logInDate;
    }

    /**
     * @param logInDate the logInDate to set
     */
    public void setLogInDate(String logInDate) {
        this.logInDate = logInDate;
    }

    /**
     * @return the logOffTime
     */
    public String getLogOffTime() {
        return logOffTime;
    }

    /**
     * @param logOffTime the logOffTime to set
     */
    public void setLogOffTime(String logOffTime) {
        this.logOffTime = logOffTime;
    }

    /**
     * @return the logOffDate
     */
    public String getLogOffDate() {
        return logOffDate;
    }

    /**
     * @param logOffDate the logOffDate to set
     */
    public void setLogOffDate(String logOffDate) {
        this.logOffDate = logOffDate;
    }

    /**
     * @return the initialAmount
     */
    public double getInitialAmount() {
        return initialAmount;
    }

    /**
     * @param initialAmount the initialAmount to set
     */
    public void setInitialAmount(double initialAmount) {
        this.initialAmount = initialAmount;
    }

    /**
     * @return the cashWithdrawals
     */
    public double getCashWithdrawals() {
        return cashWithdrawals;
    }

    /**
     * @param cashWithdrawals the cashWithdrawals to set
     */
    public void setCashWithdrawals(double cashWithdrawals) {
        this.cashWithdrawals = cashWithdrawals;
    }

    /**
     * @return the logOffExpected
     */
    public double getLogOffExpected() {
        return logOffExpected;
    }

    /**
     * @param logOffExpected the logOffExpected to set
     */
    public void setLogOffExpected(double logOffExpected) {
        this.logOffExpected = logOffExpected;
    }

    /**
     * @return the logOffActual
     */
    public double getLogOffActual() {
        return logOffActual;
    }

    /**
     * @param logOffActual the logOffActual to set
     */
    public void setLogOffActual(double logOffActual) {
        this.logOffActual = logOffActual;
    }

    /**
     * @return the shiftEnded
     */
    public boolean isShiftEnded() {
        return shiftEnded;
    }

    /**
     * @param shiftEnded the shiftEnded to set
     */
    public void setShiftEnded(boolean shiftEnded) {
        this.shiftEnded = shiftEnded;
    }

    
}
