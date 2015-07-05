/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.pos;

import javax.swing.JFrame;
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

    public boolean isUserAuthenticated(String userName, char[] password) {
        String strPassword = new String(password);
        
        //get users from the datanase using LoginControoler and authenticate the given username and password
        return false;
    }

    public void setIntitialAmount(String amount) {

    }
}
