package me.beastman3226.bc.player;

import java.util.HashSet;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.errors.OpenJobException;

/**
 *
 * @author beastman3226
 */
public class Employee {

    private final String employeeName;
    private final int id;
    private Business business;
    private int completedJobs;
    private int jobID;

    public static HashSet<Employee> employeeList = new HashSet<>();

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
        return this.business;
    }

    public Employee setBusiness(Business b) {
        this.business = b;
        return this;
    }

    public Employee setBusiness(int id) {
        this.business = BusinessManager.getBusiness(id);
        return this;
    }

    public Employee setBusiness(String owner) {
        this.business = BusinessManager.getBusiness(owner);
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
        }
        return this;
    }

    public Employee completeJob() {
        this.jobID = -1;
        this.completedJobs = this.completedJobs++;
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
