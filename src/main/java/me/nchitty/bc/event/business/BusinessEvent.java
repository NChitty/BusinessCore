package me.nchitty.bc.event.business;

import me.nchitty.bc.business.Business;
import me.nchitty.bc.business.BusinessManager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This class is abstract to avoid instantiation
 * @author beastman3226
 */
public abstract class BusinessEvent extends Event implements Cancellable{

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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean bln) {
        this.cancelled = bln;
    }

    public int getID() {
        return businessID;
    }

    public Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business b) {
        this.business = b;
    }

}
