package me.beastman3226.BusinessCore.ce;

import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.business.Business;
import me.beastman3226.BusinessCore.business.BusinessManager;
import me.beastman3226.BusinessCore.business.CommandHandler;
import me.beastman3226.BusinessCore.util.MessageUtility;
import org.bukkit.command.CommandSender;

/**Handles money aspects of a business
 *
 * @author beastman3226
 */
public class BusinessMoney extends CommandHandler {

    public static BusinessMain main;

    public BusinessMoney(BusinessMain main) {
        super(main);
        BusinessMoney.main = main;
    }

    /**
     * Does all the information and handling of
     * depositing money into a business
     * @param sender The owner/commandsender
     * @param args The arguments inthe command
     */
    public static void executeDeposit(CommandSender sender, String[] args) {
        Business b = BusinessManager.getBusiness(sender.getName());
        BusinessManager.deposit(sender.getName(), Double.parseDouble(args[0]) + b.getWorth());
        sender.sendMessage(MessageUtility.PREFIX_INFO + b.getName() + " has " + b.getWorth() + " in the bank.");
        main.econ.withdrawPlayer(sender.getName(), Double.parseDouble(args[0]));
    }

    /**
     * Handles withdrawing money
     * @param sender The owner/commandsender
     * @param args the arguments
     */
    public static void executeWithdraw(CommandSender sender, String[] args) {
        Business b = BusinessManager.getBusiness(sender.getName());
        BusinessManager.withdraw(sender.getName(), Double.parseDouble(args[0]));
        main.econ.depositPlayer(sender.getName(), Double.parseDouble(args[0]));
        sender.sendMessage(MessageUtility.PREFIX_INFO + b.getName() + " has " + b.getWorth() + " left.");
        sender.sendMessage("You have " + main.econ.getBalance(sender.getName()) + main.econ.currencyNamePlural() + " in your wallet.");
    }

}
