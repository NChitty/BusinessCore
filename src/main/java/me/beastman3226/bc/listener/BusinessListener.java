package me.beastman3226.bc.listener;

import com.evilmidget38.UUIDFetcher;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.data.file.BusinessFileManager;
import me.beastman3226.bc.data.file.EmployeeFileManager;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.event.business.*;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.util.Prefixes;
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
            BusinessFileManager.editConfig(new FileData().add(e.getBusiness().getName() + ".balance", e.getFinalAmount()));
        }
        BusinessCore.log(Level.INFO, e.getBusiness() + "'s balance has changed to " + e.getFinalAmount());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDeleted(BusinessDeletedEvent e) {
        if(!e.isCancelled()) {
            Business business = e.getBusiness();
            Business.businessList.remove(business);

            BusinessFileManager.editConfig(new FileData().add(business.getName(), null));

            BusinessCore.log(Level.WARNING, business.getOwnerName() + " has just deleted business " + business.getName());
            try {
                Bukkit.getPlayer(UUIDFetcher.getUUIDOf(business.getOwnerName())).sendMessage(Prefixes.ERROR + "Your business has been deleted");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreated(BusinessPostCreatedEvent e) {
        if (!e.isCancelled()) {
            try {
                String ownerName = e.getBusiness().getOwnerName();
                BusinessFileManager.editConfig(new FileData().add(e.getBusiness().getName() + ".name", e.getBusiness().getName())
                        .add(e.getBusiness().getName() + ".ownerUUID", "" + UUIDFetcher.getUUIDOf(ownerName).toString())
                        .add(e.getBusiness().getName() + ".id", e.getBusiness().getID())
                        .add(e.getBusiness().getName() + ".employeeIDs", e.getBusiness().getEmployeeIDs())
                        .add(e.getBusiness().getName() + ".balance", e.getBusiness().getBalance()));
            } catch (Exception ex) {
                Logger.getLogger(BusinessListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHire(BusinessHiredEmployeeEvent e) {
        for (Object o : e.getBusiness().getEmployeeIDs()) {
            if (((Integer) o) == e.getEmployee().getID()) {
                e.setCancelled(true);
            }
        }
        BusinessCore.log(Level.INFO, e.getBusiness() + " has hired an employee.");
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
            BusinessFileManager.editConfig(new FileData().add(e.getBusiness().getName() + ".employeeIDs", e.finalEmployeeList()));
            EmployeeFileManager.editConfig(new FileData().add(e.getEmployee().getName(), null));
        }
        Employee.employeeList.remove(e.getEmployee());
    }

}
