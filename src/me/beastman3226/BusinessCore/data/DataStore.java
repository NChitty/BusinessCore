package me.beastman3226.BusinessCore.data;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.util.MessageUtility;

/**Storing data
 *
 * @author beastman3226
 */
public class DataStore extends Data {

    /**<p>Creates a new table called 'business'.
     * Does not check to see if it has already been made.</p>
     */
    private static void createBusinessTable() {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.execute("CREATE TABLE 'business'"
                    + "("
                    + "BusinessName VARCHAR(255),"
                    + "BusinessID INTEGER,"
                    + "BusinessOwner VARCHAR(255),"
                    + "BusinessWorth FLOAT);");
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * <p>Creates an employee table, will error out if the table already exists.</p>
     */
    private static void createEmployeeTable () {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.execute("CREATE TABLE 'employee'" + "\r\n" +
                    "("
                    + "EmployeeName VARCHAR(255),"
                    + "BusinessID INTEGER,"
                    + "CompletedJobs INTEGER,"
                    + "ScoutedJobs INTEGER,"
                    + "HasJob BOOLEAN,"
                    + "ActiveJobId INTEGER"
                    + ");");
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * <p>Creates a table called 'job', errors out if the table exists</p>
     */
    private static void createJobTable () {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.execute("CREATE TABLE 'job'" + "\r\n" +
                    "("
                    + "JobIssuer VARCHAR(255),"
                    + "JobDescription TEXT,"
                    + "JobPayment FLOAT,"
                    + "JobLocationX FLOAT,"
                    + "JobLocationY FLOAT,"
                    + "JobLocationZ FLOAT,"
                    + "JobID INTEGER"
                    + "Employee VARCHAR(255),"
                    + "BusinessID INTEGER"
                    + ");");
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * Creates all the tables
     */
    public static void createTables() {
        createBusinessTable();
        createEmployeeTable();
        createJobTable();
    }

    /**
     * Adds a business to the table
     * @param name The name of the business
     * @param id The id
     * @param owner the owner
     * @param worth the worth
     */
    public static void addBusiness(String name, int id, String owner, double worth) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("INSERT INTO 'business'" + MessageUtility.newLine
                    + "VALUES ('" + name + "'," + id + ",'" + owner + "'," + worth + ");");
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * Adds an employee to the business
     * @param name Name
     * @param bId id of the business
     * @param jobs Jobs completed
     * @param scouted Jobs scouted
     * @param hasJob if the employee currently has a job
     * @param jobId the id of the current job
     */
    public static void addEmployee(String name, int bId, int jobs, int scouted, boolean hasJob, int jobId) {
        try {
            if(s == null){
                s = Data.c.createStatement();
            }
            s.executeUpdate("INSERT INTO 'employee'"+ MessageUtility.newLine
                    + "VALUES ('" + name + "'," + bId + "," + jobs + "," + scouted + "," + hasJob + "," + jobId + ");");
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * Adds a job to the database
     * @param issuer Name of the issuer
     * @param description The description of the job
     * @param pay the amount to be payed
     * @param x the location on the x axis
     * @param y the location on the y axis (up and down)
     * @param z the location on the z axis
     * @param jobId the id of the job
     * @param employeeName the current employee
     * @param bid the business id
     */
    public static void addJob(String issuer, String description, double pay, double x, double y, double z, int jobId, String employeeName, int bid) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("INSERT INTO 'job'" + MessageUtility.newLine
                    + "VALUES ('"+issuer+"','"+description+"',"+pay+","+x+","+y+","+z+","+jobId+",'"+employeeName+"',"+bid+");");
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

    /**
     * Deletes a business
     * @param name The name of the owner where the business should be deleted
     */
    public static void deleteBusiness(String name) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("DELETE FROM 'business'" + MessageUtility.newLine
                    + "WHERE BusinessOwner="+name+";");
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "Data is having an error", ex.getLocalizedMessage());
        }
    }

}
