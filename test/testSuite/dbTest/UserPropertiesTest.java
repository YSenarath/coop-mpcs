/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testSuite.dbTest;

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
        // TODO code application logic here

        logger.info("Counter : " + Utilities.loadProperty("counter"));

        Utilities.saveProperty("counter", "1");
        logger.info("Counter : " + Utilities.loadProperty("counter"));

    }

}
