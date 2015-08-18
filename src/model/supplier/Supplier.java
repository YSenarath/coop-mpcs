/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.supplier;

import java.util.Date;

/**
 *
 * @author Yasas
 */
public class Supplier {

    private String supplerID;
    private String name;
    private String contactPerson;
    private String address;
    private int telephoneNumber;
    private int fax;
    private String email;
    private Date ragDate;
    private Date cancelDate;

    public Supplier(String supplerID, String name, String contactPerson, String address, int telephoneNumber, int fax, String email, Date ragDate, Date cancelDate) {
        this.supplerID = supplerID;
        this.name = name;
        this.contactPerson = contactPerson;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.fax = fax;
        this.email = email;
        this.ragDate = ragDate;
        this.cancelDate = cancelDate;
    }

    public String getSupplerID() {
        return supplerID;
    }

    public void setSupplerID(String supplerID) {
        this.supplerID = supplerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(int telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public int getFax() {
        return fax;
    }

    public void setFax(int fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRagDate() {
        return ragDate;
    }

    public void setRagDate(Date ragDate) {
        this.ragDate = ragDate;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    @Override
    public String toString() {
        return this.name; //To change body of generated methods, choose Tools | Templates.
    }
}
