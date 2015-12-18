package me.beastman3226.bc.player;

import com.evilmidget38.UUIDFetcher;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.bc.BusinessCore.Information;
import me.beastman3226.bc.data.file.EmployeeFileManager;
import me.beastman3226.bc.data.file.FileData;
import me.beastman3226.bc.errors.OpenJobException;
import org.bukkit.Bukkit;

/**
 *
 * @author beastman3226
 */
public class EmployeeManager {

    public static HashMap<String, Integer> pending = new HashMap<String, Integer>(50);

    public static void loadEmployees() {
            for(String key : Information.employeeYml.getKeys(false)) {
                    int id = Information.employeeYml.getInt(key + ".id");
                    String name = Bukkit.getOfflinePlayer(UUID.fromString(Information.employeeYml.getString(key + ".UUID"))).getName();
                    if(name == null) {
                        name = Bukkit.getPlayer(UUID.fromString(Information.employeeYml.getString(key + ".UUID"))).getName();
                    }
                    int bID = Information.employeeYml.getInt(key + ".business");
                    int completed = Information.employeeYml.getInt(key + ".completed");
                    int jID = Information.employeeYml.getInt(key + ".job");
                if(id == 0 || bID == 0 || completed == 0 || jID == 0) {
                    if(Information.debug) {
                        Logger log = Information.log;
                        log.severe("Name: " + name);
                        log.severe("ID: " + id);
                        log.severe("Business: " + bID);
                        log.severe("Completed: " + completed);
                        log.severe("Current Job: " + jID);
                    }
                }
                try {
                    Employee e = new Employee(name, id).setBusiness(bID).setCompletedJobs(completed).startJob(jID);
                    Employee.employeeList.add(e);
                } catch (OpenJobException ex) {
                } catch (NullPointerException npe) {
                    if(Information.debug) {
                        Logger log = Information.log;
                        log.log(Level.SEVERE, "Name: {0}", name);
                        log.log(Level.SEVERE, "ID: {0}", id);
                        log.log(Level.SEVERE, "Business: {0}", bID);
                        log.log(Level.SEVERE, "Completed: {0}", completed);
                        log.log(Level.SEVERE, "Current Job: {0}", jID);
                    }
                        Logger log = Information.log;
                        log.log(Level.SEVERE, "Name: {0}", name);
                        log.log(Level.SEVERE, "ID: {0}", id);
                        log.log(Level.SEVERE, "Business: {0}", bID);
                        log.log(Level.SEVERE, "Completed: {0}", completed);
                        log.log(Level.SEVERE, "Current Job: {0}", jID);
                }
            
        }
    }

    public static Employee addEmployee(String name, int BID) {
        try {
            Employee employee = new Employee(name, 1000 + Employee.employeeList.size() + 1);
            employee.setBusiness(BID);
            Employee.employeeList.add(employee);
            EmployeeFileManager.editConfig(new FileData().add(name + ".UUID", UUIDFetcher.getUUIDOf(name))
                    .add(name + ".id", employee.getID())
                    .add(name + ".business", employee.getBusiness().getID())
                    .add(name + ".completed", 0)
                    .add(name + ".job", -1));
            
            
            return employee;
        } catch (Exception ex) {
            Logger.getLogger(EmployeeManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Employee getEmployee(String name) {
        Employee employee = null;
        for(Employee e : Employee.employeeList) {
            if(e.getName().equalsIgnoreCase(name)) {
                employee = e;
                break;
            }
        }
        return employee;
    }

    public static Employee getEmployee(int id) {
        Employee employee = null;
        for(Employee e : Employee.employeeList) {
            if(e.getID() == id) {
                employee = e;
                break;
            }
        }
        return employee;
    }

    public static boolean isEmployee(String name) {
        return getEmployee(name) != null;
    }
}
