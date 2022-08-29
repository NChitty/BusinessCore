package me.nchitty.bc.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import me.nchitty.bc.util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractCommand implements CommandExecutor {

    private String command;
    private String permission;
    private boolean hasSubcommands;

    private final HashMap<String, Method> invocation = new HashMap<>();
    private HashMap<String, Subcommand> data = new HashMap<>();
    protected static AbstractCommand instance;

    protected AbstractCommand(JavaPlugin plugin, String command, String permission, boolean subcommands) {
        this.command = command;
        this.permission = permission;
        this.hasSubcommands = subcommands;
        plugin.getCommand(command).setExecutor(this);

        for (Method m : this.getClass().getDeclaredMethods()) {
            if(m.isAnnotationPresent(Subcommand.class)) {
                invocation.put(m.getName().toLowerCase(), m);
                data.put(m.getName().toLowerCase(), m.getAnnotation(Subcommand.class));
            }
        }
    }

    public abstract AbstractCommand getImplementation();

    /**
     * This method gets called if the args length is 0
     * @param sender
     * @return
     */
    public abstract boolean execute(CommandSender sender);

    @Override
    public final boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (!cmd.getLabel().equalsIgnoreCase(command))
            return false;
        if(!sender.hasPermission(permission))
            new Message("errors.no_permission", sender).setOther(permission).sendMessage();
        if(args.length == 0 || !hasSubcommands)
             return execute(sender);

        // this is the sub command parameter
        String sub = args[0].toLowerCase();
        Subcommand commandData = data.get(sub);
        if(commandData == null) {
            // todo no such subcommand
            return true;
        }
        if(!sender.hasPermission(commandData.permission())) {
            new Message("errors.no_permission", sender).setOther(commandData.permission()).sendMessage();
            return true;
        }
        if(!commandData.consoleUse() && !(sender instanceof Player)){
            new Message("errors.player_only", sender).sendMessage();
            return true;
        }
        if(args.length - 1 < commandData.minArgs()) {
            new Message("errors.usage", sender).setOther(commandData.usage()).sendMessage();
            return true;
        }
        try {
            invocation.get(sub).invoke(this.getImplementation(), sender, Arrays.copyOf(args, 1));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }

    public final String parseArgs(String[] args) {
        StringBuilder sb = new StringBuilder();
        for(String s : args) {
            sb.append(" ");
            sb.append(s);
        }
        return sb.toString().replaceFirst(" ", "");
    }
}