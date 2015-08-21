package model.people;

public class User {

    public final static String MANAGER = "MANAGER";
    public final static String CASHIER = "CASHIER";
    public final static String INVENTORY = "INVENTORY";

    private String userName;
    private String password;
    private String userType;
    private boolean loggedin;

    public User(String userName, String password, String userType, boolean loggedin) {
        this.userName = userName;
        this.password = password;
        switch (userType.toLowerCase().trim()) {
            case "manager":
                this.userType = MANAGER;
                break;
            case "cashier":
                this.userType = CASHIER;
                break;
            case "inventory":
                this.userType = INVENTORY;
                break;
        }
        this.loggedin = loggedin;
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
    public String getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * @return the loggedin
     */
    public boolean isLoggedin() {
        return loggedin;
    }

    /**
     * @param loggedin the loggedin to set
     */
    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public String dataTruncation(String level) {
        String ret = "";
       
        switch (level.trim().toUpperCase()) {
            case User.MANAGER:
                ret= "manager";
                break;
            case CASHIER :
                ret= "cashier";
                break;
            case INVENTORY :
                ret= "inventory_manager";
                break;
                
        }
 
        return ret;
    }
    @Override
    public String toString(){
        return userName;
    }
}
