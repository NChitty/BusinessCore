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
        Employee  e = Employee.getEmployee(b.getEmployeeList().get(b.getEmployeeList().indexOf(employee)));
        if(e.getCompletedJobs() > 0) {
            pay = pay * (e.getCompletedJobs() * .1);
        } else if (e.getCompletedJobs() == 0) {
            pay = pay - (pay*.45);
        }
        return pay;
    }

}
