package com.yeepay.base.koala.util;

import com.yeepay.base.koala.model.KeyValueStore;
import com.yeepay.base.koala.transport.http.HttpService;

import java.util.ArrayList;
import java.util.List;

public class TraceUtil {

    private static final String CASE_TRACE_URL = "http://l-qtest25.beta.cn1.yeepay.com:8888/casetrace/add.do";

    private static final String APPNAME;

    static {
        String currentDir = System.getProperty("user.dir");
        APPNAME = currentDir.substring(currentDir.lastIndexOf("/") + 1);
    }

    public static void trace(String message) {
        List<KeyValueStore> params = new ArrayList<KeyValueStore>();
        params.add(new KeyValueStore("filePath", APPNAME));
        params.add(new KeyValueStore("message", message));
        HttpService.entityRequest(CASE_TRACE_URL, "POST", params);
    }

}