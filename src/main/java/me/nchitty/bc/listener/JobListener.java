package me.nchitty.bc.listener;

import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.job.Job;
import me.nchitty.bc.job.Job.JobManager;
import me.nchitty.bc.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.nchitty.bc.business.Business;
import me.nchitty.bc.event.business.BusinessBalanceChangeEvent;
import me.nchitty.bc.event.job.JobClaimedEvent;
import me.nchitty.bc.event.job.JobCompletedEvent;
import me.nchitty.bc.event.job.JobCreatedEvent;
import me.nchitty.bc.player.Employee;
import me.nchitty.bc.player.Employee.EmployeeManager;
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

            Business b = null;
            Employee emp = null;

            if (Business.BusinessManager.isOwner(e.getClaimingPlayer()))
                b = Business.BusinessManager.getBusiness(e.getClaimingPlayer());
            else {
                emp = EmployeeManager.getEmployee(e.getClaimingPlayer());
                b = emp.getBusiness();
            }

            if(b == null) {
                e.setCancelled(true);
                return;
            }

            new Message("job.claim.success", e.getClaimingPlayer(), e.getJob()).setBusiness(b).sendMessage();
            if(emp != null) emp.startJob(e.getID());

        } else {
            new Message("job.claim.fail", e.getClaimingPlayer(), e.getJob()).sendMessage();
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCompleted(JobCompletedEvent e) {
        Player worker = e.getJob().getWorker();
        Player player = (Player) e.getJob().getCreator();
        if (JobManager.completeJob(e.getJob())) {
            Business b = EmployeeManager.isEmployee(worker.getUniqueId())
                    ? EmployeeManager.getEmployee(worker.getUniqueId()).getBusiness()
                    : Business.BusinessManager.getBusiness(worker.getUniqueId());

            if (player.isOnline())
                new Message("job.complete.to_player", player, e.getJob()).setBusiness(b).sendMessage();

            if (EmployeeManager.isEmployee(worker.getUniqueId()))
                EmployeeManager.getEmployee(b, worker.getUniqueId()).completeJob();

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