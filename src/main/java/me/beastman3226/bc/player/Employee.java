package me.beastman3226.bc.player;

import java.util.HashSet;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.data.Data;
import me.beastman3226.bc.data.EmployeeHandler;
import me.beastman3226.bc.data.file.EmployeeFileManager;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.errors.OpenJobException;

/**
 *
 * @author beastman3226
 */
public class Employee {

    private final String employeeName;
    private final int id;
    private int business;
    private int completedJobs;
    private int jobID;

    public static HashSet<Employee> employeeList = new HashSet<Employee>();

    public Employee(String name, int id) {
        this.employeeName = name;
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
            if(Information.database) {
                EmployeeHandler.update("JobID", id, "EmployeeID", this.id);
            } else {
                EmployeeFileManager.editConfig(new FileData().add(this.employeeName + ".job", id));
            }
        }
        return this;
    }

    public Employee completeJob() {
        this.jobID = -1;
        this.completedJobs = this.completedJobs++;
        if(Information.database) {
                EmployeeHandler.update("JobID", -1, "EmployeeID", this.id);
                EmployeeHandler.update("CompletedJobs", this.completedJobs, "EmployeeID", this.id);
            } else {
                EmployeeFileManager.editConfig(new FileData().add(this.employeeName + ".job", -1).add(this.employeeName + ".completed", this.completedJobs));
            }
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
        return this.employeeName;
    }
}
