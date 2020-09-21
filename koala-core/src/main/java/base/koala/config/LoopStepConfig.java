package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.LoopStepCommand;
import com.yeepay.base.koala.command.StepCommand;


public class LoopStepConfig extends CompositeStepConfig {

    @Property(required = true)
    private String time;

    @Override
    public StepCommand createCommand() {
        return new LoopStepCommand(Integer.valueOf(time), this.createChildren());
    }

}
