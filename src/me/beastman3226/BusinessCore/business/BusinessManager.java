package me.beastman3226.BusinessCore.business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.config.Configuration;
import me.beastman3226.BusinessCore.data.Data;
import me.beastman3226.BusinessCore.data.DataHandler;
import me.beastman3226.BusinessCore.data.DataRetrieve;
import me.beastman3226.BusinessCore.data.DataStore;
import me.beastman3226.BusinessCore.data.DataUpdate;
import me.beastman3226.BusinessCore.util.Email;
import me.beastman3226.BusinessCore.util.Email.Provider;
import org.bukkit.Bukkit;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {


    public static void createBusiness(String bName, String name) {
        Business newBusiness = new Business(Business.businessList.size(), bName, name);
        if(DataHandler.isDataStore()) {
            DataStore.addBusiness(newBusiness.getName(), newBusiness.getIndex(), newBusiness.getOwnerName(), newBusiness.getWorth());
        } else if (DataHandler.isFileStore()) {
            BusinessMain.flatfile.set(name, newBusiness.toString());
            BusinessMain.flatfile.set("ownernames", ownerList());
        } else {
            BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
            Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
        }
    }

    public static ArrayList<Business> listBusinesses() {
        return Business.businessList;
    }

    public static void deleteBusiness(String name, BusinessMain plugin) {
        switch(DataHandler.storeType.toLowerCase()) {
            case "db": {
                break;
            }
            case "flatfile": {
                break;
            }
            default: {
                BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
            }
        }
    }

    public static Business getBusiness(String name) {
        Business b = null;
        switch(DataHandler.storeType.toLowerCase()) {
            case "db": {
                break;
            }
            case "flatfile": {
                break;
            }
            default: {
                BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
            }
        }
        return b;
    }

    public static void deposit(String name, double d) {
        switch(DataHandler.storeType.toLowerCase()) {
            case "db": {
                break;
            }
            case "flatfile": {
                break;
            }
            default: {
                BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
            }
        }
    }

    public static void withdraw(String name, double parseDouble) {
        switch(DataHandler.storeType.toLowerCase()) {
            case "db": {
                break;
            }
            case "flatfile": {
                break;
            }
            default: {
                BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
            }
        }
    }

    public static void populateBusiness() {
        switch(DataHandler.storeType.toLowerCase()) {
            case "db": {
                ResultSet rs = null;
                try {
                    rs = Data.c.createStatement().executeQuery(DataRetrieve.retrieveBusinesses);
                } catch (SQLException ex) {
                    Logger.getLogger(BusinessManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(rs != null) {
                    try {
                        while(rs.next()) {
                            Business populate = new Business(rs.getInt("BusinessID"), rs.getString("BusinessName"), rs.getString("BusinessOwner"));
                            populate.setWorth(rs.getDouble("BusinessWorth"));
                            Logger.getLogger(BusinessManager.class.getSimpleName()).info("Successfully retrieved " + populate.getName());
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(BusinessManager.class.getSimpleName()).info(ex.getLocalizedMessage());
                        BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "BusinessManager threw an error", ex.getLocalizedMessage());
                    }
                }
                break;
            }
            case "flatfile": {
                Business.createBusiness(BusinessMain.flatfile.getStringList("ownernames"));
                break;
            }
            default: {
                BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
            }
        }
    }

    public static Vector<String> getEmployeeList(String name) {
        return null;

    }

    public static void payOut(double calculate) {

    }

    private static List<String> ownerList() {
        String[] owners = new String[Business.businessList.size()];
        int i = 0;
        for(Business b : Business.businessList) {
            owners[i] = b.getOwnerName();
            i++;
        }
        return Arrays.asList(owners);
    }

    public static void addEmployee(Business get, String name) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
