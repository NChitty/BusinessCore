package me.beastman3226.bc.commands;

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
import me.beastman3226.bc.util.Message;
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
                Message message = new Message("errors.name_too_similar").setRecipient(playerSender);
                message.sendMessage();
                return;
            }
            Business b = BusinessManager.createBusiness(
                    new Business.Builder(id).name(businessName).owner(playerSender.getUniqueId().toString()));
            BusinessCreatedEvent event = new BusinessCreatedEvent(b);
            Bukkit.getPluginManager().callEvent(event);
        } else {
            Message message = new Message("errors.already_own_business").setRecipient(playerSender)
                    .setBusiness(BusinessManager.getBusiness(playerSender.getUniqueId()));
            message.sendMessage();
        }
    }

    @Subcommand(permission = "businesscore.business.close", usage = "/business close <id>")
    public void close(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (BusinessManager.isOwner(playerSender.getUniqueId().toString())) {
                BusinessClosedEvent event = new BusinessClosedEvent(
                        BusinessManager.getBusiness(playerSender.getUniqueId()));
                event.setSource(sender);
                Bukkit.getPluginManager().callEvent(event);
            } else {
                Message message = new Message("errors.not_an_owner").setRecipient(playerSender);
                message.sendMessage();
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
                        Message error = new Message("errors.invalid_id", true, null, null);
                        error.sendMessage();
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
                if (args[0].matches("[^0-9.0-9]")) {
                    Message message = new Message("errors.not_a_number").setRecipient(playerSender);
                    message.sendMessage();
                } else {
                    Business b = BusinessManager.getBusiness(playerSender.getUniqueId());
                    double amount = Double.parseDouble(args[0]);
                    BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, -amount);
                    event.setSource(sender);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else {
                Message message = new Message("errors.not_an_owner").setRecipient(playerSender)
                        .setBusiness(BusinessManager.getBusiness(playerSender.getUniqueId()));
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
                if (args[0].matches("[^0-9.0-9]")) {
                    Message message = new Message("errors.not_a_number").setRecipient(playerSender);
                    message.sendMessage();
                } else {
                    Business b = BusinessManager.getBusiness(playerSender.getUniqueId());
                    double amount = Double.parseDouble(args[0]);
                    BusinessBalanceChangeEvent event = new BusinessBalanceChangeEvent(b, amount);
                    event.setSource(sender);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else {
                Message message = new Message("errors.not_an_owner").setRecipient(playerSender)
                        .setBusiness(BusinessManager.getBusiness(playerSender.getUniqueId()));
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
            if (BusinessManager.isOwner(playerSender.getUniqueId())) {
                Business b = BusinessManager.getBusiness((playerSender.getUniqueId()));
                Message message = new Message("business.balance").setRecipient(playerSender).setBusiness(b);
                message.sendMessage();
            } else {
                Message message = new Message("errors.not_an_owner").setRecipient(playerSender)
                        .setBusiness(BusinessManager.getBusiness(playerSender.getUniqueId()));
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
                Business b = BusinessManager.getBusiness(id);
                Message balance = new Message("business.balance", true, null, null).setBusiness(b);
                balance.sendMessage();
            }
        }
    }

    @Subcommand(permission = "businesscore.business.info", usage = "/business info <id>")
    public void info(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (BusinessManager.isOwner((playerSender.getUniqueId())) || EmployeeManager.isEmployee(playerSender.getUniqueId())) {
                Business b = BusinessManager.getBusiness((playerSender.getUniqueId()));
                if(b == null)
                    b = EmployeeManager.getEmployee(playerSender.getUniqueId()).getBusiness();
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
                Business b = BusinessManager.getBusiness(id);
                Message message = new Message("business.info", true, null, null).setBusiness(b);
                message.sendMessage();
            }
        }
    }

    @Subcommand(minArgs = 1, permission = "businesscore.business.list", usage = "/business list [<\"id\"|\"balance\"|\"name\">] [<page>]")
    public void list(CommandSender sender, String[] args) {
        int page = 1;
        int pagesOutOf = BusinessManager.getBusinessList().size() / 5;
        if (args.length > 1) {
            if (!args[1].matches("[^0-9]"))
                page = Integer.parseInt(args[1]);
        }
        while (page > pagesOutOf && page > 0)
            page--;
        int fromIndex = page == 1 ? 0 : page * 5;
        int toIndex = fromIndex + 4;
        while (toIndex >= BusinessManager.getBusinessList().size())
            toIndex--;
        switch (args[0].toLowerCase()) {
            case "id":
                Business[] businesses = BusinessManager.sortById()
                        .subList(fromIndex,
                                toIndex >= BusinessManager.getBusinessList().size()
                                        ? BusinessManager.getBusinessList().size() - 1
                                        : toIndex)
                        .toArray(new Business[0]);
                Message header = new Message("business.list.by_id.header", sender);
                header.sendMessage();
                for (Business b : businesses) {
                    Message listItem = new Message("business.list.by_id.format", sender)
                            .setBusiness(b);
                    listItem.sendMessage();
                }
                Message footer = new Message("business.list.by_id.footer", sender).setOther(page,
                        pagesOutOf);
                footer.sendMessage();
                break;
            case "balance":
                businesses = BusinessManager.sortByBalance().subList(fromIndex, toIndex).toArray(new Business[0]);
                header = new Message("business.list.by_balance.header", sender);
                header.sendMessage();
                for (Business b : businesses) {
                    Message listItem = new Message("business.list.by_balance.format", sender)
                            .setBusiness(b);
                    listItem.sendMessage();
                }
                footer = new Message("business.list.by_balance.footer", sender).setOther(page,
                        pagesOutOf);
                footer.sendMessage();
                break;
            case "name":
                businesses = BusinessManager.sortByName().subList(fromIndex, toIndex).toArray(new Business[0]);
                header = new Message("business.list.by_name.header", sender);
                header.sendMessage();
                for (Business b : businesses) {
                    Message listItem = new Message("business.list.by_name.format", sender)
                            .setBusiness(b);
                    listItem.sendMessage();
                }
                footer = new Message("business.list.by_name.footer", sender).setOther(page,
                        pagesOutOf);
                footer.sendMessage();
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
        if (BusinessManager.isOwner(playerSender.getUniqueId())) {
            Business b = BusinessManager.getBusiness(playerSender.getUniqueId());
            Player player = null;
            int page = 1;
            int pagesOutOf = b.getEmployees().size() / 5;
            int fromIndex = 0, toIndex = 1;
            if (args.length > 1) {
                if (args[1].matches("[^0-9]+")) {
                    player = Bukkit.getPlayer(args[1]);
                } else if (args[0].equalsIgnoreCase("fire")) {
                    int id = Integer.parseInt(args[1]);
                    if (EmployeeManager.isEmployeeFor(b, id))
                        player = Bukkit.getPlayer(EmployeeManager.getEmployee(b, id).getUniqueId());
                } else {
                    page = Integer.parseInt(args[1]);
                    while (page > pagesOutOf && page > 0)
                        page--;
                    fromIndex = page == 1 ? 0 : page * 5;
                    toIndex = fromIndex + 4;
                    while (toIndex >= b.getEmployees().size())
                        toIndex--;
                }
            }
            if (player == null && !args[0].toLowerCase().equals("list")) {
                Message message = new Message("errors.no_player").setRecipient(playerSender);
                message.sendMessage();
                return;
            }
            switch (args[0].toLowerCase()) {
                case "hire":
                    if (EmployeeManager.isEmployee(player.getUniqueId())
                            || BusinessManager.isOwner(player.getUniqueId())) {
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
                        Scheduler.runAcceptance();
                    }
                    break;
                case "fire":
                    if (EmployeeManager.isEmployeeFor(b, player.getUniqueId())) {
                        Employee emp = EmployeeManager.getEmployee(player.getName());
                        BusinessFiredEmployeeEvent event = new BusinessFiredEmployeeEvent(b, emp);
                        Bukkit.getPluginManager().callEvent(event);
                    } else {
                        Message message = new Message("errors.not_business_employee").setRecipient(playerSender)
                                .setBusiness(b);
                        message.sendMessage();
                    }
                    break;
                case "list":
                    Message header = new Message("business.employee.list.header").setRecipient(playerSender);
                    header.sendMessage();
                    for (Employee e : b.getEmployees()) {
                        Message listItem = new Message("business.employee.list.format").setRecipient((Player) sender)
                                .setBusiness(b).setEmployee(e);
                        listItem.sendMessage();
                    }
                    Message footer = new Message("business.employee.list.footer").setRecipient((Player) sender)
                            .setOther(page, pagesOutOf);
                    footer.sendMessage();
                    break;
                default:
                    sender.sendMessage(BusinessCore.ERROR_PREFIX + "Try \"hire\",\"fire\", or \"list\"");
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