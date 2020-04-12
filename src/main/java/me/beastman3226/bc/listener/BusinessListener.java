package me.beastman3226.bc.listener;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.event.business.BusinessClosedEvent;
import me.beastman3226.bc.event.business.BusinessCreatedEvent;
import me.beastman3226.bc.event.business.BusinessFiredEmployeeEvent;
import me.beastman3226.bc.event.business.BusinessHiredEmployeeEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author beastman3226
 */
public class BusinessListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBalanceChange(BusinessBalanceChangeEvent e) {
        Economy eco = BusinessCore.getInstance().getEconomy();
        if (e.getSource() != null) {
            if (e.isWithdrawal()) {
                if (!e.isOverdraft()) {
                    if (e.getSource() instanceof Player) {
                        EconomyResponse er = eco.depositPlayer((Player) e.getSource(), e.getAbsoluteAmount());
                        if (!er.transactionSuccess()) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                    e.getSource().sendMessage(BusinessCore.NOMINAL_PREFIX + "Successfully withdrew "
                            + e.getAbsoluteAmount() + " from " + e.getBusiness().getName());
                    e.getBusiness().withdraw(e.getAbsoluteAmount());
                } else
                    e.getSource().sendMessage(BusinessCore.ERROR_PREFIX
                            + "Unable to make a withdraw larger than the balance of the business.");
            } else {
                if (e.getSource() instanceof Player) {
                    EconomyResponse er = eco.withdrawPlayer((Player) e.getSource(), e.getAmount());
                    if (!er.transactionSuccess()) {
                        e.setCancelled(true);
                        return;
                    }
                }
                e.getSource().sendMessage(BusinessCore.NOMINAL_PREFIX + "Successfully deposited "
                        + e.getAbsoluteAmount() + " to " + e.getBusiness().getName());
                e.getBusiness().deposit(e.getAbsoluteAmount());
            }
        } else {
            if(e.isWithdrawal()) {
                if(e.isOverdraft())
                    e.setCancelled(true);
                else
                    e.getBusiness().withdraw(e.getAbsoluteAmount());
            } else {
                e.getBusiness().deposit(e.getAbsoluteAmount());
            }
        }
        if (!e.isCancelled())
            BusinessCore.getInstance().getBusinessFileManager()
                    .edit(new FileData().add(e.getBusiness().getName() + ".balance", e.getFinalAmount()));

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClosed(BusinessClosedEvent e) {
        Business b = e.getBusiness();
        if (b.getOwner().isOnline()) {
            b.getOwner().sendMessage(BusinessCore.OTHER_PREFIX + "Your business has been closed.");
        }
        BusinessCore.getInstance().getLogger().info(e.getBusiness().getName() + " has been closed.");
        BusinessManager.closeBusiness(e.getBusiness());
        BusinessCore.getInstance().getBusinessFileManager()
                .edit(new FileData().add(e.getBusiness().getName(), null));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreated(BusinessCreatedEvent e) {
        Player owner = e.getBusiness().getOwner();
        owner.sendMessage(BusinessCore.OTHER_PREFIX + "You have successfully started " + e.getBusiness().getName());
        String ownerUUID = e.getBusiness().getOwnerUUID();
        String businessName = e.getBusiness().getName();
        BusinessCore.getInstance().getBusinessFileManager()
                .edit(new FileData().add(businessName + ".name", businessName)
                        .add(e.getBusiness().getName() + ".ownerUUID", ownerUUID)
                        .add(e.getBusiness().getName() + ".id", e.getBusiness().getID())
                        .add(e.getBusiness().getName() + ".balance", e.getBusiness().getBalance()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFire(BusinessFiredEmployeeEvent e) {
        EmployeeManager.getEmployeeList().remove(e.getEmployee());
        e.getBusiness().removeEmployee(e.getEmployee());
        BusinessCore.getInstance().getBusinessFileManager()
                .edit(new FileData().add(e.getEmployee().getID() + "", null));
        e.getBusiness().getOwner().sendMessage(BusinessCore.NOMINAL_PREFIX + "Fired " + e.getEmployee().getName()
                + " from " + e.getBusiness().getName());
        BusinessCore.getInstance().getLogger()
                .info("Fired " + e.getEmployee().getName() + " from " + e.getBusiness().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHire(BusinessHiredEmployeeEvent e) {
        Player owner = e.getBusiness().getOwner();
        Player employee = Bukkit.getPlayer(e.getEmployee().getUniqueId());
        if (owner.isOnline()) {
            owner.sendMessage(
                    BusinessCore.WORKING_PREFIX + employee.getName() + " has accepted a position in your business.");
        }
        if (employee.isOnline()) {
            employee.sendMessage(
                    BusinessCore.WORKING_PREFIX + "You are now an employee of " + e.getBusiness().getName());
        }
        BusinessCore.getInstance().getLogger()
                .info(employee.getName() + " has accepted a position in " + e.getBusiness().getName());
        e.getBusiness().addEmployee(e.getEmployee());
        EmployeeManager.getEmployeeList().add(e.getEmployee());
        Employee emp = e.getEmployee();
        BusinessCore.getInstance().getEmployeeFileManager()
                .edit(new FileData().add(emp.getID() + ".id", emp.getID())
                        .add(emp.getID() + ".UUID", emp.getUniqueId().toString())
                        .add(emp.getID() + ".business", emp.getBusiness().getID()));
    }
}
