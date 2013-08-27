package me.beastman3226.BusinessCore.business;

import me.beastman3226.BusinessCore.BusinessMain;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {

    public static Business createBusiness(String name, String owner) {
        Business b = null;
        int i = 0;
        while (i < Business.businessList.length) {
            if(Business.businessList[i] == null) {
                break;
            }
            i++;
        }
         Business.businessList[i] = new Business(i, name, owner);
         b = Business.businessList[i];
        return  b;
    }

    public static void deleteBusiness(String name, BusinessMain plugin) {
        for(Business b : Business.businessList) {
            if(b.getName().equals(name)) {
                Business.businessList[b.getIndex()] = null;
                plugin.getPluginLoader().disablePlugin(plugin);
                plugin.getPluginLoader().enablePlugin(plugin);
            }
        }
    }

    public static String[] listBusinesses() {
        String[] businessList = null;
        String bList = "";
        for(int i = 0; i < Business.businessList.length; i++) {
            if(Business.businessList[i] != null) {
                if(i == 0) {
                    bList = bList + Business.businessList[i].getName();
                 } else {
                    bList = bList + "," + Business.businessList[i].getName();
                 }
            }
        }
        businessList = bList.split(",");
        return businessList;
    }
}
