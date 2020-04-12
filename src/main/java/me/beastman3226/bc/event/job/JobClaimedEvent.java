package me.beastman3226.bc.event.job;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.beastman3226.bc.job.Job;

/**
 *
 * @author beastman3226
 */
public class JobClaimedEvent extends JobEvent {

    Player claimingPlayer;

    public JobClaimedEvent(int id, UUID uniqueId) {
        super(id);
        this.claimingPlayer = Bukkit.getPlayer(uniqueId);
    }

    public JobClaimedEvent(Job j, Player player) {
        super(j);
        this.claimingPlayer = player;
    }

    public Player getClaimingPlayer() {
        return claimingPlayer;
    }


}
