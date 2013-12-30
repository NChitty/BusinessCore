package me.beastman3226.bc.util;

import org.bukkit.ChatColor;

/**
 *
 * @author Nicholas
 */
public enum Prefixes {

    ERROR(ChatColor.GRAY + "[" + ChatColor.RED + "BusinessCore" + ChatColor.GRAY + "]: " + ChatColor.WHITE),
    NOMINAL(ChatColor.GRAY + "[" + ChatColor.AQUA + "BusinessCore" + ChatColor.GRAY + "]: " + ChatColor.WHITE),
    POSITIVE(ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "BusinessCore" + ChatColor.GRAY + "]: " + ChatColor.WHITE);

    private final String toString;
    Prefixes(String string) {
        this.toString = string;
    }

    @Override
    public String toString() {
        return this.toString;
    }
}
