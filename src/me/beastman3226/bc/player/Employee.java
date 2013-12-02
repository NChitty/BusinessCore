package me.beastman3226.bc.player;

import java.util.HashSet;

/**
 *
 * @author beastman3226
 */
public class Employee {

    private final String employeeName;
    private final int id;

    public static HashSet<Employee> employeeList = new HashSet<>();

    public Employee(String name, int id) {
        this.employeeName = name;
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

}
