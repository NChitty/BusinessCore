package me.beastman3226.bc.player;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.data.file.FileData;

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

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.employeeName;
    }

    public Business getBusiness() {
        return BusinessManager.getBusiness(this.business);
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public Employee setBusiness(Business b) {
        this.business = b.getID();
        return this;
    }

    public Employee setBusiness(int id) {
        this.business = BusinessManager.getBusiness(id).getID();
        return this;
    }

    public Employee setBusiness(String owner) {
        this.business = BusinessManager.getBusiness(owner).getID();
        return this;
    }

    public Employee setCompletedJobs(int i) {
        this.completedJobs = i;
        return this;
    }

    public Employee startJob(int id) throws OpenJobException {
        if(this.jobID != -1) {
            throw new OpenJobException();
        } else {
            this.jobID = id;
            BusinessCore.getInstance().getEmployeeFileManager().editConfig(new FileData().add(this.employeeName + ".job", id));
        }
        return this;
    }

    public Employee completeJob() {
        this.jobID = -1;
        this.completedJobs = this.completedJobs++;
        BusinessCore.getInstance().getEmployeeFileManager().editConfig(new FileData().add(this.employeeName + ".job", -1).add(this.employeeName + ".completed", this.completedJobs));
        return this;
    }

    public int getCompletedJobs() {
        return this.completedJobs;
    }

    public int getCurrentJob() {
        return this.jobID;
    }

    @Override
    public String toString() {
        return this.id + "";
    }
}
