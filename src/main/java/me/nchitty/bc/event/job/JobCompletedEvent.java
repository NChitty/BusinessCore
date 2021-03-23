package me.nchitty.bc.event.job;

import me.nchitty.bc.job.Job;

public class JobCompletedEvent extends JobEvent {

    public JobCompletedEvent(int i) {
        super(i);
    }

    public JobCompletedEvent(Job j) {
        super(j);
    }

}