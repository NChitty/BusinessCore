/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.beastman3226.BusinessCore.file;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;

/**
 *
 * @author beastman3226
 */
public class FileStore {

    /**
     * Updates information in the business file.
     * @param path the path to be updated
     * @param updateWith the path to be updated
     */
    public static void update(String path, String updateWith) {
        if(updateWith == null) deleteBusiness(path);
        if(BusinessMain.flatfile.contains(path)) {
            String business = BusinessMain.flatfile.getString(path);
            BusinessMain.logger.log(Level.INFO, "Found {0}...", path);
            BusinessMain.logger.info(("Updating..."));
            BusinessMain.flatfile.set(path, updateWith);
            BusinessMain.logger.log(Level.INFO, "Updated {0} with {1} from {2}", new Object[]{path, updateWith, business});
        } else {
            BusinessMain.logger.log(Level.INFO, "Could not find path: {0}", path);
            BusinessMain.logger.info(("Creating path..."));
            BusinessMain.flatfile.set(path, updateWith);
            BusinessMain.logger.log(Level.INFO, "File reads: {0}: ''{1}''", new Object[]{path, updateWith});
        }
    }

    /**
     * Saves all the data to the disk
     */
    public static void save() {

    }

    /**
     * Loads all the data from the disk
     */
    public static void load() {

    }

    private static void deleteBusiness(String path) {
        BusinessMain.flatfile.set(path, null);
    }

}
