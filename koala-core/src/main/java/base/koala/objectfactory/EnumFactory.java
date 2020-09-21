package com.yeepay.base.koala.objectfactory;

import com.yeepay.base.koala.util.Util;

import java.lang.reflect.Type;


public class EnumFactory extends InstanceFactory {
    @Override
    protected Object create(Type type, Object value) {
        if (Util.isEmpty(value)) return null;

        return Enum.valueOf((Class) type, value.toString());
    }

    @Override
    protected boolean support(Type type) {
        if (type instanceof Class) {
            return ((Class) type).isEnum();
        }
        return false;
    }
}
