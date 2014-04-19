package me.beastman3226.bc.util;

import java.util.HashMap;
import java.util.logging.Level;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.errors.InsufficientFundsException;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.player.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class Scheduler {

    public static HashMap<String, Long> playerMilli = new HashMap<String, Long>();

    public static void runAcceptance() {
        for (String name : EmployeeManager.pending.keySet()) {
            Player player = Bukkit.getPlayer(name);
            if (player != null && player.isOnline()) {
                player.sendMessage(Prefixes.NOMINAL + "Say 'yes' in chat within 10 seconds to accept your current job offer.");
                playerMilli.put(name, System.currentTimeMillis());
            }
        }
    }

    public static void runPayPeriod() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Information.BusinessCore, new Runnable() {
            @Override
            public void run() {
                BusinessCore.log(Level.INFO, "Started paying operation.");
                for (Business b : Business.businessList) {
                    Object[] employees = b.getEmployeeIDs();
                    if(b.isSalary()) {
                        for(Object id : employees) {
                            Employee e = EmployeeManager.getEmployee((Integer) id);
                            if(e != null) {
                                BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, -b.getSalary());
                                Bukkit.getPluginManager().callEvent(event);
                                if(!event.isCancelled()) {
                                    Bukkit.getPlayer(e.getName()).sendMessage("You have been payed!");
                                    Information.eco.depositPlayer(e.getName(), b.getSalary());
                                    try {
                                        b.withdraw(b.getSalary());
                                    } catch (InsufficientFundsException ex) {
                                        Information.BusinessCore.getLogger().warning(ex.getLocalizedMessage());
                                    }
                                }
                            }
                        }
                    } else {
                    for (Object id : employees) {
                        Employee e = EmployeeManager.getEmployee((Integer) id);
                        if (e != null) {
                            BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, -((b.getBalance() / employees.length) + e.getCompletedJobs() > 15 ? e.getCompletedJobs() : e.getCompletedJobs() ^ -1));
                            Bukkit.getPluginManager().callEvent(event);
                            if (!event.isCancelled()) {
                                Bukkit.getPlayerExact(e.getName()).sendMessage("You have been payed!");
                                Information.eco.depositPlayer(e.getName(), ((b.getBalance() / employees.length) + e.getCompletedJobs() > 15 ? e.getCompletedJobs() : e.getCompletedJobs() ^ -1));
                                try {
                                    b.withdraw(event.getAbsoluteAmount());
                                } catch (InsufficientFundsException ex) {
                                    Information.BusinessCore.getLogger().warning(ex.getLocalizedMessage());
                                }
                            }

                        }
                    }
                    }
                }
            }
        }, Information.getTime().getTicks(Information.getValue()), Information.getTime().getTicks(Information.getValue()));
    }

    public static void runPrefixUpdater() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Information.BusinessCore, new Runnable() {
            public void run() {
                String prefix;
                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(BusinessManager.isOwner(player.getName())) {
                        prefix = ChatColor.GRAY + "[" + Information.config.getString("prefixes.colorcodes.owner") + BusinessManager.getBusiness(player.getName()).getName() + ChatColor.GRAY + "]";
                        Information.chat.setPlayerPrefix(player, Information.chat.getPlayerPrefix(player) + ChatColor.translateAlternateColorCodes('&', prefix));
                    } else if (Manager.isManager(player.getName())) {
                        prefix = ChatColor.GRAY + "[" + Information.config.getString("prefixes.colorcodes.manager") + Manager.getBusiness(player.getName()).getName() +ChatColor.GRAY+ "]";
                        Information.chat.setPlayerPrefix(player, Information.chat.getPlayerPrefix(player) + ChatColor.translateAlternateColorCodes('&', prefix));
                    } else if (EmployeeManager.isEmployee(player.getName())) {
                        prefix = ChatColor.GRAY + "[" + Information.config.getString("prefixes.colorcodes.employee") + EmployeeManager.getEmployee(player.getName()).getBusiness().getName() +ChatColor.GRAY+ "]";
                        Information.chat.setPlayerPrefix(player, Information.chat.getPlayerPrefix(player) + ChatColor.translateAlternateColorCodes('&', prefix));
                    }
                }
            }
        }, 10, 10);
    }

}
