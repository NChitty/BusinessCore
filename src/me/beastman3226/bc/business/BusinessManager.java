package me.beastman3226.bc.business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.data.BusinessHandler;
import me.beastman3226.bc.data.Data;
import me.beastman3226.bc.data.file.BusinessFileManager;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.db.Table;
import me.beastman3226.bc.errors.NoOpenIDException;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {

    public static ArrayList<String> names = new ArrayList<>();

    /**
     * This creates a business from a pre-existing business in the database.
     */
    public static void createBusiness(ResultSet rs) {
        try {
            while(rs.next()) {
                createBusiness(new Business.Builder(rs.getInt("BusinessID"))
                        .balance(rs.getDouble("BusinessBalance"))
                        .name(rs.getString("BusinessName"))
                        .owner(rs.getString("BusinessOwner"))
                        .ids(rs.getString("EmployeeIDs").split(",")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BusinessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates all businesses from file.
     */
    public static void createBusinesses() {
        ArrayList<String> names = new ArrayList<>(Information.businessYml.getStringList("names"));
        for(String s : names) {
            FileConfiguration yml = Information.businessYml;
            createBusiness(new Business.Builder(yml.getInt(s + ".id")).name(s).owner(yml.getString(s + ".ownerName")).balance(yml.getDouble(s + ".balance")).ids(yml.getString(s + ".employeeIDs").split(",")));
        }
    }

    /**
     * Base method for creating a new business
     * @param build
     * @return a new business
     */
    public static Business createBusiness(Business.Builder build) {
        Business b = build.build();
        names.add(b.getName());
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
        Business business = null;
        for(Business b : Business.businessList) {
            if(b.getOwnerName().equalsIgnoreCase(name)) {
                business = b;
                break;
            }
        }
        return business;
    }

    /**
     * Finds an open business ID for a newly created business
     * @return The open id
     * @throws NoOpenIDException
     */
    public static int openID() throws NoOpenIDException {
        int id = 1000;
        Random r = new Random();
        id = (r.nextInt(1000) + 1000);
        for(Business b : Business.businessList) {
            if(b.getID() == id) {
                id = (r.nextInt(5000) + 5000);
                for(Business b1 : Business.businessList) {
                    if(b1.getID() == id) {
                        id = (r.nextInt(10000) + 10000);
                        for(Business b2 : Business.businessList) {
                            if(b2.getID() == id) {
                                throw new NoOpenIDException(id);
                            }
                        }
                    }
                }
            }
        }
        return id;
    }

    /**
     * Deletes a business from storage and in memory
     * @param business The business to be deleted
     */
    public static void deleteBusiness(Business business) {
        if(Information.database) {
            BusinessHandler.remove("BusinessID", business.getID());
        } else {
            BusinessFileManager.editConfig(new FileData().add(business.getName(), null));
        }
        Business.businessList.remove(business);
    }

    /**
     * Checks if the player is an owner via a null check
     * using the getBusiness(name) method
     * @param name The name of the player
     * @return True if name has a business, false if not.
     */
    public static boolean isOwner(String name) {
        return getBusiness(name) != null;
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
}
