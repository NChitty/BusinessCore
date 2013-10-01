package me.beastman3226.BusinessCore.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.business.Business;
import me.beastman3226.BusinessCore.business.BusinessManager;

/**
 *
 * @author beastman3226
 */
public class DataRetrieve extends Data {

    public static String retrieveBusinesses = "SELECT * FROM 'business'";
    public static String retrieveEmployees = "SELECT * FROM 'employee'";
    public static String retrieveJobs = "SELECT * FROM 'job'";
}
