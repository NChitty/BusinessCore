package me.beastman3226.BusinessCore.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.business.Business;
import me.beastman3226.BusinessCore.job.Job;
import me.beastman3226.BusinessCore.player.Employee;

/**
 *
 * @author beastman3226
 */
public class MyPersist {

    private static BusinessMain main;

    public static void saving(BusinessMain plugin) {
        /*
         * Business saving
         */
        for(Business b : Business.businessList) {
            if(b != null) {
                plugin.bConfig.set(b.getName(), b.toString());
            }
        }
        /*
         * Employee Saving
         */
        for(Employee e : Employee.employeeList) {
            plugin.eConfig.set(e.getEmployeeName(), e.toString());
        }
        /*
         * Job saving
         */
        for(Job j : Job.jobList) {
            if(j != null) {
                plugin.jConfig.set(j.getId() + "", j.toString());
            }
        }
    }

    public static void loading(BusinessMain plugin) throws NullPointerException {
        main = plugin;
        Map<String, Object> eConfigvalues = plugin.eConfig.getConfig().getValues(true);
        Collection<Object> value1 = eConfigvalues.values();
        for(Object values : value1) {
           String stringRep = values.toString();
           Employee.fromString(stringRep);
        }
        Map<String, Object> jConfigvalues = plugin.jConfig.getConfig().getValues(true);
        Collection<Object> value2 = jConfigvalues.values();
        for(Object values : value2) {
            Job.fromString(values.toString());
        }
        Map<String, Object> bConfigvalues = plugin.bConfig.getConfig().getValues(true);
        Collection<Object> value3 = bConfigvalues.values();
        for(Object value : value3) {
            Business business = new Business(value.toString());
        }
    }

    public static Employee loadEmployee(String string, Job j) {
        Employee emp = Employee.fromString((String) main.eConfig.get(string));
        emp.setActiveJob(j);
        return emp;
    }

    public static Job loadJob(String string, Employee aThis) {
        Job job = Job.fromString((String) main.jConfig.get(string));
        job.setWorker(aThis);
        return job;
    }

}
