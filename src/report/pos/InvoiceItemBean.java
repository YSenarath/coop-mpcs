package report.pos;

/**
 *
 * @author Shehan
 */
public class InvoiceItemBean {

    private final String itemDesc;
    private final String itemCode;
    private final double itemQty;
    private final double itemPrice;
    private final double itemDiscount;
    private final double itemSubTotal;

    public InvoiceItemBean(String itemDesc, String itemCode, double itemQty, double itemPrice, double itemDiscount, double itemSubTotal) {
        this.itemDesc = itemDesc;
        this.itemCode = itemCode;
        this.itemQty = itemQty;
        this.itemPrice = itemPrice;
        this.itemDiscount = itemDiscount;
        this.itemSubTotal = itemSubTotal;
    }

    /**
     * @return the itemDesc
     */
    public String getItemDesc() {
        return itemDesc;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @return the itemQty
     */
    public double getItemQty() {
        return itemQty;
    }

    /**
     * @return the itemPrice
     */
    public double getItemPrice() {
        return itemPrice;
    }

    /**
     * @return the itemDiscount
     */
    public double getItemDiscount() {
        return itemDiscount;
    }

    /**
     * @return the itemSubTotal
     */
    public double getItemSubTotal() {
        return itemSubTotal;
    }

    
}
