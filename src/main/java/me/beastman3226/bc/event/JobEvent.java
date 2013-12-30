package me.beastman3226.bc.event;

import me.beastman3226.bc.job.Job;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author beastman3226
 */
public class JobEvent extends Event implements Cancellable{

    protected static final HandlerList handlers = new HandlerList();
    protected boolean cancelled;
    private Job job;
    private int id;

    public JobEvent(int i) {
        id = i;
    }

    public JobEvent(Job j) {
        job = j;
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
}
