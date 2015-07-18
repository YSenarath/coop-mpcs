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

    private int  shiftId;
    private String userName;
    private int counterId;
    private String time;
    private String date;
    private double initialAmount;

    public CounterLogin(String userName, int counterId, String time, String date, double initialAmount) {
        this.shiftId = -1;
        this.userName = userName;
        this.counterId = counterId;
        this.time = time;
        this.date = date;
        this.initialAmount = initialAmount;
    }

    public CounterLogin(int shiftId, String userName, int counterId, String time, String date, double initialAmount) {
        this.shiftId = shiftId;
        this.userName = userName;
        this.counterId = counterId;
        this.time = time;
        this.date = date;
        this.initialAmount = initialAmount;
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
    public String getuUserName() {
        return userName;
    }

    /**
     * @param user_name the userName to set
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
