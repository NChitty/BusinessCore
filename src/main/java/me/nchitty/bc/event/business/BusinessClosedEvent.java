package me.nchitty.bc.event.business;

import me.nchitty.bc.business.Business;
import org.bukkit.command.CommandSender;

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
