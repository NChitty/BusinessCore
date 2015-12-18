package me.beastman3226.bc.listener;

import java.util.logging.Level;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.data.file.JobFileManager;
import me.beastman3226.bc.event.job.JobClaimedEvent;
import me.beastman3226.bc.event.job.JobCreatedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class JobListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreate(JobCreatedEvent e) {
        if(!e.isCancelled()) {
                JobFileManager.editConfig(new FileData().add(e.getID() + ".player", e.getName())
                        .add(e.getID() + ".description", e.getDescription())
                        .add(e.getID() + ".location", e.getLocation().getBlockX() + "," + e.getLocation().getBlockY() + "," + e.getLocation().getBlockZ())
                        .add(e.getID() + ".world", e.getLocation().getWorld().getName())
                        .add(e.getID() + ".payment", e.getPayment()));
        }
        BusinessCore.log(Level.INFO, "Job #" + e.getID() + " has been created.");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaim(JobClaimedEvent e) {
        if(!e.isCancelled()) {
                JobFileManager.editConfig(new FileData().add(e.getID() + ".employee", e.getEmployee().getID()));
            BusinessCore.log(Level.INFO, e.getEmployee().getCurrentJob() + " has been claimed by " + e.getEmployee().getName());
        }
    }

}