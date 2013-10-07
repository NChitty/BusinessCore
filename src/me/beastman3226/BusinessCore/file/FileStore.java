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
 * @author Nicholas
 */
public class FileStore {

    public static void update(String path, String updateWith) {
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

    public static void save() {
        
    }

    public static void load() {

    }

}
