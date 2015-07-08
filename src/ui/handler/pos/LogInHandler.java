/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.pos;

import controller.pos.CounterController;
import controller.pos.EmployeeController;
import controller.pos.UserController;
import java.sql.SQLException;
import javax.swing.JFrame;
import model.pos.CounterLogin;
import model.pos.Employee;
import model.pos.User;
import model.pos.UserType;
import org.apache.log4j.Logger;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class LogInHandler {

    private static final Logger logger = Logger.getLogger(LogInHandler.class);
    private final JFrame loginFrame;

    public LogInHandler(JFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

    public boolean isUserAuthenticated(String userName, char[] password) throws Exception {
        logger.debug("isUserAuthenticated invoked");
        if (UserController.isUserAuthenticated(userName, new String(password))) {
            User user = UserController.getUser("user_name", userName);
            if (!(user.getUserType() == UserType.MANAGER || user.getUserType() == UserType.CASHIER)) {
                throw new Exception("Only cashier and admin can access the pos system ");
            }
            if (user.getUserType() != UserType.MANAGER && user.isLoggedin()) {
                throw new Exception("User :" + userName + " is already logged in");
            }
            return true;
        }
        return false;

    }

    public boolean performCounterLogin(String userName, double intialAmount) throws SQLException {
        logger.debug("setIntitialAmount invoked");
        //Get cashier info ,counter info ,time, date and create a counter login
        User user = UserController.getUser("user_name", userName);
        return UserController.setUserLoginState(userName, true)
                && CounterController.addCounterLogin(new CounterLogin(user.getUserName(), Utilities.loadProperty("counter"),
                                Utilities.getCurrentTime(true), Utilities.getCurrentDate(), intialAmount));
    }
}
