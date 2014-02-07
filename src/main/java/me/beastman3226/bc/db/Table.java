/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.beastman3226.bc.db;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore.Information;

/**
 *
 * @author beastman3226
 */
public enum Table {
    BUSINESS("business", new String[]{"BusinessID INTEGER",
        "BusinessName VARCHAR(255)",
        "BusinessOwner VARCHAR(40)",
        "BusinessBalance FLOAT",
        "EmployeeIDs TEXT"}),
    EMPLOYEE("employees", new String[]{"EmployeeID INTEGER",
        "EmployeeName VARCHAR(40)",
        "BusinessID INTEGER",
        "CompletedJobs INTEGER",
        "JobID INTEGER"}),
    JOB("jobs", new String[]{"JobID INTEGER",
        "PlayerName VARCHAR(65)",
        "JobDescription TEXT",
        "JobLocation VARCHAR(75)",
        "World VARCHAR(40)",
        "JobPayment FLOAT",
        "EmployeeID INTEGER"});

    private String name;
    Table(String name, String[] columns) {
        this.name = name;
        if(!Database.instance().MySQL.checkConnection()) {
            Information.connection = Database.instance().MySQL.openConnection();
        }
        Statement s = null;
        try {
           s = Information.connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            s.execute("IF OBJECT_ID('" + Database.instance().getDatabase() +"." + name + "') IS NULL" + "\n" + "CREATE TABLE " + name + "(" + "\n" + concat(columns) + "\n" + ");");
        } catch (SQLException ex) {
            Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String concat(String[] strings) {
        StringBuilder sb = new StringBuilder();
        for(String string : strings) {
            sb.append(string).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return name;
    }
}
