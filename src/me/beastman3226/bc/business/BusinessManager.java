package me.beastman3226.bc.business;

import java.sql.ResultSet;
import java.util.Iterator;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {

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
     * @return
     */
    public static Business createBusiness(Business.Builder build) {
        return build.build();
    }


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

}
