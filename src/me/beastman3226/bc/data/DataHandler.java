package me.beastman3226.bc.data;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.Main;
import me.beastman3226.bc.db.Database;
import me.beastman3226.bc.db.Table;

/**
 *
 * @author beastman3226
 */
public abstract class DataHandler {

    public static void update(Table table, String column, Object data, Object condition) {
        if(Database.MySQL.checkConnection()) {
            try {
                Statement s =  Database.MySQL.getConnection().createStatement();
                s.execute("UPDATE " + table + "\n"
                        + "SET " + column + "='" + data + "'\n"
                        + "WHERE " + column + "='" + condition + "';");
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void remove(Table table, String column, Object condition) {
        if(Database.MySQL.checkConnection()) {
            try {
                Statement s = Database.MySQL.getConnection().createStatement();
                s.execute("DELETE FROM " + table + "\n"
                        + "WHERE " + column + "='" + condition + "';");
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static void add(Table table, Data object) {
        if(Database.MySQL.checkConnection()) {
            try {
                Statement s = Database.MySQL.getConnection().createStatement();
                Iterator i = object.getData().values().iterator();
                String string = "";
                while(i.hasNext()) {
                    string = string + ",'" + i.next() + "'";
                }
                s.execute("INSERT INTO " + table + "\n"
                        + "VALUES(" + string + ");");
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}