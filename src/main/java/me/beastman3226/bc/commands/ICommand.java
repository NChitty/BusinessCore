package me.beastman3226.bc.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.beastman3226.bc.BusinessCore;

public abstract class ICommand implements CommandExecutor {

    private String command;
    private String permission;
    private boolean hasSubcommands;

    public ICommand(String command, String permission, boolean subcommands) {
        this.command = command;
        this.permission = permission;
        this.hasSubcommands = subcommands;
        BusinessCore.getInstance().getCommand(command).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getLabel().equalsIgnoreCase(command)) {
            if (hasSubcommands && args.length > 0) {
                String sub = args[0];
                try {
                    Method subcommand = getClass().getMethod(sub.toLowerCase(), CommandSender.class, String[].class);
                    if (subcommand.isAnnotationPresent(Subcommand.class)) {
                        Subcommand info = subcommand.getAnnotation(Subcommand.class);
                        if (!sender.hasPermission(info.permission())) {
                            sender.sendMessage(
                                    BusinessCore.getPrefix(BusinessCore.ERROR) + "You must have the permission "
                                            + info.permission() + " to execute this command.");
                            return true;
                        }
                        if (!info.consoleUse() && !(sender instanceof Player)) {
                            sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR) + "Only players may use this command!");
                            return true;
                        }
                        if (args.length - 1 < info.minArgs()) {
                            sender.sendMessage(
                                    BusinessCore.getPrefix(BusinessCore.ERROR) + info.usage());
                            return true;
                        }
                        subcommand.invoke(null, sender, Arrays.copyOfRange(args, 1, args.length));
                        return true;
                    }
                } catch (NoSuchMethodException e) {
                    return false;
                } catch(SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else if (hasSubcommands && args.length == 0) {
                return false;
            } else {
                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(BusinessCore.getPrefix(BusinessCore.ERROR) + "You must have the permission "
                            + permission + " to execute this command.");
                    return true;
                } else {
                    execute(sender);
                }
            }
        }
        return true;
    }

    public abstract void execute(CommandSender sender);

    public final String parseArgs(String[] args) {
        StringBuilder sb = new StringBuilder();
        for(String s : args) {
            sb.append(" ");
            sb.append(s);
        }
        return sb.toString().replaceFirst(" ", "");
    }
}