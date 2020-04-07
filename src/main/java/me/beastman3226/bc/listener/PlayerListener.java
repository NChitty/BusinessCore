package me.beastman3226.bc.listener;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.event.business.BusinessHiredEmployeeEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author beastman3226
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (Scheduler.playerMilli.containsKey(e.getPlayer().getName()) && e.getMessage().contains("yes")) {
            if (Scheduler.playerMilli.get(e.getPlayer().getName()) >= (System.currentTimeMillis() - 10000)) {
                Business b = BusinessManager.getBusiness(EmployeeManager.pending.get(e.getPlayer().getName()));
                BusinessHiredEmployeeEvent event = new BusinessHiredEmployeeEvent(b, null);
                Employee newEmployee = EmployeeManager.addEmployee(e.getPlayer().getName(), b.getID());
                event.setEmployee(newEmployee);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    event.getBusiness().addEmployee(event.getEmployee());
                    EmployeeManager.pending.remove(e.getPlayer().getName());
                    BusinessManager.businessList.remove(event.getBusiness());
                    BusinessManager.businessList.add(event.getBusiness());
                    e.getPlayer().sendMessage(BusinessCore.getPrefix(BusinessCore.WORKING) + "You have been hired to work for " + event.getBusiness().getName());
                    e.setCancelled(true);
                }
            } else {
                e.getPlayer().sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR) + "Timed out.");
                Scheduler.playerMilli.remove(e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        /*Player player = e.getPlayer();
        String prefix = "";
        if (Information.prefix) {
            if (BusinessManager.isOwner(player.getName())) {
                prefix = ChatColor.GRAY + "[" + Information.config.getString("prefixes.colorcodes.owner") + BusinessManager.getBusiness(player.getName()).getName() + ChatColor.GRAY + "]";
            } else if (Manager.isManager(player.getName()) && BusinessCore.Information.managers) {
                prefix = ChatColor.GRAY + "[" + Information.config.getString("prefixes.colorcodes.manager") + Manager.getBusiness(player.getName()).getName() + ChatColor.GRAY + "]";
            } else if (EmployeeManager.isEmployee(player.getName())) {
                prefix = ChatColor.GRAY + "[" + Information.config.getString("prefixes.colorcodes.employee") + EmployeeManager.getEmployee(player.getName()).getBusiness().getName() + ChatColor.GRAY + "]";
            }
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            if (!Information.chat.getPlayerPrefix(player).contains(prefix)) {
                Information.chat.setPlayerPrefix(player, ChatColor.translateAlternateColorCodes('&', prefix) + Information.chat.getPlayerPrefix(player));
            } else if (Information.chat.getPlayerPrefix(player).contains(prefix)) {
                int first = Information.chat.getPlayerPrefix(player).indexOf(prefix);
                int last = Information.chat.getPlayerPrefix(player).lastIndexOf(prefix);
                if (first != last) {
                    prefix = Information.chat.getPlayerPrefix(player).replace(Information.chat.getPlayerPrefix(player).subSequence(first, last + prefix.length()), prefix);
                    Information.chat.setPlayerPrefix(player, ChatColor.translateAlternateColorCodes('&', prefix));
                }
            }
        } else if(Information.chat.getPlayerPrefix(player).contains(BusinessManager.getBusiness(player.getName()).getName())){
            prefix = Information.chat.getPlayerPrefix(player).replace("[" + BusinessManager.getBusiness(player.getName()).getName() + "]", "");
           Information.chat.setPlayerPrefix(player, ChatColor.translateAlternateColorCodes('&', prefix));
        }*/
    }
}
