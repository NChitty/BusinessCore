package me.beastman3226.bc.listener;

import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.event.BusinessHiredEmployeeEvent;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author beastman3226
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(Scheduler.playerMilli.containsKey(e.getPlayer().getName()) && e.getMessage().equalsIgnoreCase("yes")) {
            if(Scheduler.playerMilli.get(e.getPlayer().getName()) >= (System.currentTimeMillis() - 10000)) {
                Business b = BusinessManager.getBusiness(EmployeeManager.pending.get(e.getPlayer().getName()));
                BusinessHiredEmployeeEvent event = new BusinessHiredEmployeeEvent(b, null);
                event.setEmployee(EmployeeManager.addEmployee(e.getPlayer().getName()));
                Bukkit.getPluginManager().callEvent(event);
                if(!event.isCancelled()) {
                    event.getBusiness().addEmployee(event.getEmployee());
                    EmployeeManager.pending.remove(e.getPlayer().getName());
                    Business.businessList.remove(event.getBusiness());
                    Business.businessList.add(event.getBusiness());
                    e.setCancelled(true);
                }
            } else {
                Scheduler.playerMilli.remove(e.getPlayer().getName());
            }
        }
    }
}
