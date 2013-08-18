package me.beastman3226.BusinessCore.Jobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

    public String getIssuer() {
        return this.issuer;
    }

    public Player getIssuerAsPlayer() {
        return Bukkit.getPlayer(issuer);
    }

    public Location getLocation() {
        return this.loc;
    }

    public int getId() {
        return this.id;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void complete() {
        this.completed = true;
    }
}
