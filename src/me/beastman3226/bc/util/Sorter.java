package me.beastman3226.bc.util;

import java.util.ArrayList;
import me.beastman3226.bc.business.Business;

/**
 *
 * @author beastman3226
 */
public class Sorter {

    public static ArrayList<Business> sort() {
        ArrayList<Business> list = new ArrayList<>();
        for(Business b : Business.businessList) {
            int i = 0;
            if(list.get(i) == null || list.get(i).getBalance() < b.getBalance()) {
                list.set(i, b);
            } else {
                while(b.getBalance() < list.get(i).getBalance()) {
                    i++;
                }
                list.set(i, b);
            }
        }
        return list;
    }

}
