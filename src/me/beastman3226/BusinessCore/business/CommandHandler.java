package me.beastman3226.BusinessCore.business;

import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.ce.BusinessCreate;
import me.beastman3226.BusinessCore.ce.BusinessDelete;
import me.beastman3226.BusinessCore.ce.BusinessEmployee;
import me.beastman3226.BusinessCore.ce.BusinessHelp;
import me.beastman3226.BusinessCore.ce.BusinessMoney;
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
                if(args.length > 0) {
                    switch(args[0].toLowerCase()) {
                        case "help" : {
                            BusinessHelp.execute(sender, subtractArg(args));
                            return true;
                        }
                        case "create" : {
                            BusinessCreate.execute(sender, subtractArg(args));
                            return true;
                        }
                        case "" : {
                            BusinessDelete.execute(sender, subtractArg(args));
                            return true;
                        }
                        case "deposit" : {
                            BusinessMoney.executeDeposit(sender, subtractArg(args));
                            return true;
                        }
                        case "withdraw" : {
                            BusinessMoney.executeWithdraw(sender, subtractArg(args));
                            return true;
                        }
                        case "hire" : {
                            BusinessEmployee.executeHire(sender, subtractArg(args));
                            return true;
                        }
                        case "force-pay" : {
                            BusinessEmployee.executePay(sender, subtractArg(args));
                        }
                    }
                } else {
                    sender.sendMessage(MessageUtility.B_HELP);
                }
            }
        }
        return false;
    }

    private static String[] subtractArg(String[] args) {
        String[] newArray = new String[args.length - 1];
        for(int i = 0; i < newArray.length; i++) {
            newArray[i] = args[(i + 1)];
        }
        return newArray;
    }
}
