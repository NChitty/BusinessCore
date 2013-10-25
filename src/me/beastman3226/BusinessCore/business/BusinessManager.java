package me.beastman3226.BusinessCore.business;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.data.*;
import me.beastman3226.BusinessCore.error.InvalidFormatException;
import me.beastman3226.BusinessCore.file.FileStore;
import me.beastman3226.BusinessCore.util.MessageUtility;
import org.bukkit.Bukkit;

/**A class for handling all our data
 * from the business object, really made for
 * handling the data upstream for storage
 * @author beastman3226
 */
public class BusinessManager {

    /**
     * Creates a business by finding the index
     * @param bName Name of the business
     * @param name The business owner
     */
    public static void createBusiness(String bName, String name) {
        Business newBusiness = new Business(Business.businessList.size(), bName, name);
        if(DataHandler.isDataStore()) {
            DataStore.addBusiness(newBusiness.getName(), newBusiness.getIndex(), newBusiness.getOwnerName(), newBusiness.getWorth());
        } else if (DataHandler.isFileStore()) {
            BusinessMain.flatfile.set(name, newBusiness);
            BusinessMain.flatfile.set("ownernames", ownerList());
        } else {
            BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
            Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
        }
    }

    /**
     * Returns a list of business
     * @return List of businesses
     */
    public static ArrayList<Business> listBusinesses() {
        return Business.businessList;
    }

    /**
     * Deletes a business within memory and downstream inside
     * our data handlers (file and db)
     * @param name
     * @param plugin
     */
    public static void deleteBusiness(String name, BusinessMain plugin) {
        Business.businessList.remove(Business.getBusiness(name));
        switch(DataHandler.storeType.toLowerCase()) {
            case "db": {
                DataUpdate.deleteBusiness(name);
                Business.businessList.remove(BusinessManager.getBusiness(name));
                break;
            }
            case "flatfile": {
                FileStore.update(name, null);
                Business.businessList.remove(BusinessManager.getBusiness(name));
                break;
            }
            default: {
                BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
            }
        }
    }

    /**
     * Gets a business from the database, incase you didn't want
     * to go with what is stored in memory.
     * @param name Name of the owner
     * @return The business where the owner's name is
     * match to that on recored
     */
    public static Business getBusiness(String name) {
        Business b = null;
        switch(DataHandler.storeType.toLowerCase()) {
            case "db": {
                ResultSet rs = Data.MySQL.querySQL(DataRetrieve.retrieveBusinesses);
            try {
                do {
                    if(rs.getString("BusinessOwner").equalsIgnoreCase(name)){
                        b = Business.getBusiness(rs.getInt("BusinessID"));
                        break;
                    } else {
                        continue;
                    }
                } while (rs.next());
            } catch (SQLException ex) {
                Logger.getLogger(BusinessManager.class.getName()).log(Level.SEVERE, null, ex);
            }

                break;
            }
            case "flatfile": {
                while(FileStore.getAll().hasNext()) {

                }
                break;
            }
            default: {
                BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
            }
        }
        if(b == null) {
            b = Business.getBusiness(name);
        }
        return b == null ? Business.getBusiness(name) : b;
    }

    /**
     * <p>
     * Deposits money to the business where the name is a match.
     * Handles the adding and handles it in the database/flatfile
     * </p>
     * @param name
     * @param d
     */
    public static void deposit(String name, double d) {
        switch(DataHandler.storeType.toLowerCase()) {
            case "db": {
                DataUpdate.setBusinessWorth(name, Business.getBusiness(name).getWorth() + d);
                Business.getBusiness(name).setWorth(Business.getBusiness(name).getWorth() + d);
                break;
            }
            case "flatfile": {
                FileStore.update(name, Business.getBusiness(name).toString());
                break;
            }
            default: {
                BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
            }
        }
    }

    /**
     * Withdraws money from the business
     * @param name The business owner
     * @param parseDouble The number to withdraw
     */
    public static void withdraw(String name, double parseDouble) {
        if(Business.getBusiness(name).removeFromWorth(parseDouble)) {
            switch(DataHandler.storeType.toLowerCase()) {
                case "db": {
                    DataUpdate.setBusinessWorth(name, BusinessManager.getBusiness(name).getWorth());
                    break;
                }
                case "flatfile": {
                    FileStore.update(name + ".worth", BusinessManager.getBusiness(name).getWorth());
                    break;
                }
                default: {
                    BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                    Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
                }
            }
        } else {
            Bukkit.getPlayer(name).sendMessage(MessageUtility.PREFIX_ERROR + "You do not have enough money!");
        }
    }

    /**
     * <p>Takes the information from the data or flatfile and
     * puts all the information into memory. </p>
     */
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
                            Logger.getLogger(BusinessManager.class.getSimpleName()).log(Level.INFO, "Successfully retrieved {0}", populate.getName());
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(BusinessManager.class.getSimpleName()).info(ex.getLocalizedMessage());
                    }
                }
                break;
            }
            case "flatfile": {
                // FIXME: File load
                break;
            }
            default: {
                BusinessMain.p.getLogger().info("BusinessManager has encountered a problem! You do not have a valid way of storing data!");
                Bukkit.getServer().getPluginManager().disablePlugin(BusinessMain.p);
            }
        }
    }

    public static Vector<String> getEmployeeList(String name) {
        return Business.getBusiness(name).getEmployeeList();

    }

    /**
     * Handles the withdrawal from the business
     * @param calculate Amount to withdraw
     * @param ownerName The name of the owner
     */
    public static void payOut(double calculate, String ownerName) {
        if(Business.getBusiness(ownerName).removeFromWorth(calculate)) {
            switch(DataHandler.storeType.toLowerCase()) {
                case "db": {
                    if(Data.MySQL.checkConnection()) {
                        DataUpdate.setBusinessWorth(ownerName, Business.getBusiness(ownerName).getWorth());
                    } else {
                        System.out.println("");
                    }
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
        } else {
            Bukkit.getPlayer(ownerName).sendMessage(MessageUtility.PREFIX_ERROR + "You do not have enough money!");
        }
    }

    /**
     *
     * @return A list of business owners
     */
    private static List<String> ownerList() {
        String[] owners = new String[Business.businessList.size()];
        int i = 0;
        for(Business b : Business.businessList) {
            owners[i] = b.getOwnerName();
            i++;
        }
        return Arrays.asList(owners);
    }

    /**
     *
     * @param get The business to add an employee to
     * @param name The employee name
     */
    public static void addEmployee(Business get, String name) {
        get.addEmployee(name);

    }

    public static Business fromString(String string) throws InvalidFormatException {
        Business b;
        String[] params = string.split(",");
        int index = 0;
        double worth = 0;
        String name = null;
        String ownerName = null;
        for(int i = 0; i < params.length; i++) {
            switch(i) {
                case 0: {
                    index = Integer.valueOf(params[i]);
                    break;
                }
                case 1: {
                    worth = Double.valueOf(params[i]);
                    break;
                }
                case 2: {
                    name = params[i];
                    break;
                }
                case 3: {
                    ownerName = params[i];
                }
            }
        }
        if(name == null | ownerName ==null) {
            throw new InvalidFormatException();
        } else {
            b = new Business(index, name, ownerName);
            b.setWorth(worth);
        }
        return b;
    }
}
