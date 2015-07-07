/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.definitions;

/**
 *
 * @author Shehan
 */
public interface AppConstants {

    //Database connection
    public static final String SERVER = "localhost";
    public static final String PORT = "3306";
    public static final String DATABASE = "COOP";
    
    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "admin";
    
    

    //Database table names
    public static final String CASHIER = "cashier";
    public static final String COUNTER = "counter";
    public static final String CASH_WITHDRAWAL = "cash_withdrawal";
    public static final String COUNTER_LOGIN = "counter_login";
    public static final String CASH_PAYMENT = "cash_payment";
    public static final String CARD_PAYMENT = "card_payment";
    public static final String DEPARTMENT = "department";
    public static final String CATAGORY = "catagory";
    public static final String PRODUCT = "product";
    public static final String BATCh = "batch";
    public static final String BILL_ITEMS = "bill_items";
    public static final String USER = "user_credentials";

}
