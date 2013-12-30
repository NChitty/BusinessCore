package me.beastman3226.bc.event.job;

import me.beastman3226.bc.job.Job;

/**
 *
 * @author beastman3226
 */
public class JobCompletedEvent extends JobEvent {

    public JobCompletedEvent(Job j) {
        super(j);
    }

    public JobCompletedEvent(int id) {
        super(id);
    }
    

}
