package model.pos;

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
