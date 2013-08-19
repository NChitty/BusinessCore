package me.beastman3226.BusinessCore;

import me.beastman3226.BusinessCore.config.ConfigManager;
import me.beastman3226.BusinessCore.config.Configuration;
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
    public ConfigManager manager = new ConfigManager(this);
    public Configuration bConfig;
    public Configuration jConfig;
    public Configuration persist;


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
