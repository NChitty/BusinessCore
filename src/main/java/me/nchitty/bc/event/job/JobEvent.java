package me.nchitty.bc.event.job;

import me.nchitty.bc.job.Job;
import me.nchitty.bc.job.JobManager;
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
        job = JobManager.getJob(i);
    }

    public JobEvent(Job j) {
        job = j;
        id = job.getID();
    }


    public Job getJob() {
        return this.job;
    }

    public int getID() {
        return this.id;
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
}
