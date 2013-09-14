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
    public static String[] B_HELP = new String[]{ChatColor.GRAY + "[" + ChatColor.AQUA + "BusinessCore" + ChatColor.GRAY + "]" + ChatColor.RESET + ": These are arguments for the command business",
            "help: Returns this screen.",
            "create: Creates a business. Correct format is: /business create <name of business>",
            "delete: Deletes your business, if you are not the owner, the business will not be deleted",
            "hire: Hires the player with the specified name",
            "deposit: Deposit the amount specified, subtracts that number from your server account",
            "withdraw: Withdraws the amount specified from your business",
            "force-pay: Gives all the employees smaller paycheck depending on the last pay cycle"};
    public static final String newLine = "\r\n";
}
