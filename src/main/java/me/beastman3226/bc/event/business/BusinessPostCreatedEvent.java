package me.beastman3226.bc.event.business;

import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;

/**
 *
 * @author beastman3226
 */
public class BusinessPostCreatedEvent extends BusinessEvent {

    public BusinessPostCreatedEvent(Business b) {
        super(b);
    }

    public BusinessPostCreatedEvent(int id) {
        super(id);
    }

    @Override
    public void setCancelled(boolean vln) {
        if(vln == true) {
            super.cancelled = true;
            BusinessManager.deleteBusiness(super.business);
        } else {
            super.cancelled = vln;
        }
    }
}
