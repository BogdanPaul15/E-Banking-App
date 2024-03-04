package org.poo.cb;

import com.google.gson.Gson;

public class JsonHandler {
    // Convert any generic data type to Json format
    public static <T> String convertToJson(T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
