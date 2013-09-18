package me.beastman3226.BusinessCore.ce;

import java.util.HashMap;
import java.util.Vector;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.business.Business;
import me.beastman3226.BusinessCore.business.BusinessManager;
import me.beastman3226.BusinessCore.player.Employee;
import me.beastman3226.BusinessCore.util.PayCalculator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class BusinessEmployee {

    public static HashMap<String, Business> employeeBusinessMap = new HashMap<String, Business>();

    public static void executePay(CommandSender sender, String[] subtractArg, BusinessMain main) {
        Business b = BusinessManager.getBusiness(sender.getName());
        Vector<String> list = BusinessManager.getEmployeeList(sender.getName());
        for(String employee : list) {
            main.econ.depositPlayer(employee, PayCalculator.calculate(b, employee));
            BusinessManager.payOut(PayCalculator.calculate(b, employee));
            Bukkit.getPlayer(employee).sendMessage("You have been payed " + PayCalculator.calculate(b, employee) + " "+ main.econ.currencyNamePlural());
        }
    }

    public static void executeHire(CommandSender sender, String[] subtractArg) {
        Player employee = Bukkit.getPlayer(subtractArg[0]);
        if(employee != null) {
           employeeBusinessMap.put(employee.getName(), BusinessManager.getBusiness(sender.getName()));
           employee.sendMessage("Do /yes if you would like to be hired, else do /no");
        } else {
            employeeBusinessMap.put(employee.getName(), BusinessManager.getBusiness(sender.getName()));
        }
    }

}
