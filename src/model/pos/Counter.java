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
public class Counter {

    private int counterId;
    private double currentAmount;

    public Counter(int counterId, double currentAmount) {
        this.counterId = counterId;
        this.currentAmount = currentAmount;
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
     * @return the currentAmount
     */
    public double getCurrentAmount() {
        return currentAmount;
    }

    /**
     * @param currentAmount the currentAmount to set
     */
    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

}
