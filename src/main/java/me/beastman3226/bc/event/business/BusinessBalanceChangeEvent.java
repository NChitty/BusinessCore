package me.beastman3226.bc.event.business;

import org.bukkit.command.CommandSender;

import me.beastman3226.bc.business.Business;

/**
 *
 * @author beastman3226
 */
public class BusinessBalanceChangeEvent extends BusinessEvent {

    private double change;
    private CommandSender source;

    /**
     *
     * @param b The business
     * @param change If the change is negative then it is a withdrawal
     */
    public BusinessBalanceChangeEvent(Business b, double change) {
        super(b);
        this.change = change;
    }

    public BusinessBalanceChangeEvent(int id, double change) {
        super(id);
        this.change = change;
    }

    public void setSource(CommandSender source) {
        this.source = source;
    }

    public CommandSender getSource() {
        return this.source;
    }

    /**
     *
     * @return Returns a double positive or negative
     */
    public double getAmount() {
        return this.change;
    }

    /**
     * Checks to see if the
     * @return
     */
    public boolean isWithdrawal() {
        return (this.change < 0);
    }

    public boolean isOverdraft() {
        return this.getBusiness().getBalance() + this.getAmount() < 0;
    }

    /**
     * Gets the absolute value of the amount specified
     *
     * @return The space between the amount and zero
     */
    public double getAbsoluteAmount() {
        if(this.isWithdrawal()) {
            return -1 * this.getAmount();
        }
        return this.getAmount();
    }

    public double getFinalAmount() {
        if(this.getBusiness().getBalance() + this.getAmount() < 0) {
            return this.getBusiness().getBalance();
        } else {
            return this.getBusiness().getBalance() + this.getAmount();
        }
    }

    public void setFinalAmount(double d) {
        this.change = d;
    }
}
