package me.nchitty.bc.commands;

import java.util.Arrays;
import java.util.List;

import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.business.Business;
import me.nchitty.bc.job.Job;
import me.nchitty.bc.job.Job.JobManager;
import me.nchitty.bc.util.Message;
import me.nchitty.bc.util.Paginator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.nchitty.bc.event.job.JobClaimedEvent;
import me.nchitty.bc.event.job.JobCompletedEvent;
import me.nchitty.bc.event.job.JobCreatedEvent;
import me.nchitty.bc.player.Employee.EmployeeManager;

public class JobCommand extends AbstractCommand {

    private JobCommand() {
        super(BusinessCore.getInstance(), "job", "businesscore.job", true);
    }

    public static AbstractCommand getInstance() {
        return instance == null ? instance = new JobCommand() : instance;
    }

    @Override
    public AbstractCommand getImplementation() {
        return getInstance();
    }

    @Override
    public boolean execute(CommandSender sender) {
        return true;
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
        if (j.isCreator(playerSender)) {
            new Message("errors.self_created", sender).sendMessage();
            return;
        } else if (Business.BusinessManager.isOwner(playerSender)
                || EmployeeManager.isEmployee(playerSender)) {
            JobClaimedEvent event = new JobClaimedEvent(j, playerSender);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            new Message("errors.not_an_employee", sender).sendMessage();
        }
    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.job.list", usage = "/job list [mine|open] [page number @Optional]")
    public void list(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length > 1)
            if (!args[1].matches("[^0-9]"))
                page = Integer.parseInt(args[1]);
        if (args[0].equalsIgnoreCase("mine") && sender instanceof Player) {

            List<Job> jobs = Arrays.asList(JobManager.getPlayerJobs((Player) sender));
            new Paginator<Job>("job.list", jobs, sender)
                    .page(page)
                    .forEach(Message::sendMessage);

        } else if (args[0].equalsIgnoreCase("open")) {

            List<Job> jobs = Arrays.asList(JobManager.getOpenJobs());
            new Paginator<Job>("job.list", jobs, sender)
                    .page(page)
                    .forEach(Message::sendMessage);

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
        if (j.isCreator(playerSender)) {
            JobCompletedEvent event = new JobCompletedEvent(j);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            new Message("errors.incorrect_player", sender).sendMessage();
        }
    }

    @Subcommand(consoleUse = false, permission = "businesscore.job.current", usage = "/job current")
    public void current(CommandSender sender, String[] args) {
        Player playerSender = (Player) sender;
        if (JobManager.hasClaimedJob(playerSender)) {
            Job j = JobManager.getClaimedJob(playerSender);
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
            j = JobManager.getClaimedJob(playerSender);
        if (j == null) {
            new Message("errors.job_not_found", sender).sendMessage();
            return;
        }
        playerSender.teleport(j.getLocation(), TeleportCause.COMMAND);
    }
}