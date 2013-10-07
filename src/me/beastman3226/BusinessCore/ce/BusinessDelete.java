package me.beastman3226.BusinessCore.ce;

import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.business.BusinessManager;
import me.beastman3226.BusinessCore.util.MessageUtility;
import org.bukkit.command.CommandSender;

/** Handles deleteing a business
 *
 * @author beastman3226
 */
public class BusinessDelete {

    public static void execute(CommandSender sender, String[] subtractArg, BusinessMain plugin) {
        BusinessManager.deleteBusiness(sender.getName(), plugin);
        sender.sendMessage(MessageUtility.PREFIX_ERROR + " You have just deleted your business.");
    }

}
