package com.yeepay.base.koala.config;

import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.command.TearDownStepCommand;


public class TearDownStepConfig extends CompositeStepConfig{

    @Override
    public StepCommand createCommand() {
        return new TearDownStepCommand(this.createChildren());
    }

}
