package me.nchitty.bc.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

import static net.md_5.bungee.api.ChatColor.*;

public class TextComponentBuilder {
    
    private final List<TextComponent> components = new ArrayList<>();
    private ClickEvent nextClickEvent;
    private HoverEvent nextHoverEvent;
    private ArrayList<ChatColor> colors = new ArrayList<>();

    TextComponentBuilder()
    {
    }

    List<TextComponent> getComponents()
    {
        return this.components;
    }

    void add(TextComponent component)
    {
        if (getNextClickEvent() != null)
            component.setClickEvent(this.getNextClickEvent());
        if (getNextHoverEvent() != null)
            component.setHoverEvent(this.getNextHoverEvent());
        this.setColorFormatting(component);
        this.components.add(component);
    }

    private void setColorFormatting(TextComponent component) {
        for(ChatColor format : this.colors) {
            if (format.equals(STRIKETHROUGH)) {
                component.setStrikethrough(true);
            } else if (format.equals(UNDERLINE)) {
                component.setUnderlined(true);
            } else if (format.equals(ITALIC)) {
                component.setItalic(true);
            } else if (format.equals(BOLD)) {
                component.setBold(true);
            } else if (format.equals(MAGIC)) {
                component.setObfuscated(true);
            } else {
                component.setColor(format);
            }
        }
    }

    void add(TextComponentBuilder builder)
    {
        builder.getComponents().forEach(this::add);
    }

    void add(List<TextComponent> components)
    {
        components.forEach(this::add);
    }

    private ClickEvent getNextClickEvent()
    {
        return this.nextClickEvent;
    }

    void setNextClickEvent(ClickEvent nextClickEvent)
    {
        this.nextClickEvent = nextClickEvent;
    }

    private HoverEvent getNextHoverEvent()
    {
        return this.nextHoverEvent;
    }

    void setNextHoverEvent(HoverEvent nextHoverEvent)
    {
        this.nextHoverEvent = nextHoverEvent;
    }

    void setColor(ChatColor chatColor) {
        if(chatColor == null) {
            this.colors.clear();
            this.colors.add(ChatColor.WHITE);
        } else {
            this.colors.add(chatColor);
        }
    }

}