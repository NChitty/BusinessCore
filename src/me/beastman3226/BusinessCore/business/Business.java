package me.beastman3226.BusinessCore.business;

import java.util.Vector;
import me.beastman3226.BusinessCore.job.Job;
import me.beastman3226.BusinessCore.player.Employee;
import org.bukkit.Bukkit;

public class Business {

	public static Business[] businessList = new Business[50];

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
	}

	public boolean addToWorth(double amount) {
		this.worth = this.worth + amount;
		return true;
	}

	public boolean removeFromWorth(double amount) {
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
		while(!businessList[i].ownerName.equals(owner)) {
			if(businessList[i].ownerName.equals(owner)) {
				break;
			}
			i++;
		}
		return businessList[i];
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
            switch(i) {
                case 1: {

                }
                case 2: {

                }
                case 3: {

                }
                case 4: {

                }
                case 5: {

                }
                case 6: {
                    
                }
            }
        }
        return object;
    }
}