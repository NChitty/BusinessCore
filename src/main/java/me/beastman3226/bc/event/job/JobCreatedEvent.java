package me.beastman3226.bc.event.job;

import java.util.UUID;

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

    protected String description;
    protected Location loc;
    protected double pay;
	protected UUID uuid;

    public JobCreatedEvent(String description, Player sender, double pay) {
        this.uuid = sender.getUniqueId();
        this.description = description;
        this.loc = sender.getLocation();
        this.pay = pay;
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
}
