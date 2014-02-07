package me.beastman3226.bc.data;

import me.beastman3226.bc.data.DataHandler;
import me.beastman3226.bc.db.Table;

/**
 *
 * @author beastman3226
 */
public class BusinessHandler {


    public static void update(String column, Object data, String other_column, Object condition) {
        DataHandler.update(Table.BUSINESS, column, data, other_column, condition);
    }

    public static void remove(String column, Object condition) {
        DataHandler.remove(Table.BUSINESS, column, condition);
    }

    public static void add(Data object) {
        DataHandler.add(Table.BUSINESS, object);
    }

}
