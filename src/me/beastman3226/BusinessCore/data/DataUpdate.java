package me.beastman3226.BusinessCore.data;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.util.MessageUtility;

/**
 *
 * @author beastman3226
 */
public class DataUpdate extends Data {

    /**
     * See DataStore.deleteBusiness(String string);
     * @param owner
     */
    public static void deleteBusiness(String owner) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("DELETE FROM 'business'" + MessageUtility.newLine
                    + "WHERE BusinessOwner=" + owner);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * Deletes a job from the database
     * @param issuer The issuer of the job
     */
    public static void deleteJob(String issuer) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("DELETE FROM 'job'" + MessageUtility.newLine
                    + "WHERE JobIssuer=" + issuer);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * Updates a job to add a business
     * @param issuer the person who created the job
     * @param bId the business who is taking the job
     */
    public static void updateJob(String issuer, int bId) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("UPDATE 'job'" + MessageUtility.newLine
                    + "SET BusinessID="+bId+ MessageUtility.newLine
                    + "WHERE JobIssuer="+issuer);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * Changes the worth of a business
     * @param owner Name of the owner for the business
     * @param worth new worth
     */
    public static void setBusinessWorth(String owner, double worth) {
        try {
            if(s == null) {
                s =  Data.c.createStatement();
            }
            s.executeUpdate("UPDATE 'business'"+ MessageUtility.newLine
                    + "SET BusinessWorth="+worth+MessageUtility.newLine
                    + "WHERE BusinessOwner="+owner);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * Updates the number of completed jobs an employee has
     * @param employeeName name of the employee
     * @param completedJobs number of completed jobs
     */
    public static void updateEmployee(String employeeName, int completedJobs) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("UPDATE 'business'" + MessageUtility.newLine
                    + "SET CompletedJobs="+completedJobs + MessageUtility.newLine
                    + "WHERE EmployeeName="+employeeName);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * updates scouted jobs
     * @param employeeName name of the employee
     * @param scoutedJobs scouted jobs
     */
    public static void updateEmployeeScouted(String employeeName, int scoutedJobs) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("UPDATE 'business'" + MessageUtility.newLine
                    + "SET Scouted="+scoutedJobs + MessageUtility.newLine
                    + "WHERE EmployeeName="+employeeName);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * Changes the current job
     * @param employeeName name of employee
     * @param id job id
     */
    public static void updateEmployeeJob(String employeeName, int id) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("UPDATE 'business'" + MessageUtility.newLine
                    + "SET ActiveJobId="+id + MessageUtility.newLine
                    + "WHERE EmployeeName="+employeeName);
        } catch (SQLException ex) {
           Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
           BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    public static void purgeResetBusiness() {

    }

    public static void purgeResetEmployee() {

    }

    public static void purgeResetJob() {

    }
}
