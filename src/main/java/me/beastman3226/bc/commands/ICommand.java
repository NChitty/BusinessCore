package me.beastman3226.bc.commands;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.util.Message;

public abstract class ICommand implements CommandExecutor {

    private String command;
    private String permission;
    private boolean hasSubcommands;

    private static HashMap<Class<? extends ICommand>, ICommand> invokeInstance = new HashMap<>();

    public ICommand(String command, String permission, boolean subcommands) {
        this.command = command;
        this.permission = permission;
        this.hasSubcommands = subcommands;
        BusinessCore.getInstance().getCommand(command).setExecutor(this);
        invokeInstance.put(this.getClass(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getLabel().equalsIgnoreCase(command)) {
            if (hasSubcommands && args.length > 0) {
                String sub = args[0];
                try {
                    Method subcommand = this.getClass().getMethod(sub.toLowerCase(), CommandSender.class, String[].class);
                    if (subcommand.isAnnotationPresent(Subcommand.class)) {
                        Subcommand info = subcommand.getAnnotation(Subcommand.class);
                        if (!sender.hasPermission(info.permission())) {
                            new Message("errors.no_permission", sender).setOther(info.permission()).sendMessage();
                            return true;
                        }
                        if (!info.consoleUse() && !(sender instanceof Player)) {
                            new Message("errors.player_only", sender).sendMessage();
                            return true;
                        }
                        if (args.length - 1 < info.minArgs()) {
                            new Message("errors.usage", sender).setOther(info.usage()).sendMessage();
                            return true;
                        }
                        subcommand.invoke(invokeInstance.get(subcommand.getDeclaringClass()), sender, Arrays.copyOfRange(args, 1, args.length));
                        return true;
                    }
                } catch (NoSuchMethodException e) {
                    return false;
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (!sender.hasPermission(permission)) {
                    new Message("errors.no_permission", sender).sendMessage();
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