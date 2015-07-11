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
public class CardPayment extends Payment {

    private CardType cardType;
    private int cardNo;

    public CardPayment(int billId,int paymentId, CardType cardType, int cardNo, double amount) {
        super(billId,paymentId, amount);
        this.cardType = cardType;
        this.cardNo = cardNo;
    }

    /**
     * @return the cardType
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * @param cardType the cardType to set
     */
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    /**
     * @return the cardNo
     */
    public int getCardNo() {
        return cardNo;
    }

    /**
     * @param cardNo the cardNo to set
     */
    public void setCardNo(int cardNo) {
        this.cardNo = cardNo;
    }

}
