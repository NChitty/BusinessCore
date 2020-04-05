package me.beastman3226.bc.data.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.*;

import me.beastman3226.bc.BusinessCore;

/**
 *
 * @author beastman3226
 */
public class FileManager {

    protected File file;
    protected FileConfiguration fileConfig;

    public FileManager(String fileName) {
        file = new File(BusinessCore.getInstance().getDataFolder(), fileName);

        if(!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch(IOException ex) {
                BusinessCore.getInstance().getLogger().severe(ex.getMessage());
            }
        }
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getFileConfiguration() {
        return this.fileConfig;
    }

    public void setFile(File f) {
        this.file = f;
    }

    public void setFileConfiguration(FileConfiguration fileConfig) {
        this.fileConfig = fileConfig;
    }

    public void editConfig(FileData data) {
        for(String path : data.getData().keySet()) {
            fileConfig.set(path, data.getData().get(path));
        }
        this.save();
    }
    
    /**
     * Reloads the configuration, simply dumps all information that is in
     * memory and replaces it with all information from file.
     */
    public void reload() {
        try {
            fileConfig.load(file);
        }   catch (FileNotFoundException ex) {
            BusinessCore.getInstance().getLogger().severe(ex.getLocalizedMessage());
        } catch (IOException ex) {
            BusinessCore.getInstance().getLogger().severe(ex.getLocalizedMessage());
        } catch (InvalidConfigurationException ex) {
            BusinessCore.getInstance().getLogger().severe(ex.getLocalizedMessage());
        }
    }

    /**
     * Dumps all the information asscociated with the config into the actual
     * file
     */
    public void save() {
        try {
            fileConfig.save(file);
        } catch (IOException ex) {
            BusinessCore.getInstance().getLogger().severe(ex.getLocalizedMessage());
        }
    }
}