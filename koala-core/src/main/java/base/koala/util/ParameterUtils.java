package com.yeepay.base.koala.util;

import com.yeepay.base.koala.context.Context;
import com.yeepay.base.koala.model.KeyValueStore;
import com.yeepay.base.koala.paramfilter.FilterFactory;
import com.yeepay.base.koala.response.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yeepay.base.koala.util.PropertyUtils.replaceConfigValue;


public class ParameterUtils {
    public static List<KeyValueStore> prepareParameters(List<KeyValueStore> params, Response preResult, Context context) {
        List<KeyValueStore> processedParams = prepareParameters(params, preResult);
        processedParams = replaceValueFromContext(context, processedParams);
        return filter(processedParams);
    }

    private static List<KeyValueStore> prepareParameters(List<KeyValueStore> params, Response preResult) {
        for (KeyValueStore param : params) {
            replaceVariables(param, param.getValue(), preResult == null ? null : preResult.getBody());
        }
        return params;
    }

    private static void replaceVariables(Object entry, Object value, Object preResult) {
        if (value instanceof String) {
            String expression = value.toString();
            expression = replaceConfigValue(expression);
            ReflectionUtils.setFieldValue(entry, "value", ExtractValueUtils.replaceVariables(expression, preResult));
        }
        if (value instanceof Map) {
            Map map = (Map) value;
            for (Object o : map.entrySet()) {
                Map.Entry item = (Map.Entry) o;
                replaceVariables(item, item.getValue(), preResult);
            }
        }
    }

    private static List<KeyValueStore> filter(List<KeyValueStore> params) {
        List<KeyValueStore> result = new ArrayList<KeyValueStore>(params);
        for (KeyValueStore param : result) {
            filter(param, param.getValue());
        }
        return result;
    }

    private static void filter(Object entry, Object value) {
        if (value instanceof String) {
            Object param = FilterFactory.handle(value.toString());
            ReflectionUtils.setFieldValue(entry, "value", param);
        }
        if (value instanceof Map) {
            Map map = (Map) value;
            for (Object o : map.entrySet()) {
                Map.Entry item = (Map.Entry) o;
                filter(item, item.getValue());
            }
        }
    }

    static public List<KeyValueStore> replaceValueFromContext(Context context, List<KeyValueStore> processedParams) {
        List<KeyValueStore> resultList = new ArrayList<KeyValueStore>(processedParams);
        for (KeyValueStore kvs : resultList) {
            Object value = kvs.getValue();
            Object result = replaceValue(value, context);
            kvs.setValue(result);
        }
        return resultList;
    }

    private static Object replaceValue(Object value, Context context) {
        if (null == value) {
            return value;
        }
        if (value instanceof Map) {
            return replace((Map) value, context);
        } else if (value instanceof List) {
            return replace((List) value, context);
        } else if (value.getClass().isArray()) {
            return replace(changeObjectToList(value), context).toArray();
        } else if (value instanceof KeyValueStore) {
            KeyValueStore newValue = (KeyValueStore) value;
            newValue.setValue(replaceValue(newValue.getValue(), context));
            return newValue;
        } else if (value instanceof String) {
            if (context == null) return value;
            return context.replace(value.toString());
        }
        return value;
    }

    private static List<Object> changeObjectToList(Object object) {
        List<Object> result = new ArrayList<Object>();
        if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            for (int index = 0; index < length; index++) {
                result.add(Array.get(object, index));
            }
        } else {
            result.add(object);
        }
        return result;
    }

    private static Object replace(Map<Object, Object> map, Context context) {
        Map<Object, Object> result = new HashMap<Object, Object>(map);
        for (Map.Entry entry : result.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            Object o = replaceValue(value, context);
            result.put(key, o);
        }
        return result;
    }

    private static List<Object> replace(List<Object> list, Context context) {
        List<Object> resultList = new ArrayList<Object>(list);
        for (int index = 0; index < resultList.size(); index++) {
            Object object = resultList.get(index);
            if (!(object instanceof String)) {
                Object result = replaceValue(object, context);
                resultList.set(index, result);
            }
        }
        return resultList;
    }
}
