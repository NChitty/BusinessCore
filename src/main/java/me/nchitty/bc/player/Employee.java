package me.nchitty.bc.player;

import java.util.UUID;

import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.business.Business;
import me.nchitty.bc.data.file.FileData;
import org.bukkit.Bukkit;

import me.nchitty.bc.util.PlaceholderPattern;

/**
 *
 * @author beastman3226
 */
public class Employee {

    private String employeeName;
    private final UUID uniqueId;
    private final int id;
    private int business;
    private int completedJobs;
    private int jobID;

    public Employee(UUID uuid, int id) {
        this.uniqueId = uuid;
        this.employeeName = Bukkit.getPlayer(uuid).getName();
        this.id = id;
    }

    @PlaceholderPattern(pattern = "<employee_id>")
    public int getID() {
        return this.id;
    }

    @PlaceholderPattern(pattern = "<employee_name>")
    public String getName() {
        return this.employeeName;
    }

    @PlaceholderPattern(pattern = "<employee_business>")
    public Business getBusiness() {
        return Business.BusinessManager.getBusiness(this.business);
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public Employee setBusiness(Business b) {
        this.business = b.getID();
        return this;
    }

    public Employee setBusiness(int id) {
        this.business = Business.BusinessManager.getBusiness(id).getID();
        return this;
    }

    public Employee setBusiness(UUID owner) {
        this.business = Business.BusinessManager.getBusiness(owner).getID();
        return this;
    }

    public Employee setCompletedJobs(int i) {
        this.completedJobs = i;
        return this;
    }

    public Employee startJob(int id) {
        this.jobID = id;
        return this;
    }

    public Employee completeJob() {
        this.jobID = -1;
        this.completedJobs = this.completedJobs++;
        BusinessCore.getInstance().getEmployeeFileManager().edit(new FileData().add(this.employeeName + ".job", null).add(this.employeeName + ".completed", this.completedJobs));
        return this;
    }

    @PlaceholderPattern(pattern = "<employee_completed_jobs>")
    public int getCompletedJobs() {
        return this.completedJobs;
    }

    @PlaceholderPattern(pattern = "<employee_current_job_id>")
    public int getCurrentJob() {
        return this.jobID;
    }

    @Override
    public String toString() {
        return this.id + "";
    }
}
