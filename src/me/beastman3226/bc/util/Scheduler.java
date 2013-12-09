package me.beastman3226.bc.util;

import java.util.HashMap;
import me.beastman3226.bc.Main.Information;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import org.bukkit.Bukkit;

/**
 *
 * @author beastman3226
 */
public class Scheduler {

    public static HashMap<String, Long> playerMilli = new HashMap<>();

    public static void runAcceptance() {
        Bukkit.getServer().getScheduler().runTask(Information.BusinessCore, new Runnable() {
            @Override
            public void run() {
                for (String name : EmployeeManager.pending.keySet()) {
                    Bukkit.getPlayerExact(name).sendMessage(Prefixes.NOMINAL + "Say yes in chat within 10 seconds to accept your current job offer.");
                    playerMilli.put(name, System.currentTimeMillis());
                }
            }
        });
    }

    public static void runPayPeriod() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Information.BusinessCore, new Runnable() {
            @Override
            public void run() {
                for(Business b : Business.businessList) {
                    int[] employees = b.getEmployeeIDs();
                    for(int id : employees) {
                        Employee e = EmployeeManager.getEmployee(id);
                        if(e != null) {
                            Information.eco.depositPlayer(e.getName(), ((b.getBalance()/employees.length) - (e.getCompletedJobs()^-1)));
                            Bukkit.getPlayerExact(e.getName()).sendMessage("You have been payed!");
                        }
                    }
                }
            }
        }, 6000, Information.getTime().getTicks(Information.getValue()));

    }

}
