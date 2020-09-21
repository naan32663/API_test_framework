package com.yeepay.base.koala.objectfactory;

import com.yeepay.base.koala.model.KeyValueStore;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.List;

public class ArrayFactory extends InstanceFactory {
    @Override
    protected Object create(Type genericType, Object value) {
        if (value == null) return null;
        Class<?> componentType = getComponentType(genericType);
        if (componentType == null) {
            throw new RuntimeException("目前不支持泛型数组!");
        }
        Object[] values = getValues(value);
        Object bean = Array.newInstance(componentType, values.length);
        for (int i = 0; i < values.length; ++i) {
            Object element = values[i];
            if (element instanceof KeyValueStore) {
                element = ((KeyValueStore) element).getValue();
            }
            Array.set(bean, i, BeanUtils.create(componentType, element));
        }
        return bean;
    }

    private Class getComponentType(Type genericType) {
        if (genericType instanceof Class) {
            return ((Class) genericType).getComponentType();
        } else if (genericType instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) genericType).getGenericComponentType();
            if (componentType instanceof Class) return (Class) componentType;
            return null;
        }
        return null;
    }

    private Object[] getValues(Object value) {
        if (value instanceof String) {
            return ((String) value).split(",");
        }
        return ((List) value).toArray();
    }

    @Override
    protected boolean support(Type type) {
        if (type instanceof Class) {
            if (((Class) type).isArray()) return true;
        }
        return type instanceof GenericArrayType;
    }
}
