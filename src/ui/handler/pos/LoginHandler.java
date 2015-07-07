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
import org.apache.log4j.Logger;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class LoginHandler {

    private static final Logger logger = Logger.getLogger(LoginHandler.class);
    private final JFrame loginFrame;

    public LoginHandler(JFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

    public boolean isUserAuthenticated(String userName, char[] password) throws SQLException {
        logger.debug("isUserAuthenticated invoked");
        return UserController.isUserAuthenticated(userName, new String(password));
    }

    public Employee performCounterLogin(String userName, double intialAmount) throws SQLException {
        logger.debug("setIntitialAmount invoked");
        //Get cashier info ,counter info ,time, date and create a counter login
        Employee employee = EmployeeController.getEmployee(UserController.getUser(userName).getEmployeeId());
        if (CounterController.addCounterLogin(new CounterLogin(employee.getEmployee_Id(), Utilities.loadProperty("counter"),
                Utilities.getCurrentTime(true), Utilities.getCurrentDate(), intialAmount))) {
            return employee;
        }
        return null;
    }
}
