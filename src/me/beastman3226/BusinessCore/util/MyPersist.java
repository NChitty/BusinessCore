package me.beastman3226.BusinessCore.util;

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

            }
        }
        /*
         * Employee Saving
         */
        for(Employee e : Employee.employeeList) {

        }
        /*
         * Job saving
         */
        for(Job j : Job.jobList) {
            if(j != null) {

            }
        }
    }

    public static void loading(BusinessMain plugin) {
        
    }

}
