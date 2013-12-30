package me.beastman3226.bc.listener;

import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.data.BusinessHandler;
import me.beastman3226.bc.data.Data;
import me.beastman3226.bc.data.file.BusinessFileManager;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.event.business.BusinessFiredEmployeeEvent;
import me.beastman3226.bc.event.business.BusinessHiredEmployeeEvent;
import me.beastman3226.bc.event.business.BusinessPostCreatedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author beastman3226
 */
public class BusinessListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBalanceChange(BusinessBalanceChangeEvent e) {
        if(Information.database && !e.isCancelled()) {
                BusinessHandler.update("BusinessBalance", e.getFinalAmount(), "BusinessID", e.getID());
        } else if(!Information.database && !e.isCancelled()) {
            BusinessFileManager.editConfig(new FileData().add(e.getBusiness().getName() + ".balance", e.getFinalAmount()));
            e.setFinalAmount(e.getAbsoluteAmount());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreated(BusinessPostCreatedEvent e) {
        if(Information.database && !e.isCancelled()) {
                BusinessHandler.add(Data.BUSINESS
                    .add("BusinessID", e.getBusiness().getID())
                    .add("BusinessName", e.getBusiness().getName())
                    .add("BusinessOwner", e.getBusiness().getOwnerName())
                    .add("BusinessBalance", e.getBusiness().getBalance())
                    .add("EmployeeIDs", e.getBusiness().getEmployeeIDs()));
        } else if(!Information.database && !e.isCancelled()) {
            BusinessFileManager.editConfig(new FileData().add(e.getBusiness().getName() + ".name", e.getBusiness().getName())
                    .add(e.getBusiness().getName() + ".ownerName", e.getBusiness().getOwnerName())
                    .add(e.getBusiness().getName() + ".id", e.getBusiness().getID())
                    .add(e.getBusiness().getName() + ".employeeIDs", e.getBusiness().getEmployeeIDs())
                    .add(e.getBusiness().getName() + ".balance", e.getBusiness().getBalance()));
            BusinessFileManager.editConfig(new FileData().add("names", BusinessManager.names));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHire(BusinessHiredEmployeeEvent e) {
        if(Information.database && !e.isCancelled()) {
            BusinessHandler.update("EmployeeIDs", e.getFinalList(), "BusinessID", e.getID());
        } else if(!Information.database && !e.isCancelled()) {
            BusinessFileManager.editConfig(new FileData().add(e.getBusiness().getName() + ".employeeIDs", e.getFinalList()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFire(BusinessFiredEmployeeEvent e) {
        if(Information.database && !e.isCancelled()) {
            BusinessHandler.update("EmployeeIDs", e.finalEmployeeList(), "BusinessID", e.getID());
        } else if(!Information.database && !e.isCancelled()) {
            BusinessFileManager.editConfig(new FileData().add(e.getBusiness().getName() + ".employeeIDs", e.finalEmployeeList()));
        }
    }

}
