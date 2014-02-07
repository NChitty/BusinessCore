package me.beastman3226.bc.event.job;

import me.beastman3226.bc.job.Job;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;

/**
 *
 * @author beastman3226
 */
public class JobClaimedEvent extends JobEvent {

    Employee e;

    public JobClaimedEvent(int id, int eID) {
        super(id);
        e = EmployeeManager.getEmployee(eID);
    }

    public JobClaimedEvent(Job j, int eID) {
        super(j);
        e = EmployeeManager.getEmployee(eID);
    }

    public Employee getEmployee() {
        return e;
    }


}
