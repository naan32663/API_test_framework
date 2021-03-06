package com.yeepay.base.koala.objectfactory;

import java.lang.reflect.Type;

public class LongFactory extends InstanceFactory {

    @Override
    protected Object create(Type type, Object value) {
        if (value == null) {
            return type.equals(Long.TYPE) ? 0L : null;
        }
        return Long.valueOf(value.toString());
    }

    @Override
    protected boolean support(Type type) {
        return type.equals(Long.class) || type.equals(Long.TYPE);
    }
}
