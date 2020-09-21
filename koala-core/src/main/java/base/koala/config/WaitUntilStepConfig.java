package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.command.WaitUntilStepCommand;


public class WaitUntilStepConfig extends CompositeStepConfig {

    @Property(required = true)
    private String timeout;

    @Override
    public StepCommand createCommand() {
        return new WaitUntilStepCommand(Long.valueOf(timeout), this.createChildren());
    }
}
