package me.beastman3226.BusinessCore.util;

import me.beastman3226.BusinessCore.business.Business;
import me.beastman3226.BusinessCore.player.Employee;

/**
 *
 * @author beastman3226
 */
public class PayCalculator {

    public static double calculate(Business b, String employee) {
        double pay = b.getNumberOfEmployees()/b.getWorth();
        if(Employee.getEmployee(b.getEmployeeList()[b.getIndexOf(employee)]).getCompletedJobs() > 0) {
            pay = pay * (Employee.getEmployee(b.getEmployeeList()[b.getIndexOf(employee)]).getCompletedJobs() * .1);
        } else if (Employee.getEmployee(b.getEmployeeList()[b.getIndexOf(employee)]).getCompletedJobs() == 0) {
            pay = pay - (pay*.5);
        }
        return pay;
    }

}
