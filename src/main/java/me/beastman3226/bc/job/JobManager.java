package me.beastman3226.bc.job;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.data.file.FileManager;

/**
 *
 * @author beastman3226
 */
public class JobManager {

    private static HashSet<Job> jobList = new HashSet<Job>();

    public static Job getJob(int i) {
        for (Job j : jobList)
            if (j.getID() == i)
                return j;
        return null;
    }

    public static boolean hasClaimedJob(UUID uniqueId) {
		for(Job j : jobList) {
            if(j.isClaimed())
                if(j.getWorker().getUniqueId().equals(uniqueId))
                    return true;
        }
        return false;
    }
    
    public static Job getClaimedJob(UUID uniqueId) {
        for(Job j : jobList) {
            if(j.isClaimed())
                if(j.getWorker().getUniqueId().equals(uniqueId))
                    return j;
        }
        return null;
    }

    public static Job[] getPlayerJobs(Player player) {
        HashSet<Job> jobs = new HashSet<>();
        for(Job j : jobList)
            if(j.getPlayer().getUniqueId().equals(player.getUniqueId()))
                jobs.add(j);
        return jobs.toArray(new Job[]{});
    }

    public static Job[] getOpenJobs() {
        HashSet<Job> jobs = new HashSet<>();
        for(Job j : jobList)
            if(!j.isClaimed())
                jobs.add(j);
        return jobs.toArray(new Job[]{});
    }

    public static Job createJob(String description, Location location, double payment, UUID uuid) {
        int id = jobList.size();
        Job newJob = new Job(id, uuid, description, location, payment);
        jobList.add(newJob);
        FileManager fm = BusinessCore.getInstance().getJobFileManager();
        String xyz = location.getX() + "," + location.getY() + "," + location.getZ();
        fm.edit(new FileData()
        .add(newJob.getID() + ".description", description)
        .add(newJob.getID() + ".location", xyz)
        .add(newJob.getID() + ".world", location.getWorld())
        .add(newJob.getID() + ".issuer", uuid)
        .add(newJob.getID() + ".payment", newJob.getPayment()));
        return newJob;
	}

	public static boolean claimJob(Job job, Player claimingPlayer) {
        job.setClaimed(true);
        job.setWorker(claimingPlayer);
        FileManager fm = BusinessCore.getInstance().getJobFileManager();
        fm.edit(new FileData().add(job.getID() + ".worker", claimingPlayer.getUniqueId()));
		return true;
	}

	public static boolean completeJob(Job job) {
        BusinessCore.getInstance().getJobFileManager().edit(new FileData().add(job.getID() + "", null));
        jobList.remove(job);
		return true;
	}

    public static void loadJobs() {
        FileConfiguration jobYml = BusinessCore.getInstance().getJobFileManager().getFileConfiguration();
        for (String string : jobYml.getKeys(false)) {
            Job j = null;
            int id = Integer.parseInt(string);
            String description = jobYml.getString(id + ".description");
            World world = Bukkit.getWorld(jobYml.getString(id + ".world"));
            double x = 0, y = 0, z = 0;
            try {
                String location = jobYml.getString(id + ".location");
                String[] s = location.split(",");
                x = Double.parseDouble(s[0]);
                y = Double.parseDouble(s[1]);
                z = Double.parseDouble(s[2]);
            } catch (NumberFormatException nfe) {
            }
            Location loc = new Location(world, x, y, z);
            double pay = jobYml.getDouble(id + ".payment");
            UUID issuer = UUID.fromString(jobYml.getString(id + ".issuer"));
            String worker = jobYml.getString(id + ".worker");
            if(worker == null || worker.isBlank() || worker.isEmpty())
                j = new Job(id, issuer, description, loc, pay);
            else
                j = new Job(id, issuer, description, loc, pay, UUID.fromString(worker));
            jobList.add(j);
        }

    }
}
