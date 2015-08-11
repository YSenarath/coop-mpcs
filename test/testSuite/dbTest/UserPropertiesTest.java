/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testSuite.dbTest;

import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import org.apache.log4j.Logger;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class UserPropertiesTest {

    private static final Logger logger = Logger.getLogger(UserPropertiesTest.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here

            // Utilities.saveProperty("counter", "1");
            Utilities.clearProperties();
        } catch (BackingStoreException ex) {
            java.util.logging.Logger.getLogger(UserPropertiesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //logger.info("Counter : " + Utilities.loadProperty("counter"));
       // System.out.println(7.0 / 3);
        //System.out.println(Math.floor(7.0 / 3)*3);
        
        System.out.println(Utilities.getSHA1("123".toCharArray()));
        System.out.println(Utilities.getSHA1("1234".toCharArray()));
        System.out.println(Utilities.getSHA1("159".toCharArray()));
                
    }

}
