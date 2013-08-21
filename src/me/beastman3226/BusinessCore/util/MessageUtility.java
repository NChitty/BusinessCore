package me.beastman3226.BusinessCore.util;

import me.beastman3226.BusinessCore.business.Business;
import org.bukkit.ChatColor;

/**
 *
 * @author beastman3226
 */
public class MessageUtility {

    public static String PREFIX_ERROR = ChatColor.GRAY + "[" + ChatColor.RED + "BusinessCore" + ChatColor.GRAY + "]" + ChatColor.RESET + ":";
    public static String B_HELP = ChatColor.GRAY + "[" + ChatColor.AQUA + "BusinessCore" + ChatColor.GRAY + "]" + ChatColor.RESET + ": These are arguments for the command business" + System.getProperty("line.seperator")
            + "help: Returns this screen." + System.getProperty("line.seperator")
            + "create: Creates a business. Correct format is: /business create <name of business>" + System.getProperty("line.seperator")
            + "delete: Deletes your business, if you are not the owner, the business will not be deleted" + System.getProperty("line.seperator")
            + "hire: Hires the player with the specified name" + System.getProperty("line.seperator")
            + "deposit: Deposit the amount specified, subtracts that number from your server account" + System.getProperty("line.seperator")
            + "withdrawal: Withdraws the amount specified from your business" + System.getProperty("line.seperator")
            + "force-pay: Gives all the employees smaller paycheck depending on the last pay cycle";

    public static String getTop(Business[] business) {
        String list = "";
        return list;
    }

}
