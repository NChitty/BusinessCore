package me.nchitty.bc.util;

import java.util.HashMap;
import me.nchitty.bc.business.BusinessManager;
import me.nchitty.bc.player.EmployeeManager;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class Scheduler {

    public static HashMap<Player, Long> playerMilli = new HashMap<>();

    public static void runAcceptance(Player sender) {
        for (Player player : EmployeeManager.getPendingPlayers().keySet()) {
            if (player != null && player.isOnline()) {
                Message message = new Message("business.employee.hire.offer", player, BusinessManager.getBusiness(EmployeeManager.getPendingPlayers().get(player))).setCause(sender);
                message.sendMessage();
                playerMilli.put(player, System.currentTimeMillis());
            }
        }
    }

    public static void runPayPeriod() {
        //todo
    }

}
