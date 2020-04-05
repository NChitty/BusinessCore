package me.beastman3226.bc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.commands.BusinessCommandHandler;
import me.beastman3226.bc.commands.JobCommandHandler;
import me.beastman3226.bc.commands.MiscCommandHandler;
import me.beastman3226.bc.job.Job;
import me.beastman3226.bc.job.JobManager;
import me.beastman3226.bc.listener.BusinessListener;
import me.beastman3226.bc.listener.JobListener;
import me.beastman3226.bc.listener.PlayerListener;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.Scheduler;
import me.beastman3226.bc.util.Time;
import net.milkbowl.vault.chat.Chat;
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
public class BusinessCore extends JavaPlugin {

    private static BusinessCore instance;
    
    HashMap<String, String> hm = new HashMap<String, String>();


    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().log(Level.INFO, "Loaded Economy: {0}", setupEconomy());
        this.getLogger().log(Level.INFO, "Loaded Chat: {0}", setupChat());
        Information.initFiles(this);
        FileFunctions.load();
        registerListeners();
        registerCommands();
        Information.log = this.getLogger();
        if (getConfig().getBoolean("firstrun")) {
            saveDefaultConfig();
            this.reloadConfig();
            getConfig().set("firstrun", false);
            this.saveConfig();
        } else {
            Information.debug = getConfig().getBoolean("debug-message");
            if (getConfig().getBoolean("managers")) {
                Information.managers = true;
                initManagers(this);
            } else {
                Information.managers = false;
            }
            Information.prefix = getConfig().getBoolean("prefixes.enabled");
            for (String path : Information.businessYml.getKeys(true)) {
                System.out.print(path);
                if (path.contains(".ownerName")) {
                    System.out.print("Matched");
                    try {
                        String name = Information.businessYml.getString(path);
                        System.out.println(name + " got the name");
                        UUID owner = UUIDFetcher.getUUIDOf(name);
                        System.out.println("Got UUID of owner " + owner);
                        Information.businessYml.set(path.replace("ownerName", "ownerUUID"), "" + owner.toString());
                        Information.businessYml.set(path, null);
                        Information.businessYml.save(Information.businessFile);
                    } catch (Exception ex) {
                        Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    }
                }
            }
            for (String path : Information.jobYml.getKeys(true)) {
                if (path.contains(".player")) {
                    try {
                        String name = Information.jobYml.getString(path);
                        UUID employer = UUIDFetcher.getUUIDOf(name);
                        Information.jobYml.set(path.replace("player", "UUID"), "" + employer.toString());
                        Information.jobYml.set(path, null);
                        Information.jobYml.save(Information.jobFile);
                    } catch (Exception ex) {
                        Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            for (String path : Information.employeeYml.getKeys(false)) {
                try {
                    UUID employeeUUID = UUIDFetcher.getUUIDOf(path);
                    if(employeeUUID == null) {
                        Information.employeeYml.set(path, null);
                        Information.employeeYml.save(Information.employeeFile);
                    } else {
                        Information.employeeYml.set(path + ".UUID", "" + employeeUUID.toString());
                    }
                    
                } catch (Exception ex) {
                    Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            BusinessManager.createBusinesses();
            EmployeeManager.loadEmployees();
            JobManager.loadJobs();
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
        BusinessCommandHandler bch = BusinessCommandHandler.getInstance();
        JobCommandHandler jch = JobCommandHandler.getInstance();
        MiscCommandHandler mch = MiscCommandHandler.getInstance();
        getCommand("b.create").setExecutor(bch);
        getCommand("b.delete").setExecutor(bch);
        getCommand("b.withdraw").setExecutor(bch);
        getCommand("b.deposit").setExecutor(bch);
        getCommand("b.balance").setExecutor(bch);
        getCommand("b.info").setExecutor(bch);
        getCommand("b.top").setExecutor(bch);
        if (Information.managers) {
            getCommand("b.promote").setExecutor(bch);
            getCommand("b.demote").setExecutor(bch);
        }
        getCommand("b.toggle").setExecutor(bch);
        getCommand("b.salary").setExecutor(bch);
        getCommand("b.hire").setExecutor(bch);
        getCommand("b.fire").setExecutor(bch);
        getCommand("b.employee").setExecutor(bch);
        getCommand("j.open").setExecutor(jch);
        getCommand("j.claim").setExecutor(jch);
        getCommand("j.list").setExecutor(jch);
        getCommand("j.complete").setExecutor(jch);
        getCommand("j.me").setExecutor(jch);
        getCommand("j.id").setExecutor(jch);
        getCommand("businesscore").setExecutor(mch);
        getCommand("bc.help").setExecutor(mch);
        getCommand("update").setExecutor(mch);
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

    private boolean setupChat() {
        Plugin p = this.getServer().getPluginManager().getPlugin("Vault");
        if (p == null) {
            return false;
        }
        RegisteredServiceProvider<net.milkbowl.vault.chat.Chat> rsp = null;
        rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            System.out.println("Chat plugin not detected");
            return false;
        }
        Information.chat = rsp.getProvider();
        return Information.chat != null;
    }

    public static void log(Level level, String message) {
        if (Information.debug) {
            Information.log.log(level, message);
        }
    }

    public static BusinessCore getInstance() {
        if(this.instance == null)
            return null;
        return this.instance;
    }

        private static File businessFile, jobFile, employeeFile, managerFile;
        public static FileConfiguration businessYml, employeeYml, jobYml, managerYml;
        public static net.milkbowl.vault.economy.Economy eco;
        public static boolean debug;
        public static Logger log;
        public static boolean managers;
        public static boolean prefix;
        public static Chat chat;

        public static void initManagers(Plugin p) {
            if (managers) {
                managerFile = new File(p.getDataFolder(), "manager.yml");
                if (!managerFile.exists()) {
                    try {
                        managerFile.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(BusinessCore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                managerYml = YamlConfiguration.loadConfiguration(managerFile);
            }
        }

    public void initFiles() {
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
        initManagers(p);
    }
}
