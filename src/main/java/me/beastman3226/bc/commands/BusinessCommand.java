package me.beastman3226.bc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.event.business.BusinessClosedEvent;
import me.beastman3226.bc.event.business.BusinessCreatedEvent;

public class BusinessCommand extends ICommand {

    public BusinessCommand() {
        super("business", "businesscore.business", true);
    }

    @Override
    public void execute(CommandSender sender) {

    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.business.start", usage = "/business start [name]")
    public void start(CommandSender sender, String[] args) {
        if (!BusinessManager.isOwner(sender.getName())) {
            String businessName = this.parseArgs(args);
            int id = BusinessManager.getNewID(businessName);
            if (id == -1) {
                sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR)
                        + "That name is too similar to another business's name.");
                return;
            }
            Business b = BusinessManager
                    .createBusiness(new Business.Builder(id).name(businessName).owner(sender.getName()));
            BusinessCreatedEvent event = new BusinessCreatedEvent(b);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                sender.sendMessage(BusinessCore.getPrefix(BusinessCore.WORKING) + "You founded " + businessName + "!");
            }
        } else {
            sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR) + "You already own a business!");
        }
    }

    @Subcommand(permission = "businesscore.business.close", usage = "/business close <id|owner name>")
    public void close(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (BusinessManager.isOwner(sender.getName())) {
                BusinessClosedEvent event = new BusinessClosedEvent(BusinessManager.getBusiness(sender.getName()));
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    BusinessManager.closeBusiness(BusinessManager.getBusiness(sender.getName()));
                    sender.sendMessage(BusinessCore.getPrefix(BusinessCore.NOMINAL) + "Your business has been closed.");
                }
            } else {
                sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR) + "You are not an owner!");
            }
        } else {
            if (args.length < 1) {
                sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR) + "/business close <id|owner name>");
            } else {
                int id = -1;
                if (args[0].matches("[0-9]")) {
                    id = Integer.parseInt(args[0]);
                    if (BusinessManager.isID(id)) {
                        Business b = BusinessManager.getBusiness(id);
                        BusinessClosedEvent event = new BusinessClosedEvent(b);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            BusinessManager.closeBusiness(b);
                            sender.sendMessage(
                                    BusinessCore.getPrefix(BusinessCore.NOMINAL) + b.getName() + " has been closed.");
                        }
                    } else {
                        sender.sendMessage(
                                BusinessCore.getPrefix(BusinessCore.ERROR) + "There is no such business with that ID.");
                    }
                } else if (BusinessManager.isOwner(args[0])) {
                    Business b = BusinessManager.getBusiness(args[0]);
                    BusinessClosedEvent event = new BusinessClosedEvent(b);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        BusinessManager.closeBusiness(b);
                        sender.sendMessage(
                                BusinessCore.getPrefix(BusinessCore.NOMINAL) + b.getName() + " has been closed.");
                    }
                } else {
                    sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR) + "/business close <id|owner name>");
                }
            }
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.business.withdraw", usage = "/business withdraw <id>|[amount] <amount>")
    public void withdraw(CommandSender sender, String[] args) {

    }

    @Subcommand(minArgs = 1, permission = "businesscore.business.deposit", usage = "/business deposit <id>[amount] <amount>")
    public void deposit(CommandSender sender, String[] args) {

    }

    @Subcommand(permission = "businesscore.business.balance", usage = "/business balance <id>")
    public void balance(CommandSender sender, String[] args) {

    }

    @Subcommand(permission = "businesscore.business.info", usage = "/business info [<id>]")
    public void info(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (BusinessManager.isOwner(sender.getName())) {

            } else if (args.length == 0) {
                sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR)
                        + "If you are looking for a list of businesses try /business list [sort] [page]");
            }
        } else {
            if (args.length == 0) {
                sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR)
                        + "If you are looking for a list of businesses try /business list [sort] [page] or just /business list for ways to sort the list.");
            }
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.business.list", usage = "/business list [<\"id\"|\"balance\"|\"name\">] [<page>]")
    public void list(CommandSender sender, String[] args) {
        int page = 1;
        int pagesOutOf = BusinessManager.businessList.size() / 5;
        int fromIndex = 0;
        int toIndex = 4;
        if (args.length > 1) {
            if (args[1].matches("[0-9]"))
                page = Integer.parseInt(args[1]);
        }
        if (page > 1) {
            fromIndex = page * 5;
            toIndex = page * 5 + 4;
        }
        switch (args[0].toLowerCase()) {
            case "id":
                Business[] businesses = BusinessManager.sortById().subList(fromIndex, toIndex).toArray(new Business[0]);
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
                sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR)
                        + "/business list [<\"id\"|\"balance\"|\"name\">] [<page>]");
                break;
        }
    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.business.employee", usage = "/business employee [hire|fire|list]")
    public void employee(CommandSender sender, String[] args) {

    }
}