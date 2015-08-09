package util;

import database.connector.DatabaseInterface;
import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Utilities {

    //Do not instantiate this class
    private Utilities() {

    }

    //Convert 24hour time to 12 hour time
    public static String convert24hTo12h(String str24h) throws ParseException {
        DateFormat format24h = new SimpleDateFormat("HH:mm:ss");
        Date time = format24h.parse(str24h);
        DateFormat format12h = new SimpleDateFormat("hh:mm:ss a");
        return format12h.format(time).toLowerCase();
    }

    //Deep clone a object
    public static Object deepClone(Object object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }

    //double Format text filed 
    public static void doubleFormatComponentText(JTextField jTextField) {

        if (jTextField.getText().equals("") || jTextField.getText().isEmpty()) {
            return;
        }
        try {
            jTextField.setText(String.format("%.2f", Double.parseDouble(jTextField.getText())));
        } catch (NumberFormatException | IllegalFormatConversionException ex) {
            jTextField.setText("");
        }
    }

    //Get the mysql compatible current date as string
    public static Date getCurrentDate() {
        //return (new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        String date = getStringDate((Calendar.getInstance().getTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
        }
        return null;

    }

    public static String getStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    //Get the mysql compatible current time in 24h clock as string
    public static String getCurrentTime(boolean is24h) {
        if (is24h) {
            return (new SimpleDateFormat("HH:mm:ss").format(new Date())).toLowerCase();
        } else {
            return (new SimpleDateFormat("hh:mm:ss a").format(new Date())).toLowerCase();
        }

    }

    //convert a string date to util.date
    public static Date getDateFromString(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException | NullPointerException ex) {
            return null;
        }
    }

    //Check if a util.date is in given range
    public static boolean isDateBetweenRange(Date date, Date lowerLimit, Date upperLimit) {
        if (date == null) {
            return false;
        } else {
            if (lowerLimit != null && upperLimit != null) {
                return date.after(lowerLimit) && date.before(upperLimit);
            } else if (lowerLimit != null) {
                return date.after(lowerLimit);
            }
        }
        return false;
    }

    //Used to check if product is expired
    public static boolean isDateBeforeLimit(Date date, Date upperLimit) {

        if (date != null && upperLimit != null) {
            return date.before(upperLimit);
        }
        return false;
    }

    //Check if String date is in given range
    public static boolean isDateBetweenRange(String date, String lowerLimit, String upperLimit) {
        return isDateBetweenRange(getDateFromString(date), getDateFromString(lowerLimit), getDateFromString(upperLimit));
    }

    //Format  int to key
    public static String convertKeyToString(int key, String type) {

        String suffix;
        int size;

        switch (type) {

            case DatabaseInterface.DEPARTMENT:
                suffix = "D";
                size = 2;
                break;

            case DatabaseInterface.CATEGORY:
                suffix = "C";
                size = 4;
                break;

            case DatabaseInterface.PRODUCT:
                suffix = "P";
                size = 5;
                break;

            case DatabaseInterface.BATCH:
                suffix = "B";
                size = 3;
                break;

            case DatabaseInterface.COUNTER_LOGIN:
                suffix = "L";
                size = 6;
                break;

            case DatabaseInterface.CASH_WITHDRAWAL:
                suffix = "W";
                size = 5;
                break;

            case DatabaseInterface.INVOICE:
                suffix = "I";
                size = 5;
                break;
                
            case DatabaseInterface.REFUND:
                suffix = "U";
                size = 5;
                break;
            case DatabaseInterface.GRN:
                suffix = "G";
                size = 5;
                break;

            case DatabaseInterface.SUPPLIER:
                suffix = "S";
                size = 3;
                break;

            case DatabaseInterface.SRN:
                suffix = "R";
                size = 5;
                break;

            default:
                return null;
        }

        return String.format(suffix + "%0" + size + "d", key);
    }

    //Get int from code
    public static int convertKeyToInteger(String key) {
        if (key.isEmpty()) {
            return -1;
        }
        key = key.substring(1).trim();
        try {
            return Integer.parseInt(key);
        } catch (Exception e) {
            return 0;
        }
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

    //remove a client app specific property
    public static void removeProperty(String key) {
        Preferences.userNodeForPackage(Utilities.class).remove(key);
    }

    //clear a client app specific property
    public static void clearProperties() throws BackingStoreException {
        Preferences.userNodeForPackage(Utilities.class).clear();
    }

    public static void setupUI() {
        Properties props = new Properties();

        //RGB colours
        String buttonClolor = "200 200 200";
        String controlClolor = "200 200 200";

        String menuColor = "222 222 222";
        String menuBackgroundColor = "224 224 224";

        String selectionBackgroundColor = "240 240 240";
        String selectionForegroundColor = "67 148 103";

        String rollOverClolor = "114 114 114";

        String frameColor = "171 171 171";
        String windowTitleColor = "10 10 10";

        //Customize Theme
        props.put("logoString", "");

        props.put("linuxStyleScrollBar", "on");
        props.put("centerWindowTitle", "on");
        props.put("textAntiAliasing", "on");
        props.put("textAntiAliasingMode", "default");
        props.put("toolbarDecorated", "off");
        props.put("windowDecoration", "on");
        props.put("dynamicLayout", "on");
        props.put("darkTexture", "off");

        props.put("buttonColor", buttonClolor);//button colours
        props.put("buttonColorLight", buttonClolor);
        props.put("buttonColorDark", buttonClolor);

        props.put("controlColor", controlClolor);//Control colours
        props.put("controlColorLight", controlClolor);
        props.put("controlColorDark", controlClolor);

        props.put("menuColorLight", menuColor);//menu colours
        props.put("menuColorDark", menuColor);
        props.put("menuBackgroundColor", menuBackgroundColor);

        props.put("selectionBackgroundColor", selectionBackgroundColor);//hilighted text
        props.put("selectionForegroundColor", selectionForegroundColor);

        props.put("rolloverColor", rollOverClolor); //on hovering
        props.put("rolloverColorLight", rollOverClolor);
        props.put("rolloverColorDark", rollOverClolor);

        props.put("frameColor", frameColor);
        props.put("windowTitleColorLight", windowTitleColor);//Windows boarder colours
        props.put("windowTitleColorDark", windowTitleColor);
        props.put("disabledForegroundColor", windowTitleColor);

        try {
            com.jtattoo.plaf.graphite.GraphiteLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            // UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ey) {
                System.exit(3);
            }
            System.exit(3);
        }
    }

    //nadheesh
    public static Date getTody() {
        String date = getStringDate((Calendar.getInstance().getTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void ShowErrorMsg(Component component, String msg) {
        JOptionPane.showMessageDialog(component, msg, "Error", 0);
    }

    public static void ShowWarningMsg(Component component, String msg) {
        JOptionPane.showMessageDialog(component, msg, "Warning", 2);
    }
    //nadheesh//end
}
