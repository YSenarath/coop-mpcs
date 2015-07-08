/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.pos;

import controller.pos.UserController;
import java.sql.SQLException;
import javax.swing.JFrame;
import org.apache.log4j.Logger;

/**
 *
 * @author Shehan
 */
public class UserLogOffHandler {

    private static final Logger logger = Logger.getLogger(UserLogInHandler.class);
    private final JFrame posInterface;

    public UserLogOffHandler(JFrame posInterface) {
        this.posInterface = posInterface;
    }

    public void logOffUser(String userName) throws SQLException {
        UserController.setUserLoginState(userName, false);
    }

}