package me.beastman3226.bc.player;

import java.util.ArrayList;
import java.util.Set;
import me.beastman3226.bc.BusinessCore.Config;
import me.beastman3226.bc.BusinessCore.FileFunctions;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;

/**
 *
 * @author beastman3226
 */
public class Manager {
    
    public static void addManager(String name, String business) {
            ArrayList<String> managers = new ArrayList<String>(Information.managerYml.getStringList(business));
            managers.add(name);
            Information.managerYml.set(business, managers);
            FileFunctions.save(Config.MANAGER);
    }
    
    public static boolean isManager(String name, String business) {
            ArrayList<String> managers = new ArrayList<String>(Information.managerYml.getStringList(business));
            return managers.contains(name);
    }

    public static ArrayList<String> getManagers(String business) {
        return new ArrayList<String>(Information.managerYml.getStringList(business));
    }
    
    public static void removeManager(String name, String business) {
        ArrayList<String> managers = new ArrayList<String>(Information.managerYml.getStringList(business));
        managers.remove(name);
        Information.managerYml.set(business, managers);
        FileFunctions.save(Config.MANAGER);
    }

    public static boolean isManager(String name) {
        Set<String> names = Information.managerYml.getKeys(false);
        for(String bname : names) {
            ArrayList<String> managers = getManagers(bname);
            for(String manager : managers) {
                if(manager.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Business getBusiness(String name) {
        Set<String> names = Information.managerYml.getKeys(false);
        for(String bname : names) {
            ArrayList<String> managers = getManagers(bname);
            for(String manager : managers) {
                if(manager.equalsIgnoreCase(name)) {
                    return BusinessManager.getBusiness(BusinessManager.getID(bname));
                }
            }
        }
        return null;
    }
}
