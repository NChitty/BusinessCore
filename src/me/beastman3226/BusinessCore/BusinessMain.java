package me.beastman3226.BusinessCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.business.BusinessManager;
import me.beastman3226.BusinessCore.config.ConfigManager;
import me.beastman3226.BusinessCore.config.Configuration;
import me.beastman3226.BusinessCore.util.MyPersist;
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
    public Configuration bConfig;
    public Configuration jConfig;
    public Configuration eConfig;


    @Override
    public void onEnable() {
        this.setupEconomy();

        MyPersist.loading(this);
        try {
            bConfig = manager.getNewConfig("business.yml", new String[]{"This file gets deleted on startup", "If it still there please delete it"});
            jConfig = manager.getNewConfig("job.yml", new String[]{"This file gets deleted on startup", "If it still there please delete it"});
            eConfig = manager.getNewConfig("employee.yml", new String[]{"This file gets deleted on startup", "If it still there please delete it"});
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BusinessMain.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
        }
        this.getCommand("business").setExecutor(new me.beastman3226.BusinessCore.business.CommandHandler(this));
        if(bConfig.getFile().exists() || jConfig.getFile().exists() || eConfig.getFile().exists()) {
            bConfig.getFile().delete();
            jConfig.getFile().delete();
            eConfig.getFile().delete();
        } else {
            this.getLogger().info("No files found, all good.");
        }
    }

    @Override
    public void onDisable() {
        try {
            bConfig = manager.getNewConfig("business.yml", new String[]{"This file gets deleted on startup", "If it still there please delete it"});
            jConfig = manager.getNewConfig("job.yml", new String[]{"This file gets deleted on startup", "If it still there please delete it"});
            eConfig = manager.getNewConfig("employee.yml", new String[]{"This file gets deleted on startup", "If it still there please delete it"});
            MyPersist.saving(this);
        } catch (FileNotFoundException ex) {
            getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
        }
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
