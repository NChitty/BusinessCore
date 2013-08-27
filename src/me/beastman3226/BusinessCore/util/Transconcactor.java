package me.beastman3226.BusinessCore.util;

import me.beastman3226.BusinessCore.business.Business;

/**
 *
 * @author beastman3226
 */
public class Transconcactor {

  public static String[] order(Business[] bList) {
    String[] list = null;
    double[] prices = new double[5];
    int i = 0;
    for(Business b : bList) {
      if(i == 0) {
        list[0] = b.getName();
        prices[0] = b.getWorth();
      } else {
         if(isGreaterThan(prices[0], b.getWorth())) {
           double oldFirst = prices[0];
           prices = shiftDown(prices, 0);
           prices[0] = b.getWorth();
         } else if (isLessThan()) {

         }
      }
      i++;
    }

    return list;
  }

  public static double[] shiftDown(double[] array, int numberToShift) {
     int i = 0;
     double[] newArray = new double[array.length];
     double[] bottomArray = new double[array.length - numberToShift];
     for(i = 0; i < numberToShift; i++) {
        newArray[i] = 0.0;
     }
     for(double d : bottomArray) {

     }
     return newArray;
  }

  /**
   * @return Returns true if second is greater than first
   */
  private static boolean isGreaterThan(double first, double second) {
    if(second > first) {
      return true;
    }
    return false;
  }

    private static boolean isLessThan() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
