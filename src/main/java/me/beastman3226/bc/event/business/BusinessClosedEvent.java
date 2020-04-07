package me.beastman3226.bc.event.business;

import me.beastman3226.bc.business.Business;

/**
 * Created by Nicholas on 3/19/2017.
 */
public class BusinessClosedEvent extends BusinessEvent {


    public BusinessClosedEvent(int id) {
        super(id);
    }


    public BusinessClosedEvent(Business business) {
        super(business);
    }
}
