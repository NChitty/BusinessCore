package me.beastman3226.bc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.db.Database;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author beastman3226
 */
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Information.BusinessCore = this;
        reloadConfig();
        Information.config = getConfig();
        if(getConfig().getBoolean("database.enabled")) {
            Database.instance();
        } else {
            Information.initFiles();
        }
        registerListeners();
        registerCommands();
        registerEvents();
    }

    /**
     * A method to condense the clutter inside the onEnable method.
     */
    public void registerListeners() {
        //TODO: No listeners registered
    }

    /**
     * Method to avoid clutter inside onEnable for commands.
     */
    public void registerCommands() {
        //TODO: No commands registered
    }

    private void registerEvents() {
        //TODO: Add events
    }

    /**
     * This class turns normally protected, private or other information that isn't
     * in scope and puts it out there for everyone to use; information stored in the
     * actual main class will ever be in scope.
     */
    public static class Information {
        private static File businessFile, jobFile, employeeFile;
        public static FileConfiguration config;
        public static FileConfiguration businessYml, employeeYml, jobYml;

        public static Plugin BusinessCore;

        public static Connection connection;

        public static void initFiles() {
            businessFile = new File("business.yml");
            jobFile = new File("jobs.yml");
            employeeFile = new File("employee.yml");

            businessYml = YamlConfiguration.loadConfiguration(businessFile);
            employeeYml = YamlConfiguration.loadConfiguration(employeeFile);
            jobYml = YamlConfiguration.loadConfiguration(jobFile);
        }
    }

    /**
     * Class for file functions (loading, reloading and saving)
     *
     * @author beastman3226
     */
    private static class FileFunctions {

        /**
         * Reloads the specified config, simply dumps all information
         * that is in memory and replaces it with all information from
         * file.
         *
         * @param config the config to be reloaded
         */
        public static void reload(Config config) {
            switch(config) {
                case BUSINESS:
                try {
                    Information.businessYml.load(Information.businessFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException | InvalidConfigurationException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                case EMPLOYEE:
                try {
                    Information.employeeYml.load(Information.employeeFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException | InvalidConfigurationException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                case JOB:
                try {
                    Information.jobYml.load(Information.jobFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException | InvalidConfigurationException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }

        /**This is a method for loading all the files at startup
         *
         */
        public static void load() {
            try {
                    Information.businessYml.load(Information.businessFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException | InvalidConfigurationException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            try {
                    Information.employeeYml.load(Information.employeeFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException | InvalidConfigurationException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            try {
                    Information.jobYml.load(Information.jobFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException | InvalidConfigurationException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * Dumps all the information asscociated with the config
         * into the actual file
         * @param config Config to dump to
         */
        public static void save(Config config) {
            switch(config) {
                case BUSINESS:
                try {
                    Information.businessYml.save(Information.businessFile);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                case EMPLOYEE:
                    try {
                        Information.employeeYml.save(Information.employeeFile);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case JOB:
                    try {
                        Information.jobYml.save(Information.jobFile);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            }
        }

        /**Saves all information in all the configs
         *
         */
        public static void save() {
            try {
                    Information.businessYml.save(Information.businessFile);
                    Information.employeeYml.save(Information.employeeFile);
                    Information.jobYml.save(Information.jobFile);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }

    /**
     * Simple class for finding which config is being referred to.
     */
    public static enum Config {
        BUSINESS, EMPLOYEE, JOB;
    }
}
