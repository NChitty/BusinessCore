package me.beastman3226.BusinessCore.ce;

import me.beastman3226.BusinessCore.business.Business;
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
            if(i != args.length) {
                bName +=args[i] + " ";
            } else {
                bName +=args[i];
            }
        }
        for(Business b : BusinessManager.listBusinesses()) {
            if(b.getName().equals(bName)) {
                sender.sendMessage(MessageUtility.PREFIX_ERROR + "A business with that name exists");
                return;
            }
        }
        for(Business b : Business.businessList) {
            if(b.getOwnerName().equalsIgnoreCase(sender.getName())) {
                sender.sendMessage(MessageUtility.PREFIX_ERROR + "You are already a business owner");
                return;
            }
        }
        BusinessManager.createBusiness(bName, sender.getName());
        Bukkit.getServer().broadcastMessage(MessageUtility.PREFIX_OTHER + sender.getName() + " has just founded " + ChatColor.AQUA + bName);
    }
}
