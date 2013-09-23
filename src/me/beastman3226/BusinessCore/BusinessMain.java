package me.beastman3226.BusinessCore;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.config.ConfigManager;
import me.beastman3226.BusinessCore.config.Configuration;
import me.beastman3226.BusinessCore.data.Data;
import me.beastman3226.BusinessCore.data.DataStore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the plugin's main class
 *
 * @author beastman3226
 */
public class BusinessMain extends JavaPlugin {

    public Economy econ;
    public ConfigManager manager = new ConfigManager(this);
    public static Configuration flatfile;
    public static FileConfiguration config;
    public me.beastman3226.BusinessCore.business.CommandHandler businessHandler = new me.beastman3226.BusinessCore.business.CommandHandler(this);

    @Override
    public void onEnable() {
        this.getCommand("business").setExecutor(businessHandler);
        this.saveConfig();
        this.reloadConfig();
        this.setupEconomy();
        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("db.enabled", true);
        defaults.put("db.ip", "localhost");
        defaults.put("db.port", "3306");
        defaults.put("db.name", "please_change_before_starting");
        defaults.put("db.user", "user");
        defaults.put("db.pass", "password");
        defaults.put("db.business.tableName", "business");
        defaults.put("db.employee.tableName", "employee");
        defaults.put("db.jobs.tableName", "jobs");

        if(!getConfig().contains("db.enabled")) {
            Set<String> keys = defaults.keySet();
                        for(String path : keys) {
                            getConfig().set(path, defaults.get(path));
                        }
                        this.saveDefaultConfig();
                        saveConfig();
                        reloadConfig();
        }
        if(getConfig().getBoolean("db.enabled")) {
            Data.startup(this, getConfig().getString("db.ip"), getConfig().getString("db.port"), getConfig().getString("db.name"), getConfig().getString("db.user"), getConfig().getString("db.pass"));
            DataStore.createTables(getConfig().getString("db.business.tableName"), getConfig().getString("db.employee.tableName"), getConfig().getString("db.employee.tableName"));
        } else {
            getLogger().info("Not using MySQL, using flatfile");
            try {
                flatfile = manager.getNewConfig("storage.prop");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BusinessMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        reloadConfig();
        config = getConfig();
    }

    @Override
    public void onDisable() {
    }



    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer()
                .getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
