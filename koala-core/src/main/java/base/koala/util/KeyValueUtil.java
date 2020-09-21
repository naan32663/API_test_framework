package com.yeepay.base.koala.util;

import com.yeepay.base.koala.model.KeyValueStore;

import java.util.List;


public class KeyValueUtil {
    public static String getValueByKey(String key, List<KeyValueStore> processedParams) {
        for (KeyValueStore kvs : processedParams) {
            if (key.equals(kvs.getName())) {
                return (String) kvs.getValue();
            }
        }
        return null;
    }
}
