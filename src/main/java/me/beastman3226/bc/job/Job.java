package me.beastman3226.bc.job;

import java.util.HashSet;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.data.file.JobFileManager;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import org.bukkit.Location;

/**
 *
 * @author beastman3226
 */
public class Job {

    private final int id;
    private String player;
    private String description;
    private Location loc;
    private double pay;
    private int employeeid;
    private boolean claimed = false;
    public static HashSet<Job> jobList = new HashSet<Job>();

    /**
     * From command
     *
     * @param id The id
     * @param description the description of the job
     * @param loc location that the job was started
     * @param pay Payment that a business will recieve if it completed
     */
    public Job(int id, String name, String description, Location loc, double pay) {
        this.id = id;
        this.player = name;
        this.description = description;
        this.loc = loc;
        this.pay = pay;
    }

    /**
     * From file/database
     *
     * @param id The id
     * @param description the description of the job
     * @param loc location that the job was started
     * @param pay Payment that a business will recieve if it completed
     * @param e Employeeid
     */
    public Job(int id, String playername, String description, Location loc, double pay, int e) {
        this.id = id;
        this.player = playername;
        this.description = description;
        this.loc = loc;
        this.pay = pay;
        this.employeeid = e;
        JobFileManager.editConfig(new FileData().add(id + ".player", playername)
                .add(id + ".description", description)
                .add(id + ".location", loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ())
                .add(id + ".world", loc.getWorld().getName())
                .add(id + ".payment", pay)
                .add(id + ".employee", e));
    }

    public int getID() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public Location getLocation() {
        return this.loc;
    }

    public double getPayment() {
        return this.pay;
    }

    public int getEmployee() {
        return this.employeeid;
    }

    public void claim(Employee e) {
        if (this.employeeid == 0 && !claimed) {
            this.employeeid = e.getID();
            claimed = true;
        }
    }

    public void finish() {
        String employee = EmployeeManager.getEmployee(this.employeeid).getName();
        this.employeeid = 0;
        Information.eco.withdrawPlayer(player, pay);
        Business b = EmployeeManager.getEmployee(employee).getBusiness();
        b.deposit(pay);
        JobFileManager.editConfig(new FileData().add("id", null));
    }

    public String getPlayer() {
        return this.player;
    }

    public boolean isClaimed() {
        return this.claimed;
    }
}
