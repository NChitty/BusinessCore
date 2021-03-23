package me.nchitty.bc.listener;

import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.business.Business;
import me.nchitty.bc.event.business.BusinessHiredEmployeeEvent;
import me.nchitty.bc.player.Employee;
import me.nchitty.bc.player.EmployeeManager;
import me.nchitty.bc.util.Message;
import me.nchitty.bc.util.Scheduler;
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
        if (Scheduler.playerMilli.containsKey(e.getPlayer()) && e.getMessage().contains("yes")) {
            Business b = Business.BusinessManager.getBusiness(EmployeeManager.getPendingPlayers().get(e.getPlayer()));
            if (Scheduler.playerMilli.get(e.getPlayer()) >= (System.currentTimeMillis() - 10000)) {
                Employee newEmployee = EmployeeManager.addEmployee(e.getPlayer(), b.getID());
                e.setCancelled(true);
                BusinessHiredEmployeeEvent event = new BusinessHiredEmployeeEvent(b, newEmployee);
                Bukkit.getScheduler().runTask(BusinessCore.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
                Scheduler.playerMilli.remove(e.getPlayer());
            } else {
                Message message = new Message("errors.timeout", e.getPlayer(), b);
                message.sendMessage();
                Scheduler.playerMilli.remove(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        /*Player player = e.getPlayer();
        String prefix = "";
        if (Information.prefix) {
            if (Business.BusinessManager.isOwner(player.getName())) {
                prefix = ChatColor.GRAY + "[" + Information.config.getString("prefixes.colorcodes.owner") + Business.BusinessManager.getBusiness(player.getName()).getName() + ChatColor.GRAY + "]";
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
        } else if(Information.chat.getPlayerPrefix(player).contains(Business.BusinessManager.getBusiness(player.getName()).getName())){
            prefix = Information.chat.getPlayerPrefix(player).replace("[" + Business.BusinessManager.getBusiness(player.getName()).getName() + "]", "");
           Information.chat.setPlayerPrefix(player, ChatColor.translateAlternateColorCodes('&', prefix));
        }*/
    }
}
