package me.beastman3226.BusinessCore.ce;

import me.beastman3226.BusinessCore.business.BusinessManager;
import me.beastman3226.BusinessCore.util.MessageUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author beastman3226
 */
public class BusinessCreate {

    public static void execute(CommandSender sender, String[] args) {
        String bName = "";
        for(int i = 0; i <args.length; i++) {
            bName = bName + args[i];
        }
        BusinessManager.createBusiness(bName, sender.getName());
        Bukkit.getServer().broadcastMessage(MessageUtility.PREFIX_OTHER + sender.getName() + " has just founded " + ChatColor.AQUA + bName);
    }
}
