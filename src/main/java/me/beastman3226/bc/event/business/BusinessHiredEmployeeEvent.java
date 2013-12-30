package me.beastman3226.bc.event.business;

import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;

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

    public int[] getFinalList() {
        return this.getBusiness().addEmployee(this.employee.getID()).getEmployeeIDs();
    }
}
