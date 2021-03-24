package me.nchitty.bc.commands;

import me.nchitty.bc.util.Message;
import me.nchitty.bc.util.Paginator;
import me.nchitty.bc.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nchitty.bc.business.Business;
import me.nchitty.bc.business.Business.BusinessManager;
import me.nchitty.bc.event.business.BusinessBalanceChangeEvent;
import me.nchitty.bc.event.business.BusinessClosedEvent;
import me.nchitty.bc.event.business.BusinessCreatedEvent;
import me.nchitty.bc.event.business.BusinessFiredEmployeeEvent;
import me.nchitty.bc.player.Employee;
import me.nchitty.bc.player.Employee.EmployeeManager;

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
        if (!Business.BusinessManager.isOwner(playerSender)) {
            String businessName = this.parseArgs(args);
            int id = Business.BusinessManager.getNewID(businessName);
            // TODO: Change the way IDs are assigned
            if (id == -1) {
                Message message = new Message("errors.name_too_similar").setRecipient(playerSender);
                message.sendMessage();
                return;
            }
            Business b = Business.BusinessManager.createBusiness(
                    new Business.Builder(id)
                            .name(businessName)
                            .owner(playerSender)
            );
            BusinessCreatedEvent event = new BusinessCreatedEvent(b);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            Message message = new Message("errors.already_own_business").setRecipient(playerSender)
                    .setBusiness(
                            Business.BusinessManager.getBusiness(playerSender)
                    );
            message.sendMessage();
        }
    }

    @Subcommand(permission = "businesscore.business.close", usage = "/business close <id>")
    public void close(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (Business.BusinessManager.isOwner(playerSender)) {

                BusinessClosedEvent event = new BusinessClosedEvent(
                        Business.BusinessManager.getBusiness(playerSender)
                );

                event.setSource(sender);
                Bukkit.getPluginManager().callEvent(event);
            } else {
                Message message = new Message("errors.not_an_owner").setRecipient(playerSender);
                message.sendMessage();
            }
        } else {
            if (args.length < 1) {
                sender.sendMessage("/business close <id|owner name>");
            } else {
                int id = -1;
                if (!args[0].matches("[^0-9]+")) {
                    id = Integer.parseInt(args[0]);
                    if (Business.BusinessManager.isID(id)) {
                        Business b = Business.BusinessManager.getBusiness(id);
                        BusinessClosedEvent event = new BusinessClosedEvent(b);
                        event.setSource(sender);
                        Bukkit.getPluginManager().callEvent(event);
                    } else {
                        Message error = new Message("errors.invalid_id", true, null, null);
                        error.sendMessage();
                    }
                } else {
                    sender.sendMessage("/business close <id>");
                }
            }
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.business.withdraw", usage = "/business withdraw <id>|[amount] <amount>")
    public void withdraw(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (Business.BusinessManager.isOwner(playerSender)) {
                if (args[0].matches("[^0-9.0-9]")) {
                    Message message = new Message("errors.not_a_number").setRecipient(playerSender);
                    message.sendMessage();
                } else {
                    Business b = Business.BusinessManager.getBusiness(playerSender);
                    double amount = Double.parseDouble(args[0]);
                    BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, -amount);
                    event.setSource(sender);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else {
                Message message = new Message("errors.not_an_owner").setRecipient(playerSender)
                        .setBusiness(Business.BusinessManager.getBusiness(playerSender));
                message.sendMessage();
            }
        } else {
            if (args.length == 1) {
                Message error = new Message("errors.minimum_arguments", true, null, null);
                error.sendMessage();
            } else if (args[0].matches("[^0-9]+") || args[1].matches("[^0-9.0-9]")) {
                Message error = new Message("errors.not_a_number", true, null, null);
                error.sendMessage();
            } else {
                int id = Integer.parseInt(args[0]);
                Business b = Business.BusinessManager.getBusiness(id);
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
            if (Business.BusinessManager.isOwner(playerSender)) {
                if (args[0].matches("[^0-9.0-9]")) {
                    Message message = new Message("errors.not_a_number").setRecipient(playerSender);
                    message.sendMessage();
                } else {
                    Business b = Business.BusinessManager.getBusiness(playerSender);
                    double amount = Double.parseDouble(args[0]);
                    BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, amount);
                    event.setSource(sender);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else {
                Message message = new Message("errors.not_an_owner").setRecipient(playerSender)
                        .setBusiness(
                                Business.BusinessManager.getBusiness(playerSender)
                        );
                message.sendMessage();
            }
        } else {
            if (args.length == 1) {
                Message error = new Message("errors.minimum_arguments", true, null, null);
                error.sendMessage();
            } else if (args[0].matches("[^0-9]+") || args[1].matches("[^0-9.0-9]")) {
                Message error = new Message("errors.not_a_number", true, null, null);
                error.sendMessage();
            } else {
                int id = Integer.parseInt(args[0]);
                Business b = Business.BusinessManager.getBusiness(id);
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
            if (Business.BusinessManager.isOwner(playerSender)) {
                Business b = Business.BusinessManager.getBusiness((playerSender));
                Message message = new Message("business.balance").setRecipient(playerSender).setBusiness(b);
                message.sendMessage();
            } else {
                Message message = new Message("errors.not_an_owner").setRecipient(playerSender)
                        .setBusiness(Business.BusinessManager.getBusiness(playerSender));
                message.sendMessage();
            }
        } else {
            if (args.length == 0) {
                Message error = new Message("errors.minimum_arguments", true, null, null);
                error.sendMessage();
            } else if (args[0].matches("[^0-9]+")) {
                Message error = new Message("errors.not_a_number", true, null, null);
                error.sendMessage();
            } else {
                int id = Integer.parseInt(args[0]);
                Business b = Business.BusinessManager.getBusiness(id);
                Message balance = new Message("business.balance", true, null, null).setBusiness(b);
                balance.sendMessage();
            }
        }
    }

    @Subcommand(permission = "businesscore.business.info", usage = "/business info <id>")
    public void info(CommandSender sender, String[] args) {
        Business b = null;
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            b = Business.BusinessManager.getBusiness(playerSender);
            b = b == null ? EmployeeManager.getEmployee(playerSender).getBusiness() : b;
            if (b != null){
                Message message = new Message("business.info").setRecipient(playerSender).setBusiness(b);
                message.sendMessage();
            } else if (args.length == 0) {
                Message message = new Message("errors.try_list").setRecipient(playerSender);
                message.sendMessage();
            }
        } else {
            if (args.length == 0) {
                Message message = new Message("errors.try_list", true, null, null);
                message.sendMessage();
            } else if (args[0].matches("[^0-9]+")) {
                Message error = new Message("errors.not_a_number", true, null, null);
                error.sendMessage();
            } else {
                int id = Integer.parseInt(args[0]);
                b = BusinessManager.getBusiness(id);
                Message message = new Message("business.info", true, null, null).setBusiness(b);
                message.sendMessage();
            }
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.business.list", usage = "/business list [<\"id\"|\"balance\"|\"name\">] [<page>]")
    public void list(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length > 1) {
            if (!args[1].matches("[^0-9]"))
                page = Integer.parseInt(args[1]);
        }
        switch (args[0].toLowerCase()) {
            case "id":

                new Paginator<Business>("business.list.by_id", Business.BusinessManager.sortById(), sender)
                        .page(page)
                        .forEach(Message::sendMessage);

                break;
            case "balance":

                new Paginator<Business>("business.list.by_balance", Business.BusinessManager.sortByBalance(), sender)
                        .page(page)
                        .forEach(Message::sendMessage);

                break;
            case "name":

                new Paginator<Business>("business.list.by_name", Business.BusinessManager.sortByName(), sender)
                        .page(page)
                        .forEach(Message::sendMessage);

                break;
            default:
                new Message("errors.usage", sender).setOther("/business list [<\"id\"|\"balance\"|\"name\">] [<page>]").sendMessage();
                break;
        }
    }

    @Subcommand(consoleUse = false, minArgs = 1, permission = "businesscore.business.employee", usage = "/business employee [hire|fire|list] [id|player name]")
    public void employee(CommandSender sender, String[] args) {
        Player playerSender = (Player) sender;
        Business b = null;
        if ((b = Business.BusinessManager.getBusiness(playerSender)) != null) {
            // TODO: Get player logic
            switch (args[0].toLowerCase()) {
                case "hire":
                    if (EmployeeManager.isEmployee(player)
                            || Business.BusinessManager.isOwner(player)) {
                        Message message = new Message("errors.already_a_worker").setRecipient(playerSender);
                        message.sendMessage();
                    } else {
                        Message offerSent = new Message("business.employee.hire.sent_offer").setCause(player)
                                .setRecipient(playerSender);
                        offerSent.sendMessage();
                        Message offerReceived = new Message("business.employee.hire.offer").setCause(playerSender)
                                .setRecipient(player).setBusiness(b);
                        offerReceived.sendMessage();
                        EmployeeManager.getPendingPlayers().put(player, b.getID());
                        Scheduler.runAcceptance(playerSender);
                    }
                    break;
                case "fire":
                    if (EmployeeManager.isEmployeeFor(b, player)) {
                        Employee emp = EmployeeManager.getEmployee(player);
                        BusinessFiredEmployeeEvent event = new BusinessFiredEmployeeEvent(b, emp);
                        Bukkit.getPluginManager().callEvent(event);
                    } else {
                        Message message = new Message("errors.not_business_employee").setRecipient(playerSender)
                                .setBusiness(b);
                        message.sendMessage();
                    }
                    break;
                case "list":
                    int page = 1;
                    if(args.length > 1)
                        if (!args[1].matches("[^0-9]"))
                            page = Integer.parseInt(args[1]);

                    new Paginator<Employee>("business.employee.list", b.getEmployees(), sender)
                        .page(page)
                        .forEach(Message::sendMessage);

                    break;
                default:
                    new Message("errors.usage", sender).setOther("Try \"hire\",\"fire\", or \"list\"").sendMessage();
                    break;
            }
        } else {
            Message message = new Message("errors.not_an_owner").setRecipient(playerSender);
            message.sendMessage();
        }
    }

    @Subcommand
    public void help(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            String[] helpPage = {
                    ChatColor.BLUE + "/business start [name]: " + ChatColor.AQUA
                            + "Starts a business with the specified name and you as the owner.",
                    ChatColor.BLUE + "/business withdraw [amount]: " + ChatColor.AQUA
                            + "Withdraws the specified amount from your business's account.",
                    ChatColor.BLUE + "/business deposit [amount]: " + ChatColor.AQUA
                            + "Deposit the specified amount into your business's account.",
                    ChatColor.BLUE + "/business employee hire [playername]: " + ChatColor.AQUA
                            + "Send a job offer to the specified player.",
                    ChatColor.BLUE + "/business employee fire [playername|ID]: " + ChatColor.AQUA
                            + "Fire the player specified by name or their employee ID.",
                    ChatColor.BLUE + "/business employee list: " + ChatColor.AQUA
                            + "List information about your current employees.",
                    ChatColor.BLUE + "/business balance" + ChatColor.AQUA + "Get the balance of your business.",
                    ChatColor.BLUE + "/business close: " + ChatColor.AQUA
                            + "Close your business forever. Note you should withdraw everything first.",
                    ChatColor.BLUE + "/business info: " + ChatColor.AQUA
                            + "Get general information about your business.",
                    ChatColor.BLUE + "/business list [\"balance\"|\"id\"|\"name\"] [page #]: " + ChatColor.AQUA
                            + "Lists businesses sorted by the various methods." };
            sender.sendMessage(helpPage);
        } else {
            String[] helpPage = {
                    ChatColor.BLUE + "/business withdraw <id> <amount>: " + ChatColor.AQUA
                            + "Withdraws the specified amount from the business specified by id.",
                    ChatColor.BLUE + "/business deposit <id> <amount>: " + ChatColor.AQUA
                            + "Deposit the specified amount into the business specified by id.",
                    ChatColor.BLUE + "/business balance <id>" + ChatColor.AQUA + "Get the balance of the business.",
                    ChatColor.BLUE + "/business close <id>: " + ChatColor.AQUA + "Close the specified business.",
                    ChatColor.BLUE + "/business info <id>: " + ChatColor.AQUA
                            + "Get general information about your business.",
                    ChatColor.BLUE + "/business list [\"balance\"|\"id\"|\"name\"] [page #]: " + ChatColor.AQUA
                            + "Lists businesses sorted by the various methods." };
            sender.sendMessage(helpPage);
        }
    }

}