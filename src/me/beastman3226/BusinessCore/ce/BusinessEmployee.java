package me.beastman3226.BusinessCore.ce;

import java.util.HashMap;
import me.beastman3226.BusinessCore.business.Business;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class BusinessEmployee {

    public static HashMap<String, Business> employeeBusinessMap = new HashMap();

    public static void executePay(CommandSender sender, String[] subtractArg) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void executeHire(CommandSender sender, String[] subtractArg) {
        Player employee = Bukkit.getPlayer(subtractArg[0]);
        if(employee != null) {
           employeeBusinessMap.put(employee.getName(), Business.getBusiness(sender.getName()));
           employee.sendMessage("Do /yes if you would like to be hired, else do /no");
        }
    }

}
