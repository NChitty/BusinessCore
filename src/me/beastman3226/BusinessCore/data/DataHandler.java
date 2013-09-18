package me.beastman3226.BusinessCore.data;

/**
 *
 * @author beastman3226
 */
public interface DataHandler {

    public abstract String getString(String query);
    public abstract int getInt(String query);
    public abstract double getDouble(String query);
    public abstract void update(String query);

}
