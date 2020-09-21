package com.yeepay.base.koala.objectfactory;

import com.yeepay.base.koala.util.Util;

import java.lang.reflect.Type;
import java.math.BigDecimal;


public class BigDecimalFactory extends InstanceFactory {
    @Override
    protected Object create(Type type, Object value) {
        if (Util.isEmpty(value)) return null;

        return new BigDecimal(value.toString());
    }

    @Override
    protected boolean support(Type type) {
        return type.equals(BigDecimal.class);
    }
}
