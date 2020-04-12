package me.beastman3226.bc.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.event.job.JobClaimedEvent;
import me.beastman3226.bc.event.job.JobCompletedEvent;
import me.beastman3226.bc.event.job.JobCreatedEvent;
import me.beastman3226.bc.job.Job;
import me.beastman3226.bc.job.JobManager;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import net.milkbowl.vault.economy.EconomyResponse;

public class JobListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onCreated(JobCreatedEvent e) {
        Player player = Bukkit.getPlayer(e.getUUID());
        EconomyResponse er = BusinessCore.getInstance().getEconomy().withdrawPlayer(player, e.getPayment());
        if(er.transactionSuccess()) {
            player.sendMessage(BusinessCore.OTHER_PREFIX + "Withdrew " + BusinessCore.getInstance().getEconomy().format(e.getPayment()) + " for job creation.");
            Job newJob = JobManager.createJob(e.getDescription(), e.getLocation(), e.getPayment(), e.getUUID());
            player.sendMessage(BusinessCore.WORKING_PREFIX + "Successfully created Job #" + newJob.getID());
        } else {
            player.sendMessage(BusinessCore.ERROR_PREFIX + "You have to front the cost of the job to ensure you can pay for it later.");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onClaim(JobClaimedEvent e) {
        if(JobManager.claimJob(e.getJob(), e.getClaimingPlayer())) {
            if(BusinessManager.isOwner(e.getClaimingPlayer().getUniqueId()))
                e.getClaimingPlayer().sendMessage(BusinessCore.NOMINAL_PREFIX + "Successfully claimed Job #" + e.getID() + " for " + BusinessManager.getBusiness(e.getClaimingPlayer().getUniqueId()).getName());
            else {
                Employee emp = EmployeeManager.getEmployee(e.getClaimingPlayer().getUniqueId());
                e.getClaimingPlayer().sendMessage(BusinessCore.NOMINAL_PREFIX + "Successfully claimed Job #" + e.getID() + " for " + emp.getBusiness().getName());
                emp.startJob(e.getID());
            }
        } else {
            e.getClaimingPlayer().sendMessage(BusinessCore.ERROR_PREFIX + "Claiming job failed.");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onClompleted(JobCompletedEvent e) {
        if(JobManager.completeJob(e.getJob())) {
            Player worker = e.getJob().getWorker();
            Player player = (Player) e.getJob().getPlayer();
            if(worker.isOnline())
                worker.sendMessage(BusinessCore.WORKING_PREFIX + "Job #" + e.getJob().getID() + " has been completed, " + BusinessCore.getInstance().getEconomy().format(e.getJob().getPayment()) + " is being deposited into the business account.");
            if(player.isOnline())
                player.sendMessage(BusinessCore.WORKING_PREFIX + "You have marked " + e.getJob().getID() + " as complete.");
            if(EmployeeManager.isEmployee(worker.getUniqueId())) {
                Business b = EmployeeManager.getEmployee(worker.getUniqueId()).getBusiness();
                BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, e.getJob().getPayment());
                Bukkit.getPluginManager().callEvent(event);
            } else {
                Business b = BusinessManager.getBusiness(worker.getUniqueId());
                BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, e.getJob().getPayment());
                Bukkit.getPluginManager().callEvent(event);
            }
        } else {
           ((Player) e.getJob().getPlayer()).sendMessage(BusinessCore.ERROR_PREFIX + "An error has occurred completing the job.");
           e.setCancelled(true);
        }
    }

    
}