package me.beastman3226.BusinessCore.data;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.util.MessageUtility;

/**
 *
 * @author beastman3226
 */
public class DataStore extends Data {

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

    public static void createTables() {
        createBusinessTable();
        createEmployeeTable();
        createJobTable();
    }

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
