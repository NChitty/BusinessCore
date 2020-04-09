package me.beastman3226.bc.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.event.business.BusinessBalanceChangeEvent;
import me.beastman3226.bc.event.business.BusinessClosedEvent;
import me.beastman3226.bc.event.business.BusinessCreatedEvent;
import me.beastman3226.bc.event.business.BusinessFiredEmployeeEvent;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.Scheduler;

public class BusinessCommand extends ICommand {

    public BusinessCommand() {
        super("business", "businesscore.business", true);
    }

    @Override
    public void execute(CommandSender sender) {

    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.business.start", usage = "/business start [name]")
    public void start(CommandSender sender, String[] args) {
        Player playerSender = (Player) sender;
        if (!BusinessManager.isOwner(playerSender.getUniqueId().toString())) {
            String businessName = this.parseArgs(args);
            int id = BusinessManager.getNewID(businessName);
            if (id == -1) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "That name is too similar to another business's name.");
                return;
            }
            Business b = BusinessManager.createBusiness(
                    new Business.Builder(id).name(businessName).owner(playerSender.getUniqueId().toString()));
            BusinessCreatedEvent event = new BusinessCreatedEvent(b);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "You already own a business!");
        }
    }

    @Subcommand(permission = "businesscore.business.close", usage = "/business close <id>")
    public void close(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (BusinessManager.isOwner(playerSender.getUniqueId().toString())) {
                BusinessClosedEvent event = new BusinessClosedEvent(
                        BusinessManager.getBusiness(playerSender.getUniqueId().toString()));
                event.setSource(sender);
                Bukkit.getPluginManager().callEvent(event);
            } else {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "You are not an owner!");
            }
        } else {
            if (args.length < 1) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "/business close <id|owner name>");
            } else {
                int id = -1;
                if (!args[0].matches("[^0-9]+")) {
                    id = Integer.parseInt(args[0]);
                    if (BusinessManager.isID(id)) {
                        Business b = BusinessManager.getBusiness(id);
                        BusinessClosedEvent event = new BusinessClosedEvent(b);
                        event.setSource(sender);
                        Bukkit.getPluginManager().callEvent(event);
                    } else {
                        sender.sendMessage(BusinessCore.ERROR_PREFIX + "There is no such business with that ID.");
                    }
                } else {
                    sender.sendMessage(BusinessCore.ERROR_PREFIX + "/business close <id>");
                }
            }
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.business.withdraw", usage = "/business withdraw <id>|[amount] <amount>")
    public void withdraw(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (BusinessManager.isOwner(playerSender.getUniqueId().toString())) {
                if (args.length > 1) {
                    sender.sendMessage(BusinessCore.ERROR_PREFIX + "You only need to provide the amount.");
                    return;
                }
                if (args[0].matches("[^0-9.0-9]")) {
                    sender.sendMessage(BusinessCore.ERROR_PREFIX + "The amount must be a number");
                } else {
                    Business b = BusinessManager.getBusiness(playerSender.getUniqueId().toString());
                    double amount = Double.parseDouble(args[0]);
                    BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, -amount);
                    event.setSource(sender);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "You must be an owner to execute this command.");
            }
        } else {
            if (args.length == 1) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "You must supply an amount.");
            } else if (args[0].matches("[^0-9]+") || args[1].matches("[^0-9.0-9]")) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX
                        + "The business ID and amount need to be a number needs to be a number.");
            } else {
                int id = Integer.parseInt(args[0]);
                Business b = BusinessManager.getBusiness(id);
                double amount = Double.parseDouble(args[1]);
                BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, -amount);
                event.setSource(sender);
                Bukkit.getPluginManager().callEvent(event);
            }
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.business.deposit", usage = "/business deposit <id>[amount] <amount>")
    public void deposit(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (BusinessManager.isOwner(playerSender.getUniqueId().toString())) {
                if (args.length > 1) {
                    sender.sendMessage(BusinessCore.ERROR_PREFIX + "You only need to provide the amount.");
                    return;
                }
                if (args[0].matches("[^0-9.0-9]")) {
                    sender.sendMessage(BusinessCore.ERROR_PREFIX + "The amount must be a number");
                } else {
                    Business b = BusinessManager.getBusiness(playerSender.getUniqueId().toString());
                    double amount = Double.parseDouble(args[0]);
                    BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, amount);
                    event.setSource(sender);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "You must be an owner to execute this command.");
            }
        } else {
            if (args.length == 1) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "You must supply an amount.");
            } else if (args[0].matches("[^0-9]+") || args[1].matches("[^0-9.0-9]")) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX
                        + "The business ID and amount need to be a number needs to be a number.");
            } else {
                int id = Integer.parseInt(args[0]);
                Business b = BusinessManager.getBusiness(id);
                double amount = Double.parseDouble(args[1]);
                BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, amount);
                event.setSource(sender);
                Bukkit.getPluginManager().callEvent(event);
            }
        }
    }

    @Subcommand(permission = "businesscore.business.balance", usage = "/business balance <id>")
    public void balance(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (BusinessManager.isOwner(playerSender.getUniqueId().toString())) {
                Business b = BusinessManager.getBusiness((playerSender.getUniqueId().toString()));
                sender.sendMessage(BusinessCore.NOMINAL_PREFIX + b.getBalance()
                        + BusinessCore.getInstance().getEconomy().currencyNameSingular());

            } else {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "You must be an owner to execute this command.");
            }
        } else {
            if (args.length == 0) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "Make sure you supply a business ID with this command.");
            } else if (args[0].matches("[^0-9]+")) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "The business ID needs to be a number.");
            } else {
                int id = Integer.parseInt(args[0]);
                Business b = BusinessManager.getBusiness(id);
                sender.sendMessage(BusinessCore.NOMINAL_PREFIX + b.getBalance()
                        + BusinessCore.getInstance().getEconomy().currencyNameSingular());
            }
        }
    }

    @Subcommand(permission = "businesscore.business.info", usage = "/business info <id>")
    public void info(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (BusinessManager.isOwner((playerSender.getUniqueId().toString()))) {
                Business b = BusinessManager.getBusiness((playerSender.getUniqueId().toString()));
                String header = String.format(ChatColor.DARK_GREEN + "|%5s|%30s|%13s|", "ID", "Business Name",
                        "Balance");
                String info = String.format(ChatColor.GREEN + "|%5d|%30s|%13.2f|", b.getID(), b.getName(),
                        b.getBalance());
                String splitter = ChatColor.DARK_GREEN + "==========================================";
                String employees = ChatColor.GREEN + Arrays.toString(b.getEmployees().toArray());
                sender.sendMessage(new String[] { header, info, splitter, employees });

            } else if (args.length == 0) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX
                        + "If you are looking for a list of businesses try /business list [sort] [page]");
            }
        } else {
            if (args.length == 0) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX
                        + "If you are looking for a list of businesses try /business list [sort] [page] or just /business list for ways to sort the list.");
            } else if (args[0].matches("[^0-9]+")) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "The business ID needs to be a number.");
            } else {
                int id = Integer.parseInt(args[0]);
                Business b = BusinessManager.getBusiness(id);
                String header = String.format(ChatColor.DARK_GREEN + "|%5s|%30s|%13s|", "ID", "Business Name",
                        "Balance");
                String info = String.format(ChatColor.GREEN + "|%5d|%30s|%10.2f|", b.getID(), b.getName(),
                        b.getBalance());
                String splitter = ChatColor.DARK_GREEN + "==========================================";
                String employees = ChatColor.GREEN + Arrays.toString(b.getEmployees().toArray());
                sender.sendMessage(new String[] { header, info, splitter, employees });
            }
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.business.list", usage = "/business list [<\"id\"|\"balance\"|\"name\">] [<page>]")
    public void list(CommandSender sender, String[] args) {
        int page = 1;
        int pagesOutOf = BusinessManager.getBusinessList().size() / 5;
        int fromIndex = 0;
        int toIndex = 4;
        if (args.length > 1) {
            if (!args[1].matches("[^0-9]+"))
                page = Integer.parseInt(args[1]);
            else
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "The page number needs to be a number.");
        }
        if (page > 1) {
            fromIndex = page <= pagesOutOf ? page * 5 : pagesOutOf * 5;
            toIndex = page <= pagesOutOf ? page * 5 + 4 : BusinessManager.getBusinessList().size();
        }
        switch (args[0].toLowerCase()) {
            case "id":
                Business[] businesses = BusinessManager.sortById().subList(fromIndex, toIndex >=  BusinessManager.getBusinessList().size() ? BusinessManager.getBusinessList().size()- 1 : toIndex).toArray(new Business[0]);
                sender.sendMessage(ChatColor.DARK_GREEN + "|=========Businesses by ID==========|");
                for (Business b : businesses) {
                    String message = String.format(ChatColor.GREEN + "[%d] " + ChatColor.WHITE + "%s", b.getID(),
                            b.getName());
                    sender.sendMessage(message);
                }

                sender.sendMessage(
                        ChatColor.DARK_GREEN + "|=========Page [" + page + " of " + pagesOutOf + "] ==========|");
                break;
            case "balance":
                businesses = BusinessManager.sortByBalance().subList(fromIndex, toIndex).toArray(new Business[0]);
                sender.sendMessage(ChatColor.DARK_GREEN + "|=========Top Performing Businesses==========|");
                for (Business b : businesses) {
                    String message = String.format(ChatColor.GREEN + "[%10d] [%12.2lf] " + ChatColor.WHITE + " %s",
                            b.getID(), b.getBalance(), b.getName());
                    sender.sendMessage(message);
                }
                sender.sendMessage(
                        ChatColor.DARK_GREEN + "|=========Page [" + page + " of " + pagesOutOf + "] ==========|");
                break;
            case "name":
                businesses = BusinessManager.sortByName().subList(fromIndex, toIndex).toArray(new Business[0]);
                sender.sendMessage(ChatColor.DARK_GREEN + "|=========Businesses by ID==========|");
                for (Business b : businesses) {
                    String message = String.format(ChatColor.GREEN + "[%d] " + ChatColor.WHITE + "%s", b.getID(),
                            b.getName());
                    sender.sendMessage(message);
                }

                sender.sendMessage(
                        ChatColor.DARK_GREEN + "|=========Page [" + page + " of " + pagesOutOf + "] ==========|");
                break;
            default:
                sender.sendMessage(
                        BusinessCore.ERROR_PREFIX + "/business list [<\"id\"|\"balance\"|\"name\">] [<page>]");
                break;
        }
    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.business.employee", usage = "/business employee [hire|fire|list] [id|player name]")
    public void employee(CommandSender sender, String[] args) {
        Player playerSender = (Player) sender;
        if (BusinessManager.isOwner(playerSender.getUniqueId().toString())) {
            Business b = BusinessManager.getBusiness(playerSender.getUniqueId().toString());
            Player player = null;
            if (args.length > 1) {
                if (args[1].matches("[^0-9]+")) {
                    player = Bukkit.getPlayer(args[1]);
                } else {
                    int id = Integer.parseInt(args[1]);
                    if (EmployeeManager.isEmployeeFor(b, id))
                        player = Bukkit.getPlayer(EmployeeManager.getEmployee(b, id).getUniqueId());
                }
            }
            if (player == null && !args[0].toLowerCase().equals("list")) {
                sender.sendMessage(BusinessCore.ERROR_PREFIX + "Could not find the specified player.");
                return;
            }
            switch (args[0].toLowerCase()) {
                case "hire":
                    if (EmployeeManager.isEmployee(player.getUniqueId()) || BusinessManager.isOwner(((Player) sender).getUniqueId().toString())) {
                        sender.sendMessage(
                                BusinessCore.ERROR_PREFIX + "That player is already an employee of a business.");
                    } else {
                        sender.sendMessage(BusinessCore.OTHER_PREFIX + "An offer to join your business has been sent.");
                        player.sendMessage(String.format("%sYou have been offered a job at %s by %s",
                                BusinessCore.OTHER_PREFIX,
                                b.getName(), sender.getName()));
                        EmployeeManager.getPendingPlayers().put(player,
                                b.getID());
                        Scheduler.runAcceptance();
                    }
                    break;
                case "fire":
                    if (EmployeeManager.isEmployeeFor(b, player.getUniqueId())) {
                        Employee emp = EmployeeManager.getEmployee(player.getName());
                        BusinessFiredEmployeeEvent event = new BusinessFiredEmployeeEvent(b, emp);
                        Bukkit.getPluginManager().callEvent(event);
                    } else {
                        sender.sendMessage(
                                String.format("%sThat player is not one of your employees.", BusinessCore.ERROR_PREFIX));
                    }
                    break;
                case "list":
                    final String format = "%s|%5d|%20s|%4d|%7d|\n";
                    StringBuilder sb = new StringBuilder(
                            String.format("%s|%5s|%20s|%4s|%7s|\n", ChatColor.GOLD, "ID", "Name", "Jobs", "Current"));
                    for (Employee e : b.getEmployees()) {
                        sb.append(String.format(format, ChatColor.GRAY, e.getID(), e.getName(), e.getCompletedJobs(), e.getCurrentJob()));
                    }
                    sender.sendMessage(sb.toString().split("\n"));
                    break;
                default:
                    sender.sendMessage(BusinessCore.ERROR_PREFIX + "Try \"hire\",\"fire\", or \"list\"");
                    break;
            }
        } else {
            sender.sendMessage(BusinessCore.ERROR_PREFIX + "You must be an owner to run this command.");
        }
    }
}