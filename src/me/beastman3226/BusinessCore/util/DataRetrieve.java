package me.beastman3226.BusinessCore.util;

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

    public static void shutdown() {

    }

    public static void retrieveBusinesses() {
        try {
            String query = "SELECT * FROM " + bTable;
            BusinessManager.populateBusinesses(s.executeQuery(query));
        } catch (SQLException ex) {
            Logger.getLogger(DataRetrieve.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void retrieveEmployees() {

    }

    public static void retrieveJobs() {

    }
}
