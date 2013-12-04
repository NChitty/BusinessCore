package me.beastman3226.bc.event;

import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;

/**
 *
 * @author beastman3226
 */
public class BusinessUpdatedEvent extends BusinessEvent {

    private final Update type;
    private Object changedValue;
     public BusinessUpdatedEvent(int id, Update update, Object change) {
        super(id);
        this.type = update;
        this.changedValue = change;
    }

    public BusinessUpdatedEvent(Business business, Update update, Object change) {
        super(business);
        this.type = update;
        this.changedValue = change;
    }

    public Update getType() {
        return this.type;
    }

    public Object getValueChanged() {
        return this.changedValue;
    }

    public void setChangedValue(Object v) {
        this.changedValue = v;
    }

    public enum Update {

        MONEY("Business balance has been updated."),
        EMPLOYEE_LIST("Added or subtracted an employee"),
        OWNER_CHANGE("The owner has been changed.");

        private String toString;
        Update(String string) {
            this.toString = string;
        }

        @Override
        public String toString() {
            return this.toString;
        }
    }
}
