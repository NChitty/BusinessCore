package me.beastman3226.bc.data;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.db.Database;
import me.beastman3226.bc.db.Table;

/**
 *
 * @author beastman3226
 */
public abstract class DataHandler {

    public static void update(Table table, String column, Object data, String column_condition, Object condition) {
        if(Database.instance().MySQL.checkConnection()) {
            try {
                Statement s =  Database.instance().MySQL.getConnection().createStatement();
                if(data instanceof int[]) {
                    String k = "";
                    for(int i : (int[]) data) {
                        k = k + "," + i;
                    }
                    s.execute("UPDATE " + table + "\n"
                        + "SET " + column + "='" + k + "'\n"
                        + "WHERE " + column_condition + "='" + condition + "';");
                } else {
                    s.execute("UPDATE " + table + "\n"
                        + "SET " + column + "='" + data + "'\n"
                        + "WHERE " + column_condition + "='" + condition + "';");
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void remove(Table table, String column, Object condition) {
        if(Database.instance().MySQL.checkConnection()) {
            try {
                Statement s = Database.instance().MySQL.getConnection().createStatement();
                s.execute("DELETE FROM " + table + "\n"
                        + "WHERE " + column + "='" + condition + "';");
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static void add(Table table, Data object) {
        if(Database.instance().MySQL.checkConnection()) {
            try {
                Statement s = Database.instance().MySQL.getConnection().createStatement();
                Iterator i = object.getData().values().iterator();
                String string = "";
                while(i.hasNext()) {
                    string = string + ",'" + i.next() + "'";
                }
                s.execute("INSERT INTO " + table + "\n"
                        + "VALUES(" + string + ");");
                object.clear();
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
