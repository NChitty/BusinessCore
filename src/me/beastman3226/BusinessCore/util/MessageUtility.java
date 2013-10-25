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
            ChatColor.AQUA + "help:" + ChatColor.WHITE +  " Returns this screen.",
            ChatColor.AQUA + "create:" + ChatColor.WHITE +  " Creates a business. Correct format is: /business create <name of business>",
            ChatColor.AQUA + "delete:" + ChatColor.WHITE +  " Deletes your business, if you are not the owner, the business will not be deleted",
            ChatColor.AQUA + "hire:" + ChatColor.WHITE +  " Hires the player with the specified name",
            ChatColor.AQUA + "deposit:" + ChatColor.WHITE +  " Deposit the amount specified, subtracts that number from your server account",
            ChatColor.AQUA + "withdraw:" + ChatColor.WHITE +  " Withdraws the amount specified from your business",
            ChatColor.AQUA + "force-pay:" + ChatColor.WHITE +  "  Gives all the employees smaller paycheck depending on the last pay cycle"};
    public static final String newLine = "\r\n";

    public static String debug(Business param) {
        return "Name: " + param.getName() + "\r\n Owner name: " + param.getOwnerName() + "\r\n ID:Worth" + param.getIndex() + ":" + param.getWorth();
    }
}
