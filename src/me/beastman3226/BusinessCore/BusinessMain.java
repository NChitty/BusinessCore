package me.beastman3226.BusinessCore;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.config.ConfigManager;
import me.beastman3226.BusinessCore.config.Configuration;
import me.beastman3226.BusinessCore.data.Data;
import me.beastman3226.BusinessCore.data.DataStore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the plugin's main class
 *
 * @author beastman3226
 */
public class BusinessMain extends JavaPlugin {

    public Economy econ;
    public final ConfigManager manager = new ConfigManager(this);
    public Configuration config;
    public Configuration flatfile;

    @Override
    public void onEnable() {
        this.setupEconomy();
        try {
            config = manager.getNewConfig("config.yml");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BusinessMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("db.enabled", true);
        defaults.put("db.ip", "localhost");
        defaults.put("db.port", "3306");
        defaults.put("db.name", "BusinessCore");
        defaults.put("db.user", "user");
        defaults.put("db.pass", "password");
        defaults.put("db.business.tableName", "business");
        defaults.put("db.employee.tableName", "employee");
        defaults.put("db.jobs.tableName", "jobs");

        config.getConfig().addDefaults(defaults);
        if(!config.contains("db.enabled")) {
            config.getConfig().setDefaults(config.getConfig().getDefaults());
        }
        if(config.getBoolean("db.enabled")) {
            Data.startup(this, config.getString("db.ip"), config.getString("db.port"), config.getString("db.name"), config.getString("db.user"), config.getString("db.pass"));
            DataStore.createTables(config.getString("db.business.tableName"), config.getString("db.employee.tableName"), config.getString("db.employee.tableName"));
        } else {
            getLogger().info("Not using MySQL, using flatfile");
            try {
                flatfile = manager.getNewConfig("storage.prop");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BusinessMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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
