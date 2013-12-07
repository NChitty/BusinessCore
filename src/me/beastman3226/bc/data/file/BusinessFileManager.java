package me.beastman3226.bc.data.file;

import me.beastman3226.bc.Main;

/**
 *
 * @author beastman3226
 */
public class BusinessFileManager {

    public static void editConfig(FileData data) {
        FileManager.editConfig(Main.Config.BUSINESS, data);
    }

}
