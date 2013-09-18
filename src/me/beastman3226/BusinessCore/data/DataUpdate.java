package me.beastman3226.BusinessCore.data;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.util.MessageUtility;

/**
 *
 * @author beastman3226
 */
public class DataUpdate extends Data {

    public static void deleteBusiness(String owner) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("DELETE FROM " + bTable + MessageUtility.newLine
                    + "WHERE BusinessOwner=" + owner);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteJob(String issuer) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("DELETE FROM " + jTable + MessageUtility.newLine
                    + "WHERE JobIssuer=" + issuer);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateJob(String issuer, int bId) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("UPDATE " + jTable + MessageUtility.newLine
                    + "SET BusinessID="+bId+ MessageUtility.newLine
                    + "WHERE JobIssuer="+issuer);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setBusinessWorth(String owner, double worth) {
        try {
            if(s == null) {
                s =  Data.c.createStatement();
            }
            s.executeUpdate("UPDATE " + bTable + MessageUtility.newLine
                    + "SET BusinessWorth="+worth+MessageUtility.newLine
                    + "WHERE BusinessOwner="+owner);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateEmployee(String employeeName, int completedJobs) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("UPDATE " + bTable + MessageUtility.newLine
                    + "SET CompletedJobs="+completedJobs + MessageUtility.newLine
                    + "WHERE EmployeeName="+employeeName);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateEmployeeScouted(String employeeName, int scoutedJobs) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("UPDATE " + bTable + MessageUtility.newLine
                    + "SET Scouted="+scoutedJobs + MessageUtility.newLine
                    + "WHERE EmployeeName="+employeeName);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateEmployeeJob(String employeeName, int id) {
        try {
            if(s == null) {
                s = Data.c.createStatement();
            }
            s.executeUpdate("UPDATE " + bTable + MessageUtility.newLine
                    + "SET ActiveJobId="+id + MessageUtility.newLine
                    + "WHERE EmployeeName="+employeeName);
        } catch (SQLException ex) {
            Logger.getLogger(DataUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
