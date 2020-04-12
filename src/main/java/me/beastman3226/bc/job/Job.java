package me.beastman3226.bc.job;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


/**
 *
 * @author beastman3226
 */
public class Job {

    private final int id;
    private UUID player;
    private String description;
    private Location loc;
    private double pay;
    private Player worker;
    private boolean claimed = false;

    /**
     * From command
     *
     * @param id The id
     * @param description the description of the job
     * @param loc location that the job was started
     * @param pay Payment that a business will recieve if it completed
     */
    public Job(int id, UUID uuid, String description, Location loc, double pay) {
        this.id = id;
        this.player = uuid;
        this.description = description;
        this.loc = loc;
        this.pay = pay;
    }

    /**
     * 
     * @param id id of the job
     * @param uuid uuid of player who created the job
     * @param description description of the job
     * @param loc 
     * @param pay
     * @param uniqueId uuid of player who is working the job
     */
    public Job(int id, UUID uuid, String description, Location loc, double pay, UUID uniqueId) {
        this.id = id;
        this.player = uuid;
        this.description = description;
        this.loc = loc;
        this.pay = pay;
        this.worker = Bukkit.getPlayer(uniqueId);
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

    public Player getWorker() {
        return this.worker;
    }

    public void setWorker(Player player) {
        this.worker = player;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public boolean isClaimed() {
        return this.claimed;
    }

    public void setClaimed(boolean bln) {
        this.claimed = bln;
    }
}
