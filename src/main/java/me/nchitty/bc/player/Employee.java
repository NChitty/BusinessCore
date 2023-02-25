package me.nchitty.bc.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.business.Business;
import me.nchitty.bc.data.file.FileData;
import me.nchitty.bc.data.file.FileManager;
import org.bukkit.Bukkit;

import me.nchitty.bc.util.PlaceholderPattern;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class Employee {

    private final String employeeName;
    private final UUID uniqueId;
    private final int id;
    private int business;
    private int completedJobs;
    private int jobID;

    public Employee(UUID uuid, int id) {
        this.uniqueId = uuid;
        this.employeeName = Bukkit.getPlayer(uuid).getName();
        this.id = id;
    }

    @PlaceholderPattern(pattern = "<employee_id>")
    public int getID() {
        return this.id;
    }

    @PlaceholderPattern(pattern = "<employee_name>")
    public String getName() {
        return this.employeeName;
    }

    @PlaceholderPattern(pattern = "<employee_business>")
    public Business getBusiness() {
        return Business.BusinessManager.getBusiness(this.business);
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public Employee setBusiness(Business b) {
        this.business = b.getID();
        return this;
    }

    public Employee setBusiness(int id) {
        this.business = Business.BusinessManager.getBusiness(id).getID();
        return this;
    }

    public Employee setBusiness(UUID owner) {
        this.business = Business.BusinessManager.getBusiness(owner).getID();
        return this;
    }

    public Employee setCompletedJobs(int i) {
        this.completedJobs = i;
        return this;
    }

    public Employee startJob(int id) {
        this.jobID = id;
        return this;
    }

    public Employee completeJob() {
        this.jobID = -1;
        this.completedJobs = this.completedJobs++;
        BusinessCore.getInstance().getEmployeeFileManager().edit(new FileData().add(this.employeeName + ".job", null).add(this.employeeName + ".completed", this.completedJobs));
        return this;
    }

    @PlaceholderPattern(pattern = "<employee_completed_jobs>")
    public int getCompletedJobs() {
        return this.completedJobs;
    }

    @PlaceholderPattern(pattern = "<employee_current_job_id>")
    public int getCurrentJob() {
        return this.jobID;
    }

    @Override
    public String toString() {
        return this.id + "";
    }

    public static class EmployeeManager {

        private static final HashMap<Player, Integer> pending = new HashMap<>(50);
        private static final HashSet<Employee> employeeList = new HashSet<>();

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
                Business.BusinessManager.getBusiness(e.getBusiness().getID()).addEmployee(e);
            }

        }

        public static void saveEmployees() {
            FileManager fm = BusinessCore.getInstance().getEmployeeFileManager();
            for(Employee e : employeeList) {
                fm.edit(new FileData()
                        .add(e.getName() + ".id", e.getID())
                        .add(e.getName() + ".UUID", e.getUniqueId().toString())
                        .add(e.getName() + ".business", e.getBusiness().getID())
                        .add(e.getName() + ".job", e.getCurrentJob())
                        .add(e.getName() + ".completed", e.getCompletedJobs()));
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
            return getEmployee(Business.BusinessManager.getBusiness(bid), uuid);
        }

        public static Employee getEmployee(int bid, int id) {
            return getEmployee(Business.BusinessManager.getBusiness(bid), id);
        }

        public static Employee getEmployee(int bid, String name) {
            return getEmployee(Business.BusinessManager.getBusiness(bid), name);
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

        public static Employee getEmployee(Player playerSender) {
            return getEmployee(playerSender.getUniqueId());
        }

        public static boolean isEmployeeFor(Business b, UUID uuid) {
            return getEmployee(b, uuid).getBusiness().equals(b);
        }

        public static boolean isEmployeeFor(Business b, Player playerSender) { return isEmployeeFor(b, playerSender.getUniqueId()); }

        public static boolean isEmployee(Employee e) {
            return employeeList.contains(e);
        }

        public static boolean isEmployee(UUID uuid) {
            return getEmployee(uuid) != null;
        }

        public static boolean isEmployee(int id) {
            return  getEmployee(id) != null;
        }

        public static boolean isEmployee(String name) {
            return  getEmployee(name) != null;
        }

        public static boolean isEmployee(Player playerSender) { return isEmployee(playerSender.getUniqueId());  }



    }
}
