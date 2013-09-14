package me.beastman3226.BusinessCore.util;

import me.beastman3226.BusinessCore.business.Business;
import org.bukkit.ChatColor;

/**
 *
 * @author beastman3226
 */
public class MessageUtility {

    public static String PREFIX_ERROR = ChatColor.GRAY + "[" + ChatColor.RED + "BusinessCore" + ChatColor.GRAY + "]" + ChatColor.RESET + ": ";
    public static String PREFIX_INFO = ChatColor.GRAY + "[" + ChatColor.AQUA + "BusinessCore" + ChatColor.GRAY + "]" + ChatColor.RESET + ": ";
    public static String PREFIX_OTHER = ChatColor.GRAY + "[" + ChatColor.GREEN + "BusinessCore" + ChatColor.GRAY + "]" + ChatColor.RESET + ": ";
    public static String B_HELP = ChatColor.GRAY + "[" + ChatColor.AQUA + "BusinessCore" + ChatColor.GRAY + "]" + ChatColor.RESET + ": These are arguments for the command business" + "\r\n"
            + "help: Returns this screen." + "\r\n"
            + "create: Creates a business. Correct format is: /business create <name of business>" + "\r\n"
            + "delete: Deletes your business, if you are not the owner, the business will not be deleted" + "\r\n"
            + "hire: Hires the player with the specified name" + "\r\n"
            + "deposit: Deposit the amount specified, subtracts that number from your server account" + "\r\n"
            + "withdrawal: Withdraws the amount specified from your business" + "\r\n"
            + "force-pay: Gives all the employees smaller paycheck depending on the last pay cycle";
    public static final String newLine = "\r\n";
}
