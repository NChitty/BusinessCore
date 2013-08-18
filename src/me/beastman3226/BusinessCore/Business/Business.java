package me.beastman3226.BusinessCore.Business;

import me.beastman3226.BusinessCore.Jobs.Job;

public class Business {

	public static Business[] businessList = new Business[50];

	private final int index;
	private double worth;
	private final String name;
	private final String ownerName;
	private String[] employeeList = new String[100];
	private Job[] jobList = new Job[100];

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

	public String[] getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(String[] employeeList) {
		this.employeeList = employeeList;
	}

	public void addEmployee(String employeeName) {
		this.employeeList[this.getFreeIndex("employeelist")] = employeeName;
	}

	public Job[] getJobList() {
		return jobList;
	}

	public void setJobList(Job[] jobList) {
		this.jobList = jobList;
	}

	public void addJob(Job job) {
		this.jobList[getFreeIndex("joblist")] = job;
	}

	public int getFreeIndex(String array) {
		int index = 0;
		switch (array.toLowerCase()) {
			case "employeelist":
				while(index < this.employeeList.length) {
					if(this.employeeList[index] == null) {
						break;
					} else {
						index++;
					}
				}
			case "joblist":
				while(index < this.jobList.length) {
					if(this.jobList[index] == null) {
						break;
					} else {
						index++;
					}
				}
		}
		return index;
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

}
