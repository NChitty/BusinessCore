package me.beastman3226.BusinessCore.player;

import java.util.ArrayList;
import me.beastman3226.BusinessCore.business.Business;
import me.beastman3226.BusinessCore.job.Job;
import me.beastman3226.BusinessCore.data.DataStore;
import me.beastman3226.BusinessCore.data.DataUpdate;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class Employee {

    private Business b;
    private int completedJobs = 0;
    private int scoutedJobs = 0;
    private final Player employee;
    private final String name;
    private Job activeJob;
    private boolean hasJob = false;

    public static ArrayList<Employee> employeeList = new ArrayList<>();

    public Employee(Player player, Business b) {
        this.b = b;
        this.employee = player;
        this.name = player.getName();
        DataStore.addEmployee(name, b.getIndex(), completedJobs, scoutedJobs, hasJob, activeJob.getId());
    }

    public int getCompletedJobs() {
        return this.completedJobs;
    }

    public void addCompletedJob() {
        this.completedJobs = this.completedJobs + 1;
        DataUpdate.updateEmployee(this.getEmployeeName(), completedJobs);
    }

    public void setCompletedJobs(int amount) {
        this.completedJobs = amount;
        DataUpdate.updateEmployee(this.getEmployeeName(), completedJobs);
    }

    public int getScoutedJobs() {
        return this.scoutedJobs;
    }

    public void addScoutedJob() {
        this.scoutedJobs = this.scoutedJobs + 1;
        DataUpdate.updateEmployeeScouted(this.getEmployeeName(), scoutedJobs);
    }

    public void setScoutedJobs(int amount) {
        this.scoutedJobs = amount;
        DataUpdate.updateEmployeeScouted(this.getEmployeeName(), scoutedJobs);
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
        this.hasJob = true;
        DataUpdate.updateEmployeeJob(this.getEmployeeName(), j.getId());
    }

    public static Employee getEmployee(String name) {
        return employeeList.get(employeeList.indexOf(name));
    }
}
