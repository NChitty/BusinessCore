package me.beastman3226.bc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.commands.BusinessCommandHandler;
import me.beastman3226.bc.db.Database;
import me.beastman3226.bc.db.Table;
import me.beastman3226.bc.listener.BusinessListener;
import me.beastman3226.bc.listener.PlayerListener;
import me.beastman3226.bc.util.Time;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
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
        if(getConfig().getBoolean("debug-messages")) {
            Information.debug = true;
        } else {
            Information.debug = false;
        }
        if (getConfig().getBoolean("database.enabled")) {
            Database.instance();
            Information.database = true;
        } else {
            Information.initFiles(this);
            Information.database = false;
        }
        setupEconomy();
        registerListeners();
        registerCommands();
        Information.log = this.getLogger();
        if(getConfig().getBoolean("firstrun")) {
            getConfig().set("firstrun", false);
        } else {
            if(Information.database) {
                Connection c= Database.MySQL.getConnection();
                try {
                    Statement s = c.createStatement();
                    BusinessManager.createBusiness(s.executeQuery("SELECT * FROM " + Table.BUSINESS));
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                BusinessManager.createBusinesses();
            }
        }
    }


    @Override
    public void onDisable() {
        Information.BusinessCore.saveConfig();
        Information.BusinessCore = null;
        FileFunctions.save();
    }
    /**
     * A method to condense the clutter inside the onEnable method.
     */
    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BusinessListener(), this);
    }

    /**
     * Method to avoid clutter inside onEnable for commands.
     */
    public void registerCommands() {
        BusinessCommandHandler bch = new BusinessCommandHandler();
        getCommand("b.create").setExecutor(bch);
        getCommand("b.delete").setExecutor(bch);
        getCommand("b.withdraw").setExecutor(bch);
        getCommand("b.deposit").setExecutor(bch);
        getCommand("b.balance").setExecutor(bch);
        getCommand("hire").setExecutor(bch);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        Information.eco = rsp.getProvider();
        return Information.eco != null;
    }

    /**
     * This class turns normally protected, private or other information that
     * isn't in scope and puts it out there for everyone to use; information
     * stored in the actual main class will ever be in scope.
     */
    public static class Information {

        private static File businessFile, jobFile, employeeFile;
        public static FileConfiguration config;
        public static FileConfiguration businessYml, employeeYml, jobYml;
        public static boolean database;
        public static Plugin BusinessCore;
        public static Connection connection;
        public static Economy eco;
        public static boolean debug;
        public static Logger log;

        public static void initFiles(Plugin p) {
            businessFile = new File(p.getDataFolder(), "business.yml");
            jobFile = new File(p.getDataFolder(), "jobs.yml");
            employeeFile = new File(p.getDataFolder(), "employee.yml");

            if(!businessFile.exists() || !jobFile.exists() || !employeeFile.exists()) {
                businessFile.getParentFile().mkdirs();
                jobFile.getParentFile().mkdirs();
                employeeFile.getParentFile().mkdirs();
                try {
                    businessFile.createNewFile();
                    jobFile.createNewFile();
                    employeeFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            businessYml = YamlConfiguration.loadConfiguration(businessFile);
            employeeYml = YamlConfiguration.loadConfiguration(employeeFile);
            jobYml = YamlConfiguration.loadConfiguration(jobFile);
        }

        public static Time getTime() {
            String string = config.getString("payperiod");
            for(Time t : Time.values()) {
                if(string.contains(t.identifier + "")) {
                    return t;
                }
            }
            return null;
        }

        public static int getValue() {
            String string = config.getString("payperiod");
            string = string.replace(getTime().identifier, ' ');
            return Integer.valueOf(string.trim());
        }
    }

    /**
     * Class for file functions (loading, reloading and saving)
     *
     * @author beastman3226
     */
    public static class FileFunctions {

        /**
         * Reloads the specified config, simply dumps all information that is in
         * memory and replaces it with all information from file.
         *
         * @param config the config to be reloaded
         */
        public static void reload(Config config) {
            switch (config) {
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

        /**
         * This is a method for loading all the files at startup
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
         * Dumps all the information asscociated with the config into the actual
         * file
         *
         * @param config Config to dump to
         */
        public static void save(Config config) {
            switch (config) {
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

        /**
         * Saves all information in all the configs
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
