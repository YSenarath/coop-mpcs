/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.pos;

/**
 *
 * @author Shehan
 */
public class User {

    private String userName;
    private String employeeId;
    private String password;
    private UserType userType;
    private boolean isLoggedin;

    public User(String userName, String password, String userType, boolean isLoggedin) {
        this.userName = userName;
        this.password = password;
        switch (userType) {
            case "manager":
                this.userType = UserType.MANAGER;
                break;
            case "cashier":
                this.userType = UserType.CASHIER;
                break;
            case "inventory_manager":
                this.userType = UserType.INVENTORY;
                break;
        }
        this.isLoggedin = isLoggedin;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the userType
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * @return the isLoggedin
     */
    public boolean isLoggedin() {
        return isLoggedin;
    }

    /**
     * @param isLoggedin the isLoggedin to set
     */
    public void setIsLoggedin(boolean isLoggedin) {
        this.isLoggedin = isLoggedin;
    }

}
