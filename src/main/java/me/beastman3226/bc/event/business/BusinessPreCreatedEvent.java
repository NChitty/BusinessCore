package me.beastman3226.bc.event.business;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * <p>Event thrown before the business is created, when the command is fired.
 * This event isn't cancellable because it is a command being fired. See PostCreated
 * for cancelling creation
 * </p>
 * @author beastman3226
 */
public class BusinessPreCreatedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private CommandSender sender;
    private String[] args;

    public BusinessPreCreatedEvent(CommandSender send, String[] args) {
        this.sender = send;
        this.args = args;
    }
    public String[] getArguments() {
        return this.args;
    }

    /**
     * This method gets the name from the arguments
     * @return Appended version of args
     */
    public String getName() {
        String toReturn = "";
        for(String s : args) {
            toReturn = toReturn.concat(s + " ");
        }
        return toReturn.trim();
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public void setArguments(String[] newArgs) {
        this.args = newArgs;
    }

    public void setName(String newName) {
        this.args = newName.split(" ");
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
