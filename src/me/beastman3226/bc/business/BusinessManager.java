package me.beastman3226.bc.business;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Random;
import me.beastman3226.bc.errors.NoOpenIDException;

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

    public static int openID() throws NoOpenIDException {
        int id = 1000;
        Random r = new Random();
        id = (r.nextInt(1000) + 1000);
        for(Business b : Business.businessList) {
            if(b.getID() == id) {
                id = (r.nextInt(5000) + 1000);
                for(Business b1 : Business.businessList) {
                    if(b1.getID() == id) {
                        id = (r.nextInt(10000) + 1000);
                        for(Business b2 : Business.businessList) {
                            if(b1.getID() == id) {
                                throw new NoOpenIDException(id);
                            }
                        }
                    }
                }
            }
        }
        return id;
    }

    public static void deleteBusiness(Business business) {
        Business.businessList.remove(business);
    }

}
