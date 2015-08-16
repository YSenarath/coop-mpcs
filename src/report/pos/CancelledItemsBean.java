/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report.pos;

/**
 *
 * @author Shehan
 */
public class CancelledItemsBean {

    private final String invoiceId;
    private final String productCode;
    private final String name;
    private final String qty;

    public CancelledItemsBean(String invoiceId, String productCode, String name, String qty) {
        this.invoiceId = invoiceId;
        this.productCode = productCode;
        this.name = name;
        this.qty = qty;
    }

    /**
     * @return the invoiceId
     */
    public String getInvoiceId() {
        return invoiceId;
    }

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

}
