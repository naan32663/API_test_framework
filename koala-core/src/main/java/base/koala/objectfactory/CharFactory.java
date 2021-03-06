package com.yeepay.base.koala.objectfactory;

import java.lang.reflect.Type;


public class CharFactory extends InstanceFactory {
    @Override
    protected Object create(Type type, Object value) {
        if (value == null) {
            return type.equals(Character.TYPE) ? ' ' : null;
        }
        String s = value.toString();
        return s.charAt(0);
    }

    @Override
    protected boolean support(Type type) {
        return type.equals(Character.class) || type.equals(Character.TYPE);
    }
}
