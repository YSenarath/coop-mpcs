/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testSuite.dbTest;

import java.text.ParseException;
import org.apache.log4j.Logger;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class DateTimeTest {

    private static final Logger logger = Logger.getLogger(DateTimeTest.class);

    /**
     * @param args the command line arguments
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        logger.info("Current date : " + Utilities.getCurrentDate());
        logger.info("Current time : " + Utilities.getCurrentTime(false));
        logger.info("Convert time : " + Utilities.convert24hTo12h(Utilities.getCurrentTime(true)));

    }

}
