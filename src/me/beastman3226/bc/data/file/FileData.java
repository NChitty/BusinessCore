package me.beastman3226.bc.data.file;

import java.util.HashMap;

/**
 *
 * @author beastman3226
 */
public class FileData {

    public HashMap<String, Object> data = new HashMap<>();

    public FileData add(String path, Object data) {
        if(data instanceof int[]) {
            String fin = "";
            for(int k : (int[]) data) {
                fin = fin+","+k;
            }
        } else {
            this.data.put(path, data);
        }
        return this;
    }
}
