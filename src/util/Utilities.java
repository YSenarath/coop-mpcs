package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

/**
 *
 * @author Shehan
 */
public class Utilities {

    //Do not instantiate this class
    private Utilities() {

    }

    //Get the mysql compatible current date as string
    public static String getCurrentDate() {
        return (new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    //Get the mysql compatible current time in 24h clock as string
    public static String getCurrentTime(boolean is24h) {
        if (is24h) {
            return (new SimpleDateFormat("HH:mm:ss").format(new Date())).toLowerCase();
        } else {
            return (new SimpleDateFormat("hh:mm:ss a").format(new Date())).toLowerCase();
        }

    }

    //Convert 24hour time to 12 hour time
    public static String convert24hTo12h(String str24h) throws ParseException {
        DateFormat format24h = new SimpleDateFormat("HH:mm:ss");
        Date time = format24h.parse(str24h);
        DateFormat format12h = new SimpleDateFormat("hh:mm:ss a");
        return format12h.format(time).toLowerCase();
    }

    //Show msg boxes
    public static void showMsgBox(String msg, String title, int msgType) {
        JOptionPane.showMessageDialog(null, msg, title, msgType);
    }

    //Save a client app specific property
    public static void saveProperty(String key, String value) {
        Preferences.userNodeForPackage(Utilities.class).put(key, value);
    }

    //Load a client app specific property
    public static String loadProperty(String key) {
        return Preferences.userNodeForPackage(Utilities.class).get(key, "NULL");
    }
}
