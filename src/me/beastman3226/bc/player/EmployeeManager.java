package me.beastman3226.bc.player;

import java.util.HashMap;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.data.Data;
import me.beastman3226.bc.data.EmployeeHandler;
import me.beastman3226.bc.data.file.EmployeeFileManager;
import me.beastman3226.bc.data.file.FileData;

/**
 *
 * @author beastman3226
 */
public class EmployeeManager {

    public static HashMap<String, Integer> pending = new HashMap<>(50);

    public static Employee addEmployee(String name) {
        Employee employee = new Employee(name, 1000 + Employee.employeeList.size() + 1);
        Employee.employeeList.add(employee);
        if(Information.database) {
            EmployeeHandler.add(Data.EMPLOYEE.add("EmployeeID", employee.getID())
                                             .add("EmployeeName", name)
                                             .add("BusinessID", employee.getBusiness().getID())
                                             .add("CompletedJobs", employee.getCompletedJobs())
                                             .add("JobID", employee.getCurrentJob()));
        } else {
            EmployeeFileManager.editConfig(new FileData().add(name + ".name", name)
                    .add(name + ".id", employee.getID())
                    .add(name + ".business", employee.getBusiness().getID())
                    .add(name + ".completed", employee.getCompletedJobs())
                    .add(name + ".job", employee.getCurrentJob()));

        }
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
