package me.beastman3226.BusinessCore.business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.config.Configuration;
import me.beastman3226.BusinessCore.data.DataStore;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {

    public static Business createBusiness(String name, String owner) {
        Business b = null;
        Business finalB = Business.businessList.get(Business.businessList.size());
        int index = finalB.getIndex() + 1;
        b = new Business(index, name, owner);
        if(BusinessMain.config.getBoolean("db.enabled")) {
            DataStore.addBusiness(name, index, owner, 0.0);
        } else {
            BusinessMain.flatfile.set(name + ".owner", owner);
            BusinessMain.flatfile.set(name + ".id", index);
            BusinessMain.flatfile.set(name + ".worth", 0.0);
        }
        return  b;
    }

    public static void deleteBusiness(String name, BusinessMain plugin) {
        Business.businessList.remove(Business.getBusiness(name));
        if(BusinessMain.config.getBoolean("db.enabled")) {
            DataStore.deleteBusiness(name);
        } else {
            BusinessMain.flatfile.set(name, null);
        }
    }

    public static String[] listBusinesses() {
        String[] businessList = null;
        String bList = "";
        for(int i = 0; i < Business.businessList.size(); i++) {
            if(Business.businessList != null) {
                if(i == 0) {
                    bList = bList + Business.businessList.get(i).getName();
                 } else {
                    bList = bList + "," + Business.businessList.get(i).getName();
                 }
            }
        }
        businessList = bList.split(",");
        return businessList;
    }

    public static void populateBusinesses(ResultSet executeQuery) {
        try {
            while(executeQuery.next() == true) {
               Business b = createBusiness(executeQuery.getString("BusinessName"), executeQuery.getString("BusinessOwner"), executeQuery.getInt("BusinessID"));
               b.setWorth(executeQuery.getDouble("BusinessWorth"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BusinessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void populateBusinesses(Configuration config) {

    }

    public static void payOut(double calculate) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void withdraw(String name, double parseDouble) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Business getBusiness(String ownername) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Vector<String> getEmployeeList(String ownername) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void deposit(String name, double parseDouble) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void addEmployee(Business get, String name) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static Business createBusiness(String string, String string0, int aInt) {
        Business b= new Business(aInt, string, string0);
        Business.businessList.add(aInt, b);
        return b;
    }
}
