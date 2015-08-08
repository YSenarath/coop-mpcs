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
public class Employee {
  
    
    private String employeeName;
    private int employeeId;
    private boolean voucherIssued=false;
    private String position;
    public Employee(int employeeId,String employeeName,String position,boolean voucherIssued){
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.voucherIssued = voucherIssued;
        this.position = position;
    }
  
    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the employeeId
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the voucherIssued
     */
    public boolean isVoucherIssued() {
        return voucherIssued;
    }

    /**
     * @param voucherIssued the voucherIssued to set
     */
    public void setVoucherIssued(boolean voucherIssued) {
        this.voucherIssued = voucherIssued;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }
    
}