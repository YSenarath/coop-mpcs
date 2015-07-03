/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ledger;

import java.util.Date;
import model.supplier.Supplier;

/**
 *
 * @author Yasas
 */
public class SalesInvoice {

    private String f16aNumber;
    private String invoiceNo;
    private Date invoiceDate;
    private String employee;
    private String mpcs;
    private String paymentType;

    public String getF16aNumber() {
        return f16aNumber;
    }

    public void setF16aNumber(String f16aNumber) {
        this.f16aNumber = f16aNumber;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getMpcs() {
        return mpcs;
    }

    public void setMpcs(String mpcs) {
        this.mpcs = mpcs;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

}
