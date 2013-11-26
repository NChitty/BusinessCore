package me.beastman3226.bc.commands;

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
        if(sender.hasPermission(string)) {
            if(cmnd.getName().equalsIgnoreCase("b.create") && args.length > 0) {
                BusinessPreCreatedEvent event = new BusinessPreCreatedEvent(sender, args);
                Bukkit.getPluginManager().callEvent(event);
                //TODO: create from args, call post created after
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', cmnd.getPermissionMessage()));
            return false;
        }
        return false;
    }

}
