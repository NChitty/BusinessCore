package me.nchitty.bc.event.job;

import me.nchitty.bc.job.Job;

public class JobCompletedEvent extends JobEvent {

    public JobCompletedEvent(Job j) {
        super(j);
    }

}