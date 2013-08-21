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

    public static void loading(BusinessMain plugin) {
        Map<String, Object> Configvalues = plugin.eConfig.getConfig().getValues(true);
        Collection<Object> value = Configvalues.values();
        for(Object values : value) {
           String stringRep = values.toString();
           Employee.fromString(stringRep);
        }
    }

    public

}
