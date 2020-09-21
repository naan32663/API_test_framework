package com.yeepay.base.koala.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ReaderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderUtil.class);

    public static String readeAsString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("read input stream has error.", e);
        }
        return sb.toString();
    }

}
