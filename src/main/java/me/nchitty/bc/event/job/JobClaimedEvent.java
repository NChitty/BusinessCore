package me.nchitty.bc.event.job;

import java.util.UUID;

import me.nchitty.bc.job.Job;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class JobClaimedEvent extends JobEvent {

    Player claimingPlayer;

    public JobClaimedEvent(Job j, Player player) {
        super(j);
        this.claimingPlayer = player;
    }

    public Player getClaimingPlayer() {
        return claimingPlayer;
    }


}
