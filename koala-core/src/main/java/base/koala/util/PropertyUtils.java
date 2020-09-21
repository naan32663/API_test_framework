package com.yeepay.base.koala.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.replace;
import static org.apache.commons.lang.StringUtils.trim;


public class PropertyUtils {

    static Map<String, String> configs = new HashMap<String, String>();
    static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);

    static {
        loadProperties("koala.properties");
    }

    private static void loadProperties(String propertiesFileName) {
        try {
            Properties PROPERTIES = new Properties();

            PROPERTIES.load(PropertyUtils.class.getClassLoader().getResourceAsStream(propertiesFileName));
            Set<Map.Entry<Object, Object>> entries = PROPERTIES.entrySet();
            for (Map.Entry entry : entries) {
                String key = entry.getKey() == null ? null : StringUtils.trim(entry.getKey().toString());
                String value = entry.getValue() == null ? null : StringUtils.trim(entry.getValue().toString());
                configs.put(key, value);
            }
        } catch (IOException e) {
            LOGGER.error("load properties file has error,", e);
        }
    }

    public static String getProperty(String name) {
        return configs.get(name);
    }

    public static String getProperty(String name, String defaultValue) {
        String result = configs.get(name);
        if (StringUtils.isBlank(result)) {
            result = defaultValue;
        }
        return result;
    }

    public static String replaceConfigValue(String source) {
        if (StringUtils.isNotBlank(source)) {
            for (Map.Entry<String, String> entry : configs.entrySet()) {
                source = replace(source, "$" + trim(entry.getKey()), entry.getValue());
                source = replace(source, "${" + trim(entry.getKey()) + "}", entry.getValue());
            }
        }
        return source;
    }

}
