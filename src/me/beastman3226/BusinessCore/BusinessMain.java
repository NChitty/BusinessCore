package me.beastman3226.BusinessCore;

import java.io.File;
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

    public static final File folder = new File("plugins" + File.separator +"BusinessCore");


    @Override
    public void onEnable() {
        this.setupEconomy();
        MyPersist.loading(this);
        bConfig.getFile().delete();
        jConfig.getFile().delete();
        eConfig.getFile().delete();
    }

    @Override
    public void onDisable() {
        bConfig = manager.getNewConfig(folder + File.separator + "business.yml", new String[]{"This file gets deleted on startup", "If it still there please delete it"});
        jConfig = manager.getNewConfig(folder + File.separator + "job.yml", new String[]{"This file gets deleted on startup", "If it still there please delete it"});
        eConfig = manager.getNewConfig(folder + File.separator + "employee.yml", new String[]{"This file gets deleted on startup", "If it still there please delete it"});
        MyPersist.saving(this);
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
