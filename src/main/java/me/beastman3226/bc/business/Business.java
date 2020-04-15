package me.beastman3226.bc.business;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.player.Employee;
import me.beastman3226.bc.player.EmployeeManager;
import me.beastman3226.bc.util.PlaceholderPattern;

public class Business {

    private final int id;
    private String name;
    private String ownerUUID;
    private double balance;
    private HashSet<Employee> employees = new HashSet<Employee>();
    
    
    private Business(Builder build) {
        this.id = build.id;
        this.name = build.name;
        this.ownerUUID = build.ownerUUID;
        this.balance = build.balance;
        if(build.employees != null)
            this.employees.addAll(Arrays.asList(build.employees));
    }

    @PlaceholderPattern(pattern = "<business_id>")
    public int getID() {
        return id;
    }

    @PlaceholderPattern(pattern = "<business_name>")
    public String getName() {
        return this.name;
    }

    public String getOwnerUUID() {
        return this.ownerUUID;
    }

    @PlaceholderPattern(pattern = "<business_balance>")
    public double getBalance() {
        return this.balance;
    }

    public HashSet<Employee> getEmployees() {
        return this.employees;
    }

    public Player getOwner() {
        return Bukkit.getPlayer(UUID.fromString(this.ownerUUID));
    }

    @PlaceholderPattern(pattern = "<business_owner>")
    public String getOwnerName() {
		return this.getOwner().getName();
	}

    @PlaceholderPattern(pattern = "<business_employees_byID>")
    public String getEmployeesById() {
        StringBuilder sb = new StringBuilder("[");
        for(Employee e : employees) {
            sb.append("," + e.getID());
        }
        sb.append("]");
        return sb.toString().replaceFirst(",", "");
    }

    @PlaceholderPattern(pattern = "<business_employees_byName>")
    public String getEmployeesByName() {
        StringBuilder sb = new StringBuilder("[");
        for(Employee e : employees) {
            sb.append("," + e.getName());
        }
        sb.append("]");
        return sb.toString().replaceFirst(",", "");
    }

    public Business setName(String name) {
        this.name = name;
        return this;
    }

    public Business setOwnerUUID(String owner) {
        this.ownerUUID = owner;
        return this;
    }

    public Business setBalance(double newBalance) {
        this.balance = newBalance;
        return this;
    }

    public Business deposit(double amount) {
        this.balance = this.balance + amount;
        BusinessCore.getInstance().getLogger().log(Level.INFO, "Depositing " + amount + " into " + this.getName());
        return this;
    }

    public Business withdraw(double amount) {
        this.balance = this.balance - amount;
        return this;
    }


    public Business setEmployees(Employee[] e) {
        this.employees.addAll(Arrays.asList(e));
        return this;
    }

    public Business removeEmployee(Employee e) {
        this.employees.remove(e);
        return this;
    }

    /**
     * Adds an employee based on id
     * @param id The id of the employee to be added
     * @return this instance of the business
     */
    public Business addEmployee(Employee e) {
        this.employees.add(e);
        return this;
    }

    /**
     * Adds an employee from employee object
     * @param employee The employee to be added
     * @return this instance of the business
     */
    public Business addEmployee(int id) {
        this.employees.add(EmployeeManager.getEmployee(id));
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    public static class Builder {
        private int id;
        private String name;
        private String ownerUUID;
        private double balance;
        private Employee[] employees;
        
        public int getID() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }

        public String getOwnerUUID() {
            return this.ownerUUID;
        }

        public double getBalance() {
            return this.balance;
        }

        public Employee[] getEmployees() {
            return this.employees;
        }

        public Builder(int id) {
            this.id = id;
            this.name = "No Name " + id;
        }

        public Builder name(String anme) {
            name = anme;
            return this;
        }
        
        /**
         * 
         * @param owner Name of owner
         * @return 
         */
        public Builder owner(String owner) {
            this.ownerUUID = owner;
            return this;
        }

        public Builder balance(double d) {
            this.balance = d;
            return this;
        }

        public Builder employees(Employee[] employee) {
            this.employees = employee;
            return this;
        }
        
        public Builder employees(String[] ids) {
            int i = 0;
            this.employees = new Employee[ids.length];
            for(String s : ids) {
                this.employees[i] = EmployeeManager.getEmployee(Integer.parseInt(s));
                i++;
            }
            return this;
        }

        public Business build() {
            return new Business(this);
        }  
    }
    
}