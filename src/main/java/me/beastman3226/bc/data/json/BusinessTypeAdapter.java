package me.beastman3226.bc.data.json;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import me.beastman3226.bc.business.Business;
import me.beastman3226.bc.business.BusinessManager;

public class BusinessTypeAdapter extends TypeAdapter<Business> {

    @Override
    public void write(JsonWriter out, Business value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.name(value.getName());
        out.beginObject();
        out.name("id");
        out.value(value.getID());
        out.name("name");
        out.value(value.getName());
        out.name("uuid");
        out.value(value.getOwnerUUID());
        out.name("balance");
        out.value(value.getBalance());
        out.endObject();
    }

    @Override
    public Business read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        int id = in.nextInt();
        String name = in.nextString();
        String uuid = in.nextString();
        double balance = in.nextDouble();
        return BusinessManager.createBusiness(new Business.Builder(id).name(name).owner(uuid).balance(balance));
    }

}