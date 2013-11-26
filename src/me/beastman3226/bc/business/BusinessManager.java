package me.beastman3226.bc.business;

import java.util.Iterator;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {

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
