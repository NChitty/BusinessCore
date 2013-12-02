package me.beastman3226.bc.commands;

import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.event.BusinessPostCreatedEvent;
import me.beastman3226.bc.event.BusinessPreCreatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * This class handles all business related commands
 * @author beastman3226
 */
public class BusinessCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
        if(sender.hasPermission(cmnd.getPermission())) {
            if(cmnd.getName().equalsIgnoreCase("b.create") && args.length > 0) {
                BusinessPreCreatedEvent event = new BusinessPreCreatedEvent(sender, args);
                Bukkit.getPluginManager().callEvent(event);
                Business b = BusinessManager.createBusiness(new Business.Builder(BusinessManager.openID()).name(event.getName()).owner(event.getSender().getName()));
                BusinessPostCreatedEvent event1 = new BusinessPostCreatedEvent(b);
                Bukkit.getPluginManager().callEvent(event1);
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', cmnd.getPermissionMessage()));
            return false;
        }
        return false;
    }

}
