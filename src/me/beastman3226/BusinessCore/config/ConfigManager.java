package me.beastman3226.BusinessCore.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.beastman3226.BusinessCore.BusinessMain;
import me.beastman3226.BusinessCore.util.Email;
import me.beastman3226.BusinessCore.util.Email.Provider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Basic file configuration class.
 */
public class ConfigManager {



    private JavaPlugin plugin;

    /*
    * Manage custom configurations and files
    */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
    * Get new configuration with header
    * @param filePath - Path to file
    * @return  New Config
    */
    public Configuration getNewConfig(String filePath, String[] header) throws FileNotFoundException {

        File file = this.getConfigFile(filePath);

        if(file == null || !file.exists()) {
            this.prepareFile(filePath);
            if(header != null && header.length != 0) {
                this.setHeader(file, header);
            }

        }
        if(file == null || !file.exists()) {
            file = new File(plugin.getDataFolder() + File.separator + filePath);
        }

        Configuration config = null;
        try {
            config = new Configuration(new FileInputStream(file), file, this.getCommentsNum(file), plugin);
        } catch (FileNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "ConfigurationManager failed", ex.getLocalizedMessage());
        } finally {
            if(config == null) {
                config = new Configuration(new FileInputStream(file), file, this.getCommentsNum(file), plugin);
            }
        }
        return config;

    }

    /*
    * Get new configuration
    * @param filePath - Path to file
    * @return - New SimpleConfig
    */
    public Configuration getNewConfig(String filePath) throws FileNotFoundException {
        return this.getNewConfig(filePath, null);
    }

    /*
    * Get configuration file from string
    * @param file - File path
    * @return - New file object
    */
    private File getConfigFile(String file) {

        if(file.isEmpty() || file == null) {
            return null;
        }

        File configFile;

        if(file.contains("/")) {

            if(file.startsWith("/")) {
                configFile = new File(plugin.getDataFolder() + file.replace("/", File.separator));
            } else {
                configFile = new File(plugin.getDataFolder() + File.separator + file.replace("/", File.separator));
            }

        } else {
            configFile = new File(plugin.getDataFolder(), file);
        }

        return configFile;

    }

    /*
    * Create new file for config and copy resource into it
    * @param file - Path to file
    * @param resource - Resource to copy
    */
    public void prepareFile(String filePath, String resource) {

        File file = this.getConfigFile(filePath);

        if(file.exists()) {
            return;
        }

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            if(!resource.isEmpty() && resource != null) {
                this.copyResource(plugin.getResource(resource), file);
            } else {
                this.copyResource(plugin.getResource(resource), file);
            }

        } catch (IOException e) {
        }

    }

    /*
    * Create new file for config without resource
    * @param file - File to create
    */
    public void prepareFile(String filePath) {
        this.prepareFile(filePath, filePath);
    }

    /*
    * Adds header block to config
    * @param file - Config file
    * @param header - Header lines
    */
    public void setHeader(File file, String[] header) {

        if(!file.exists()) {
            return;
        }

        try {
            String currentLine;
            StringBuilder config = new StringBuilder("");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while((currentLine = reader.readLine()) != null) {
                    config.append(currentLine).append("\r\n");
                }
            }
            config.append("# +----------------------------------------------------+ #\r\n");

            for(String line : header) {

                if(line.length() > 50) {
                    continue;
                }

                int lenght = (50 - line.length()) / 2;
                StringBuilder finalLine = new StringBuilder(line);

                for(int i = 0; i < lenght; i++) {
                    finalLine.append(" ");
                    finalLine.reverse();
                    finalLine.append(" ");
                    finalLine.reverse();
                }

                if(line.length() % 2 != 0) {
                    finalLine.append(" ");
                }

                config.append("# < ").append(finalLine.toString()).append(" > #\n");

        }

        config.append("# +----------------------------------------------------+ #");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(this.prepareConfigString(config.toString()));
                writer.flush();
            }

        } catch (IOException e) {
        }

    }

    /*
    * Read file and make comments SnakeYAML friendly
    * @param filePath - Path to file
    * @return - File as Input Stream
    */
    public InputStream getConfigContent(File file) {

        if(!file.exists()) {
            return null;
        }

        try {
            int commentNum = 0;

            String addLine;
            String currentLine;
            String pluginName = this.getPluginName();

            StringBuilder whole = new StringBuilder("");
            InputStream configStream;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while((currentLine = reader.readLine()) != null) {

                    if(currentLine.startsWith("#")) {
                        addLine = currentLine.replaceFirst("#", pluginName + "_COMMENT_" + commentNum + ":");
                        whole.append(addLine).append("\r\n");
                        commentNum++;

                    } else {
                        whole.append(currentLine).append("\r\n");
                    }

                }
                String config = whole.toString();
                configStream = new ByteArrayInputStream(config.getBytes(Charset.forName("UTF-8")));
            }
            return configStream;

        } catch (IOException e) {
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "ConfigurationManager failed", e.getLocalizedMessage());
            return null;
        }

    }

    /*
    * Get comments from file
    * @param file - File
    * @return - Comments number
    */
    private int getCommentsNum(File file) {

        if(!file.exists()) {
            return 0;
        }

        try {
            int comments = 0;
            String currentLine;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while((currentLine = reader.readLine()) != null) {

                    if(currentLine.startsWith("#")) {
                        comments++;
                    }
                }
            }
        return comments;

        } catch (IOException e) {
            BusinessMain.email.sendEmail("server.errors.minecraft@gmail.com", "ConfigurationManager failed", e.getLocalizedMessage());
            return 0;
        }

    }

    /*
    * Get config content from file
    * @param filePath - Path to file
    * @return - readied file
    */
    public InputStream getConfigContent(String filePath) {
        return this.getConfigContent(this.getConfigFile(filePath));
    }


    private String prepareConfigString(String configString) {

        int lastLine = 0;
        int headerLine = 0;

        String[] lines = configString.split("\n");
        StringBuilder config = new StringBuilder("");

        for(String line : lines) {

            if(line.startsWith(this.getPluginName() + "_COMMENT")) {
                String comment = "#" + line.trim().substring(line.indexOf(":") + 1);

                if(comment.startsWith("# +-")) {

                    /*
                    * If header line = 0 then it is
                    * header start, if it's equal
                    * to 1 it's the end of header
                    */

                    if(headerLine == 0) {
                        config.append(comment).append("\r\n");

                        lastLine = 0;
                        headerLine = 1;

                    } else if(headerLine == 1) {
                        config.append(comment).append("\r\n");

                        lastLine = 0;
                        headerLine = 0;

                    }

                } else {

                    /*
                    * Last line = 0 - Comment
                    * Last line = 1 - Normal path
                    */

                    String normalComment;

                    if(comment.startsWith("# ' ")) {
                        normalComment = comment.substring(0, comment.length() - 1).replaceFirst("# ' ", "# ");
                    } else {
                        normalComment = comment;
                    }

                    if(lastLine == 0) {
                        config.append(normalComment).append("\n");
                    } else if(lastLine == 1) {
                        config.append("\r\n").append(normalComment).append("\n");
                    }

                    lastLine = 0;

                }

            } else {
                config.append(line).append("\r\n");
                lastLine = 1;
            }

        }

   return config.toString();

    }


    /*
    * Saves configuration to file
    * @param configString - Config string
    * @param file - Config file
    */
    public void saveConfig(String configString, File file) {
        String configuration = this.prepareConfigString(configString);

        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(configuration);
                writer.flush();
            }

        } catch (IOException e) {
        }

    }

    public String getPluginName() {
        return plugin.getDescription().getName();
    }

    /*
    * Copy resource from Input Stream to file
    * @param resource - Resource from .jar
    * @param file - File to write
    */
    private void copyResource(InputStream resource, File file) {

        try {
            try (OutputStream out = new FileOutputStream(file)) {
                int lenght;
                byte[] buf = new byte[1024];

                while((lenght = resource.read(buf)) > 0){
                    out.write(buf, 0, lenght);
                }
            }
            resource.close();

        } catch (Exception e) {
        }

    }

}