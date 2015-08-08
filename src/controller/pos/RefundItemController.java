package controller.pos;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.connector.DatabaseInterface;
import java.util.ArrayList;
import model.pos.item.RefundItem;

public class RefundItemController implements DatabaseInterface {

    public static boolean addRefundItem(RefundItem refundItem) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + REFUND_ITEMS + " (refund_id,batch_id,product_id,discount,unit_price,qty) VALUES (?,?,?,?,?,?)";
        Object[] ob = {
            refundItem.getRefundId(),
            refundItem.getBatchId(),
            refundItem.getProductId(),
            refundItem.getDiscount(),
            refundItem.getUnitPrice(),
            refundItem.getQty()
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static ArrayList<RefundItem> getRefundItems(int refundId) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + REFUND_ITEMS + " WHERE refund_id=?";
        Object[] ob = {
            refundId
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        ArrayList<RefundItem> refundItems = new ArrayList();

        while (resultSet.next()) {
            RefundItem refundItem = new RefundItem(
                    resultSet.getInt("refund_id"),
                    resultSet.getInt("product_id"),
                    resultSet.getInt("batch_id"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDouble("qty"),
                    resultSet.getDouble("discount")
            );
            refundItems.add(refundItem);
        }

        return refundItems;

    }
}
