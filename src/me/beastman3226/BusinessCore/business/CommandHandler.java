package me.beastman3226.BusinessCore.business;

import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.util.MessageUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class CommandHandler implements CommandExecutor {
    private BusinessMain main;

    public CommandHandler(BusinessMain plugin) {
        this.main = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if(sender instanceof Player) {
            if(cmd.getName().toLowerCase().equals("business")) {
                if(args.length == 0) {
                    sender.sendMessage(MessageUtility.B_HELP);
                } else if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "help":
                            sender.sendMessage(MessageUtility.B_HELP);
                            return true;
                        case "delete":
                            BusinessManager.deleteBusiness(sender.getName(), main);
                            return true;
                    }
                } else if (args.length > 1) {
                    switch(args[0].toLowerCase()) {
                        case "create": {
                            String name = "";
                            for(int i = 1; i < args.length; i++) {
                                name = name + args[i];
                            }
                            Business b = BusinessManager.createBusiness(name, sender.getName());
                            main.getServer().broadcastMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "BusinessCore" + ChatColor.GRAY + "]" + ChatColor.RESET + ": " + ChatColor.GOLD + b.getName() + ChatColor.WHITE + " has been created!");
                            return true;
                        }
                        case "": {

                        }
                    }
                }
            }
        } else {
            sender.sendMessage(MessageUtility.PREFIX_ERROR + "I need the sender to be a player for the owner's name and bank account");
        }
        return false;
    }

}
