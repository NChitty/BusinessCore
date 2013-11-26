package me.beastman3226.bc.business;

import java.util.HashSet;

public class Business {

    private int id;
    private String name;
    private String ownerName;
    private double worth;
    private int[] employeeIDs;
    protected static HashSet<Business> businessList = new HashSet<>();

    private Business(Builder build) {
        this.id = build.id;
        this.name = build.name;
        this.ownerName = build.ownerName;
        this.worth = build.worth;
        this.employeeIDs = build.employeeIDs;
    }

    public int getID() {
        return id;
    }

    public static class Builder {
        private int id;
        private String name;
        private String ownerName;
        private double worth;
        private int[] employeeIDs;

        public Builder(int id) {
            this.id = id;
            this.name = "No Name " + id;
        }

        public Builder name(String anme) {
            name = anme;
            return this;
        }

        public Builder owner(String owner) {
            this.ownerName = owner;
            return this;
        }

        public Builder balance(double d) {
            this.worth = d;
            return this;
        }

        public Builder ids(int[] ids) {
            this.employeeIDs = ids;
            return this;
        }

        public Builder ids(String[] ids) {
            int i = 0;
            int k;
            this.employeeIDs = new int[ids.length];
            for(String s : ids) {
                k = Integer.valueOf(s);
                this.employeeIDs[i] = k;
                i++;
            }
            return this;
        }

        public Business build() {
            return new Business(this);
        }
    }
}