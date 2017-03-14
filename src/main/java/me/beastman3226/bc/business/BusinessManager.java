package me.beastman3226.bc.business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Config;
import me.beastman3226.bc.BusinessCore.FileFunctions;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.data.BusinessHandler;
import me.beastman3226.bc.data.file.BusinessFileManager;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.util.Prefixes;
import me.beastman3226.bc.util.Sorter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {

    /**
     * This creates a business from a pre-existing business in the database.
     */
    public static void createBusiness(ResultSet rs) {
        try {
            while(rs.next()) {
                Business b = new Business.Builder(rs.getInt("BusinessID"))
                        .balance(rs.getDouble("BusinessBalance"))
                        .name(rs.getString("BusinessName"))
                        .owner(rs.getString("BusinessOwner"))
                        .ids(rs.getString("EmployeeIDs").split(","))
                        .salary(rs.getBoolean("Salary"))
                        .pay(rs.getDouble("Payment")).build();
                Business.businessList.add(b);
                BusinessCore.log(Level.INFO, "Created business " + b.getName() + " from database.");
            }
        } catch (SQLException ex) {
            BusinessCore.log(Level.SEVERE, ex.getMessage());
            Information.log.severe(ex.getLocalizedMessage());
        }
    }

    /**
     * Creates all businesses from file.
     */
    public static void createBusinesses() {
        FileFunctions.reload(Config.BUSINESS);
        FileConfiguration yml = Information.businessYml;
        for(String s : Information.businessYml.getKeys(false)) {
            if(yml.getList(s + ".employeeIDs") != null) {
                List<String> list = yml.getStringList(s + ".employeeIDs");
                Business b = createBusiness(new Business.Builder(yml.getInt(s + ".id"))
                        .name(s)
                        .owner(yml.getString(s + ".ownerName"))
                        .balance(yml.getDouble(s + ".balance"))
                        .ids(list.toArray(new String[]{}))
                        .salary(yml.getBoolean(s + ".salary"))
                        .pay(yml.getDouble(s + ".pay")));
               BusinessCore.log(Level.INFO, "Loaded business " + b.getName() + " from file");
            } else {
                Business b = createBusiness(new Business.Builder(yml.getInt(s + ".id"))
                        .name(s)
                        .owner(yml.getString(s + ".ownerName"))
                        .balance(yml.getDouble(s + ".balance")));
                if(Information.debug) {
                    Information.BusinessCore.getLogger().log(Level.INFO, "Loaded business {0} with owner as {1}!", new Object[]{b.getName(), b.getOwnerName()});
                }
            }
          }
    }

    /**
     * Base method for creating a new business
     * @param build
     * @return a new business
     */
    public static Business createBusiness(Business.Builder build) {
        Business b = build.build();
        Business.businessList.add(b);
        if(Information.debug) {
            Information.log.log(Level.INFO, "Created a business with name {0}", build.getName());
        }
        return b;
    }


    /**
     * Gets a business based on id
     * @param id The id of the business
     * @return The business
     */
    public static Business getBusiness(int id) {
        Business b = null;
        Business[] array = Business.businessList.toArray(new Business[]{});
        for (Business business : array) {
            if(business.getID() == id) {
                b = business;
                break;
            }
        }
        return b;
    }

    /**
     * Finds a business based on the name of the current owner,
     * 100% match is not guaranteed (ownership change)
     * @param name Name of the owner
     * @return The business
     */
    public static Business getBusiness(String name) {
        for(Business b : Business.businessList) {
            if(b.getOwnerName().equalsIgnoreCase(name)) {
                return b;
            } else {
                continue;
            }
        }
        return null;
    }
    
    public static int getID(String bname) {
        int id = -1;
        for(Business b : Business.businessList) {
            if(b.getName().equalsIgnoreCase(bname)) {
                id = b.getID();
                break;
            }
        }
        return id;
    }
    
    /**
     * Finds an open business ID for a newly created business
     * @return The open id
     */
    public static int openID() {
        int id = 1000;
        Random r = new Random();
        id = (r.nextInt(1000) + 1000);
        for(Business b : Business.businessList) {
            if(b.getID() == id) {
                return openID();
            }
        }
        return id;
    }

    /**
     * Deletes a business from storage and in memory
     * @param business The business to be deleted
     */
    public static void deleteBusiness(Business business) {
        Business.businessList.remove(business);
        if(Information.database) {
            BusinessHandler.remove("BusinessID", business.getID());
        } else {
            BusinessFileManager.editConfig(new FileData().add(business.getName(), null));
        }
        BusinessCore.log(Level.WARNING, business.getOwnerName() + " has just deleted business " + business.getName());
        Bukkit.getPlayer(business.getOwnerName()).sendMessage(Prefixes.ERROR + "Your business has been deleted");
    }

    /**
     * Checks if the player is an owner via a null check
     * using the getBusiness(name) method
     * @param name The name of the player
     * @return True if name has a business, false if not.
     */
    public static boolean isOwner(String name) {
        for(Business b : Business.businessList) {
            if(b.getOwnerName().equalsIgnoreCase(name)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    /**
     * Checks if the player is an owner via
     * null check using getbusiness(id) method.
     * @param id The id in question
     * @return True if the id is attached to a business, false if not
     */
    public static boolean isID(int id) {
        return getBusiness(id) != null;
    }

    /**
     * Sorts the list and gets the rank specified
     * @param rank The rank
     * @return The name of the business at said rank
     */
    public static String getIndex(int rank) {
        Business b = null;
        try {
            b = Sorter.sort().get(--rank);
        } catch (Exception e) {
            return "You could be here.";
        }
        return b.getName() + ChatColor.GREEN + " ID: " + ChatColor.WHITE + b.getID();
    }

}
