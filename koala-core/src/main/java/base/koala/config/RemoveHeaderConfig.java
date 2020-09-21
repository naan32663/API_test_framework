package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.RemoveHeaderCommand;
import com.yeepay.base.koala.command.StepCommand;


public class RemoveHeaderConfig extends StepConfig {

    @Property(required = true)
    String header;

    @Override
    public StepCommand createCommand() {
        return new RemoveHeaderCommand(header);
    }
}
