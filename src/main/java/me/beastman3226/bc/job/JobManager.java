package me.beastman3226.bc.job;

import me.beastman3226.bc.event.job.JobCreatedEvent;
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

}
