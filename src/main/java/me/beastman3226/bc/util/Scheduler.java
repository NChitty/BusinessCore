package me.beastman3226.bc.util;

import java.util.HashMap;
import java.util.logging.Level;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.errors.InsufficientFundsException;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import org.bukkit.Bukkit;
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
                player.sendMessage(BusinessCore.getPrefix(BusinessCore.NOMINAL) + "Say 'yes' in chat within 10 seconds to accept your current job offer.");
                playerMilli.put(name, System.currentTimeMillis());
            }
        }
    }

    public static void runPayPeriod() {
        //todo
    }

}
