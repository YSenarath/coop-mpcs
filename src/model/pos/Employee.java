/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.pos;

/**
 *
 * @author Shehan
 */
public class Employee {
    private String employee_Id;
    private String name;

    public Employee(String employee_Id, String name) {
        this.employee_Id = employee_Id;
        this.name = name;
    }

    /**
     * @return the employee_Id
     */
    public String getEmployee_Id() {
        return employee_Id;
    }

    /**
     * @param employee_Id the employee_Id to set
     */
    public void setEmployee_Id(String employee_Id) {
        this.employee_Id = employee_Id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
