package me.beastman3226.bc.business;

import java.util.LinkedHashSet;

public class Business {

    private int id;
    private String name;
    private String ownerName;
    private double worth;
    private int[] employeeIDs;
    protected static LinkedHashSet<Business> businessList = new LinkedHashSet<>();

    protected Business() {

    }

    public int getID() {
        return id;
    }
}