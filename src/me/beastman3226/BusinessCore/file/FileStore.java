/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.beastman3226.BusinessCore.file;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.business.Business;
import me.beastman3226.BusinessCore.business.BusinessManager;
import me.beastman3226.BusinessCore.job.Job;
import me.beastman3226.BusinessCore.player.Employee;
import me.beastman3226.BusinessCore.util.MessageUtility;

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
    public static void update(String path, Object updateWith) {
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
        for(Business b : Business.businessList) {
            BusinessMain.flatfile.set(b.getOwnerName() + ".business", b.getName());
            BusinessMain.flatfile.set(b.getOwnerName() + ".employees", b.getEmployeeList().toArray(new String[]{}));
            BusinessMain.flatfile.set(b.getOwnerName() + ".id", b.getIndex());
            BusinessMain.flatfile.set(b.getOwnerName() + ".jobs",  b.getJobList().toArray(new String[]{}));
            BusinessMain.flatfile.set(b.getOwnerName() + ".ownerName", b.getOwnerName());
            BusinessMain.flatfile.set(b.getOwnerName() + ".worth", b.getWorth());
        }
        for(Employee e : Employee.employeeList) {
            // TODO: Employee save (file)
        }
        for(Job j : Job.jobList) {
            // TODO: Job save (file)
        }
    }

    /**
     * Loads all the data from the disk
     */
    public static void load() {
        // TODO: Switch from toString to path keys
    }

    private static void deleteBusiness(String path) {
        BusinessMain.flatfile.set(path, null);
    }

    public static Iterator<Business> getAll() {
        Set<String> keys = BusinessMain.flatfile.getKeys();
        Map<String, Object> values = BusinessMain.flatfile.getConfig().getValues(true);
        Set<Business> businesses = new HashSet<>();
        for(Object value : values.values()) {
            businesses.add((Business) value);
        }
        return businesses.iterator();
    }

}
