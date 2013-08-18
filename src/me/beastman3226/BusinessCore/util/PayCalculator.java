package me.beastman3226.BusinessCore.util;

import me.beastman3226.BusinessCore.Business.Business;

/**
 *
 * @author beastman3226
 */
public class PayCalculator {

    public static double calculate(Business b, String employee) {
        double pay = b.getNumberOfEmployees()/b.getWorth();
        return pay;
    }

}
