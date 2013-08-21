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
                b = new Business(i, name, owner);
                Business.businessList[i] = b;
                break;
            }
            i++;
        }
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
}
