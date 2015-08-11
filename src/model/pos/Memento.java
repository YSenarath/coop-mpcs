package model.pos;

public class Memento {

    private final boolean member;
    private final Object[][] itemData;
    private final Object[][] paymentData;

    public Memento(boolean member, Object[][] itemData, Object[][] paymentData) {
        this.member = member;
        this.itemData = itemData;
        this.paymentData = paymentData;
    }

    public boolean isMember() {
        return member;
    }

    public Object[][] getItemData() {
        return itemData;
    }

    public Object[][] getPaymentData() {
        return paymentData;
    }

}
