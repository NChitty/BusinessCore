package me.beastman3226.bc.commands;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.job.Job;
import me.beastman3226.bc.job.JobManager;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.Prefixes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles commands relating to jobs
 *
 * @author beastman3226
 */
public class JobCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (sender.hasPermission(cmd.getPermission())) {
            if (cmd.getName().equalsIgnoreCase("j.open") && args.length > 1) {
                if (sender instanceof Player) {
                    double pay = 0.0;
                    try {
                        pay = Double.parseDouble(args[0]);
                    } catch (NumberFormatException nfe) {
                        sender.sendMessage(Prefixes.ERROR + "The first argument must be a number!!");
                        return false;
                    }
                    String description = "";
                    for (String s : args) {
                        description = description + " " + s;
                    }
                    JobManager.createJob((Player) sender, description, pay);
                    sender.sendMessage(Prefixes.NOMINAL + "Successfully created job with description: " + description);
                } else {
                    sender.sendMessage(Prefixes.ERROR + "I need a location to create a job.");
                    return false;
                }
            } else if (cmd.getName().equalsIgnoreCase("j.claim") && args.length > 0) {
                if (sender instanceof Player && EmployeeManager.isEmployee(sender.getName())) {
                    int id = 0;
                    try {
                        id = Integer.parseInt(args[0]);
                    } catch (NumberFormatException nfe) {
                        sender.sendMessage(Prefixes.ERROR + "Your first argument must be a number.");
                        return false;
                    }
                    if(JobManager.isIssuer(sender.getName()) || JobManager.doesBelongToBusiness(EmployeeManager.getEmployee(sender.getName()), JobManager.getJob(id))) {
                        sender.sendMessage(Prefixes.ERROR + "You cannot claim job from your own business!");
                        return false;
                    }
                    if (JobManager.claimJob(EmployeeManager.getEmployee(sender.getName()), JobManager.getJob(id))) {
                        sender.sendMessage(Prefixes.NOMINAL + "Successfully claimed job " + id + " with description: " + JobManager.getJob(id).getDescription());
                    } else {
                        sender.sendMessage(Prefixes.ERROR + "You either already have an open job or a job with that ID doesn't exist");
                        return false;
                    }
                } else {
                    sender.sendMessage(Prefixes.ERROR + "I need you to be an employee of a business.");
                    return false;
                }
            } else if (cmd.getName().equalsIgnoreCase("j.list") && args.length >= 0) {
                int page = 0;
                if (args[0] != null) {
                    try {
                        page = Integer.valueOf(args[0]);
                    } catch (NumberFormatException nfe) {
                    }
                }
                sender.sendMessage(ChatColor.DARK_AQUA + "|==========Non-Claimed Job List==========|");
                sender.sendMessage(JobManager.listJobs(page));
                sender.sendMessage(ChatColor.DARK_AQUA + "Try /j.list [pagenumber] to see more jobs!");
            } else if (cmd.getName().equalsIgnoreCase("j.complete") && args.length >= 0) {
                if (sender instanceof Player && JobManager.isIssuer(sender.getName())) {
                    int id = 0;
                    try {
                        id = Integer.parseInt(args[0]);
                    } catch (NumberFormatException nfe) {
                        Job j = JobManager.getJob(sender.getName());
                        if (JobManager.completeJob(EmployeeManager.getEmployee(j.getEmployee()), j)) {
                            sender.sendMessage(Prefixes.POSITIVE + "Successful completion of job!");
                        } else {
                            return false;
                        }
                    } catch (NullPointerException npe) {
                        Job j = JobManager.getJob(sender.getName());
                        if (JobManager.completeJob(EmployeeManager.getEmployee(j.getEmployee()), j)) {
                            sender.sendMessage(Prefixes.POSITIVE + "Successful completion of job!");
                        } else {
                            return false;
                        }
                    }
                    Job j = JobManager.getJob(id);
                    if (JobManager.completeJob(EmployeeManager.getEmployee(j.getEmployee()), j)) {
                        sender.sendMessage(Prefixes.POSITIVE + "Successful completion of job!");
                    } else {
                        return false;
                    }
                }
            } else if (cmd.getName().equalsIgnoreCase("j.me")) {
                if (EmployeeManager.isEmployee(sender.getName())) {
                    Job j = JobManager.getJob(EmployeeManager.getEmployee(sender.getName()).getCurrentJob());
                    sender.sendMessage(ChatColor.DARK_GREEN + "|==========Current Job==========|");
                    sender.sendMessage(ChatColor.GREEN + "Job ID:" + ChatColor.WHITE + " " + j.getID());
                    sender.sendMessage(ChatColor.GREEN + "Issued By:" + ChatColor.WHITE + " " + j.getPlayer());
                    sender.sendMessage(ChatColor.GREEN + "Description:" + ChatColor.WHITE + " " + j.getDescription());
                    sender.sendMessage(ChatColor.GREEN + "Payment:" + ChatColor.WHITE + " " + j.getPayment() + " " + Information.eco.currencyNamePlural());
                    sender.sendMessage(ChatColor.GREEN + "Location:" + ChatColor.WHITE + " " + locToString(j.getLocation()) + " in world " + j.getLocation().getWorld().getName());
                } else if (JobManager.isIssuer(sender.getName())) {
                    if (args.length == 0) {
                        sender.sendMessage(ChatColor.DARK_BLUE + "|==========Your Jobs==========|");
                        String s = "";
                        for (Job j : JobManager.getJobs(sender.getName())) {
                            s = ChatColor.BLUE + "#" + j.getID() + ":" + ChatColor.WHITE + " " + j.getDescription() + "/n";
                        }
                        sender.sendMessage(s.split("/n"));
                    } else if (args.length >= 1) {
                        int id = 0;
                        try {
                            id = Integer.parseInt(args[0]);
                        } catch (NumberFormatException nfe) {
                            sender.sendMessage(ChatColor.DARK_BLUE + "|==========Your Jobs==========|");
                            String s = "";
                            for (Job j : JobManager.getJobs(sender.getName())) {
                                s = ChatColor.BLUE + "#" + j.getID() + ":" + ChatColor.WHITE + " " + j.getDescription() + "/n";
                            }
                            return true;
                        }
                        if (JobManager.getJob(id) != null) {
                            Job j = JobManager.getJob(id);
                            sender.sendMessage(ChatColor.DARK_GREEN + "|==========Job #" + id + "==========|");
                            sender.sendMessage(ChatColor.GREEN + "Job ID:" + ChatColor.WHITE + " " + j.getID());
                            sender.sendMessage(ChatColor.GREEN + "Issued By:" + ChatColor.WHITE + " " + j.getPlayer());
                            sender.sendMessage(ChatColor.GREEN + "Description:" + ChatColor.WHITE + " " + j.getDescription());
                            sender.sendMessage(ChatColor.GREEN + "Payment:" + ChatColor.WHITE + " " + j.getPayment() + " " + Information.eco.currencyNamePlural());
                            sender.sendMessage(ChatColor.GREEN + "Location:" + ChatColor.WHITE + " " + locToString(j.getLocation()) + " in world " + j.getLocation().getWorld().getName());
                        }
                    }
                }
            } else if (cmd.getName().equalsIgnoreCase("j.id") && args.length >= 1) {
                int id = 0;
                try {
                    id = Integer.parseInt(args[0]);
                } catch (NumberFormatException nfe) {
                    sender.sendMessage(Prefixes.ERROR + "Must be a number!");
                    return false;
                }
                Job j = JobManager.getJob(id);
                if (j != null) {
                    sender.sendMessage(ChatColor.DARK_BLUE + "|==========Job #" + id + "==========|");
                    sender.sendMessage(ChatColor.BLUE + "Job ID:" + ChatColor.WHITE + " " + j.getID());
                    sender.sendMessage(ChatColor.BLUE + "Issued By:" + ChatColor.WHITE + " " + j.getPlayer());
                    sender.sendMessage(ChatColor.BLUE + "Description:" + ChatColor.WHITE + " " + j.getDescription());
                    sender.sendMessage(ChatColor.BLUE + "Payment:" + ChatColor.WHITE + " " + j.getPayment() + " " + Information.eco.currencyNamePlural());
                    sender.sendMessage(ChatColor.BLUE + "Location:" + ChatColor.WHITE + " " + locToString(j.getLocation()) + " in world " + j.getLocation().getWorld().getName());
                } else {
                    sender.sendMessage(Prefixes.ERROR + "That is not a proper id.");
                    return false;
                }
            } else {
                sender.sendMessage(Prefixes.ERROR + "Probably wrong number of arguments.");
                return false;
            }
        } else {
            sender.sendMessage(Prefixes.ERROR + ChatColor.translateAlternateColorCodes('&', cmd.getPermissionMessage()));
        }
        return true;
    }

    private String locToString(Location loc) {
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }
}
