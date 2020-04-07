package me.beastman3226.bc.listener;

import java.util.logging.Level;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.event.business.BusinessCreatedEvent;
import me.beastman3226.bc.event.business.BusinessFiredEmployeeEvent;
import me.beastman3226.bc.event.business.BusinessHiredEmployeeEvent;
import me.beastman3226.bc.event.business.BusinessCreatedEvent;
import me.beastman3226.bc.player.Employee;

import org.bukkit.Bukkit;
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
        if (!e.isCancelled()) {
            BusinessCore.getInstance().getBusinessFileManager()
                    .editConfig(new FileData().add(e.getBusiness().getName() + ".balance", e.getFinalAmount()));
        }
        BusinessCore.getInstance().getLogger().log(Level.INFO,
                e.getBusiness() + "'s balance has changed to " + e.getFinalAmount());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreated(BusinessCreatedEvent e) {
        String ownerName = e.getBusiness().getOwnerName();
        String businessName = e.getBusiness().getName();
        BusinessCore.getInstance().getBusinessFileManager()
                .editConfig(new FileData().add(businessName + ".name", businessName)
                        .add(e.getBusiness().getName() + ".ownerUUID",
                                "" + Bukkit.getServer().getPlayer(ownerName).getUniqueId())
                        .add(e.getBusiness().getName() + ".id", e.getBusiness().getID())
                        .add(e.getBusiness().getName() + ".employeeIDs", e.getBusiness().getEmployeeIDs())
                        .add(e.getBusiness().getName() + ".balance", e.getBusiness().getBalance()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHire(BusinessHiredEmployeeEvent e) {
        for (Object o : e.getBusiness().getEmployeeIDs()) {
            if (((Integer) o) == e.getEmployee().getID()) {
                e.setCancelled(true);
            }
        }
        BusinessCore.getInstance().getLogger().log(Level.INFO, e.getBusiness() + " has hired an employee.");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFire(BusinessFiredEmployeeEvent e) {
        boolean onList = false;
        for (Object o : e.getBusiness().getEmployeeIDs()) {
            int id = ((Integer) o);
            if (e.getEmployee().getID() == id) {
                onList = true;
                break;
            }
        }
        if (!onList) {
            e.setCancelled(true);
        }
        if (!e.isCancelled()) {
            BusinessCore.getInstance().getBusinessFileManager()
                    .editConfig(new FileData().add(e.getBusiness().getName() + ".employeeIDs", e.finalEmployeeList()));
            BusinessCore.getInstance().getEmployeeFileManager()
                    .editConfig(new FileData().add(e.getEmployee().getName(), null));
        }
        Employee.employeeList.remove(e.getEmployee());
    }

}
