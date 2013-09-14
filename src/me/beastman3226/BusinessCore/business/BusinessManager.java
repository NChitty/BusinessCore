package me.beastman3226.BusinessCore.business;

import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.util.DataStore;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {

    public static Business createBusiness(String name, String owner) {
        Business b = null;
        Business finalB = Business.businessList.get(Business.businessList.size());
        int index = finalB.getIndex() + 1;
        b = new Business(index, name, owner);
        DataStore.addBusiness(name, index, owner, 0.0);
        return  b;
    }

    public static void deleteBusiness(String name, BusinessMain plugin) {
        Business.businessList.remove(Business.getBusiness(name));
        DataStore.deleteBusiness(name);
    }

    public static String[] listBusinesses() {
        String[] businessList = null;
        String bList = "";
        for(int i = 0; i < Business.businessList.size(); i++) {
            if(Business.businessList != null) {
                if(i == 0) {
                    bList = bList + Business.businessList.get(i).getName();
                 } else {
                    bList = bList + "," + Business.businessList.get(i).getName();
                 }
            }
        }
        businessList = bList.split(",");
        return businessList;
    }
}
