package database.connector;

public interface DatabaseInterface {

    //Database connection
    public static final String SERVER = "localhost";//Default , must be a setting
    public static final String PORT = "3306";//Default , must be a setting
    public static final String DATABASE = "COOP";//Default , must be a setting

    public static final String MYSQL_USER_NAME = "admin";//Default , must be a setting
    public static final String MYSQL_PASSWORD = "password"; //Default , must be a setting

    //Database table names
    public static final String USER = "user_credentials";
    public static final String SETTINGS = "settings";

    public static final String CREDIT_CUSTOMER = "credit_customer";
    public static final String EMPLOYEE = "employee";

    public static final String COUNTER = "counter";
    public static final String CASH_WITHDRAWAL = "cash_withdrawal";
    public static final String COUNTER_LOGIN = "counter_login";

    public static final String DEPARTMENT = "department";
    public static final String CATEGORY = "category";
    public static final String CATEGORY_DISCOUNT = "category_discount";
    public static final String PRODUCT = "product";
    public static final String BATCH = "batch";
    public static final String BATCH_DISCOUNT = "batch_discount";

    public static final String INVOICE = "invoice";
    public static final String INVOICE_ITEMS = "invoice_items";
    public static final String REFUND = "refund";
    public static final String REFUND_ITEMS = "refund_items";

    public static final String CASH_PAYMENT = "cash_payment";
    public static final String CARD_PAYMENT = "card_payment";
    public static final String POSHANA_PAYMENT = "poshana_payment";
    public static final String EMPLOYEE_VOUCHER_PAYMENT = "employee_voucher_payment";
    public static final String CUSTOMER_VOUCHER_PAYMENT = "customer_voucher_payment";
    public static final String COOP_CREDIT_PAYMENT = "coop_credit_payment";

    public static final String SUPPLIER = "supplier";
    public static final String GRN = "grn";
    public static final String GRN_ITEM = "grn_item";
    public static final String GRN_CANCEL = "grn_cancel";
    public static final String SRN = "srn";
    public static final String SRN_ITEM = "srn_item";
    public static final String DAMAGED_STOCK = "damaged_stock";
    public static final String DAMAGED_STOCK_ITEM = "damaged_stock_item";

}
