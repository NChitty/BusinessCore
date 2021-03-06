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
import me.beastman3226.bc.util.Message;
import net.milkbowl.vault.economy.EconomyResponse;

public class JobListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreated(JobCreatedEvent e) {
        Player player = Bukkit.getPlayer(e.getUUID());
        EconomyResponse er = BusinessCore.getInstance().getEconomy().withdrawPlayer(player, e.getPayment());
        if (er.transactionSuccess()) {
            Job newJob = JobManager.createJob(e.getDescription(), e.getLocation(), e.getPayment(), e.getUUID());
            new Message("job.open.money_front_success", player, newJob).sendMessage();
            new Message("job.open.successful_creation", player, newJob).sendMessage();
        } else {
            new Message("job.open.money_front_fail", player).setOther(e.getPayment());
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClaim(JobClaimedEvent e) {
        if (JobManager.claimJob(e.getJob(), e.getClaimingPlayer())) {
            if (BusinessManager.isOwner(e.getClaimingPlayer().getUniqueId())) {
                Business b = BusinessManager.getBusiness(e.getClaimingPlayer().getUniqueId());
                new Message("job.claim.success", e.getClaimingPlayer(), e.getJob()).setBusiness(b).sendMessage();
                ;
            } else {
                Employee emp = EmployeeManager.getEmployee(e.getClaimingPlayer().getUniqueId());
                new Message("job.claim.success", e.getClaimingPlayer(), e.getJob()).setEmployee(emp)
                        .setBusiness(emp.getBusiness()).sendMessage();
                emp.startJob(e.getID());
            }
        } else {
            new Message("job.claim.fail", e.getClaimingPlayer(), e.getJob()).sendMessage();
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCompleted(JobCompletedEvent e) {
        Player worker = e.getJob().getWorker();
        Player player = (Player) e.getJob().getPlayer();
        if (JobManager.completeJob(e.getJob())) {
            Business b = EmployeeManager.isEmployee(worker.getUniqueId())
                    ? EmployeeManager.getEmployee(worker.getUniqueId()).getBusiness()
                    : BusinessManager.getBusiness(worker.getUniqueId());
            if (player.isOnline())
                new Message("job.complete.to_player", player, e.getJob()).setBusiness(b).sendMessage();
            if (EmployeeManager.isEmployee(worker.getUniqueId())) {
                EmployeeManager.getEmployee(b, worker.getUniqueId()).completeJob();
            }
            if (worker.isOnline())
                new Message("job.complete.to_worker", worker, e.getJob()).setCause(player).setBusiness(b).sendMessage();
            BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, e.getJob().getPayment());
            Bukkit.getPluginManager().callEvent(event);
        } else {
            new Message("job.complete.error", player, e.getJob()).sendMessage();
            e.setCancelled(true);
        }
    }

}