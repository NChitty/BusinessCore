package me.beastman3226.bc.player;

import java.util.HashSet;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;

/**
 *
 * @author beastman3226
 */
public class Employee {

    private final String employeeName;
    private final int id;
    private Business business;

    public static HashSet<Employee> employeeList = new HashSet<>();

    public Employee(String name, int id) {
        this.employeeName = name;
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.employeeName;
    }

    public Business getBusiness() {
        return this.business;
    }

    public Employee setBusiness(Business b) {
        this.business = b;
        return this;
    }

    public Employee setBusiness(int id) {
        this.business = BusinessManager.getBusiness(id);
        return this;
    }

    public Employee setBusiness(String owner) {
        this.business = BusinessManager.getBusiness(owner);
        return this;
    }
}
