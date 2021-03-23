package me.nchitty.bc;

import java.util.HashMap;

import me.nchitty.bc.business.Business;
import me.nchitty.bc.data.file.FileManager;
import me.nchitty.bc.job.JobManager;
import me.nchitty.bc.util.Settings;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.nchitty.bc.commands.BusinessCommand;
import me.nchitty.bc.commands.JobCommand;
import me.nchitty.bc.listener.BusinessListener;
import me.nchitty.bc.listener.JobListener;
import me.nchitty.bc.listener.PlayerListener;
import me.nchitty.bc.player.EmployeeManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

/**
 *
 * @author beastman3226
 */
public class BusinessCore extends JavaPlugin {

    private static BusinessCore instance;
    private Economy eco;
    private Chat chat;
    private FileManager businessFileManager, jobFileManager, employeeFileManager;
    private Settings settings;

    HashMap<String, String> hm = new HashMap<String, String>();


    @Override
    public void onEnable() {
        //get a static instance to the plugin
        instance = this;

        //Load Vault hooks
        this.getLogger().info("Loaded Economy: " + setupEconomy());
        this.getLogger().info("Loaded Chat: " + setupChat());

        //Get the config
        this.saveDefaultConfig();

        //Load data from files
        businessFileManager = new FileManager("business.yml");
        employeeFileManager = new FileManager("employee.yml");
        jobFileManager = new FileManager("job.yml");

        //Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BusinessListener(), this);
        getServer().getPluginManager().registerEvents(new JobListener(), this);

        //Create Commands
        new BusinessCommand();
        new JobCommand();

        //settings and configs
        settings = new Settings(this.getConfig());
        settings.info();

        //Load all the data into memory
        this.getLogger().info("Loading businesses...");
        Business.BusinessManager.createBusinesses();
        this.getLogger().info("Loading employees...");
        EmployeeManager.loadEmployees();
        this.getLogger().info("Loading jobs...");
        JobManager.loadJobs();

        //Finalize
        getLogger().info("Do /businesscore for information about this plugin");
    }

    @Override
    public void onDisable() {
        //Save all data
        this.getLogger().info("Saving businesses...");
        Business.BusinessManager.saveBusinesses();
        this.getLogger().info("Saving employees...");
        EmployeeManager.saveEmployees();
        this.getLogger().info("Saving jobs...");
        JobManager.saveJobs();

        //Ensure the config is finished
        this.reloadConfig();
        this.saveConfig();
    }

    public boolean setupEconomy() {
        Plugin p = this.getServer().getPluginManager().getPlugin("Vault");
        if (p == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = null;
        rsp = (RegisteredServiceProvider<Economy>) this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            this.getLogger().severe("Economy plugin not detected");
            rsp = (RegisteredServiceProvider<Economy>) this.getServer().getServicesManager().getRegistration(Economy.class);
        }
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;

    }

    private boolean setupChat() {
        Plugin p = this.getServer().getPluginManager().getPlugin("Vault");
        if (p == null) {
            return false;
        }
        RegisteredServiceProvider<net.milkbowl.vault.chat.Chat> rsp = null;
        rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            this.getLogger().warning("Chat plugin not detected");
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    public Economy getEconomy() {
		return this.eco;
    }

    public FileManager getBusinessFileManager() {
        return this.businessFileManager;
    }

    public FileManager getEmployeeFileManager() {
        return this.employeeFileManager;
    }

    public FileManager getJobFileManager() {
        return this.jobFileManager;
    }

    public static BusinessCore getInstance() {
        if(instance == null)
            return null;
        return instance;
    }
}
