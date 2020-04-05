package me.beastman3226.bc;

import java.util.HashMap;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.commands.BusinessCommandHandler;
import me.beastman3226.bc.commands.JobCommandHandler;
import me.beastman3226.bc.commands.MiscCommandHandler;
import me.beastman3226.bc.job.JobManager;
import me.beastman3226.bc.listener.BusinessListener;
import me.beastman3226.bc.listener.JobListener;
import me.beastman3226.bc.listener.PlayerListener;
import me.beastman3226.bc.player.EmployeeManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author beastman3226
 */
public class BusinessCore extends JavaPlugin {

    private static BusinessCore instance;
    private Economy eco;
    private Chat chat;
    
    HashMap<String, String> hm = new HashMap<String, String>();


    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().info("Loaded Economy: " + setupEconomy());
        this.getLogger().info("Loaded Chat: " + setupChat());
        registerListeners();
        registerCommands();
        /**
         * @todo:
         */
        BusinessManager.createBusinesses();
        EmployeeManager.loadEmployees();
        JobManager.loadJobs();
        getLogger().info("Do /businesscore for information about this plugin");
    }

    @Override
    public void onDisable() {
        this.saveConfig();
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

    public static BusinessCore getInstance() {
        if(instance == null)
            return null;
        return instance;
    }
}
