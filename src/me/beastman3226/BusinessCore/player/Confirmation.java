package me.beastman3226.BusinessCore.player;

import me.beastman3226.BusinessCore.ce.BusinessEmployee;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author beastman3226
 */
public class Confirmation implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(BusinessEmployee.employeeBusinessMap.containsKey(cs.getName())) {
            if(cmnd.getName().equalsIgnoreCase("yes")) {
                BusinessEmployee.employeeBusinessMap.get(cs.getName()).addEmployee(cs.getName());
            } else if (cmnd.getName().equalsIgnoreCase("no")){
                BusinessEmployee.employeeBusinessMap.remove(cs.getName());
            }
        }
        return false;
    }

}
