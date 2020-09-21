package com.yeepay.base.koala.model;

import com.yeepay.base.koala.Koala;

import java.util.HashMap;
import java.util.Map;


public class Environment {

    public static final String OPERATION = "OPERATION";

    static final Map<String,Object> environments = new HashMap<String, Object>();

    public static void addEnvironment(String key, Object value) {
        environments.put(key, value);
    }

    public static Object getEnvironment(String key) {
        return environments.get(key);
    }

    public static void initEnvironment(Class<?> clazz){
        Koala.Options options = clazz.getAnnotation(Koala.Options.class);
        addEnvironment(OPERATION, options.operation().valueOf());
    }

}
