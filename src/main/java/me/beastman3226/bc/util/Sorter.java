package me.beastman3226.bc.util;

import java.util.ArrayList;
import me.beastman3226.bc.business.Business;

/**
 *
 * @author beastman3226
 */
public class Sorter {

    public static ArrayList<Business> sort() throws ArrayIndexOutOfBoundsException {
        ArrayList<Business> list = new ArrayList<Business>();
        for(Business b : Business.businessList) {
            int i = 0;
            if(list.isEmpty()) {
                list.add(b);
            } else {
                while(b.getBalance() < list.get(i).getBalance()) {
                    i++;
                }
                i++;
                list.set(i, b);
            }
        }
        return list;
    }

}
