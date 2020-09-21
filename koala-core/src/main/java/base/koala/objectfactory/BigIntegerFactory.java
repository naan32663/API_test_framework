package com.yeepay.base.koala.objectfactory;

import com.yeepay.base.koala.util.Util;

import java.lang.reflect.Type;
import java.math.BigInteger;


public class BigIntegerFactory extends InstanceFactory {
    @Override
    protected Object create(Type type, Object value) {
        if (Util.isEmpty(value)) return null;
        return new BigInteger(value.toString());
    }

    @Override
    protected boolean support(Type type) {
        return type.equals(BigInteger.class);
    }
}
