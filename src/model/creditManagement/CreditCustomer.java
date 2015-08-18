/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.creditManagement;

/**
 *
 * @author HP
 */
public class CreditCustomer {

    private String customerName;
    private String customerAddress;
    private String telephone;
    private String nic;
    private int customerId;
    private double currentCredit;

    public CreditCustomer(int customerId, String customerName, String customerAddress, String telephone, String nic, double currentCredit) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.nic = nic;
        this.telephone = telephone;
        this.customerId = customerId;
        this.currentCredit = currentCredit;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the customerAddress
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @param customerAddress the customerAddress to set
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the nic
     */
    public String getNic() {
        return nic;
    }

    /**
     * @param nic the nic to set
     */
    public void setNic(String nic) {
        this.nic = nic;
    }

    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the currentCredit
     */
    public double getCurrentCredit() {
        return currentCredit;
    }

    /**
     * @param currentCredit the currentCredit to set
     */
    public void setCurrentCredit(double currentCredit) {
        this.currentCredit = currentCredit;
    }

}
