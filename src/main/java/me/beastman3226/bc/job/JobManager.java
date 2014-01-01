package me.beastman3226.bc.job;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.errors.OpenJobException;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.event.job.JobClaimedEvent;
import me.beastman3226.bc.event.job.JobCreatedEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.util.Prefixes;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class JobManager {

    public static Job getJob(int i) {
        for(Job j : Job.jobList) {
            if(j.getID() == i) {
                return j;
            }
        }
        return null;
    }

    public static Job createJob(Player p, String description, double pay) {
        Job j = null;
        JobCreatedEvent event = new JobCreatedEvent(description, p, pay);
        Bukkit.getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            j = new Job(event.getID(), event.getName(), event.getDescription(), event.getLocation(), event.getPayment());
            Job.jobList.add(j);
        }
        return j;
    }

    public static boolean claimJob(Employee e, Job j) {
        JobClaimedEvent event = new JobClaimedEvent(j, e.getID());
        Bukkit.getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            j.claim(e);
            try {
                e.startJob(j.getID());
            } catch (OpenJobException ex) {
                event.setCancelled(true);
                return false;
            }
        }
        return true;
    }

    public static void completeJob(Employee e, Job j) {
        EconomyResponse r = Information.eco.withdrawPlayer(j.getPlayer(), j.getPayment());
        if(r.transactionSuccess()) {
            BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(e.getBusiness(), j.getPayment());
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()) {
                e.getBusiness().deposit(event.getAmount());
                e.completeJob();
                j.finish();
            }
        } else {
            Bukkit.getPlayer(j.getPlayer()).sendMessage(Prefixes.ERROR + "Your balance is insufficient. Get more money!");
        }
        Job.jobList.remove(j);
    }

}
