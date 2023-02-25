package me.nchitty.bc.job;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.data.file.FileData;
import me.nchitty.bc.data.file.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.nchitty.bc.util.PlaceholderPattern;


/**
 *
 * @author beastman3226
 */
public class Job {

    private final int id;
    private final UUID creator;
    private final String description;
    private final Location loc;
    private final double pay;
    private Player worker;
    private boolean claimed = false;

    /**
     * From command
     *
     * @param id The id
     * @param description the description of the job
     * @param loc location that the job was started
     * @param pay Payment that a business will receive if it completed
     */
    public Job(int id, UUID uuid, String description, Location loc, double pay) {
        this.id = id;
        this.creator = uuid;
        this.description = description;
        this.loc = loc;
        this.pay = pay;
    }

    /**
     * 
     * @param id id of the job
     * @param uuid uuid of player who created the job
     * @param description description of the job
     * @param loc location in world
     * @param pay the pay
     * @param uniqueId uuid of player who is working the job
     */
    public Job(int id, UUID uuid, String description, Location loc, double pay, UUID uniqueId) {
        this.id = id;
        this.creator = uuid;
        this.description = description;
        this.loc = loc;
        this.pay = pay;
        this.worker = Bukkit.getPlayer(uniqueId);
    }

    @PlaceholderPattern(pattern = "<job_id>")
    public int getID() {
        return this.id;
    }

    @PlaceholderPattern(pattern = "<job_description>")
    public String getDescription() {
        return this.description;
    }

    public Location getLocation() {
        return this.loc;
    }

    @PlaceholderPattern(pattern = "<job_location>")
    public String getLocationToString() {
        return Objects.requireNonNull(this.loc.getWorld()).getName() + " " + this.loc.getX() + ", " + this.loc.getY() + ", " + this.loc.getZ();
    }

    @PlaceholderPattern(pattern = "<job_payment>")
    public double getPayment() {
        return this.pay;
    }

    public Player getWorker() {
        return this.worker;
    }

    @PlaceholderPattern(pattern = "<job_worker_name>")
    public String getWorkerName() {
        return this.worker.getName();
    }

    @PlaceholderPattern(pattern = "<job_player_name>")
    public String getPlayerName() {
        return getCreator().getName();
    }

    public void setWorker(Player player) {
        this.worker = player;
    }

    public OfflinePlayer getCreator() {
        return Bukkit.getPlayer(creator);
    }


    public boolean isClaimed() {
        return this.claimed;
    }

    public void setClaimed(boolean bln) {
        this.claimed = bln;
    }

    public boolean isCreator(Player playerSender) { return this.creator.equals(playerSender.getUniqueId()); }

    public static class JobManager {

        private static final HashSet<Job> jobList = new HashSet<>();

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

        public static boolean hasClaimedJob(Player playerSender) { return hasClaimedJob(playerSender.getUniqueId()); }

        public static Job getClaimedJob(UUID uniqueId) {
            for(Job j : jobList) {
                if(j.isClaimed())
                    if(j.getWorker().getUniqueId().equals(uniqueId))
                        return j;
            }
            return null;
        }

        public static Job getClaimedJob(Player playerSender) { return getClaimedJob(playerSender.getUniqueId());  }



        public static Job[] getPlayerJobs(Player player) {
            HashSet<Job> jobs = new HashSet<>();
            for(Job j : jobList)
                if(j.getCreator().getUniqueId().equals(player.getUniqueId()))
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
            return newJob;
        }

        public static boolean claimJob(Job job, Player claimingPlayer) {
            job.setClaimed(true);
            job.setWorker(claimingPlayer);
            return true;
        }

        public static boolean completeJob(Job job) {
            if(job.isClaimed()) {
                assert BusinessCore.getInstance() != null;
                BusinessCore.getInstance().getJobFileManager().edit(new FileData().add(job.getID() + "", null));
                jobList.remove(job);
                return true;
            }
            return false;
        }

        public static void loadJobs() {
            assert BusinessCore.getInstance() != null;
            FileConfiguration jobYml = BusinessCore.getInstance().getJobFileManager().getFileConfiguration();
            for (String string : jobYml.getKeys(false)) {
                Job j;
                int id = Integer.parseInt(string);
                String description = jobYml.getString(id + ".description");
                World world = Bukkit.getWorld(Objects.requireNonNull(jobYml.getString(id + ".world")));
                double x = 0, y = 0, z = 0;
                try {
                    String location = jobYml.getString(id + ".location");
                    assert location != null;
                    String[] s = location.split(",");
                    x = Double.parseDouble(s[0]);
                    y = Double.parseDouble(s[1]);
                    z = Double.parseDouble(s[2]);
                } catch (NumberFormatException nfe) {
                }
                Location loc = new Location(world, x, y, z);
                double pay = jobYml.getDouble(id + ".payment");
                UUID issuer = UUID.fromString(Objects.requireNonNull(jobYml.getString(id + ".issuer")));
                String worker = jobYml.getString(id + ".worker");
                if(worker == null  || worker.isEmpty())
                    j = new Job(id, issuer, description, loc, pay);
                else
                    j = new Job(id, issuer, description, loc, pay, UUID.fromString(worker));
                jobList.add(j);
            }
        }

        public static void saveJobs() {
            assert BusinessCore.getInstance() != null;
            FileManager fm = BusinessCore.getInstance().getJobFileManager();
            for(Job newJob : jobList) {
                Location location = newJob.getLocation();
                String xyz = location.getX() + "," + location.getY() + "," + location.getZ();
                UUID worker = null;
                if(newJob.getWorker() != null)
                    worker = newJob.getWorker().getUniqueId();
                assert worker != null;
                fm.edit(new FileData()
                        .add(newJob.getID() + ".description", newJob.getDescription())
                        .add(newJob.getID() + ".location", xyz)
                        .add(newJob.getID() + ".world", Objects.requireNonNull(location.getWorld()).getName())
                        .add(newJob.getID() + ".issuer", newJob.getCreator().getUniqueId().toString())
                        .add(newJob.getID() + ".payment", newJob.getPayment())
                        .add(newJob.getID() + ".worker", worker.toString()));
            }
        }
    }
}
