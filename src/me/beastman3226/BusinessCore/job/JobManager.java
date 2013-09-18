package me.beastman3226.BusinessCore.job;

import me.beastman3226.BusinessCore.data.DataStore;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class JobManager {

    public static Job createJob(String descr, double pay, Location loc, String issuer) {
        Job j = null;
        Job finalJ = Job.jobList.get(Job.jobList.size());
        DataStore.addJob(issuer, descr, loc.getX(), loc.getBlockY(), loc.getZ(), pay, finalJ.getId() + 1, issuer, 0);
        j = new Job(descr, pay, loc, issuer, finalJ.getId() + 1);
        return j;
    }



}
