package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ey) {
                System.exit(3);
            }
            System.exit(3);
        }
    }
}
