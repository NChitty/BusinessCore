package me.beastman3226.bc.data.file;

import me.beastman3226.bc.BusinessCore;

/**
 *
 * @author beastman3226
 */
public class JobFileManager {

    public static void editConfig(FileData data) {
        FileManager.editConfig(BusinessCore.Config.JOB, data);
    }

}