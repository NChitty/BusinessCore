package me.beastman3226.bc.db;

/**
 *
 * @author beastman3226
 */
public abstract class DataHandler {

    Database db = Database.instance();

    public abstract void update(Table table, String column, Object data);
    public abstract void remove(Table table, String column, Object condition);
    public abstract void add(Table table, Object object);

}
