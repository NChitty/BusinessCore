package me.beastman3226.bc.event;

import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author beastman3226
 */
public class BusinessEvent extends Event implements Cancellable{

    protected static final HandlerList handlers = new HandlerList();
    protected boolean cancelled;
    protected int businessID;
    protected Business business;

    public BusinessEvent(int id) {
        this.businessID = id;
        business = BusinessManager.getBusiness(id);
    }

    public BusinessEvent(Business business) {
        this.business = business;
        businessID = business.getID();
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

    public int getID() {
        return businessID;
    }

}
