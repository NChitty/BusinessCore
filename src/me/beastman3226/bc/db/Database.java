/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.beastman3226.bc.db;

import code.husky.mysql.MySQL;
import me.beastman3226.bc.Main;
import me.beastman3226.bc.Main.Information;

/**
 *
 * @author Nicholas
 */
public class Database {

    public static MySQL MySQL;

    public static Database instance() {
        return Holder.INSTANCE;
    }

    private String host, port, database, user, pass;

    private Database(Builder build) {
        MySQL = new MySQL(Information.BusinessCore, build.host, build.port, build.database, build.user, build.pass);
        build.host = host;
        build.port = port;
        build.database = database;
        build.user = user;
        build.pass = pass;
        MySQL.openConnection();
    }

    public String getDatabase() {
        return database;
    }

    public static class Builder {
        private String host, port, database, user, pass;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(String port) {
            this.port = port;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder pass(String pass) {
            this.pass = pass;
            return this;
        }

        public Database build() {
            return new Database(this);
        }
    }

    public static class Holder {
        public static final Database INSTANCE = new Database.Builder().host(Information.config.getString("database.ip"))
                .port(Information.config.getString("database.port"))
                .database(Information.config.getString("database.name"))
                .user(Information.config.getString("database.username"))
                .pass(Information.config.getString("database.password"))
                .build();
    }

}
