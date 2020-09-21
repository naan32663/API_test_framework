package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.command.WaitStepCommand;


public class WaitStepConfig extends StepConfig {

    @Property(value = "time", required = true)
    String waitTime;

    @Override
    public StepCommand createCommand() {
        return new WaitStepCommand(Long.valueOf(waitTime));
    }

}
