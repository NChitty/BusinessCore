package me.beastman3226.bc.player;

/**
 *
 * @author beastman3226
 */
public class EmployeeManager {

    public Employee addEmployee(String name) {
        Employee employee = new Employee(name, Employee.employeeList.size() + 1);
        Employee.employeeList.add(employee);
        return employee;
    }

}
