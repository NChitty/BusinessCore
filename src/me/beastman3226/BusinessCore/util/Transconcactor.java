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
           prices = shiftDown(prices, 1);
           prices[0] = b.getWorth();
         } else if (isLessThan(prices[0], b.getWorth())) {
             for(int j = 0; j < prices.length; j++) {
                 if(!isLessThan(prices[j], b.getWorth())) {
                     prices = shiftDown(prices, 1, j);
                     break;
                 }
             }
         }
      }
      i++;
    }

    return list;
  }

  public static double[] shiftDown(double[] array, int numberToShift) {
     double[] newArray = new double[array.length];
     double[] bottom = new double[array.length - numberToShift];
     for(int i = 0; i < numberToShift; i++) {
         bottom[i] = array[i];
     }
     int i = 0;
     for(int j = numberToShift; j < newArray.length; j++) {
         newArray[j] = bottom[i];
         i++;
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

    private static boolean isLessThan(double par, double worth) {
       if(worth > par) {
           return true;
       }
        return false;
    }

    private static double[] shiftDown(double[] prices, int numberToShift, int startingAt) {
        double[] newArray = new double[prices.length];
        double[] bottom = new double[prices.length - numberToShift];
        int i = 0;
        for(int k = startingAt; k < numberToShift; k++) {
            bottom[i] = prices[k];
            i++;
        }
        for(int h = 0; h < newArray.length; h++) {
            if(h != startingAt) {

            }
        }
        return newArray;
    }

}
