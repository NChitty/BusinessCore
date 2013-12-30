package me.beastman3226.bc.business;

import java.util.HashSet;
import me.beastman3226.bc.errors.InsufficientFundsException;
import me.beastman3226.bc.player.Employee;

public class Business {

    private int id;
    private String name;
    private String ownerName;
    private double worth;
    private int[] employeeIDs;
    public static HashSet<Business> businessList = new HashSet<>();

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

    public String getName() {
        return this.name;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public double getBalance() {
        return this.worth;
    }

    public int[] getEmployeeIDs() {
        return this.employeeIDs;
    }

    public Business setName(String name) {
        this.name = name;
        return this;
    }

    public Business setOwnerName(String owner) {
        this.ownerName = owner;
        return this;
    }

    public Business setBalance(double newBalance) {
        this.worth = newBalance;
        return this;
    }

    public Business deposit(double amount) {
        this.worth = this.worth + amount;
        return this;
    }

    public Business withdraw(double amount) throws InsufficientFundsException {
        if(amount > this.worth) {
            throw new InsufficientFundsException("Not enough funds. Missing " + (amount - worth));
        }
        return this;
    }

    public Business setEmployeeIDs(int[] ids) {
        this.employeeIDs = ids;
        return this;
    }

    public Business removeEmployee(int id) {
        int[] newList = new int[(this.employeeIDs.length - 1)];
        int i = 0;
        for(int k : this.employeeIDs) {
            if(k != id) {
                newList[i] = k;
            } else {
                continue;
            }
            i++;
        }
        this.employeeIDs = newList;
        return this;
    }

    /**
     * Adds an employee based on id
     * @param id The id of the employee to be added
     * @return this instance of the business
     */
    public Business addEmployee(int id) {
        int[] newList = new int[(this.employeeIDs.length+1)];
        int i = 0;
        for(int k : this.employeeIDs) {
            if(i != this.employeeIDs.length) {
                newList[i] = k;
            } else {
                newList[i] = id;
                break;
            }
        }
        this.employeeIDs = newList;
        return this;
    }

    /**
     * Adds an employee from employee object
     * @param employee The employee to be added
     * @return this instance of the business
     */
    public Business addEmployee(Employee employee) {
        int[] newList = new int[(this.employeeIDs.length+1)];
        int i = 0;
        for(int k : this.employeeIDs) {
            if(i != this.employeeIDs.length) {
                newList[i] = k;
            } else {
                newList[i] = employee.getID();
                break;
            }
        }
        this.employeeIDs = newList;
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static class Builder {
        private int id;
        private String name;
        private String ownerName;
        private double worth;
        private int[] employeeIDs;

        public int getID() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getOwnerName() {
            return this.ownerName;
        }

        public double getBalance() {
            return this.worth;
        }

        public int[] getEmployeeIDs() {
            return this.employeeIDs;
        }

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