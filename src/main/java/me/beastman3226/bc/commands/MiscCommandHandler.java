package me.beastman3226.bc.commands;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.db.Database;
import me.beastman3226.bc.db.Table;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Handles communicative commands and admin/higher level commands
 *
 * @author beastman3226
 */
public class MiscCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("businesscore")) {
            sender.sendMessage(ChatColor.BLUE + "/=========BusinessCore==========\\");
            sender.sendMessage(ChatColor.AQUA + "Name:" + ChatColor.WHITE + " BusinessCore");
            sender.sendMessage(ChatColor.AQUA + "Version:" + ChatColor.WHITE + " " + Information.BusinessCore.getDescription().getVersion());
            sender.sendMessage(ChatColor.AQUA + "Author:" + ChatColor.WHITE + " beastman3226");
            sender.sendMessage(ChatColor.BLUE + "To see the help pages type /bc.help");
        } else if (cmd.getName().equalsIgnoreCase("bc.help") && args.length >= 0) {
            if (args.length <= 0) {
                sender.sendMessage(ChatColor.RED + "/==========BusinessCore Help==========\\");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Type /bc.help job for a list of job commands");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Type /bc.help business for a list of business commands");
            } else {
                if (args[0].equalsIgnoreCase("job")) {
                    sender.sendMessage(ChatColor.RED + "/==========BusinessCore Help==========\\");
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "/j.open: /j.open <payment ie. 10.0> <description ie. I need those trees cut down");
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "/j.claim: /j.claim <id of open job>");
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "/j.list: /j.list [page number]");
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "/j.complete: /j.complete [id]");
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "/j.me: /j.me");
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "/j.id: /j.id <id>");
                } else if(args[0].equalsIgnoreCase("business")) {
                    sender.sendMessage(ChatColor.RED + "/==========BusinessCore Help==========\\");
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "/b.create <name>; /b.delete");
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "/b.deposit <amount>; /b.withdraw <amount>; /b.balance");
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "/hire <playername>; /fire <playername>");
                }
            }
        } else if(cmd.getName().equalsIgnoreCase("update") && sender instanceof ConsoleCommandSender) {
            try {
                Statement s = Database.instance().MySQL.getConnection().createStatement();
                s.execute("ALTER TABLE" + Table.BUSINESS + "\n ADD Salary BOOLEAN \n\n ALTER TABLE" + Table.BUSINESS + "\n ADD Payment Float \n\n UPDATE " + Table.BUSINESS + " SET Salary=FALSE,Payment=0.0;");
            } catch (SQLException ex) {
                sender.sendMessage("Failed update for this reason: " + ex.getLocalizedMessage());
                return false;
            }
            sender.sendMessage("Update successful.");
            return true;
        }
        return true;
    }
}
