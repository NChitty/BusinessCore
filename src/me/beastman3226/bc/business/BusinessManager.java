package me.beastman3226.bc.business;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import me.beastman3226.bc.Main;
import me.beastman3226.bc.Main.Information;
import me.beastman3226.bc.data.BusinessHandler;
import me.beastman3226.bc.data.Data;
import me.beastman3226.bc.data.file.BusinessFileManager;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.db.Table;
import me.beastman3226.bc.errors.NoOpenIDException;

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
        //TODO: business database
    }

    /**
     * Creates all businesses from file.
     */
    public static void createBusinesses() {
        //TODO: Businesses from file
    }

    /**
     * Base method for creating a new business
     * @param build
     * @return a new business
     */
    public static Business createBusiness(Business.Builder build) {
        names.add(build.getName());
        if(Information.debug) {
            Information.log.info("Created a business with name " + build.getName());
        }
        return build.build();
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
