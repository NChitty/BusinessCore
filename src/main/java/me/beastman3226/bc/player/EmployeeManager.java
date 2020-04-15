package me.beastman3226.bc.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import me.beastman3226.bc.BusinessCore;
import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class EmployeeManager {

    private static HashMap<Player, Integer> pending = new HashMap<>(50);
    private static HashSet<Employee> employeeList = new HashSet<>();

    public static void loadEmployees() {
        FileConfiguration employeeYml = BusinessCore.getInstance().getEmployeeFileManager().getFileConfiguration();
        for (String key : employeeYml.getKeys(false)) {
            int id = employeeYml.getInt(key + ".id");
            UUID uuid = UUID.fromString(employeeYml.getString(key + ".UUID"));
            int bID = employeeYml.getInt(key + ".business");
            int completed = employeeYml.getInt(key + ".completed");
            int jID = employeeYml.getInt(key + ".job");
            Employee e = new Employee(uuid, id).setBusiness(bID).setCompletedJobs(completed).startJob(jID);
            employeeList.add(e);
            BusinessManager.getBusiness(e.getBusiness().getID()).addEmployee(e);
        }

    }

    public static HashMap<Player, Integer> getPendingPlayers() {
        return pending;
    }

    public static HashSet<Employee> getEmployeeList() {
        return employeeList;
    }

    public static Employee addEmployee(Player player, int BID) {
         Employee employee = new Employee(player.getUniqueId(), 1000 + employeeList.size() + 1);
            employee.setBusiness(BID);
            employeeList.add(employee);
            return employee;
    }

    public static Employee getEmployee(Business b, UUID uuid) {
        if(b == null)
            return getEmployee(uuid);
        for(Employee e : b.getEmployees())
            if(e.getUniqueId().equals(uuid))
                return e;
        return null;
    }

    public static Employee getEmployee(Business b, int id) {
        if(b == null)
            return getEmployee(id);
        for(Employee e : b.getEmployees())
            if(e.getID() == id)
                return e;
        return null;
    }

    public static Employee getEmployee(Business b, String name) {
        if(b == null)
            return getEmployee(name);
        for(Employee e : b.getEmployees())
            if(e.getName().equals(name))
                return e;
        return null;
    }

    public static Employee getEmployee(int bid, UUID uuid) {
        return getEmployee(BusinessManager.getBusiness(bid), uuid);
    }

    public static Employee getEmployee(int bid, int id) {
        return getEmployee(BusinessManager.getBusiness(bid), id);
    }

    public static Employee getEmployee(int bid, String name) {
        return getEmployee(BusinessManager.getBusiness(bid), name);
    }

    public static Employee getEmployee(UUID uuid) {
        for(Employee e : employeeList)
            if(e.getUniqueId().equals(uuid))
                return e;
        return null;
    }

    public static Employee getEmployee(int id) {
        for(Employee e : employeeList)
            if(e.getID() == id)
                return e;
        return null;
    }

    public static Employee getEmployee(String name) {
        for(Employee e : employeeList)
            if(e.getName().equals(name))
                return e;
        return null;
    }

    public static boolean isEmployeeFor(Business b, UUID uuid) {
        return getEmployee(b, uuid) != null;
    }

    public static boolean isEmployeeFor(Business b, int id) {
        return getEmployee(b, id) != null;
    }

    public static boolean isEmployeeFor(Business b, String name) {
        return getEmployee(b, name) != null;
    }

    public static boolean isEmployee(Employee e) {
        return employeeList.contains(e);
    }

    public static boolean isEmployee(UUID uuid) {
        return isEmployee(getEmployee(uuid));
    }

    public static boolean isEmployee(int id) {
        return isEmployee(getEmployee(id));
    }

    public static boolean isEmployee(String name) {
        return isEmployee(getEmployee(name));
    }
}
