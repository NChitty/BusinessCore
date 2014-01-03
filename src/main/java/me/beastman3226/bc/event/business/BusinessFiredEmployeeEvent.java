package me.beastman3226.bc.event.business;

import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;

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

    public Object[] finalEmployeeList() {
        return this.getBusiness().removeEmployee(employee.getID()).getEmployeeIDs();
    }

    public void setEmployeeList(int[] ids) {
        this.getBusiness().setEmployeeIDs(ids);
    }

}
