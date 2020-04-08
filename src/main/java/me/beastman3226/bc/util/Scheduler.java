package me.beastman3226.bc.util;

import java.util.HashMap;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.player.EmployeeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class Scheduler {

    public static HashMap<Player, Long> playerMilli = new HashMap<>();

    public static void runAcceptance() {
        for (Player player : EmployeeManager.getPendingPlayers().keySet()) {
            if (player != null && player.isOnline()) {
                player.sendMessage(BusinessCore.NOMINAL_PREFIX + "Say 'yes' in chat within 10 seconds to accept your current job offer.");
                playerMilli.put(player, System.currentTimeMillis());
            }
        }
    }

    public static void runPayPeriod() {
        //todo
    }

}
