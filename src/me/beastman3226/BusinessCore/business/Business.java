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
/**A business object for handling all of our business related data
 *
 * @author beastman3226
 */
public class Business {
    public static ArrayList<Business> businessList = new ArrayList();
    /**
     * Creates a business from a list of strings implemented in
     * filestore.load();
     * @param stringList The list of strings (values) from BusinessMain.flatfile
     */
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

        /**<p>
         * Our constructor for creating a business object.
         * Note that there is no worth param, this is on the
         * todo list for this plugin, I need to create
         * a constructor for recreating a business object with
         * a worth.
         * </p>
         * @param index The business' id
         * @param name The name of the business
         * @param ownerName The owner's name
         */
	protected Business(int index, String name, String ownerName) {
		this.index = index;
		this.name = name;
		this.ownerName = ownerName;
	}

        /**
         * Gets the id of the current business object
         * @return The index/id of the business object,
         * can be used to retrieve the Business object from
         * the businessList
         */
	public int getIndex() {
		return index;
	}

        /**
         * Gets the name of the business
         * @return The name of the business
         */
	public String getName() {
		return name;
	}

        /**
         * Get's the current wealth of the business
         * @return The worth of the business
         */
	public double getWorth() {
		return worth;
	}

        /**
         * The current way of setting the worth of recreated business;
         * Could be used to change from a purge if stated in config to set
         * to a default worth
         * @param worth Worth to be set to
         */
	protected void setWorth(double worth) {
		this.worth = worth;
	}

        /**
         * Used to deposit money into a business;
         * Takes the current worth and adds to the
         * parameter
         * @param amount The amount to be added
         * @return Returns true if the transaction worth
         */
	protected boolean addToWorth(double amount) {
		this.worth = this.worth + amount;
		return true;
	}

        /**
         * Subtracts money from the business' wealth; returns true
         * if the transaction went through and this.worth is not negative
         * @param amount The amount to subtract
         * @return True if there is enough money to subtract from;
         * If it is false then the will be no transaction, negatives are not allowed
         */
	protected boolean removeFromWorth(double amount) {
		if(amount > this.worth) {
			return false;
		} else {
			this.worth = worth - amount;
                        return true;
		}
	}

        /**
         *
         * @return A list of employees that work for this business
         */
	public Vector<String> getEmployeeList() {
		return employeeList;
	}

        /**
         *
         * @param newList The list to replace the old list with
         */
	protected void setEmployeeList(Vector<String> newList) {
		this.employeeList = newList;
	}

        /**
         * Adds a new employee to the list of employees
         * @param employeeName Name of the employee to add
         */
	protected void addEmployee(String employeeName) {
            this.employeeList.add(employeeName);
            Employee emp = new Employee(Bukkit.getPlayer(employeeName), this);

	}

        /**
         *
         * @return A list of jobs that the business' employees are working on
         */
	protected Vector<Job> getJobList() {
		return jobList;
	}

        /**
         *
         * @param jobList The list to replace the old list with
         */
	protected void setJobList(Vector<Job> jobList) {
		this.jobList = jobList;
	}

        /**
         *
         * @param job The job to add to the job list
         */
	protected void addJob(Job job) {
		this.jobList.add(job);
	}

        /**
         *
         * @return The name of the business owner
         */
	public String getOwnerName() {
		return ownerName;
	}

        /**
         * Cycles through the businessList to find a match of the given name
         * and the name on the books.
         * @param owner The given name
         * @return The business associated with the name
         */
	protected static Business getBusiness(String owner) {
		int i = 0;
		for(Business b : businessList) {
                    if(b.ownerName.equals(owner)) {
                        return b;
                    }
                }
		return null;
	}

    /**
     *
     * @return Number of employees working for the business, its only use is to
     * calculate the payment
     */
    public int getNumberOfEmployees() {
        int number = 0;
        number = this.employeeList.lastIndexOf(this.employeeList.lastElement());
        return number;
    }

    /**
     *
     * @return Creates a string from the main parts of a business:
     * {index},{worth},{name},{ownerName},{jobId}|{jobId}|{etc}
     */
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