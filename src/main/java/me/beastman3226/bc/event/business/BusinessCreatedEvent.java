package me.beastman3226.bc.event.business;

import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;

/**
 *
 * @author beastman3226
 */
public class BusinessCreatedEvent extends BusinessEvent {

    public BusinessCreatedEvent(Business b) {
        super(b);
    }

    public BusinessCreatedEvent(int id) {
        super(id);
    }

    @Override
    public void setCancelled(boolean vln) {
        if(vln == true) {
            super.cancelled = true;
            BusinessManager.closeBusiness(super.business);
        } else {
            super.cancelled = vln;
        }
    }
}
