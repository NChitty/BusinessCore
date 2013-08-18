package me.beastman3226.BusinessCore.Jobs;

import org.bukkit.Location;

/**
 *
 * @author beastman3226
 */
public class Job {

    private final String description;
    private final double payment;
    private final Location loc;
    private final String issuer;
    private final int id;
    private boolean completed = false;
    public static Job[] jobList = new Job[600];

    public Job(String description, double payment, Location loc, String issuer, int id) {
        this.description = description;
        this.payment = payment;
        this.loc = loc;
        this.issuer = issuer;
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }
    public double getPayment() {
        return this.payment;
    }
    
}
