package me.beastman3226.bc.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.job.Job;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;

public class Message {

    static FileConfiguration messages;

    private String path;
    private Business business;
    private Employee employee;
    private Job job;
    private Player recipient;
    private Player cause;
    private Object[] other;
    private boolean console = false;

    private static void initMessage() {
        if (messages == null) {
            File messagesFile = new File(BusinessCore.getInstance().getDataFolder(), "messages.yml");
            if (!messagesFile.getParentFile().exists() || !messagesFile.exists()) {
                BusinessCore.getInstance().saveResource("messages.yml", false);
            }
            messages = YamlConfiguration.loadConfiguration(messagesFile);
        }
    }

    public Message(String path) {
        initMessage();
        this.path = path;
    }

    public Message(String path, Player recipient, Business b) {
        initMessage();
        this.path = path;
        this.recipient = recipient;
        this.business = b;
    }

    public Message(String path, Player recipient, Employee e) {
        initMessage();
        this.path = path;
        this.recipient = recipient;
        this.employee = e;
    }

    public Message(String path, Player recipient, Job j) {
        initMessage();
        this.path = path;
        this.recipient = recipient;
        this.job = j;
    }

    public Message(String path, boolean console, Player recipient, Player cause) {
        initMessage();
        this.path = path;
        this.console = console;
        this.recipient = recipient;
        this.cause = cause;
    }

    public Message(String path, CommandSender sender) {
        initMessage();
        this.path = path;
        if (sender instanceof Player) {
            this.recipient = (Player) sender;
        } else {
            this.console = true;
        }
    }

    public void sendMessage() {
        List<TextComponent> message = getMessage();
        if (!console || recipient != null) {
            if (recipient.isOnline())
                recipient.spigot().sendMessage(message.toArray(new BaseComponent[] {}));
        } else {
            Bukkit.getConsoleSender().spigot().sendMessage(message.toArray(new BaseComponent[] {}));
        }
    }

    private List<TextComponent> getMessage() {
        String message = "";
        if (messages.isList(path)) {
            StringBuilder sb = new StringBuilder();
            for (String s : messages.getStringList(path))
                sb.append("<br>" + s);
            message = sb.toString().replaceFirst("<br>", "");
        } else {
            message = messages.getString(path);
        }
        message = parsePlayerTags(message);
        if (business != null)
            message = parseBusinessTags(message);
        if (employee != null)
            message = parseEmployeeTags(message);
        if (job != null)
            message = parseJobTags(message);
        Matcher matcher = Pattern.compile("(<[a-zA-Z_]+>|<[a-zA-Z0-9_]+:[^>]*>)").matcher(message);
        TextComponentBuilder compBuilder = new TextComponentBuilder();
        int lastIndex = 0;
        StringBuilder curStr = new StringBuilder();
        while (matcher.find()) {
            if (matcher.start() != 0) {
                curStr.append(message, lastIndex, matcher.start());
                TextComponent current = new TextComponent(TextComponent.fromLegacyText(curStr.toString()));
                compBuilder.add(current);
                curStr.delete(0, curStr.length());
            }
            lastIndex = matcher.end();
            if (matcher.group().equals("<reset>")) {
                compBuilder.setNextHoverEvent(null);
                compBuilder.setNextClickEvent(null);
                compBuilder.setColor(ChatColor.WHITE);
            } else {
                try {
                    compBuilder.setColor(ChatColor
                            .valueOf(matcher.group().substring(1, matcher.group().length() - 1).toUpperCase()));
                } catch (IllegalArgumentException e) {
                    if (matcher.group().contains("economy_plugin_format")) {
                        Economy eco = BusinessCore.getInstance().getEconomy();
                        double d = Double.parseDouble(matcher.group().substring(matcher.group().indexOf(":") + 1,
                                matcher.group().lastIndexOf(">")));
                        curStr.append(eco.format(d));
                    } else if (matcher.group().contains("tooltip") || matcher.group().contains("link")
                            || matcher.group().contains("command")) {
                        Object event = parseEvent(matcher.group());
                        if (event != null) {
                            if (event instanceof HoverEvent)
                                compBuilder.setNextHoverEvent((HoverEvent) event);
                            else if (event instanceof ClickEvent)
                                compBuilder.setNextClickEvent((ClickEvent) event);
                        }
                    }
                }
            }
        }
        if (lastIndex < message.length()) {
            curStr.append(message, lastIndex, message.length());
        }
        TextComponent current = new TextComponent(TextComponent.fromLegacyText(curStr.toString()));
        compBuilder.add(current);
        Bukkit.getConsoleSender().spigot().sendMessage(compBuilder.getComponents().toArray(new BaseComponent[] {}));
        return compBuilder.getComponents();
    }

    private Object parseEvent(String group) {
        String attribute = group.split(":")[0].substring(1);
        String value = group.split(":")[1].substring(0, group.lastIndexOf(">"));
        switch (attribute) {
            case "tooltip":
                return new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(value));
            case "link":
                return new ClickEvent(ClickEvent.Action.OPEN_URL, value);
            case "command":
                return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, value);
            default:
                return null;
        }
    }

    private String parsePlayerTags(String string) {
        Matcher match = Pattern.compile("(<[a-zA-Z0-9_]+>)").matcher(string);
        StringBuilder sb = new StringBuilder();
        int lastIndex = 0;
        int otherIndex = 0;
        while (match.find()) {
            if (match.start() != 0)
                sb.append(string.substring(lastIndex, match.start()));
            lastIndex = match.end();
            if (match.group().equals("<player_cause>")) {
                if (cause != null) {
                    sb.append(cause.getName());
                }
            } else if (match.group().equals("<player_recipient>")) {
                if (recipient != null) {
                    sb.append(recipient.getName());
                }
            } else if (match.group().contains("prefix")) {
                String name = match.group().split("_")[1].replaceAll(">", "");
                sb.append(getPrefix(name));
            } else if (match.group().contains("current_page"))
                sb.append(other[otherIndex++]);
            else if (match.group().contains("total_pages"))
                sb.append(other[otherIndex++]);
            else if (match.group().equals("<br>")) {
                sb.append("\n");
            } else {
                try {
                    sb.append(other[otherIndex++]);
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    otherIndex = 0;
                    sb.append(match.group());
                }
            }
        }
        sb.append(string, lastIndex, string.length());
        return sb.toString();
    }

    private String parseBusinessTags(String string) {
        Matcher matcher = Pattern.compile("(<business_[a-zA-Z0-9_]+>)").matcher(string);
        StringBuilder sb = new StringBuilder();
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.start() != 0) {
                sb.append(string.substring(lastIndex, matcher.start()));
                if (matcher.group().equals("<business_balance_change_amount>")) {
                    sb.append(other[0].toString());
                } else {
                    for (Method m : Business.class.getMethods()) {
                        if (m.isAnnotationPresent(PlaceholderPattern.class)) {
                            if (matcher.group().equals(m.getAnnotation(PlaceholderPattern.class).pattern())) {
                                try {
                                    sb.append(m.invoke(business).toString());
                                } catch (IllegalAccessException | IllegalArgumentException
                                        | InvocationTargetException e) {
                                    sb.append(matcher.group());
                                }
                            }
                        }
                    }
                }
            }
            lastIndex = matcher.end();

        }
        sb.append(string, lastIndex, string.length());
        return sb.toString();
    }

    private String parseEmployeeTags(String string) {
        Matcher matcher = Pattern.compile("(<employee_[a-zA-Z0-9_]+>)").matcher(string);
        StringBuilder sb = new StringBuilder();
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.start() != 0) {
                sb.append(string.substring(lastIndex, matcher.start()));
                for (Method m : Employee.class.getMethods()) {
                    if (m.isAnnotationPresent(PlaceholderPattern.class)) {
                        if (matcher.group().equals(m.getAnnotation(PlaceholderPattern.class).pattern())) {
                            try {
                                sb.append(m.invoke(employee).toString());
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                sb.append(matcher.group());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            lastIndex = matcher.end();
        }
        sb.append(string, lastIndex, string.length());
        return sb.toString();
    }

    private String parseJobTags(String string) {
        Matcher matcher = Pattern.compile("(<job_[a-zA-Z0-9_]+>)").matcher(string);
        StringBuilder sb = new StringBuilder();
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.start() != 0) {
                sb.append(string.substring(lastIndex, matcher.start()));
                for (Method m : Job.class.getMethods()) {
                    if (m.isAnnotationPresent(PlaceholderPattern.class)) {
                        if (matcher.group().equals(m.getAnnotation(PlaceholderPattern.class).pattern())) {
                            try {
                                sb.append(m.invoke(job).toString());
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                sb.append(matcher.group());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            lastIndex = matcher.end();

        }

        sb.append(string, lastIndex, string.length());
        return sb.toString();
    }

    private String getPrefix(String name) {
        return messages.getString("prefixes." + name);
    }

    public Message setPath(String path) {
        this.path = path;
        return this;
    }

    public Message setBusiness(Business business) {
        this.business = business;
        return this;
    }

    public Message setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public Message setJob(Job job) {
        this.job = job;
        return this;
    }

    public Message setRecipient(Player recipient) {
        this.recipient = recipient;
        if (this.business == null)
            this.business = BusinessManager.getBusiness(recipient.getUniqueId());
        if (this.employee == null)
            this.employee = EmployeeManager.getEmployee(recipient.getUniqueId());
        return this;
    }

    public Message setCause(Player cause) {
        this.cause = cause;
        if (this.business == null)
            this.business = BusinessManager.getBusiness(cause.getUniqueId());
        if (this.employee == null)
            this.employee = EmployeeManager.getEmployee(cause.getUniqueId());
        return this;
    }

    public Message setOther(Object... other) {
        this.other = Arrays.copyOf(other, other.length);
        return this;
    }
}