/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.pos;

/**
 *
 * @author Shehan
 */
public class CounterLogin {

    private String shiftId;
    private String employeeId;
    private String counterId;
    private String time;
    private String date;
    private double initialAmount;

    public CounterLogin(String employeeId, String counterId, String time, String date, double initialAmount) {
        this.shiftId = null;
        this.employeeId = employeeId;
        this.counterId = counterId;
        this.time = time;
        this.date = date;
        this.initialAmount = initialAmount;
    }

    public CounterLogin(String shiftId, String employeeId, String counterId, String time, String date, double initialAmount) {
        this.shiftId = shiftId;
        this.employeeId = employeeId;
        this.counterId = counterId;
        this.time = time;
        this.date = date;
        this.initialAmount = initialAmount;
    }

    /**
     * @return the shiftId
     */
    public String getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return the employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the counterId
     */
    public String getCounterId() {
        return counterId;
    }

    /**
     * @param counterId the counterId to set
     */
    public void setCounterId(String counterId) {
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

}
