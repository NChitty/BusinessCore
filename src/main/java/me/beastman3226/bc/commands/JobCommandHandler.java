package me.beastman3226.bc.commands;

import me.beastman3226.bc.job.JobManager;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.Prefixes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles commands relating to jobs
 * @author beastman3226
 */
public class JobCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(cmd.getName().equalsIgnoreCase("j.open") && args.length > 1) {
            if(sender instanceof Player) {
                double pay = 0.0;
                try {
                    pay = Double.parseDouble(args[0]);
                } catch (NumberFormatException nfe) {
                    sender.sendMessage(Prefixes.ERROR + "The first argument must be a number!!");
                    return false;
                }
                String description = "";
                for(String s : args) {
                    description = description + " " + s;
                }
                JobManager.createJob((Player) sender, description, pay);
                sender.sendMessage(Prefixes.NOMINAL + "Successfully created job with description: " + description);
            } else {
                sender.sendMessage(Prefixes.ERROR + "I need a location to create a job!");
                return false;
            }
        } else if(cmd.getName().equalsIgnoreCase("j.claim") && args.length > 0) {
            if(sender instanceof Player && EmployeeManager.isEmployee(sender.getName())) {
                int id = 0;
                try{
                    id = Integer.parseInt(args[0]);
                } catch (NumberFormatException nfe) {
                    sender.sendMessage(Prefixes.ERROR + "Your first argument must be a number.");
                    return false;
                }
                if(JobManager.claimJob(EmployeeManager.getEmployee(sender.getName()), JobManager.getJob(id))) {
                    sender.sendMessage(Prefixes.NOMINAL + "Successfully claimed job " + id + " with description: " + JobManager.getJob(id).getDescription());
                } else {
                    sender.sendMessage(Prefixes.ERROR + "You either already have an open job or a job with that ID doesn't exist");
                }
            } else {
                sender.sendMessage(Prefixes.ERROR + "I need you to be an employee of a business.");
                return false;
            }
        } else {
            sender.sendMessage(Prefixes.ERROR + "Probably wrong number of arguments.");
            return false;
        }
        return true;
    }

}
