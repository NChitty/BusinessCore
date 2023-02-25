package me.nchitty.bc.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.data.file.FileData;
import me.nchitty.bc.data.file.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.nchitty.bc.player.Employee;
import me.nchitty.bc.player.Employee.EmployeeManager;
import me.nchitty.bc.util.PlaceholderPattern;

public class Business {

    private final int id;
    private String name;
    private String ownerUUID;
    private double balance;
    private final ArrayList<Employee> employees = new ArrayList<>();
    
    
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

    public ArrayList<Employee> getEmployees() {
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
            sb.append(",").append(e.getID());
        }
        sb.append("]");
        return sb.toString().replaceFirst(",", "");
    }

    @PlaceholderPattern(pattern = "<business_employees_byName>")
    public String getEmployeesByName() {
        StringBuilder sb = new StringBuilder("[");
        for(Employee e : employees) {
            sb.append(",").append(e.getName());
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
        assert BusinessCore.getInstance() != null;
        BusinessCore.getInstance().getLogger().log(Level.INFO, "Depositing " + amount + " into " + this.getName());
        return this;
    }

    public Business withdraw(double amount) {
        this.balance = this.balance - amount;
        return this;
    }

    public Business removeEmployee(Employee e) {
        this.employees.remove(e);
        return this;
    }

    /**
     * Adds an employee based on id
     * @param e The id of the employee to be added
     * @return this instance of the business
     */
    public Business addEmployee(Employee e) {
        this.employees.add(e);
        return this;
    }

    /**
     * Adds an employee from employee object
     * @param id The employee to be added
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
         * @param owner The player object of owner
         * @return 
         */
        public Builder owner(Player owner) {
            this.ownerUUID = owner.getUniqueId().toString();
            return this;
        }

        /**
         *
         * @param ownerUUID The uuid stored
         * @return
         */
        public Builder owner(String ownerUUID) {
            this.ownerUUID = ownerUUID;
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

    public static class BusinessManager {
        private static ArrayList<Business> businessList = new ArrayList<Business>();

        /**
         * Creates all businesses from file.
         */
        public static void createBusinesses() {
            BusinessCore.getInstance().getBusinessFileManager().reload();
            FileConfiguration businessYml = BusinessCore.getInstance().getBusinessFileManager().getFileConfiguration();
            int id;
            String name, owner;
            double balance;
            for (String s : businessYml.getKeys(false)) {
                id = businessYml.getInt(s + ".id");
                name = s;
                owner = businessYml.getString(s + ".owner");
                balance = businessYml.getDouble(s + ".balance");
                Business b = createBusiness(new Business.Builder(id).name(name).owner(owner).balance(balance));
                businessList.add(b);
            }
        }

        public static void saveBusinesses() {
            FileManager fm = BusinessCore.getInstance().getBusinessFileManager();
            for(Business b : businessList) {
                fm.edit(new FileData()
                        .add(b.getName() + ".id", b.getID())
                        .add(b.getName() + ".owner", b.getOwnerUUID())
                        .add(b.getName() + ".balance", b.getBalance()));
            }
        }
        public static ArrayList<Business> getBusinessList() {
            return businessList;
        }

        /**
         * Base method for creating a new business
         *
         * @param build
         * @return a new business
         */
        public static Business createBusiness(Business.Builder build) {
            Business b = build.build();
            businessList.add(b);
            return b;
        }

        /**
         * Gets a business based on id
         *
         * @param id The id of the business
         * @return The business
         */
        public static Business getBusiness(int id) {
            Business b = null;
            for (Business business : businessList) {
                if (business.getID() == id) {
                    b = business;
                    break;
                }
            }
            return b;
        }

        /**
         * Finds a business based on the name of the current owner
         *
         * @param uuid UUID of the owner
         * @return The business
         */
        public static Business getBusiness(UUID uuid) {
            for (Business b : businessList)
                if (b.getOwnerUUID().equals(uuid.toString()))
                    return b;
            return null;
        }

        /**
         * Finds a business based on the player object
         *
         * @param playerSender Player object
         * @return the business owned by the player
         */
        public static Business getBusiness(Player playerSender) {
            return getBusiness(playerSender.getUniqueId());
        }

        public static int getNewID(String name) {
            int id = 0;
            int pos = 1;
            for (char c : name.toCharArray()) {
                if (Character.isLetter(c)) {
                    c = Character.toLowerCase(c);
                    id += ((c - 'a' + 1) * (c - 'a' + 1)) * (pos++);
                } else if (Character.isDigit(c)) {
                    continue;
                }
            }
            id /= pos;
            if (isID(id))
                return -1;
            return id;
        }

        /**
         * Deletes a business from storage and in memory
         *
         * @param business The business to be deleted
         */
        public static void closeBusiness(Business business) {
            businessList.remove(business);
        }

        /**
         * Checks if the player is an owner via a null check using the getBusiness(name)
         * method
         *
         * @param uuid The name of the player
         * @return True if name has a business, false if not.
         */
        @Deprecated
        public static boolean isOwner(String uuid) {
            return isOwner(UUID.fromString(uuid));
        }

        public static boolean isOwner(Player playerSender) { return isOwner(playerSender.getUniqueId()); }

        public static boolean isOwner(UUID uniqueId) {
            for(Business b : businessList) {
                if(b.getOwnerUUID().equals(uniqueId.toString()))
                    return true;
            }
            return false;
        }

        /**
         * Checks if the player is an owner via null check using getbusiness(id) method.
         *
         * @param id The id in question
         * @return True if the id is attached to a business, false if not
         */
        public static boolean isID(int id) {
            return getBusiness(id) != null;
        }

        public static ArrayList<Business> sortById() {
            businessList.sort((Business b1, Business b2) -> b1.getID() < b2.getID() ? -1 : 1);
            return businessList;
        }

        public static ArrayList<Business> sortByBalance() {
            businessList.sort((Business b1, Business b2) -> b1.getBalance() < b2.getBalance() ? 1 : -1);
            return businessList;
        }

        public static ArrayList<Business> sortByName() {
            businessList.sort((Business b1, Business b2) -> b1.getName().compareTo(b2.getName()));
            return businessList;
        }


    }
}