package me.nchitty.bc.util;

import me.nchitty.bc.business.Business;
import me.nchitty.bc.player.EmployeeManager;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class BusinessCoreExpansion extends PlaceholderExpansion {

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        switch (identifier) {
            case "business_name":
                if (EmployeeManager.isEmployee(player.getUniqueId()))
                    return EmployeeManager.getEmployee(player.getUniqueId()).getBusiness().getName();
                else if (Business.BusinessManager.isOwner(player.getUniqueId()))
                    return Business.BusinessManager.getBusiness(player.getUniqueId()).getName();
                break;
            case "business_balance":
                if (EmployeeManager.isEmployee(player.getUniqueId()))
                    return EmployeeManager.getEmployee(player.getUniqueId()).getBusiness().getBalance() + "";
                else if (Business.BusinessManager.isOwner(player.getUniqueId()))
                    return Business.BusinessManager.getBusiness(player.getUniqueId()).getBalance() + "";
        }
        return null;
    }

    @Override
    public String getAuthor() {
        return "Desireaux";
    }

    @Override
    public String getIdentifier() {
        return "businesscore";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

}