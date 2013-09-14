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

    private final String description; //done
    private final double payment; //done
    private final Location loc; //done
    private final String issuer; //done
    private final int id; //done
    private boolean completed = false;
    private Employee worker; //done
    private String businessName;
    public static Job[] jobList = new Job[600];

    public Job(String description, double payment, Location loc, String issuer, int id) {
        this.description = description;
        this.payment = payment;
        this.loc = loc;
        this.issuer = issuer;
        this.id = id;
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

    public static Job getJob(int id) {
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

}
