package me.beastman3226.bc.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.db.Database;
import me.beastman3226.bc.errors.OpenJobException;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.event.job.JobClaimedEvent;
import me.beastman3226.bc.event.job.JobCreatedEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.util.Prefixes;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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

    public static void loadJobs() {
        if(Information.database) {
            try {
                ResultSet rs = Database.instance().MySQL.getConnection().createStatement().executeQuery("SELECT * FROM jobs");
                while(rs.next()) {
                    int x = 0, y = 0, z = 0;
                    try{
                        String location = rs.getString("JobLocation");
                        String[] s = location.split(",");
                        x = Integer.parseInt(s[0]);
                        y = Integer.parseInt(s[1]);
                        z = Integer.parseInt(s[2]);
                    } catch(NumberFormatException nfe) {
                    }
                    World world = Bukkit.getWorld(rs.getString("World"));
                    Location loc = new Location(world, x, y, z);
                    Job j = new Job(rs.getInt("JobID"), rs.getString("PlayerName"), rs.getString("JobDescription"), loc, rs.getDouble("JobPayment"));
                    Job.jobList.add(j);
                }
            } catch (SQLException ex) {
                Logger.getLogger(JobManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            for(String string : Information.jobYml.getKeys(false)) {
                int x = 0, y = 0, z = 0;
                    try{
                        String location = Information.jobYml.getString(string + ".location");
                        String[] s = location.split(",");
                        x = Integer.parseInt(s[0]);
                        y = Integer.parseInt(s[1]);
                        z = Integer.parseInt(s[2]);
                    } catch(NumberFormatException nfe) {
                    }
                    World world = Bukkit.getWorld(Information.jobYml.getString(string + ".world"));
                    Location loc = new Location(world, x, y, z);
                Job j = new Job(Integer.parseInt(string), Information.jobYml.getString(string + ".name"), Information.jobYml.getString(string + ".description"), loc, Information.jobYml.getDouble(string + ".payment"));
            }
        }
    }

}
