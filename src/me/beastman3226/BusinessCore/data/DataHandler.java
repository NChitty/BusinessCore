package me.beastman3226.BusinessCore.data;

import me.beastman3226.BusinessCore.BusinessMain;

/**
 *
 * @author beastman3226
 */
public abstract class DataHandler {

    public static String storeType = "DB";

    public static boolean isFileStore() {
        if(!BusinessMain.config.getBoolean("db.enabled")) {
            storeType = "FlatFile";
            return true;
        } else if (BusinessMain.config.getBoolean("db.enabled") && !Data.MySQL.checkConnection()) {
            return true;
        }
        return !(isDataStore());

    }

    public static boolean isDataStore() {
        return BusinessMain.config.getBoolean("db.enabled") && Data.MySQL.checkConnection();
    }

}
