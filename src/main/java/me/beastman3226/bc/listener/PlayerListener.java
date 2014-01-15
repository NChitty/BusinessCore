package me.beastman3226.bc.listener;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.event.business.BusinessHiredEmployeeEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.Prefixes;
import me.beastman3226.bc.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author beastman3226
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(Scheduler.playerMilli.containsKey(e.getPlayer().getName()) && e.getMessage().contains("yes")) {
            if(Scheduler.playerMilli.get(e.getPlayer().getName()) >= (System.currentTimeMillis() - 10000)) {
                Business b = BusinessManager.getBusiness(EmployeeManager.pending.get(e.getPlayer().getName()));
                BusinessHiredEmployeeEvent event = new BusinessHiredEmployeeEvent(b, null);
                Employee newEmployee = EmployeeManager.addEmployee(e.getPlayer().getName(), b.getID());
                event.setEmployee(newEmployee);
                Bukkit.getPluginManager().callEvent(event);
                if(!event.isCancelled()) {
                    event.getBusiness().addEmployee(event.getEmployee());
                    EmployeeManager.pending.remove(e.getPlayer().getName());
                    Business.businessList.remove(event.getBusiness());
                    Business.businessList.add(event.getBusiness());
                    e.getPlayer().sendMessage(Prefixes.POSITIVE + "You have been hired to work for " + event.getBusiness().getName());
                    e.setCancelled(true);
                }
            } else {
                e.getPlayer().sendMessage(Prefixes.ERROR + "Timed out.");
                Scheduler.playerMilli.remove(e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage();
        boolean another = hasPartner(command);
        if(another) {
            PluginCommand cmd = (PluginCommand) (Command) getPartner(command).getDescription().getCommands().get(command);
            Information.BusinessCore.getCommand(command).setExecutor(cmd.getExecutor());
        }

    }

    private boolean hasPartner(String command) {
        for(Plugin p : Information.BusinessCore.getServer().getPluginManager().getPlugins()) {
            if(p.getDescription().getCommands().get(command) != null && p != Information.BusinessCore) {
                return true;
            }
        }
        return false;
    }

    private Plugin getPartner(String command) {
        for(Plugin p : Information.BusinessCore.getServer().getPluginManager().getPlugins()) {
            if(p.getDescription().getCommands().get(command) != null) {
                return p;
            }
        }
        return null;
    }
}
