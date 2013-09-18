package me.beastman3226.BusinessCore.data;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.util.MessageUtility;

/**
 *
 * @author beastman3226
 */
public class DataStore extends Data {



    private static void createBusinessTable(String tableName) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.execute("CREATE TABLE " + tableName + "\r\n" +
                    "(" + "\r\n"
                    + "BusinessName VARCHAR(255)," + MessageUtility.newLine
                    + "BusinessID INTEGER," + MessageUtility.newLine
                    + "BusinessOwner VARCHAR(255)," + MessageUtility.newLine
                    + "BusinessWorth FLOAT);");
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
        }
        bTable = tableName;
    }

    private static void createEmployeeTable (String tableName) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.execute("CREATE TABLE " + tableName + "\r\n" +
                    "(" + "\r\n"
                    + "EmployeeName VARCHAR(255)," + MessageUtility.newLine
                    + "BusinessID INTEGER," + MessageUtility.newLine
                    + "CompletedJobs INTEGER," + MessageUtility.newLine
                    + "ScoutedJobs INTEGER," + MessageUtility.newLine
                    + "HasJob BOOLEAN," +  MessageUtility.newLine
                    + "ActiveJobId INTEGER"+ "\r\n" +
                    ");");
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        eTable = tableName;
    }

    private static void createJobTable (String tableName) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.execute("CREATE TABLE " + tableName + "\r\n" +
                    "(" + MessageUtility.newLine
                    + "JobIssuer VARCHAR(255)," + MessageUtility.newLine
                    + "JobDescription Memo," + MessageUtility.newLine
                    + "JobPayment FLOAT," + MessageUtility.newLine
                    + "JobLocationX FLOAT," + MessageUtility.newLine
                    + "JobLocationY FLOAT," + MessageUtility.newLine
                    + "JobLocationZ FLOAT," + MessageUtility.newLine
                    + "JobID INTEGER" + MessageUtility.newLine
                    + "Employee VARCHAR(255)," + MessageUtility.newLine
                    + "BusinessID INTEGER" + MessageUtility.newLine
                    + ");");
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable = tableName;
    }

    public static void createTables(String bTable, String eTable, String jTable) {
        createBusinessTable(bTable);
        createEmployeeTable(eTable);
        createJobTable(jTable);
    }

    public static void addBusiness(String name, int id, String owner, double worth) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("INSERT INTO " + bTable + MessageUtility.newLine
                    + "VALUES ('" + name + "'," + id + ",'" + owner + "'," + worth + ");");
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addEmployee(String name, int bId, int jobs, int scouted, boolean hasJob, int jobId) {
        try {
            if(s == null){
                s = Data.c.createStatement();
            }
            s.executeUpdate("INSERT INTO " + eTable + MessageUtility.newLine
                    + "VALUES ('" + name + "'," + bId + "," + jobs + "," + scouted + "," + hasJob + "," + jobId + ");");
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addJob(String issuer, String description, double pay, double x, double y, double z, int jobId, String employeeName, int bid) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("INSERT INTO " + jTable + MessageUtility.newLine
                    + "VALUES ('"+issuer+"','"+description+"',"+pay+","+x+","+y+","+z+","+jobId+",'"+employeeName+"',"+bid+");");
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteBusiness(String name) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("DELETE FROM " + bTable + MessageUtility.newLine
                    + "WHERE BusinessOwner="+name);
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
