package me.beastman3226.bc.business;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;

import me.beastman3226.bc.BusinessCore;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {

    private static ArrayList<Business> businessList = new ArrayList<Business>();

    /**
     * Creates all businesses from file.
     */
    public static void createBusinesses() {
        BusinessCore.getInstance().getBusinessFileManager().reload();
        FileConfiguration businessYml = BusinessCore.getInstance().getBusinessFileManager().getFileConfiguration();
        int id;
        String name, owner;
        double balance;
        for (String s : businessYml.getKeys(false)) {
            id = businessYml.getInt(s + ".id");
            name = s;
            owner = businessYml.getString(s + ".ownerUUID");
            balance = businessYml.getDouble(s + ".balance");
            Business b = createBusiness(new Business.Builder(id).name(name).owner(owner).balance(balance));
            businessList.add(b);
        }
    }

    public static ArrayList<Business> getBusinessList() {
        return businessList;
    }

    /**
     * Base method for creating a new business
     * 
     * @param build
     * @return a new business
     */
    public static Business createBusiness(Business.Builder build) {
        Business b = build.build();
        businessList.add(b);
        /*
         * if(Information.debug) { Information.log.log(Level.INFO,
         * "Created a business with name {0}", build.getName()); }
         */
        return b;
    }

    /**
     * Gets a business based on id
     * 
     * @param id The id of the business
     * @return The business
     */
    public static Business getBusiness(int id) {
        Business b = null;
        for (Business business : businessList) {
            if (business.getID() == id) {
                b = business;
                break;
            }
        }
        return b;
    }

    /**
     * Finds a business based on the name of the current owner, 100% match is not
     * guaranteed (ownership change)
     * 
     * @param uuid UUID of the owner
     * @return The business
     */
    public static Business getBusiness(UUID uuid) {
        for (Business b : businessList)
            if (b.getOwner().getUniqueId().equals(uuid))
                return b;
        return null;
    }

    public static int getNewID(String name) {
        int id = 0;
        int pos = 1;
        for (char c : name.toCharArray()) {
            if (Character.isLetter(c)) {
                id += ((c - 'a' + 1) * (c - 'a' + 1)) * (pos++);
            } else if (Character.isDigit(c)) {
                continue;
            }
        }
        id /= pos;
        if (isID(id))
            return -1;
        return id;
    }

    /**
     * Deletes a business from storage and in memory
     * 
     * @param business The business to be deleted
     */
    public static void closeBusiness(Business business) {
        businessList.remove(business);
    }

    /**
     * Checks if the player is an owner via a null check using the getBusiness(name)
     * method
     * 
     * @param name The name of the player
     * @return True if name has a business, false if not.
     */
    public static boolean isOwner(String uuid) {
        return isOwner(UUID.fromString(uuid));
    }

    public static boolean isOwner(UUID uniqueId) {
		for(Business b : businessList) {
            if(b.getOwner().getUniqueId().equals(uniqueId))
                return true;
        }
        return false;
    }
    
    /**
     * Checks if the player is an owner via null check using getbusiness(id) method.
     * 
     * @param id The id in question
     * @return True if the id is attached to a business, false if not
     */
    public static boolean isID(int id) {
        return getBusiness(id) != null;
    }

    public static ArrayList<Business> sortById() {
        businessList.sort((Business b1, Business b2) -> b1.getID() < b2.getID() ? -1 : 1);
        return businessList;
    }

    public static ArrayList<Business> sortByBalance() {
        businessList.sort((Business b1, Business b2) -> b1.getBalance() < b2.getBalance() ? 1 : -1);
        return businessList;
    }

    public static ArrayList<Business> sortByName() {
        businessList.sort((Business b1, Business b2) -> b1.getName().compareTo(b2.getName()));
        return businessList;
    }
}
