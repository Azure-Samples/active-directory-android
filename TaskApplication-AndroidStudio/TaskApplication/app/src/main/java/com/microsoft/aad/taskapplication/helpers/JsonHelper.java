package com.microsoft.aad.taskapplication.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.StringReader;

public class JsonHelper {

    static <T> T convertJsonToObject(final String json,
                                     final Class<T> clazz) {
        final Reader reader = new StringReader(json);
        final Gson gson = new GsonBuilder().create();
        return gson.fromJson(reader, clazz);
    }
}
