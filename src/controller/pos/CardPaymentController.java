package controller.pos;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.pos.CardPayment;

public class CardPaymentController implements DatabaseInterface {

    public static boolean addCardPayment(CardPayment cardPayment) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + CARD_PAYMENT + " (bill_id,card_payment_id,card_type,card_no,amount) VALUES (?,?,?,?,?) ";
        Object[] ob = {
            cardPayment.getInvoiceId(),
            cardPayment.getPaymentId(),
            cardPayment.getCardType(),
            cardPayment.getCardNo(),
            cardPayment.getAmount(),};
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static ArrayList<CardPayment> getCardPayments(int invoiceNo) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + CARD_PAYMENT + " WHERE bill_id=? ";
        Object[] ob = {
            invoiceNo
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        ArrayList<CardPayment> cardPayments = new ArrayList();

        while (resultSet.next()) {
            CardPayment cardPayment = new CardPayment(
                    resultSet.getInt("bill_id"),
                    resultSet.getInt("card_payment_id"),
                    resultSet.getString("card_type"),
                    resultSet.getString("card_no"),
                    resultSet.getDouble("amount")
            );
            cardPayments.add(cardPayment);
        }
        return cardPayments;
    }

}
