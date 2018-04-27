package me.beastman3226.bc.event.job;

import java.util.Random;
import java.util.UUID;

import me.beastman3226.bc.job.Job;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author beastman3226
 */
public class JobCreatedEvent extends Event implements Cancellable{

    protected static final HandlerList handlers = new HandlerList();
    protected boolean cancelled;

    protected final int id;
    protected String description;
    protected Location loc;
    protected double pay;
	protected UUID uuid;

    public JobCreatedEvent(String description, Player p, double pay) {
        id = createId();
        this.uuid = p.getUniqueId();
        this.description = description;
        this.loc = p.getLocation();
        this.pay = pay;
    }

    public int getID() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Location getLocation() {
        return this.loc;
    }

    public double getPayment() {
        return this.pay;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean bln) {
        this.cancelled = bln;
    }

    private int createId() {
        int i = 0;
        Random r = new Random();
        i = r.nextInt(1001);
        while(taken(i)) {
            i = r.nextInt(i) + i;
        }
        return i;
    }

    private boolean taken(int id) {
        for(Job j : Job.jobList) {
            if(j.getID() == id) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
}
