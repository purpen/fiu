package com.taihuoniao.fineix.utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.beans.HttpResponse;

import java.io.Reader;
import java.util.List;

public class JsonUtil {
    private static Gson gson;

    public static void init() {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public static JsonArray getJsonArray(Reader reader) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(reader);
        return element.getAsJsonArray();
    }

    public static String list2Json(List list) throws JsonSyntaxException{
        return gson.toJson(list);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(JsonElement element, Class<T> clazz) {
        return gson.fromJson(element, clazz);
    }

    public static <T> T fromJson(String json, TypeToken<HttpResponse<T>> token) throws JsonSyntaxException {
        return ((HttpResponse<T>) gson.fromJson(json, token.getType())).getData();
    }

    public static <T> HttpResponse<T> json2Bean(String json, TypeToken<HttpResponse<T>> token) throws JsonSyntaxException {
        return (HttpResponse<T>) gson.fromJson(json, token.getType());
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}

