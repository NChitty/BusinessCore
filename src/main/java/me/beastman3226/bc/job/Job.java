package me.beastman3226.bc.job;

import me.beastman3226.bc.player.Employee;
import org.bukkit.Location;

/**
 *
 * @author beastman3226
 */
public class Job {

    private final int id;
    private String description;
    private Location loc;
    private double pay;
    private Employee employee;

    /**
     * From command
     * @param id The id
     * @param description the description of the job
     * @param loc location that the job was started
     * @param pay Payment that a business will recieve if it completed
     */
    public Job(int id, String description, Location loc, double pay) {
        this.id = id;
        this.description = description;
        this.loc = loc;
        this.pay = pay;
    }

    /**
     * From file/database
     * @param id The id
     * @param description the description of the job
     * @param loc location that the job was started
     * @param pay Payment that a business will recieve if it completed
     * @param e Employee
     */
    public Job(int id, String description, Location loc, double pay, Employee e) {
        this.id = id;
        this.description = description;
        this.loc = loc;
        this.pay = pay;
        this.employee = e;
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

    public Employee getEmployee() {
        return this.employee;
    }

    public void claim(Employee e) {
        if(this.employee == null) {
            this.employee = e;
        }
    }
}
