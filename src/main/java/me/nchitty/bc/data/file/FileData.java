package me.nchitty.bc.data.file;

import java.util.HashMap;

/**
 *
 * @author beastman3226
 */
public class FileData {

    private HashMap<String, Object> data = new HashMap<String, Object>();

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

    public HashMap<String, Object> getData() {
        return this.data;
    }
}
