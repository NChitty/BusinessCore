package me.beastman3226.bc.data.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import me.beastman3226.bc.business.Business;

public class JsonManager<T> {

    private File file;
    private Gson gson; 

    public JsonManager(File file, Class<T> type) {
        this.file = file;
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            if (!file.exists())
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        if(type.equals(Business.class))
            gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(type, new BusinessTypeAdapter()).create();
    }

    public void edit() {
        
    }
}