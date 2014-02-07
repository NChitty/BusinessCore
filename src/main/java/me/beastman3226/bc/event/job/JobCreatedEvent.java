package me.beastman3226.bc.event.job;

import java.util.Random;
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
    protected String name;
    protected String description;
    protected Location loc;
    protected double pay;

    public JobCreatedEvent(String description, Player p, double pay) {
        id = createId();
        this.name = p.getName();
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

    public String getName() {
        return this.name;
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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
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
