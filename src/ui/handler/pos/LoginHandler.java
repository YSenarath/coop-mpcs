/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.pos;

import controller.pos.LoginController;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JFrame;
import model.pos.User;
import org.apache.log4j.Logger;

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

    public boolean isUserAuthenticated(String userName, char[] password, double initialAmount) throws SQLException {
        String strPassword = new String(password);
        //get users from the datanase using LoginControoler and authenticate the given username and password

        //send user name and password and use a SELECT query 
        if (!(userName.isEmpty() || strPassword.isEmpty())) {
            ArrayList<User> allUsers = LoginController.getAllCashRecipts();

            for (User user : allUsers) {
                logger.info(user.getUserName() + " , " + user.getPassword());
                if (user.getUserName().equals(userName) && user.getPassword().equals(strPassword)) {
                    setIntitialAmount(initialAmount);
                    return true;
                }
            }

        }

        return false;
    }

    private void setIntitialAmount(double initialAmount) {

    }
}
