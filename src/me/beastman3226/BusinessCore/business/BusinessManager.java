package me.beastman3226.BusinessCore.business;

/**
 *
 * @author beastman3226
 */
public class BusinessManager {

    public static Business createBusiness(String name, String owner) {
        Business b = null;
        int i = 0;
        while (i < Business.businessList.length) {
            if(Business.businessList[i] == null) {
                b = new Business(i, name, owner);
                Business.businessList[i] = b;
                break;
            }
            i++;
        }
        return  b;
    }

    public static boolean deleteBusiness() {
        return false;
    }
}
