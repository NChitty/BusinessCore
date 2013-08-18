package me.beastman3226.BusinessCore.player;

import me.beastman3226.BusinessCore.Business.Business;
import me.beastman3226.BusinessCore.Jobs.Job;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class Employee {

    private final Business b;
    private int completedJobs;
    private int scoutedJobs;
    private final Player employee;
    private final String name;
    private Job activeJob;

    public Employee(Player player, Business b) {
        this.b = b;
        this.employee = player;
        this.name = player.getName();
    }

    public int getCompletedJobs() {
        return this.completedJobs;
    }

    public void addCompletedJob() {
        this.completedJobs = this.completedJobs + 1;
    }

    public int getScoutedJobs() {
        return this.scoutedJobs;
    }

    public void addScoutedJob() {
        this.scoutedJobs = this.scoutedJobs + 1;
    }

    public Business getBusiness() {
        return this.b;
    }

    public Player getEmployee() {
        return this.employee;
    }

    public String getEmployeeName() {
        return this.name;
    }

    public Job getJob() {
        return this.activeJob;
    }

    public void setActiveJob(Job j) {
        this.activeJob = j;
    }
}
