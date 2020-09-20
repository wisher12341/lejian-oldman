package com.lejian.oldman.utils;

import com.google.gson.Gson;

public class SerializationUtils {

    private static final Gson GSON=new Gson();

    public static <T> T gsonSerialize(String str,Class<T> clazz){
        return GSON.fromJson(str,clazz);
    }
}
