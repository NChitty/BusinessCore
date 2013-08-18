package me.beastman3226.BusinessCore.player;

import me.beastman3226.BusinessCore.Business.Business;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class Employee {

    private final Business b;
    private int completedJobs;
    private int scoutedJobs;
    private final Player employee;
    private final String name;

    public Employee(Player player, Business b) {
        this.b = b;
        this.employee = player;
        this.name = player.getName();
    }
}
