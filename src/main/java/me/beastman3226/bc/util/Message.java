package me.beastman3226.bc.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import me.beastman3226.bc.job.Job;
import me.beastman3226.bc.job.JobManager;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class Message {

    static FileConfiguration messages;

    private String path;
    private Business business;
    private Employee employee;
    private Job job;
    private Player recipient;
    private Player cause;
    private Object[] other;

    public Message(String path) {
        if (messages == null) {
            File messagesFile = new File(BusinessCore.getInstance().getDataFolder(), "messages.yml");
            if (!messagesFile.getParentFile().exists() || !messagesFile.exists()) {
                BusinessCore.getInstance().saveResource("messages.yml", false);
            }
            messages = YamlConfiguration.loadConfiguration(messagesFile);
        }
        this.path = path;
    }

    private String parsePlayerTags(String string) {
        Matcher match = Pattern.compile("<[a-zA-Z0-9_]+>").matcher(string);
        StringBuilder sb = new StringBuilder();
        int lastIndex = 0;

        while (match.find()) {
            if (match.start() != 0)
                sb.append(string.substring(lastIndex, match.start()));
            lastIndex = match.end();
            if (match.group().equals("<player_cause>"))
                sb.append(cause.getName());
            else if (match.group().equals("<player_recipient>"))
                sb.append(recipient.getName());
            else if (match.group().contains("prefix")) {
                String name = match.group().split("_")[1].replaceAll(">", "");
                sb.append(getPrefix(name));
            } else if (match.group().equals("<br>")) {
                sb.append("\n");
            } else {
                sb.append(match.group());
            }
        }
        return sb.toString();
    }

    public TextComponent getMessage() {
        String message;
        if(messages.isList(path)) {
            StringBuilder sb = new StringBuilder();
            for(String s : messages.getStringList(path))
                sb.append("\n" + s);
            message = sb.toString().replaceFirst("\n", "");
        } else {
            message = messages.getString(path);
        }
        message = parsePlayerTags(message);
        if(business != null)
            message = parseBusinessTags(message);
        if(employee != null)
            message = parseEmployeeTags(message);
        if(job != null)
            message = parseJobTags(message);

        Matcher matcher = Pattern.compile("(<[a-zA-Z_]+>|<[a-zA-Z0-9]+:[^>]*)").matcher(message);
        TextComponentBuilder compBuilder = new TextComponentBuilder();
        int lastIndex = 0;
        StringBuilder curStr = new StringBuilder();
        while (matcher.find())
        {
            if (matcher.start() != 0)
            {
                curStr.append(message, lastIndex, matcher.start());
                TextComponent current = new TextComponent(curStr.toString());
                compBuilder.add(current);
                curStr.delete(0, curStr.length());
            }
            lastIndex = matcher.end();
            if (matcher.group().equals("<reset>"))
            {
                compBuilder.setNextHoverEvent(null);
                compBuilder.setNextClickEvent(null);
            }
            else
            {
                Object event = parseEvent(matcher.group());
                if (event != null)
                {
                    if (event instanceof HoverEvent)
                        compBuilder.setNextHoverEvent((HoverEvent) event);
                    else if (event instanceof ClickEvent)
                        compBuilder.setNextClickEvent((ClickEvent) event);
                }
            }
        }
        if (lastIndex < plainPage.length())
        {
            curStr.append(plainPage, lastIndex, plainPage.length());
            TextComponent current = new TextComponent(TextComponent.fromLegacyText(Text.setPlaceholders(player, curStr.toString())));
            compBuilder.add(current);
        }

        return finalMessage;
    }

    private String parseBusinessTags(String string) {
        Matcher matcher = Pattern.compile("(<business_[a-zA-Z0-9_]+>)").matcher(string);
        StringBuilder sb = new StringBuilder();
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.start() != 0) {
                sb.append(string.substring(lastIndex, matcher.start()));
                if (matcher.group().equals("<business_balance_change_amount>"))
                    sb.append(other[0].toString());
                else {
                    for (Method m : Business.class.getMethods()) {
                        if (m.isAnnotationPresent(PlaceholderPattern.class)) {
                            if (matcher.group().equals(m.getAnnotation(PlaceholderPattern.class).pattern())) {
                                try {
                                    sb.append(m.invoke(business).toString());
                                } catch (IllegalAccessException | IllegalArgumentException
                                        | InvocationTargetException e) {
                                    sb.append(matcher.group());
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            lastIndex = matcher.end();

        }

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
        return this;
    }

    public Message setCause(Player cause) {
        this.cause = cause;
        return this;
    }

    public Message setOther(Object[] other) {
        this.other = other;
        return this;
    }
}