/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.pos;

import controller.pos.CounterController;
import controller.pos.UserController;
import java.sql.SQLException;
import model.pos.CounterLogin;
import model.pos.User;
import org.apache.log4j.Logger;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class UserLogInHandler {

    private static final Logger logger = Logger.getLogger(UserLogInHandler.class);

    public static boolean isUserAuthenticated(String userName, char[] password, String requestedAccesslevel) throws Exception {
        logger.debug("isUserAuthenticated invoked");
        if (UserController.isUserAuthenticated(userName, new String(password))) {
            User user = UserController.getUser("user_name", userName);
            if ((requestedAccesslevel == User.MANAGER || requestedAccesslevel == User.INVENTORY) && user.getUserType() == User.CASHIER) {
                throw new Exception("User does not have administrator privilages ");
            }
            if (!(user.getUserType() == User.MANAGER || user.getUserType() == User.CASHIER)) {
                throw new Exception("User does not have pos privilages ");
            }
            if (user.getUserType() != User.MANAGER && user.isLoggedin()) {
                throw new Exception("User :" + userName + " is already logged in");
            }
            return true;
        }
        return false;

    }

    public static boolean performCounterLogin(String userName, double intialAmount) throws SQLException {
        logger.debug("setIntitialAmount invoked");
        //Get cashier info ,counter info ,time, date and create a counter login
        User user = UserController.getUser("user_name", userName);
        return UserController.setUserLoginState(userName, true)
                && CounterController.addCounterLogin(new CounterLogin(user.getUserName(), Utilities.loadProperty("counter"),
                                Utilities.getCurrentTime(true), Utilities.getCurrentDate(), intialAmount));
    }
}
