/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connector;

/**
 *
 * @author Shehan
 */
public interface DatabaseInterface {

    //Database connection
    public static final String SERVER = "localhost";//Default , must be a setting
    public static final String PORT = "3306";//Default , must be a setting
    public static final String DATABASE = "COOP";//Default , must be a setting

    public static final String MYSQL_USER_NAME = "admin";//Default , must be a setting
    public static final String MYSQL_PASSWORD = "admin"; //Default , must be a setting

    //Database table names
    public static final String USER = "user_credentials";
    public static final String SETTINGS = "settings";

    public static final String CREDIT_CUSTOMER = "credit_customer";
    public static final String EMPLOYEE = "employee";

    public static final String COUNTER = "counter";
    public static final String CASH_WITHDRAWAL = "cash_withdrawal";
    public static final String COUNTER_LOGIN = "counter_login";

    public static final String DEPARTMENT = "department";
    public static final String CATAGORY = "category";
    public static final String CATEGORY_DISCOUNT = "category_discount";
    public static final String PRODUCT = "product";
    public static final String GRN = "grn";
    public static final String BATCH = "batch";
    public static final String BATCH_DISCOUNT = "batch_discount";

    public static final String INVOICE = "invoice";
    public static final String INVOICE_ITEMS = "invoice_items";
    public static final String CASH_PAYMENT = "cash_payment";
    public static final String CARD_PAYMENT = "card_payment";
    public static final String POSHANA_PAYMENT = "poshana_payment";
    public static final String EMPLOYEE_VOUCHER_PAYMENT = "employee_voucher_payment";
    public static final String CUSTOMER_VOUCHER_PAYMENT = "customer_voucher_payment";
    public static final String COOP_CREDIT_PAYMENT = "coop_credit_payment";

}
