package me.beastman3226.bc.commands;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.Main.Information;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.errors.InsufficientFundsException;
import me.beastman3226.bc.errors.NoOpenIDException;
import me.beastman3226.bc.event.BusinessPostCreatedEvent;
import me.beastman3226.bc.event.BusinessPreCreatedEvent;
import me.beastman3226.bc.util.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class handles all business related commands
 * @author beastman3226
 */
public class BusinessCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
        if(sender.hasPermission(cmnd.getPermission())) {
            // <editor-fold defaultstate="collapsed" desc="Business Create">
            if(cmnd.getName().equalsIgnoreCase("b.create") && args.length > 0) {
                if(sender instanceof Player && !BusinessManager.isOwner(sender.getName())) {
                    BusinessPreCreatedEvent event = new BusinessPreCreatedEvent(sender, args);
                    Bukkit.getPluginManager().callEvent(event);
                    Business b = null;
                    try {
                        b = BusinessManager.createBusiness(new Business.Builder(BusinessManager.openID()).name(event.getName()).owner(event.getSender().getName()));
                    } catch (NoOpenIDException ex) {
                        Logger.getLogger(BusinessCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    BusinessPostCreatedEvent event1 = new BusinessPostCreatedEvent(b);
                    Bukkit.getPluginManager().callEvent(event1);
                } else {
                    sender.sendMessage(Prefixes.ERROR + "I need a player name to create a business. You aren't a player. OR You already have a business");
                }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Business Delete">
            } else if (cmnd.getName().equalsIgnoreCase("b.delete")) {
                if(sender instanceof Player && BusinessManager.isOwner(sender.getName())) {
                    BusinessManager.deleteBusiness(BusinessManager.getBusiness(sender.getName()));
                } else if (!(sender instanceof Player) && args.length > 0) {
                    int k = 0;
                    boolean caught = false;
                    try {
                        k = Integer.valueOf(args[0]);
                    } catch (NumberFormatException e) {
                        caught = true;
                    }
                    if(!caught) {
                        BusinessManager.deleteBusiness(BusinessManager.getBusiness(k));
                    } else {
                        BusinessManager.deleteBusiness(BusinessManager.getBusiness(args[0]));
                    }
                } else {
                    sender.sendMessage(Prefixes.ERROR + "Valid business not found.");
                    return false;
                }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Business withdraw">
            } else if(cmnd.getName().equalsIgnoreCase("b.withdraw") && args.length > 0) {
                if(sender instanceof Player && BusinessManager.isOwner(sender.getName())) {
                    boolean caught = false;
                    double amount = 0.0;
                    Business b = BusinessManager.getBusiness(sender.getName());
                    try {
                        amount = Double.parseDouble(args[0]);
                    } catch (NumberFormatException nfe) {
                        caught = true;
                    }
                    if(caught) {
                        sender.sendMessage(Prefixes.ERROR + "Your second argument must be an amount such as 0.0");
                        return false;
                    } else {
                        try {
                            b.withdraw(amount);
                        } catch (InsufficientFundsException ex) {
                            sender.sendMessage(Prefixes.ERROR + "The amount must be less than the current balance.");
                            return false;
                        }
                        sender.sendMessage(Prefixes.NOMINAL + "Current balance in " + b.getName() + " is " + b.getBalance() + Information.eco.currencyNamePlural());
                        return true;
                    }
                } else if(!(sender instanceof Player)) {
                    boolean caught = false;
                    int id = 0;
                    double amount = 0.0;
                    try {
                        id = Integer.parseInt(args[0]);
                        amount = Double.parseDouble(args[1]);
                    } catch (NumberFormatException nfe) {
                        caught = true;
                    }
                    if(caught) {
                        sender.sendMessage("Please do b.withdraw [business_id] [amount], both must be numbers!");
                        return false;
                    } else {
                        try {
                            BusinessManager.getBusiness(id).withdraw(amount);
                        } catch (InsufficientFundsException ex) {
                            sender.sendMessage("The amount must be less than the balance!");
                            return false;
                        }
                    }
                } else {
                    sender.sendMessage(Prefixes.ERROR + "You are not an owner.");
                }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Business Deposit">
            } else if(cmnd.getName().equalsIgnoreCase("b.deposit") && args.length > 0) {
                if(sender instanceof Player && BusinessManager.isOwner(sender.getName())){
                    boolean caught = false;
                    double amount = 0.0;
                    try {
                        amount = Double.valueOf(args[0]);
                    } catch (NumberFormatException nfe) {
                        caught = true;
                    }
                    if(caught) {
                        sender.sendMessage(Prefixes.ERROR + args[0] + " is not the proper format for a deposit");
                        return false;
                    } else {
                        Information.eco.withdrawPlayer(sender.getName(), amount);
                        Business b = BusinessManager.getBusiness(sender.getName());
                        b.deposit(amount);
                        sender.sendMessage(Prefixes.NOMINAL + "Current balance in " + b.getName() + " is " + b.getBalance() + Information.eco.currencyNamePlural());
                        return true;
                    }
                } else {
                   int id = 0;
                   double amount = 0.0;
                   boolean caught = false;
                   try {
                       id = Integer.parseInt(args[0]);
                       if(args[1] == null) {
                           sender.sendMessage(Prefixes.ERROR + "You need to specify an amount!");
                           return false;
                       } else {
                           amount = Double.parseDouble(args[1]);
                       }
                   } catch (NumberFormatException nfe) {
                       caught = true;
                   }
                   if(caught) {
                       sender.sendMessage(Prefixes.ERROR + "Please specify valid numbers as numbers ie. 1, 0.0");
                       return false;
                   } else {
                       Business b = BusinessManager.getBusiness(id);
                       b.deposit(amount);
                       Player owner = Bukkit.getPlayerExact(b.getOwnerName());
                       if(owner.isOnline() | owner != null) {
                           owner.sendMessage(Prefixes.POSITIVE + "Server has just deposited " + amount + " into your bank, your new balance is " + b.getBalance() + Information.eco.currencyNamePlural());
                       }
                       return true;
                   }
                }
            // </editor-fold>
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', cmnd.getPermissionMessage()));
            return false;
        }
        return false;
    }

    private boolean throwNew(Exception e) throws Exception {
            throw e;
    }
}
