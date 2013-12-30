package me.beastman3226.bc.util;

/**
 *
 * @author beastman3226
 */
public enum Time {

    DAY('d', 1728000),
    HOUR('h', 7200),
    MINUTE('m', 1200),
    SECOND('s', 20);

    public final char identifier;
    public final int multiplier;
    Time(char indentifier, int multiplier) {
        this.identifier = indentifier;
        this.multiplier = multiplier;
    }

    public long getTicks(int i) {
        return i * multiplier;
    }
}
