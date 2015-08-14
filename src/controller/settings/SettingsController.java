package controller.settings;

import database.connector.DBConnection;
import database.connector.DatabaseInterface;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.pos.Setting;

public class SettingsController implements DatabaseInterface {

    public static boolean addSetting(Setting setting) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "INSERT INTO " + SETTINGS + " (prop_key,prop_value) VALUES (?,?)";

        Object[] ob = {
            setting.getKey(),
            setting.getValue()

        };
        return DBHandler.setData(connection, query, ob) > 0;
    }

    public static boolean deleteSetting(String key) throws SQLException {

        Connection connection = DBConnection.getConnectionToDB();
        String query = "DELETE FROM " + SETTINGS + " WHERE prop_key=?";

        Object[] ob = {
            key
        };
        return DBHandler.setData(connection, query, ob) == 1;
    }

    public static Setting getSetting(String key) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT * FROM " + SETTINGS + " WHERE prop_key=? ";

        Object[] ob = {
            key
        };
        ResultSet resultSet = DBHandler.getData(connection, query, ob);
        if (resultSet.next()) {
            return new Setting(
                    resultSet.getString("prop_key"),
                    resultSet.getString("prop_value")
            );
        }
        return null;
    }

    public static boolean setSetting(Setting setting) throws SQLException {
        Connection connection = DBConnection.getConnectionToDB();
        String query = "UPDATE  " + SETTINGS + " SET prop_value=? WHERE prop_key=?";

        Object[] ob = {
            setting.getValue(),
            setting.getKey()
        };
        return DBHandler.setData(connection, query, ob) == 1;

    }
}
