package me.beastman3226.bc.listener;

import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.data.Data;
import me.beastman3226.bc.data.DataHandler;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.data.file.JobFileManager;
import me.beastman3226.bc.db.Table;
import me.beastman3226.bc.event.job.JobClaimedEvent;
import me.beastman3226.bc.event.job.JobCreatedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class JobListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreate(JobCreatedEvent e) {
        if(!e.isCancelled()) {
            if(Information.database) {
                DataHandler.add(Table.JOB, Data.JOB.add("JobID", e.getID())
                                                    .add("PlayerName", e.getName())
                                                    .add("JobDescription", e.getDescription())
                                                    .add("JobLocation", e.getLocation().getBlockX() + "," + e.getLocation().getBlockY() + "," + e.getLocation().getBlockZ())
                                                    .add("World", e.getLocation().getWorld().getName())
                                                    .add("JobPayment", e.getPayment()));
            } else {
                JobFileManager.editConfig(new FileData().add(e.getID() + ".player", e.getName())
                        .add(e.getID() + ".description", e.getDescription())
                        .add(e.getID() + ".location", e.getLocation().getBlockX() + "," + e.getLocation().getBlockY() + "," + e.getLocation().getBlockZ())
                        .add(e.getID() + ".world", e.getLocation().getWorld().getName())
                        .add(e.getID() + ".payment", e.getPayment()));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaim(JobClaimedEvent e) {
        if(!e.isCancelled()) {
            if(Information.database) {
                DataHandler.update(Table.JOB, "EmployeeID", e.getEmployee().getID(), "JobID", e.getJob().getID());
            } else{
                JobFileManager.editConfig(new FileData().add(e.getID() + ".employee", e.getEmployee().getID()));
            }
        }
    }

}