package me.beastman3226.BusinessCore.job;

import me.beastman3226.BusinessCore.player.Employee;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author beastman3226
 */
public class Job {

    private final String description;
    private final double payment;
    private final Location loc;
    private final String issuer;
    private final int id;
    private boolean completed = false;
    private Employee worker;
    private String businessName;
    public static Job[] jobList = new Job[600];

    public Job(String description, double payment, Location loc, String issuer, int id) {
        this.description = description;
        this.payment = payment;
        this.loc = loc;
        this.issuer = issuer;
        this.id = id;
    }

    /**
     * This constructor creates a new job object from a string representation
     * of the object
     * @param stringRep The string representation
     */
    private Job(String stringRep) {
        String[] fields = stringRep.split(",");
        this.description = fields[0];
        this.payment = Double.parseDouble(fields[1]);
        String[] coords = fields[2].split("|");
        this.loc = new Location(Bukkit.getServer().getWorlds().get(0), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]));
        this.issuer = fields[3];
        this.id = Integer.parseInt(fields[4]);
        if(!fields[5].equals("no_employee_yet")) {
        } else {
            Employee e = Employee.getEmployee(fields[6]);
            this.setWorker(e);
        }
    }

    public String getDescription() {
        return this.description;
    }

    public double getPayment() {
        return this.payment;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public Player getIssuerAsPlayer() {
        return Bukkit.getPlayer(issuer);
    }

    public Location getLocation() {
        return this.loc;
    }

    public int getId() {
        return this.id;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void complete() {
        this.worker.addCompletedJob();
        this.completed = true;
    }

    public void setWorker(Employee emp) {
        this.worker = emp;
        businessName = emp.getBusiness().getName();
    }

    public Employee getWorker() {
        return this.worker;
    }

    public Job getJob(int id) {
        Job j = null;
        for(Job jo : Job.jobList) {
            if(jo != null) {
                if(jo.getId() == id) {
                    j = jo;
                    break;
                }
            }
        }
        return j;
    }

    @Override
    public String toString() {
        String object = "";
        int i = 0;
        while(i < 8) {
            i++;
            String field = null;
            switch(i) {
                case 1: {
                    field = this.getDescription();
                    object = object + field;
                }
                case 2: {
                    field = this.getPayment() + "";
                    object = object + "," + field;
                }
                case 3: {
                    field = this.getLocation().getX()+ "|" + this.getLocation().getBlockY()+ "|" + this.getLocation().getBlockZ();
                    object = object + "," + field;
                }
                case 4: {
                    field = this.getIssuer();
                    object = object + "," + field;
                }
                case 5: {
                    field = this.getId() + "";
                    object = object + "," + field;
                }
                case 6: {
                    field = this.isCompleted() + "";
                    object = object + "," + field;
                }
                case 7: {
                    if(this.getWorker() != null) {
                        field = this.getWorker().getEmployeeName();
                        object = object + "," + field;
                    } else {
                        field = "no_employee_yet";
                        object = object + "," + field;
                    }
                }
                case 8: {
                    field = this.businessName;
                    object = object + "," + field;
                }
            }
        }
        return object;
    }

    public static Job fromString(String stringRep) {
        return new Job(stringRep);
    }
}
