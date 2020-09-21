package com.yeepay.base.koala.util;

import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.model.KeyValueStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloneUtil {

    public static List<KeyValueStore> cloneKeyValueStore(List<KeyValueStore> parameters) {
        if (parameters == null) {
            return null;
        }
        return (List<KeyValueStore>) clone(parameters);
    }

    private static Object clone(Object value) {
        if (value instanceof Map) {
            Map mapValue = (Map) value;

            Map newMap = new HashMap();
            for (Object o : mapValue.keySet()) {
                newMap.put(o, clone(mapValue.get(o)));
            }

            return newMap;
        } else if (value instanceof List) {
            List parameters = (List) value;
            List result = new ArrayList();
            for (Object item : parameters) {
                result.add(clone(item));
            }
            return result;
        } else if (value instanceof KeyValueStore) {
            KeyValueStore old = (KeyValueStore) value;
            return new KeyValueStore(old.getName(), clone(old.getValue()));
        }
        return value;
    }

    public static List<StepCommand> cloneStepCommand(List<StepCommand> children) {
        List<StepCommand> result = new ArrayList<StepCommand>();
        if (children != null) {
            for (StepCommand sc : children) {
                result.add(sc.cloneCommand());
            }
        }
        return result;
    }

}
