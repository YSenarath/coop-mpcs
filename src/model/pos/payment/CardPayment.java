package model.pos.payment;

public class CardPayment extends Payment {

    public static final String AMEX = "AMEX";
    public static final String MASTER = "MASTER";
    public static final String VISA = "VISA";

    private String cardType;
    private String cardNo;

    public CardPayment(int invoiceId, int paymentId, String cardType, String cardNo, double amount) {
        super(invoiceId, paymentId, amount);
        this.cardType = cardType;
        this.cardNo = cardNo;
    }

    /**
     * @return the cardType
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * @param cardType the cardType to set
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * @return the cardNo
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * @param cardNo the cardNo to set
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

}
