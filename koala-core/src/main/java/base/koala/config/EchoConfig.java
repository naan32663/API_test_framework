package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.ConfigElement;
import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.EchoCommand;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.model.KeyValueStore;

import java.util.Arrays;


@ConfigElement(defaultProperty = "value")
public class EchoConfig extends StepConfig {

    @Property
    private String value;

    @Override
    public StepCommand createCommand() {
        return new EchoCommand(Arrays.asList(new KeyValueStore("value", value)));
    }
}
