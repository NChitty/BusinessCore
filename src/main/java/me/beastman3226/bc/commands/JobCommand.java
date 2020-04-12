package me.beastman3226.bc.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.event.job.JobClaimedEvent;
import me.beastman3226.bc.event.job.JobCompletedEvent;
import me.beastman3226.bc.event.job.JobCreatedEvent;
import me.beastman3226.bc.job.Job;
import me.beastman3226.bc.job.JobManager;
import me.beastman3226.bc.player.EmployeeManager;
import net.md_5.bungee.api.ChatColor;

public class JobCommand extends ICommand {

    public JobCommand() {
        super("job", "businesscore.job", true);
    }

    @Override
    public void execute(CommandSender sender) {
        // TODO Auto-generated method stub
    }

    @Subcommand(consoleUse = false, minArgs = 2, permission = "businesscore.job.request", usage = "/job open [payment] [description]")
    public void open(CommandSender sender, String[] args) {
        double pay = 0.0;
        if (!args[0].matches("([^0-9.0-9])")) {
            pay = Double.parseDouble(args[0]);
        } else {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "The amount must be a number.");
            return;
        }
        String description = this.parseArgs(Arrays.copyOfRange(args, 1, args.length));
        JobCreatedEvent event = new JobCreatedEvent(description, (Player) sender, pay);
        Bukkit.getPluginManager().callEvent(event);
    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.job.claim", usage = "/job claim [id]")
    public void claim(CommandSender sender, String[] args) {
        int id = -1;
        if (!args[0].matches("[^0-9]")) {
            id = Integer.parseInt(args[0]);
        } else {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "The id must be a number.");
            return;
        }
        Player playerSender = (Player) sender;
        Job j = JobManager.getJob(id);
        if (j == null) {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "That id does not exist.");
            return;
        }
        if (j.getPlayer().getUniqueId().equals(playerSender.getUniqueId())) {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "You cannot claim a job you created.");
            return;
        } else if (BusinessManager.isOwner(playerSender.getUniqueId())
                || EmployeeManager.isEmployee(playerSender.getUniqueId())) {
            JobClaimedEvent event = new JobClaimedEvent(j, playerSender);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            sender.sendMessage(
                    BusinessCore.ERROR_PREFIX + "You cannot claim a job if you are not a part of a business.");
        }
    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.job.list", usage = "/job list [mine|open] [page number @Optional]")
    public void list(CommandSender sender, String[] args) {
        Job[] jobs;
        int page = 1;
        if (args.length > 1) {
            if (!args[1].matches("[^0-9]"))
                page = Integer.parseInt(args[1]);
        }
        if (args[0].equalsIgnoreCase("mine")) {
            jobs = JobManager.getPlayerJobs((Player) sender);
            if (page * 5 > jobs.length) {
                while (page * 5 > jobs.length && page > 0)
                    page--;
            }
            int fromIndex = page == 1 ? 0 : page * 5;
            int toIndex = fromIndex + 4;
            if (toIndex >= jobs.length)
                while (toIndex >= jobs.length)
                    toIndex--;
            StringBuilder sb = new StringBuilder();
            for (int i = fromIndex; i <= toIndex; i++)
                sb.append(ChatColor.GRAY + "[" + ChatColor.AQUA + jobs[i].getID() + ChatColor.GRAY + "] "
                        + ChatColor.WHITE + jobs[i].getDescription() + "|");
            sender.sendMessage(sb.toString().split("|"));
        } else if (args[0].equalsIgnoreCase("open")) {
            jobs = JobManager.getOpenJobs();
            if (page * 5 > jobs.length) {
                while (page * 5 > jobs.length && page > 0)
                    page--;
            }
            int fromIndex = page == 1 ? 0 : page * 5;
            int toIndex = fromIndex + 4;
            if (toIndex >= jobs.length)
                while (toIndex >= jobs.length)
                    toIndex--;
            StringBuilder sb = new StringBuilder();
            for (int i = fromIndex; i <= toIndex; i++)
                sb.append(ChatColor.GRAY + "[" + ChatColor.AQUA + jobs[i].getID() + ChatColor.GRAY + "] "
                        + ChatColor.WHITE + jobs[i].getDescription() + "|");
            sender.sendMessage(sb.toString().split("|"));
        } else {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "/job list [\"mine\"|\"open\"] [page number @Optional]");
        }
    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.job.complete", usage = "/job complete [id]")
    public void complete(CommandSender sender, String[] args) {
        int id = 0;
        if (!args[0].matches("[^0-9]")) {
            id = Integer.parseInt(args[0]);
        } else {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "The id must be a number.");
            return;
        }
        Job j = JobManager.getJob(id);
        if (j == null) {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "That id does not exist.");
            return;
        }
        Player playerSender = (Player) sender;
        if (j.getPlayer().getUniqueId().equals(playerSender.getUniqueId())) {
            JobCompletedEvent event = new JobCompletedEvent(j);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            sender.sendMessage(
                    BusinessCore.ERROR_PREFIX + "You need to be the player you created the job to say it is complete.");
        }
    }

    @Subcommand(consoleUse = false, permission = "businesscore.job.current", usage = "/job current")
    public void current(CommandSender sender, String[] args) {
        Player playerSender = (Player) sender;
        if (JobManager.hasClaimedJob(playerSender.getUniqueId())) {
            Job j = JobManager.getClaimedJob(playerSender.getUniqueId());
            String[] messages = new String[] {
                    ChatColor.RED + "(=================[" + ChatColor.DARK_RED + j.getID() + ChatColor.RED
                            + "]=================)",
                    ChatColor.DARK_RED + "[Created by] " + ChatColor.RED + j.getPlayer().getName(),
                    ChatColor.DARK_RED + "[Description] " + ChatColor.RED + j.getDescription(),
                    ChatColor.DARK_RED + "[Location]" + ChatColor.RED + j.getLocation().getWorld().getName() + " "
                            + j.getLocation().getX() + "," + j.getLocation().getY() + "," + j.getLocation().getY(),
                    ChatColor.DARK_RED + "[Payment]" + ChatColor.RED
                            + BusinessCore.getInstance().getEconomy().format(j.getPayment())};
            sender.sendMessage(messages);
        } else {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "You haven't claimed any jobs.");
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.job.info", usage = "/job info [<id>]")
    public void info(CommandSender sender, String[] args) {
        int id = 0;
        if (!args[0].matches("[^0-9]"))
            id = Integer.parseInt(args[0]);
        else {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "The id must be a number");
            return;
        }
        Job j = JobManager.getJob(id);
        String[] messages = new String[] {
                ChatColor.RED + "(=================[" + ChatColor.DARK_RED + j.getID() + ChatColor.RED
                        + "]=================)",
                ChatColor.DARK_RED + "[Created by] " + ChatColor.RED + j.getPlayer().getName(),
                ChatColor.DARK_RED + "[Description] " + ChatColor.RED + j.getDescription(),
                ChatColor.DARK_RED + "[Location]" + ChatColor.RED + j.getLocation().getWorld().getName() + " "
                        + j.getLocation().getX() + "," + j.getLocation().getY() + "," + j.getLocation().getY(),
                ChatColor.DARK_RED + "[Payment]" + ChatColor.RED
                        + BusinessCore.getInstance().getEconomy().format(j.getPayment()) };
        sender.sendMessage(messages);
    }

    @Subcommand(consoleUse = false, permission = "business.job.tp", usage = "/job tp [id @Optional]")
    public void tp(CommandSender sender, String[] args) {
        Player playerSender = (Player) sender;
        Job j = null;
        if (args.length > 0) {
            int id = 0;
            if (!args[0].matches("[^0-9]"))
                id = Integer.parseInt(args[0]);
            else {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "The id must be a number.");
                return;
            }
            j = JobManager.getJob(id);
        }
        if (j == null)
            j = JobManager.getClaimedJob(playerSender.getUniqueId());
        if (j == null) {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "Could not find a valid job to tp to.");
            return;
        }
        playerSender.teleport(j.getLocation(), TeleportCause.COMMAND);
    }
}