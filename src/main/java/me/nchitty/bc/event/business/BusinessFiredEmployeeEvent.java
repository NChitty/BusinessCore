package me.nchitty.bc.event.business;

import me.nchitty.bc.business.Business;
import me.nchitty.bc.player.Employee;

/**
 *
 * @author beastman3226
 */
public class BusinessFiredEmployeeEvent extends BusinessEvent {

    private Employee employee;
    
    public BusinessFiredEmployeeEvent(Business b, Employee e) {
        super(b);
        this.employee = e;
    }

    public BusinessFiredEmployeeEvent(int b, int e) {
        super(b);
        this.employee = Employee.EmployeeManager.getEmployee(e);
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(int e) {
        this.employee = Employee.EmployeeManager.getEmployee(e);
    }

    public void setEmployee(Employee e) {
        this.employee = e;
    }
}
