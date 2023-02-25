package me.nchitty.bc.event.business;

import me.nchitty.bc.business.Business;

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
        if(vln) {
            super.cancelled = true;
            Business.BusinessManager.closeBusiness(super.business);
        } else {
            super.cancelled = false;
        }
    }
}
