package me.beastman3226.bc.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.db.Database;
import me.beastman3226.bc.errors.OpenJobException;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.event.job.JobClaimedEvent;
import me.beastman3226.bc.event.job.JobCreatedEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.Prefixes;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            if(j == null) {
                return false;
            }
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

    public static boolean completeJob(Employee e, Job j) {
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
        return r.transactionSuccess();
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
                Job j = new Job(Integer.parseInt(string), Information.jobYml.getString(string + ".player"), Information.jobYml.getString(string + ".description"), loc, Information.jobYml.getDouble(string + ".payment"));
                Job.jobList.add(j);
                if(Information.debug) {
                    Information.log.log(Level.INFO, "Created job #{0} with description: {1}", new Object[]{j.getID(), j.getDescription()});
                }
            }
        }
    }

    public static String[] listJobs(int i) {
        List<String> jobs = new ArrayList<String>();
        for(Job j : Job.jobList) {
            if(!j.isClaimed()) {
                   jobs.add(ChatColor.AQUA + "#" + j.getID() + ": " + j.getDescription());
            }
        }
        if(((ArrayList<String>) jobs).size() < i || ((ArrayList<String>) jobs).size() < (i*5)+5) {
            try {
                return jobs.subList(i*5, jobs.size()).toArray(new String[]{});
            } catch(ArrayIndexOutOfBoundsException aioobe) {
                return jobs.toArray(new String[]{});
            }
        }
        if(i == 0 || i == 1) {
            return jobs.toArray(new String[]{});
        } else {
            return jobs.subList(i*5, (i*5) + 5).toArray(new String[]{});
        }
    }

    public static boolean isIssuer(String name) {
        for(Job j : Job.jobList) {
            if(j != null) {
                if(j.getPlayer().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Job getJob(String name) {
        for(Job j : Job.jobList) {
            if(j.getPlayer().equalsIgnoreCase(name)) {
                return j;
            }
        }
        return null;
    }

    public static Job[] getJobs(String issuer) {
        ArrayList<Job> jobs = new ArrayList<Job>();
        for(Job j : Job.jobList) {
            if(j.getPlayer().equalsIgnoreCase(issuer)) {
                jobs.add(j);
            }
        }
        return jobs.toArray(new Job[]{});
    }

    public static boolean doesBelongToBusiness(Employee employee, Job j) {
        Business b = employee.getBusiness();
        ArrayList<Employee> employees = new ArrayList<Employee>();
        for(Object id : b.getEmployeeIDs()) {
            employees.add(EmployeeManager.getEmployee((Integer) id));
        }
        for(Employee e : employees) {
            if(j.getPlayer().equalsIgnoreCase(e.getName())) {
                return true;
            }
        }
        return false;
    }

}
