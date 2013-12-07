package me.beastman3226.bc.data.file;

import me.beastman3226.bc.Main;

/**
 *
 * @author beastman3226
 */
public class JobFileManager {

    public static void editConfig(FileData data) {
        FileManager.editConfig(Main.Config.JOB, data);
    }

}
