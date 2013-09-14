package me.beastman3226.BusinessCore.business;

import java.util.ArrayList;
import java.util.Vector;
import me.beastman3226.BusinessCore.job.Job;
import me.beastman3226.BusinessCore.player.Employee;
import me.beastman3226.BusinessCore.util.DataUpdate;
import org.bukkit.Bukkit;

public class Business {

	public static ArrayList<Business> businessList = new ArrayList();

	private final int index;
	private double worth;
	private final String name;
	private final String ownerName;
	private Vector<String> employeeList = new Vector(100);
	private Vector<Job> jobList = new Vector(100);

	public Business(int index, String name, String ownerName) {
		this.index = index;
		this.name = name;
		this.ownerName = ownerName;
	}

        public Business(String represent) {
            String[] fields = represent.split(",");
            int i = 0;
            this.index = Integer.parseInt(fields[0]);
            this.worth = Double.parseDouble(fields[1]);
            this.name = fields[2];
            this.ownerName = fields[3];
            if(fields.length == 5) {
                String[] jobs = fields[4].split("|");
                for(String job : jobs) {
                    this.addJob(Job.getJob(Integer.parseInt(job)));
                }
            }

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

	public void setWorth(double worth) {
		this.worth = worth;
                DataUpdate.setBusinessWorth(this.ownerName, worth);
	}

	public boolean addToWorth(double amount) {
		this.worth = this.worth + amount;
                DataUpdate.setBusinessWorth(this.ownerName, worth);
		return true;
	}

	public boolean removeFromWorth(double amount) {
		if(amount > this.worth) {
			return false;
		} else {
			this.worth = worth - amount;
                        DataUpdate.setBusinessWorth(this.ownerName, worth);
		}
		return false;
	}

	public Vector<String> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(Vector<String> newList) {
		this.employeeList = newList;
	}

	public void addEmployee(String employeeName) {
            this.employeeList.add(employeeName);
            Employee emp = new Employee(Bukkit.getPlayer(employeeName), this);

	}

	public Vector<Job> getJobList() {
		return jobList;
	}

	public void setJobList(Vector<Job> jobList) {
		this.jobList = jobList;
	}

	public void addJob(Job job) {
		this.jobList.add(job);
	}


	public String getOwnerName() {
		return ownerName;
	}

	public static Business getBusiness(String owner) {
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