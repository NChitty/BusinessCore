package me.beastman3226.bc.commands;

import me.beastman3226.bc.BusinessCore.Information;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles communicative commands and admin/higher level commands
 *
 * @author beastman3226
 */
public class MiscCommandHandler implements CommandExecutor {
    private static MiscCommandHandler instance = null;
    protected MiscCommandHandler(){}
    public static MiscCommandHandler getInstance() {
        if(instance == null) {
            instance = new MiscCommandHandler();
        }
        return instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("businesscore")) {
            sender.sendMessage(ChatColor.BLUE + "/=========BusinessCore==========\\");
            sender.sendMessage(ChatColor.AQUA + "Name:" + ChatColor.WHITE + " BusinessCore");
            sender.sendMessage(ChatColor.AQUA + "Version:" + ChatColor.WHITE + " " + Information.BusinessCore.getDescription().getVersion());
            sender.sendMessage(ChatColor.AQUA + "Author:" + ChatColor.WHITE + " Desireaux");
            sender.sendMessage(ChatColor.BLUE + "To see the help pages type /bc.help");
        } else if (cmd.getName().equalsIgnoreCase("bc.help") && args.length >= 0) {
            if (args.length <= 0) {
                sender.sendMessage(ChatColor.RED + "/==========BusinessCore Help==========\\");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Type /bc.help job for a list of job commands");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Type /bc.help business for a list of business commands");
            } else {
                if (args[0].equalsIgnoreCase("job")) {
                    sender.sendMessage(ChatColor.BLUE + "/==========BusinessCore Help==========\\");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lOpen Job: &r&b/j.open &9<payment ie. 10.0> <description ie. I need those trees cut down"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&b&lClaim Job: &r&b/j.claim &9<id of open job>"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lList of Jobs: &r&b/j.list &9[page number]"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lClose Job: &r&b/j.complete &9[id]"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&b&lYour Job Information: &r&b/j.me"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&b&lJob Information by ID: &r&b/j.id &9<id>"));
                } else if(args[0].equalsIgnoreCase("business")) {
                    sender.sendMessage(ChatColor.RED + "/==========BusinessCore Help==========\\");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lCreate Business: &r&c/b.create &4<name>"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lDelete Business: &r&c/b.delete"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lDeposit Money: &r&c/b.deposit &4<amount>"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lWithdraw Money: &r&c/b.withdraw &4<amount>"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lCheck Balance: &r&c/b.balance"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lHire Player: &r&c/b.hire &4<playername>"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFire Player: &r&c/b.fire &4<playername>"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lList businesses: &r&c/b.top &4[page number]"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lGet Information: &r&c/b.info &4[id]"));
                }
            }
        } 
        return true;
    }
}
