package me.beastman3226.bc.event.business;

import me.beastman3226.bc.business.Business;

/**
 * Created by Nicholas on 3/19/2017.
 */
public class BusinessDeletedEvent extends BusinessEvent {


    public BusinessDeletedEvent(int id) {
        super(id);
    }


    public BusinessDeletedEvent(Business business) {
        super(business);
    }
}
