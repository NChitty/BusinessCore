package me.beastman3226.BusinessCore.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.job.Job;
import me.beastman3226.BusinessCore.player.Employee;
import me.beastman3226.BusinessCore.data.DataUpdate;
import org.bukkit.Bukkit;

public class Business {

	public static ArrayList<Business> businessList = new ArrayList();

    static void createBusiness(List<String> stringList) {
        for(String s : stringList) {
            String b = BusinessMain.flatfile.getString(s);
            String[] args = b.split(",");
            Business business = new Business(Integer.valueOf(args[0]), args[2], args[3]);
            business.setWorth(Double.valueOf(args[1]));
            Logger.getLogger(Business.class.getSimpleName()).log(Level.INFO,"Added: \r\n Business: {0} | Index: {1} | Owner: {2} | Worth: {3}", new Object[]{business.getName(), business.getIndex(), business.getOwnerName(), business.getWorth()});
        }
    }

	private final int index;
	private double worth;
	private final String name;
	private final String ownerName;
	private Vector<String> employeeList = new Vector(100);
	private Vector<Job> jobList = new Vector(100);

	protected Business(int index, String name, String ownerName) {
		this.index = index;
		this.name = name;
		this.ownerName = ownerName;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public double getWorth() {
		return worth;
	}

	protected void setWorth(double worth) {
		this.worth = worth;
	}

	protected boolean addToWorth(double amount) {
		this.worth = this.worth + amount;
		return true;
	}

	protected boolean removeFromWorth(double amount) {
		if(amount > this.worth) {
			return false;
		} else {
			this.worth = worth - amount;
		}
		return false;
	}

	public Vector<String> getEmployeeList() {
		return employeeList;
	}

	protected void setEmployeeList(Vector<String> newList) {
		this.employeeList = newList;
	}

	protected void addEmployee(String employeeName) {
            this.employeeList.add(employeeName);
            Employee emp = new Employee(Bukkit.getPlayer(employeeName), this);

	}

	protected Vector<Job> getJobList() {
		return jobList;
	}

	protected void setJobList(Vector<Job> jobList) {
		this.jobList = jobList;
	}

	protected void addJob(Job job) {
		this.jobList.add(job);
	}


	public String getOwnerName() {
		return ownerName;
	}

	protected static Business getBusiness(String owner) {
		int i = 0;
		for(Business b : businessList) {
                    if(b.ownerName.equals(owner)) {
                        return b;
                    }
                }
		return null;
	}

    public int getNumberOfEmployees() {
        int number = 0;
        number = this.employeeList.lastIndexOf(this.employeeList.lastElement());
        return number;
    }


    @Override
    public String toString() {
        String object = "";
        int i = 0;
        while(i < 6) {
            i++;
            String field = null;
            switch(i) {
                case 1: {
                    field = this.getIndex() + "";
                    object = object + field;
                    break;
                }
                case 2: {
                    field = this.getWorth() + "";
                    object = object + "," + field;
                    break;
                }
                case 3: {
                    field = this.getName();
                    object = object + "," + field;
                    break;
                }
                case 4: {
                    field = this.getOwnerName();
                    object = object + "," + field;
                    field = null;
                    break;
                }
                case 5: {
                    for(Object e : this.getJobList().toArray()) {
                        if(e != null) {
                            Job j = (Job) e;
                            field = field + "|" + j.getId();
                        }
                    }
                    break;
                }
            }
        }
        return object;
    }
}