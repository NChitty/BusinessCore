package me.beastman3226.bc.util;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.errors.InsufficientFundsException;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import org.bukkit.Bukkit;

/**
 *
 * @author beastman3226
 */
public class Scheduler {

    public static HashMap<String, Long> playerMilli = new HashMap<String, Long>();

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

                            BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, -((b.getBalance()/employees.length) + e.getCompletedJobs() > 15 ? e.getCompletedJobs() : e.getCompletedJobs()^-1));
                            Bukkit.getPluginManager().callEvent(event);
                            if(!event.isCancelled()) {
                                Bukkit.getPlayerExact(e.getName()).sendMessage("You have been payed!");
                                Information.eco.depositPlayer(e.getName(), ((b.getBalance()/employees.length) + e.getCompletedJobs() > 15 ? e.getCompletedJobs() : e.getCompletedJobs()^-1));
                                try {
                                    b.withdraw(event.getAbsoluteAmount());
                                } catch (InsufficientFundsException ex) {
                                    Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        }
                    }
                }
            }
        }, Information.getTime().getTicks(Information.getValue()), Information.getTime().getTicks(Information.getValue()));

    }

}
