package me.nchitty.bc.listener;

import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.business.Business;
import me.nchitty.bc.data.file.FileData;
import me.nchitty.bc.event.business.BusinessBalanceChangeEvent;
import me.nchitty.bc.event.business.BusinessClosedEvent;
import me.nchitty.bc.event.business.BusinessCreatedEvent;
import me.nchitty.bc.event.business.BusinessFiredEmployeeEvent;
import me.nchitty.bc.event.business.BusinessHiredEmployeeEvent;
import me.nchitty.bc.player.Employee.EmployeeManager;
import me.nchitty.bc.util.Message;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
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
        EconomyResponse er = null;

        if (e.getSource() == null) throw new NullPointerException();

        if(e.getSource() instanceof ConsoleCommandSender) {
            if(e.isWithdrawal()) {
                if(e.isOverdraft())
                    e.setCancelled(true);
                else
                    e.getBusiness().withdraw(e.getAbsoluteAmount());
            } else {
                e.getBusiness().deposit(e.getAbsoluteAmount());
            }
            // TODO if online send owner message
            return;
        } else if (e.getSource() instanceof Player) {
            if (e.isWithdrawal()) {
                if (e.isOverdraft()) {
                    Message message = new Message("business.withdraw.overdraft", e.getSource()).setBusiness(e.getBusiness()).setOther(e.getAbsoluteAmount());
                    message.sendMessage();
                    return;
                }

                er = eco.depositPlayer((Player) e.getSource(), e.getAbsoluteAmount());

                if(er.transactionSuccess()) {
                    Message message = new Message("business.withdraw.success", e.getSource()).setBusiness(e.getBusiness()).setOther(e.getAbsoluteAmount());
                    message.sendMessage();
                    e.getBusiness().withdraw(e.getAbsoluteAmount());
                    return;
                }
            } else {
                er = eco.withdrawPlayer((Player) e.getSource(), e.getAmount());
                if (er.transactionSuccess()) {
                    Message message = new Message("business.deposit", e.getSource()).setBusiness(e.getBusiness()).setOther(e.getAbsoluteAmount());
                    message.sendMessage();
                    e.getBusiness().deposit(e.getAbsoluteAmount());
                    return;
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClosed(BusinessClosedEvent e) {
        Business b = e.getBusiness();
        if (b.getOwner().isOnline()) {
            Message message = new Message("business.close", e.getSource()).setRecipient(b.getOwner()).setBusiness(b);
            message.sendMessage();
        }
        if(!e.getSource().equals(b.getOwner())) {
            Message message = new Message("business.close", e.getSource()).setBusiness(b);
            message.sendMessage();
        }
        Business.BusinessManager.closeBusiness(e.getBusiness());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreated(BusinessCreatedEvent e) {
        Player owner = e.getBusiness().getOwner();
        Message message = new Message("business.start", owner, e.getBusiness());
        message.sendMessage();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFire(BusinessFiredEmployeeEvent e) {
        Business b = e.getBusiness();
        Message message = new Message("business.employee.fire", b.getOwner(), b).setCause(Bukkit.getPlayer(e.getEmployee().getUniqueId()));
        message.sendMessage();
        EmployeeManager.getEmployeeList().remove(e.getEmployee());
        e.getBusiness().removeEmployee(e.getEmployee());
        BusinessCore.getInstance().getEmployeeFileManager()
                .edit(new FileData().add(e.getEmployee().getName() + "", null));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHire(BusinessHiredEmployeeEvent e) {
        Player owner = e.getBusiness().getOwner();
        Player employee = Bukkit.getPlayer(e.getEmployee().getUniqueId());
        Business b = e.getBusiness();
        if (owner.isOnline()) {
            Message message = new Message("business.employee.hire.offer_accepted_owner", owner, b).setCause(employee).setEmployee(e.getEmployee());
            message.sendMessage();
        }
        if (employee.isOnline()) {
            Message message = new Message("business.employee.hire.offer_accepted_employee", employee, b).setCause(owner).setEmployee(e.getEmployee());
            message.sendMessage();
        }
        e.getBusiness().addEmployee(e.getEmployee());
        EmployeeManager.getEmployeeList().add(e.getEmployee());
    }
}
