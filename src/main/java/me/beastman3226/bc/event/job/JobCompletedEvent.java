package me.beastman3226.bc.event.job;

import me.beastman3226.bc.job.Job;

public class JobCompletedEvent extends JobEvent {

    public JobCompletedEvent(int i) {
        super(i);
    }

    public JobCompletedEvent(Job j) {
        super(j);
    }

}