package me.beastman3226.bc.player;

import java.util.HashMap;

/**
 *
 * @author beastman3226
 */
public class EmployeeManager {

    public static HashMap<String, Integer> pending = new HashMap<>(50);

    public static Employee addEmployee(String name) {
        Employee employee = new Employee(name, Employee.employeeList.size() + 1);
        Employee.employeeList.add(employee);
        return employee;
    }

    public static Employee getEmployee(String name) {
        Employee employee = null;
        for(Employee e : Employee.employeeList) {
            if(e.getName().equalsIgnoreCase(name)) {
                employee = e;
                break;
            }
        }
        return employee;
    }

    public static Employee getEmployee(int id) {
        Employee employee = null;
        for(Employee e : Employee.employeeList) {
            if(e.getID() == id) {
                employee = e;
                break;
            }
        }
        return employee;
    }

    public static boolean isEmployee(String name) {
        return getEmployee(name) != null;
    }
}
