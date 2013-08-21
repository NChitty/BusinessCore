package me.beastman3226.BusinessCore.player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.business.Business;
import me.beastman3226.BusinessCore.job.Job;
import org.bukkit.Bukkit;
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
    }
    /**
     * This constructor creates a new job object from a string representation
     * of the object
     * @param stringRep The string representation
     */
    private Employee(String stringRep) {
        String[] fields = stringRep.split(",");
        if(Boolean.parseBoolean(fields[6])) {
            hasJob=true;
            activeJob = Job.getJob(Integer.parseInt(fields[4]));
        }
        this.completedJobs = Integer.parseInt(fields[1]);
        this.scoutedJobs = Integer.parseInt(fields[2]);
        this.employee = Bukkit.getPlayer(fields[3]);
        this.name = employee.getName();
        employeeList.add(this);
    }

    public int getCompletedJobs() {
        return this.completedJobs;
    }

    public void addCompletedJob() {
        this.completedJobs = this.completedJobs + 1;
    }

    public void setCompletedJobs(int amount) {
        this.completedJobs = amount;
    }

    public int getScoutedJobs() {
        return this.scoutedJobs;
    }

    public void addScoutedJob() {
        this.scoutedJobs = this.scoutedJobs + 1;
    }

    public void setScoutedJobs(int amount) {
        this.scoutedJobs = amount;
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
    }

    public static Employee getEmployee(String name) {
        return employeeList.get(employeeList.indexOf(name));
    }

    @Override
    public String toString() {
        String object = "";
        int i = 0;
        while(i < 8) {
            i++;
            String field = null;
            switch(i) {
                case 1: field = this.b.getOwnerName();
                    object = object+field;
                case 2: field = "completedJobs";
                    object = object+","+field;
                case 3: field = "scoutedJobs";
                    object = object+","+field;
                case 4: field = this.employee.getName();
                              object = object+","+field;
                case 5: field = this.getEmployeeName();
                    object = object+","+field;
                case 6: field = this.getJob().getId() + "";
                    object = object+","+field;
                case 7: field = this.hasJob + "";
                    object = object+","+field;
            }
        }
        return object;
    }

    public static Employee fromString(String stringRep) {
        return new Employee(stringRep);
    }
}
