package me.nchitty.bc.event.business;

import me.nchitty.bc.business.Business;
import me.nchitty.bc.player.Employee;
import me.nchitty.bc.player.EmployeeManager;

/**
 *
 * @author beastman3226
 */
public class BusinessHiredEmployeeEvent extends BusinessEvent {

    private Employee employee;
    public BusinessHiredEmployeeEvent(Business b, Employee e) {
        super(b);
        this.employee = e;
    }

    public BusinessHiredEmployeeEvent(int b, int e) {
        super(b);
        this.employee = EmployeeManager.getEmployee(e);
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(int e) {
        this.employee = EmployeeManager.getEmployee(e);
    }

    public void setEmployee(Employee e) {
        this.employee = e;
    }
}
