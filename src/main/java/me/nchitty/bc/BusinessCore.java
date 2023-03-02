package me.nchitty.bc;

import co.aikar.commands.*;
import me.nchitty.bc.business.Business;
import me.nchitty.bc.commands.BusinessCommand;
import me.nchitty.bc.data.file.FileManager;
import me.nchitty.bc.job.Job.JobManager;
import me.nchitty.bc.listener.BusinessListener;
import me.nchitty.bc.listener.JobListener;
import me.nchitty.bc.listener.PlayerListener;
import me.nchitty.bc.player.Employee.EmployeeManager;
import me.nchitty.bc.util.Settings;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * @author beastman3226
 */
public class BusinessCore extends JavaPlugin {

  private static BusinessCore        instance;
  private        PaperCommandManager commandManager;
  private        SessionFactory      sessionFactory;
  private        Economy             eco;
  private        Chat                chat;
  private        FileManager         businessFileManager;
  private        FileManager         jobFileManager;
  private        FileManager         employeeFileManager;
  private        Settings            settings;


  @Override
  public void onEnable() {
    // Get setup plugin instance
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
    this.setupCommands();

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

  public void setupCommands() {
    commandManager = new PaperCommandManager(this);

    commandManager.enableUnstableAPI("help");

    // Register command contexts
    commandManager.getCommandContexts().registerIssuerOnlyContext(
        ConsoleCommandSender.class,
        (context) -> {
          if (context.getSender() instanceof ConsoleCommandSender) {
            return (ConsoleCommandSender) context.getSender();
          }
          throw new InvalidCommandArgument("You are not a console.");
        }
    );

    commandManager.getCommandContexts()
        .registerIssuerAwareContext(Business.class, Business.BusinessManager.getContextResolver());

    // Register command completions
    // Register command conditions
    commandManager.getCommandConditions()
        .addCondition(Player.class, "is-owner", (context, executionContext, player) -> {
          BusinessCommand.isOwnerCondition(player);
        });

    commandManager.getCommandConditions()
        .addCondition(Player.class, "is-not-owner", (context, execContext, player) -> {
          BusinessCommand.notOwnerCondition(player);
        });

    // Register commands
    commandManager.registerCommand(new BusinessCommand());

    // Default exception handler
    commandManager.setDefaultExceptionHandler(new ExceptionHandler() {
      @Override
      public boolean execute(
          BaseCommand command,
          RegisteredCommand registeredCommand,
          CommandIssuer sender,
          List<String> args,
          Throwable t
      )
      {
        getLogger().warning("An error occurred.");
        return false;
      }
    });
  }

  public boolean setupEconomy() {
    Plugin p = this.getServer().getPluginManager().getPlugin("Vault");
    if (p == null) {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = null;
    rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
      this.getLogger().severe("Economy plugin not detected");
      rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
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

  public Chat getChar() {
    return this.chat;
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

  public BukkitCommandManager getCommandManager() {
    return this.commandManager;
  }

  public static BusinessCore getInstance() {
    if (instance == null) { return null; }
    return instance;
  }

  public SessionFactory getSessionFactory() {
    return this.sessionFactory;
  }
}
