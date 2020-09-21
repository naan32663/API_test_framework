package com.yeepay.base.koala.config;

import com.yeepay.base.koala.command.PrintStepCommand;
import com.yeepay.base.koala.command.StepCommand;


public class PrintStepConfig extends StepConfig {

    @Override
    public StepCommand createCommand() {
        return new PrintStepCommand();
    }

}
