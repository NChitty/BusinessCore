package me.beastman3226.bc.listener;

import me.beastman3226.bc.Main.Information;
import me.beastman3226.bc.data.BusinessHandler;
import me.beastman3226.bc.data.Data;
import me.beastman3226.bc.event.BusinessBalanceChangeEvent;
import me.beastman3226.bc.event.BusinessFiredEmployeeEvent;
import me.beastman3226.bc.event.BusinessHiredEmployeeEvent;
import me.beastman3226.bc.event.BusinessPostCreatedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author beastman3226
 */
public class BusinessListener implements Listener {

    @EventHandler
    public void onBalanceChange(BusinessBalanceChangeEvent e) {
        if(Information.database && !e.isCancelled()) {
                BusinessHandler.update("BusinessBalance", e.getBusiness().getBalance(), "BusinessID", e.getID());
        } else if(!Information.database && !e.isCancelled()) {
            //TODO: File update
        }
    }

    @EventHandler
    public void onCreated(BusinessPostCreatedEvent e) {
        if(Information.database && !e.isCancelled()) {
                BusinessHandler.add(Data.BUSINESS
                    .add("BusinessID", e.getBusiness().getID())
                    .add("BusinessName", e.getBusiness().getName())
                    .add("BusinessOwner", e.getBusiness().getOwnerName())
                    .add("BusinessBalance", e.getBusiness().getBalance())
                    .add("EmployeeIDs", e.getBusiness().getEmployeeIDs()));
        } else if(!Information.database && !e.isCancelled()) {
            //TODO: File update
        }
    }

    @EventHandler
    public void onHire(BusinessHiredEmployeeEvent e) {
        if(Information.database && !e.isCancelled()) {
            BusinessHandler.update("EmployeeIDs", e.getBusiness().getEmployeeIDs(), "BusinessID", e.getID());
        } else if(!Information.database && !e.isCancelled()) {
            //TODO: File update
        }
    }

    @EventHandler
    public void onFire(BusinessFiredEmployeeEvent e) {
        if(Information.database && !e.isCancelled()) {
            BusinessHandler.update("EmployeeIDs", e.finalEmployeeList(), "BusinessID", e.getID());
        } else if(!Information.database && !e.isCancelled()) {
            //TODO: File update
        }
    }

}
