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
import me.beastman3226.bc.util.Message;
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
            new Message("errors.not_a_number", sender).sendMessage();
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
            new Message("errors.not_a_number", sender).sendMessage();
            return;
        }
        Player playerSender = (Player) sender;
        Job j = JobManager.getJob(id);
        if (j == null) {
            new Message("errors.job_not_found", sender).sendMessage();
            return;
        }
        if (j.getPlayer().getUniqueId().equals(playerSender.getUniqueId())) {
            new Message("errors.self_created", sender).sendMessage();
            return;
        } else if (BusinessManager.isOwner(playerSender.getUniqueId())
                || EmployeeManager.isEmployee(playerSender.getUniqueId())) {
            JobClaimedEvent event = new JobClaimedEvent(j, playerSender);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            new Message("errors.not_an_employee", sender).sendMessage();
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
            new Message("job.list.header", sender).sendMessage();
            for (int i = fromIndex; i <= toIndex; i++)
                new Message("job.list.format", sender).setJob(jobs[i]).sendMessage();
            new Message("job.list.footer", sender).setOther(page, (int) (jobs.length / (float) 5 + .5));
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
            new Message("job.list.header", sender).sendMessage();
            for (int i = fromIndex; i <= toIndex; i++)
                new Message("job.list.format", sender).setJob(jobs[i]).sendMessage();
            new Message("job.list.footer", sender).setOther(page, (int) (jobs.length / (float) 5 + .5));
        } else {
            new Message("errors.usage", sender).setOther("/job list [\"mine\"|\"open\"] [page number @Optional]").sendMessage();
        }
    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.job.complete", usage = "/job complete [id]")
    public void complete(CommandSender sender, String[] args) {
        int id = 0;
        if (!args[0].matches("[^0-9]")) {
            id = Integer.parseInt(args[0]);
        } else {
            new Message("errors.not_a_number", sender).sendMessage();
            return;
        }
        Job j = JobManager.getJob(id);
        if (j == null) {
            new Message("errors.job_not_found", sender).sendMessage();
            return;
        }
        Player playerSender = (Player) sender;
        if (j.getPlayer().getUniqueId().equals(playerSender.getUniqueId())) {
            JobCompletedEvent event = new JobCompletedEvent(j);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            new Message("errors.incorrect_player", sender).sendMessage();
        }
    }

    @Subcommand(consoleUse = false, permission = "businesscore.job.current", usage = "/job current")
    public void current(CommandSender sender, String[] args) {
        Player playerSender = (Player) sender;
        if (JobManager.hasClaimedJob(playerSender.getUniqueId())) {
            Job j = JobManager.getClaimedJob(playerSender.getUniqueId());
            new Message("job.current", sender).setJob(j).sendMessage();
        } else {
            new Message("errors.no_claimed_jobs", sender).sendMessage();
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.job.info", usage = "/job info [<id>]")
    public void info(CommandSender sender, String[] args) {
        int id = 0;
        if (!args[0].matches("[^0-9]"))
            id = Integer.parseInt(args[0]);
        else {
            new Message("errors.not_a_number", sender).sendMessage();
            return;
        }
        Job j = JobManager.getJob(id);
        if (j != null) {
            new Message("job.current", sender).setJob(j).sendMessage();
        } else {
            new Message("errors.job_not_found", sender).sendMessage();
        }
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
                new Message("errors.not_a_number", sender).sendMessage();
                return;
            }
            j = JobManager.getJob(id);
        }
        if (j == null)
            j = JobManager.getClaimedJob(playerSender.getUniqueId());
        if (j == null) {
            new Message("errors.job_not_found", sender).sendMessage();
            return;
        }
        playerSender.teleport(j.getLocation(), TeleportCause.COMMAND);
    }
}