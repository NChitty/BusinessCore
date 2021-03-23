package me.nchitty.bc.util;

import me.nchitty.bc.BusinessCore;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings {
    private FileConfiguration config;

    public Settings(FileConfiguration config) {
        this.config = config;
    }

    public boolean verboseLogging() {
        return this.config.getBoolean("verbose-logging");
    }

    public boolean isPayRequiredBusiness() {
        return this.config.getBoolean("businesses.require-payment");
    }

    public boolean isPayRequiredJob() {
        return this.config.getBoolean("jobs.require-payment");
    }

    public double getPayRequiredBusiness() {
        return this.config.getDouble("businesses.payment-to-start");
    }

    public double getPayRequiredJob() {
        return this.config.getDouble("job.payment-to-open");
    }

    public double getMinimumJobPayment() {
        return this.config.getDouble("job.payment-minimum");
    }

	public double getStartingBusinessBalance() {
		return this.config.getDouble("businesses.starting-balance");
	}

    public void info() {
        if(verboseLogging()) {
            BusinessCore.getInstance().getLogger().info("(================Business Core Settings================)");
            BusinessCore.getInstance().getLogger().info("Businesses:");
            BusinessCore.getInstance().getLogger().info("\tRequire Payment: " + isPayRequiredBusiness());
            BusinessCore.getInstance().getLogger().info("\tRequired Payment: " + getPayRequiredBusiness());
            BusinessCore.getInstance().getLogger().info("\tStarting Balance: " + getStartingBusinessBalance());
            BusinessCore.getInstance().getLogger().info("Jobs:");
            BusinessCore.getInstance().getLogger().info("\tRequire Payment: " + isPayRequiredJob());
            BusinessCore.getInstance().getLogger().info("\tRequired Payment: " + getPayRequiredJob());
            BusinessCore.getInstance().getLogger().info("\tMinimum Payment: " + getMinimumJobPayment());
        }
    }
}