package model.pos.item;

public class RefundItem {

    private int refundId;
    private int productId;
    private int batchId;
    private double UnitPrice;
    private double qty;
    private double discount;

    //For reports
    private String desc;

    public RefundItem(int refundId, int productId, int batchId, double UnitPrice, double qty, double discount) {
        this.refundId = refundId;
        this.productId = productId;
        this.batchId = batchId;
        this.UnitPrice = UnitPrice;
        this.qty = qty;
        this.discount = discount;
    }

    public RefundItem(int refundId, int productId, int batchId, double UnitPrice, double qty, double discount, String desc) {
        this.refundId = refundId;
        this.productId = productId;
        this.batchId = batchId;
        this.UnitPrice = UnitPrice;
        this.qty = qty;
        this.discount = discount;
        this.desc = desc;
    }

    /**
     * @return the refundId
     */
    public int getRefundId() {
        return refundId;
    }

    /**
     * @param refundId the refundId to set
     */
    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    /**
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return the batchId
     */
    public int getBatchId() {
        return batchId;
    }

    /**
     * @param batchId the batchId to set
     */
    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    /**
     * @return the UnitPrice
     */
    public double getUnitPrice() {
        return UnitPrice;
    }

    /**
     * @param UnitPrice the UnitPrice to set
     */
    public void setUnitPrice(double UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    /**
     * @return the qty
     */
    public double getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(double qty) {
        this.qty = qty;
    }

    /**
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

}
