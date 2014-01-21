package me.beastman3226.bc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.commands.BusinessCommandHandler;
import me.beastman3226.bc.commands.JobCommandHandler;
import me.beastman3226.bc.commands.MiscCommandHandler;
import me.beastman3226.bc.db.Database;
import me.beastman3226.bc.db.Table;
import me.beastman3226.bc.job.Job;
import me.beastman3226.bc.job.JobManager;
import me.beastman3226.bc.listener.BusinessListener;
import me.beastman3226.bc.listener.JobListener;
import me.beastman3226.bc.listener.PlayerListener;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.Scheduler;
import me.beastman3226.bc.util.Time;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

/**
 *
 * @author beastman3226
 */
public class BusinessCore extends JavaPlugin {

    HashMap<String, String> hm = new HashMap<String, String>();

    @Override
    public void onEnable() {
        Information.BusinessCore = this;
        Information.config = getConfig();
        this.getLogger().log(Level.INFO, "{0}", setupEconomy());
        Information.initFiles(this);
        FileFunctions.load();
        registerListeners();
        registerCommands();
        Information.log = this.getLogger();
        if (getConfig().getBoolean("firstrun") || !getConfig().contains("database.enabled")) {
            saveDefaultConfig();
            this.reloadConfig();
            getConfig().set("firstrun", false);
            this.saveConfig();
        } else {
            if (getConfig().getBoolean("debug-message")) {
                Information.debug = true;
            } else {
                Information.debug = false;
            }
            if (getConfig().getBoolean("database.enabled")) {
                Database.instance();
                Information.database = true;
            } else {

                Information.database = false;
            }
            if (Information.database) {
                Connection c = (Database.instance().MySQL.checkConnection() ? Database.instance().MySQL.getConnection() : Database.instance().MySQL.openConnection());
                try {
                    Statement s = c.createStatement();
                    BusinessManager.createBusiness(s.executeQuery("SELECT * FROM " + Table.BUSINESS));
                } catch (SQLException ex) {
                    Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                BusinessManager.createBusinesses();
            }
            EmployeeManager.loadEmployees();
            JobManager.loadJobs();
        }
        try {
            Metrics metrics = new Metrics(this);

            Graph businessesCreated = metrics.createGraph("Number of Businesses Created");
            businessesCreated.addPlotter(new Metrics.Plotter("Businesses") {
                @Override
                public int getValue() {
                    return Business.businessList.size();
                }
            });

            Graph jobsCompleted = metrics.createGraph("Number of Jobs Completed");
            jobsCompleted.addPlotter(new Metrics.Plotter("Jobs") {
                @Override
                public int getValue() {
                    return Job.jobList.size();
                }
            });
            
            metrics.start();
        } catch (IOException e) {
            this.getLogger().severe("Failed to send stats :-(");
        }
        Scheduler.runPayPeriod();
        getLogger().info("Do /businesscore for information about this plugin");
    }

    @Override
    public void onDisable() {
        this.saveConfig();
        Information.BusinessCore = null;
        FileFunctions.save();
    }

    /**
     * A method to condense the clutter inside the onEnable method.
     */
    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BusinessListener(), this);
        getServer().getPluginManager().registerEvents(new JobListener(), this);
    }

    /**
     * Method to avoid clutter inside onEnable for commands.
     */
    public void registerCommands() {
        BusinessCommandHandler bch = new BusinessCommandHandler();
        JobCommandHandler jch = new JobCommandHandler();
        MiscCommandHandler mch = new MiscCommandHandler();
        getCommand("b.create").setExecutor(bch);
        getCommand("b.delete").setExecutor(bch);
        getCommand("b.withdraw").setExecutor(bch);
        getCommand("b.deposit").setExecutor(bch);
        getCommand("b.balance").setExecutor(bch);
        getCommand("b.info").setExecutor(bch);
        getCommand("b.top").setExecutor(bch);
        getCommand("hire").setExecutor(bch);
        getCommand("fire").setExecutor(bch);
        getCommand("j.open").setExecutor(jch);
        getCommand("j.claim").setExecutor(jch);
        getCommand("j.list").setExecutor(jch);
        getCommand("j.complete").setExecutor(jch);
        getCommand("j.me").setExecutor(jch);
        getCommand("j.id").setExecutor(jch);
        getCommand("businesscore").setExecutor(mch);
        getCommand("bc.help").setExecutor(mch);
    }

    public boolean setupEconomy() {
        Plugin p = this.getServer().getPluginManager().getPlugin("Vault");
        if (p == null) {
            return false;
        }
        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = null;
        rsp = (RegisteredServiceProvider<net.milkbowl.vault.economy.Economy>) this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {
            System.out.println("Economy plugin not detected");
            rsp = (RegisteredServiceProvider<net.milkbowl.vault.economy.Economy>) this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        }
        if (rsp == null) {
            return false;
        }
        Information.eco = rsp.getProvider();
        return Information.eco != null;

    }

    public static void log(Level level, String message) {
        if (Information.debug) {
            Information.log.log(level, message);
        }
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
        public static BusinessCore BusinessCore;
        public static Connection connection;
        public static net.milkbowl.vault.economy.Economy eco;
        public static boolean debug;
        public static Logger log;

        public static void initFiles(Plugin p) {
            businessFile = new File(p.getDataFolder(), "business.yml");
            jobFile = new File(p.getDataFolder(), "jobs.yml");
            employeeFile = new File(p.getDataFolder(), "employee.yml");

            if (!businessFile.exists() || !jobFile.exists() || !employeeFile.exists()) {
                businessFile.getParentFile().mkdirs();
                jobFile.getParentFile().mkdirs();
                employeeFile.getParentFile().mkdirs();
                try {
                    businessFile.createNewFile();
                    jobFile.createNewFile();
                    employeeFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            businessYml = YamlConfiguration.loadConfiguration(businessFile);
            employeeYml = YamlConfiguration.loadConfiguration(employeeFile);
            jobYml = YamlConfiguration.loadConfiguration(jobFile);
        }

        public static Time getTime() {
            String string = config.getString("payperiod");
            for (Time t : Time.values()) {
                if (string.contains(t.identifier + "")) {
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
                        Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
                    } catch (IOException ex) {
                        Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
                    } catch (InvalidConfigurationException ex) {
                        Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
                    }
                    break;
                case EMPLOYEE:
                    try {
                        Information.employeeYml.load(Information.employeeFile);
                    } catch (FileNotFoundException ex) {
                        Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
                    } catch (IOException ex) {
                        Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
                    } catch (InvalidConfigurationException ex) {
                        Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
                    }
                    break;
                case JOB:
                    try {
                        Information.jobYml.load(Information.jobFile);
                    } catch (FileNotFoundException ex) {
                        Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
                    } catch (IOException ex) {
                        Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
                    } catch (InvalidConfigurationException ex) {
                        Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
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
                Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
            } catch (IOException ex) {
                Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
            } catch (InvalidConfigurationException ex) {
                Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
            }
            try {
                Information.employeeYml.load(Information.employeeFile);
            } catch (FileNotFoundException ex) {
                Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
            } catch (IOException ex) {
                Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
            } catch (InvalidConfigurationException ex) {
                Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
            }
            try {
                Information.jobYml.load(Information.jobFile);
            } catch (FileNotFoundException ex) {
                Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
            } catch (IOException ex) {
                Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
            } catch (InvalidConfigurationException ex) {
                Information.BusinessCore.getLogger().severe(ex.getLocalizedMessage());
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
                        Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case EMPLOYEE:
                    try {
                        Information.employeeYml.save(Information.employeeFile);
                    } catch (IOException ex) {
                        Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case JOB:
                    try {
                        Information.jobYml.save(Information.jobFile);
                    } catch (IOException ex) {
                        Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, null, ex);
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