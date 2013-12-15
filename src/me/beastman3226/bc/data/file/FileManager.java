package me.beastman3226.bc.data.file;

import me.beastman3226.bc.BusinessCore.Config;
import me.beastman3226.bc.BusinessCore.FileFunctions;
import me.beastman3226.bc.BusinessCore.Information;

/**
 *
 * @author beastman3226
 */
public class FileManager {

    public static void editConfig(Config type, FileData data) {
        switch(type) {
            case BUSINESS:
                FileFunctions.reload(type);
                for(String path : data.data.keySet()) {
                    Information.businessYml.set(path, data.data.get(path));
                }
                FileFunctions.save(type);
                break;
            case EMPLOYEE:
                FileFunctions.reload(type);
                for(String path : data.data.keySet()) {
                    Information.employeeYml.set(path, data.data.get(path));
                }
                FileFunctions.save(type);
                break;
            case JOB:
                FileFunctions.reload(type);
                for(String path : data.data.keySet()) {
                    Information.jobYml.set(path, data.data.get(path));
                }
                FileFunctions.save(type);
                break;
        }
    }


}
