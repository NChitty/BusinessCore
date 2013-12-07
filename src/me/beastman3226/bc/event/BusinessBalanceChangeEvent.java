package me.beastman3226.bc.event;

import me.beastman3226.bc.business.Business;

/**
 *
 * @author beastman3226
 */
public class BusinessBalanceChangeEvent extends BusinessEvent {

    private double change;

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
}
