package me.beastman3226.BusinessCore.ce;

import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.business.Business;
import me.beastman3226.BusinessCore.business.CommandHandler;
import me.beastman3226.BusinessCore.util.MessageUtility;
import org.bukkit.command.CommandSender;

/**
 *
 * @author beastman3226
 */
public class BusinessMoney extends CommandHandler {

    public static BusinessMain main;

    public BusinessMoney(BusinessMain main) {
        super(main);
        BusinessMoney.main = main;
    }

    public static void executeDeposit(CommandSender sender, String[] args) {
        Business b = Business.getBusiness(sender.getName());
        b.addToWorth(Double.parseDouble(args[0]));
        sender.sendMessage(MessageUtility.PREFIX_INFO + b.getName() + " has " + b.getWorth() + " in the bank.");
    }

    public static void executeWithdraw(CommandSender sender, String[] args) {
        Business b =Business.getBusiness(sender.getName());
        b.removeFromWorth(Double.parseDouble(args[0]));
        main.econ.depositPlayer(sender.getName(), Double.parseDouble(args[0]));
        sender.sendMessage(MessageUtility.PREFIX_INFO + b.getName() + " has " + b.getWorth() + " left.");
        sender.sendMessage("You have " + main.econ.getBalance(sender.getName()) + main.econ.currencyNamePlural() + " in your wallet.");
    }

}
