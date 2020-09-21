package com.yeepay.base.koala.util;

import org.apache.commons.lang.StringUtils;


public class Util {
    public static Boolean isEmpty(Object value) {
        if (value == null) return true;
        if (StringUtils.isBlank(value.toString())) return true;
        return false;
    }
}
