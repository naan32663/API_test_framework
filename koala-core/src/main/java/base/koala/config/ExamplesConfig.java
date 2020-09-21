package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.ConfigElement;
import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.ExamplesCommand;
import com.yeepay.base.koala.command.StepCommand;

import java.io.IOException;


@ConfigElement(defaultProperty = ExamplesConfig.BODY_TAG_NAME)
public class ExamplesConfig extends StepConfig {
    public static final String BODY_TAG_NAME = "body";

    @Property
    private String body;

    @Override
    public StepCommand createCommand() {
        try {
            return new ExamplesCommand(body);
        } catch (IOException e) {
            return null;
        }
    }
}
