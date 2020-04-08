package me.beastman3226.bc.event.business;

import org.bukkit.command.CommandSender;

import me.beastman3226.bc.business.Business;

/**
 * Created by Nicholas on 3/19/2017.
 */
public class BusinessClosedEvent extends BusinessEvent {

    private CommandSender source;

    public BusinessClosedEvent(int id) {
        super(id);
    }


    public BusinessClosedEvent(Business business) {
        super(business);
    }

    public void setSource(CommandSender source) {
        this.source = source;
    }

    public CommandSender getSource() {
        return this.source;
    }
}
